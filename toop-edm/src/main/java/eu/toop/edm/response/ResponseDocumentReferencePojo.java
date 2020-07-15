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

import java.util.UUID;

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
import eu.toop.edm.slot.SlotDocumentMetadata;
import eu.toop.edm.xml.dcatap.DatasetMarshaller;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.ObjectRefType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.ValueType;

/**
 * Represents a single response object reference
 *
 * @author Konstantinos Douloudis
 * @author Philip Helger
 * @since 2.0.0-beta3
 */
public class ResponseDocumentReferencePojo implements IEDMResponsePayloadDocumentReference
{
  private final String m_sRegistryObjectID;
  private final DatasetPojo m_aDataset;

  public ResponseDocumentReferencePojo (@Nonnull @Nonempty final String sRegistryObjectID, @Nonnull final DatasetPojo aDataset)
  {
    ValueEnforcer.notEmpty (sRegistryObjectID, "RegistryObjectID");
    ValueEnforcer.notNull (aDataset, "Dataset");

    m_sRegistryObjectID = sRegistryObjectID;
    m_aDataset = aDataset;
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
  @Override
  public ObjectRefType getAsObjectRef ()
  {
    final ObjectRefType ret = new ObjectRefType ();
    ret.setId (m_sRegistryObjectID);

    // DocumentMetadata
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
    final ResponseDocumentReferencePojo rhs = (ResponseDocumentReferencePojo) o;
    return EqualsHelper.equals (m_sRegistryObjectID, rhs.m_sRegistryObjectID) && EqualsHelper.equals (m_aDataset, rhs.m_aDataset);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sRegistryObjectID).append (m_aDataset).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("RegistryObjectID", m_sRegistryObjectID).append ("Dataset", m_aDataset).getToString ();
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

    public void checkConsistency ()
    {
      if (StringHelper.hasNoText (m_sRegistryObjectID))
        throw new IllegalStateException ("RegistryObjectID must be present");
      if (m_aDataset == null)
        throw new IllegalStateException ("Dataset MUST be present");
    }

    @Nonnull
    public ResponseDocumentReferencePojo build ()
    {
      checkConsistency ();

      return new ResponseDocumentReferencePojo (m_sRegistryObjectID, m_aDataset);
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
}
