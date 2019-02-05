package eu.toop.commons.usecase.regorg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.usecase.SMMDocumentTypeMapping;

/**
 * Predefined TOOP concepts for "registered organization".
 * 
 * @author Philip Helger
 * @since 0.10.0
 */
public enum ERegOrgConcept implements IHasID <String>
{
  ACTIVITY_DESCRIPTION ("activityDescription"),
  COMPANY_CODE ("CompanyCode"),
  COMPANY_NAME ("companyName"),
  HAS_COMPANY_CODE ("hasCompanyCode"),
  HAS_COMPANY_TYPE ("hasCompanyType"),
  HAS_LEGAL_STATUS ("hasLegalStatus"),
  HAS_NACE_CODE ("hasNaceCode"),
  HAS_REGISTRATION_AUTH ("hasRegistrationAuthority"),
  LEGAL_STATUS_EFFECTIVE_DATE ("legalStatusEffectiveDate"),
  PHYSICAL_ADDRESS ("PhysicalAddress"),
  REGISTRATION_DATE ("registrationDate"),
  REGISTRATION_NUMBER ("RegistrationNumber"),
  SOCIAL_SEC_NUMBER ("SSNumber"),
  VAT_NUMBER ("VATNumber");

  private final String m_sID;

  private ERegOrgConcept (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ConceptValue getAsConceptValue ()
  {
    return new ConceptValue (SMMDocumentTypeMapping.URI_REGISTERED_ORGANIZATION, m_sID);
  }

  @Nullable
  public static ERegOrgConcept getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (ERegOrgConcept.class, sID);
  }
}
