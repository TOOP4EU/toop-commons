/**
 * Copyright (C) 2018-2020 toop.eu
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
package eu.toop.commons.usecase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;

/**
 * Predefined TOOP concepts for "registered organization". Source
 * http://wiki.ds.unipi.gr/display/TOOP/National+data+models - all properties
 * (P)
 *
 * @author Philip Helger
 * @since 0.10.0
 * @see EToopConcept for the most update to date concept names
 */
public enum EToopConcept_0_10_7 implements IToopConcept
{
  ACTIVITY_DESCRIPTION ("activityDescription", EToopConcept.ACTIVITY_DESCRIPTION),
  BIRTH_DATE ("birthDate", EToopConcept.BIRTH_DATE),
  CAPTIAL_TYPE ("capitalType", EToopConcept.CAPTIAL_TYPE),
  COMPANY_CODE ("CompanyCode", EToopConcept.COMPANY_CODE),
  COMPANY_NAME ("companyName", EToopConcept.COMPANY_NAME),
  COMPANY_TYPE ("companyType", EToopConcept.COMPANY_TYPE),
  COUNTRY_NAME ("countryName", EToopConcept.COUNTRY_NAME),
  EMAIL_ADDRESS ("EmailAddress", EToopConcept.EMAIL_ADDRESS),
  FAMILY_NAME ("familyName", EToopConcept.FAMILY_NAME),
  FAX_NUMBER ("FaxNumber", EToopConcept.FAX_NUMBER),
  FOUNDATION_DATE ("foundationDate", EToopConcept.FOUNDATION_DATE),
  GIVEN_NAME ("givenName", EToopConcept.GIVEN_NAME),
  HAS_LEGAL_REPRESENTATIVE ("hasLegalRepresentative", EToopConcept.HAS_LEGAL_REPRESENTATIVE),
  LEGAL_STATUS ("legalStatus", EToopConcept.LEGAL_STATUS),
  LEGAL_STATUS_EFFECTIVE_DATE ("legalStatusEffectiveDate", EToopConcept.LEGAL_STATUS_EFFECTIVE_DATE),
  LOCALITY ("locality", EToopConcept.LOCALITY),
  NACE_CODE ("naceCode", EToopConcept.NACE_CODE),
  PERSON ("Person", EToopConcept.PERSON),
  POSTAL_CODE ("postalCode", EToopConcept.POSTAL_CODE),
  REGION ("region", EToopConcept.REGION),
  REGISTERED_ORGANIZATION ("RegisteredOrganization", EToopConcept.REGISTERED_ORGANIZATION),
  REGISTRATION_AUTH ("RegistrationAuthority", EToopConcept.REGISTRATION_AUTH),
  REGISTRATION_DATE ("registrationDate", EToopConcept.REGISTRATION_DATE),
  REGISTRATION_NUMBER ("RegistrationNumber", EToopConcept.REGISTRATION_NUMBER),
  SOCIAL_SEC_NUMBER ("SSNumber", EToopConcept.SOCIAL_SEC_NUMBER),
  STREET_ADDRESS ("streetAddress", EToopConcept.STREET_ADDRESS),
  TELEPHONE_NUMBER ("TelephoneNumber", EToopConcept.TELEPHONE_NUMBER),
  VAT_NUMBER ("VATNumber", EToopConcept.VAT_NUMBER);

  private final String m_sID;
  private final EToopConcept m_eNewConcept;

  private EToopConcept_0_10_7 (@Nonnull @Nonempty final String sID, @Nonnull final EToopConcept eNewConcept)
  {
    m_sID = sID;
    m_eNewConcept = eNewConcept;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getConceptNamespaceURI ()
  {
    return SMMDocumentTypeMapping.SMM_DOMAIN_REGISTERED_ORGANIZATION;
  }

  @Nonnull
  public EToopConcept getNewConcept ()
  {
    return m_eNewConcept;
  }

  @Nullable
  public static EToopConcept_0_10_7 getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopConcept_0_10_7.class, sID);
  }
}
