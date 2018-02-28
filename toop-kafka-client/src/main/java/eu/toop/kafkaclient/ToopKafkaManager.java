/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.kafkaclient;

import java.util.Properties;
import java.util.concurrent.Future;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.concurrent.SimpleReadWriteLock;

/**
 * Global Kafka resource manager. Call shutdown() upon end of application.
 *
 * @author Philip Helger
 */
final class ToopKafkaManager {
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static KafkaProducer<String, String> s_aProducer;
  private static final ICommonsMap<String, String> s_aProps = new CommonsHashMap<> ();

  static {
    // Instead of 16K
    if (false)
      s_aProps.put ("batch.size", "1");
    // Server URL
    s_aProps.put ("bootstrap.servers", "tracker.central.toop:7073");
    // Default: 5secs
    s_aProps.put ("max.block.ms", "5000");
  }

  /**
   * @return The default properties for customization. Changes to this map only
   *         effect new connections! Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableObject
  public static ICommonsMap<String, String> defaultProperties () {
    return s_aProps;
  }

  private ToopKafkaManager () {
  }

  @Nonnull
  @ReturnsMutableObject
  private static Properties _getCreationProperties () {
    final Properties aProps = new Properties ();
    // Use all default props
    aProps.putAll (s_aProps);
    return aProps;
  }

  /**
   * Init the global {@link KafkaProducer} - must be called once before the first
   * message is logged. This is only invoked internally.
   *
   * @return The non-<code>null</code> producer to be used.
   * @throws KafkaException
   *           in case of invalid properties (like non-existing server domain9
   */
  @Nonnull
  public static KafkaProducer<String, String> getOrCreateProducer () throws KafkaException {
    // Read-lock first
    KafkaProducer<String, String> ret = s_aRWLock.readLocked ( () -> s_aProducer);
    if (ret == null) {
      s_aRWLock.writeLock ().lock ();
      try {
        // Try again in write lock
        ret = s_aProducer;
        if (ret == null) {
          // Create new one
          s_aProducer = ret = new KafkaProducer<> (_getCreationProperties (), new StringSerializer (),
                                                   new StringSerializer ());
        }
      } finally {
        s_aRWLock.writeLock ().unlock ();
      }
    }
    return ret;
  }

  /**
   * Shutdown the global {@link KafkaProducer}. This method can be called
   * independent of the initialization state.
   */
  public static void shutdown () {
    s_aRWLock.writeLocked ( () -> {
      if (s_aProducer != null) {
        s_aProducer.close ();
        s_aProducer = null;
      }
    });
  }

  /**
   * Main sending of a message. Since the send call is asynchronous it returns a
   * Future for the RecordMetadata that will be assigned to this record. Invoking
   * get() on this future will block until the associated request completes and
   * then return the metadata for the record or throw any exception that occurred
   * while sending the record.
   *
   * @param sKey
   *          Key to be send. May not be <code>null</code>.
   * @param sValue
   *          Value to be send. May not be <code>null</code>.
   * @param aKafkaCallback
   *          Optional Kafka callback
   * @return The {@link Future} with the details on message receipt
   */
  @Nonnull
  public static Future<RecordMetadata> send (@Nonnull final String sKey, @Nonnull final String sValue,
                                             @Nullable final Callback aKafkaCallback) {
    ValueEnforcer.notNull (sKey, "Key");
    ValueEnforcer.notNull (sValue, "Value");

    final ProducerRecord<String, String> aMessage = new ProducerRecord<> ("toop", sKey, sValue);
    return getOrCreateProducer ().send (aMessage, aKafkaCallback);
  }
}
