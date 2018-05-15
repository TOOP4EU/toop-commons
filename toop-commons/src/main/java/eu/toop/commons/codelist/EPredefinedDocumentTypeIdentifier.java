package eu.toop.commons.codelist;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.version.Version;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedDocumentTypeIdentifier {

    /**
     * Registered Organization - <code>urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10</code><br>
     * 
     * @since code list v1
     */
    urn_eu_toop_ns_dataexchange_1p10__Request__urn_eu_toop_request_registeredorganization__1_10("Registered Organization", "urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10", Version.parse("1"), false, null),

    /**
     * Registered Organization - <code>urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10</code><br>
     * 
     * @since code list v1
     */
    urn_eu_toop_ns_dataexchange_1p10__Response__urn_eu_toop_response_registeredorganization__1_10("Registered Organization", "urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10", Version.parse("1"), false, null);
    public final static String DOC_TYPE_SCHEME = "toop-doctypeid-qns";
    private final String m_sName;
    private final String m_sID;
    private final Version m_aSince;
    private final boolean m_bDeprecated;
    private final Version m_aDeprecatedSince;

    private EPredefinedDocumentTypeIdentifier(
        @Nonnull
        @Nonempty
        final String sName,
        @Nonnull
        @Nonempty
        final String sID,
        @Nonnull
        final Version aSince, final boolean bDeprecated,
        @Nullable
        final Version aDeprecatedSince) {
        m_sName = sName;
        m_sID = sID;
        m_aSince = aSince;
        m_bDeprecated = bDeprecated;
        m_aDeprecatedSince = aDeprecatedSince;
    }

    @Nonnull
    @Nonempty
    public String getName() {
        return m_sName;
    }

    @Nonnull
    @Nonempty
    public String getScheme() {
        return DOC_TYPE_SCHEME;
    }

    @Nonnull
    @Nonempty
    public String getID() {
        return m_sID;
    }

    @Nonnull
    public Version getSince() {
        return m_aSince;
    }

    public boolean isDeprecated() {
        return m_bDeprecated;
    }

    @Nullable
    public Version getDeprecatedSince() {
        return m_aDeprecatedSince;
    }

    @Nullable
    public static EPredefinedDocumentTypeIdentifier getFromDocumentTypeIdentifierOrNull(
        @Nullable
        final String sDocTypeID) {
        for (EPredefinedDocumentTypeIdentifier e: EPredefinedDocumentTypeIdentifier.values()) {
            if (e.getID().equals(sDocTypeID)) {
                return e;
            }
        }
        return null;
    }
}
