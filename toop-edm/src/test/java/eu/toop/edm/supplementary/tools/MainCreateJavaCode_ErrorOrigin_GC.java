/**
 * Copyright (C) 2018-2020 toop.eu
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
package eu.toop.edm.supplementary.tools;

import java.io.File;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.regex.RegExHelper;
import com.helger.genericode.Genericode10Helper;
import com.helger.genericode.builder.GenericodeReader;
import com.helger.genericode.v10.CodeListDocument;
import com.helger.genericode.v10.Row;

import eu.toop.edm.error.EToopErrorOrigin;

/**
 * Extract {@link EToopErrorOrigin} enum content from Genericode file
 *
 * @author Philip Helger
 */
public final class MainCreateJavaCode_ErrorOrigin_GC
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainCreateJavaCode_ErrorOrigin_GC.class);

  @Nonnull
  private static String _addUnderscore (@Nonnull final String s)
  {
    final StringBuilder ret = new StringBuilder (s.length () * 2);
    int i = 0;
    for (final char c : s.toCharArray ())
    {
      if (Character.isUpperCase (c) && i > 0)
        ret.append ('_');
      ret.append (c);
      i++;
    }
    return ret.toString ();
  }

  public static void main (final String [] args)
  {
    final CodeListDocument aCLD = GenericodeReader.gc10CodeList ()
                                                  .read (new File ("src/main/resources/codelist/toop/ErrorOrigin-CodeList.gc"));
    final StringBuilder aSB = new StringBuilder ();
    for (final Row aRow : aCLD.getSimpleCodeList ().getRow ())
    {
      final String sID = Genericode10Helper.getRowValue (aRow, "code");
      final String sName = Genericode10Helper.getRowValue (aRow, "name-en");
      if (sName != null)
        aSB.append ("/** ").append (sName).append (" */\n");
      if (sID != null)
        aSB.append (RegExHelper.getAsIdentifier (_addUnderscore (sID).toUpperCase (Locale.US)))
           .append (" (\"")
           .append (sID)
           .append ("\"),\n");
    }
    LOGGER.info (aSB.toString ());
  }
}
