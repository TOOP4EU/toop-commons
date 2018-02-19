package eu.toop.commons.exchange.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.helger.asic.SignatureHelper;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.commons.exchange.mock.MSDataResponse;
import eu.toop.commons.exchange.mock.ToopDataRequest;
import eu.toop.commons.exchange.mock.ToopDataResponse;

@SuppressWarnings ("deprecation")
public final class ToopMessageBundleBuilderTest {
  private static final SignatureHelper SH = new SignatureHelper(FileHelper.getInputStream(new File("src/test/resources/demo-keystore.jks")),
                                                                "password", null, "password");

  @Test
  public void testRequestMessage () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
      ToopMessageBuilder.createRequestMessage(new MSDataRequest("SE", "docTypeID1", "procID2", false, "ABC123"),
                                              archiveOutput, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream()) {
        // Read ASIC again
        final ToopRequestMessage bundleRead = ToopMessageBuilder.parseRequestMessage(archiveInput,
                                                                                     MSDataRequest.getDeserializerFunction());
        assertNotNull(bundleRead);

        assertTrue(bundleRead.getMSDataRequest() instanceof MSDataRequest);

        final MSDataRequest aMSReq = (MSDataRequest) bundleRead.getMSDataRequest();
        assertEquals(aMSReq.getDestinationCountryCode(), "SE");
        assertEquals(aMSReq.getDocumentTypeID(), "docTypeID1");
        assertEquals(aMSReq.getProcessID(), "procID2");
        assertFalse(aMSReq.isProduction());
        assertEquals(aMSReq.getIdentifier(), "ABC123");
      }
    }
  }

  @Test
  public void testResponseMessage () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
      ToopMessageBuilder.createResponseMessage(new MSDataRequest("SE", "docTypeID1", "procID2", false, "ABC123"),
                                               new ToopDataRequest("DEF456"), new MSDataResponse("AAA111"),
                                               new ToopDataResponse("BBB222"), archiveOutput, SH);

      try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream()) {
        // Read ASIC again
        final ToopResponseMessage bundleRead = ToopMessageBuilder.parseResponseMessage(archiveInput,
                                                                                       MSDataRequest.getDeserializerFunction(),
                                                                                       ToopDataRequest.getDeserializerFunction(),
                                                                                       MSDataResponse.getDeserializerFunction(),
                                                                                       ToopDataResponse.getDeserializerFunction());
        assertNotNull(bundleRead);

        assertTrue(bundleRead.getMSDataRequest() instanceof MSDataRequest);
        assertTrue(bundleRead.getToopDataRequest() instanceof ToopDataRequest);
        assertTrue(bundleRead.getMSDataResponse() instanceof MSDataResponse);
        assertTrue(bundleRead.getToopDataResponse() instanceof ToopDataResponse);

        final MSDataRequest aMSReq = (MSDataRequest) bundleRead.getMSDataRequest();
        assertEquals(aMSReq.getDestinationCountryCode(), "SE");
        assertEquals(aMSReq.getDocumentTypeID(), "docTypeID1");
        assertEquals(aMSReq.getProcessID(), "procID2");
        assertFalse(aMSReq.isProduction());
        assertEquals(aMSReq.getIdentifier(), "ABC123");
        assertEquals(((ToopDataRequest) bundleRead.getToopDataRequest()).getIdentifier(), "DEF456",
                     "ToopDataRequest did not arrive safely");
        assertEquals(((MSDataResponse) bundleRead.getMSDataResponse()).getIdentifier(), "AAA111",
                     "MSDataResponse did not arrive safely");
        assertEquals(((ToopDataResponse) bundleRead.getToopDataResponse()).getIdentifier(), "BBB222",
                     "MSDataResponse did not arrive safely");
      }
    }
  }

  @Test
  @SuppressFBWarnings ("NP_NONNULL_PARAM_VIOLATION")
  public void testEmpty () throws IOException {
    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
      // No data
      ToopMessageBuilder.createRequestMessage(null, archiveOutput, SH);
      fail("Exception expected");
    } catch (final NullPointerException ex) {
      // Expected
    }

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
      // No data
      ToopMessageBuilder.createResponseMessage(null, null, null, null, archiveOutput, SH);
      fail("Exception expected");
    } catch (final NullPointerException ex) {
      // Expected
    }
  }
}
