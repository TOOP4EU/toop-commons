<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!--

    Copyright (C) 2018 toop.eu

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
<xsl:stylesheet xmlns:svrl="http://purl.oclc.org/dsdl/svrl" xmlns:iso="http://purl.oclc.org/dsdl/schematron" xmlns:saxon="http://saxon.sf.net/" xmlns:schold="http://www.ascc.net/xml/schematron" xmlns:toop="urn:eu:toop:ns:dataexchange-1p10" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
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
    <svrl:schematron-output schemaVersion="" title="TOOP Business Rules (specs Version 1.1.0)">
      <xsl:comment>
        <xsl:value-of select="$archiveDirParameter" />   
		 <xsl:value-of select="$archiveNameParameter" />  
		 <xsl:value-of select="$fileNameParameter" />  
		 <xsl:value-of select="$fileDirParameter" />
      </xsl:comment>
      <svrl:ns-prefix-in-attribute-values prefix="toop" uri="urn:eu:toop:ns:dataexchange-1p10" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M2" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M3" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M4" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M5" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M6" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M7" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M8" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M9" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M10" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M11" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M12" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M13" select="/" />
      <svrl:active-pattern>
        <xsl:attribute name="document">
          <xsl:value-of select="document-uri(/)" />
        </xsl:attribute>
        <xsl:apply-templates />
      </svrl:active-pattern>
      <xsl:apply-templates mode="M14" select="/" />
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
    </svrl:schematron-output>
  </xsl:template>

<!--SCHEMATRON PATTERNS-->
<svrl:text>TOOP Business Rules (specs Version 1.1.0)</svrl:text>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:Request | toop:Response" mode="M2" priority="1000">
    <svrl:fired-rule context="toop:Request | toop:Response" />

		<!--REPORT -->
<xsl:if test="( (exists(toop:DataElementRequest)) and (exists(toop:DocumentRequest)) )">
      <svrl:successful-report test="( (exists(toop:DataElementRequest)) and (exists(toop:DocumentRequest)) )">
        <xsl:attribute name="id">mandatory_split</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                The message cannot contain a request both for Data Elements and Documents. Please split the message.
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>
    <xsl:apply-templates mode="M2" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M2" priority="-1" />
  <xsl:template match="@*|node()" mode="M2" priority="-2">
    <xsl:apply-templates mode="M2" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:DocumentUniversalUniqueIdentifier | toop:DataRequestIdentifier | toop:DocumentRequestIdentifier" mode="M3" priority="1000">
    <svrl:fired-rule context="toop:DocumentUniversalUniqueIdentifier | toop:DataRequestIdentifier | toop:DocumentRequestIdentifier" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(text(),'^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$','i')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(text(),'^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$','i')">
          <xsl:attribute name="id">wrong_uuid_format</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The UUID MUST be created following the UUID Version 4 specification. 
                Copies of a document must be identified with a different UUID. 
                Compulsory use of schemeAgencyID attribute.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M3" priority="-1" />
  <xsl:template match="@*|node()" mode="M3" priority="-2">
    <xsl:apply-templates mode="M3" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:Request" mode="M4" priority="1000">
    <svrl:fired-rule context="toop:Request" />

		<!--REPORT -->
<xsl:if test="exists(toop:DataRequestIdentifier)">
      <svrl:successful-report test="exists(toop:DataRequestIdentifier)">
        <xsl:attribute name="id">misplaced_dr_id</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                A Request should not contain a DataRequestIdentifier, which is used in the response to link to the request.
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(toop:DocumentTypeIdentifier/text(),'urn:eu.toop.request.registeredorganization::1.10$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(toop:DocumentTypeIdentifier/text(),'urn:eu.toop.request.registeredorganization::1.10$')">
          <xsl:attribute name="id">mandatory_req_doc_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A Request DocumentTypeIdentifier must be 'urn:eu:toop:ns:dataexchange-1p10::Request##urn:eu.toop.request.registeredorganization::1.10'.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--REPORT -->
<xsl:if test="( (exists(//toop:DocumentResponse)) or (exists(//toop:DataElementResponseValue)) )">
      <svrl:successful-report test="( (exists(//toop:DocumentResponse)) or (exists(//toop:DataElementResponseValue)) )">
        <xsl:attribute name="id">misplaced_response</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                A Request can not contain a DocumentResponse or any DataElementResponseValue element.
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>

		<!--REPORT -->
<xsl:if test="exists(//toop:DataProvider)">
      <svrl:successful-report test="exists(//toop:DataProvider)">
        <xsl:attribute name="id">misplaced_data_provider</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                A Request should not contain information about the DataProvider.
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(toop:SpecificationIdentifier/text(),'urn:eu:toop:ns:dataexchange-1p10::Request$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(toop:SpecificationIdentifier/text(),'urn:eu:toop:ns:dataexchange-1p10::Request$')">
          <xsl:attribute name="id">mandatory_req_specs_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: A Toop data request MUST have the specification identifier "urn:eu:toop:ns:dataexchange-1p10::Request".
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M4" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M4" priority="-1" />
  <xsl:template match="@*|node()" mode="M4" priority="-2">
    <xsl:apply-templates mode="M4" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:Response" mode="M5" priority="1000">
    <svrl:fired-rule context="toop:Response" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="exists(toop:DataRequestIdentifier)" />
      <xsl:otherwise>
        <svrl:failed-assert test="exists(toop:DataRequestIdentifier)">
          <xsl:attribute name="id">mandatory_dr_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A Response must contain a DataRequestIdentifier. 
                UNCHECKED: Use the same value that was used in the corresponding Toop Request (path: Request/DocumentUniversalUniqueIdentifier).
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(toop:DocumentTypeIdentifier/text(),'urn:eu.toop.response.registeredorganization::1.10$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(toop:DocumentTypeIdentifier/text(),'urn:eu.toop.response.registeredorganization::1.10$')">
          <xsl:attribute name="id">mandatory_res_doc_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                A Response DocumentTypeIdentifier must be 'urn:eu:toop:ns:dataexchange-1p10::Response##urn:eu.toop.response.registeredorganization::1.10'.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--REPORT -->
<xsl:if test="matches(toop:DataRequestIdentifier,toop:DocumentUniversalUniqueIdentifier)">
      <svrl:successful-report test="matches(toop:DataRequestIdentifier,toop:DocumentUniversalUniqueIdentifier)">
        <xsl:attribute name="id">duplicate_req_id</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                The DocumentUniversalUniqueIdentifier cannot be identical to the DataRequestIdentifier (which is copied from the Request).
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(toop:SpecificationIdentifier/text(),'urn:eu:toop:ns:dataexchange-1p10::Response$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(toop:SpecificationIdentifier/text(),'urn:eu:toop:ns:dataexchange-1p10::Response$')">
          <xsl:attribute name="id">mandatory_res_specs_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: A Toop data response MUST have the specification identifier "urn:eu:toop:ns:dataexchange-1p10::Response".
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M5" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M5" priority="-1" />
  <xsl:template match="@*|node()" mode="M5" priority="-2">
    <xsl:apply-templates mode="M5" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:DocumentTypeIdentifier | toop:SpecificationIdentifier" mode="M6" priority="1000">
    <svrl:fired-rule context="toop:DocumentTypeIdentifier | toop:SpecificationIdentifier" />
    <xsl:variable name="varschemeID" select="@schemeID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($varschemeID,'^toop-doctypeid-qns$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($varschemeID,'^toop-doctypeid-qns$')">
          <xsl:attribute name="id">mandatory_scheme_docid</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Compulsory use of attributes schemeID "toop-doctypeid-qns" (instead of <xsl:text />
            <xsl:value-of select="$varschemeID" />
            <xsl:text />).    
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M6" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M6" priority="-1" />
  <xsl:template match="@*|node()" mode="M6" priority="-2">
    <xsl:apply-templates mode="M6" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:ProcessIdentifier" mode="M7" priority="1000">
    <svrl:fired-rule context="toop:ProcessIdentifier" />
    <xsl:variable name="varschemeID" select="@schemeID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($varschemeID,'^toop-procid-agreement$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($varschemeID,'^toop-procid-agreement$')">
          <xsl:attribute name="id">mandatory_scheme_processid</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Compulsory use of attributes schemeID "toop-procid-agreement" (instead of <xsl:text />
            <xsl:value-of select="$varschemeID" />
            <xsl:text />).    
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M7" priority="-1" />
  <xsl:template match="@*|node()" mode="M7" priority="-2">
    <xsl:apply-templates mode="M7" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:DCElectronicAddressIdentifier | toop:DPElectronicAddressIdentifier" mode="M8" priority="1000">
    <svrl:fired-rule context="toop:DCElectronicAddressIdentifier | toop:DPElectronicAddressIdentifier" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@schemeID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@schemeID">
          <xsl:attribute name="id">mandatory_address_schemeid</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: An electronic address identifier MUST have a scheme identifier attribute. 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M8" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M8" priority="-1" />
  <xsl:template match="@*|node()" mode="M8" priority="-2">
    <xsl:apply-templates mode="M8" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:DCElectronicAddressIdentifier | toop:DPElectronicAddressIdentifier" mode="M9" priority="1000">
    <svrl:fired-rule context="toop:DCElectronicAddressIdentifier | toop:DPElectronicAddressIdentifier" />
    <xsl:variable name="varschemeID" select="@schemeID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches($varschemeID,'^iso6523-actorid-upis$')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches($varschemeID,'^iso6523-actorid-upis$')">
          <xsl:attribute name="id">mandatory_scheme_address</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Compulsory use of attributes schemeID "iso6523-actorid-upis" (instead of <xsl:text />
            <xsl:value-of select="$varschemeID" />
            <xsl:text />). 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M9" priority="-1" />
  <xsl:template match="@*|node()" mode="M9" priority="-2">
    <xsl:apply-templates mode="M9" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:LegalPersonUniqueIdentifier | toop:PersonIdentifier" mode="M10" priority="1000">
    <svrl:fired-rule context="toop:LegalPersonUniqueIdentifier | toop:PersonIdentifier" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(text(),'^[a-z]{2}/[a-z]{2}/(.*?)$','i')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(text(),'^[a-z]{2}/[a-z]{2}/(.*?)$','i')">
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
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M10" priority="-1" />
  <xsl:template match="@*|node()" mode="M10" priority="-2">
    <xsl:apply-templates mode="M10" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:DataConsumerDocumentIdentifier | toop:DPIdentifier | toop:DocumentUniversalUniqueIdentifier | toop:DCUniqueIdentifier | toop:DocumentIssuerIdentifier | toop:DataConsumerGlobalSessionIdentifier | toop:DataRequestIdentifier | toop:DocumentRequestIdentifier" mode="M11" priority="1000">
    <svrl:fired-rule context="toop:DataConsumerDocumentIdentifier | toop:DPIdentifier | toop:DocumentUniversalUniqueIdentifier | toop:DCUniqueIdentifier | toop:DocumentIssuerIdentifier | toop:DataConsumerGlobalSessionIdentifier | toop:DataRequestIdentifier | toop:DocumentRequestIdentifier" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@schemeAgencyID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@schemeAgencyID">
          <xsl:attribute name="id">mandatory_schemeagency</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The schemeAgencyID attribute is mandatory. 
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M11" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M11" priority="-1" />
  <xsl:template match="@*|node()" mode="M11" priority="-2">
    <xsl:apply-templates mode="M11" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:ConceptRequest" mode="M12" priority="1000">
    <svrl:fired-rule context="toop:ConceptRequest" />
    <xsl:variable name="conceptDefinitions" select="count(./toop:ConceptDefinition)" />

		<!--REPORT -->
<xsl:if test="matches(toop:ConceptTypeCode,'DC') and ($conceptDefinitions!=1)">
      <svrl:successful-report test="matches(toop:ConceptTypeCode,'DC') and ($conceptDefinitions!=1)">
        <xsl:attribute name="id">wrong_dc_concept_def_cardinality</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                Rule: If the Concept Type Code is set on "Data Consumer" the cardinality of the ConceptDefinition element MUST be set to 1...1 (instead of <xsl:text />
          <xsl:value-of select="$conceptDefinitions" />
          <xsl:text />).
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>
    <xsl:apply-templates mode="M12" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M12" priority="-1" />
  <xsl:template match="@*|node()" mode="M12" priority="-2">
    <xsl:apply-templates mode="M12" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:AuthorizedRepresentativeIdentifier" mode="M13" priority="1000">
    <svrl:fired-rule context="toop:AuthorizedRepresentativeIdentifier" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="text()=//toop:DataRequestSubject/toop:NaturalPerson/toop:PersonIdentifier" />
      <xsl:otherwise>
        <svrl:failed-assert test="text()=//toop:DataRequestSubject/toop:NaturalPerson/toop:PersonIdentifier">
          <xsl:attribute name="id">unmatched_person_rep_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: Use the value that is used in the element: DataSubject/NaturalPerson/PersonIdentifier.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M13" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M13" priority="-1" />
  <xsl:template match="@*|node()" mode="M13" priority="-2">
    <xsl:apply-templates mode="M13" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:ContactEmailAddress" mode="M14" priority="1000">
    <svrl:fired-rule context="toop:ContactEmailAddress" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="matches(text(),'[a-z0-9!#\$%&amp;''*+/=?^_`{{|}}~-]+(?:\.[a-z0-9!#\$%&amp;''*+/=?^_`{{|}}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?','i')" />
      <xsl:otherwise>
        <svrl:failed-assert test="matches(text(),'[a-z0-9!#\$%&amp;''*+/=?^_`{{|}}~-]+(?:\.[a-z0-9!#\$%&amp;''*+/=?^_`{{|}}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?','i')">
          <xsl:attribute name="id">invalid_email</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The e-mail address format looks invalid.
            </svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M14" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M14" priority="-1" />
  <xsl:template match="@*|node()" mode="M14" priority="-2">
    <xsl:apply-templates mode="M14" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->


	<!--RULE -->
<xsl:template match="toop:LegalEntityIdentifier" mode="M15" priority="1000">
    <svrl:fired-rule context="toop:LegalEntityIdentifier" />
    <xsl:variable name="varleilength" select="string-length(normalize-space(text()))" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$varleilength=20" />
      <xsl:otherwise>
        <svrl:failed-assert test="$varleilength=20">
          <xsl:attribute name="id">invalid_euid_length</xsl:attribute>
          <xsl:attribute name="flag">warning</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The LEI code length should be 20 (instead of <xsl:text />
            <xsl:value-of select="$varleilength" />
            <xsl:text />).
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
<xsl:template match="toop:DataElementResponseValue//toop:ResponseIndicator | toop:ErrorIndicator | toop:CopyIndicator | toop:SemanticMappingExecutionIndicator | toop:AlternativeResponseIndicator" mode="M16" priority="1000">
    <svrl:fired-rule context="toop:DataElementResponseValue//toop:ResponseIndicator | toop:ErrorIndicator | toop:CopyIndicator | toop:SemanticMappingExecutionIndicator | toop:AlternativeResponseIndicator" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="( (matches(normalize-space(text()),'^true$','i')) or (matches(normalize-space(text()),'^false$','i')) )" />
      <xsl:otherwise>
        <svrl:failed-assert test="( (matches(normalize-space(text()),'^true$','i')) or (matches(normalize-space(text()),'^false$','i')) )">
          <xsl:attribute name="id">value_is_true_or_false</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                The provided value should be a boolean: true or false. 
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
<xsl:template match="toop:DataElementResponseValue//toop:ResponseAmount" mode="M17" priority="1000">
    <svrl:fired-rule context="toop:DataElementResponseValue//toop:ResponseAmount" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="@currencyID" />
      <xsl:otherwise>
        <svrl:failed-assert test="@currencyID">
          <xsl:attribute name="id">mandatory_currency_id</xsl:attribute>
          <xsl:attribute name="flag">ERROR</xsl:attribute>
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>
                Rule: The currencyID attribute is mandatory (e.g. "EUR"). 
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
<xsl:template match="toop:DataElementResponseValue//toop:ResponseNumeric" mode="M18" priority="1000">
    <svrl:fired-rule context="toop:DataElementResponseValue//toop:ResponseNumeric" />

		<!--REPORT -->
<xsl:if test="matches(normalize-space(text()),'%')">
      <svrl:successful-report test="matches(normalize-space(text()),'%')">
        <xsl:attribute name="id">avoid_percentage</xsl:attribute>
        <xsl:attribute name="flag">ERROR</xsl:attribute>
        <xsl:attribute name="location">
          <xsl:apply-templates mode="schematron-select-full-path" select="." />
        </xsl:attribute>
        <svrl:text>
                Rule: do not format the percentage "%" symbol with the response value, just provide a float value (e.g. 0.4).
            </svrl:text>
      </svrl:successful-report>
    </xsl:if>
    <xsl:apply-templates mode="M18" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M18" priority="-1" />
  <xsl:template match="@*|node()" mode="M18" priority="-2">
    <xsl:apply-templates mode="M18" select="*|comment()|processing-instruction()" />
  </xsl:template>

<!--PATTERN -->
<xsl:variable name="countrycodes" select="document('..\codelists\std-gc\CountryIdentificationCode-2.1.gc')//Value[@ColumnRef='code']" />
  <xsl:variable name="identifier" select="normalize-space(//toop:PersonIdentifier)" />

	<!--RULE gc_check_country_countrycode-->
<xsl:template match="toop:CountryCode" mode="M19" priority="1001">
    <svrl:fired-rule context="toop:CountryCode" id="gc_check_country_countrycode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The country code must always be specified using the correct code list.</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M19" select="*|comment()|processing-instruction()" />
  </xsl:template>

	<!--RULE gc_check_id_countrycode-->
<xsl:template match="toop:LegalPersonUniqueIdentifier | toop:PersonIdentifier" mode="M19" priority="1000">
    <svrl:fired-rule context="toop:LegalPersonUniqueIdentifier | toop:PersonIdentifier" id="gc_check_id_countrycode" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes//SimpleValue[normalize-space(.) = (tokenize($identifier,'/')[1])]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes//SimpleValue[normalize-space(.) = (tokenize($identifier,'/')[1])]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The country code in the first part of the identifier must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="(tokenize($identifier,'/')[1])" />
            <xsl:text />).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$countrycodes//SimpleValue[normalize-space(.) = (tokenize($identifier,'/')[2])]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$countrycodes//SimpleValue[normalize-space(.) = (tokenize($identifier,'/')[2])]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>The country code in the second part of the identifier must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="(tokenize($identifier,'/')[2])" />
            <xsl:text />).</svrl:text>
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
<xsl:variable name="subjecttypecodes" select="document('..\codelists\gc\DataRequestSubjectTypeCode-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_subject_type_code-->
<xsl:template match="toop:DataRequestSubjectTypeCode" mode="M20" priority="1000">
    <svrl:fired-rule context="toop:DataRequestSubjectTypeCode" id="gc_check_subject_type_code" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$subjecttypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$subjecttypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A subject type code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="gendertypecodes" select="document('..\codelists\gc\Gender-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_gender_code-->
<xsl:template match="toop:GenderCode" mode="M21" priority="1000">
    <svrl:fired-rule context="toop:GenderCode" id="gc_check_gender_code" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$gendertypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$gendertypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A gender type code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="concepttypecodes" select="document('..\codelists\gc\ConceptTypeCode-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_concept_code-->
<xsl:template match="toop:ConceptTypeCode" mode="M22" priority="1000">
    <svrl:fired-rule context="toop:ConceptTypeCode" id="gc_check_concept_code" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$concepttypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$concepttypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A concept type code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="currencytypecodes" select="document('..\codelists\std-gc\CurrencyCode-2.1.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_currency_code-->
<xsl:template match="toop:ResponseAmount" mode="M23" priority="1000">
    <svrl:fired-rule context="toop:ResponseAmount" id="gc_check_currency_code" />
    <xsl:variable name="varcurrencyID" select="@currencyID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$currencytypecodes//SimpleValue[normalize-space(.) = $varcurrencyID]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$currencytypecodes//SimpleValue[normalize-space(.) = $varcurrencyID]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A currency type code code must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="$varcurrencyID" />
            <xsl:text />).</svrl:text>
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
<xsl:variable name="industrialtypecodes" select="document('..\codelists\gc\StandardIndustrialClassCode-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_industrial_code-->
<xsl:template match="toop:StandardIndustrialClassification" mode="M24" priority="1000">
    <svrl:fired-rule context="toop:StandardIndustrialClassification" id="gc_check_industrial_code" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$industrialtypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$industrialtypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A standard industrial class type code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="dataelementresponseerrorcodes" select="document('..\codelists\gc\DataElementResponseErrorCode-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_error_data_element_response-->
<xsl:template match="toop:DataElementResponseValue//toop:ErrorCode" mode="M25" priority="1000">
    <svrl:fired-rule context="toop:DataElementResponseValue//toop:ErrorCode" id="gc_check_error_data_element_response" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$dataelementresponseerrorcodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$dataelementresponseerrorcodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>An error code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="documentresponseerrorcodes" select="document('..\codelists\gc\DocumentResponseErrorCode-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_error_document_response-->
<xsl:template match="toop:DocumentResponse//toop:ErrorCode" mode="M26" priority="1000">
    <svrl:fired-rule context="toop:DocumentResponse//toop:ErrorCode" id="gc_check_error_document_response" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$documentresponseerrorcodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$documentresponseerrorcodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>An error code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="docrequesttypecodes" select="document('..\codelists\gc\DocumentRequestTypeCode-CodeList.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_doc_req_type-->
<xsl:template match="toop:DocumentRequestTypeCode | toop:DocumentTypeCode" mode="M27" priority="1000">
    <svrl:fired-rule context="toop:DocumentRequestTypeCode | toop:DocumentTypeCode" id="gc_check_doc_req_type" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$docrequesttypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$docrequesttypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A document request type code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="mimetypecodes" select="document('..\codelists\std-gc\BinaryObjectMimeCode-2.1.gc')//Value[@ColumnRef='code']" />

	<!--RULE gc_check_doc_mime_type-->
<xsl:template match="toop:PreferredDocumentMimeTypeCode | toop:DocumentMimeTypeCode" mode="M28" priority="1000">
    <svrl:fired-rule context="toop:PreferredDocumentMimeTypeCode | toop:DocumentMimeTypeCode" id="gc_check_doc_mime_type" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$mimetypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$mimetypecodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A MIME type code code must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="processidcodes" select="document('..\codelists\dynamic\ToopProcessIdentifiers-v1.gc')//Value[@ColumnRef='id']" />

	<!--RULE gc_check_process_id-->
<xsl:template match="toop:ProcessIdentifier" mode="M29" priority="1000">
    <svrl:fired-rule context="toop:ProcessIdentifier" id="gc_check_process_id" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$processidcodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]" />
      <xsl:otherwise>
        <svrl:failed-assert test="$processidcodes//SimpleValue[normalize-space(.) = normalize-space(current()/.)]">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A process identifier must always be specified using the correct code list.</svrl:text>
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
<xsl:variable name="agencyids" select="document('..\codelists\dynamic\ToopParticipantIdentifierSchemes-v1.gc')//Value[@ColumnRef='iso6523']" />

	<!--RULE mandatory_schemeagency_actors-->
<xsl:template match="toop:DCElectronicAddressIdentifier | toop:DPElectronicAddressIdentifier" mode="M30" priority="1000">
    <svrl:fired-rule context="toop:DCElectronicAddressIdentifier | toop:DPElectronicAddressIdentifier" id="mandatory_schemeagency_actors" />
    <xsl:variable name="thisId" select="@schemeAgencyID" />

		<!--ASSERT -->
<xsl:choose>
      <xsl:when test="$agencyids//SimpleValue[normalize-space(.)] = @schemeAgencyID" />
      <xsl:otherwise>
        <svrl:failed-assert test="$agencyids//SimpleValue[normalize-space(.)] = @schemeAgencyID">
          <xsl:attribute name="location">
            <xsl:apply-templates mode="schematron-select-full-path" select="." />
          </xsl:attribute>
          <svrl:text>A schemeAgencyID must always be specified using the correct code list (found:<xsl:text />
            <xsl:value-of select="$thisId" />
            <xsl:text />).</svrl:text>
        </svrl:failed-assert>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:apply-templates mode="M30" select="*|comment()|processing-instruction()" />
  </xsl:template>
  <xsl:template match="text()" mode="M30" priority="-1" />
  <xsl:template match="@*|node()" mode="M30" priority="-2">
    <xsl:apply-templates mode="M30" select="*|comment()|processing-instruction()" />
  </xsl:template>
</xsl:stylesheet>
