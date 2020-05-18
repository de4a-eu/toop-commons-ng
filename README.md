# TOOP Commons NG

This is the successor project of the old [toop-commons](https://github.com/TOOP4EU/toop-commons) project.
The code contained in this project is used by:
* https://github.com/TOOP4EU/toop-connector-ng - TOOP Connector NG (TC NG)
* https://github.com/TOOP4EU/data-services-directory - Data Services Directory (DSD)

## Status

Work in progress
* Initial copy from TOOP Connector NG for easier dependency reuse
* Added new Request query definition "Object Reference" to request a document with an ID only
* Renamed `getReader()` to `reader()`
* Updated the Schematron rules
* A new response object layout was introduced: list of RegRep `ObjectRef`s - it's referred to as "DocumentRef" internally
* The `EDMRequest` has a new mandatory property: "ResponseOption" - this determines if the payload is contained in the response or referenced
* `EDMResponse` now has explicit builder classes depending on the result layout: `EDMResponse.ConceptBuilder`, `EDMResponse.DocumentBuilder` and `EDMResponse.DocumentRefBuilder`

2020-05-12: release of `v2.0.0-beta2`
* Changed the main EDM classes for request, response and error response to `EDMRequest`, `EDMResponse` and `EDMErrorResponse`
* Added `getReader()` and `getWriter()` methods to easily read and write these objects from and to different structures

2020-05-06: release of `v2.0.0-beta1`
* Libraries for creating the new data model
* Consisting of `toop-edm`, `toop-regrep`, `toop-kafka-client` and `toop-commons`
* Allows to create Requests, Responses and Errors according to the new EDM (Electronic Data Model)

## Maven coordinates

```xml
      <dependency>
        <groupId>eu.toop</groupId>
        <artifactId>toop-edm</artifactId>
        <version>2.0.0-beta2</version>
      </dependency>
```

The rest comes via transitive dependencies.

## Building

Requires at least

* Java 1.8 or later
* Apache Maven for building

Do an initial `mvn clean install` on the command line.

Afterwards don't forget to add the following paths to your build path (in your IDE):

* toop-regrep/target/generated-sources/xjc
* toop-edm/target/generated-sources/xjc

Note: the `toop-codelist-tools` is for internal usage only.
