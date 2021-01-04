/**
 * Copyright (C) 2018-2021 toop.eu
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
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.functional.IFunction;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.jaxb.JAXBContextCache;

import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.edm.xml.cccev.CCCEVNamespaceContext;

/**
 * Abstract DCat AP XML marshaller
 *
 * @author Philip Helger
 * @param <T>
 *        The type to be marshaled
 */
public abstract class AbstractDCatMarshaller <T> extends GenericJAXBMarshaller <T>
{
  public AbstractDCatMarshaller (@Nonnull final Class <T> aType,
                                 @Nonnull final IFunction <? super T, ? extends JAXBElement <T>> aJAXBElementWrapper)
  {
    super (aType, CCCEV.XSDS, aJAXBElementWrapper);
    setNamespaceContext (CCCEVNamespaceContext.getInstance ());
    setIndentString ("  ");
  }

  @Override
  protected JAXBContext getJAXBContext (@Nullable final ClassLoader aClassLoader) throws JAXBException
  {
    final Class <?> [] aClasses = new Class <?> [] { com.helger.xsds.ccts.cct.schemamodule.ObjectFactory.class,
                                                     com.helger.xsds.xlink.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.cv.cbc.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.cv.dt.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.dcatap.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.dcterms.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.foaf.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.rdf.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.spdx.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.vcard.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.w3.adms.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.w3.locn.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.w3.odrl.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.w3.org.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.w3.skos.ObjectFactory.class };

    if (isUseContextCache ())
      return JAXBContextCache.getInstance ().getFromCache (new CommonsArrayList <> (aClasses));
    return JAXBContext.newInstance (aClasses);
  }
}
