package eu.toop.commons.jaxb;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import eu.toop.commons.dataexchange.TDETOOPDataResponseType;

/**
 * Test class for {@link ToopReader} and {@link ToopWriter}.
 *
 * @author Philip Helger
 */
public final class ToopDataExchangeFuncTest {
  @Test
  public void testReadWriteExample () {
    final TDETOOPDataResponseType aResponse = ToopReader.dataResponse ()
                                                        .read (new File ("src/test/resources/xml/instance1.xml"));
    assertNotNull (aResponse);
    final String sXML = ToopWriter.dataResponse ().getAsString (aResponse);
    assertNotNull (sXML);
  }
}
