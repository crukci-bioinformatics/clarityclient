## Using The CRUK-CI Client Within A Jakarta EE 10 Application Server

### Using the cache in an application server

There isn't anything inherently wrong with using the cache in a service
deployed in the application server, but if you leave the time objects can
remain in the cache too long you're more likely to get stale information
back from the client. We've found in practice that the cache is still very
useful on the server, but you don't want objects to live in it for much
longer than it takes to process individual calls to your service: one or
two minutes is fine. Remember that the cache is shared, so multiple
independent requests may hit the same objects in the cache when deployed
in a server. A stand alone program is not going to have the same
interference from different sources.

Take a copy of the `org/cruk/clarity/api/ehcache.xml` file from
the JAR or the source of this project and adapt it to your needs, typically
by reducing the _expiry_ element on each of the caches. Put
this file in your WAR's resources in the same path, so:

```
WEB-INF/classes/org/cruk/clarity/api/ehcache.xml
```

This will take precedence over the file in the client JAR.

Note that to use the `jakarta` namespace, the Maven dependency for
ehcache needs the "jakarta" classifier:

```XML
<dependency>
    <groupId>org.ehcache</groupId>
    <artifactId>ehcache</artifactId>
    <version>3.10.8</version>
    <classifier>jakarta</classifier>
</dependency>
```
