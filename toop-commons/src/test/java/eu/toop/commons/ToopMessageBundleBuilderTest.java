package eu.toop.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

public class ToopMessageBundleBuilderTest {
  @Test
  public void testTOOPMessagebundleBuilder() throws IOException {
    final File keystore = new File("src/test/resources/demo-keystore.jks");
    final String keystorePassword = "password";
    final String keystoreKeyPassword = "password";

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream()) {
      new ToopMessageBundleBuilder().setMSDataRequest(new MSDataRequest("ABC123"))
          .setTOOPDataRequest(new ToopDataRequest("DEF456")).setMSDataResponse(new MSDataResponse("AAA111"))
          .setTOOPDataResponse(new ToopDataResponse("BBB222"))
          .sign(archiveOutput, keystore, keystorePassword, keystoreKeyPassword);
      archiveOutput.flush();

      try (final NonBlockingByteArrayInputStream archiveInput = archiveOutput.getAsInputStream()) {
        // Read ASIC again
        final ToopMessageBundle bundleRead = ToopMessageBundleBuilder.parse(archiveInput);

        assertEquals(bundleRead.getMsDataRequest().getIdentifier(), "ABC123", "MSDataRequest did not arrive safely");
        assertEquals(bundleRead.getToopDataRequest().getIdentifier(), "DEF456",
            "ToopDataRequest did not arrive safely");
        assertEquals(bundleRead.getMsDataResponse().getIdentifier(), "AAA111", "MSDataResponse did not arrive safely");
        assertEquals(bundleRead.getToopDataResponse().getIdentifier(), "BBB222",
            "MSDataResponse did not arrive safely");
      }
    }
  }
}
