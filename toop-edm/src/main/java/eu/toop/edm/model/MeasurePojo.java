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
package eu.toop.edm.model;

import java.math.BigDecimal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.math.MathHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.cv.cbc.MeasureType;

/**
 * Representation of an "Measure" value.
 *
 * @author Philip Helger
 */
@Immutable
public class MeasurePojo
{
  private final BigDecimal m_aValue;
  private final String m_sUnitCode;

  public MeasurePojo (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
  {
    m_aValue = aValue;
    m_sUnitCode = sUnitCode;
  }

  @Nullable
  public final BigDecimal getValue ()
  {
    return m_aValue;
  }

  @Nullable
  public final String getUnitCode ()
  {
    return m_sUnitCode;
  }

  @Nonnull
  public MeasureType getAsMeasure ()
  {
    final MeasureType ret = new MeasureType ();
    ret.setValue (m_aValue);
    ret.setUnitCode (m_sUnitCode);
    return ret;
  }

  @Nullable
  public String getAsString ()
  {
    if (m_aValue != null)
    {
      if (m_sUnitCode != null)
        return m_aValue.toString () + " " + m_sUnitCode;
      return m_aValue.toString ();
    }
    return m_sUnitCode;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final MeasurePojo rhs = (MeasurePojo) o;
    return EqualsHelper.equals (m_aValue, rhs.m_aValue) && EqualsHelper.equals (m_sUnitCode, rhs.m_sUnitCode);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).append (m_sUnitCode).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Value", m_aValue).append ("UnitCode", m_sUnitCode).getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final MeasureType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
      ret.value (a.getValue ()).unitCode (a.getUnitCode ());
    return ret;
  }

  /**
   * A builder for this class
   *
   * @author Philip Helger
   */
  @NotThreadSafe
  public static class Builder
  {
    private BigDecimal m_aValue;
    private String m_sUnitCode;

    public Builder ()
    {}

    public Builder value (@Nonnull final long n)
    {
      return value (MathHelper.toBigDecimal (n));
    }

    @Nonnull
    public Builder value (@Nonnull final double d)
    {
      return value (MathHelper.toBigDecimal (d));
    }

    @Nonnull
    public Builder value (@Nullable final BigDecimal a)
    {
      m_aValue = a;
      return this;
    }

    @Nonnull
    public Builder unitCode (@Nullable final String s)
    {
      m_sUnitCode = s;
      return this;
    }

    @Nonnull
    public MeasurePojo build ()
    {
      return new MeasurePojo (m_aValue, m_sUnitCode);
    }
  }
}
