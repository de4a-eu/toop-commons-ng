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
package eu.toop.edm.schematron;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.io.resource.ClassPathResource;

/**
 * TOOP EDM Schematron constants
 *
 * @author Philip Helger
 * @since 2.0.0-beta5
 */
@Immutable
public final class CEDMSchematron
{
  @Nonnull
  private static ClassLoader _getCL ()
  {
    return CEDMSchematron.class.getClassLoader ();
  }

  public static final ClassPathResource TOOP_IS_REQUEST = new ClassPathResource ("200/TOOP_is_request.xslt", _getCL ());
  public static final ClassPathResource TOOP_IS_RESPONSE = new ClassPathResource ("200/TOOP_is_response.xslt", _getCL ());
  public static final ClassPathResource TOOP_IS_ERROR_RESPONSE = new ClassPathResource ("200/TOOP_is_error.xslt", _getCL ());

  /**
   * The resource with the rules. Important: this Schematron requires additional
   * code lists in a relative directory!
   */
  public static final ClassPathResource TOOP_EDM2_XSLT = new ClassPathResource ("200/TOOP_EDM.xslt", _getCL ());

  /**
   * The resource with the rules. Important: this Schematron requires additional
   * code lists in a relative directory!
   */
  public static final ClassPathResource TOOP_BUSINESS_RULES_XSLT = new ClassPathResource ("200/TOOP_BUSINESS_RULES.xslt", _getCL ());

  private CEDMSchematron ()
  {}
}
