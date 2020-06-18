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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.error.IToopErrorCode;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cccev.CCCEVValueType;
import eu.toop.edm.jaxb.cv.cbc.IDType;

/**
 * Represents a "Concept" that can be recursively nested. Only response concepts
 * can have values.
 *
 * @author Philip Helger
 */
public class ConceptPojo
{
  /**
   * A visitor for concepts.
   *
   * @author Philip Helger
   */
  public interface IConceptVisitor
  {
    void onConcept (@Nonnegative int nLevel, @Nonnull ConceptPojo aPojo);
  }

  private final String m_sID;
  private final QName m_aName;
  private final ConceptValuePojo m_aValue;
  private final ICommonsList <ConceptPojo> m_aChildren = new CommonsArrayList <> ();

  public ConceptPojo (@Nullable final String sID,
                      @Nullable final QName aName,
                      @Nullable final ConceptValuePojo aValue,
                      @Nullable final List <ConceptPojo> aChildren)
  {
    m_sID = sID;
    m_aName = aName;
    m_aValue = aValue;
    if (aChildren != null)
      m_aChildren.addAll (aChildren);
  }

  /**
   * @return The internal ID of the concept inside the request. This is NOT the
   *         concet name.
   */
  @Nullable
  public final String getID ()
  {
    return m_sID;
  }

  /**
   * @return The qualified name of the concept. Consisting of a namespace URI
   *         and the local part as the name.
   */
  @Nullable
  public final QName getName ()
  {
    return m_aName;
  }

  /**
   * @return The value of the concept. Usually only present for responses.
   */
  @Nullable
  public final ConceptValuePojo getValue ()
  {
    return m_aValue;
  }

  /**
   * @return <code>true</code> if a value is present, <code>false</code> if not.
   */
  public final boolean hasValue ()
  {
    return m_aValue != null;
  }

  /**
   * @return <code>true</code> if a value is present, and if that value contains
   *         an error code.
   */
  public final boolean isErrorValue ()
  {
    return m_aValue != null && m_aValue.isErrorCodeValue ();
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <ConceptPojo> children ()
  {
    return m_aChildren;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ConceptPojo> getAllChildren ()
  {
    return m_aChildren.getClone ();
  }

  private void _visitRecursive (final int nLevel, @Nonnull final IConceptVisitor aVisitor)
  {
    // Invoke
    aVisitor.onConcept (nLevel, this);

    // For all children; aka depth first search
    for (final ConceptPojo aItem : m_aChildren)
      aItem._visitRecursive (nLevel + 1, aVisitor);
  }

  public void visitRecursive (@Nonnull final IConceptVisitor aVisitor)
  {
    _visitRecursive (0, aVisitor);
  }

  @Nonnull
  public Builder getBuilder ()
  {
    return builder (this);
  }

  @Nonnull
  public ConceptPojo cloneAndModify (@Nullable final Consumer <? super ConceptPojo.Builder> aModificationHandler)
  {
    return builder (this, aModificationHandler).build ();
  }

  @Nonnull
  public CCCEVConceptType getAsCCCEVConcept ()
  {
    final CCCEVConceptType ret = new CCCEVConceptType ();
    if (StringHelper.hasText (m_sID))
    {
      final IDType aID = new IDType ();
      aID.setValue (m_sID);
      ret.addId (aID);
    }
    if (m_aName != null)
      ret.addQName (m_aName);
    if (m_aValue != null)
    {
      final CCCEVValueType aValue = m_aValue.getAsCCCEVValueType ();
      if (aValue != null)
        ret.addValue (aValue);
    }

    // Recursive call
    for (final ConceptPojo aChild : m_aChildren)
      ret.addConcept (aChild.getAsCCCEVConcept ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ConceptPojo rhs = (ConceptPojo) o;
    return EqualsHelper.equals (m_sID, rhs.m_sID) &&
           EqualsHelper.equals (m_aName, rhs.m_aName) &&
           EqualsHelper.equals (m_aValue, rhs.m_aValue) &&
           EqualsHelper.equals (m_aChildren, rhs.m_aChildren);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID).append (m_aName).append (m_aValue).append (m_aChildren).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("Name", m_aName)
                                       .append ("Value", m_aValue)
                                       .append ("Children", m_aChildren)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final CCCEVConceptType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      if (a.hasIdEntries ())
        ret.id (a.getIdAtIndex (0).getValue ());
      if (a.hasQNameEntries ())
        ret.name (a.getQNameAtIndex (0));
      if (a.hasValueEntries ())
        ret.value (ConceptValuePojo.builder (a.getValueAtIndex (0)));

      // Recursive call
      for (final CCCEVConceptType aChild : a.getConcept ())
        ret.addChild (builder (aChild));
    }
    return ret;
  }

  @Nonnull
  public static Builder builder (@Nullable final ConceptPojo a)
  {
    return builder (a, null);
  }

  @Nonnull
  public static Builder builder (@Nullable final ConceptPojo a, @Nullable final Consumer <? super ConceptPojo.Builder> aModificationHandler)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.id (a.getID ()).name (a.getName ()).value (a.getValue ());

      // Recursive call
      for (final ConceptPojo aChild : a.children ())
        ret.addChild (builder (aChild, aModificationHandler));

      // Call handler after children
      if (aModificationHandler != null)
        aModificationHandler.accept (ret);
    }
    return ret;
  }

  public static class Builder
  {
    private String m_sID;
    private QName m_aName;
    private ConceptValuePojo m_aValue;
    private final ICommonsList <ConceptPojo> m_aChildren = new CommonsArrayList <> ();

    public Builder ()
    {}

    @Nullable
    public String id ()
    {
      return m_sID;
    }

    @Nonnull
    public Builder randomID ()
    {
      return id (UUID.randomUUID ().toString ());
    }

    @Nonnull
    public Builder id (@Nullable final String s)
    {
      m_sID = s;
      return this;
    }

    @Nullable
    public QName name ()
    {
      return m_aName;
    }

    @Nonnull
    public Builder name (@Nullable final String sNamespaceURI, @Nullable final String sLocalName)
    {
      return name (StringHelper.hasNoText (sLocalName) ? null : new QName (sNamespaceURI, sLocalName));
    }

    @Nonnull
    public Builder name (@Nullable final IConceptName aConcept)
    {
      return name (aConcept == null ? null : aConcept.getAsQName ());
    }

    @Nonnull
    public Builder name (@Nullable final QName a)
    {
      m_aName = a;
      return this;
    }

    @Nullable
    public ConceptValuePojo value ()
    {
      return m_aValue;
    }

    @Nonnull
    public Builder valueID (@Nullable final String s)
    {
      return value (ConceptValuePojo.builder ().identifier (s));
    }

    @Nonnull
    public Builder valueAmount (@Nullable final Consumer <? super AmountPojo.Builder> a)
    {
      return value (ConceptValuePojo.builder ().amount (a));
    }

    @Nonnull
    public Builder valueAmount (@Nullable final AmountPojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().amount (a));
    }

    @Nonnull
    public Builder valueAmount (@Nullable final AmountPojo a)
    {
      return value (ConceptValuePojo.builder ().amount (a));
    }

    @Nonnull
    public Builder valueAmount (@Nullable final BigDecimal aValue, @Nullable final String sCurrencyID)
    {
      return value (ConceptValuePojo.builder ().amount (aValue, sCurrencyID));
    }

    @Nonnull
    public Builder valueCode (@Nullable final String s)
    {
      return value (ConceptValuePojo.builder ().code (s));
    }

    @Nonnull
    public Builder valueDate (@Nullable final LocalDate a)
    {
      return value (ConceptValuePojo.builder ().date (a));
    }

    @Nonnull
    public Builder valueIndicator (final boolean b)
    {
      return value (ConceptValuePojo.builder ().indicator (b));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final Consumer <? super MeasurePojo.Builder> a)
    {
      return value (ConceptValuePojo.builder ().measure (a));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final MeasurePojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().measure (a));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final MeasurePojo a)
    {
      return value (ConceptValuePojo.builder ().measure (a));
    }

    @Nonnull
    public Builder valueMeasure (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
    {
      return value (ConceptValuePojo.builder ().measure (aValue, sUnitCode));
    }

    @Nonnull
    public Builder valuePeriod (@Nullable final Consumer <? super PeriodPojo.Builder> a)
    {
      return value (ConceptValuePojo.builder ().period (a));
    }

    @Nonnull
    public Builder valuePeriod (@Nullable final PeriodPojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().period (a));
    }

    @Nonnull
    public Builder valuePeriod (@Nullable final PeriodPojo a)
    {
      return value (ConceptValuePojo.builder ().period (a));
    }

    @Nonnull
    public Builder valuePeriod (@Nullable final LocalDateTime aStartDate, @Nullable final LocalDateTime aEndDate)
    {
      return value (ConceptValuePojo.builder ().period (aStartDate, aEndDate));
    }

    @Nonnull
    public Builder valueNumeric (@Nonnull final long n)
    {
      return value (ConceptValuePojo.builder ().numeric (n));
    }

    @Nonnull
    public Builder valueNumeric (@Nonnull final double d)
    {
      return value (ConceptValuePojo.builder ().numeric (d));
    }

    @Nonnull
    public Builder valueNumeric (@Nullable final BigDecimal a)
    {
      return value (ConceptValuePojo.builder ().numeric (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final Consumer <? super QuantityPojo.Builder> a)
    {
      return value (ConceptValuePojo.builder ().quantity (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final QuantityPojo.Builder a)
    {
      return value (ConceptValuePojo.builder ().quantity (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final QuantityPojo a)
    {
      return value (ConceptValuePojo.builder ().quantity (a));
    }

    @Nonnull
    public Builder valueQuantity (@Nullable final BigDecimal aValue, @Nullable final String sUnitCode)
    {
      return value (ConceptValuePojo.builder ().quantity (aValue, sUnitCode));
    }

    @Nonnull
    public Builder valueText (@Nonnull final String... a)
    {
      return value (ConceptValuePojo.builder ().text (a));
    }

    @Nonnull
    public Builder valueTime (@Nullable final LocalTime a)
    {
      return value (ConceptValuePojo.builder ().time (a));
    }

    @Nonnull
    public Builder valueURI (@Nonnull final String s)
    {
      return value (ConceptValuePojo.builder ().uri (s));
    }

    @Nonnull
    public Builder valueErrorCode (@Nullable final IToopErrorCode a)
    {
      return value (ConceptValuePojo.builder ().errorCode (a));
    }

    @Nonnull
    public Builder valueErrorCode (@Nullable final String s)
    {
      return value (ConceptValuePojo.builder ().errorCode (s));
    }

    @Nonnull
    public Builder value (@Nullable final Consumer <? super ConceptValuePojo.Builder> a)
    {
      if (a != null)
      {
        final ConceptValuePojo.Builder aBuilder = ConceptValuePojo.builder ();
        a.accept (aBuilder);
        value (aBuilder);
      }
      return this;
    }

    @Nonnull
    public Builder value (@Nullable final ConceptValuePojo.Builder a)
    {
      return value (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder value (@Nullable final ConceptValuePojo a)
    {
      m_aValue = a;
      return this;
    }

    @Nonnull
    public ICommonsList <ConceptPojo> children ()
    {
      return m_aChildren;
    }

    @Nonnull
    public Builder addChild (@Nullable final Consumer <? super ConceptPojo.Builder> a)
    {
      if (a != null)
      {
        final ConceptPojo.Builder aBuilder = ConceptPojo.builder ();
        a.accept (aBuilder);
        addChild (aBuilder);
      }
      return this;
    }

    @Nonnull
    public Builder addChild (@Nullable final IConceptName a)
    {
      return addChild (a == null ? null : ConceptPojo.builder ().name (a));
    }

    @Nonnull
    public Builder addChild (@Nullable final ConceptPojo.Builder a)
    {
      return addChild (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addChild (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aChildren.add (a);
      return this;
    }

    @Nonnull
    public Builder child (@Nullable final Consumer <? super ConceptPojo.Builder> a)
    {
      if (a != null)
      {
        final ConceptPojo.Builder aBuilder = ConceptPojo.builder ();
        a.accept (aBuilder);
        child (aBuilder);
      }
      return this;
    }

    @Nonnull
    public Builder child (@Nullable final ConceptPojo.Builder a)
    {
      return child (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder child (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aChildren.set (a);
      else
        m_aChildren.clear ();
      return this;
    }

    @Nonnull
    public Builder children (@Nullable final ConceptPojo... a)
    {
      m_aChildren.setAll (a);
      return this;
    }

    @Nonnull
    public Builder children (@Nullable final Iterable <? extends ConceptPojo> a)
    {
      m_aChildren.setAll (a);
      return this;
    }

    @Nonnull
    public ConceptPojo build ()
    {
      return new ConceptPojo (m_sID, m_aName, m_aValue, m_aChildren);
    }
  }
}
