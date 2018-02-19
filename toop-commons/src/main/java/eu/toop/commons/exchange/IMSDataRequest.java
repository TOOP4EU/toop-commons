package eu.toop.commons.exchange;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Member state request (DC to DP). This data request MUST serialize to XML
 * because it is used
 *
 * @author Philip Helger
 */
public interface IMSDataRequest extends IToopDataElement {
  /**
   * @return The ID of the destination country. May not be <code>null</code> and
   *         must be the 2-letter country code as in "DE" or "AT". This field is
   *         required for R2D2 client.
   */
  @Nonnull
  @Nonempty
  String getDestinationCountryCode ();

  /**
   * @return The document type ID that is e.g. used in R2D2 for endpoint
   *         discovery.
   */
  @Nonnull
  @Nonempty
  String getDocumentTypeID ();

  /**
   * @return The process ID that is e.g. used in R2D2 for endpoint discovery.
   */
  @Nonnull
  @Nonempty
  String getProcessID ();

  /**
   *
   * @return <code>true</code> if this is a production message, <code>false</code>
   *         if it is a test message.
   */
  boolean isProduction ();
}
