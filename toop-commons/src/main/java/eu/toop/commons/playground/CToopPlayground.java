package eu.toop.commons.playground;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.security.keystore.EKeyStoreType;
import com.helger.security.keystore.IKeyStoreType;

/**
 * Playground constants
 *
 * @author Philip Helger
 */
public class CToopPlayground {
  public static final IKeyStoreType TYPE_PLAYGROUND_TRUST_STORE = EKeyStoreType.JKS;
  public static final IReadableResource PATH_PLAYGROUND_TRUST_STORE = new ClassPathResource ("/truststore/playground-truststore.jks");
  public static final String PASSWORD_PLAYGROUND_TRUST_STORE = "toop4eu";

  private CToopPlayground () {
  }
}
