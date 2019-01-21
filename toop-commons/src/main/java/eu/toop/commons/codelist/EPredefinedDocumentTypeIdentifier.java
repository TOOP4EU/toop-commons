package eu.toop.commons.codelist;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringHelper;
import com.helger.commons.version.Version;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * This file was automatically generated.
 * Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedDocumentTypeIdentifier
    implements IPredefinedIdentifier
{

    /**
     * <b>This item is deprecated since version 2 and should not be used to issue new identifiers!</b><br>Registered Organization - <code>urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10</code><br>
     * 
     * @since code list v1
     */
    @Deprecated
    URN_EU_TOOP_NS_DATAEXCHANGE_1P10_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_10("Registered Organization", "urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10", Version.parse("1"), true, Version.parse("2")),

    /**
     * <b>This item is deprecated since version 2 and should not be used to issue new identifiers!</b><br>Registered Organization - <code>urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10</code><br>
     * 
     * @since code list v1
     */
    @Deprecated
    URN_EU_TOOP_NS_DATAEXCHANGE_1P10_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_10("Registered Organization", "urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10", Version.parse("1"), true, Version.parse("2")),

    /**
     * Registered Organization - <code>urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_40("Registered Organization", "urn:eu:toop:ns:dataexchange-1p40::Request##urn:eu.toop.request.registeredorganization::1.40", Version.parse("2"), false, null),

    /**
     * Registered Organization - <code>urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40</code><br>
     * 
     * @since code list v2
     */
    URN_EU_TOOP_NS_DATAEXCHANGE_1P40_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_40("Registered Organization", "urn:eu:toop:ns:dataexchange-1p40::Response##urn:eu.toop.response.registeredorganization::1.40", Version.parse("2"), false, null);
    public static final EPredefinedDocumentTypeIdentifier REQUEST_REGISTEREDORGANIZATION = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P10_REQUEST_URN_EU_TOOP_REQUEST_REGISTEREDORGANIZATION_1_10;
    public static final EPredefinedDocumentTypeIdentifier RESPONSE_REGISTEREDORGANIZATION = EPredefinedDocumentTypeIdentifier.URN_EU_TOOP_NS_DATAEXCHANGE_1P10_RESPONSE_URN_EU_TOOP_RESPONSE_REGISTEREDORGANIZATION_1_10;
    public static final String DOC_TYPE_SCHEME = "toop-doctypeid-qns";
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
        final String sID) {
        if (StringHelper.hasText(sID)) {
            for (EPredefinedDocumentTypeIdentifier e: EPredefinedDocumentTypeIdentifier.values()) {
                if (e.getID().equals(sID)) {
                    return e;
                }
            }
        }
        return null;
    }

    @Nullable
    public static EPredefinedDocumentTypeIdentifier getFromDocumentTypeIdentifierOrNull(
        @Nullable
        final String sScheme,
        @Nullable
        final String sID) {
        if (StringHelper.hasText(sScheme)&&StringHelper.hasText(sID)) {
            for (EPredefinedDocumentTypeIdentifier e: EPredefinedDocumentTypeIdentifier.values()) {
                if (e.getScheme().equals(sScheme)&&e.getID().equals(sID)) {
                    return e;
                }
            }
        }
        return null;
    }
}
