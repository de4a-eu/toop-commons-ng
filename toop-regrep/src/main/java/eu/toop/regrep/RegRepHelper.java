/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2020-2021. All rights reserved.
 *
 * This work is dual licensed under Apache License, Version 2.0
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
package eu.toop.regrep;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;

import eu.toop.regrep.query.QueryRequest;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.query.ResponseOptionType;
import eu.toop.regrep.rim.QueryType;
import eu.toop.regrep.rim.SlotType;

/**
 * Helper class to simplify the creation of RegRep data constructs.
 *
 * @author Philip Helger
 */
@Immutable
public final class RegRepHelper
{
  private RegRepHelper ()
  {}

  @Nonnull
  public static QueryRequest createEmptyQueryRequest ()
  {
    final QueryRequest ret = new QueryRequest ();
    ret.setId (UUID.randomUUID ().toString ());
    ret.setResponseOption (new ResponseOptionType ());
    return ret;
  }

  @Nonnull
  public static QueryRequest createQueryRequest (@Nonnull @Nonempty final String sQueryDefinition,
                                                 @Nonnull @Nonempty final SlotType... aSlots)
  {
    ValueEnforcer.notEmpty (sQueryDefinition, "sQueryDefinition");
    ValueEnforcer.notEmptyNoNullValue (aSlots, "Slots");
    final QueryRequest ret = createEmptyQueryRequest ();

    final QueryType aQuery = new QueryType ();
    aQuery.setQueryDefinition (sQueryDefinition);
    for (final SlotType aSlot : aSlots)
      aQuery.addSlot (aSlot);
    ret.setQuery (aQuery);
    return ret;
  }

  @Nonnull
  public static QueryResponse createEmptyQueryResponse (@Nonnull final ERegRepResponseStatus eStatus)
  {
    ValueEnforcer.notNull (eStatus, "Status");

    final QueryResponse ret = new QueryResponse ();
    ret.setStatus (eStatus.getID ());
    return ret;
  }

  @Nonnull
  public static QueryResponse createQueryResponse (@Nonnull final ERegRepResponseStatus eStatus,
                                                   @Nullable final String sRequestID,
                                                   @Nonnull @Nonempty final SlotType... aSlots)
  {
    final QueryResponse ret = createEmptyQueryResponse (eStatus);
    ret.setRequestId (sRequestID);
    for (final SlotType aSlot : aSlots)
      ret.addSlot (aSlot);
    return ret;
  }
}
