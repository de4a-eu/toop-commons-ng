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
import com.helger.xml.namespace.MapBasedNamespaceContext;

import eu.toop.edm.xml.cccev.CCCEVNamespaceContext;

/**
 * XML Namespace context for DCAT
 *
 * @author yerlibilgin
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
    addMappings (CCCEVNamespaceContext.getInstance ());
    addMapping ("dcat", "http://data.europa.eu/r5r/");
    addMapping ("dct", "http://purl.org/dc/terms/");
  }

  @Nonnull
  public static DCatNamespaceContext getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }
}
