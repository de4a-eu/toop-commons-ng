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

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.slot.SlotConceptValues;
import eu.toop.edm.slot.SlotDocumentMetadata;
import eu.toop.edm.xml.cccev.ConceptMarshaller;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.CollectionValueType;
import eu.toop.regrep.rim.ExtrinsicObjectType;
import eu.toop.regrep.rim.ObjectRefType;
import eu.toop.regrep.rim.RegistryObjectType;
import eu.toop.regrep.rim.SimpleLinkType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.ValueType;

/**
 * Represents a single response object
 *
 * @author Konstantinos Douloudis
 * @author Philip Helger
 */
public class ResponseObjectPojo
{
  private final String m_sRegistryObjectID;
  private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
  private final DatasetPojo m_aDataset;
  private final RepositoryItemRefPojo m_aRepositoryItemRef;

  public ResponseObjectPojo (@Nullable final String sRegistryObjectID,
                             @Nullable final ICommonsList <ConceptPojo> aConcepts,
                             @Nullable final DatasetPojo aDataset,
                             @Nullable final RepositoryItemRefPojo aRepositoryItemRef)
  {
    ValueEnforcer.notEmpty (sRegistryObjectID, "RegistryObjectID");
    final int nConcepts = CollectionHelper.getSize (aConcepts);
    ValueEnforcer.isFalse ((nConcepts == 0 && aDataset == null) || (nConcepts != 0 && aDataset != null),
                           "Exactly one of Concept and Dataset must be set");
    ValueEnforcer.isTrue (aRepositoryItemRef == null || nConcepts == 0,
                          "RepositoryItemRef must not be used in combination with concepts");

    m_sRegistryObjectID = sRegistryObjectID;
    if (aConcepts != null)
      m_aConcepts.addAll (aConcepts);
    m_aDataset = aDataset;
    m_aRepositoryItemRef = aRepositoryItemRef;
  }

  @Nullable
  public final String getID ()
  {
    return m_sRegistryObjectID;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <ConceptPojo> concepts ()
  {
    return m_aConcepts;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ConceptPojo> getAllConcepts ()
  {
    return m_aConcepts.getClone ();
  }

  @Nullable
  public final DatasetPojo getDataset ()
  {
    return m_aDataset;
  }

  @Nullable
  public final RepositoryItemRefPojo getRepositoryItemRef ()
  {
    return m_aRepositoryItemRef;
  }

  @Nonnull
  public ExtrinsicObjectType getAsRegistryObject ()
  {
    final ExtrinsicObjectType ret = new ExtrinsicObjectType ();
    ret.setId (m_sRegistryObjectID);

    // ConceptValues
    if (m_aConcepts.isNotEmpty ())
      ret.addSlot (new SlotConceptValues (m_aConcepts).createSlot ());

    // DocumentMetadata
    if (m_aDataset != null)
      ret.addSlot (new SlotDocumentMetadata (m_aDataset).createSlot ());

    if (m_aRepositoryItemRef != null)
      ret.setRepositoryItemRef (m_aRepositoryItemRef.getAsSimpleLink ());

    return ret;
  }

  @Nonnull
  public ObjectRefType getAsObjectRef ()
  {
    if (m_aRepositoryItemRef != null)
      throw new IllegalStateException ("ObjectRef may NOT contain a RepositoryItemRef.");
    if (m_aConcepts.isNotEmpty ())
      throw new IllegalStateException ("ObjectRef may NOT contain Concepts.");

    final ObjectRefType ret = new ObjectRefType ();
    ret.setId (m_sRegistryObjectID);

    // DocumentMetadata
    if (m_aDataset != null)
      ret.addSlot (new SlotDocumentMetadata (m_aDataset).createSlot ());

    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ResponseObjectPojo rhs = (ResponseObjectPojo) o;
    return EqualsHelper.equals (m_sRegistryObjectID, rhs.m_sRegistryObjectID) &&
           EqualsHelper.equals (m_aConcepts, rhs.m_aConcepts) &&
           EqualsHelper.equals (m_aDataset, rhs.m_aDataset) &&
           EqualsHelper.equals (m_aRepositoryItemRef, rhs.m_aRepositoryItemRef);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sRegistryObjectID)
                                       .append (m_aConcepts)
                                       .append (m_aDataset)
                                       .append (m_aRepositoryItemRef)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("RegistryObjectID", m_sRegistryObjectID)
                                       .append ("Concepts", m_aConcepts)
                                       .append ("Dataset", m_aDataset)
                                       .append ("RepositoryItemRef", m_aRepositoryItemRef)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  public static class Builder
  {
    private String m_sRegistryObjectID;
    private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
    private DatasetPojo m_aDataset;
    private RepositoryItemRefPojo m_aRepositoryItemRef;

    protected Builder ()
    {}

    @Nonnull
    public Builder registryObjectID (@Nullable final String s)
    {
      m_sRegistryObjectID = s;
      return this;
    }

    @Nonnull
    public Builder randomRegistryObjectID ()
    {
      return registryObjectID (UUID.randomUUID ().toString ());
    }

    @Nonnull
    public Builder addConcept (@Nullable final CCCEVConceptType a)
    {
      return addConcept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public Builder addConcept (@Nullable final ConceptPojo.Builder a)
    {
      return addConcept (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addConcept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.add (a);
      return this;
    }

    @Nonnull
    public Builder concept (@Nullable final CCCEVConceptType a)
    {
      return concept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder concept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.set (a);
      else
        m_aConcepts.clear ();
      return this;
    }

    @Nonnull
    public Builder concepts (@Nullable final ConceptPojo... a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Nonnull
    public Builder concepts (@Nullable final Iterable <ConceptPojo> a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Nonnull
    public Builder dataset (@Nullable final DatasetPojo.Builder a)
    {
      return dataset (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataset (@Nullable final DatasetPojo a)
    {
      m_aDataset = a;
      return this;
    }

    @Nonnull
    public Builder dataset (@Nullable final DCatAPDatasetType a)
    {
      return dataset (a == null ? null : DatasetPojo.builder (a));
    }

    @Nonnull
    public Builder repositoryItemRef (@Nullable final RepositoryItemRefPojo.Builder a)
    {
      return repositoryItemRef (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder repositoryItemRef (@Nullable final RepositoryItemRefPojo a)
    {
      m_aRepositoryItemRef = a;
      return this;
    }

    @Nonnull
    public Builder repositoryItemRef (@Nullable final SimpleLinkType a)
    {
      return repositoryItemRef (a == null ? null : RepositoryItemRefPojo.builder (a));
    }

    public void checkConsistency ()
    {
      if (StringHelper.hasNoText (m_sRegistryObjectID))
        throw new IllegalStateException ("RegistryObjectID must be present");

      final int nConcepts = CollectionHelper.getSize (m_aConcepts);
      if ((nConcepts == 0 && m_aDataset == null) || (nConcepts != 0 && m_aDataset != null))
        throw new IllegalStateException ("Exactly one of Concept and Dataset must be set");

      if (nConcepts > 0 && m_aRepositoryItemRef != null)
        throw new IllegalStateException ("RepositoryItemRef MUST ONLY be used in combination with Dataset");
    }

    @Nonnull
    public ResponseObjectPojo build ()
    {
      checkConsistency ();

      return new ResponseObjectPojo (m_sRegistryObjectID, m_aConcepts, m_aDataset, m_aRepositoryItemRef);
    }
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final Builder aBuilder)
  {
    final String sName = aSlot.getName ();
    final ValueType aSlotValue = aSlot.getSlotValue ();
    switch (sName)
    {
      case SlotConceptValues.NAME:
        if (aSlotValue instanceof CollectionValueType)
        {
          final List <ValueType> aElements = ((CollectionValueType) aSlotValue).getElement ();
          if (aElements != null)
            for (final ValueType aElement : aElements)
              if (aElement instanceof AnyValueType)
              {
                final Object aElementValue = ((AnyValueType) aElement).getAny ();
                if (aElementValue instanceof Node)
                  aBuilder.addConcept (new ConceptMarshaller ().read ((Node) aElementValue));
              }
        }
        break;
      case SlotDocumentMetadata.NAME:
      {
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataset (DatasetPojo.builder (new DatasetMarshaller ().read (aAny)));
        }
        break;
      }
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static Builder builder (@Nullable final ObjectRefType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.registryObjectID (a.getId ());
      for (final SlotType aSlot : a.getSlot ())
        _applySlots (aSlot, ret);
    }
    return ret;
  }

  @Nonnull
  public static Builder builder (@Nullable final RegistryObjectType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.registryObjectID (a.getId ());
      for (final SlotType aSlot : a.getSlot ())
        _applySlots (aSlot, ret);

      if (a instanceof ExtrinsicObjectType)
      {
        final ExtrinsicObjectType aEO = (ExtrinsicObjectType) a;
        ret.repositoryItemRef (aEO.getRepositoryItemRef ());
      }
    }
    return ret;
  }
}
