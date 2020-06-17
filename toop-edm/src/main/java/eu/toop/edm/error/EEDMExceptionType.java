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
package eu.toop.edm.error;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import eu.toop.regrep.query.QueryExceptionType;
import eu.toop.regrep.rs.AuthenticationExceptionType;
import eu.toop.regrep.rs.AuthorizationExceptionType;
import eu.toop.regrep.rs.InvalidRequestExceptionType;
import eu.toop.regrep.rs.ObjectExistsExceptionType;
import eu.toop.regrep.rs.ObjectNotFoundExceptionType;
import eu.toop.regrep.rs.QuotaExceededExceptionType;
import eu.toop.regrep.rs.ReferencesExistExceptionType;
import eu.toop.regrep.rs.RegistryExceptionType;
import eu.toop.regrep.rs.TimeoutExceptionType;
import eu.toop.regrep.rs.UnresolvedReferenceExceptionType;
import eu.toop.regrep.rs.UnsupportedCapabilityExceptionType;

/**
 * Contains the different possible exception types.
 *
 * @author Philip Helger
 */
public enum EEDMExceptionType
{
  /**
   * Generated when a client sends a request with authentication credentials and
   * the authentication fails for any reason.
   */
  AUTHENTICATION (AuthenticationExceptionType.class, AuthenticationExceptionType::new),
  /**
   * Generated when a client sends a request to the server for which it is not
   * authorized.
   */
  AUTHORIZATION (AuthorizationExceptionType.class, AuthorizationExceptionType::new),
  /**
   * Generated when a client sends a request that is syntactically or
   * semantically invalid.
   */
  INVALID_REQUEST (InvalidRequestExceptionType.class, InvalidRequestExceptionType::new),
  /**
   * Generated when a SubmitObjectsRequest attempts to create an object with the
   * same id as an existing object and the mode is “CreateOnly”.
   */
  OBJECT_EXISTS (ObjectExistsExceptionType.class, ObjectExistsExceptionType::new),
  /**
   * Generated when a QueryRequest expects an object but it is not found in
   * server.
   */
  OBJECT_NOT_FOUND (ObjectNotFoundExceptionType.class, ObjectNotFoundExceptionType::new),
  /**
   * Generated when a a request exceeds a server specific quota for the client.
   */
  QUOTA_EXCEEDED (QuotaExceededExceptionType.class, QuotaExceededExceptionType::new),
  /**
   * Generated when a RemoveObjectRequest attempts to remove a RegistryObject
   * while references to it still exist.
   */
  REFERENCES_EXIST (ReferencesExistExceptionType.class, ReferencesExistExceptionType::new),
  /**
   * Generated when a the processing of a request exceeds a server specific
   * timeout period.
   */
  TIMEOUT (TimeoutExceptionType.class, TimeoutExceptionType::new),
  /**
   * Generated when a request references an object that cannot be resolved
   * within the request or to an existing object in the server.
   */
  UNRESOLVED_REFERENCE (UnresolvedReferenceExceptionType.class, UnresolvedReferenceExceptionType::new),
  /**
   * Generated when when a request attempts to use an optional feature or
   * capability that the server does not support.
   */
  UNSUPPORTED_CAPABILITY (UnsupportedCapabilityExceptionType.class, UnsupportedCapabilityExceptionType::new),
  /**
   * Generated when the query syntax or semantics was invalid. Client must fix
   * the query syntax or semantic error and re-submit the query
   */
  QUERY (QueryExceptionType.class, QueryExceptionType::new);

  private final Class <? extends RegistryExceptionType> m_aClass;
  private final Supplier <? extends RegistryExceptionType> m_aInvoker;

  <T extends RegistryExceptionType> EEDMExceptionType (@Nonnull final Class <T> aClass, @Nonnull final Supplier <T> aInvoker)
  {
    m_aClass = aClass;
    m_aInvoker = aInvoker;
  }

  /**
   * @return Create a new RegRep JAXB object. Never <code>null</code>
   */
  @Nonnull
  public RegistryExceptionType invoke ()
  {
    return m_aInvoker.get ();
  }

  /**
   * Find the exception type enum entry matching the provided class name.
   * 
   * @param aClass
   *        The class name to search. May be <code>null</code>.
   * @return <code>null</code> if none was found.
   */
  @Nullable
  public static EEDMExceptionType getFromClassOrNull (@Nullable final Class <? extends RegistryExceptionType> aClass)
  {
    if (aClass != null)
      for (final EEDMExceptionType e : values ())
        if (e.m_aClass.isAssignableFrom (aClass))
          return e;
    return null;
  }
}
