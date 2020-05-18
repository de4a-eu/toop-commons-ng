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

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.slot.SlotErrorOrigin;
import eu.toop.edm.slot.SlotTimestamp;
import eu.toop.regrep.rim.DateTimeValueType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.StringValueType;
import eu.toop.regrep.rim.ValueType;
import eu.toop.regrep.rs.RegistryExceptionType;

/**
 * Build TOOP EDM exceptions to be used in TOOP error responses.
 *
 * @author Philip Helger
 */
public class EDMExceptionPojo
{
  private final EEDMExceptionType m_eExceptionType;
  private final EToopErrorSeverity m_eSeverity;
  private final String m_sErrorMessage;
  private final String m_sErrorDetail;
  private final String m_sErrorCode;
  private final LocalDateTime m_aTimestamp;
  private final String m_sErrorOrigin;

  protected EDMExceptionPojo (@Nonnull final EEDMExceptionType eExceptionType,
                              @Nonnull final EToopErrorSeverity eSeverity,
                              @Nonnull final String sErrorMessage,
                              @Nullable final String sErrorDetail,
                              @Nullable final String sErrorCode,
                              @Nonnull final LocalDateTime aTimestamp,
                              @Nullable final String sErrorOrigin)
  {
    ValueEnforcer.notNull (eExceptionType, "ExceptionType");
    ValueEnforcer.notNull (eSeverity, "Severity");
    ValueEnforcer.notNull (sErrorMessage, "ErrorMessage");
    ValueEnforcer.notNull (aTimestamp, "Timestamp");

    m_eExceptionType = eExceptionType;
    m_eSeverity = eSeverity;
    m_sErrorMessage = sErrorMessage;
    m_sErrorDetail = sErrorDetail;
    m_sErrorCode = sErrorCode;
    m_aTimestamp = aTimestamp;
    m_sErrorOrigin = sErrorOrigin;
  }

  @Nonnull
  public final EEDMExceptionType getExceptionType ()
  {
    return m_eExceptionType;
  }

  @Nonnull
  public final EToopErrorSeverity getSeverity ()
  {
    return m_eSeverity;
  }

  @Nonnull
  public final String getErrorMessage ()
  {
    return m_sErrorMessage;
  }

  @Nullable
  public final String getErrorDetails ()
  {
    return m_sErrorDetail;
  }

  @Nullable
  public final String getErrorCode ()
  {
    return m_sErrorCode;
  }

  @Nonnull
  public final LocalDateTime getTimestamp ()
  {
    return m_aTimestamp;
  }

  @Nullable
  public final String getErrorOrigin ()
  {
    return m_sErrorOrigin;
  }

  @Nonnull
  public RegistryExceptionType getAsRegistryException ()
  {
    // Dynamic invocation
    final RegistryExceptionType ret = m_eExceptionType.invoke ();
    ret.setSeverity (m_eSeverity.getID ());
    ret.setMessage (m_sErrorMessage);
    ret.setDetail (m_sErrorDetail);
    ret.setCode (m_sErrorCode);
    if (m_aTimestamp != null)
      ret.addSlot (new SlotTimestamp (m_aTimestamp).createSlot ());
    if (StringHelper.hasText (m_sErrorOrigin))
      ret.addSlot (new SlotErrorOrigin (m_sErrorOrigin).createSlot ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final EDMExceptionPojo rhs = (EDMExceptionPojo) o;
    return EqualsHelper.equals (m_eExceptionType, rhs.m_eExceptionType) &&
           EqualsHelper.equals (m_eSeverity, rhs.m_eSeverity) &&
           EqualsHelper.equals (m_sErrorMessage, rhs.m_sErrorMessage) &&
           EqualsHelper.equals (m_sErrorDetail, rhs.m_sErrorDetail) &&
           EqualsHelper.equals (m_sErrorCode, rhs.m_sErrorCode) &&
           EqualsHelper.equals (m_aTimestamp, rhs.m_aTimestamp) &&
           EqualsHelper.equals (m_sErrorOrigin, rhs.m_sErrorOrigin);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eExceptionType)
                                       .append (m_eSeverity)
                                       .append (m_sErrorMessage)
                                       .append (m_sErrorDetail)
                                       .append (m_sErrorCode)
                                       .append (m_aTimestamp)
                                       .append (m_sErrorOrigin)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ExceptionType", m_eExceptionType)
                                       .append ("Severity", m_eSeverity)
                                       .append ("ErrorMessage", m_sErrorMessage)
                                       .append ("ErrorDetail", m_sErrorDetail)
                                       .append ("ErrorCode", m_sErrorCode)
                                       .append ("Timestamp", m_aTimestamp)
                                       .append ("ErrorOrigin", m_sErrorOrigin)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final Builder aBuilder)
  {
    final String sName = aSlot.getName ();
    final ValueType aSlotValue = aSlot.getSlotValue ();
    switch (sName)
    {
      case SlotTimestamp.NAME:
        if (aSlotValue instanceof DateTimeValueType)
        {
          final XMLGregorianCalendar aValue = ((DateTimeValueType) aSlotValue).getValue ();
          aBuilder.timestamp (aValue);
        }
        break;
      case SlotErrorOrigin.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.errorOrigin (sValue);
        }
        break;
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static Builder builder (@Nullable final RegistryExceptionType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.exceptionType (a.getClass ())
         .severity (EToopErrorSeverity.getFromIDOrNull (a.getSeverity ()))
         .errorMessage (a.getMessage ())
         .errorDetail (a.getDetail ())
         .errorCode (a.getCode ());
      for (final SlotType aSlot : a.getSlot ())
        _applySlots (aSlot, ret);
    }
    return ret;
  }

  /**
   * Builder class for {@link EDMExceptionPojo} objects.
   *
   * @author Philip Helger
   * @since 2.0.0-beta3
   */
  public static class Builder
  {
    private EEDMExceptionType m_eExceptionType;
    private EToopErrorSeverity m_eSeverity;
    private String m_sErrorMessage;
    private String m_sErrorDetail;
    private String m_sErrorCode;
    private LocalDateTime m_aTimestamp;
    private String m_sErrorOrigin;

    protected Builder ()
    {}

    @Nonnull
    public Builder exceptionType (@Nullable final Class <? extends RegistryExceptionType> a)
    {
      return exceptionType (a == null ? null : EEDMExceptionType.getFromClassOrNull (a));
    }

    @Nonnull
    public Builder exceptionType (@Nullable final EEDMExceptionType e)
    {
      m_eExceptionType = e;
      return this;
    }

    @Nonnull
    public Builder severityFailure ()
    {
      return severity (EToopErrorSeverity.FAILURE);
    }

    @Nonnull
    public Builder severity (@Nullable final EToopErrorSeverity e)
    {
      m_eSeverity = e;
      return this;
    }

    @Nonnull
    public Builder errorMessage (@Nullable final String s)
    {
      m_sErrorMessage = s;
      return this;
    }

    @Nonnull
    public Builder errorDetail (@Nullable final String s)
    {
      m_sErrorDetail = s;
      return this;
    }

    @Nonnull
    public Builder errorCode (@Nullable final IToopErrorCode a)
    {
      return errorCode (a == null ? null : a.getID ());
    }

    @Nonnull
    public Builder errorCode (@Nullable final String s)
    {
      m_sErrorCode = s;
      return this;
    }

    @Nonnull
    public Builder timestampNow ()
    {
      return timestamp (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public Builder timestamp (@Nullable final XMLGregorianCalendar a)
    {
      return timestamp (PDTXMLConverter.getLocalDateTime (a));
    }

    @Nonnull
    public Builder timestamp (@Nullable final LocalDateTime a)
    {
      m_aTimestamp = a;
      return this;
    }

    @Nonnull
    public Builder errorOrigin (@Nullable final EToopErrorOrigin e)
    {
      return errorOrigin (e == null ? null : e.getID ());
    }

    @Nonnull
    public Builder errorOrigin (@Nullable final String s)
    {
      m_sErrorOrigin = s;
      return this;
    }

    public void checkConsistency ()
    {
      if (m_eExceptionType == null)
        throw new IllegalStateException ("Exception Type must be provided");
      if (m_eSeverity == null)
        throw new IllegalStateException ("Error Severity must be provided");
      if (StringHelper.hasNoText (m_sErrorMessage))
        throw new IllegalStateException ("Error Message must be provided");
      if (m_aTimestamp == null)
        throw new IllegalStateException ("Timestamp must be provided");
    }

    @Nonnull
    public EDMExceptionPojo build ()
    {
      checkConsistency ();

      return new EDMExceptionPojo (m_eExceptionType,
                                   m_eSeverity,
                                   m_sErrorMessage,
                                   m_sErrorDetail,
                                   m_sErrorCode,
                                   m_aTimestamp,
                                   m_sErrorOrigin);
    }
  }
}
