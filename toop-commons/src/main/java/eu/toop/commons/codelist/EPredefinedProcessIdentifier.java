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
public enum EPredefinedProcessIdentifier
    implements IPredefinedIdentifier
{

    /**
     * TOOP Request Response for Data - <code>urn:eu.toop.process.datarequestresponse</code><br>
     * 
     * @since code list v1
     */
    URN_EU_TOOP_PROCESS_DATAREQUESTRESPONSE("TOOP Request Response for Data", "urn:eu.toop.process.datarequestresponse", Version.parse("1"), false, null),

    /**
     * TOOP Request Response for Documents - <code>urn:eu.toop.process.documentrequestresponse</code><br>
     * 
     * @since code list v1
     */
    URN_EU_TOOP_PROCESS_DOCUMENTREQUESTRESPONSE("TOOP Request Response for Documents", "urn:eu.toop.process.documentrequestresponse", Version.parse("1"), false, null);
    public final static EPredefinedProcessIdentifier DATAREQUESTRESPONSE = EPredefinedProcessIdentifier.URN_EU_TOOP_PROCESS_DATAREQUESTRESPONSE;
    public final static EPredefinedProcessIdentifier DOCUMENTREQUESTRESPONSE = EPredefinedProcessIdentifier.URN_EU_TOOP_PROCESS_DOCUMENTREQUESTRESPONSE;
    public final static String PROCESS_SCHEME = "toop-procid-agreement";
    private final String m_sName;
    private final String m_sID;
    private final Version m_aSince;
    private final boolean m_bDeprecated;
    private final Version m_aDeprecatedSince;

    private EPredefinedProcessIdentifier(
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
        return PROCESS_SCHEME;
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
    public static EPredefinedProcessIdentifier getFromProcessIdentifierOrNull(
        @Nullable
        final String sProcessID) {
        for (EPredefinedProcessIdentifier e: EPredefinedProcessIdentifier.values()) {
            if (e.getID().equals(sProcessID)) {
                return e;
            }
        }
        return null;
    }
}
