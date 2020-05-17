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
package eu.toop.edm;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.*;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.datetime.util.PDTXMLConverter;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.ResponseObjectPojo;
import eu.toop.edm.slot.SlotDataProvider;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileReader;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cagv.AgentMarshaller;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.*;
import eu.toop.regrep.slot.ISlotProvider;
import org.w3c.dom.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * This class contains the data model for a single TOOP EDM Request. It requires
 * at least the following fields:
 * <ul>
 * <li>QueryDefinition - Concept or Document query?</li>
 * <li>Response Status - Success, partial success or failure</li>
 * <li>Request ID - the ID of the request to which this response
 * correlates.</li>
 * <li>Specification Identifier - must be the value
 * {@link CToopEDM#SPECIFICATION_IDENTIFIER_TOOP_EDM_V20}.</li>
 * <li>Issue date time - when the response was created. Ideally in UTC.</li>
 * <li>Data Provider - the basic infos of the DP</li>
 * <li>If it is a "ConceptQuery" the response Concepts must be provided.</li>
 * <li>If it is a "DocumentQuery" the response Dataset must be provided.</li>
 * </ul>
 * It is recommended to use the {@link #builder()} methods to create the EDM
 * request using the builder pattern with a fluent API.
 *
 * @author Philip Helger
 * @author Konstantinos Douloudis
 */
public class EDMResponse
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotIssueDateTime.NAME,
                                                                                                  SlotDataProvider.NAME);

  private final EQueryDefinitionType m_eQueryDefinition;
  private final ERegRepResponseStatus m_eResponseStatus;
  private final String m_sRequestID;
  private final String m_sSpecificationIdentifier;
  private final LocalDateTime m_aIssueDateTime;
  private final AgentPojo m_aDataProvider;
  private final ICommonsList <ResponseObjectPojo> m_aResponseObjects = new CommonsArrayList <> ();

  public EDMResponse (@Nonnull final EQueryDefinitionType eQueryDefinition,
                      @Nonnull final ERegRepResponseStatus eResponseStatus,
                      @Nonnull @Nonempty final String sRequestID,
                      @Nonnull @Nonempty final String sSpecificationIdentifier,
                      @Nonnull final LocalDateTime aIssueDateTime,
                      @Nonnull final AgentPojo aDataProvider,
                      @Nonnull final ICommonsList <ResponseObjectPojo> aResponseObjects)
  {
    ValueEnforcer.notNull (eQueryDefinition, "QueryDefinition");
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.isTrue (eResponseStatus == ERegRepResponseStatus.SUCCESS ||
                          eResponseStatus == ERegRepResponseStatus.FAILURE,
                          "Only SUCCESS and FAILURE are supported");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notNull (aIssueDateTime, "IssueDateTime");
    ValueEnforcer.notNull (aDataProvider, "DataProvider");
    ValueEnforcer.notEmpty (aResponseObjects, "ResponseObjects");

    switch (eQueryDefinition)
    {
      case CONCEPT:
        for(ResponseObjectPojo aResponseObject : aResponseObjects)
          ValueEnforcer.notEmpty(aResponseObject.concepts(), "Concept");
        break;
      case DOCUMENT:
      case OBJECTREF:
        for(ResponseObjectPojo aResponseObject : aResponseObjects)
          ValueEnforcer.notNull(aResponseObject.getDataset(), "Dataset");
        break;
      default:
        throw new IllegalArgumentException ("Unsupported query definition: " + eQueryDefinition);
    }

    m_eQueryDefinition = eQueryDefinition;
    m_eResponseStatus = eResponseStatus;
    m_sRequestID = sRequestID;
    m_sSpecificationIdentifier = sSpecificationIdentifier;
    m_aIssueDateTime = aIssueDateTime;
    m_aDataProvider = aDataProvider;
    m_aResponseObjects.addAll(aResponseObjects);
  }

  @Nonnull
  public final EQueryDefinitionType getQueryDefinition ()
  {
    return m_eQueryDefinition;
  }

  @Nonnull
  public final ERegRepResponseStatus getResponseStatus ()
  {
    return m_eResponseStatus;
  }

  @Nonnull
  @Nonempty
  public final String getRequestID ()
  {
    return m_sRequestID;
  }

  @Nonnull
  @Nonempty
  public final String getSpecificationIdentifier ()
  {
    return m_sSpecificationIdentifier;
  }

  @Nonnull
  public final LocalDateTime getIssueDateTime ()
  {
    return m_aIssueDateTime;
  }

  @Nonnull
  public final AgentPojo getDataProvider ()
  {
    return m_aDataProvider;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <ResponseObjectPojo> responseObjects ()
  {
    return m_aResponseObjects;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ResponseObjectPojo> getAllResponseObjects ()
  {
    return m_aResponseObjects.getClone ();
  }

  /**
   * @deprecated Since beta3; Use {@link #responseObjects()} or {@link #getAllResponseObjects()}
   * and get the concepts through the {@link ResponseObjectPojo} ()} instead
   */
  @Nonnull
  @Deprecated
  @ReturnsMutableObject
  public final List<ConceptPojo> concepts ()
  {
    return m_aResponseObjects.isNotEmpty() ? m_aResponseObjects.get(0).concepts() : new CommonsArrayList<>();
  }

  /**
   * @deprecated Since beta3; Use {@link #responseObjects()} or {@link #getAllResponseObjects()}
   * and get the concepts through the {@link ResponseObjectPojo} ()} instead
   */
  @Nonnull
  @Deprecated
  @ReturnsMutableCopy
  public final List <ConceptPojo> getAllConcepts ()
  {
    return m_aResponseObjects.isNotEmpty() ? m_aResponseObjects.get(0).getAllConcepts() : new CommonsArrayList<>();
  }

  @Nonnull
  private QueryResponse _createQueryResponse (@Nonnull final ICommonsList <ISlotProvider> aProviders)
  {
    final ICommonsOrderedMap <String, ISlotProvider> aProviderMap = new CommonsLinkedHashMap <> ();
    for (final ISlotProvider aItem : aProviders)
    {
      final String sName = aItem.getName ();
      if (aProviderMap.containsKey (sName))
        throw new IllegalArgumentException ("A slot provider for name '" + sName + "' is already present");
      aProviderMap.put (sName, aItem);
    }

    final QueryResponse ret = RegRepHelper.createEmptyQueryResponse (m_eResponseStatus);
    ret.setRequestId (m_sRequestID);

    // All top-level slots outside of object list
    for (final String sHeader : TOP_LEVEL_SLOTS)
    {
      final ISlotProvider aSP = aProviderMap.get (sHeader);
      if (aSP != null)
        ret.addSlot (aSP.createSlot ());
    }

    {
      if (m_eQueryDefinition.equals (EQueryDefinitionType.OBJECTREF))
      {
        final ObjectRefListType aORList = new ObjectRefListType ();

        for (ResponseObjectPojo aRO : m_aResponseObjects) {
          aORList.addObjectRef(aRO.getAsObjectRef());
        }
        ret.setObjectRefList (aORList);
      }
      else
      {
        final RegistryObjectListType aROList = new RegistryObjectListType ();

        for (ResponseObjectPojo aRO : m_aResponseObjects) {
          aROList.addRegistryObject(aRO.getAsRegistryObject());
        }
        ret.setRegistryObjectList (aROList);
      }
    }

    return ret;
  }

  @Nonnull
  public QueryResponse getAsQueryResponse ()
  {
    final ICommonsList <ISlotProvider> aSlots = new CommonsArrayList <> ();
    if (m_sSpecificationIdentifier != null)
      aSlots.add (new SlotSpecificationIdentifier (m_sSpecificationIdentifier));
    if (m_aIssueDateTime != null)
      aSlots.add (new SlotIssueDateTime (m_aIssueDateTime));
    if (m_aDataProvider != null)
      aSlots.add (new SlotDataProvider (m_aDataProvider));

    return _createQueryResponse (aSlots);
  }

  @Nonnull
  public IVersatileWriter <QueryResponse> getWriter ()
  {
    return new JAXBVersatileWriter <> (getAsQueryResponse (),
                                       RegRep4Writer.queryResponse (CCCEV.XSDS).setFormattedOutput (true));
  }

  /**
   * @deprecated Since beta3; Use {@link #reader()} instead
   */
  @Deprecated
  @Nonnull
  public static IJAXBVersatileReader <EDMResponse> getReader ()
  {
    return reader ();
  }

  @Nonnull
  public static IJAXBVersatileReader <EDMResponse> reader ()
  {
    return new JAXBVersatileReader <> (RegRep4Reader.queryResponse (CCCEV.XSDS), EDMResponse::create);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || getClass () != o.getClass ())
      return false;
    final EDMResponse that = (EDMResponse) o;
    return EqualsHelper.equals (m_eQueryDefinition, that.m_eQueryDefinition) &&
           EqualsHelper.equals (m_eResponseStatus, that.m_eResponseStatus) &&
           EqualsHelper.equals (m_sRequestID, that.m_sRequestID) &&
           EqualsHelper.equals (m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
           EqualsHelper.equals (m_aIssueDateTime, that.m_aIssueDateTime) &&
           EqualsHelper.equals (m_aDataProvider, that.m_aDataProvider) &&
           EqualsHelper.equals (m_aResponseObjects, that.m_aResponseObjects);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eQueryDefinition)
                                       .append (m_eResponseStatus)
                                       .append (m_sRequestID)
                                       .append (m_sSpecificationIdentifier)
                                       .append (m_aIssueDateTime)
                                       .append (m_aDataProvider)
                                       .append (m_aResponseObjects)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("QueryDefinition", m_eQueryDefinition)
                                       .append ("RequestID", m_sRequestID)
                                       .append ("ResponseStatus", m_eResponseStatus)
                                       .append ("SpecificationIdentifier", m_sSpecificationIdentifier)
                                       .append ("IssueDateTime", m_aIssueDateTime)
                                       .append ("DataProvider", m_aDataProvider)
                                       .append ("ResponseObjects", m_aResponseObjects)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    // Use the default specification identifier
    return new Builder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
  }

  @Nonnull
  public static Builder builderConcept ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.CONCEPT);
  }

  @Nonnull
  public static Builder builderDocument ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.DOCUMENT);
  }

  @Nonnull
  public static Builder builderDocumentRef ()
  {
    return builder ().queryDefinition (EQueryDefinitionType.OBJECTREF);
  }

  public static class Builder
  {
    private EQueryDefinitionType m_eQueryDefinition;
    private ERegRepResponseStatus m_eResponseStatus;
    private String m_sRequestID;
    private String m_sSpecificationIdentifier;
    private LocalDateTime m_aIssueDateTime;
    private AgentPojo m_aDataProvider;
    private final ICommonsList <ResponseObjectPojo> m_aResponseObjects = new CommonsArrayList <> ();
    // To support deprecated method calls
    private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();

    protected Builder ()
    {}

    @Nonnull
    public Builder queryDefinition (@Nullable final EQueryDefinitionType e)
    {
      m_eQueryDefinition = e;
      return this;
    }

    @Nonnull
    public Builder responseStatus (@Nullable final ERegRepResponseStatus e)
    {
      m_eResponseStatus = e;
      return this;
    }

    @Nonnull
    public Builder requestID (@Nullable final UUID a)
    {
      return requestID (a == null ? null : a.toString ());
    }

    @Nonnull
    public Builder requestID (@Nullable final String s)
    {
      m_sRequestID = s;
      return this;
    }

    @Nonnull
    public Builder specificationIdentifier (@Nullable final String s)
    {
      m_sSpecificationIdentifier = s;
      return this;
    }

    @Nonnull
    public Builder issueDateTimeNow ()
    {
      return issueDateTime (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public Builder issueDateTime (@Nullable final LocalDateTime a)
    {
      m_aIssueDateTime = a == null ? null : a.truncatedTo (ChronoUnit.MILLIS);
      return this;
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentPojo.Builder a)
    {
      return dataProvider (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentPojo a)
    {
      m_aDataProvider = a;
      return this;
    }

    @Nonnull
    public Builder dataProvider (@Nullable final AgentType a)
    {
      return dataProvider (a == null ? null : AgentPojo.builder (a));
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder addConcept (@Nullable final CCCEVConceptType a)
    {
      return addConcept (a == null ? null : ConceptPojo.builder (a));
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder addConcept (@Nullable final ConceptPojo.Builder a)
    {
      return addConcept (a == null ? null : a.build ());
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder addConcept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.add (a);
      return this;
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder concept (@Nullable final CCCEVConceptType a)
    {
      return concept (a == null ? null : ConceptPojo.builder (a));
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder concept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.set (a);
      else
        m_aConcepts.clear ();
      return this;
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder concepts (@Nullable final ConceptPojo... a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    /**
     * @deprecated Since beta3; Create a {@link ResponseObjectPojo} ()} using
     * {@link #responseObject(ResponseObjectPojo.Builder)} and put the concepts there instead
     */
    @Nonnull
    @Deprecated
    public Builder concepts (@Nullable final Iterable <ConceptPojo> a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Nonnull
    public Builder addResponseObject (@Nullable final RegistryObjectType a)
    {
      return addResponseObject (a == null ? null : ResponseObjectPojo.builder (a));
    }

    @Nonnull
    public Builder addResponseObject (@Nullable final ObjectRefType a)
    {
      return addResponseObject (a == null ? null : ResponseObjectPojo.builder (a));
    }

    @Nonnull
    public Builder addResponseObject (@Nullable final ResponseObjectPojo.Builder a)
    {
      return addResponseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder addResponseObject (@Nullable final ResponseObjectPojo a)
    {
      if (a != null)
        m_aResponseObjects.add (a);
      return this;
    }

    @Nonnull
    public Builder responseObject (@Nullable final RegistryObjectType a)
    {
      return responseObject (a == null ? null : ResponseObjectPojo.builder (a));
    }

    @Nonnull
    public Builder responseObject (@Nullable final ResponseObjectPojo.Builder a)
    {
      return responseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public Builder responseObject (@Nullable final ResponseObjectPojo a)
    {
      if (a != null)
        m_aResponseObjects.set (a);
      else
        m_aResponseObjects.clear ();
      return this;
    }

    @Nonnull
    public Builder responseObject (@Nullable final ResponseObjectPojo... a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Nonnull
    public Builder responseObject (@Nullable final Iterable <ResponseObjectPojo> a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    public void checkConsistency ()
    {
      if (m_eQueryDefinition == null)
        throw new IllegalStateException ("Query Definition must be present");
      if (m_eResponseStatus == null)
        throw new IllegalStateException ("Response Status must be present");
      if (m_eResponseStatus != ERegRepResponseStatus.SUCCESS && m_eResponseStatus != ERegRepResponseStatus.FAILURE)
        throw new IllegalStateException ("Response Status must be SUCCESS or FAILURE");
      if (StringHelper.hasNoText (m_sRequestID))
        throw new IllegalStateException ("Request ID must be present");
      if (StringHelper.hasNoText (m_sSpecificationIdentifier))
        throw new IllegalStateException ("SpecificationIdentifier must be present");
      if (m_aIssueDateTime == null)
        throw new IllegalStateException ("Issue Date Time must be present");
      if (m_aDataProvider == null)
        throw new IllegalStateException ("Data Provider must be present");
      if (m_aResponseObjects.isEmpty ())
        throw new IllegalStateException ("Response Objects must be present");

      switch (m_eQueryDefinition)
      {
        case CONCEPT:
          for(ResponseObjectPojo aResponseObject : m_aResponseObjects)
          {
            if (aResponseObject.concepts ().isEmpty ())
              throw new IllegalStateException ("A Query Definition of type 'Concept' must contain ResponseObjects with a Concept");
            if (aResponseObject.getDataset () != null)
              throw new IllegalStateException ("A Query Definition of type 'Concept' must NOT contain ResponseObjects with a Dataset");
          }
          break;
        case DOCUMENT:
          for(ResponseObjectPojo aResponseObject : m_aResponseObjects)
          {
            if (!aResponseObject.concepts ().isEmpty ())
              throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain ResponseObjects with a Concept");
            if (aResponseObject.getDataset () == null)
              throw new IllegalStateException ("A Query Definition of type 'Document' must contain ResponseObjects with a Dataset");
          }
          break;
        case OBJECTREF:
          for(ResponseObjectPojo aResponseObject : m_aResponseObjects)
          {
            if (!aResponseObject.concepts ().isEmpty ())
              throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain ResponseObjects with a Concept");
            if (aResponseObject.getDataset () == null)
              throw new IllegalStateException ("A Query Definition of type 'Document' must contain ResponseObjects with a Dataset");
            if (aResponseObject.getRepositoryItemRef () != null)
              throw new IllegalStateException ("A Query Definition of type 'ObjectRef' must NOT contain ResponseObjects with a RepositoryItemRef");
          }
          break;
        default:
          throw new IllegalStateException ("Unhandled query definition " + m_eQueryDefinition);
      }
    }

    @Nonnull
    public EDMResponse build ()
    {
      // Support old (deprecated) method of adding concepts
      if(m_aConcepts.isNotEmpty())
        m_aResponseObjects.add (ResponseObjectPojo.builder().randomID().concepts(m_aConcepts).build());

      checkConsistency ();

      return new EDMResponse (m_eQueryDefinition,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              m_aResponseObjects);
    }
  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final EDMResponse.Builder aBuilder)
  {
    final String sName = aSlot.getName ();
    final ValueType aSlotValue = aSlot.getSlotValue ();
    switch (sName)
    {
      case SlotSpecificationIdentifier.NAME:
        if (aSlotValue instanceof StringValueType)
        {
          final String sValue = ((StringValueType) aSlotValue).getValue ();
          aBuilder.specificationIdentifier (sValue);
        }
        break;
      case SlotIssueDateTime.NAME:
        if (aSlotValue instanceof DateTimeValueType)
        {
          final XMLGregorianCalendar aCal = ((DateTimeValueType) aSlotValue).getValue ();
          aBuilder.issueDateTime (PDTXMLConverter.getLocalDateTime (aCal));
        }
        break;
      case SlotDataProvider.NAME:
        if (aSlotValue instanceof AnyValueType)
        {
          final Node aAny = (Node) ((AnyValueType) aSlotValue).getAny ();
          aBuilder.dataProvider (AgentPojo.builder (new AgentMarshaller ().read (aAny)));
        }
        break;
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static EDMResponse create (@Nonnull final QueryResponse aQueryResponse)
  {
    final EDMResponse.Builder aBuilder = EDMResponse.builder ()
                                                    .responseStatus (ERegRepResponseStatus.getFromIDOrNull (aQueryResponse.getStatus ()))
                                                    .requestID (aQueryResponse.getRequestId ());

    for (final SlotType s : aQueryResponse.getSlot ())
      _applySlots (s, aBuilder);

    if (aQueryResponse.getRegistryObjectList () != null &&
        aQueryResponse.getRegistryObjectList ().hasRegistryObjectEntries ())
    {
      for (final RegistryObjectType aRO : aQueryResponse.getRegistryObjectList ().getRegistryObject ())
       aBuilder.addResponseObject(aRO);

      int nConcepts=0;
      int nDatasets=0;

      for(final ResponseObjectPojo aResponseObject : aBuilder.m_aResponseObjects){
        nConcepts+=aResponseObject.concepts().size();
        if(aResponseObject.getDataset() != null)
          nDatasets++;
      }
      if ((nConcepts!=0) && (nDatasets!=0))
        throw new IllegalStateException("The response contains both concepts and datasets which is not allowed.");
      if ((nConcepts==0) && (nDatasets==0))
        throw new IllegalStateException("The response contains no concepts or datasets.");
      if (nConcepts!=0)
        aBuilder.queryDefinition(EQueryDefinitionType.CONCEPT);
      if (nDatasets!=0)
        aBuilder.queryDefinition(EQueryDefinitionType.DOCUMENT);
    }

    if (aQueryResponse.getObjectRefList () != null && aQueryResponse.getObjectRefList ().hasObjectRefEntries ())
    {
      for (final ObjectRefType aOR : aQueryResponse.getObjectRefList ().getObjectRef ())
        aBuilder.addResponseObject(aOR);

      aBuilder.queryDefinition (EQueryDefinitionType.OBJECTREF);
    }

    return aBuilder.build ();
  }
}
