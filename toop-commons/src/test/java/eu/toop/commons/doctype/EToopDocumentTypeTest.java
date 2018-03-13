package eu.toop.commons.doctype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class EToopDocumentTypeTest {
  @Test
  public void testBasic () {
    for (final EToopDocumentType e : EToopDocumentType.values ()) {
      assertEquals (EToopDocumentType.DOC_TYPE_SCHEME, e.getScheme ());
      assertSame (e, EToopDocumentType.getFromIDOrNull (e.getScheme (), e.getValue ()));
      // Each doc type must be request OR response
      assertTrue (e.getMatchingRequestDocumentType () != null || e.getMatchingResponseDocumentType () != null);
    }
  }
}
