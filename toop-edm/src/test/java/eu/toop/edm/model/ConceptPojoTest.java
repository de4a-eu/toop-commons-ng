/**
 * This work is protected under copyrights held by the members of the
 * TOOP Project Consortium as indicated at
 * http://wiki.ds.unipi.gr/display/TOOP/Contributors
 * (c) 2018-2021. All rights reserved.
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
package eu.toop.edm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.datetime.PDTFactory;
import com.helger.commons.mock.CommonsTestHelper;
import com.helger.commons.url.URLHelper;
import com.helger.datetime.util.PDTXMLConverter;

import eu.toop.edm.error.EToopDataElementResponseErrorCode;
import eu.toop.edm.error.EToopErrorCode;
import eu.toop.edm.jaxb.cccev.CCCEVConceptType;
import eu.toop.edm.pilot.gbm.EToopConcept;
import eu.toop.edm.xml.cccev.ConceptMarshaller;

/**
 * Test class for class {@link ConceptPojo}
 *
 * @author Philip Helger
 */
public final class ConceptPojoTest
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ConceptPojoTest.class);

  private static void _testWriteAndRead (@Nonnull final ConceptPojo x)
  {
    assertNotNull (x);

    final CCCEVConceptType aObj = x.getAsCCCEVConcept ();
    assertNotNull (aObj);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aObj, aObj.clone ());

    // Write
    final ConceptMarshaller m = new ConceptMarshaller ();
    m.setFormattedOutput (true);
    assertNotNull (m.getAsDocument (aObj));
    if (false)
      LOGGER.info (m.getAsString (aObj));

    // Re-read
    final ConceptPojo y = ConceptPojo.builder (aObj).build ();
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (x, y);
  }

  @Test
  public void testBasic ()
  {
    final String NS = "http://toop.eu/registered-organization";
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (NS, "CompanyData")
                                     .addChild (y -> y.id ("ConceptID-2").name (NS, "Concept-Name-2"))
                                     .addChild (y -> y.id ("ConceptID-3").name (NS, "Concept-Name-3"))
                                     .build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testPilotGBM ()
  {
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (EToopConcept.NAMESPACE_URI, "CompanyData")
                                     .addChild (y -> y.id ("ConceptID-2").name (EToopConcept.COMPANY_NAME))
                                     .addChild (y -> y.id ("ConceptID-3").name (EToopConcept.COMPANY_TYPE))
                                     .build ();
    _testWriteAndRead (x);
  }

  private static final AtomicInteger COUNT = new AtomicInteger (1);

  @Nonnull
  private static ConceptPojo.Builder _concept ()
  {
    final String sID = "id" + COUNT.incrementAndGet ();
    return ConceptPojo.builder ().id (sID).name ("urn:bla", sID);
  }

  @Nonnull
  private static ConceptPojo.Builder _concept (final Consumer <? super ConceptValuePojo.Builder> aValue)
  {
    return _concept ().value (aValue);
  }

  @Test
  public void testValues ()
  {
    final String NS = "http://toop.eu/registered-organization";
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (NS, "CompanyData")
                                     .addChild (_concept (y -> y.identifier ("identifier")))
                                     .addChild (_concept (y -> y.amount (BigDecimal.TEN, "EUR")))
                                     .addChild (_concept (y -> y.code ("code")))
                                     .addChild (_concept (y -> y.date (PDTXMLConverter.getXMLCalendarDateNow ())))
                                     .addChild (_concept (y -> y.indicator (true)))
                                     .addChild (_concept (y -> y.measure (BigDecimal.ONE, "unit")))
                                     .addChild (_concept (y -> y.numeric (42)))
                                     .addChild (_concept (y -> y.numeric (4.2)))
                                     .addChild (_concept (y -> y.period (PDTFactory.getCurrentLocalDateTime ().minusDays (1),
                                                                         PDTFactory.getCurrentLocalDateTime ().plusDays (1))))
                                     .addChild (_concept (y -> y.quantity (BigDecimal.ONE.negate (), "qty")))
                                     .addChild (_concept (y -> y.text ("a", "b", "c")))
                                     .addChild (_concept (y -> y.time (PDTXMLConverter.getXMLCalendarTimeNow ())))
                                     .addChild (_concept (y -> y.uri (URLHelper.getAsURI ("http://toop.eu"))))
                                     .addChild (_concept (y -> y.errorCode ("who-cares")))

                                     .addChild (_concept ().valueID ("identifier"))
                                     .addChild (_concept ().valueAmount (BigDecimal.TEN, "EUR"))
                                     .addChild (_concept ().valueCode ("code"))
                                     .addChild (_concept ().valueDate (PDTFactory.getCurrentLocalDate ()))
                                     .addChild (_concept ().valueIndicator (true))
                                     .addChild (_concept ().valueMeasure (BigDecimal.ONE, "unit"))
                                     .addChild (_concept ().valueNumeric (42))
                                     .addChild (_concept ().valueNumeric (4.2))
                                     .addChild (_concept ().valuePeriod (PDTFactory.getCurrentLocalDateTime ().minusDays (1),
                                                                         PDTFactory.getCurrentLocalDateTime ().plusDays (1)))
                                     .addChild (_concept ().valueQuantity (BigDecimal.ONE.negate (), "qty"))
                                     .addChild (_concept ().valueText ("a", "b", "c"))
                                     .addChild (_concept ().valueTime (PDTFactory.getCurrentLocalTime ()))
                                     .addChild (_concept ().valueURI ("http://toop.eu"))
                                     .addChild (_concept ().valueErrorCode (EToopErrorCode.DD_003))
                                     .build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testMinimum ()
  {
    final ConceptPojo x = ConceptPojo.builder ().build ();
    _testWriteAndRead (x);
  }

  @Test
  public void testBuilderFromInstance ()
  {
    final ConceptPojo x = ConceptPojo.builder ()
                                     .id ("ConceptID-1")
                                     .name (EToopConcept.NAMESPACE_URI, "CompanyData")
                                     .addChild (y -> y.id ("ConceptID-2").name (EToopConcept.COMPANY_NAME))
                                     .addChild (y -> y.id ("ConceptID-3").name (EToopConcept.COMPANY_TYPE))
                                     .build ();

    // Clone concept
    ConceptPojo y = x.getBuilder ().build ();
    assertNotNull (y);
    assertNotSame (x, y);
    assertEquals (x, y);
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (x, y);

    // Create a new concept that has a value
    y = x.getBuilder ().value (z -> z.numeric (12)).build ();
    assertNotNull (y);
    assertNotSame (x, y);
    assertNotEquals (x, y);
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (x, y);
  }

  @Test
  public void testBuilderFromInstanceNested ()
  {
    final ConceptPojo x = ConceptPojo.builder ()
                                     .randomID ()
                                     .name (EToopConcept.NAMESPACE_URI, "CompanyData")
                                     .addChild (y -> y.randomID ().name (EToopConcept.COMPANY_NAME))
                                     .addChild (y -> y.randomID ().name (EToopConcept.COMPANY_TYPE))
                                     .build ();

    final ConceptPojo y = x.cloneAndModify (aHdl -> {
      if (EToopConcept.COMPANY_NAME.getAsQName ().equals (aHdl.name ()))
        aHdl.valueText ("My company name");
      else
        if (EToopConcept.COMPANY_TYPE.getAsQName ().equals (aHdl.name ()))
          aHdl.valueText ("My company type");
        else
          if (!aHdl.name ().getLocalPart ().equals ("CompanyData"))
            aHdl.valueErrorCode (EToopDataElementResponseErrorCode.DP_ELE_001);
    });

    LOGGER.info (new ConceptMarshaller ().getAsString (y.getAsCCCEVConcept ()));
  }
}
