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
package eu.toop.edm.request;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.regrep.slot.ISlotProvider;
import eu.toop.regrep.slot.predefined.SlotId;

/**
 * Request payload: Document ID
 *
 * @author Philip Helger
 */
public class EDMRequestPayloadDocumentID implements IEDMRequestPayloadProvider
{
  private final String m_sDocumentID;

  public EDMRequestPayloadDocumentID (@Nonnull @Nonempty final String sDocumentID)
  {
    ValueEnforcer.notEmpty (sDocumentID, "DocumentID");
    m_sDocumentID = sDocumentID;
  }

  @Nullable
  public final String getDocumentID ()
  {
    return m_sDocumentID;
  }

  @Nonnull
  public ISlotProvider getAsSlotProvider ()
  {
    return new SlotId (m_sDocumentID);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final EDMRequestPayloadDocumentID rhs = (EDMRequestPayloadDocumentID) o;
    return EqualsHelper.equals (m_sDocumentID, rhs.m_sDocumentID);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDocumentID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("DocumentID", m_sDocumentID).getToString ();
  }
}
