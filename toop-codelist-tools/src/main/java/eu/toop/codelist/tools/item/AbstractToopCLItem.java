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
package eu.toop.codelist.tools.item;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.StringHelper;
import com.helger.xml.microdom.IMicroElement;

/**
 * Abstract code list item
 *
 * @author Philip Helger
 */
public abstract class AbstractToopCLItem implements Serializable
{
  public static final boolean DEFAULT_DEPRECATED = false;

  private final String m_sName;
  private final String m_sID;
  private final String m_sSince;
  private final boolean m_bDeprecated;
  private final String m_sDeprecatedSince;

  public AbstractToopCLItem (@Nonnull @Nonempty final String sName,
                             @Nonnull @Nonempty final String sID,
                             @Nonnull @Nonempty final String sSince,
                             final boolean bDeprecated,
                             @Nullable final String sDeprecatedSince)
  {
    ValueEnforcer.notEmpty (sName, "Name");
    ValueEnforcer.notEmpty (sID, "ID");
    ValueEnforcer.notEmpty (sSince, "Since");
    if (bDeprecated && StringHelper.hasNoText (sDeprecatedSince))
      throw new IllegalStateException ("Code list entry is deprecated but there is no deprecated-since entry");

    m_sName = sName;
    m_sID = sID;
    m_sSince = sSince;
    m_bDeprecated = bDeprecated;
    m_sDeprecatedSince = sDeprecatedSince;
  }

  @Nonnull
  @Nonempty
  public final String getName ()
  {
    return m_sName;
  }

  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public final String getSince ()
  {
    return m_sSince;
  }

  public final boolean isDeprecated ()
  {
    return m_bDeprecated;
  }

  @Nullable
  public final String getDeprecatedSince ()
  {
    return m_sDeprecatedSince;
  }

  protected void fillMicroElement (@Nonnull final IMicroElement aElement)
  {
    aElement.setAttribute ("name", m_sName);
    aElement.setAttribute ("id", m_sID);
    aElement.setAttribute ("since", m_sSince);
    aElement.setAttribute ("deprecated", m_bDeprecated);
    aElement.setAttribute ("deprecated-since", m_sDeprecatedSince);
  }
}
