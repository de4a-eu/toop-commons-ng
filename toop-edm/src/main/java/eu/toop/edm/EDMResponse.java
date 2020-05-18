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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Node;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.CommonsLinkedHashMap;
import com.helger.commons.collection.impl.CommonsLinkedHashSet;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsOrderedMap;
import com.helger.commons.collection.impl.ICommonsOrderedSet;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.traits.IGenericImplTrait;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.jaxb.cv.agent.AgentType;
import eu.toop.edm.jaxb.dcatap.DCatAPDatasetType;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.RepositoryItemRefPojo;
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
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.DateTimeValueType;
import eu.toop.regrep.rim.ObjectRefListType;
import eu.toop.regrep.rim.ObjectRefType;
import eu.toop.regrep.rim.RegistryObjectListType;
import eu.toop.regrep.rim.RegistryObjectType;
import eu.toop.regrep.rim.SimpleLinkType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.StringValueType;
import eu.toop.regrep.rim.ValueType;
import eu.toop.regrep.slot.ISlotProvider;

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

  protected EDMResponse (@Nonnull final EQueryDefinitionType eQueryDefinition,
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
        ValueEnforcer.isEqual (1, aResponseObjects.size (), "ResponseObject count");
        for (final ResponseObjectPojo aResponseObject : aResponseObjects)
          ValueEnforcer.notEmpty (aResponseObject.concepts (), "Concept");
        break;
      case DOCUMENT:
      case OBJECTREF:
        for (final ResponseObjectPojo aResponseObject : aResponseObjects)
          ValueEnforcer.notNull (aResponseObject.getDataset (), "Dataset");
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
    m_aResponseObjects.addAll (aResponseObjects);
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

    if (m_eQueryDefinition.equals (EQueryDefinitionType.OBJECTREF))
    {
      final ObjectRefListType aORList = new ObjectRefListType ();
      for (final ResponseObjectPojo aRO : m_aResponseObjects)
        aORList.addObjectRef (aRO.getAsObjectRef ());
      ret.setObjectRefList (aORList);
    }
    else
    {
      final RegistryObjectListType aROList = new RegistryObjectListType ();
      for (final ResponseObjectPojo aRO : m_aResponseObjects)
        aROList.addRegistryObject (aRO.getAsRegistryObject ());
      ret.setRegistryObjectList (aROList);
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
  public static ConceptBuilder builderConcept ()
  {
    return new ConceptBuilder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
  }

  @Nonnull
  public static DocumentBuilder builderDocument ()
  {
    return new DocumentBuilder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
  }

  @Nonnull
  public static DocumentRefBuilder builderDocumentRef ()
  {
    return new DocumentRefBuilder ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
  }

  /**
   * Abstract builder for the main builders
   *
   * @author Philip Helger
   * @param <T>
   *        The effective builder type
   */
  public static abstract class AbstractBuilder <T extends AbstractBuilder <T>> implements IGenericImplTrait <T>
  {
    protected final EQueryDefinitionType m_eQueryDefinition;
    protected ERegRepResponseStatus m_eResponseStatus;
    protected String m_sRequestID;
    protected String m_sSpecificationIdentifier;
    protected LocalDateTime m_aIssueDateTime;
    protected AgentPojo m_aDataProvider;

    protected AbstractBuilder (@Nonnull final EQueryDefinitionType e)
    {
      ValueEnforcer.notNull (e, "QueryDefinitionType");
      m_eQueryDefinition = e;
    }

    @Nonnull
    public final T responseStatus (@Nullable final ERegRepResponseStatus e)
    {
      m_eResponseStatus = e;
      return thisAsT ();
    }

    @Nonnull
    public final T requestID (@Nullable final UUID a)
    {
      return requestID (a == null ? null : a.toString ());
    }

    @Nonnull
    public final T requestID (@Nullable final String s)
    {
      m_sRequestID = s;
      return thisAsT ();
    }

    @Nonnull
    public final T specificationIdentifier (@Nullable final String s)
    {
      m_sSpecificationIdentifier = s;
      return thisAsT ();
    }

    @Nonnull
    public final T issueDateTimeNow ()
    {
      return issueDateTime (PDTFactory.getCurrentLocalDateTime ());
    }

    @Nonnull
    public final T issueDateTime (@Nullable final LocalDateTime a)
    {
      m_aIssueDateTime = a == null ? null : a.truncatedTo (ChronoUnit.MILLIS);
      return thisAsT ();
    }

    @Nonnull
    public final T dataProvider (@Nullable final AgentPojo.Builder a)
    {
      return dataProvider (a == null ? null : a.build ());
    }

    @Nonnull
    public final T dataProvider (@Nullable final AgentPojo a)
    {
      m_aDataProvider = a;
      return thisAsT ();
    }

    @Nonnull
    public final T dataProvider (@Nullable final AgentType a)
    {
      return dataProvider (a == null ? null : AgentPojo.builder (a));
    }

    @OverridingMethodsMustInvokeSuper
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
    }

    @Nonnull
    public abstract EDMResponse build ();
  }

  /**
   * A builder for Concept responses.
   *
   * @author Philip Helger
   */
  public static class ConceptBuilder extends AbstractBuilder <ConceptBuilder>
  {
    private String m_sID;
    private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();

    public ConceptBuilder ()
    {
      super (EQueryDefinitionType.CONCEPT);
    }

    @Nonnull
    public ConceptBuilder id (@Nullable final String s)
    {
      m_sID = s;
      return this;
    }

    @Nonnull
    public ConceptBuilder randomID ()
    {
      return id (UUID.randomUUID ().toString ());
    }

    @Nonnull
    public ConceptBuilder addConcept (@Nullable final CCCEVConceptType a)
    {
      return addConcept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public ConceptBuilder addConcept (@Nullable final ConceptPojo.Builder a)
    {
      return addConcept (a == null ? null : a.build ());
    }

    @Nonnull
    public ConceptBuilder addConcept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.add (a);
      return this;
    }

    @Nonnull
    public ConceptBuilder concept (@Nullable final CCCEVConceptType a)
    {
      return concept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public ConceptBuilder concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    @Nonnull
    public ConceptBuilder concept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.set (a);
      else
        m_aConcepts.clear ();
      return this;
    }

    @Nonnull
    public ConceptBuilder concepts (@Nullable final ConceptPojo... a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Nonnull
    public ConceptBuilder concepts (@Nullable final Iterable <ConceptPojo> a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Override
    public void checkConsistency ()
    {
      super.checkConsistency ();

      if (m_aConcepts.isEmpty ())
        throw new IllegalStateException ("A Query Definition of type 'Concept' must contain at least one Concept");
    }

    @Override
    @Nonnull
    public EDMResponse build ()
    {
      checkConsistency ();

      // Build the ResponseObjectPojo
      final ICommonsList <ResponseObjectPojo> aResponseObjects = new CommonsArrayList <> ();
      aResponseObjects.add (ResponseObjectPojo.builder ().id (m_sID).concepts (m_aConcepts).build ());

      return new EDMResponse (m_eQueryDefinition,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              aResponseObjects);
    }
  }

  /**
   * A builder for document responses.
   *
   * @author Philip Helger
   */
  public static class DocumentBuilder extends AbstractBuilder <DocumentBuilder>
  {
    private String m_sID;
    private DatasetPojo m_aDataset;
    private RepositoryItemRefPojo m_aRepositoryItemRef;

    public DocumentBuilder ()
    {
      super (EQueryDefinitionType.DOCUMENT);
    }

    @Nonnull
    public DocumentBuilder id (@Nullable final String s)
    {
      m_sID = s;
      return this;
    }

    @Nonnull
    public DocumentBuilder randomID ()
    {
      return id (UUID.randomUUID ().toString ());
    }

    @Nonnull
    public DocumentBuilder dataset (@Nullable final DatasetPojo.Builder a)
    {
      return dataset (a == null ? null : a.build ());
    }

    @Nonnull
    public DocumentBuilder dataset (@Nullable final DatasetPojo a)
    {
      m_aDataset = a;
      return this;
    }

    @Nonnull
    public DocumentBuilder dataset (@Nullable final DCatAPDatasetType a)
    {
      return dataset (a == null ? null : DatasetPojo.builder (a));
    }

    @Nonnull
    public DocumentBuilder repositoryItemRef (@Nullable final RepositoryItemRefPojo.Builder a)
    {
      return repositoryItemRef (a == null ? null : a.build ());
    }

    @Nonnull
    public DocumentBuilder repositoryItemRef (@Nullable final RepositoryItemRefPojo a)
    {
      m_aRepositoryItemRef = a;
      return this;
    }

    @Nonnull
    public DocumentBuilder repositoryItemRef (@Nullable final SimpleLinkType a)
    {
      return repositoryItemRef (a == null ? null : RepositoryItemRefPojo.builder (a));
    }

    @Override
    public void checkConsistency ()
    {
      super.checkConsistency ();

      if (m_aDataset == null)
        throw new IllegalStateException ("A Query Definition of type 'Document' must contain a Dataset");
    }

    @Override
    @Nonnull
    public EDMResponse build ()
    {
      checkConsistency ();

      // Build the ResponseObjectPojo
      final ICommonsList <ResponseObjectPojo> aResponseObjects = new CommonsArrayList <> ();
      aResponseObjects.add (ResponseObjectPojo.builder ()
                                              .id (m_sID)
                                              .dataset (m_aDataset)
                                              .repositoryItemRef (m_aRepositoryItemRef)
                                              .build ());

      return new EDMResponse (m_eQueryDefinition,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              aResponseObjects);
    }
  }

  public static class DocumentRefBuilder extends AbstractBuilder <DocumentRefBuilder>
  {
    private final ICommonsList <ResponseObjectPojo> m_aResponseObjects = new CommonsArrayList <> ();

    public DocumentRefBuilder ()
    {
      super (EQueryDefinitionType.OBJECTREF);
    }

    @Nonnull
    public DocumentRefBuilder addResponseObject (@Nullable final RegistryObjectType a)
    {
      return addResponseObject (a == null ? null : ResponseObjectPojo.builder (a));
    }

    @Nonnull
    public DocumentRefBuilder addResponseObject (@Nullable final ObjectRefType a)
    {
      return addResponseObject (a == null ? null : ResponseObjectPojo.builder (a));
    }

    @Nonnull
    public DocumentRefBuilder addResponseObject (@Nullable final ResponseObjectPojo.Builder a)
    {
      return addResponseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public DocumentRefBuilder addResponseObject (@Nullable final ResponseObjectPojo a)
    {
      if (a != null)
        m_aResponseObjects.add (a);
      return this;
    }

    @Nonnull
    public DocumentRefBuilder responseObject (@Nullable final RegistryObjectType a)
    {
      return responseObject (a == null ? null : ResponseObjectPojo.builder (a));
    }

    @Nonnull
    public DocumentRefBuilder responseObject (@Nullable final ResponseObjectPojo.Builder a)
    {
      return responseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public DocumentRefBuilder responseObject (@Nullable final ResponseObjectPojo a)
    {
      if (a != null)
        m_aResponseObjects.set (a);
      else
        m_aResponseObjects.clear ();
      return this;
    }

    @Nonnull
    public DocumentRefBuilder responseObjects (@Nullable final ResponseObjectPojo... a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Nonnull
    public DocumentRefBuilder responseObjects (@Nullable final Iterable <ResponseObjectPojo> a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Override
    public void checkConsistency ()
    {
      super.checkConsistency ();
      if (m_aResponseObjects.isEmpty ())
        throw new IllegalStateException ("Response Objects must be present");

      for (final ResponseObjectPojo aResponseObject : m_aResponseObjects)
      {
        if (!aResponseObject.concepts ().isEmpty ())
          throw new IllegalStateException ("A Query Definition of type 'Document' must NOT contain ResponseObjects with a Concept");
        if (aResponseObject.getDataset () == null)
          throw new IllegalStateException ("A Query Definition of type 'Document' must contain ResponseObjects with a Dataset");
        if (aResponseObject.getRepositoryItemRef () != null)
          throw new IllegalStateException ("A Query Definition of type 'ObjectRef' must NOT contain ResponseObjects with a RepositoryItemRef");
      }
    }

    @Override
    @Nonnull
    public EDMResponse build ()
    {
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

  private static void _applySlots (@Nonnull final SlotType aSlot,
                                   @Nonnull final EDMResponse.AbstractBuilder <?> aBuilder)
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

  @Nullable
  public static EDMResponse create (@Nonnull final QueryResponse aQueryResponse)
  {
    final ERegRepResponseStatus eResponseStatus = ERegRepResponseStatus.getFromIDOrNull (aQueryResponse.getStatus ());
    final String sRequestID = aQueryResponse.getRequestId ();

    if (aQueryResponse.getObjectRefList () != null && aQueryResponse.getObjectRefList ().hasObjectRefEntries ())
    {
      // Document Reference
      final DocumentRefBuilder aRealBuilder = builderDocumentRef ().responseStatus (eResponseStatus)
                                                                   .requestID (sRequestID);
      for (final SlotType s : aQueryResponse.getSlot ())
        _applySlots (s, aRealBuilder);

      for (final ObjectRefType aOR : aQueryResponse.getObjectRefList ().getObjectRef ())
        aRealBuilder.addResponseObject (aOR);
      return aRealBuilder.build ();
    }

    if (aQueryResponse.getRegistryObjectList () != null &&
        aQueryResponse.getRegistryObjectList ().hasRegistryObjectEntries ())
    {
      // Can be only one RegistryObject
      final RegistryObjectType aRO = aQueryResponse.getRegistryObjectList ().getRegistryObject ().get (0);
      final ResponseObjectPojo aROP = ResponseObjectPojo.builder (aRO).build ();

      final AbstractBuilder <?> aBuilder;
      if (aROP.getDataset () != null)
      {
        // Must be a Document response
        aBuilder = builderDocument ().responseStatus (eResponseStatus)
                                     .requestID (sRequestID)
                                     .id (aROP.getID ())
                                     .dataset (aROP.getDataset ())
                                     .repositoryItemRef (aROP.getRepositoryItemRef ());
      }
      else
      {
        // Must be a Concept response
        aBuilder = builderConcept ().responseStatus (eResponseStatus)
                                    .requestID (sRequestID)
                                    .id (aROP.getID ())
                                    .concepts (aROP.concepts ());
      }

      for (final SlotType s : aQueryResponse.getSlot ())
        _applySlots (s, aBuilder);

      return aBuilder.build ();
    }

    return null;
  }
}
