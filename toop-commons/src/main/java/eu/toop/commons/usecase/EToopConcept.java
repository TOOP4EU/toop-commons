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
package eu.toop.commons.usecase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

import eu.toop.commons.concept.ConceptValue;

/**
 * Predefined TOOP concepts for "registered organization". Source
 * http://wiki.ds.unipi.gr/display/TOOP/National+data+models - all properties
 * (P)
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public enum EToopConcept implements IHasID <String>
{
  ACTIVITY_DESCRIPTION ("activityDescription"),
  BIRTH_DATE ("birthDate"),
  CAPTIAL_TYPE ("capitalType"),
  COMPANY_CODE ("CompanyCode"),
  COMPANY_NAME ("companyName"),
  COMPANY_TYPE ("companyType"),
  COUNTRY_NAME ("countryName"),
  EMAIL_ADDRESS ("EmailAddress"),
  FAMILY_NAME ("familyName"),
  FAX_NUMBER ("FaxNumber"),
  FOUNDATION_DATE ("foundationDate"),
  GIVEN_NAME ("givenName"),
  HAS_LEGAL_REPRESENTATIVE ("hasLegalRepresentative"),
  LEGAL_STATUS ("legalStatus"),
  LEGAL_STATUS_EFFECTIVE_DATE ("legalStatusEffectiveDate"),
  LOCALITY ("locality"),
  NACE_CODE ("naceCode"),
  PERSON ("Person"),
  POSTAL_CODE ("postalCode"),
  REGION ("region"),
  REGISTERED_ORGANIZATION ("RegisteredOrganization"),
  REGISTRATION_AUTH ("RegistrationAuthority"),
  REGISTRATION_DATE ("registrationDate"),
  REGISTRATION_NUMBER ("RegistrationNumber"),
  SOCIAL_SEC_NUMBER ("SSNumber"),
  STREET_ADDRESS ("streetAddress"),
  TELEPHONE_NUMBER ("TelephoneNumber"),
  VAT_NUMBER ("VATNumber");

  private final String m_sID;

  private EToopConcept (@Nonnull @Nonempty final String sID)
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
    return new ConceptValue (SMMDocumentTypeMapping.SMM_DOMAIN_REGISTERED_ORGANIZATION, m_sID);
  }

  @Nullable
  public static EToopConcept getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopConcept.class, sID);
  }
}
