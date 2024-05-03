## Installation

The client can be used by including its JAR file and its dependencies
in your class path.

### With Maven

  The client can be added as a dependency to your POM:

```XML
<dependency>
    <groupId>org.cruk.clarity</groupId>
    <artifactId>clarity-client</artifactId>
    <version>...</version>
</dependency>
```

(Obviously, update the version number as later versions appear.)

We here at CRUK-CI's bioinformatics group host a Maven repository for
our code. You can let Maven do the work and fetch the client from us if
you add our repository's details to your POM:

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

If you are not running in an EE8 container, you will also need to provide
a JAXB implementation in your final code. This should be one of the Jakarta EE 10
implementations, namely a Glassfish implementation version 4.x.x:

```XML
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.5</version>
    <scope>runtime</scope>
</dependency>
```

### Without Maven

If you are not using Maven, you should also fetch this project's
dependencies and add them to your class path. The dependencies are given
in the [dependencies report](dependencies.html).

### Building from source

Assuming you have downloaded the source from GitHub, building the Java
API client is simple. You will need Maven version 3.5 or newer. Then building
is as simple as:

```
mvn install
```

You need to run this command from the project's top level directory. It will
build the client JAR file and install it in your local Maven cache.
