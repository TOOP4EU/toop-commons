package eu.toop.commons.schematron;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.schematron.SchematronHelper;
import com.helger.schematron.svrl.SVRLFailedAssert;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.xslt.SchematronResourceSCH;
import com.helger.xml.transform.LoggingTransformErrorListener;

/**
 * TOOP Schematron validator
 *
 * @author Philip Helger
 */
public class TOOPSchematronValidator
{
  public TOOPSchematronValidator ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <SVRLFailedAssert> validateTOOPMessage (@Nonnull final IReadableResource aXML)
  {
    final SchematronResourceSCH aSchematron = SchematronResourceSCH.fromClassPath ("schematron/TOOP_BusinessRules_DataExchange.sch");
    aSchematron.setErrorListener (new LoggingTransformErrorListener (Locale.US));
    if (!aSchematron.isValidSchematron ())
      throw new IllegalStateException ("Failed to compile Schematron");
    final SchematronOutputType aSOT = SchematronHelper.applySchematron (aSchematron, aXML);
    if (aSOT == null)
      throw new IllegalStateException ("Failed to read the provided XML");
    return SVRLHelper.getAllFailedAssertions (aSOT);
  }
}
