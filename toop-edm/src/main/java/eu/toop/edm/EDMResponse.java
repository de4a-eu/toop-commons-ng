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
import java.util.function.Consumer;

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
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.EResponseOptionType;
import eu.toop.edm.response.EDMResponsePayloadConcepts;
import eu.toop.edm.response.IEDMResponsePayloadProvider;
import eu.toop.edm.response.ResponseDocumentPojo;
import eu.toop.edm.response.ResponseDocumentReferencePojo;
import eu.toop.edm.slot.SlotConceptValues;
import eu.toop.edm.slot.SlotDataProvider;
import eu.toop.edm.slot.SlotIssueDateTime;
import eu.toop.edm.slot.SlotSpecificationIdentifier;
import eu.toop.edm.xml.IJAXBVersatileReader;
import eu.toop.edm.xml.IVersatileWriter;
import eu.toop.edm.xml.JAXBVersatileReader;
import eu.toop.edm.xml.JAXBVersatileWriter;
import eu.toop.edm.xml.cagv.AgentMarshaller;
import eu.toop.edm.xml.cccev.CCCEV;
import eu.toop.edm.xml.cccev.ConceptMarshaller;
import eu.toop.regrep.ERegRepResponseStatus;
import eu.toop.regrep.RegRep4Reader;
import eu.toop.regrep.RegRep4Writer;
import eu.toop.regrep.RegRepHelper;
import eu.toop.regrep.query.QueryResponse;
import eu.toop.regrep.rim.AnyValueType;
import eu.toop.regrep.rim.CollectionValueType;
import eu.toop.regrep.rim.DateTimeValueType;
import eu.toop.regrep.rim.ExtrinsicObjectType;
import eu.toop.regrep.rim.ObjectRefListType;
import eu.toop.regrep.rim.ObjectRefType;
import eu.toop.regrep.rim.RegistryObjectListType;
import eu.toop.regrep.rim.RegistryObjectType;
import eu.toop.regrep.rim.SlotType;
import eu.toop.regrep.rim.StringValueType;
import eu.toop.regrep.rim.ValueType;
import eu.toop.regrep.slot.ISlotProvider;

/**
 * This class contains the data model for a single TOOP EDM Request. It requires
 * at least the following fields:
 * <ul>
 * <li>ResponseOption - "Registry Object" or "Object Reference"?</li>
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
public class EDMResponse implements IEDMTopLevelObject
{
  private static final ICommonsOrderedSet <String> TOP_LEVEL_SLOTS = new CommonsLinkedHashSet <> (SlotSpecificationIdentifier.NAME,
                                                                                                  SlotIssueDateTime.NAME,
                                                                                                  SlotDataProvider.NAME);

  private final EResponseOptionType m_eResponseOption;
  private final ERegRepResponseStatus m_eResponseStatus;
  private final String m_sRequestID;
  private final String m_sSpecificationIdentifier;
  private final LocalDateTime m_aIssueDateTime;
  private final AgentPojo m_aDataProvider;
  private final ICommonsList <IEDMResponsePayloadProvider> m_aPayloadProviders = new CommonsArrayList <> ();

  protected EDMResponse (@Nonnull final EResponseOptionType eResponseOption,
                         @Nonnull final ERegRepResponseStatus eResponseStatus,
                         @Nonnull @Nonempty final String sRequestID,
                         @Nonnull @Nonempty final String sSpecificationIdentifier,
                         @Nonnull final LocalDateTime aIssueDateTime,
                         @Nonnull final AgentPojo aDataProvider,
                         @Nonnull final ICommonsList <? extends IEDMResponsePayloadProvider> aPayloadProviders)
  {
    ValueEnforcer.notNull (eResponseOption, "ResponseOption");
    ValueEnforcer.notNull (eResponseStatus, "ResponseStatus");
    ValueEnforcer.isTrue (eResponseStatus == ERegRepResponseStatus.SUCCESS ||
                          eResponseStatus == ERegRepResponseStatus.FAILURE,
                          "Only SUCCESS and FAILURE are supported");
    ValueEnforcer.notEmpty (sRequestID, "RequestID");
    ValueEnforcer.notEmpty (sSpecificationIdentifier, "SpecificationIdentifier");
    ValueEnforcer.notNull (aIssueDateTime, "IssueDateTime");
    ValueEnforcer.notNull (aDataProvider, "DataProvider");
    ValueEnforcer.notEmpty (aPayloadProviders, "PayloadProviders");

    m_eResponseOption = eResponseOption;
    m_eResponseStatus = eResponseStatus;
    m_sRequestID = sRequestID;
    m_sSpecificationIdentifier = sSpecificationIdentifier;
    m_aIssueDateTime = aIssueDateTime;
    m_aDataProvider = aDataProvider;
    m_aPayloadProviders.addAll (aPayloadProviders);
  }

  @Nonnull
  public final EResponseOptionType getResponseOption ()
  {
    return m_eResponseOption;
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
  public final List <IEDMResponsePayloadProvider> payloadProviders ()
  {
    return m_aPayloadProviders;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <IEDMResponsePayloadProvider> getAllPayloadProviders ()
  {
    return m_aPayloadProviders.getClone ();
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

    switch (m_eResponseOption)
    {
      case INLINE:
        final RegistryObjectListType aROList = new RegistryObjectListType ();
        for (final IEDMResponsePayloadProvider aItem : m_aPayloadProviders)
          aROList.addRegistryObject (aItem.getAsRegistryObject ());
        ret.setRegistryObjectList (aROList);
        break;
      case REFERENCE:
        final ObjectRefListType aORList = new ObjectRefListType ();
        for (final IEDMResponsePayloadProvider aItem : m_aPayloadProviders)
          aORList.addObjectRef (aItem.getAsObjectRef ());
        ret.setObjectRefList (aORList);
        break;
      default:
        throw new IllegalStateException ("Found unsupported ResponseOption " + m_eResponseOption);
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
    return EqualsHelper.equals (m_eResponseOption, that.m_eResponseOption) &&
           EqualsHelper.equals (m_eResponseStatus, that.m_eResponseStatus) &&
           EqualsHelper.equals (m_sRequestID, that.m_sRequestID) &&
           EqualsHelper.equals (m_sSpecificationIdentifier, that.m_sSpecificationIdentifier) &&
           EqualsHelper.equals (m_aIssueDateTime, that.m_aIssueDateTime) &&
           EqualsHelper.equals (m_aDataProvider, that.m_aDataProvider) &&
           EqualsHelper.equals (m_aPayloadProviders, that.m_aPayloadProviders);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eResponseOption)
                                       .append (m_eResponseStatus)
                                       .append (m_sRequestID)
                                       .append (m_sSpecificationIdentifier)
                                       .append (m_aIssueDateTime)
                                       .append (m_aDataProvider)
                                       .append (m_aPayloadProviders)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ResponseOption", m_eResponseOption)
                                       .append ("RequestID", m_sRequestID)
                                       .append ("ResponseStatus", m_eResponseStatus)
                                       .append ("SpecificationIdentifier", m_sSpecificationIdentifier)
                                       .append ("IssueDateTime", m_aIssueDateTime)
                                       .append ("DataProvider", m_aDataProvider)
                                       .append ("ResponseObjects", m_aPayloadProviders)
                                       .getToString ();
  }

  @Nonnull
  public static BuilderConcept builderConcept ()
  {
    // RegistryObjectID doesn't matter for concepts but must be settable in
    // import for comparison
    return new BuilderConcept ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20)
                                .randomRegistryObjectID ();
  }

  @Nonnull
  public static BuilderDocument builderDocument ()
  {
    return new BuilderDocument ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
  }

  @Nonnull
  public static BuilderDocumentReference builderDocumentReference ()
  {
    return new BuilderDocumentReference ().specificationIdentifier (CToopEDM.SPECIFICATION_IDENTIFIER_TOOP_EDM_V20);
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
    protected EResponseOptionType m_eResponseOption;
    protected ERegRepResponseStatus m_eResponseStatus;
    protected String m_sRequestID;
    protected String m_sSpecificationIdentifier;
    protected LocalDateTime m_aIssueDateTime;
    protected AgentPojo m_aDataProvider;

    protected AbstractBuilder (@Nonnull final EResponseOptionType e)
    {
      ValueEnforcer.notNull (e, "ResponseOption");
      m_eResponseOption = e;
    }

    /**
     * This element was replaced with the "ResponseOptionType"
     *
     * @param e
     *        Query definition type.
     * @return this for chaining
     * @deprecated Since beta3; not needed anymore
     */
    @Nonnull
    @Deprecated
    public final T queryDefinition (@Nullable final EQueryDefinitionType e)
    {
      return thisAsT ();
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
    public final T dataProvider (@Nullable final Consumer <? super AgentPojo.Builder> a)
    {
      if (a != null)
      {
        final AgentPojo.Builder aBuilder = AgentPojo.builder ();
        a.accept (aBuilder);
        dataProvider (aBuilder.build ());
      }
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
      if (m_eResponseOption == null)
        throw new IllegalStateException ("Response Layout MUST must be present");
      if (m_eResponseStatus == null)
        throw new IllegalStateException ("Response Status MUST be present");
      if (m_eResponseStatus != ERegRepResponseStatus.SUCCESS && m_eResponseStatus != ERegRepResponseStatus.FAILURE)
        throw new IllegalStateException ("Response Status MUST be SUCCESS or FAILURE");
      if (StringHelper.hasNoText (m_sRequestID))
        throw new IllegalStateException ("Request ID MUST be present");
      if (StringHelper.hasNoText (m_sSpecificationIdentifier))
        throw new IllegalStateException ("SpecificationIdentifier MUST be present");
      if (m_aIssueDateTime == null)
        throw new IllegalStateException ("Issue Date Time MUST be present");
      if (m_aDataProvider == null)
        throw new IllegalStateException ("Data Provider MUST be present");
    }

    @Nonnull
    public abstract EDMResponse build ();
  }

  /**
   * A builder for Concept responses. Contains exactly 1 response.
   *
   * @author Philip Helger
   */
  public static class BuilderConcept extends AbstractBuilder <BuilderConcept>
  {
    private String m_sRegistryObjectID;
    private final ICommonsList <ConceptPojo> m_aConcepts = new CommonsArrayList <> ();

    protected BuilderConcept ()
    {
      // Always inline responses
      super (EResponseOptionType.INLINE);
    }

    @Nonnull
    public BuilderConcept registryObjectID (@Nullable final String s)
    {
      m_sRegistryObjectID = s;
      return this;
    }

    @Nonnull
    public BuilderConcept randomRegistryObjectID ()
    {
      return registryObjectID (UUID.randomUUID ().toString ());
    }

    @Nonnull
    public final BuilderConcept addConcept (@Nullable final Consumer <? super ConceptPojo.Builder> a)
    {
      if (a != null)
      {
        final ConceptPojo.Builder aBuilder = ConceptPojo.builder ();
        a.accept (aBuilder);
        addConcept (aBuilder.build ());
      }
      return this;
    }

    @Nonnull
    public BuilderConcept addConcept (@Nullable final CCCEVConceptType a)
    {
      return addConcept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public BuilderConcept addConcept (@Nullable final ConceptPojo.Builder a)
    {
      return addConcept (a == null ? null : a.build ());
    }

    @Nonnull
    public BuilderConcept addConcept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.add (a);
      return this;
    }

    @Nonnull
    public final BuilderConcept concept (@Nullable final Consumer <? super ConceptPojo.Builder> a)
    {
      if (a != null)
      {
        final ConceptPojo.Builder aBuilder = ConceptPojo.builder ();
        a.accept (aBuilder);
        concept (aBuilder.build ());
      }
      return this;
    }

    @Nonnull
    public BuilderConcept concept (@Nullable final CCCEVConceptType a)
    {
      return concept (a == null ? null : ConceptPojo.builder (a));
    }

    @Nonnull
    public BuilderConcept concept (@Nullable final ConceptPojo.Builder a)
    {
      return concept (a == null ? null : a.build ());
    }

    @Nonnull
    public BuilderConcept concept (@Nullable final ConceptPojo a)
    {
      if (a != null)
        m_aConcepts.set (a);
      else
        m_aConcepts.clear ();
      return this;
    }

    @Nonnull
    public BuilderConcept concepts (@Nullable final ConceptPojo... a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Nonnull
    public BuilderConcept concepts (@Nullable final Iterable <ConceptPojo> a)
    {
      m_aConcepts.setAll (a);
      return this;
    }

    @Override
    public void checkConsistency ()
    {
      super.checkConsistency ();

      if (StringHelper.hasNoText (m_sRegistryObjectID))
        throw new IllegalStateException ("RegistryObjectID MUST be present");
      if (m_aConcepts.isEmpty ())
        throw new IllegalStateException ("At least one Concept MUST be contained");
    }

    @Override
    @Nonnull
    public EDMResponse build ()
    {
      checkConsistency ();

      // Build the ResponseObjectPojo
      final ICommonsList <IEDMResponsePayloadProvider> aResponseObjects = new CommonsArrayList <> ();
      aResponseObjects.add (new EDMResponsePayloadConcepts (m_sRegistryObjectID, m_aConcepts));

      return new EDMResponse (m_eResponseOption,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              aResponseObjects);
    }
  }

  /**
   * A builder for document responses. Contains 1-n payloads.
   *
   * @author Philip Helger
   */
  public static class BuilderDocument extends AbstractBuilder <BuilderDocument>
  {
    private final ICommonsList <ResponseDocumentPojo> m_aResponseObjects = new CommonsArrayList <> ();

    protected BuilderDocument ()
    {
      // Always inline responses
      super (EResponseOptionType.INLINE);
    }

    @Nonnull
    public BuilderDocument addResponseObject (@Nonnull final Consumer <? super ResponseDocumentPojo.Builder> a)
    {
      if (a != null)
      {
        // RegistryObject ID not relevant for inline responses
        final ResponseDocumentPojo.Builder aBuilder = ResponseDocumentPojo.builder ().randomRegistryObjectID ();
        a.accept (aBuilder);
        addResponseObject (aBuilder);
      }
      return this;
    }

    @Nonnull
    public BuilderDocument addResponseObject (@Nullable final ExtrinsicObjectType a)
    {
      return addResponseObject (a == null ? null : ResponseDocumentPojo.builder (a));
    }

    @Nonnull
    public BuilderDocument addResponseObject (@Nullable final ResponseDocumentPojo.Builder a)
    {
      return addResponseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public BuilderDocument addResponseObject (@Nullable final ResponseDocumentPojo a)
    {
      if (a != null)
        m_aResponseObjects.add (a);
      return this;
    }

    @Nonnull
    public BuilderDocument responseObject (@Nonnull final Consumer <? super ResponseDocumentPojo.Builder> a)
    {
      if (a != null)
      {
        // RegistryObject ID not relevant for inline responses
        final ResponseDocumentPojo.Builder aBuilder = ResponseDocumentPojo.builder ().randomRegistryObjectID ();
        a.accept (aBuilder);
        responseObject (aBuilder);
      }
      return this;
    }

    @Nonnull
    public BuilderDocument responseObject (@Nullable final ExtrinsicObjectType a)
    {
      return responseObject (a == null ? null : ResponseDocumentPojo.builder (a));
    }

    @Nonnull
    public BuilderDocument responseObject (@Nullable final ResponseDocumentPojo.Builder a)
    {
      return responseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public BuilderDocument responseObject (@Nullable final ResponseDocumentPojo a)
    {
      if (a != null)
        m_aResponseObjects.set (a);
      else
        m_aResponseObjects.clear ();
      return this;
    }

    @Nonnull
    public BuilderDocument responseObjects (@Nullable final ResponseDocumentPojo... a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Nonnull
    public BuilderDocument responseObjects (@Nullable final Iterable <ResponseDocumentPojo> a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Override
    public void checkConsistency ()
    {
      super.checkConsistency ();

      if (m_aResponseObjects.isEmpty ())
        throw new IllegalStateException ("Response Object MUST be present");
    }

    @Override
    @Nonnull
    public EDMResponse build ()
    {
      checkConsistency ();

      return new EDMResponse (m_eResponseOption,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              m_aResponseObjects);
    }
  }

  public static class BuilderDocumentReference extends AbstractBuilder <BuilderDocumentReference>
  {
    private final ICommonsList <ResponseDocumentReferencePojo> m_aResponseObjects = new CommonsArrayList <> ();

    protected BuilderDocumentReference ()
    {
      // Always object references
      super (EResponseOptionType.REFERENCE);
    }

    @Nonnull
    public BuilderDocumentReference addResponseObject (@Nonnull final Consumer <? super ResponseDocumentReferencePojo.Builder> a)
    {
      if (a != null)
      {
        final ResponseDocumentReferencePojo.Builder aBuilder = ResponseDocumentReferencePojo.builder ();
        a.accept (aBuilder);
        addResponseObject (aBuilder);
      }
      return this;
    }

    @Nonnull
    public BuilderDocumentReference addResponseObject (@Nullable final ObjectRefType a)
    {
      return addResponseObject (a == null ? null : ResponseDocumentReferencePojo.builder (a));
    }

    @Nonnull
    public BuilderDocumentReference addResponseObject (@Nullable final ResponseDocumentReferencePojo.Builder a)
    {
      return addResponseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public BuilderDocumentReference addResponseObject (@Nullable final ResponseDocumentReferencePojo a)
    {
      if (a != null)
        m_aResponseObjects.add (a);
      return this;
    }

    @Nonnull
    public BuilderDocumentReference responseObject (@Nonnull final Consumer <? super ResponseDocumentReferencePojo.Builder> a)
    {
      if (a != null)
      {
        final ResponseDocumentReferencePojo.Builder aBuilder = ResponseDocumentReferencePojo.builder ();
        a.accept (aBuilder);
        responseObject (aBuilder);
      }
      return this;
    }

    @Nonnull
    public BuilderDocumentReference responseObject (@Nullable final ObjectRefType a)
    {
      return responseObject (a == null ? null : ResponseDocumentReferencePojo.builder (a));
    }

    @Nonnull
    public BuilderDocumentReference responseObject (@Nullable final ResponseDocumentReferencePojo.Builder a)
    {
      return responseObject (a == null ? null : a.build ());
    }

    @Nonnull
    public BuilderDocumentReference responseObject (@Nullable final ResponseDocumentReferencePojo a)
    {
      if (a != null)
        m_aResponseObjects.set (a);
      else
        m_aResponseObjects.clear ();
      return this;
    }

    @Nonnull
    public BuilderDocumentReference responseObjects (@Nullable final ResponseDocumentReferencePojo... a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Nonnull
    public BuilderDocumentReference responseObjects (@Nullable final Iterable <ResponseDocumentReferencePojo> a)
    {
      m_aResponseObjects.setAll (a);
      return this;
    }

    @Override
    public void checkConsistency ()
    {
      super.checkConsistency ();

      if (m_aResponseObjects.isEmpty ())
        throw new IllegalStateException ("Response Object MUST be present");
    }

    @Override
    @Nonnull
    public EDMResponse build ()
    {
      checkConsistency ();

      return new EDMResponse (m_eResponseOption,
                              m_eResponseStatus,
                              m_sRequestID,
                              m_sSpecificationIdentifier,
                              m_aIssueDateTime,
                              m_aDataProvider,
                              m_aResponseObjects);
    }

  }

  private static void _applySlots (@Nonnull final SlotType aSlot, @Nonnull final AbstractBuilder <?> aBuilder)
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

  private static void _applyConceptSlots (@Nonnull final SlotType aSlot, @Nonnull final BuilderConcept aBuilder)
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
      default:
        throw new IllegalStateException ("Slot is not defined: " + sName);
    }
  }

  @Nonnull
  public static EDMResponse create (@Nonnull final QueryResponse aQueryResponse)
  {
    // Get common stuff
    final ERegRepResponseStatus eResponseStatus = ERegRepResponseStatus.getFromIDOrNull (aQueryResponse.getStatus ());
    final String sRequestID = aQueryResponse.getRequestId ();

    // Check references
    final ObjectRefListType aObjectRefList = aQueryResponse.getObjectRefList ();
    if (aObjectRefList != null && aObjectRefList.hasObjectRefEntries ())
    {
      // Document Reference
      final BuilderDocumentReference aRealBuilder = builderDocumentReference ().responseStatus (eResponseStatus)
                                                                               .requestID (sRequestID);
      for (final SlotType aSlot : aQueryResponse.getSlot ())
        _applySlots (aSlot, aRealBuilder);

      for (final ObjectRefType aOR : aObjectRefList.getObjectRef ())
        aRealBuilder.addResponseObject (aOR);
      return aRealBuilder.build ();
    }

    // Check inline
    final RegistryObjectListType aRegistryObjectList = aQueryResponse.getRegistryObjectList ();
    if (aRegistryObjectList != null && aRegistryObjectList.hasRegistryObjectEntries ())
    {
      if (aRegistryObjectList.getRegistryObject ().size () == 1 &&
          aRegistryObjectList.getRegistryObjectAtIndex (0).getSlotCount () == 1 &&
          SlotConceptValues.NAME.equals (aRegistryObjectList.getRegistryObjectAtIndex (0)
                                                            .getSlotAtIndex (0)
                                                            .getName ()))
      {
        // It's a Concept Response
        final RegistryObjectType aRO = aRegistryObjectList.getRegistryObject ().get (0);
        final BuilderConcept aRealBuilder = builderConcept ().responseStatus (eResponseStatus)
                                                             .requestID (sRequestID)
                                                             .registryObjectID (aRO.getId ());

        // Apply top-level response slots
        for (final SlotType aSlot : aQueryResponse.getSlot ())
          _applySlots (aSlot, aRealBuilder);

        // Read main concepts
        for (final SlotType aSlot : aRO.getSlot ())
          _applyConceptSlots (aSlot, aRealBuilder);

        return aRealBuilder.build ();
      }

      // It's a Document Response
      final BuilderDocument aRealBuilder = builderDocument ().responseStatus (eResponseStatus).requestID (sRequestID);

      // Apply top-level response slots
      for (final SlotType aSlot : aQueryResponse.getSlot ())
        _applySlots (aSlot, aRealBuilder);

      for (final RegistryObjectType aRO : aRegistryObjectList.getRegistryObject ())
        if (aRO instanceof ExtrinsicObjectType)
          aRealBuilder.addResponseObject ((ExtrinsicObjectType) aRO);

      return aRealBuilder.build ();
    }

    throw new IllegalStateException ("Found neither inline nor reference content in the response");
  }
}
