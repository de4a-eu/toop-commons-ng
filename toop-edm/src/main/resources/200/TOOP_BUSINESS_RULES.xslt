<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<xsl:stylesheet xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:cagv="https://semic.org/sa/cv/cagv/agent-2.0.0#" xmlns:cbc="https://semic.org/sa/cv/common/cbc-2.0.0#" xmlns:cbd="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" xmlns:cccev="https://semic.org/sa/cv/cccev-2.0.0#" xmlns:cpov="http://www.w3.org/ns/corevocabulary/po" xmlns:cva="http://www.w3.org/ns/corevocabulary/AggregateComponents" xmlns:cvb="http://www.w3.org/ns/corevocabulary/BasicComponents" xmlns:dcat="http://data.europa.eu/r5r/" xmlns:dct="http://purl.org/dc/terms/" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:locn="http://www.w3.org/ns/locn#" xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:4.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0" xmlns:saxon="http://saxon.sf.net/" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
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
    <svrl:schematron-output schemaVersion="" title="TOOP EDM Business Rules (specs Version 2.0.1)">
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
      <svrl:ns-prefix-in-attribute-values prefix="cbc" uri="https://semic.org/sa/cv/common/cbc-2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="locn" uri="http://www.w3.org/ns/locn#" />
      <svrl:ns-prefix-in-attribute-values prefix="cccev" uri="https://semic.org/sa/cv/cccev-2.0.0#" />
      <svrl:ns-prefix-in-attribute-values prefix="dcat" uri="http://data.europa.eu/r5r/" />
      <svrl:ns-prefix-in-attribute-values prefix="dct" uri="http://purl.org/dc/terms/" />
      <svrl:ns-prefix-in-attribute-values prefix="xsi" uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M15" select="/" />
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
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>TOOP EDM Business Rules (specs Version 2.0.1)</svrl:text>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest/@id | query:QueryResponse/@requestId" mode="M15" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/@id | query:QueryResponse/@requestId" />

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
    <xsl:apply-templates mode="M15" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M15" priority="-1" />
  <xsl:template match="@*|node()" mode="M15" priority="-2">
    <xsl:apply-templates mode="M15" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="query:QueryRequest | query:QueryResponse" mode="M16" priority="1000">
    <svrl:fired-rule context="query:QueryRequest | query:QueryResponse" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue/rim:Value/text(),'toop-edm:v2.0')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(rim:Slot[@name = 'SpecificationIdentifier']/rim:SlotValue/rim:Value/text(),'toop-edm:v2.0')">
          <xsl:attribute name="id">br_mandatory_res_specs_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The message MUST have the specification identifier "toop-edm:v2.0".
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
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID" mode="M17" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(normalize-space(text()),'^[a-z]{2}/[a-z]{2}/(.*?)','i')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(normalize-space(text()),'^[a-z]{2}/[a-z]{2}/(.*?)','i')">
          <xsl:attribute name="id">wrong_id_format</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The uniqueness identifier consists of:
                1. The first part is the Nationality Code of the identifier. This is one of the ISO 3166-1 alpha-2 codes, followed by a slash ("/"))
                2. The second part is the Nationality Code of the destination country or international organization. This is one of the ISO 3166-1 alpha-2 codes, followed by a slash ("/")
                3. The third part a combination of readable characters. This uniquely identifies the identity asserted in the country of origin but does not necessarily reveal any discernible correspondence with the subject's actual identifier (for example, username, fiscal number etc).
                Please check <xsl:text />
            <xsl:value-of select="name(.)" />
            <xsl:text />.
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
      <xsl:when test="count(cccev:concept/cbc:QName) = count(distinct-values(cccev:concept/cbc:QName))" />
      <xsl:otherwise>
        <svrl:failed-assert test="count(cccev:concept/cbc:QName) = count(distinct-values(cccev:concept/cbc:QName))">
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
<xsl:variable name="gendertypecodes" select="document('..\codelists\gc\Gender-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE -->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'NaturalPerson']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode              | query:QueryRequest/query:Query/rim:Slot[@name = 'AuthorizedRepresentative']/rim:SlotValue/cva:CorePerson/cvb:PersonGenderCode" mode="M20" priority="1000">
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
    <xsl:apply-templates mode="M20" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M20" priority="-1" />
  <xsl:template match="@*|node()" mode="M20" priority="-2">
    <xsl:apply-templates mode="M20" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="countrycodes" select="document('..\codelists\std-gc\CountryIdentificationCode-2.1.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_country_countrycode-->
<xsl:template match="cva:PersonCoreAddress/cvb:AddressAdminUnitLocationOne              | cva:LegalEntityCoreAddress/cvb:AddressAdminUnitLocationOne              | query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent/cagv:location/locn:address/locn:adminUnitLevel1" mode="M21" priority="1001">
    <svrl:fired-rule context="cva:PersonCoreAddress/cvb:AddressAdminUnitLocationOne              | cva:LegalEntityCoreAddress/cvb:AddressAdminUnitLocationOne              | query:QueryRequest/rim:Slot[@name = 'DataConsumer']/rim:SlotValue/cagv:Agent/cagv:location/locn:address/locn:adminUnitLevel1" id="gc_check_country_countrycode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes/SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The country code must always be specified using the correct code list. Please check <xsl:text />
            <xsl:value-of select="name(.)" />
            <xsl:text />.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE gc_check_id_countrycode-->
<xsl:template match="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID" mode="M21" priority="1000">
    <svrl:fired-rule context="query:QueryRequest/query:Query/rim:Slot[@name = 'LegalPerson']/rim:SlotValue/cva:CoreBusiness/cvb:LegalEntityID" id="gc_check_id_countrycode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[1])]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[1])]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The country code in the first part of the identifier must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="(tokenize(normalize-space(current()/.),'/')[1])" />
            <xsl:text />).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[2])]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes/SimpleValue[normalize-space(.) = (tokenize(normalize-space(current()/.),'/')[2])]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The country code in the second part of the identifier must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="(tokenize(normalize-space(current()/.),'/')[2])" />
            <xsl:text />).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M21" priority="-1" />
  <xsl:template match="@*|node()" mode="M21" priority="-2">
    <xsl:apply-templates mode="M21" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
