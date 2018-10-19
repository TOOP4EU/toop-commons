package eu.toop.commons.schematron.supplementary.tools;

import java.io.File;
import java.util.Locale;

import com.helger.commons.regex.RegExHelper;
import com.helger.genericode.Genericode10Helper;
import com.helger.genericode.builder.GenericodeReader;
import com.helger.genericode.v10.CodeListDocument;
import com.helger.genericode.v10.Row;

/**
 * Extract error code enum content from Genericode file
 * 
 * @author Philip Helger
 */
public final class MainCreateErrorCodeJavaCode
{
  public static void main (final String [] args)
  {
    final CodeListDocument aCLD = GenericodeReader.gc10CodeList ()
                                                  .read (new File ("src/main/resources/codelists/gc/ErrorCode-CodeList.gc"));
    final StringBuilder aSB = new StringBuilder ();
    for (final Row aRow : aCLD.getSimpleCodeList ().getRow ())
    {
      final String sID = Genericode10Helper.getRowValue (aRow, "code");
      final String sName = Genericode10Helper.getRowValue (aRow, "name-Value");
      if (sName != null)
        aSB.append ("/** ").append (sName).append (" */\n");
      if (sID != null)
        aSB.append (RegExHelper.getAsIdentifier (sID.toUpperCase (Locale.US)))
           .append (" (\"")
           .append (sID)
           .append ("\"),\n");
    }
    System.out.println (aSB.toString ());
  }
}
