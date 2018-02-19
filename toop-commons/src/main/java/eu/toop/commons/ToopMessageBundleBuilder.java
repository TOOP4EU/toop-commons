package eu.toop.commons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;

import com.helger.asic.AsicReaderFactory;
import com.helger.asic.AsicWriterFactory;
import com.helger.asic.IAsicReader;
import com.helger.asic.IAsicWriter;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.serialize.SerializationHelper;

public class ToopMessageBundleBuilder {
  private static final String ENTRY_NAME_MS_DATA_REQUEST = "MSDataRequest";
  private static final String ENTRY_NAME_MS_DATA_RESPONSE = "MSDataResponse";
  private static final String ENTRY_NAME_TOOP_DATA_REQUEST = "TOOPDataRequest";
  private static final String ENTRY_NAME_TOOP_DATA_RESPONSE = "TOOPDataResponse";

  private final ToopMessageBundle _mb = new ToopMessageBundle();

  public ToopMessageBundleBuilder() {
  }

  @Nonnull
  public ToopMessageBundleBuilder setMSDataRequest(final MSDataRequest msDataRequest) {
    _mb.setMsDataRequest(msDataRequest);
    return this;
  }

  @Nonnull
  public ToopMessageBundleBuilder setMSDataResponse(final MSDataResponse msDataResponse) {
    this._mb.setMsDataResponse(msDataResponse);
    return this;
  }

  @Nonnull
  public ToopMessageBundleBuilder setTOOPDataRequest(final ToopDataRequest toopDataRequest) {
    _mb.setToopDataRequest(toopDataRequest);
    return this;
  }

  @Nonnull
  public ToopMessageBundleBuilder setTOOPDataResponse(final ToopDataResponse toopDataResponse) {
    _mb.setToopDataResponse(toopDataResponse);
    return this;
  }

  @Nonnull
  public ToopMessageBundle sign(final OutputStream archiveOutput, final File keystoreFile,
      final String keystorePassword, final String keyPassword) throws IOException {

    final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
    final IAsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutput);

    if (_mb.getMsDataRequest() != null) {
      final byte[] msDataRequestBytes = SerializationHelper.getSerializedByteArray(_mb.getMsDataRequest());
      asicWriter.add(new NonBlockingByteArrayInputStream(msDataRequestBytes), ENTRY_NAME_MS_DATA_REQUEST,
          CMimeType.APPLICATION_XML);
    }

    if (_mb.getMsDataResponse() != null) {
      final byte[] msDataResponseBytes = SerializationHelper.getSerializedByteArray(_mb.getMsDataResponse());
      asicWriter.add(new NonBlockingByteArrayInputStream(msDataResponseBytes), ENTRY_NAME_MS_DATA_RESPONSE,
          CMimeType.APPLICATION_XML);
    }

    if (_mb.getToopDataRequest() != null) {
      final byte[] toopDataRequestBytes = SerializationHelper.getSerializedByteArray(_mb.getToopDataRequest());
      asicWriter.add(new NonBlockingByteArrayInputStream(toopDataRequestBytes), ENTRY_NAME_TOOP_DATA_REQUEST,
          CMimeType.APPLICATION_XML);
    }

    if (_mb.getToopDataResponse() != null) {
      final byte[] toopDataResponseBytes = SerializationHelper.getSerializedByteArray(_mb.getToopDataResponse());
      asicWriter.add(new NonBlockingByteArrayInputStream(toopDataResponseBytes), ENTRY_NAME_TOOP_DATA_RESPONSE,
          CMimeType.APPLICATION_XML);
    }

    asicWriter.sign(keystoreFile, keystorePassword, keyPassword);
    return _mb;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link ToopMessageBundle}.
   *
   * @param archiveInput
   *          Input stream to read from. May not be <code>null</code>.
   * @return New {@link ToopMessageBundle} every time the method is called.
   * @throws IOException
   *           In case of IO error
   */
  @Nonnull
  @ReturnsMutableObject
  public static ToopMessageBundle parse(@Nonnull @WillClose final InputStream archiveInput) throws IOException {
    ValueEnforcer.notNull(archiveInput, "archiveInput");

    final ToopMessageBundle ret = new ToopMessageBundle();
    try (final IAsicReader asicReader = AsicReaderFactory.newFactory().open(archiveInput)) {
      String entryName;
      while ((entryName = asicReader.getNextFile()) != null) {
        if (entryName.equals(ENTRY_NAME_MS_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final MSDataRequest msDataRequest = SerializationHelper.getDeserializedObject(bos.toByteArray());
            ret.setMsDataRequest(msDataRequest);
          }
        } else if (entryName.equals(ENTRY_NAME_MS_DATA_RESPONSE)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final MSDataResponse msDataResponse = SerializationHelper.getDeserializedObject(bos.toByteArray());
            ret.setMsDataResponse(msDataResponse);
          }
        } else if (entryName.equals(ENTRY_NAME_TOOP_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final ToopDataRequest toopDataRequest = SerializationHelper.getDeserializedObject(bos.toByteArray());
            ret.setToopDataRequest(toopDataRequest);
          }
        } else if (entryName.equals(ENTRY_NAME_TOOP_DATA_RESPONSE)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final ToopDataResponse toopDataResponse = SerializationHelper.getDeserializedObject(bos.toByteArray());
            ret.setToopDataResponse(toopDataResponse);
          }
        }
      }
    }

    return ret;
  }
}
