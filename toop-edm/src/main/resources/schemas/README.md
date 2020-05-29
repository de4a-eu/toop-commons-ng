# TOOP EDM Schemas

## EDM Request

* Base file: `CV-Agent.xsd`

## EDM Response

* Base file: `dcat-ap.xsd`

## EDM Error Response

* Base file: `CV-Agent.xsd`

## adms.xsd

Target namespace: `http://www.w3.org/ns/adms#`

Dependencies:
* dcat-ap.xsd
* rdf.xsd
* skos.xsd

## cccev-2.0.0.xsd

Target namespace: `https://semic.org/sa/cv/cccev-2.0.0#`

Dependencies:
* CV-CommonAggregateComponents.xsd
* CV-CommonBasicComponents.xsd
* dcat-ap.xsd
* CV-Agent.xsd
* foaf.xsd

External dependencies:
* `urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2`

## CoreVocabularies-AggregateComponents-1.1.xsd

Target namespace: `http://www.w3.org/ns/corevocabulary/AggregateComponents`

Dependencies:
* CoreVocabularies-BasicComponents-1.1.xsd

## CoreVocabularies-BasicComponents-1.1.xsd

Target namespace: `http://www.w3.org/ns/corevocabulary/BasicComponents`

External dependencies:
* `urn:oasis:names:specification:ubl:schema:xsd:UnqualifiedDataTypes-2`

## CV-Agent.xsd

Target namespace: `https://semic.org/sa/cv/cagv/agent-2.0.0#`

Dependencies:
* CV-CommonAggregateComponents.xsd
* CV-CommonBasicComponents.xsd
* dcterms.xsd
* foaf.xsd
* locn.xsd
* org.xsd
* regorg.xsd
* skos.xsd

## CV-CommonAggregateComponents.xsd

Target namespace: `https://semic.org/sa/cv/common/cac-2.0.0#`

Dependencies:
* CV-CommonBasicComponents.xsd
* locn.xsd

## CV-CommonBasicComponents.xsd

Target namespace: `https://semic.org/sa/cv/common/cbc-2.0.0#`

Dependencies:
* CV-DataTypes.xsd

## CV-DataTypes.xsd

Target namespace: `https://semic.org/sa/cv/common/dataTypes-2.0.0#`

External dependencies:
* `urn:un:unece:uncefact:data:specification:CoreComponentTypeSchemaModule:2`

## dcat-ap.xsd

Target namespace: `http://data.europa.eu/r5r/`

Dependencies:
* dcterms.xsd
* CV-CommonBasicComponents.xsd
* foaf.xsd
* adms.xsd
* odrl.xsd
* rdf.xsd
* skos.xsd
* spdx.xsd
* vcard.xsd

## dcterms.xsd

Target namespace: `http://purl.org/dc/terms/`

Dependencies:
* foaf.xsd
* locn.xsd
* skos.xsd
* rdf.xsd

## foaf.xsd

Target namespace: `http://xmlns.com/foaf/0.1/`

Dependencies:
* *none*

## locn.xsd

Target namespace: `http://www.w3.org/ns/locn#`

Dependencies:
* *none*

## odrl.xsd

Target namespace: `http://www.w3.org/ns/odrl/2/`

Dependencies:
* *none*

## org.xsd

Target namespace: `http://www.w3.org/ns/org#`

Dependencies:
* foaf.xsd
* skos.xsd

## rdf.xsd

Target namespace: `http://www.w3.org/1999/02/22-rdf-syntax-ns#`

External dependencies:
* `http://www.w3.org/XML/1998/namespace`

## regorg.xsd

Target namespace: `http://www.w3.org/ns/regorg#`

Dependencies:
* dcterms.xsd
* org.xsd
* CV-CommonBasicComponents.xsd

## skos.xsd

Target namespace: `http://www.w3.org/2004/02/skos/core#`

Dependencies:
* *none*

## spdx.xsd

Target namespace: `spdx:xsd::1.0`

Dependencies:
* *none*

## vcard.xsd

Target namespace: `http://www.w3.org/2001/vcard-rdf/3.0#`

External dependencies:
* `http://www.w3.org/XML/1998/namespace`
* `http://www.w3.org/1999/xlink`
