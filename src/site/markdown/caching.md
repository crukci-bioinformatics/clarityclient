## Caching

Using the in-memory cache with the client can **massively** increase
the speed a script can run. It accumulates a number of entities from REST API
calls so subsequent calls to retrieve these same entities need not pass through
to the API. This also has the effect to simplifying user code, as the script
can make the calls through the
[ClarityAPI](apidocs/org.cruk.clarity.api/org/cruk/clarity/api/ClarityAPI.html)
interface without worrying
about whether it already has some or all of the objects requested.

Like any client side caching, there are risks that an entity will be updated
from elsewhere while the client holds an earlier copy of the object. This
hasn't presented us much of a problem in EPP scripts (where this client is
used a lot) as these run from beginning to end and then stop, so the lifetime
of the cache is simply the duration of the script.

If used in a server environment, the cache can still be beneficial but it
may need to be tweaked so objects don't live as long in the cache. We've found
that most operations rarely hit the same objects from two different people
(though this is a risk) and the performance gain much outweighs the risk of
out of date information. The cache can be tuned or disabled as required by
the application.

### Enabling the cache

The cache is essentially an aspect around the main `ClarityAPI`
implementation. It is configured with Spring and can be enabled for an
application by adding a file to Spring's application context path:

```
/org/cruk/clarity/api/clarity-cache-context.xml
```

This configuration does require the general
`/org/cruk/clarity/api/clarity-client-context.xml` configuration
to be in Spring's application context path as it depends on beans defined
there.

`clarity-cache-context.xml` is provided in the client's JAR file.

### Tuning the cache

The cache is controlled by the file:

```
/org/cruk/clarity/api/ehcache.xml
```

This file is also provided in the client's JAR file. It provides caches
for different entities that can be individually tuned for each class in
the API. Any class that does not have a specific cache defined goes into
the catch-all cache _com.genologics.ri.LimsEntity_.

  If you do need to change the cache configuration, make a new
`org/cruk/clarity/api/ehcache.xml` that appears in a folder or JAR
before the client's JAR file in the class path.

As of release 2.31, the implementation of EhCache used has been moved
from version 2 to version 3. This has resulted in a change in how the
_ehcache.xml_ file is structured. The file in this project provides an
example, and the full documentation can be found on the
(EhCache web site)[https://www.ehcache.org/documentation/3.0/xml.html].

### Bulk fetch, create and update operations

Real world use has found that the bulk operations for fetching, creating
and updating objects from the API has a "sweet spot" for the number of objects
in an operation. This is a number that minimises the request time per object.
For example, if one tries fetching 10,000 artifacts in one call to the
API's "`.../artifacts/batch/retrieve`" end point it will take longer than
making 20 calls for 500 artifacts (500 per call is the optimum number given
by Genologics, though testing has shown this can vary between installations).

As of release 2.22, the client library will batch bulk operations with
large numbers of objects into one or more calls to the relevant API end point
transparently to the user. So we can request our 10,000 artifacts in one call
to the client and let that fetch them in the most efficient manner.

The number of objects to retrieve or send in each batch can be tuned
with the `setBulkOperationBatchSize` method on the client. The default
is the recommended 500 per call to the API.

## Artifacts and their state parameter

The `Artifact` class provides some complication to the cache by
always having a _state_ parameter attached to its URIs. This means a given
artifact can be returned in different states depending on the state in the
URI.

The cache can be put in one of three modes for dealing with `Artifact` objects:

1. `EXACT`: The version of the artifact requested by the state
parameter must match the version in the cache exactly. If it is older or
newer than the cached version, the requested version is fetched and replaces
that already in the cache.
2. `LATEST`: If the version of the artifact requested by the state
parameter is newer than the version in the cache, the requested version is
fetched and replaces the version in the cache. If it is the same version or
older, the newer version in the cache is returned.
3. `ANY`: The cached version of the artifact is returned regardless
of the version requested by the state parameter.

If an artifact is requested without an explicit state in the URI, the
artifact in the cache is returned regardless of its version or the mode of
cache operation.

The default mode is `LATEST`, which is the way the cache behaved in
releases of the client before 2.22. Modes can be set in the Spring
configuration (`clarity-cache-context.xml`) or programmatically on
the `ClarityAPICache` bean using the `setStatefulBehaviour` method.

### Overriding the cache behaviour for a single fetch

In actual use, the only time the state parameter is important for an
artifact is when one is looking for its QC flag value. No other fields have
a history that is preserved by the state: they are always their current database
value. Most of the time the cache behaviour is irrelevant, and the `LATEST`
or `ANY` cache behaviours are most suitable for the performance gain they
provide. This is not true when using the post process URI of the input/output
maps to obtain the QC flags.

The `overrideStateful` method of [ClarityAPI](apidocs/org.cruk.clarity.api/org/cruk/clarity/api/ClarityAPI.html)
allows a single call to be made through the API that will
fetch the artifact at either the exact version given in the URI or the latest
available state.

When `overrideStateful` is set to `EXACT`, the client will call
through to the Clarity API unless we already have exactly this version of the
artifact in the cache. As the call returns the cache follows its normal rules
for whether this version of the artifact is cached.

When `overrideStateful` is set to `LATEST`, the client will remove
the _state_ parameter from the artifact's URI before always calling through
to the Clarity API. Again, as the call returns the cache follows its normal rules
for whether this version of the artifact is cached. If the client is again
instructed to fetch one of the artifact with this same override, the call will
go through to the Clarity API again.

The stateful override behaviour is only relevant to artifacts and is in place for
exactly one call to the API after the `overrideStateful` call. It will
always be cleared after the next call to the API, regardless of whether it
actually has an effect after that call (e.g. calling `overrideStateful`
and then `getServer` will cause the override to be cleared), so one
should put the call to `overrideStateful` immediately before the call to
`retrieve`, `load`, `loadAll` or `reload`. This behaviour is
local to the calling thread, so will not affect calls from other threads.
