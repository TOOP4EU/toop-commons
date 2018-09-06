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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.string.StringHelper;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.xslt.SchematronResourceSCH;
import com.helger.xml.serialize.read.DOMReader;
import com.helger.xml.transform.DefaultTransformURIResolver;
import com.helger.xml.transform.LoggingTransformErrorListener;

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
  public static final IReadableResource SCHEMATRON_RES = new ClassPathResource ("schematron/TOOP_BusinessRules_DataExchange.sch");
  private static final Logger LOGGER = LoggerFactory.getLogger (TOOPSchematronValidator.class);

  public TOOPSchematronValidator ()
  {}

  /**
   * Create a new {@link SchematronResourceSCH} that is configured correctly so
   * that it can be used to validate TOOP messages. This method is only used
   * internally and is extracted to allow potential modifications in derived
   * classes.
   *
   * @return A new instance every time.
   * @see #validateTOOPMessage(Document)
   * @see #validateTOOPMessage(IReadableResource)
   */
  @Nonnull
  public SchematronResourceSCH createSchematronResource ()
  {
    final SchematronResourceSCH aSchematron = new SchematronResourceSCH (SCHEMATRON_RES);

    // The URI resolver is necessary for the XSLT to resolve the codelists
    aSchematron.setURIResolver (new DefaultTransformURIResolver ()
    {
      @Override
      protected Source internalResolve (final String sHref, final String sBase) throws TransformerException
      {
        final String sRealBase = StringHelper.hasText (sBase) ? sBase : SCHEMATRON_RES.getAsURL ().toExternalForm ();
        final Source ret = super.internalResolve (sHref, sRealBase);
        if (LOGGER.isDebugEnabled ())
          LOGGER.debug ("URIResolver resolved " + sHref + " to " + ret);
        return ret;
      }
    });
    aSchematron.setErrorListener (new LoggingTransformErrorListener (Locale.US));
    if (!aSchematron.isValidSchematron ())
      throw new IllegalStateException ("Failed to compile Schematron " + SCHEMATRON_RES.getPath ());

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
      final SchematronResourceSCH aSchematron = createSchematronResource ();
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
