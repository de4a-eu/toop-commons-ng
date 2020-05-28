package eu.toop.edm.schematron;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.io.resource.ClassPathResource;

/**
 * TOOP EDM Schematron constants
 *
 * @author Philip Helger
 */
@Immutable
public final class CEDMSchematron
{
  @Nonnull
  private static ClassLoader _getCL ()
  {
    return CEDMSchematron.class.getClassLoader ();
  }

  public static final ClassPathResource TOOP_IS_REQUEST = new ClassPathResource ("200/TOOP_is_request.xslt", _getCL ());
  public static final ClassPathResource TOOP_IS_RESPONSE = new ClassPathResource ("200/TOOP_is_response.xslt", _getCL ());
  public static final ClassPathResource TOOP_IS_ERROR_RESPONSE = new ClassPathResource ("200/TOOP_is_error.xslt", _getCL ());

  /**
   * The resource with the rules. Important: this Schematron requires additional
   * code lists in a relative directory!
   */
  public static final ClassPathResource TOOP_BUSINESS_RULES_XSLT = new ClassPathResource ("200/TOOP_BUSINESS_RULES.xslt", _getCL ());

  /**
   * The resource with the rules. Important: this Schematron requires additional
   * code lists in a relative directory!
   */
  public static final ClassPathResource TOOP_EDM2_XSLT = new ClassPathResource ("200/TOOP_EDM.xslt", _getCL ());

  private CEDMSchematron ()
  {}
}
