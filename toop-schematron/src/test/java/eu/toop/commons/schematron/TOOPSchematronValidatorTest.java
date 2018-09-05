package eu.toop.commons.schematron;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
  @Test
  public void testBasic ()
  {
    final TOOPSchematronValidator v = new TOOPSchematronValidator ();
    final ICommonsList <SVRLFailedAssert> aFAs = v.validateTOOPMessage (new FileSystemResource ("xml/data-request-example.xml"));
    assertNotNull (aFAs);
    assertTrue (aFAs.isEmpty ());
  }
}
