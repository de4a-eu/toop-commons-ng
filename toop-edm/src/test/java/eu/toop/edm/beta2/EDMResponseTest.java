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
package eu.toop.edm.beta2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Month;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.EDMResponse;
import eu.toop.edm.model.AddressPojo;
import eu.toop.edm.model.AgentPojo;
import eu.toop.edm.model.ConceptPojo;
import eu.toop.edm.model.DatasetPojo;
import eu.toop.edm.model.DocumentReferencePojo;
import eu.toop.edm.model.EQueryDefinitionType;
import eu.toop.edm.model.QualifiedRelationPojo;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.edm.schematron.SchematronEDM2Validator;
import eu.toop.regrep.ERegRepResponseStatus;

/**
 * Test class for class {@link EDMResponse}.
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class EDMResponseTest
{
  private static void _testWriteAndRead (@Nonnull final EDMResponse aResp)
  {
    assertNotNull (aResp);

    // Write
    final byte [] aBytes = aResp.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    // Re-read
    final EDMResponse aResp2 = EDMResponse.getReader ().read (aBytes);

    // Compare with original
    assertEquals (aResp, aResp2);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aResp, aResp2);

    {
      // Schematron validation
      final Document aDoc = aResp.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      final ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Nonnull
  private static <T extends EDMResponse.AbstractBuilder <T>> T _resp (@Nonnull final T aBuilder)
  {
    return aBuilder.requestID (UUID.randomUUID ())
                   .issueDateTimeNow ()
                   .dataProvider (AgentPojo.builder ()
                                           .address (AddressPojo.builder ()
                                                                .town ("MyTown")
                                                                .streetName ("MyStreet")
                                                                .buildingNumber ("22")
                                                                .countryCode ("GR")
                                                                .fullAddress ("MyStreet 22, 11134, MyTown, GR")
                                                                .postalCode ("11134")
                                                                .build ())
                                           .name ("DP NAME")
                                           .id ("1234")
                                           .idSchemeID ("VAT")
                                           .build ())
                   .responseStatus (ERegRepResponseStatus.SUCCESS)
                   .specificationIdentifier ("Niar");
  }

  @Nonnull
  private static EDMResponse.BuilderConcept _respConcept ()
  {
    return _resp (EDMResponse.builderConcept ()).queryDefinition (EQueryDefinitionType.CONCEPT)
                                                .concept (ConceptPojo.builder ()
                                                                     .id ("ConceptID-1")
                                                                     .name (EToopConcept.REGISTERED_ORGANIZATION)
                                                                     .addChild (ConceptPojo.builder ()
                                                                                           .randomID ()
                                                                                           .name (EToopConcept.COMPANY_NAME)
                                                                                           .valueText ("Helger Enterprises"))
                                                                     .addChild (ConceptPojo.builder ()
                                                                                           .randomID ()
                                                                                           .name (EToopConcept.FAX_NUMBER)
                                                                                           .valueText ("342342424"))
                                                                     .addChild (ConceptPojo.builder ()
                                                                                           .randomID ()
                                                                                           .name (EToopConcept.FOUNDATION_DATE)
                                                                                           .valueDate (PDTFactory.createLocalDate (1960,
                                                                                                                                   Month.AUGUST,
                                                                                                                                   12))));
  }

  @Nonnull
  private static DatasetPojo.Builder _dataset ()
  {
    return DatasetPojo.builder ()
                      .description ("bla desc")
                      .title ("bla title")
                      .distribution (DocumentReferencePojo.builder ()
                                                          .documentURI ("URI")
                                                          .documentDescription ("DocumentDescription")
                                                          .documentType ("docType")
                                                          .localeCode ("GR"))
                      .creator (AgentPojo.builder ()
                                         .name ("Agent name")
                                         .address (AddressPojo.builder ().town ("Kewlkidshome")))
                      .ids ("RE238918378", "DOC-555")
                      .issuedNow ()
                      .language ("en")
                      .lastModifiedNow ()
                      .validFrom (PDTFactory.getCurrentLocalDate ().minusMonths (1))
                      .validTo (PDTFactory.getCurrentLocalDate ().plusYears (1))
                      .qualifiedRelation (QualifiedRelationPojo.builder ()
                                                               .description ("LegalResourceDesc")
                                                               .title ("Name")
                                                               .id ("RE238918378"));
  }

  @Nonnull
  private static EDMResponse.BuilderDocument _respDocument ()
  {
    return _resp (EDMResponse.builderDocument ()).queryDefinition (EQueryDefinitionType.DOCUMENT)
                                                 .addResponseObject (x -> x.randomRegistryObjectID ()
                                                                           .dataset (_dataset ())
                                                                           .repositoryItemRef (y -> y.link ("http://bla")
                                                                                                     .title ("whatsoever")));
  }

  @Test
  public void createConceptResponse ()
  {
    final EDMResponse aResp = _respConcept ().build ();
    _testWriteAndRead (aResp);
  }

  @Test
  public void createDocumentResponse ()
  {
    final EDMResponse aResp = _respDocument ().build ();
    _testWriteAndRead (aResp);
  }

  @Test
  public void testReadAndWriteExampleFiles ()
  {
    EDMResponse aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Concept Response.xml"));
    _testWriteAndRead (aResponse);

    aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Document Response.xml"));
    _testWriteAndRead (aResponse);
  }

  @Test
  public void testBadCases ()
  {
    EDMResponse aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Bogus.xml"));
    assertNull (aResponse);

    aResponse = EDMResponse.getReader ().read (new ClassPathResource ("Concept Request_LP.xml"));
    assertNull (aResponse);
  }
}
