/**
 * Copyright (C) 2018-2019 toop.eu
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
 * Predefined TOOP concepts for "registered organization". Source
 * http://wiki.ds.unipi.gr/display/TOOP/National+data+models - all properties
 * (P)
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public enum ERegOrgConcept implements IHasID <String>
{
  ACTIVITY_DESCRIPTION ("activityDescription"),
  BIRTHDAY ("birthday"),
  COMPANY_NAME ("companyName"),
  CONTACT_NUMBER_VALUE ("contactNumberValue"),
  COUNTRY_NAME ("countryName"),
  EMAIL_ADDRESS_VALUE ("emailAddressValue"),
  FAMILY_NAME ("familyName"),
  GIVEN_NAME ("givenName"),
  FOUNDATION_DATE ("foundationDate"),
  HAS_CAPTIAL_TYPE ("hasCapitalType"),
  HAS_COMPANY_CODE ("hasCompanyCode"),
  HAS_COMPANY_TYPE ("hasCompanyType"),
  HAS_CONTACT_NUMBER ("hasContactNumber"),
  HAS_EMAIL_ADDRESS ("hasEmailAddress"),
  HAS_LEGAL_STATUS ("hasLegalStatus"),
  HAS_NACE_CODE ("hasNaceCode"),
  HAS_REGISTRATION_AUTH ("hasRegistrationAuthority"),
  HAS_REGISTRATION_NUMBER ("hasRegistrationNumber"),
  HAS_SOCIAL_SEC_NUMBER ("hasSSNumber"),
  HAS_VAT_NUMBER ("hasVATNumber"),
  LEGAL_STATUS_EFFECTIVE_DATE ("legalStatusEffectiveDate"),
  LOCALITY ("locality"),
  POSTAL_CODE ("postalCode"),
  REGION ("region"),
  REGISTRATION_DATE ("registrationDate"),
  STREET_ADDRESS ("streetAddress");

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
