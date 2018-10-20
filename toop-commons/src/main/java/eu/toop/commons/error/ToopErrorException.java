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
package eu.toop.commons.error;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ToopErrorException extends Exception
{
  private final EToopErrorCode m_eCode;

  public ToopErrorException (@Nonnull final String sMsg, @Nonnull final EToopErrorCode eCode)
  {
    this (sMsg, null, eCode);
  }

  public ToopErrorException (@Nonnull final String sMsg,
                             @Nullable final Throwable aCause,
                             @Nonnull final EToopErrorCode eCode)
  {
    super (sMsg, aCause);
    m_eCode = eCode;
  }

  @Nonnull
  public EToopErrorCode getErrorCode ()
  {
    return m_eCode;
  }
}
