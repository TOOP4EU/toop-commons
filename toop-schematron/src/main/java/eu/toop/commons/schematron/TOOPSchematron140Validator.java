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
package eu.toop.commons.schematron;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;

/**
 * TOOP Schematron validator for the 1.4.0 data model. Validate DOM documents or
 * other resources using the predefined TOOP Schematron rules.
 *
 * @author Philip Helger
 * @since 0.10.0
 */
@ThreadSafe
public class TOOPSchematron140Validator extends AbstractTOOPSchematronValidator
{
  /**
   * The resource with the rules. Important: this Schematron requires additional
   * code lists in a relative directory!
   */
  public static final IReadableResource TOOP_140_SCHEMATRON_RES_XSLT = new ClassPathResource ("141/xslt/TOOP_BusinessRules_DataExchange.xslt");

  public TOOPSchematron140Validator ()
  {}

  @Override
  @Nonnull
  protected final IReadableResource getSchematronXSLTResource ()
  {
    return TOOP_140_SCHEMATRON_RES_XSLT;
  }
}
