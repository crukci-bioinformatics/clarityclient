# CRUK-CI Genologics API Java Client

The Cancer Research UK Cambridge Institute (CRUK-CI) Genologics Java Client
provides the Java or Groovy developer a means to work with
[Genologics' REST API](https://www.genologics.com/developer/) using objects
rather than XML or DOM document trees.

It provides a single interface to perform (almost) all the operations
supported by the Genologics' REST API with XML to Java object conversion,
error handling and, optionally, client side caching. The developer works
with Java objects that map onto the XML documents described by Genologics'
API documentation.

It uses the JAXB object to XML binding mechanism to convert the XML
messages sent to and received from Genologics' REST API. The communication
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

    mvn install

This will build and install the project into your local Maven cache.
You'll need Maven 3.5 or newer.

Alternatively, you can add our Maven repository to your POM and let
Maven do the work. Add a <repositories> section containing:

```XML
    <repository>
        <id>crukci-bioinformatics</id>
        <url>http://content.cruk.cam.ac.uk/bioinformatics/maven</url>
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
        <groupId>org.cruk.genologics</groupId>
        <artifactId>clarity-client</artifactId>
        <version>...</version>
    </dependency>
```

(Fill in the <version> tag with the version of the API.)

For details of using the API, please refer to the documentation at
<http://crukci-bioinformatics.github.io/clarityclient>

## Java Version

From version 2.24.16 of the client, the build has been changed to build
using Java 8 source and bytecode. It's no longer realistic to keep the
code stuck on Java 6: the true Java 6 JVM is not maintained, and there
are problems with its SSL certificates no longer being valid. From
release 2.24.12 the code has been tested with Java 11. It does not include
Java 9+ module information.

The code was set at Java 6 for what now seems like the foolish idea of
deploying in-house server code in the JBoss 4.2.3 that is installed
with Clarity 4. That approach has been abandoned here at CRUK-CI and is
not recommended.

## JAXB Implementation

The Java EE modules used by the client, specifically JAXB, have been
move to **Jakarta EE** specifications from version the 2.24.16, complying
with [Jakarta EE 8](https://jakarta.ee/release/8). The traditional
`javax.xml.bind` packages produce issues with class module clashes
when building under newer JDKs. The move should not have any noticeable
side effects.

The POM will pull in the Jakarta JAXB API (version 2.3) that the code needs to
compile. It marks the actual implementation of JAXB as an optional
dependency, so other code that uses this client will not automatically
have a JAXB implementation when the JRE is Java 9 or newer. One should
add the JAXB implementation to the final POM:

```XML
    <dependency>
        <groupId>com.sun.xml.bind</groupId>
        <artifactId>jaxb-impl</artifactId>
        <version>2.3.3</version>
        <scope>runtime</scope>
    </dependency>
```

The scope should be `runtime` for building stand alone applications
using the client. Where one has created another tool that uses the API
but isn't itself a final application, the scope should be `test` if
unit tests need to use the client (if not, this dependency isn't needed).
Where the client is part of a JEE container, the container will supply
the JAXB implementation.

There is no harm in adding this dependency when running on a Java 8 JRE
but it is not necessary.
