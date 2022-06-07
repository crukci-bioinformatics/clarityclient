## Known Issues

The caching issue has been addressed from release 2.22 by allowing the
user to choose a mode of operation most suitable to their application.
See the "Artifacts and Their State Parameter" section of the
[caching page](caching.html) for details.

Bulk fetch, create and update operations can suffer when the number of
objects requested or sent is too large. Version 2.22 introduced batching of
bulk operations so that each call to the server sends a maximum number of
objects to fetch or update.
See the "Bulk Fetch, Create and Update Operations" section of the
[caching page](caching.html) for details.

Uploading files to the file store with the HTTP mechanism will fail if
the process the files are being attached to has completed. It seems this is a
deliberate restriction in the
[files/limsid/upload](https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.files.limsid.upload.html)
end point. The older mechanism of uploading to the file store with SFTP does
not have this restriction and so, where possible, this method is preferred
(HTTP uploads are deactivated in the client by default).
