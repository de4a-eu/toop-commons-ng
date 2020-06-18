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
package eu.toop.edm.response;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.commons.annotation.Nonempty;

import eu.toop.regrep.rim.ObjectRefType;
import eu.toop.regrep.rim.RegistryObjectType;

/**
 * Abstract EDM Response payload provider.
 *
 * @author Philip Helger
 * @since 2.0.0-beta3
 */
@MustImplementEqualsAndHashcode
public interface IEDMResponsePayloadProvider
{
  @Nonnull
  @Nonempty
  String getRegistryObjectID ();

  @Nonnull
  default RegistryObjectType getAsRegistryObject ()
  {
    throw new UnsupportedOperationException ();
  }

  @Nonnull
  default ObjectRefType getAsObjectRef ()
  {
    throw new UnsupportedOperationException ();
  }
}
