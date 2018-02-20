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

import java.util.function.Function;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.serialize.SerializationHelper;

import eu.toop.commons.exchange.IToopDataResponse;

/**
 * Mock TOOP Demo UI response (DP to DC)
 *
 * @author Anton
 */
@Deprecated
@DevelopersNote ("Mock class")
public class ToopDataResponse implements IToopDataResponse, IMockDataElement {
  private final String _identifier;

  public ToopDataResponse (final String identifier) {
    _identifier = identifier;
  }

  public String getIdentifier () {
    return _identifier;
  }

  @Nonnull
  public static Function<byte[], ToopDataResponse> getDeserializerFunction () {
    return SerializationHelper::getDeserializedObject;
  }
}
