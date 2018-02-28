package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;

import com.helger.jaxb.builder.JAXBWriterBuilder;

import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;

public class ToopWriter<JAXBTYPE> extends JAXBWriterBuilder<JAXBTYPE, ToopWriter<JAXBTYPE>> {
  /**
   * Constructor with an arbitrary document type.
   *
   * @param aDocType
   *          Document type to be used. May not be <code>null</code>.
   */
  public ToopWriter (@Nonnull final EToopXMLDocumentType aDocType) {
    super (aDocType);
  }

  /**
   * Create a writer builder for {@link TDETOOPDataRequestType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter<TDETOOPDataRequestType> dataRequest () {
    final ToopWriter<TDETOOPDataRequestType> ret = new ToopWriter<> (EToopXMLDocumentType.DATA_REQUEST);
    ret.setFormattedOutput (true);
    return ret;
  }

  /**
   * Create a writer builder for {@link TDETOOPDataResponseType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopWriter<TDETOOPDataResponseType> dataResponse () {
    final ToopWriter<TDETOOPDataResponseType> ret = new ToopWriter<> (EToopXMLDocumentType.DATA_RESPONSE);
    ret.setFormattedOutput (true);
    return ret;
  }
}
