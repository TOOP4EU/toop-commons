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
package eu.toop.commons.exchange;

import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.Nonnull;

import com.helger.commons.mime.IMimeType;

/**
 * Base interface for request and response data, for Member State and TOOP
 * internal stuff.
 *
 * @author Philip Helger
 */
public interface IToopDataElement extends Serializable {
  /**
   * @return The MIME type to be used for that data element. May not be
   *         <code>null</code>. This must match the output of the input stream.
   * @see #getAsSerializedVersion()
   */
  @Nonnull
  IMimeType getMimeType ();

  /**
   *
   * @return An input stream of <code>this</code> in a serialized version. This
   *         can be done using standard serialization or using other exchange
   *         formats like JSON or XML. This should match the returned MIME type.
   * @see #getMimeType()
   */
  @Nonnull
  InputStream getAsSerializedVersion ();
}
