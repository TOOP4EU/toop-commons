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
package eu.toop.commons.schematron;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.xslt.SchematronResourceXSLT;
import com.helger.xml.serialize.read.DOMReader;

/**
 * TOOP Schematron validator. Validate DOM documents or other resources using
 * the predefined TOOP Schematron rules.
 *
 * @author Philip Helger
 * @since 0.9.2
 */
@ThreadSafe
public class TOOPSchematronValidator
{
  /**
   * The resource with the rules. Important: this Schematron requires additional
   * code lists in a relative directory!
   */
  public static final IReadableResource SCHEMATRON_RES_XSLT = new ClassPathResource ("xslt/TOOP_BusinessRules_DataExchange.xslt");

  public TOOPSchematronValidator ()
  {}

  /**
   * Create a new {@link ISchematronResource} that is configured correctly so
   * that it can be used to validate TOOP messages. This method is only used
   * internally and is extracted to allow potential modifications in derived
   * classes.
   *
   * @return A new instance every time.
   * @see #validateTOOPMessage(Document)
   * @see #validateTOOPMessage(IReadableResource)
   */
  @Nonnull
  public ISchematronResource createSchematronResource ()
  {
    final SchematronResourceXSLT aSchematron = new SchematronResourceXSLT (SCHEMATRON_RES_XSLT);
    if (!aSchematron.isValidSchematron ())
      throw new IllegalStateException ("Failed to compile Schematron/XSLT " + SCHEMATRON_RES_XSLT.getPath ());
    return aSchematron;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <SVRLFailedAssert> validateTOOPMessage (@Nonnull final IReadableResource aXML)
  {
    // Parse XML to DOM
    final Document aXMLDoc;
    try
    {
      aXMLDoc = DOMReader.readXMLDOM (aXML);
    }
    catch (final SAXException ex)
    {
      throw new IllegalStateException ("Failed to read the provided XML", ex);
    }
    if (aXMLDoc == null)
      throw new IllegalStateException ("Failed to read the provided XML");

    return validateTOOPMessage (aXMLDoc);
  }

  /**
   * Validate the provided DOM representation of a TOOP Request or Response.
   *
   * @param aXMLDoc
   *        The XML DOM node to be validated. May not be <code>null</code>.
   * @return The list of all failed asserts
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <SVRLFailedAssert> validateTOOPMessage (@Nonnull final Document aXMLDoc)
  {
    try
    {
      final ISchematronResource aSchematron = createSchematronResource ();
      // No base URI needed since Schematron contains no includes
      final SchematronOutputType aSOT = aSchematron.applySchematronValidationToSVRL (aXMLDoc, null);
      return SVRLHelper.getAllFailedAssertions (aSOT);
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("Error applying SCH onto XML", ex);
    }
  }
}
