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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
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
import eu.toop.regrep.rim.*;
import eu.toop.regrep.slot.ISlotProvider;
import org.w3c.dom.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ResponseObjectPojo {
    private final String m_sID;
    private final ICommonsList<ConceptPojo> m_aConcepts = new CommonsArrayList<>();
    private final DatasetPojo m_aDataset;
    private final RepositoryItemRefPojo m_aRepositoryItemRef;

    public ResponseObjectPojo(@Nonnull @Nonempty final String sID,
                              @Nullable final ICommonsList <ConceptPojo> aConcepts,
                              @Nullable final DatasetPojo aDataset,
                              @Nullable final RepositoryItemRefPojo aRepositoryItemRef) {
        ValueEnforcer.notEmpty (sID, "RequestID");
        final int nConcepts = CollectionHelper.getSize (aConcepts);
        ValueEnforcer.isFalse ((nConcepts == 0 && aDataset == null) || (nConcepts != 0 && aDataset != null),
                "Exactly one of Concept and Dataset must be set");

        m_sID = sID;
        if (aConcepts != null)
            m_aConcepts.addAll(aConcepts);
        m_aDataset = aDataset;
        m_aRepositoryItemRef = aRepositoryItemRef;
    }

    @Nonnull
    @ReturnsMutableObject
    public final List<ConceptPojo> concepts ()
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
    public RegistryObjectType getAsRegistryObject()
    {
        final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();

        final ExtrinsicObjectType ret = new ExtrinsicObjectType();

        if(StringHelper.hasText(m_sID))
            ret.setId(m_sID);

        // ConceptValues
        if (m_aConcepts.isNotEmpty ())
            aSlots.add (new SlotConceptValues (m_aConcepts));

        // DocumentMetadata
        if (m_aDataset != null)
            aSlots.add (new SlotDocumentMetadata (m_aDataset));

        // All slots inside of RegistryObject
        for (final ISlotProvider aSlot : aSlots)
            ret.addSlot (aSlot.createSlot ());

        if (m_aRepositoryItemRef != null)
            ret.setRepositoryItemRef(m_aRepositoryItemRef.getAsSimpleLink());

        return ret;
    }

    @Nonnull
    public ObjectRefType getAsObjectRef()
    {
        if (m_aRepositoryItemRef != null)
            throw new IllegalStateException("ObjectRef may NOT contain a RepositoryItemRef.");

        final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();

        final ObjectRefType ret = new ObjectRefType();

        if(StringHelper.hasText(m_sID))
            ret.setId(m_sID);

        // ConceptValues
        if (m_aConcepts.isNotEmpty ())
            aSlots.add (new SlotConceptValues (m_aConcepts));

        // DocumentMetadata
        if (m_aDataset != null)
            aSlots.add (new SlotDocumentMetadata (m_aDataset));

        // All slots inside of RegistryObject
        for (final ISlotProvider aSlot : aSlots)
            ret.addSlot (aSlot.createSlot ());

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
        return EqualsHelper.equals (m_sID, rhs.m_sID) &&
                EqualsHelper.equals (m_aConcepts, rhs.m_aConcepts) &&
                EqualsHelper.equals (m_aDataset, rhs.m_aDataset) &&
                EqualsHelper.equals (m_aRepositoryItemRef, rhs.m_aRepositoryItemRef);
    }

    @Override
    public int hashCode ()
    {
        return new HashCodeGenerator(this).append (m_sID)
                .append (m_sID)
                .append (m_aConcepts)
                .append (m_aDataset)
                .append (m_aRepositoryItemRef)
                .getHashCode ();
    }

    @Override
    public String toString ()
    {
        return new ToStringGenerator(this).append ("ID", m_sID)
                .append ("IDSchemeID", m_sID)
                .append ("FamilyName", m_aConcepts)
                .append ("GivenName", m_aDataset)
                .append ("GenderCode", m_aRepositoryItemRef)
                .getToString ();
    }


    @Nonnull
    public static Builder builder ()
    {
        return new Builder();
    }

    @Nonnull
    public static Builder builder (@Nullable final RegistryObjectType a)
    {
        final Builder ret = new Builder();
        if (a != null)
        {
            if((a.getId() != null) && (StringHelper.hasText(a.getId())))
                ret.id(a.getId());
            for (final SlotType aSlot : a.getSlot())
                _applySlots(aSlot, ret);

            if (a instanceof ExtrinsicObjectType) {
                final ExtrinsicObjectType aEO = (ExtrinsicObjectType) a;

                if (aEO.getRepositoryItemRef() != null)
                    ret.repositoryItemRef(aEO.getRepositoryItemRef());
            }
        }
        return ret;
    }

    @Nonnull
    public static Builder builder (@Nullable final ObjectRefType a)
    {
        final Builder ret = new Builder();
        if (a != null)
        {
            if((a.getId() != null) && (!a.getId().isEmpty()))
                ret.id(a.getId());
            for (final SlotType aSlot : a.getSlot())
                _applySlots(aSlot, ret);
        }
        return ret;
    }

    public static class Builder
    {
        private String m_sID;
        private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();
        private DatasetPojo m_aDataset;
        private RepositoryItemRefPojo m_aRepositoryItemRef;

        protected Builder ()
        {}

        @Nonnull
        public Builder id (@Nullable final UUID a)
        {
            return id (a == null ? null : a.toString ());
        }

        @Nonnull
        public Builder id (@Nullable final String s)
        {
            m_sID = s;
            return this;
        }

        @Nonnull
        public Builder randomID ()
        {
            m_sID = UUID.randomUUID().toString();
            return this;
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
            if (StringHelper.hasNoText (m_sID))
                throw new IllegalStateException ("ID must be present");
            final int nConcepts = CollectionHelper.getSize (m_aConcepts);
            if((nConcepts == 0 && m_aDataset == null) || (nConcepts != 0 && m_aDataset != null))
                throw new IllegalStateException("Exactly one of Concept and Dataset must be set");

        }

        @Nonnull
        public ResponseObjectPojo build ()
        {
            checkConsistency ();

            return new ResponseObjectPojo (m_sID,
                                           m_aConcepts,
                                           m_aDataset,
                                           m_aRepositoryItemRef);
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
                    if (!aElements.isEmpty ())
                    {
                        for (final ValueType aElement : aElements)
                            if (aElement instanceof AnyValueType)
                            {
                                final Object aElementValue = ((AnyValueType) aElement).getAny ();
                                if (aElementValue instanceof Node)
                                    aBuilder.addConcept (new ConceptMarshaller().read ((Node) aElementValue));
                            }
                    }
                }
                break;
            case SlotDocumentMetadata.NAME:
            {
                if (aSlotValue instanceof AnyValueType)
                {
                    final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
                    aBuilder.dataset (DatasetPojo.builder (new DatasetMarshaller().read (aAny)));
                }
                break;
            }
            default:
                throw new IllegalStateException ("Slot is not defined: " + sName);
        }
    }
}
