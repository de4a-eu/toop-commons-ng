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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.schematron.svrl.AbstractSVRLMessage;

import eu.toop.edm.error.EDMExceptionPojo;
import eu.toop.edm.error.EEDMExceptionType;
import eu.toop.edm.error.EToopErrorCode;
import eu.toop.edm.error.EToopErrorOrigin;
import eu.toop.edm.error.EToopErrorSeverity;
import eu.toop.edm.schematron.SchematronBusinessRules2Validator;
import eu.toop.edm.schematron.SchematronEDM2Validator;

/**
 * Test class for class {@link EDMErrorResponse}
 *
 * @author Philip Helger
 */
public final class EDMErrorResponseTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (EDMErrorResponseTest.class);

  private static void _testWriteAndRead (@Nonnull final EDMErrorResponse aResp)
  {
    assertNotNull (aResp);

    // Write
    final byte [] aBytes = aResp.getWriter ().getAsBytes ();
    assertNotNull (aBytes);

    if (false)
      LOGGER.info (aResp.getWriter ().getAsString ());

    // Re-read
    final EDMErrorResponse aResp2 = EDMErrorResponse.reader ().read (aBytes);

    // Compare with original
    assertEquals (aResp, aResp2);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aResp, aResp2);

    {
      // Schematron validation
      final Document aDoc = aResp.getWriter ().getAsDocument ();
      assertNotNull (aDoc);
      ICommonsList <AbstractSVRLMessage> aMsgs = new SchematronEDM2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
      aMsgs = new SchematronBusinessRules2Validator ().validateDocument (aDoc);
      assertTrue (aMsgs.toString (), aMsgs.isEmpty ());
    }
  }

  @Nonnull
  private static EDMExceptionPojo.Builder _exBuilder (final EEDMExceptionType eType)
  {
    return EDMExceptionPojo.builder ()
                           .exceptionType (eType)
                           .errorCode (EToopErrorCode.GEN)
                           .errorMessage ("What went wrong: " + eType.name ())
                           .severity (EToopErrorSeverity.FAILURE)
                           .timestampNow ()
                           .errorOrigin (EToopErrorOrigin.RESPONSE_RECEPTION);
  }

  @Nonnull
  private static EDMErrorResponse.Builder _builder ()
  {
    return EDMErrorResponse.builder ().requestID ("c4369c4d-740e-4b64-80f0-7b209a66d629");
  }

  @Test
  public void testRequestConceptLegalPerson ()
  {
    final EDMErrorResponse aErrorResponse = _builder ().addException (_exBuilder (EEDMExceptionType.OBJECT_NOT_FOUND))
                                                       .addException (_exBuilder (EEDMExceptionType.TIMEOUT))
                                                       .build ();
    _testWriteAndRead (aErrorResponse);
  }

  @Test
  public void testReadAndWriteExampleFiles ()
  {
    final EDMErrorResponse aErrorResponse = EDMErrorResponse.reader ().read (new ClassPathResource ("Error Response 1.xml"));
    _testWriteAndRead (aErrorResponse);
  }

  @Test
  public void testBadCases ()
  {
    EDMErrorResponse aErrorResponse = EDMErrorResponse.reader ().read (new ClassPathResource ("Bogus.xml"));
    assertNull (aErrorResponse);

    aErrorResponse = EDMErrorResponse.reader ().read (new ClassPathResource ("Concept Request_LP.xml"));
    assertNull (aErrorResponse);

    aErrorResponse = EDMErrorResponse.reader ().read (new ClassPathResource ("Concept Response.xml"));
    assertNull (aErrorResponse);
  }
}
