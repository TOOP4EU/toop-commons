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
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Source: ErrorCode-CodeList.gc
 *
 * @author Philip Helger
 *
 */
public enum EToopErrorCode implements IHasID<String> {
  GENERIC ("GEN"),
  IF_001 ("IF-001"),
  IF_002 ("IF-002"),
  SM_001 ("SM-001"),
  SM_002a ("SM-002a"),
  SM_002b ("SM-002b"),
  DD_001 ("DD-001"),
  DD_002 ("DD-002"),
  DD_003 ("DD-003"),
  DD_004 ("DD-004"),
  TC_001 ("TC-001"),
  ME_001 ("ME-001"),
  ME_002 ("ME-002"),
  ME_003 ("ME-003"),
  ME_004 ("ME-004");

  private final String m_sID;

  private EToopErrorCode (@Nonnull @Nonempty final String sID) {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID () {
    return m_sID;
  }

  @Nullable
  public static EToopErrorCode getFromIDOrNull (@Nullable final String sID) {
    return EnumHelper.getFromIDOrNull (EToopErrorCode.class, sID);
  }
}
