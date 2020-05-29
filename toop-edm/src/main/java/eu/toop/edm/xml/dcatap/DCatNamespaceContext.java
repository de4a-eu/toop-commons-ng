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
package eu.toop.edm.xml.dcatap;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Singleton;
import com.helger.ubl21.UBL21NamespaceContext;
import com.helger.xml.namespace.MapBasedNamespaceContext;
import com.helger.xsds.ccts.cct.schemamodule.CCCTS;
import com.helger.xsds.xlink.CXLink;

/**
 * XML Namespace context for DCAT. Depends on CGAV stuff.
 *
 * @author yerlibilgin
 * @author Philip Helger
 */
@Singleton
public class DCatNamespaceContext extends MapBasedNamespaceContext
{
  private static final class SingletonHolder
  {
    static final DCatNamespaceContext s_aInstance = new DCatNamespaceContext ();
  }

  protected DCatNamespaceContext ()
  {
    addMappings (UBL21NamespaceContext.getInstance ());
    addMapping (CCCTS.DEFAULT_PREFIX, CCCTS.NAMESPACE_URI);
    addMapping (CXLink.DEFAULT_PREFIX, CXLink.NAMESPACE_URI);
    addMapping ("foaf", "http://xmlns.com/foaf/0.1/");
    addMapping ("locn", "http://www.w3.org/ns/locn#");
    addMapping ("skos", "http://www.w3.org/2004/02/skos/core#");
    addMapping ("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    addMapping ("dct", "http://purl.org/dc/terms/");
    addMapping ("csdt", "https://semic.org/sa/cv/common/dataTypes-2.0.0#");
    addMapping ("csbc", "https://semic.org/sa/cv/common/cbc-2.0.0#");
    addMapping ("adms", "http://www.w3.org/ns/adms#");
    addMapping ("odrl", "http://www.w3.org/ns/odrl/2/");
    addMapping ("spdx", "spdx:xsd::1.0");
    addMapping ("vcard", "http://www.w3.org/2001/vcard-rdf/3.0#");
    addMapping ("dcat", "http://data.europa.eu/r5r/");
  }

  @Nonnull
  public static DCatNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
