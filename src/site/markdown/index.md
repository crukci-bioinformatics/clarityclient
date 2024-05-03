## CRUK-CI Clarity API Java Client

The Cancer Research UK Cambridge Institute (CRUK-CI) Clarity Java Client
provides the Java or Groovy developer a means to work with Clarity's REST
API using objects rather than XML or DOM document trees.

It provides a single interface to perform (almost) all the operations
supported by the Clarity REST API with XML to Java object conversion,
error handling and, optionally, client side caching. The developer works
with Java objects that map onto the XML documents described by Illumina's
API documentation.

It uses the JAXB object to XML binding mechanism to convert the XML
messages sent to and received from Clarity's REST API. The communication
uses Apache's HTTP Java client and Spring's REST client. Spring is used
throughout this tool.

### Features

1. Automatic conversion between XML and Java objects.
2. Automatic handling of exceptions from the server.
3. Automatic and transparent use of batch retrieve, update and create mechanisms where available.
4. Creation of files with the upload to the file store (likewise file removal).
5. Optional transparent caching mechanism.
6. Debugging classes to show traffic and timings between the client and server.

## Clarity 6

The 2.31 and later versions of the library has been updated to work with Clarity 6,
supporting version 2.31+ of the Clarity API. The opportunity has been taken to
move things on with the client with this release. -It is now based on Java 11- and
is packaged with Java's module information. It has also provided a sensible time
to do some repackaging, reflecting Genologics' disappearance as a separate company
after its purchase by and absorbtion into Illumina.

1. Anything in the packages below `org.cruk.genologics` is now under `org.cruk.clarity`.
2. `GenologicsAPI` is now `ClarityAPI`, with the implementation classes also renamed.
3. `com.genologics.ri.process.GenologicsProcess` is now `com.genologics.ri.process.ClarityProcess`.
4. `com.genologics.ri.file.GenologicsFile` is now `com.genologics.ri.file.ClarityFile`.
5. The Spring XML files are renamed from `genologics-*.xml` to `clarity-*.xml`.
6. Any Spring beans in the configuration files prefixed with "genologics" are now prefixed with "clarity", for example `clarityAPIBase`.

The client has kept the `com.genologics.ri` namespace for the model classes because
that namespace is still what is used in the XSD files provided by Illumina.

### Jakarta EE 10

The EE 10 version of the code is based on Java 17 and Jakarta EE 10. This is the
code on the "master" branch in GitHub. The Java EE 8 version is on the "ee8" branch.
We are moving all our code to EE 10 so the EE 8 branch may not be maintained beyond
API version 2.31.
