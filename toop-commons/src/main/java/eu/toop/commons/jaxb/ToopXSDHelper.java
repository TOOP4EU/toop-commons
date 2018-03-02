package eu.toop.commons.jaxb;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.CodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IndicatorType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.TextType;

public final class ToopXSDHelper {
  private ToopXSDHelper () {
  }

  @Nonnull
  public static IdentifierType createIdentifier (@Nonnull @Nonempty final String sValue) {
    final IdentifierType ret = new IdentifierType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static IdentifierType createIdentifier (@Nonnull @Nonempty final String sSchemeID,
                                                 @Nonnull @Nonempty final String sValue) {
    final IdentifierType ret = new IdentifierType ();
    ret.setSchemeID (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static IndicatorType createIndicator (@Nonnull @Nonempty final boolean bValue) {
    final IndicatorType ret = new IndicatorType ();
    ret.setValue (bValue);
    return ret;
  }

  @Nonnull
  public static TextType createText (@Nonnull @Nonempty final String sValue) {
    final TextType ret = new TextType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static CodeType createCode (@Nonnull @Nonempty final String sValue) {
    final CodeType ret = new CodeType ();
    ret.setValue (sValue);
    return ret;
  }

  @Nonnull
  public static CodeType createCode (@Nonnull @Nonempty final String sSchemeID,
                                     @Nonnull @Nonempty final String sValue) {
    final CodeType ret = new CodeType ();
    ret.setName (sSchemeID);
    ret.setValue (sValue);
    return ret;
  }
}
