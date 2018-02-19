package eu.toop.commons.exchange.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.Immutable;

import com.helger.asic.AsicReaderFactory;
import com.helger.asic.AsicWriterFactory;
import com.helger.asic.IAsicReader;
import com.helger.asic.IAsicWriter;
import com.helger.asic.SignatureHelper;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

import eu.toop.commons.exchange.IMSDataRequest;
import eu.toop.commons.exchange.IMSDataResponse;
import eu.toop.commons.exchange.IToopDataRequest;
import eu.toop.commons.exchange.IToopDataResponse;

@Immutable
public final class ToopMessageBuilder {
  public static final String ENTRY_NAME_MS_DATA_REQUEST = "MSDataRequest";
  public static final String ENTRY_NAME_TOOP_DATA_REQUEST = "TOOPDataRequest";
  public static final String ENTRY_NAME_MS_DATA_RESPONSE = "MSDataResponse";
  public static final String ENTRY_NAME_TOOP_DATA_RESPONSE = "TOOPDataResponse";

  private ToopMessageBuilder() {
  }

  public static void createRequestMessage(@Nullable final IMSDataRequest msDataRequest,
      @Nullable final IToopDataRequest toopDataRequest, @Nonnull final OutputStream aOS,
      @Nonnull final SignatureHelper aSigHelper) throws IOException, IllegalStateException {
    ValueEnforcer.notNull(aOS, "ArchiveOutput");
    ValueEnforcer.notNull(aSigHelper, "SignatureHelper");

    final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
    final IAsicWriter asicWriter = asicWriterFactory.newContainer(aOS);
    int nItems = 0;

    if (msDataRequest != null) {
      asicWriter.add(msDataRequest.getAsSerializedVersion(), ENTRY_NAME_MS_DATA_REQUEST, msDataRequest.getMimeType());
      ++nItems;
    }

    if (toopDataRequest != null) {
      asicWriter.add(toopDataRequest.getAsSerializedVersion(), ENTRY_NAME_TOOP_DATA_REQUEST,
          toopDataRequest.getMimeType());
      ++nItems;
    }

    if (nItems == 0)
      throw new IllegalStateException("No request data present!");

    asicWriter.sign(aSigHelper);
  }

  public static void createResponseMessage(@Nullable final IMSDataRequest msDataRequest,
      @Nullable final IToopDataRequest toopDataRequest, @Nullable final IMSDataResponse msDataResponse,
      @Nullable final IToopDataResponse toopDataResponse, @Nonnull final OutputStream aOS,
      @Nonnull final SignatureHelper aSigHelper) throws IOException, IllegalStateException {
    ValueEnforcer.notNull(aOS, "ArchiveOutput");
    ValueEnforcer.notNull(aSigHelper, "SignatureHelper");

    final AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();
    final IAsicWriter asicWriter = asicWriterFactory.newContainer(aOS);
    int nItems = 0;

    if (msDataRequest != null) {
      asicWriter.add(msDataRequest.getAsSerializedVersion(), ENTRY_NAME_MS_DATA_REQUEST, msDataRequest.getMimeType());
      ++nItems;
    }

    if (toopDataRequest != null) {
      asicWriter.add(toopDataRequest.getAsSerializedVersion(), ENTRY_NAME_TOOP_DATA_REQUEST,
          toopDataRequest.getMimeType());
      ++nItems;
    }

    if (msDataResponse != null) {
      asicWriter.add(msDataResponse.getAsSerializedVersion(), ENTRY_NAME_MS_DATA_RESPONSE,
          msDataResponse.getMimeType());
      ++nItems;
    }

    if (toopDataResponse != null) {
      asicWriter.add(toopDataResponse.getAsSerializedVersion(), ENTRY_NAME_TOOP_DATA_RESPONSE,
          toopDataResponse.getMimeType());
      ++nItems;
    }

    if (nItems == 0)
      throw new IllegalStateException("No data present!");

    asicWriter.sign(aSigHelper);
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link ToopRequestMessage}.
   *
   * @param archiveInput
   *          Input stream to read from. May not be <code>null</code>.
   * @param aDecryptorMSDataRequest
   *          The callback function that is invoked for the read byte[] to convert
   *          it to the implementation object. May not be <code>null</code>.
   * @param aDecryptorToopDataRequest
   *          The callback function that is invoked for the read byte[] to convert
   *          it to the implementation object. May not be <code>null</code>.
   * @return New {@link ToopRequestMessage} every time the method is called.
   * @throws IOException
   *           In case of IO error
   */
  @Nonnull
  @ReturnsMutableObject
  public static ToopRequestMessage parseRequestMessage(@Nonnull @WillClose final InputStream archiveInput,
      @Nonnull final Function<byte[], ? extends IMSDataRequest> aDecryptorMSDataRequest,
      @Nonnull final Function<byte[], ? extends IToopDataRequest> aDecryptorToopDataRequest) throws IOException {
    ValueEnforcer.notNull(archiveInput, "archiveInput");
    ValueEnforcer.notNull(aDecryptorMSDataRequest, "aDecryptorMSDataRequest");
    ValueEnforcer.notNull(aDecryptorToopDataRequest, "aDecryptorToopDataRequest");

    final ToopRequestMessage ret = new ToopRequestMessage();
    try (final IAsicReader asicReader = AsicReaderFactory.newFactory().open(archiveInput)) {
      String entryName;
      while ((entryName = asicReader.getNextFile()) != null) {
        if (entryName.equals(ENTRY_NAME_MS_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final IMSDataRequest msDataRequest = aDecryptorMSDataRequest.apply(bos.toByteArray());
            ret.setMSDataRequest(msDataRequest);
          }
        } else if (entryName.equals(ENTRY_NAME_TOOP_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final IToopDataRequest toopDataRequest = aDecryptorToopDataRequest.apply(bos.toByteArray());
            ret.setToopDataRequest(toopDataRequest);
          }
        }
      }
    }

    return ret;
  }

  /**
   * Parse the given InputStream as an ASiC container and return the contained
   * {@link ToopResponseMessage}.
   *
   * @param archiveInput
   *          Input stream to read from. May not be <code>null</code>.
   * @param aDecryptorMSDataRequest
   *          The callback function that is invoked for the read byte[] to convert
   *          it to the implementation object. May not be <code>null</code>.
   * @param aDecryptorToopDataRequest
   *          The callback function that is invoked for the read byte[] to convert
   *          it to the implementation object. May not be <code>null</code>.
   * @param aDecryptorMSDataResponse
   *          The callback function that is invoked for the read byte[] to convert
   *          it to the implementation object. May not be <code>null</code>.
   * @param aDecryptorToopDataResponse
   *          The callback function that is invoked for the read byte[] to convert
   *          it to the implementation object. May not be <code>null</code>.
   * @return New {@link ToopResponseMessage} every time the method is called.
   * @throws IOException
   *           In case of IO error
   */
  @Nonnull
  @ReturnsMutableObject
  public static ToopResponseMessage parseResponseMessage(@Nonnull @WillClose final InputStream archiveInput,
      @Nonnull final Function<byte[], ? extends IMSDataRequest> aDecryptorMSDataRequest,
      @Nonnull final Function<byte[], ? extends IToopDataRequest> aDecryptorToopDataRequest,
      @Nonnull final Function<byte[], ? extends IMSDataResponse> aDecryptorMSDataResponse,
      @Nonnull final Function<byte[], ? extends IToopDataResponse> aDecryptorToopDataResponse) throws IOException {
    ValueEnforcer.notNull(archiveInput, "archiveInput");
    ValueEnforcer.notNull(aDecryptorMSDataRequest, "aDecryptorMSDataRequest");
    ValueEnforcer.notNull(aDecryptorToopDataRequest, "aDecryptorToopDataRequest");
    ValueEnforcer.notNull(aDecryptorMSDataResponse, "aDecryptorMSDataResponse");
    ValueEnforcer.notNull(aDecryptorToopDataResponse, "aDecryptorToopDataResponse");

    final ToopResponseMessage ret = new ToopResponseMessage();
    try (final IAsicReader asicReader = AsicReaderFactory.newFactory().open(archiveInput)) {
      String entryName;
      while ((entryName = asicReader.getNextFile()) != null) {
        if (entryName.equals(ENTRY_NAME_MS_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final IMSDataRequest msDataRequest = aDecryptorMSDataRequest.apply(bos.toByteArray());
            ret.setMSDataRequest(msDataRequest);
          }
        } else if (entryName.equals(ENTRY_NAME_TOOP_DATA_REQUEST)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final IToopDataRequest toopDataRequest = aDecryptorToopDataRequest.apply(bos.toByteArray());
            ret.setToopDataRequest(toopDataRequest);
          }
        } else if (entryName.equals(ENTRY_NAME_MS_DATA_RESPONSE)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final IMSDataResponse msDataResponse = aDecryptorMSDataResponse.apply(bos.toByteArray());
            ret.setMSDataResponse(msDataResponse);
          }
        } else if (entryName.equals(ENTRY_NAME_TOOP_DATA_RESPONSE)) {
          try (final NonBlockingByteArrayOutputStream bos = new NonBlockingByteArrayOutputStream()) {
            asicReader.writeFile(bos);
            final IToopDataResponse toopDataResponse = aDecryptorToopDataResponse.apply(bos.toByteArray());
            ret.setToopDataResponse(toopDataResponse);
          }
        }
      }
    }

    return ret;
  }
}
