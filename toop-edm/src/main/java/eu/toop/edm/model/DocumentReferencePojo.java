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
package eu.toop.edm.model;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.annotation.ReturnsMutableObject;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;

import eu.toop.edm.jaxb.dcatap.DCatAPDistributionType;
import eu.toop.edm.jaxb.dcterms.DCMediaType;

/**
 * Represents a "Reference to a Document"
 *
 * @author Philip Helger
 */
@Immutable
public class DocumentReferencePojo
{
  private final String m_sDocumentURI;
  private final ICommonsList <String> m_aDocumentDescriptions = new CommonsArrayList <> ();
  private final String m_sDocumentType;

  public DocumentReferencePojo (@Nonnull final String sDocumentURI,
                                @Nullable final List <String> aDocumentDescriptions,
                                @Nullable final String sDocumentType)
  {
    ValueEnforcer.notNull (sDocumentURI, "DocumentURI");

    m_sDocumentURI = sDocumentURI;
    m_aDocumentDescriptions.addAll (aDocumentDescriptions);
    m_sDocumentType = sDocumentType;
  }

  @Nullable
  public final String getDocumentURI ()
  {
    return m_sDocumentURI;
  }

  @Nonnull
  @ReturnsMutableObject
  public final List <String> documentDescriptions ()
  {
    return m_aDocumentDescriptions;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllDocumentDescriptions ()
  {
    return m_aDocumentDescriptions.getClone ();
  }

  @Nullable
  public final String getDocumentType ()
  {
    return m_sDocumentType;
  }

  @Nonnull
  public DCatAPDistributionType getAsDocumentReference ()
  {
    final DCatAPDistributionType ret = new DCatAPDistributionType ();
    // Mandatory element but not needed atm
    ret.setAccessURL (StringHelper.getNotNull (m_sDocumentURI));
    for (final String s : m_aDocumentDescriptions)
      ret.addDescription (s);
    if (StringHelper.hasText (m_sDocumentType))
    {
      final DCMediaType dm = new DCMediaType ();
      dm.addContent (m_sDocumentType);
      ret.setMediaType (dm);
    }
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final DocumentReferencePojo rhs = (DocumentReferencePojo) o;
    return EqualsHelper.equals (m_sDocumentURI, rhs.m_sDocumentURI) &&
           EqualsHelper.equals (m_aDocumentDescriptions, rhs.m_aDocumentDescriptions) &&
           EqualsHelper.equals (m_sDocumentType, rhs.m_sDocumentType);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDocumentURI).append (m_aDocumentDescriptions).append (m_sDocumentType).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("DocumentURI", m_sDocumentURI)
                                       .append ("DocumentDescriptions", m_aDocumentDescriptions)
                                       .append ("DocumentType", m_sDocumentType)
                                       .getToString ();
  }

  @Nonnull
  public static Builder builder ()
  {
    return new Builder ();
  }

  @Nonnull
  public static Builder builder (@Nullable final DCatAPDistributionType a)
  {
    final Builder ret = new Builder ();
    if (a != null)
    {
      ret.documentURI (a.getAccessURL ());
      ret.documentDescriptions (a.getDescription ());
      if (a.getMediaType () != null && a.getMediaType ().hasContentEntries () && a.getMediaType ().getContentAtIndex (0) instanceof String)
        ret.documentType ((String) a.getMediaType ().getContentAtIndex (0));
    }
    return ret;
  }

  /**
   * A builder for this class
   *
   * @author Philip Helger
   */
  @NotThreadSafe
  public static class Builder
  {
    private String m_sDocumentURI;
    private final ICommonsList <String> m_aDocumentDescriptions = new CommonsArrayList <> ();
    private String m_sDocumentType;

    public Builder ()
    {}

    @Nonnull
    public Builder documentURI (@Nullable final String s)
    {
      m_sDocumentURI = s;
      return this;
    }

    @Nonnull
    public Builder addDocumentDescription (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDocumentDescriptions.add (s);
      return this;
    }

    @Nonnull
    public Builder documentDescription (@Nullable final String s)
    {
      if (StringHelper.hasText (s))
        m_aDocumentDescriptions.set (s);
      else
        m_aDocumentDescriptions.clear ();
      return this;
    }

    @Nonnull
    public Builder documentDescriptions (@Nullable final String... a)
    {
      m_aDocumentDescriptions.setAll (a);
      return this;
    }

    @Nonnull
    public Builder documentDescriptions (@Nullable final Iterable <String> a)
    {
      m_aDocumentDescriptions.setAll (a);
      return this;
    }

    @Nonnull
    public <T> Builder documentDescriptions (@Nullable final Iterable <? extends T> a, @Nonnull final Function <? super T, String> aMapper)
    {
      m_aDocumentDescriptions.setAllMapped (a, aMapper);
      return this;
    }

    @Nonnull
    public Builder documentType (@Nullable final String s)
    {
      m_sDocumentType = s;
      return this;
    }

    @Nonnull
    public DocumentReferencePojo build ()
    {
      return new DocumentReferencePojo (m_sDocumentURI, m_aDocumentDescriptions, m_sDocumentType);
    }
  }
}
