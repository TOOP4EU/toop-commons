/**
 * Copyright (C) 2018 toop.eu
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
package eu.toop.commons.codelist;

import com.helger.commons.annotation.CodingStyleguideUnaware;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.version.Version;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This file was automatically generated. Do NOT edit!
 */
@CodingStyleguideUnaware
public enum EPredefinedParticipantIdentifierScheme implements IPredefined
{

  /**
   * Institut National de la Statistique et des Etudes Economiques, (I.N.S.E.E.)
   * - <code>0002</code> Structure of the code: 1) Number of characters: 9
   * characters (&quot;SIREN&quot;) 14 &quot; 9+5 (&quot;SIRET&quot;), The 9
   * character number designates an organization, The 14 character number
   * designates a specific establishment of the organization designated by the
   * first 9 characters. 2) Check digits: 9th &amp; 14th character
   * respectively<br>
   * Display requirements: The 9 figure code number (SIREN) is written in groups
   * of 3 characters. Example: 784 301 772, The 14 figure code number is written
   * in 3 groups of 3 characters and a single group of 5. Example: 784 301 772
   * 00025<br>
   * 
   * @since code list v1
   */
  FR_SIRENE ("Institut National de la Statistique et des Etudes Economiques, (I.N.S.E.E.)",
             "0002",
             Version.parse ("1"),
             false,
             null),

  /**
   * The National Tax Board - <code>0007</code> Structure of the code: 1) 10
   * digits. 1st digit = Group number, 2nd - 9th digit = Ordinalnumber1st digit,
   * = Group number, 10th digit = Check digit, 2) Last digit.<br>
   * Display requirements: Single group of 10 digits.<br>
   * 
   * @since code list v1
   */
  SE_ORGNR ("The National Tax Board", "0007", Version.parse ("1"), false, null),

  /**
   * DU PONT DE NEMOURS - <code>0009</code> Structure of the code: 1) 14 digits,
   * 2) None<br>
   * Display requirements: In four groups, Groups 1 - 3 = three digits each,
   * Group 4 = five digits<br>
   * 
   * @since code list v1
   */
  FR_SIRET ("DU PONT DE NEMOURS", "0009", Version.parse ("1"), false, null),

  /**
   * National Board of Taxes, (Verohallitus) - <code>0037</code> Structure of
   * the code: 1) ICD 4 Digits, Organization code upto 11 characters,
   * Organization name upto 250 characters, 2) None - Example: 00371234567800001
   * - 0037 Country code for Finland (ISO 6523 International Code Designator
   * (ICD) value) - 12345678 Business ID without hyphen - 00001 Optional
   * specifier for organisation unit (assigned by the organisation itself)<br>
   * Display requirements: None<br>
   * Usage information: OVT identifier conforming to standard ISO6523. -
   * Constant 0037 (Finnish tax administration organisation code) - Finnish
   * local tax ID, 8 characters with initial zero and no hyphen - Free-format 5
   * characters, for example profit center. Example: 003710948874<br>
   * 
   * @since code list v1
   */
  FI_OVT ("National Board of Taxes, (Verohallitus)", "0037", Version.parse ("1"), false, null),

  /**
   * Dun and Bradstreet Ltd - <code>0060</code> Structure of the code: 1) 8
   * digits, 1st-7th digit = number, 8th digit = check number, 2) digit<br>
   * Display requirements: Single group of 8 digits<br>
   * 
   * @since code list v1
   */
  DUNS ("Dun and Bradstreet Ltd", "0060", Version.parse ("1"), false, null),

  /**
   * GS1 GLN - <code>0088</code> Structure of the code: 1) Eight identification
   * digits and a check digit. A two digit prefix will be added in the future
   * but it will not be used to calculate the check digit. 2) The Organization
   * name is not part of the D-U-N-S number.<br>
   * Display requirements: IIIIIIIIC where all characters are the digits 0, to
   * 9, I = an identification digit and C = the check digit. When the prefix (P)
   * is added the display requirement will be eleven digits, PPIIIIIIIIC.<br>
   * 
   * @since code list v1
   */
  GLN ("GS1 GLN", "0088", Version.parse ("1"), false, null),

  /**
   * Danish Chamber of Commerce - <code>0096</code> Structure of the code: 1) 13
   * digits including check digits, 2) None<br>
   * Display requirements: None<br>
   * 
   * @since code list v1
   */
  DK_P ("Danish Chamber of Commerce", "0096", Version.parse ("1"), false, null),

  /**
   * FTI - Ediforum Italia - <code>0097</code> Structure of the code: Character
   * repertoire, The EDI identifier consists of digits only. The identifier has
   * a fixed length. No separators are required. Structure: [123] [123456]
   * [123456] [12], 17, &lt; &gt;, A B C D, A: numerical value allocated by the
   * RA to the regional sub-authority, (3 digits), B: numerical value allocated
   * by the sub-authority to the registered organization (mandatory part of the
   * identifier; 6 digits), C: numerical value used by the registered
   * organization (free part; 6 digits), D: numerical check digit calculated by
   * the registered organization; (2 digits), Check digit computation, The check
   * digit is modular 97 computed on ABC as one number.<br>
   * Display requirements: None<br>
   * 
   * @since code list v1
   */
  IT_FTI ("FTI - Ediforum Italia", "0097", Version.parse ("1"), false, null),

  /**
   * Vereniging van Kamers van Koophandel en Fabrieken in Nederland, Scheme -
   * <code>0106</code>
   * 
   * @since code list v1
   */
  NL_KVK ("Vereniging van Kamers van Koophandel en Fabrieken in Nederland, Scheme",
          "0106",
          Version.parse ("1"),
          false,
          null),

  /**
   * SIA-Società Interbancaria per l'Automazione S.p.A. - <code>0135</code>
   * Structure of the code: Structure of EDI identifier, Character repertoire,
   * The EDI identifier consists of digits only. The identifier has a fixed
   * length. No separators are required. Structure: [1234567] [123] [1] [12345],
   * min 11- max 16, &lt; &gt;, A B C D, A: numerical value (7 digits) assigned
   * by Uffico Provinciale IVA (local branch of Ministry of Finance); B:
   * numerical value a (3 digits) identifying the County; C: numerical check
   * digit (1 digit); D: optional numerical value (up to 5 digits0 used by the
   * registered organization (free part). Check digit computation, The check
   * digit algorithm is the one published in the Gazzetta Ufficiale no 345 of
   * December 29 1976.<br>
   * Display requirements: None<br>
   * 
   * @since code list v1
   */
  IT_SIA ("SIA-Societ\u00e0 Interbancaria per l'Automazione S.p.A.", "0135", Version.parse ("1"), false, null),

  /**
   * Servizi Centralizzati SECETI S.p.A. - <code>0142</code> Structure of the
   * code: First field: ICD: 4 digits, Second field: sequence of digits<br>
   * Display requirements: None<br>
   * 
   * @since code list v1
   */
  IT_SECETI ("Servizi Centralizzati SECETI S.p.A.", "0142", Version.parse ("1"), false, null),

  /**
   * DIGSTORG - <code>0184</code> Structure of the code: Defined by Danish
   * Agency for Digitisation<br>
   * 
   * @since code list v1
   */
  DK_DIGST ("DIGSTORG", "0184", Version.parse ("1"), false, null),

  /**
   * Dutch Originator's Identification Number - <code>0190</code>
   * 
   * @since code list v1
   */
  NL_OINO ("Dutch Originator's Identification Number", "0190", Version.parse ("1"), false, null),

  /**
   * Centre of Registers and Information Systems of the Ministry of Justice -
   * <code>0191</code> Structure of the code: Always 8-digit number<br>
   * Display requirements: None<br>
   * 
   * @since code list v1
   */
  EE_CC ("Centre of Registers and Information Systems of the Ministry of Justice",
         "0191",
         Version.parse ("1"),
         false,
         null),

  /**
   * The Brønnøysund Register Centre - <code>0192</code> Structure of the code:
   * 9 digits The organization number consists of 9 digits where the last digit
   * is a control digit calculated with standard weights, modulus 11. After
   * this, weights 3, 2, 7, 6, 5, 4, 3 and 2 are calculated from the first
   * digit.<br>
   * Display requirements: None<br>
   * 
   * @since code list v1
   */
  NO_ORG ("The Br\u00f8nn\u00f8ysund Register Centre", "0192", Version.parse ("1"), false, null);
  private final String m_sName;
  private final String m_sID;
  private final Version m_aSince;
  private final boolean m_bDeprecated;
  private final Version m_aDeprecatedSince;

  private EPredefinedParticipantIdentifierScheme (@Nonnull @Nonempty final String sName,
                                                  @Nonnull @Nonempty final String sID,
                                                  @Nonnull final Version aSince,
                                                  final boolean bDeprecated,
                                                  @Nullable final Version aDeprecatedSince)
  {
    m_sName = sName;
    m_sID = sID;
    m_aSince = aSince;
    m_bDeprecated = bDeprecated;
    m_aDeprecatedSince = aDeprecatedSince;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public Version getSince ()
  {
    return m_aSince;
  }

  public boolean isDeprecated ()
  {
    return m_bDeprecated;
  }

  @Nullable
  public Version getDeprecatedSince ()
  {
    return m_aDeprecatedSince;
  }
}
