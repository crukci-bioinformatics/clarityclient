# CRUK-CI Clarity API Java Client

The Cancer Research UK Cambridge Institute (CRUK-CI) Clarity Java Client
provides the Java or Groovy developer a means to work with
[Clarity's REST API](https://d10e8rzir0haj8.cloudfront.net/6.0/REST.html) using objects
rather than XML or DOM document trees.

It provides a single interface to perform (almost) all the operations
supported by the Clarity REST API with XML to Java object conversion,
error handling and, optionally, client side caching. The developer works
with Java objects that map onto the XML documents described by Illumina's
Clarity API documentation.

It uses the JAXB object to XML binding mechanism to convert the XML
messages sent to and received from Clarity's REST API. The communication
uses Apache's HTTP Java client and Spring's REST client. Spring is used
throughout this tool.

## Features

1. Automatic conversion between XML and Java objects.
2. Automatic handling of exceptions from the server.
3. Automatic and transparent use of batch retrieve, update and create
mechanisms where available.
4. Creation of files with the upload to the file store (likewise file
removal).
5. Optional transparent caching mechanism.
6. Debugging classes to show traffic and timings between the client
and server.


## Building

Having got this check out of the code, run:

```
mvn install
```

This will build and install the project into your local Maven cache.
You'll need Maven 3.5 or newer.

Alternatively, you can add our Maven repository to your POM and let
Maven do the work. Add a &lt;repositories&gt; section containing:

```XML
<repository>
    <id>crukci-bioinformatics</id>
    <url>https://content.cruk.cam.ac.uk/bioinformatics/maven</url>
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
</repository>
```

## Usage

Add the JAR file to your POM (I'm assuming you're using Maven now):

```XML
<dependency>
    <groupId>org.cruk.clarity</groupId>
    <artifactId>clarity-client</artifactId>
    <version>...</version>
</dependency>
```

_Fill in the <version> tag with the version of the API._
_For this branch, and code using EE10, the version should start "2.31.ee10"._

For details of using the API, please refer to the documentation at
https://crukci-bioinformatics.github.io/clarityclient

## Clarity 6

The 2.31+ versions of the library have been updated to work with Clarity 6,
supporting the corresponding versions of the Clarity API.

## Java Version

The Clarity client is built with Java 17 source and bytecode. Jakarta EE 10
brings some dependencies compiled for this version of Java, so that is the
oldest version now supported.

## JAXB Implementation

The Java EE modules used by the client, specifically JAXB, have been
moved to **Jakarta EE** specifications, complying
with [Jakarta EE 10](https://jakarta.ee/release/10).

The POM will pull in the Jakarta JAXB API (version 4) that the code needs to
compile. One should add a JAXB implementation to the final POM, such as:

```XML
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.5</version>
    <scope>runtime</scope>
</dependency>
```

The scope should be `runtime` for building stand alone applications
using the client. Where one has created another tool that uses the API
but isn't itself a final application, the scope should be `test` if
unit tests need to use the client (if not, this dependency isn't needed).
Where the client is part of an EE container, the container will supply
the JAXB implementation.

JAXB version 4.x.x is for EE10, with its renaming of `javax.xml.bind`
to `jakarta.xml.bind`, and is not suitable for this build of the
Clarity client. Also the `com.sun.xml.bind:jaxb-impl` artifacts
available in Maven won't work well with newer JREs.

## Other Branches

This project has some other streams of the code for legacy reasons.

1. `clarity4`: A version of the client using the 2.24.1 schema of the API,
the latest for Clarity 4.3.
2. `clarity5`: A version using the 2.28 schema, the latest for Clarity 5.
3. `clarity6-ee8`: A version using this same version of the schema (2.31)
but built for EE8.

Illumina is due to stop support for Clarity versions 4 and 5 in June 2022,
so one would expect no more changes on those branches.
