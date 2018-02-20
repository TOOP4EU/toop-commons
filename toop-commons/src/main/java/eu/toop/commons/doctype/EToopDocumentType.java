package eu.toop.commons.doctype;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * Predefined TOOP document types. Currently all mock.
 *
 * @author Philip Helger
 *
 */
public enum EToopDocumentType {
  DOCTYPE1 ("urn:eu.toop:docType1"),
  DOCTYPE2 ("urn:eu.toop:docType1"),
  DOCTYPE3 ("urn:eu.toop:docType1"),
  DOCTYPE4 ("urn:eu.toop:docType1");

  // For R2D2 we need a scheme
  public static final String DOC_TYPE_SCHEME = "toop-doctypeid";

  private final String m_sDocTypeID;

  private EToopDocumentType (@Nonnull @Nonempty final String sDocTypeID) {
    m_sDocTypeID = sDocTypeID;
  }

  /**
   * @return Always {@link #DOC_TYPE_SCHEME}.
   */
  @Nonnull
  @Nonempty
  public String getScheme () {
    return DOC_TYPE_SCHEME;
  }

  /**
   *
   * @return The document type ID (value) from the constructor. Does not contain
   *         the scheme!
   */
  @Nonnull
  @Nonempty
  public String getValue () {
    return m_sDocTypeID;
  }

  @Nonnull
  @Nonempty
  public String getURIEncoded () {
    return getScheme () + "::" + getValue ();
  }
}
