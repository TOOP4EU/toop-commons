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
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.commons.error.level.EErrorLevel;
import com.helger.commons.lang.ClassHelper;
import com.helger.commons.log.LogHelper;

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
   * Enable or disable globally. Call this only globally on startup.
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

  private static void _sendIfEnabled (@Nonnull final String sValue) {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Sending to Kafka: '" + sValue + "'");

    // Send but don't wait for the commit!
    ToopKafkaManager.send ((String) null, sValue, null);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param aErrorLevel
   *          Error level to log the message. May be <code>null</code> to not log
   *          it.
   * @param sValue
   *          Value to send. May not be <code>null</code>.
   * @see #isEnabled()
   */
  public static void send (@Nullable final EErrorLevel aErrorLevel, @Nonnull final String sValue) {
    if (aErrorLevel != null)
      LogHelper.log (ToopKafkaClient.class, aErrorLevel, sValue);
    if (isEnabled ())
      _sendIfEnabled (sValue);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param aErrorLevel
   *          Error level to log the message. May be <code>null</code> to not log
   *          it.
   * @param aValue
   *          Value supplier to send. Is only evaluated if enabled. May not be
   *          <code>null</code>.
   * @see #isEnabled()
   */
  public static void send (@Nullable final EErrorLevel aErrorLevel, @Nonnull final Supplier<String> aValue) {
    send (aErrorLevel, aValue, (Throwable) null);
  }

  /**
   * Send a message, if it is enabled.
   *
   * @param aErrorLevel
   *          Error level to log the message. May be <code>null</code> to not log
   *          it.
   * @param aValue
   *          Value supplier to send. Is only evaluated if enabled. May not be
   *          <code>null</code>.
   * @param t
   *          Exception to be logged. May be <code>null</code>.
   * @see #isEnabled()
   */
  public static void send (@Nullable final EErrorLevel aErrorLevel, @Nonnull final Supplier<String> aValue,
                           @Nullable final Throwable t) {
    String sValue = null;
    if (aErrorLevel != null) {
      sValue = aValue.get ();
      LogHelper.log (ToopKafkaClient.class, aErrorLevel, sValue, t);
    }
    if (isEnabled ()) {
      if (sValue == null)
        sValue = aValue.get ();
      if (t != null)
        sValue += " -- " + ClassHelper.getClassLocalName (t.getClass ()) + ": " + t.getMessage ();
      _sendIfEnabled (sValue);
    }
  }

  /**
   * Shutdown at the end. Note: this only does something, if the client is
   * enabled. Do this only once globally on application shutdown.
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
