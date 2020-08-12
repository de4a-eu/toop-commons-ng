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

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.model.DistributionPojo;
import eu.toop.edm.slot.SlotDistributionRequestList;
import eu.toop.regrep.slot.ISlotProvider;

/**
 * Request payload: Distribution
 *
 * @author Philip Helger
 * @since 2.0.0-beta3
 */
public class EDMRequestPayloadDistribution implements IEDMRequestPayloadDistribution
{
  private final ICommonsList <DistributionPojo> m_aDistributions = new CommonsArrayList <> ();

  public EDMRequestPayloadDistribution (@Nonnull @Nonempty final ICommonsList <DistributionPojo> aDistributions)
  {
    ValueEnforcer.notEmpty (aDistributions, "Distributions");
    m_aDistributions.addAll (aDistributions);
  }

  @Nonnull
  @ReturnsMutableObject
  public final ICommonsList <DistributionPojo> distributions ()
  {
    return m_aDistributions;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final ICommonsList <DistributionPojo> getAllDistributions ()
  {
    return m_aDistributions.getClone ();
  }

  @Nonnull
  public ISlotProvider getAsSlotProvider ()
  {
    return new SlotDistributionRequestList (m_aDistributions);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final EDMRequestPayloadDistribution rhs = (EDMRequestPayloadDistribution) o;
    return EqualsHelper.equals (m_aDistributions, rhs.m_aDistributions);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDistributions).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Distributions", m_aDistributions).getToString ();
  }
}
