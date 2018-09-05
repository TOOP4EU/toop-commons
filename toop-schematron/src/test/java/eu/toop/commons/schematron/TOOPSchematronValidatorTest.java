package eu.toop.commons.schematron;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.schematron.svrl.SVRLFailedAssert;

/**
 * Test class for class {@link TOOPSchematronValidator}.
 *
 * @author Philip Helger
 */
public final class TOOPSchematronValidatorTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (TOOPSchematronValidatorTest.class);

  @Test
  public void testBasic ()
  {
    final TOOPSchematronValidator v = new TOOPSchematronValidator ();
    final ICommonsList <SVRLFailedAssert> aFAs = v.validateTOOPMessage (new FileSystemResource ("src/test/resources/xml/data-request-example.xml"));
    assertNotNull (aFAs);
    for (final SVRLFailedAssert aFA : aFAs)
      LOGGER.info (aFA.toString ());
    if (false)
      assertTrue (aFAs.isEmpty ());
  }
}
