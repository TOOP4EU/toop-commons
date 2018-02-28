package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;

import com.helger.jaxb.builder.JAXBReaderBuilder;

import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;

public class ToopReader<JAXBTYPE> extends JAXBReaderBuilder<JAXBTYPE, ToopReader<JAXBTYPE>> {
  /**
   * Constructor with an arbitrary document type.
   *
   * @param aDocType
   *          Document type to be used. May not be <code>null</code>.
   * @param aImplClass
   *          Implementation class to use. May not be <code>null</code>.
   */
  public ToopReader (@Nonnull final EToopXMLDocumentType aDocType, @Nonnull final Class<JAXBTYPE> aImplClass) {
    super (aDocType, aImplClass);
  }

  /**
   * Create a reader builder for {@link TDETOOPDataRequestType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopReader<TDETOOPDataRequestType> dataRequest () {
    return new ToopReader<> (EToopXMLDocumentType.DATA_REQUEST, TDETOOPDataRequestType.class);
  }

  /**
   * Create a reader builder for {@link TDETOOPDataResponseType}.
   *
   * @return The builder and never <code>null</code>
   */
  @Nonnull
  public static ToopReader<TDETOOPDataResponseType> dataResponse () {
    return new ToopReader<> (EToopXMLDocumentType.DATA_RESPONSE, TDETOOPDataResponseType.class);
  }
}
