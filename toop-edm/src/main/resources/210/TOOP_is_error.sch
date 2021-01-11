<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) 2018-2021 toop.eu. All rights reserved.

    This project is dual licensed under Apache License, Version 2.0
    and the EUPL 1.2.

     = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

     = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

    Licensed under the EUPL, Version 1.2 or – as soon they will be approved
    by the European Commission - subsequent versions of the EUPL
    (the "Licence");
    You may not use this work except in compliance with the Licence.
    You may obtain a copy of the Licence at:

            https://joinup.ec.europa.eu/software/page/eupl

-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    queryBinding="xslt2" 
    >
    <ns prefix="query"  uri="urn:oasis:names:tc:ebxml-regrep:xsd:query:4.0"/>
    <ns prefix="rs"     uri="urn:oasis:names:tc:ebxml-regrep:xsd:rs:4.0"/>
    
    
    <title>TOOP EDM - isError Checker</title>
    
    
    <pattern>
        <rule context="/">
            <assert test="( (exists(query:QueryResponse)) and (exists(query:QueryResponse/rs:Exception)) )"  flag='ERROR' id="NOT_AN_ERROR">
                The message does not look like an Error (meaning: a  QueryResponse including Exceptions). 
            </assert>
        </rule>
    </pattern>
    
    
</schema>