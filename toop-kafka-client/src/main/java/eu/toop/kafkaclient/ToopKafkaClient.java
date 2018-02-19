package eu.toop.kafkaclient;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global TOOP Kafka Client. It is disabled by default.
 *
 * @author Philip Helger
 */
public final class ToopKafkaClient {
  private static final Logger s_aLogger = LoggerFactory.getLogger(ToopKafkaClient.class);
  public static final AtomicBoolean s_aEnabled = new AtomicBoolean(false);

  /**
   * Enable or disable globally.
   *
   * @param bEnabled
   *          <code>true</code> to enable, <code>false</code> to disable.
   */
  public static void setEnabled(final boolean bEnabled) {
    s_aEnabled.set(bEnabled);
    s_aLogger.info("TOOP Kafka Client is now " + (bEnabled ? "enabled" : "disabled"));
  }

  /**
   * @return <code>true</code> if client is enabled, <code>false</code> if not. By
   *         default is is disabled.
   */
  public static boolean isEnabled() {
    return s_aEnabled.get();
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
  public static void send(@Nonnull final String sKey, @Nonnull final String sValue) {
    if (isEnabled()) {
      s_aLogger.info("Sending to Kafka: '" + sKey + "' / '" + sValue + "'");

      // Send but don't wait for the commit!
      ToopKafkaManager.send(sKey, sValue, null);
    }
  }

  /**
   * Shutdown at the end. Note: this only does something, if the client is
   * enabled.
   *
   * @see #isEnabled()
   */
  public static void close() {
    if (isEnabled()) {
      ToopKafkaManager.shutdown();
      s_aLogger.info("Successfully shutdown Kafka client");
    }
  }
}
