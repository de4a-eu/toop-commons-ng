/**
 * Copyright (C) 2018-2021 toop.eu
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

import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.RepositoryItemRefPojo;
import eu.toop.edm.slot.SlotDocumentMetadata;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.ExtrinsicObjectType;
import eu.toop.regrep.rim.SimpleLinkType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.ValueType;

/**
 * Represents a single response object
 *
 * @author Konstantinos Douloudis
 * @author Philip Helger
 * @since 2.0.0-beta3
 */
public class ResponseDocumentPojo implements IEDMResponsePayloadDocument
{
  private final String m_sRegistryObjectID;
  private final DatasetPojo m_aDataset;
  private final RepositoryItemRefPojo m_aRepositoryItemRef;

  public ResponseDocumentPojo (@Nonnull @Nonempty final String sRegistryObjectID,
                               @Nonnull final DatasetPojo aDataset,
                               @Nonnull final RepositoryItemRefPojo aRepositoryItemRef)
  {
    ValueEnforcer.notEmpty (sRegistryObjectID, "RegistryObjectID");
    ValueEnforcer.notNull (aDataset, "Dataset");
    ValueEnforcer.notNull (aRepositoryItemRef, "RepositoryItemRef");

    m_sRegistryObjectID = sRegistryObjectID;
    m_aDataset = aDataset;
    m_aRepositoryItemRef = aRepositoryItemRef;
  }

  @Nonnull
  @Nonempty
  public final String getRegistryObjectID ()
  {
    return m_sRegistryObjectID;
  }

  @Nonnull
  public final DatasetPojo getDataset ()
  {
    return m_aDataset;
  }

  @Nonnull
  public final RepositoryItemRefPojo getRepositoryItemRef ()
  {
    return m_aRepositoryItemRef;
  }

  @Nonnull
  @Override
  public ExtrinsicObjectType getAsRegistryObject ()
  {
    final ExtrinsicObjectType ret = new ExtrinsicObjectType ();
    ret.setId (m_sRegistryObjectID);

    // DocumentMetadata
    ret.addSlot (new SlotDocumentMetadata (m_aDataset).createSlot ());

    // Reference to AS4 artifact
    ret.setRepositoryItemRef (m_aRepositoryItemRef.getAsSimpleLink ());

    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ResponseDocumentPojo rhs = (ResponseDocumentPojo) o;
    return EqualsHelper.equals (m_sRegistryObjectID, rhs.m_sRegistryObjectID) &&
           EqualsHelper.equals (m_aDataset, rhs.m_aDataset) &&
           EqualsHelper.equals (m_aRepositoryItemRef, rhs.m_aRepositoryItemRef);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sRegistryObjectID).append (m_aDataset).append (m_aRepositoryItemRef).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("RegistryObjectID", m_sRegistryObjectID)
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
    public Builder repositoryItemRef (@Nullable final Consumer <? super RepositoryItemRefPojo.Builder> a)
    {
      if (a != null)
      {
        final RepositoryItemRefPojo.Builder aBuilder = RepositoryItemRefPojo.builder ();
        a.accept (aBuilder);
        repositoryItemRef (aBuilder);
      }
      return this;
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
      if (m_aDataset == null)
        throw new IllegalStateException ("Dataset MUST be present");
      if (m_aRepositoryItemRef == null)
        throw new IllegalStateException ("RepositoryItemRef MUST be present");
    }

    @Nonnull
    public ResponseDocumentPojo build ()
    {
      checkConsistency ();

      return new ResponseDocumentPojo (m_sRegistryObjectID, m_aDataset, m_aRepositoryItemRef);
    }
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final Builder aBuilder)
  {
    final String sName = aSlot.getName ();
    final ValueType aSlotValue = aSlot.getSlotValue ();
    switch (sName)
    {
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
        throw new IllegalStateException ("Found unsupported slot '" + sName + "'");
    }
  }

  @Nonnull
  public static Builder builder (@Nullable final ExtrinsicObjectType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.registryObjectID (a.getId ());
      for (final SlotType aSlot : a.getSlot ())
        _applySlots (aSlot, ret);

      ret.repositoryItemRef (a.getRepositoryItemRef ());
    }
    return ret;
  }
}
