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
package eu.toop.commons.playground;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.security.keystore.KeyStoreHelper;
import com.helger.security.keystore.LoadedKeyStore;

/**
 * Test class for class {@link CToopPlayground}.
 *
 * @author Philip Helger
 */
public final class CToopPlaygroundTest
{
  @Test
  public void testLoadKeyStores ()
  {
    assertTrue (CToopPlayground.PATH_PLAYGROUND_TRUST_STORE.exists ());
    final LoadedKeyStore aKS = KeyStoreHelper.loadKeyStore (CToopPlayground.TYPE_PLAYGROUND_TRUST_STORE,
                                                            CToopPlayground.PATH_PLAYGROUND_TRUST_STORE.getPath (),
                                                            CToopPlayground.PASSWORD_PLAYGROUND_TRUST_STORE);
    assertNotNull (aKS);
  }
}
