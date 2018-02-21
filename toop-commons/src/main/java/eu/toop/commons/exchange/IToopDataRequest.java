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

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;

/**
 * TOOP Demo UI request (DC to DP)
 *
 * @author Philip Helger
 */
public interface IToopDataRequest extends IToopDataElement {
  /**
   * @return The unique request ID that can be used to match the response to the
   *         request.
   */
  @Nonnull
  @Nonempty
  String getRequestID ();
}
