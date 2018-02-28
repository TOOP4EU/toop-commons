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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.ICommonsMap;

/**
 * Global TOOP Kafka Client. It is disabled by default.
 *
 * @author Philip Helger
 */
public final class ToopKafkaClient {
  private static final Logger s_aLogger = LoggerFactory.getLogger (ToopKafkaClient.class);
  private static final AtomicBoolean s_aEnabled = new AtomicBoolean (false);

  /**
   * @return The default properties for customization. Changes to this map only
   *         effect new connections! Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableObject
  public static ICommonsMap<String, String> defaultProperties () {
    return ToopKafkaManager.defaultProperties ();
  }

  /**
   * Enable or disable globally.
   *
   * @param bEnabled
   *          <code>true</code> to enable, <code>false</code> to disable.
   */
  public static void setEnabled (final boolean bEnabled) {
    s_aEnabled.set (bEnabled);
    s_aLogger.info ("TOOP Kafka Client is now " + (bEnabled ? "enabled" : "disabled"));
  }

  /**
   * @return <code>true</code> if client is enabled, <code>false</code> if not. By
   *         default is is disabled.
   */
  public static boolean isEnabled () {
    return s_aEnabled.get ();
  }

  private static void _sendIfEnabled (@Nonnull final String sKey, @Nonnull final String sValue) {
    s_aLogger.info ("Sending to Kafka: '" + sKey + "' / '" + sValue + "'");

    // Send but don't wait for the commit!
    ToopKafkaManager.send (sKey, sValue, null);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param sKey
   *          Key to send. May not be <code>null</code>.
   * @param sValue
   *          Value to send. May not be <code>null</code>.
   * @see #isEnabled()
   */
  public static void send (@Nonnull final String sKey, @Nonnull final String sValue) {
    if (isEnabled ())
      _sendIfEnabled (sKey, sValue);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param sKey
   *          Key to send. May not be <code>null</code>.
   * @param aValue
   *          Value supplier to send. Is only evaluated if enabled. May not be
   *          <code>null</code>.
   * @see #isEnabled()
   */
  public static void send (@Nonnull final String sKey, @Nonnull final Supplier<String> aValue) {
    if (isEnabled ())
      _sendIfEnabled (sKey, aValue.get ());
  }

  /**
   * Shutdown at the end. Note: this only does something, if the client is
   * enabled.
   *
   * @see #isEnabled()
   */
  public static void close () {
    if (isEnabled ()) {
      ToopKafkaManager.shutdown ();
      s_aLogger.info ("Successfully shutdown Kafka client");
    }
  }
}
