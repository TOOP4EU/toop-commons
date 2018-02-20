package eu.toop.commons.doctype;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Predefined TOOP process ID. Currently mock.
 *
 * @author Philip Helger
 *
 */
public enum EToopProcess {
  PROC ("urn:eu.toop:proc");

  // For R2D2 we need a scheme
  public static final String PROCESS_SCHEME = "toop-procid";

  private final String m_sProcessID;

  private EToopProcess (@Nonnull @Nonempty final String sProcessID) {
    m_sProcessID = sProcessID;
  }

  /**
   * @return Always {@link #PROCESS_SCHEME}.
   */
  @Nonnull
  @Nonempty
  public String getScheme () {
    return PROCESS_SCHEME;
  }

  /**
   *
   * @return The process ID (value) from the constructor. Does not contain the
   *         scheme!
   */
  @Nonnull
  @Nonempty
  public String getValue () {
    return m_sProcessID;
  }

  @Nonnull
  @Nonempty
  public String getURIEncoded () {
    return getScheme () + "::" + getValue ();
  }
}
