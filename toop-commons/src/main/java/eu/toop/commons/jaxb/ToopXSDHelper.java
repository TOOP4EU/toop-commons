/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.commons.jaxb;

import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;

import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.CodeType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IndicatorType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.NumericType;
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
  public static TextType createText (@Nullable final Locale aLanguage, @Nonnull @Nonempty final String sValue) {
    final TextType ret = new TextType ();
    if (aLanguage != null)
      ret.setLanguageID (aLanguage.getLanguage ());
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

  @Nonnull
  public static NumericType createNumeric (@Nonnull final BigDecimal aValue) {
    final NumericType ret = new NumericType ();
    ret.setValue (aValue);
    return ret;
  }
}
