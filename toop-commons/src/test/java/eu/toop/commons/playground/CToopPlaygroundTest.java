package eu.toop.commons.playground;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.helger.security.keystore.KeyStoreHelper;
import com.helger.security.keystore.LoadedKeyStore;

/**
 * Test class for class {@link CToopPlayground}.
 *
 * @author Philip Helger
 */
public class CToopPlaygroundTest {
  @Test
  public void testBasic () {
    assertTrue (CToopPlayground.PATH_PLAYGROUND_TRUST_STORE.exists ());
    final LoadedKeyStore aKS = KeyStoreHelper.loadKeyStore (CToopPlayground.TYPE_PLAYGROUND_TRUST_STORE,
                                                            CToopPlayground.PATH_PLAYGROUND_TRUST_STORE.getPath (),
                                                            CToopPlayground.PASSWORD_PLAYGROUND_TRUST_STORE);
    assertNotNull (aKS);
  }
}
