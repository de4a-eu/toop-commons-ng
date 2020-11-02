<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!--

    Copyright (C) 2018-2020 toop.eu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:cagv="https://semic.org/sa/cv/cagv/agent-2.0.0#" xmlns:cbc="https://data.europe.eu/semanticassets/ns/cv/common/cbc_v2.0.0#" xmlns:cbd="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:cccev="https://data.europe.eu/semanticassets/ns/cv/cccev_v2.0.0#" xmlns:cpov="http://www.w3.org/ns/corevocabulary/po" xmlns:cva="http://www.w3.org/ns/corevocabulary/AggregateComponents" xmlns:cvb="http://www.w3.org/ns/corevocabulary/BasicComponents" xmlns:dcat="http://data.europa.eu/r5r/" xmlns:dct="http://purl.org/dc/terms/" xmlns:gc="http://docs.oasis-open.org/codelist/ns/genericode/1.0/" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:locn="http://www.w3.org/ns/locn#" xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0" xmlns:saxon="http://saxon.sf.net/" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
<!--Implementers: please note that overriding process-prolog or process-root is 
    the preferred method for meta-stylesheets to use where possible. -->

<xsl:param name="archiveDirParameter" />
  <xsl:param name="archiveNameParameter" />
  <xsl:param name="fileNameParameter" />
  <xsl:param name="fileDirParameter" />
  <xsl:variable name="document-uri">
    <xsl:value-of select="document-uri(/)" />
  </xsl:variable>

<!--PHASES-->


<!--PROLOG-->
<xsl:output indent="yes" method="xml" omit-xml-declaration="no" standalone="yes" />

<!--XSD TYPES FOR XSLT2-->


<!--KEYS AND FUNCTIONS-->


<!--DEFAULT RULES-->


<!--MODE: SCHEMATRON-SELECT-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<xsl:template match="*" mode="schematron-select-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="." />
  </xsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-->
<!--This mode can be used to generate an ugly though full XPath for locators-->
<xsl:template match="*" mode="schematron-get-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="parent::*" />
    <xsl:text>/</xsl:text>
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">
        <xsl:value-of select="name()" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>*:</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>[namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()" />
        <xsl:text>']</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:variable name="preceding" select="count(preceding-sibling::*[local-name()=local-name(current())                                   and namespace-uri() = namespace-uri(current())])" />
    <xsl:text>[</xsl:text>
    <xsl:value-of select="1+ $preceding" />
    <xsl:text>]</xsl:text>
  </xsl:template>
  <xsl:template match="@*" mode="schematron-get-full-path">
    <xsl:apply-templates mode="schematron-get-full-path" select="parent::*" />
    <xsl:text>/</xsl:text>
    <xsl:choose>
      <xsl:when test="namespace-uri()=''">@<xsl:value-of select="name()" />
</xsl:when>
      <xsl:otherwise>
        <xsl:text>@*[local-name()='</xsl:text>
        <xsl:value-of select="local-name()" />
        <xsl:text>' and namespace-uri()='</xsl:text>
        <xsl:value-of select="namespace-uri()" />
        <xsl:text>']</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

<!--MODE: SCHEMATRON-FULL-PATH-2-->
<!--This mode can be used to generate prefixed XPath for humans-->
<xsl:template match="node() | @*" mode="schematron-get-full-path-2">
    <xsl:for-each select="ancestor-or-self::*">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="name(.)" />
      <xsl:if test="preceding-sibling::*[name(.)=name(current())]">
        <xsl:text>[</xsl:text>
        <xsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1" />
        <xsl:text>]</xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(self::*)">
      <xsl:text />/@<xsl:value-of select="name(.)" />
    </xsl:if>
  </xsl:template>
<!--MODE: SCHEMATRON-FULL-PATH-3-->
<!--This mode can be used to generate prefixed XPath for humans 
	(Top-level element has index)-->

<xsl:template match="node() | @*" mode="schematron-get-full-path-3">
    <xsl:for-each select="ancestor-or-self::*">
      <xsl:text>/</xsl:text>
      <xsl:value-of select="name(.)" />
      <xsl:if test="parent::*">
        <xsl:text>[</xsl:text>
        <xsl:value-of select="count(preceding-sibling::*[name(.)=name(current())])+1" />
        <xsl:text>]</xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:if test="not(self::*)">
      <xsl:text />/@<xsl:value-of select="name(.)" />
    </xsl:if>
  </xsl:template>

<!--MODE: GENERATE-ID-FROM-PATH -->
<xsl:template match="/" mode="generate-id-from-path" />
  <xsl:template match="text()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.text-', 1+count(preceding-sibling::text()), '-')" />
  </xsl:template>
  <xsl:template match="comment()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.comment-', 1+count(preceding-sibling::comment()), '-')" />
  </xsl:template>
  <xsl:template match="processing-instruction()" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.processing-instruction-', 1+count(preceding-sibling::processing-instruction()), '-')" />
  </xsl:template>
  <xsl:template match="@*" mode="generate-id-from-path">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:value-of select="concat('.@', name())" />
  </xsl:template>
  <xsl:template match="*" mode="generate-id-from-path" priority="-0.5">
    <xsl:apply-templates mode="generate-id-from-path" select="parent::*" />
    <xsl:text>.</xsl:text>
    <xsl:value-of select="concat('.',name(),'-',1+count(preceding-sibling::*[name()=name(current())]),'-')" />
  </xsl:template>

<!--MODE: GENERATE-ID-2 -->
<xsl:template match="/" mode="generate-id-2">U</xsl:template>
  <xsl:template match="*" mode="generate-id-2" priority="2">
    <xsl:text>U</xsl:text>
    <xsl:number count="*" level="multiple" />
  </xsl:template>
  <xsl:template match="node()" mode="generate-id-2">
    <xsl:text>U.</xsl:text>
    <xsl:number count="*" level="multiple" />
    <xsl:text>n</xsl:text>
    <xsl:number count="node()" />
  </xsl:template>
  <xsl:template match="@*" mode="generate-id-2">
    <xsl:text>U.</xsl:text>
    <xsl:number count="*" level="multiple" />
    <xsl:text>_</xsl:text>
    <xsl:value-of select="string-length(local-name(.))" />
    <xsl:text>_</xsl:text>
    <xsl:value-of select="translate(name(),':','.')" />
  </xsl:template>
<!--Strip characters-->  <xsl:template match="text()" priority="-1" />

<!--SCHEMA SETUP-->
<xsl:template match="/">
    <svrl:schematron-output schemaVersion="" title="TOOP EDM Business Rules (specs Version 2.1.0)">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="query" uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="rim" uri="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="cva" uri="http://www.w3.org/ns/corevocabulary/AggregateComponents" />
      <svrl:ns-prefix-in-attribute-values prefix="cvb" uri="http://www.w3.org/ns/corevocabulary/BasicComponents" />
      <svrl:ns-prefix-in-attribute-values prefix="cbd" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" />
      <svrl:ns-prefix-in-attribute-values prefix="rs" uri="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="cpov" uri="http://www.w3.org/ns/corevocabulary/po" />
      <svrl:ns-prefix-in-attribute-values prefix="cagv" uri="https://semic.org/sa/cv/cagv/agent-2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="https://data.europe.eu/semanticassets/ns/cv/common/cbc_v2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="locn" uri="http://www.w3.org/ns/locn#" />
      <svrl:ns-prefix-in-attribute-values prefix="cccev" uri="https://data.europe.eu/semanticassets/ns/cv/cccev_v2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="dcat" uri="http://data.europa.eu/r5r/" />
      <svrl:ns-prefix-in-attribute-values prefix="dct" uri="http://purl.org/dc/terms/" />
      <svrl:ns-prefix-in-attribute-values prefix="xsi" uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" />
      <svrl:ns-prefix-in-attribute-values prefix="gc" uri="http://docs.oasis-open.org/codelist/ns/genericode/1.0/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M16" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M17" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M18" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M19" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M20" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M21" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M22" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M23" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M24" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M25" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M26" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M27" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M28" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M29" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M30" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M31" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M32" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M33" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M34" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M35" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M36" select="/" />
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>TOOP EDM Business Rules (specs Version 2.1.0)</svrl:text>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/@id | query:QueryResponse/@requestId | query:QueryResponse/rim:ObjectRefList/rim:ObjectRef/@id | query:QueryRequest/query:Query/rim:Slot[@name = 'id']/rim:SlotValue/rim:Value" mode="M16" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/@id | query:QueryResponse/@requestId | query:QueryResponse/rim:ObjectRefList/rim:ObjectRef/@id | query:QueryRequest/query:Query/rim:Slot[@name = 'id']/rim:SlotValue/rim:Value" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(normalize-space((.)),'^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$','i')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(normalize-space((.)),'^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$','i')">
          <xsl:attribute name="id">br_wrong_uuid_format</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The UUID MUST be created following the UUID Version 4 specification. 
                Please check <xsl:text />
            <xsl:value-of select="name(.)" />
            <xsl:text />, found: <xsl:text />
            <xsl:value-of select="normalize-space((.))" />
            <xsl:text /> .
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M16" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M16" priority="-1" />
  <xsl:template match="@*|node()" mode="M16" priority="-2">
    <xsl:apply-templates mode="M16" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest | query:QueryResponse" mode="M17" priority="1000">
    <svrl:fired-rule context="query:QueryRequest | query:QueryResponse" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue/rim:Value/text(),'toop-edm:v2.1')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue/rim:Value/text(),'toop-edm:v2.1')">
          <xsl:attribute name="id">br_mandatory_res_specs_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The message MUST have the specification identifier "toop-edm:v2.1".
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M17" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M17" priority="-1" />
  <xsl:template match="@*|node()" mode="M17" priority="-2">
    <xsl:apply-templates mode="M17" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element/cccev:concept" mode="M18" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element/cccev:concept" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="count(//cbc:id) = count(distinct-values(//cbc:id))" />
      <xsl:otherwise>
        <svrl:failed-assert test="count(//cbc:id) = count(distinct-values(//cbc:id))">
          <xsl:attribute name="id">br_request_concept_id_not_unique</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                In a QueryRequest,  two or more concepts can not share the same ID.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M18" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M18" priority="-1" />
  <xsl:template match="@*|node()" mode="M18" priority="-2">
    <xsl:apply-templates mode="M18" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element/cccev:concept" mode="M19" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'ConceptRequestList']/rim:SlotValue/rim:Element/cccev:concept" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="count(cccev:concept/cbc:qName) = count(distinct-values(cccev:concept/cbc:qName))" />
      <xsl:otherwise>
        <svrl:failed-assert test="count(cccev:concept/cbc:qName) = count(distinct-values(cccev:concept/cbc:qName))">
          <xsl:attribute name="id">br_request_concept_qname_not_unique</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                In a QueryRequest,  two or more concepts at the same level (with a common parent) can not share the same Qname. 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M19" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M19" priority="-1" />
  <xsl:template match="@*|node()" mode="M19" priority="-2">
    <xsl:apply-templates mode="M19" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE br_check_localizedstring_unique_lang-->
<xsl:template match="query:QueryResponse/rs:Exception/rim:Slot[@name = 'ErrorText']/rim:SlotValue/rim:Value              | query:QueryRequest/rim:Slot[@name = 'Procedure']/rim:SlotValue/rim:Value" mode="M20" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rs:Exception/rim:Slot[@name = 'ErrorText']/rim:SlotValue/rim:Value              | query:QueryRequest/rim:Slot[@name = 'Procedure']/rim:SlotValue/rim:Value" id="br_check_localizedstring_unique_lang" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="count(rim:LocalizedString) = count(distinct-values(rim:LocalizedString/@xml:lang))" />
      <xsl:otherwise>
        <svrl:failed-assert test="count(rim:LocalizedString) = count(distinct-values(rim:LocalizedString/@xml:lang))">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                When there are several LocalizedStrings, they all need to have a different language ID. 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M20" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M20" priority="-1" />
  <xsl:template match="@*|node()" mode="M20" priority="-2">
    <xsl:apply-templates mode="M20" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness" mode="M21" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(count(cvb:LegalEntityID/@schemeID) = count (distinct-values(cvb:LegalEntityID/@schemeID)) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="(count(cvb:LegalEntityID/@schemeID) = count (distinct-values(cvb:LegalEntityID/@schemeID)) )">
          <xsl:attribute name="id">br_legal_person_scheme_id_not_unique</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each alternative LegaEntityID must have a different schemeID.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M21" priority="-1" />
  <xsl:template match="@*|node()" mode="M21" priority="-2">
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson" mode="M22" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="(count(cvb:PersonID/@schemeID) = count (distinct-values(cvb:PersonID/@schemeID)) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="(count(cvb:PersonID/@schemeID) = count (distinct-values(cvb:PersonID/@schemeID)) )">
          <xsl:attribute name="id">br_natural_person_scheme_id_not_unique</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Each alternative PersonID must have a different schemeID.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M22" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M22" priority="-1" />
  <xsl:template match="@*|node()" mode="M22" priority="-2">
    <xsl:apply-templates mode="M22" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID                      | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID" mode="M23" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID                      | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( (@schemeID = 'LEI') and ( string-length(normalize-space(.)) = 20) or (@schemeID != 'LEI')   )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( (@schemeID = 'LEI') and ( string-length(normalize-space(.)) = 20) or (@schemeID != 'LEI') )">
          <xsl:attribute name="id">br_invalid_lei_length</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The LEI code length should be 20.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M23" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M23" priority="-1" />
  <xsl:template match="@*|node()" mode="M23" priority="-2">
    <xsl:apply-templates mode="M23" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="gendertypecodes" select="document('..\codelist\toop\Gender-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode" mode="M24" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$gendertypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$gendertypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="id">br_check_gender_code</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A gender code must always be specified using the correct code list. 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M24" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M24" priority="-1" />
  <xsl:template match="@*|node()" mode="M24" priority="-2">
    <xsl:apply-templates mode="M24" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="countrycodes" select="document('..\codelist\external\CountryIdentificationCode-2.2.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_country_countrycode-->
<xsl:template match="cva:PersonCoreAddress/cvb:AddressAdminUnitLocationOne              | cva:LegalEntityCoreAddress/cvb:AddressAdminUnitLocationOne              | query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent/cagv:location/locn:address/locn:adminUnitLevel1" mode="M25" priority="1001">
    <svrl:fired-rule context="cva:PersonCoreAddress/cvb:AddressAdminUnitLocationOne              | cva:LegalEntityCoreAddress/cvb:AddressAdminUnitLocationOne              | query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent/cagv:location/locn:address/locn:adminUnitLevel1" id="br_check_country_countrycode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The country code must always be specified using the correct code list. Please check <xsl:text />
            <xsl:value-of select="name(.)" />
            <xsl:text />.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M25" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE br_check_id_countrycode-->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonID[@schemeID='EIDAS']              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonID[@schemeID='EIDAS']             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID[@schemeID='EIDAS']             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID[@schemeID='EIDAS']" mode="M25" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonID[@schemeID='EIDAS']              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonID[@schemeID='EIDAS']             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID[@schemeID='EIDAS']             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID[@schemeID='EIDAS']" id="br_check_id_countrycode" />
    <xsl:variable name="hasEidasFormat" select="matches(normalize-space(current()/.),'^[a-z]{2}/[a-z]{2}/(.*?)','i')" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[1])]) or ($hasEidasFormat=false()) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[1])]) or ($hasEidasFormat=false()) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                If the EIDAS code has the format "XX/YY/12345", the country code in the first part of the identifier must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="(tokenize(normalize-space(current()/.),'/')[1])" />
            <xsl:text />).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( ($countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[2])]) or ($hasEidasFormat=false()) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( ($countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[2])]) or ($hasEidasFormat=false()) )">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                If the EIDAS code has the format "XX/YY/12345", the country code in the second part of the identifier must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="(tokenize(normalize-space(current()/.),'/')[2])" />
            <xsl:text />).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M25" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M25" priority="-1" />
  <xsl:template match="@*|node()" mode="M25" priority="-2">
    <xsl:apply-templates mode="M25" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="mimetypecodes" select="document('..\codelist\external\BinaryObjectMimeCode-2.2.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_doc_media_type-->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element/dcat:distribution/dcat:mediaType             | query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:dataset/dcat:distribution/dcat:mediaType" mode="M26" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element/dcat:distribution/dcat:mediaType             | query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:dataset/dcat:distribution/dcat:mediaType" id="br_check_doc_media_type" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$mimetypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$mimetypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A mimetype code SHOULD always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M26" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M26" priority="-1" />
  <xsl:template match="@*|node()" mode="M26" priority="-2">
    <xsl:apply-templates mode="M26" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="errorseveritycodes" select="document('..\codelist\toop\ErrorSeverity-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />
  <xsl:variable name="errorcodecodes" select="document('..\codelist\toop\ErrorCode-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryResponse/rs:Exception" mode="M27" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rs:Exception" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$errorseveritycodes/SimpleValue[normalize-space(.) = normalize-space(current()/@severity)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$errorseveritycodes/SimpleValue[normalize-space(.) = normalize-space(current()/@severity)]">
          <xsl:attribute name="id">br_check_error_severity</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                An error severity code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$errorcodecodes/SimpleValue[normalize-space(.) = normalize-space(current()/@code)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$errorcodecodes/SimpleValue[normalize-space(.) = normalize-space(current()/@code)]">
          <xsl:attribute name="id">br_check_error_code</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                An error code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M27" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M27" priority="-1" />
  <xsl:template match="@*|node()" mode="M27" priority="-2">
    <xsl:apply-templates mode="M27" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="errororigincodes" select="document('..\codelist\toop\ErrorOrigin-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_error_origin-->
<xsl:template match="query:QueryResponse/rs:Exception/rim:Slot[@name = 'ErrorOrigin']/rim:SlotValue/rim:Value" mode="M28" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rs:Exception/rim:Slot[@name = 'ErrorOrigin']/rim:SlotValue/rim:Value" id="br_check_error_origin" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$errororigincodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$errororigincodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                An error origin code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M28" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M28" priority="-1" />
  <xsl:template match="@*|node()" mode="M28" priority="-2">
    <xsl:apply-templates mode="M28" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="distributionformatcodes" select="document('..\codelist\toop\DistributionFormat-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_distribution_format-->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element/dcat:distribution/dct:format" mode="M29" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'DistributionRequestList']/rim:SlotValue/rim:Element/dcat:distribution/dct:format" id="br_check_distribution_format" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$distributionformatcodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$distributionformatcodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A distribution format code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M29" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M29" priority="-1" />
  <xsl:template match="@*|node()" mode="M29" priority="-2">
    <xsl:apply-templates mode="M29" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="querydefinitions" select="document('..\codelist\toop\QueryDefinition-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_query_definition-->
<xsl:template match="query:QueryRequest/query:Query" mode="M30" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query" id="br_check_query_definition" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$querydefinitions/SimpleValue[normalize-space(.) = normalize-space(current()/@queryDefinition)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$querydefinitions/SimpleValue[normalize-space(.) = normalize-space(current()/@queryDefinition)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A query definition code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M30" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M30" priority="-1" />
  <xsl:template match="@*|node()" mode="M30" priority="-2">
    <xsl:apply-templates mode="M30" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="currencytypecodes" select="document('..\codelist\external\CurrencyCode-2.2.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_currency_code-->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element//cccev:concept/cccev:value/cccev:amountValue" mode="M31" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element//cccev:concept/cccev:value/cccev:amountValue" id="br_check_currency_code" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$currencytypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/@currencyID)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$currencytypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/@currencyID)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A currency type code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M31" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M31" priority="-1" />
  <xsl:template match="@*|node()" mode="M31" priority="-2">
    <xsl:apply-templates mode="M31" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="languagecodes" select="document('..\codelist\external\LanguageCode-2.2.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE br_check_language_code-->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name='DocumentMetadata']/rim:SlotValue/dcat:dataset/dct:language                      | query:QueryRequest/rim:Slot[@name='Procedure']/rim:SlotValue/rim:Value/rim:LocalizedString/@xml:lang                      | query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:dataset/dcat:distribution/dcat:description/@languageID                      " mode="M32" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name='DocumentMetadata']/rim:SlotValue/dcat:dataset/dct:language                      | query:QueryRequest/rim:Slot[@name='Procedure']/rim:SlotValue/rim:Value/rim:LocalizedString/@xml:lang                      | query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot/rim:SlotValue/dcat:dataset/dcat:distribution/dcat:description/@languageID                      " id="br_check_language_code" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$languagecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$languagecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A language code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M32" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M32" priority="-1" />
  <xsl:template match="@*|node()" mode="M32" priority="-2">
    <xsl:apply-templates mode="M32" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="dataelementresponseerrorcodes" select="document('..\codelist\toop\DataElementResponseErrorCode-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element//cccev:concept/cccev:value/cccev:error" mode="M33" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rim:RegistryObjectList/rim:RegistryObject/rim:Slot[@name = 'ConceptValues']/rim:SlotValue/rim:Element//cccev:concept/cccev:value/cccev:error" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$dataelementresponseerrorcodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$dataelementresponseerrorcodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="id">br_check_error_data_element_response</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                An error code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M33" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M33" priority="-1" />
  <xsl:template match="@*|node()" mode="M33" priority="-2">
    <xsl:apply-templates mode="M33" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="industrialtypecodes" select="document('..\codelist\toop\StandardIndustrialClassCode-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID                      | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID" mode="M34" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID                      | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( (@schemeID = 'SIC') and ($industrialtypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]) or (@schemeID != 'SIC') )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( (@schemeID = 'SIC') and ($industrialtypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]) or (@schemeID != 'SIC') )">
          <xsl:attribute name="id">br_check_sic_code</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A standard industrial classification code should always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M34" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M34" priority="-1" />
  <xsl:template match="@*|node()" mode="M34" priority="-2">
    <xsl:apply-templates mode="M34" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="procotolexceptioncodes" select="document('..\codelist\toop\ProcotolException-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryResponse/rs:Exception" mode="M35" priority="1000">
    <svrl:fired-rule context="query:QueryResponse/rs:Exception" />
    <xsl:variable name="datatype" select="@*[ends-with(name(.), ':type') and . != '']" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$procotolexceptioncodes/SimpleValue[normalize-space(.) = normalize-space(substring-after($datatype,':'))]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$procotolexceptioncodes/SimpleValue[normalize-space(.) = normalize-space(substring-after($datatype,':'))]">
          <xsl:attribute name="id">br_check_error_protocol_exception</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A protocol exception code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M35" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M35" priority="-1" />
  <xsl:template match="@*|node()" mode="M35" priority="-2">
    <xsl:apply-templates mode="M35" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="identifiertypecodes" select="document('..\codelist\toop\IdentifierType-CodeList.gc')/gc:CodeList/SimpleCodeList/Row/Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonID              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonID             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID" mode="M36" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonID              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonID             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID             | query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityLegalID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$identifiertypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/@schemeID)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$identifiertypecodes/SimpleValue[normalize-space(.) = normalize-space(current()/@schemeID)]">
          <xsl:attribute name="id">br_check_identifier_code</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                An identifier type code SHOULD always be specified using the correct code list. 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M36" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M36" priority="-1" />
  <xsl:template match="@*|node()" mode="M36" priority="-2">
    <xsl:apply-templates mode="M36" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
