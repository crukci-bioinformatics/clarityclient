## Using The Client In Your Code

All the work of the client goes through the `ClarityAPI` interface.
[Take a look at its Javadoc for details of the operations it offers.](apidocs/org.cruk.clarity.api/org/cruk/clarity/api/ClarityAPI.html)
You should also consult the
[Clarity Developer documentation](https://d10e8rzir0haj8.cloudfront.net/6.0/REST.html)
for what operations are supported by the API and what they do.

Before you can start making calls to the API, you need to set the client
up with the URL of the Clarity API and credentials to access it. The
`setServer` method sets the base URL for API calls and should be set as the
base of any other Clarity URL. Don't include the "`/api/v2`" root of the
path in this URL, just the base up to and including the port.
The credentials need to be the user name and password of a
user who has API access to the Clarity system.

Operations that use the file store (`uploadFile` and
`deleteAndRemoveFile`) also need the credentials to access the file store
set. This is the user name of the file store owner (usually "_glsftp_") and its
password. The file store host is, by default, taken to be the same as the
LIMS server. If it is on a different host, this needs to be set explicitly
with a call to `setFilestoreServer`.

The API client can also be configured with a properties file with a call
to `setConfiguration` or at construction time (this needs a tweaking of
the Spring configuration). The properties file should contain:

* `api.server` - The root URL for the LIMS API.
* `api.user` - User name for the account to access the API.
* `api.pass` - The password for the user given in <api.user>.
* `filestore.server` - The host name of the file store server.
* `filestore.user` - The user name for the file store owner.
* `filestore.pass` - The password for the user given in <filestore.user>.

The properties file may also contain some other configuration options:

* `batch.size` - The maximum number of objects to fetch/create/update in one call to the API.
* `http.upload` - Whether to use the HTTP file upload mechanism introduced with Clarity 4.0.
* `http.upload.maximum` - The maximum size of file that can be uploaded over HTTP.
* `revert.to.sftp.upload` - Whether it is permissible to revert to SFTP uploads if the file is too large to send over HTTP.
* `http.direct.download` - Whether to download files in a HTTP file store directly from their server or whether to download through the Clarity API.

Any properties that are missing are quietly ignored when the properties
are read. If required information is missing, errors will be raised when you
make API calls.

### Thread safety

With the usual provisos of no liability etc, we think the client is safe to
use in multithreaded code once the initial configuration of the server and file
store URLs and credentials are set and are subsequently not changed.
The cache should also be safe to use with threaded code unless there is
something tricky about EhCache that we have missed.
