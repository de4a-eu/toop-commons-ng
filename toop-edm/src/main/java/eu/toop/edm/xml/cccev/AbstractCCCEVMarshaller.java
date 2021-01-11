/**
 * Copyright (C) 2018-2021 toop.eu. All rights reserved.
 *
 * This project is dual licensed under Apache License, Version 2.0
 * and the EUPL 1.2.
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
 *
 *  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL
 * (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *         https://joinup.ec.europa.eu/software/page/eupl
 */
package eu.toop.edm.xml.cccev;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.functional.IFunction;
import com.helger.jaxb.GenericJAXBMarshaller;
import com.helger.jaxb.JAXBContextCache;

/**
 * Abstract CCCEV XML marshaller
 *
 * @author Philip Helger
 * @param <T>
 *        Type to be marshalled
 */
public abstract class AbstractCCCEVMarshaller <T> extends GenericJAXBMarshaller <T>
{
  public AbstractCCCEVMarshaller (@Nonnull final Class <T> aType,
                                  @Nonnull final IFunction <? super T, ? extends JAXBElement <T>> aJAXBElementWrapper)
  {
    super (aType, CCCEV.XSDS, aJAXBElementWrapper);
    setNamespaceContext (CCCEVNamespaceContext.getInstance ());
  }

  @Override
  protected JAXBContext getJAXBContext (@Nullable final ClassLoader aClassLoader) throws JAXBException
  {
    final Class <?> [] aClasses = new Class <?> [] { eu.toop.edm.jaxb.cccev.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.cv.agent.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.cv.cac.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.cv.cbc.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.dcatap.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.foaf.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.owl.ObjectFactory.class,
                                                     eu.toop.edm.jaxb.w3.adms.ObjectFactory.class };
    if (isUseContextCache ())
      return JAXBContextCache.getInstance ().getFromCache (new CommonsArrayList <> (aClasses));
    return JAXBContext.newInstance (aClasses);
  }
}
