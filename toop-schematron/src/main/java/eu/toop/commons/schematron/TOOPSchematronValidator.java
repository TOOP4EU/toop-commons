package eu.toop.commons.schematron;

import java.util.Locale;

import javax.annotation.Nonnull;
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
 * TOOP Schematron validator
 *
 * @author Philip Helger
 */
public class TOOPSchematronValidator
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TOOPSchematronValidator.class);

  public TOOPSchematronValidator ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <SVRLFailedAssert> validateTOOPMessage (@Nonnull final IReadableResource aXML)
  {
    final IReadableResource aSCH = new ClassPathResource ("schematron/TOOP_BusinessRules_DataExchange.sch");
    final SchematronResourceSCH aSchematron = new SchematronResourceSCH (aSCH);
    aSchematron.setURIResolver (new DefaultTransformURIResolver ()
    {
      @Override
      protected Source internalResolve (final String sHref, final String sBase) throws TransformerException
      {
        final String sRealBase = StringHelper.hasText (sBase) ? sBase : aSCH.getAsURL ().toExternalForm ();
        final Source ret = super.internalResolve (sHref, sRealBase);
        if (LOGGER.isDebugEnabled ())
          LOGGER.debug ("URIResolver resolved " + sHref + " to " + ret);
        return ret;
      }
    });
    aSchematron.setErrorListener (new LoggingTransformErrorListener (Locale.US));
    if (!aSchematron.isValidSchematron ())
      throw new IllegalStateException ("Failed to compile Schematron");

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

    try
    {
      final SchematronOutputType aSOT = aSchematron.applySchematronValidationToSVRL (aXMLDoc, aXML.getPath ());
      return SVRLHelper.getAllFailedAssertions (aSOT);
    }
    catch (final Exception ex)
    {
      throw new IllegalStateException ("Error applying SCH onto XML", ex);
    }
  }
}
