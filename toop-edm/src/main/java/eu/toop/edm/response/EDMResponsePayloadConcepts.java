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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.slot.SlotConceptValues;
import eu.toop.regrep.rim.ExtrinsicObjectType;

/**
 * Represents a single "Concept response" payload.
 *
 * @author Philip Helger
 * @since 2.0.0-beta3
 */
public class EDMResponsePayloadConcepts implements IEDMResponsePayloadConcept
{
  private final String m_sRegistryObjectID;
  private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();

  public EDMResponsePayloadConcepts (@Nonnull @Nonempty final String sRegistryObjectID,
                                     @Nullable final ICommonsList <ConceptPojo> aConcepts)
  {
    ValueEnforcer.notEmpty (sRegistryObjectID, "RegistryObjectID");
    ValueEnforcer.notEmpty (aConcepts, "Concepts");

    m_sRegistryObjectID = sRegistryObjectID;
    m_aConcepts.addAll (aConcepts);
  }

  @Nonnull
  @Nonempty
  public final String getRegistryObjectID ()
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

  @Nonnull
  public ExtrinsicObjectType getAsRegistryObject ()
  {
    final ExtrinsicObjectType ret = new ExtrinsicObjectType ();
    ret.setId (m_sRegistryObjectID);

    // ConceptValues
    ret.addSlot (new SlotConceptValues (m_aConcepts).createSlot ());

    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final EDMResponsePayloadConcepts rhs = (EDMResponsePayloadConcepts) o;
    return EqualsHelper.equals (m_sRegistryObjectID, rhs.m_sRegistryObjectID) &&
           EqualsHelper.equals (m_aConcepts, rhs.m_aConcepts);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sRegistryObjectID).append (m_aConcepts).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("RegistryObjectID", m_sRegistryObjectID)
                                       .append ("Concepts", m_aConcepts)
                                       .getToString ();
  }
}
