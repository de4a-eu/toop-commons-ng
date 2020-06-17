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
package eu.toop.edm.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Source: IdentifierType-CodeList.gc<br>
 *
 * @since 2.0.0-beta5
 * @author Philip Helger
 */
public enum EToopIdentifierType implements IHasID <String>
{
  /** VAT Registration Number */
  VATREGISTRATION ("VATRegistration"),
  /** Tax Reference Number */
  TAXREFERENCE ("TaxReference"),
  /** Directive 2012/17/EU Identifier */
  BUSINESSCODES ("BusinessCodes"),
  /** Legal Entity Identifier (LEI) */
  LEI ("LEI"),
  /** Economic Operator Registration and Identification (EORI) */
  EORI ("EORI"),
  /** System for Exchange of Excise Data (SEED) */
  SEED ("SEED"),
  /** Standard Industrial Classification (SIC) */
  SIC ("SIC"),
  /** EIDAS Identifier */
  EIDAS ("EIDAS");

  private final String m_sID;

  EToopIdentifierType (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static EToopIdentifierType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EToopIdentifierType.class, sID);
  }
}
