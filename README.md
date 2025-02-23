**This project has been discontinued.** It has been replaced by
[Clarity Client 2](https://github.com/crukci-bioinformatics/clarityclient2),
which is a repackaging of this project and the recorder combined with an
update to Jakarta EE 10.

There will not be any further updates to this project. If anyone is using
it and needs the Clarity 6.3 API additions, please raise an issue.

# CRUK-CI Clarity API Java Client

The Cancer Research UK Cambridge Institute (CRUK-CI) Clarity Java Client
provides the Java or Groovy developer a means to work with
[Clarity's REST API](https://d10e8rzir0haj8.cloudfront.net/6.2/REST.html) using objects
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
_For this branch, and code using EE8, the version should start "2.31.ee8"._

For details of using the API, please refer to the documentation at
https://crukci-bioinformatics.github.io/clarityclient

## Clarity 6

The 2.31+ versions of the library have been updated to work with Clarity 6,
supporting the corresponding versions of the Clarity API.

## Java Version

The Clarity client is built with Java 11 source and bytecode. The release
of Clarity 6 has provided a good time to move the requirements for this
library forward in Java terms. It now includes Java module information.

## JAXB Implementation

The Java EE modules used by the client, specifically JAXB, have been
moved to **Jakarta EE** specifications, complying
with [Jakarta EE 8](https://jakarta.ee/release/8).
The move should not have any noticeable side effects.

The POM will pull in the Jakarta JAXB API (version 2.3) that the code needs to
compile. One should add a JAXB implementation to the final POM, such as:

```XML
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.6</version>
    <scope>runtime</scope>
</dependency>
```

The scope should be `runtime` for building stand alone applications
using the client. Where one has created another tool that uses the API
but isn't itself a final application, the scope should be `test` if
unit tests need to use the client (if not, this dependency isn't needed).
Where the client is part of an EE container, the container will supply
the JAXB implementation.

JAXB version 3.x.x is for EE9, with its renaming of `javax.xml.bind`
to `jakarta.xml.bind`, and is not suitable for this build of the
Clarity client. Also the `com.sun.xml.bind:jaxb-impl` artifacts
available in Maven won't work well with newer JREs.

## Other Branches

The `master` branch is the Jakarta EE 8 version. It is the same as the
`ee8` branch except the artifacts don't have the "ee8" part of the version
label.

The move to EE10 was going to be on the master branch with the EE8 code
on the "ee8" branch, but work done on the client in the move to EE10 has
involved such substantial changes to the code beyond changing the namespace
of the EE classes that it's been created as a new project. This is available
as [Clarity Client 2](https://github.com/crukci-bioinformatics/clarityclient2).

Illumina is due to stop support for Clarity versions 4 and 5 in June 2022,
so one would expect no more changes on those branches.

