package eu.toop.edm.schematron;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link CEDMSchematron}.
 *
 * @author Philip Helger
 */
public final class CEDMSchematronTest
{
  @Test
  public void testBasic ()
  {
    assertTrue (CEDMSchematron.TOOP_IS_REQUEST.exists ());
    assertTrue (CEDMSchematron.TOOP_IS_RESPONSE.exists ());
    assertTrue (CEDMSchematron.TOOP_IS_ERROR_RESPONSE.exists ());
    assertTrue (CEDMSchematron.TOOP_BUSINESS_RULES_XSLT.exists ());
    assertTrue (CEDMSchematron.TOOP_EDM2_XSLT.exists ());
  }
}
