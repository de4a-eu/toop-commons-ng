package eu.toop.edm.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;

import eu.toop.edm.EDMErrorResponse;
import eu.toop.edm.EDMRequest;
import eu.toop.edm.EDMResponse;
import eu.toop.edm.IEDMTopLevelObject;

/**
 * Test class for class {@link EDMPayloadDeterminator}.
 *
 * @author Philip Helger
 */
public final class EDMPayloadDeterminatorTest
{
  @Test
  public void testGoodCases ()
  {
    IEDMTopLevelObject aTLO;

    // EDM Requests
    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Concept Request_LP.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMRequest);

    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Concept Request_NP.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMRequest);

    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Document Request_LP.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMRequest);

    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Document Request_NP.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMRequest);

    // EDM Responses
    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Concept Response.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMResponse);

    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Document Response.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMResponse);

    // EDM Error Responses
    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Error Response 1.xml"));
    assertNotNull (aTLO);
    assertTrue (aTLO instanceof EDMErrorResponse);
  }

  @Test
  public void testBadCases ()
  {
    IEDMTopLevelObject aTLO;

    // EDM Requests
    aTLO = EDMPayloadDeterminator.parseAndFind (ClassPathResource.getInputStream ("Bogus.xml"));
    assertNull (aTLO);
  }
}
