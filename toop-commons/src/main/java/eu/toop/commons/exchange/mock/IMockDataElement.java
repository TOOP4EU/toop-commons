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
package eu.toop.commons.exchange.mock;

import java.io.InputStream;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.io.stream.NonBlockingByteArrayInputStream;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IToopDataElement;

/**
 * Base interface for request and response data, for Member State and Toop
 * internal stuff.
 *
 * @author Philip Helger
 */
@Deprecated
@DevelopersNote ("Mock class")
public interface IMockDataElement extends IToopDataElement {
  @Nonnull
  default IMimeType getMimeType () {
    return CMimeType.APPLICATION_OCTET_STREAM;
  }

  @Nonnull
  default InputStream getAsSerializedVersion () {
    final byte[] aData = SerializationHelper.getSerializedByteArray (this);
    return new NonBlockingByteArrayInputStream (aData);
  }
}
