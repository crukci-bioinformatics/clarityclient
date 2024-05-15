/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2013 Cancer Research UK Cambridge Institute.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cruk.clarity.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.hc.client5.http.auth.Credentials;
import org.springframework.web.client.HttpClientErrorException;

import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.Locatable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.file.ClarityFile;
import com.genologics.ri.process.ClarityProcess;
import com.genologics.ri.processexecution.ExecutableProcess;
import com.genologics.ri.routing.Routing;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.step.Actions;
import com.genologics.ri.step.AvailableProgram;
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.ProgramStatus;
import com.genologics.ri.step.StepCreation;
import com.genologics.ri.stepconfiguration.ProtocolStep;


/**
 * Interface to the Clarity API version 2 via REST.
 *
 * <p>
 * This interface provides the basic methods one would expect to interact
 * with the server via REST. It and the objects under the {@code com.genologics.ri}
 * packages hide much of the trickiness from users of this class.
 * </p>
 *
 * <p>
 * All methods that make calls to the server throw some run time exceptions that
 * client code should expect and handle as appropriate:
 * </p>
 *
 * <ul>
 * <li>
 * {@link ClarityException} - thrown when the server sends back a response that
 * indicates an exception being raised on the server. The reply is automatically
 * unmarshalled and thrown as this equivalent Java exception.
 * </li>
 * <li>
 * {@link ClarityUpdateException} - indicates more an error in client code, where
 * an update is attempted on an entity that does not allow such an update. This can
 * apply to create, update and delete methods. Refer to the REST documentation
 * for details of what is and is not allowed.
 * </li>
 * <li>
 * {@link IllegalStateException} - thrown when calls are made to the server before
 * the server URL has been set.
 * </li>
 * <li>
 * {@link HttpClientErrorException} - thrown when a call to the server doesn't get as
 * far as being processed. A typical example is when the credentials have not been set.
 * </li>
 * <li>
 * {@link IllegalArgumentException} - thrown when parameters that must clearly be given
 * are {@code null}. <i>entityClass</i> parameters are a prime example.
 * </li>
 * </ul>
 *
 * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/REST.html">Genologic's REST documentation</a>
 * @see <a href="https://hc.apache.org/httpcomponents-client-4.5.x/index.html">Apache HTTP
 * Components client</a>
 */
public interface ClarityAPI
{
    // Configuration methods

    /**
     * Get the base URL of the Clarity server. This does not include the path to the API,
     * just the protocol, host and port.
     *
     * @return The server base URL.
     *
     * @throws IllegalStateException if the API has not been configured.
     */
    URL getServer();

    /**
     * Set the base URL to the Clarity server.
     *
     * @param serverAddress The base URL. Should just be the protocol, host and port with
     * no path.
     *
     * @throws IllegalArgumentException if {@code serverAddress} is null.
     */
    void setServer(URL serverAddress);

    /**
     * Get the base address of the REST API. This is the server base address plus the
     * API path ({@code /api/v2}).
     *
     * @return The API base address.
     */
    String getServerApiAddress();

    /**
     * Get the user name of the researcher currently connecting to the server.
     *
     * @return The user name used for connections.
     */
    String getUsername();

    /**
     * Set the credentials to use to connect to the REST API.
     *
     * @param username The researcher's user name.
     * @param password The researcher's password.
     *
     * @throws IllegalArgumentException if {@code username} is null.
     */
    void setCredentials(String username, String password);

    /**
     * Set the credentials to use to connect to the REST API.
     *
     * @param httpCredentials A Credentials object for the HTTP client.
     * Does nothing if this is {@code null}.
     */
    @SuppressWarnings("exports")
    void setCredentials(Credentials httpCredentials);

    /**
     * Set the name of the file store host.
     *
     * @param host The name of the file store server.
     *
     * @throws IllegalArgumentException if {@code host} is empty.
     */
    void setFilestoreServer(String host);

    /**
     * Set the credentials used to connect to the file store.
     *
     * @param username The file store user account.
     * @param password The file store user password.
     *
     * @throws IllegalArgumentException if {@code username} is null.
     */
    void setFilestoreCredentials(String username, String password);

    /**
     * Set configuration from a properties file. The properties may contain any
     * of the following:
     *
     * <ul>
     * <li>{@code api.server} - The base URL of the Clarity REST API (see {@link #setServer(URL)}).</li>
     * <li>{@code api.user} - The user to access the API as (see {@link #setCredentials(String, String)}).</li>
     * <li>{@code api.pass} - The password for {@code api.user}.</li>
     * <li>{@code filestore.server} - The host name for the file store (see {@link #setFilestoreServer(String)}).</li>
     * <li>{@code filestore.user} - The user to access the file store as (see {@link #setFilestoreCredentials(String, String)}).</li>
     * <li>{@code filestore.pass} - The password for {@code filestore.user}.</li>
     *
     * <li>{@code batch.size} - The maximum number of objects to fetch/create/update in one call to the API
     *          (see {@link #setBulkOperationBatchSize(int)}).</li>
     * <li>{@code http.upload} - Whether to use the HTTP file upload mechanism introduced with Clarity 4.0
     *          (see {@link #setUploadOverHttp(boolean)}).</li>
     * <li>{@code http.upload.maximum} - The maximum size of file that can be uploaded over HTTP
     *          (see {@link #setHttpUploadSizeLimit(long)}).</li>
     * <li>{@code revert.to.sftp.upload} - Whether it is permissible to revert to SFTP uploads if the file is
     *          too large to send over HTTP (see {@link #setAutoRevertToSFTPUploads(boolean)}).</li>
     * <li>{@code http.direct.download} - Whether to download files in a HTTP file store directly from
     *          their server or whether to download through the Clarity API
     *          (see {@link #setDownloadDirectFromHttpStore(boolean)}).</li>
     * </ul>
     *
     * <p>
     * The properties file does not have to contain all of these values. Any that are
     * missing are simply ignored and, where possible, left as their defaults.
     * </p>
     *
     * @param configuration The properties file to configure from.
     *
     * @throws InvalidURIException if the {@code api.server} property is set to
     * an invalid URL.
     */
    void setConfiguration(Properties configuration);

    /**
     * Set the number of objects that will be fetched, updated or created in each
     * call the Clarity API for the bulk operations (those that use a collection of
     * links or objects).
     *
     * <p>
     * It seems that naively asking for or sending all the links or objects in the
     * collections does not sit well with the back end server. Experimentation has
     * shown that the fastest overall operation time (at least for fetching artifacts)
     * occurs when between twenty and thirty links or artifacts are in the POST
     * call to the Clarity API.
     * </p>
     *
     * <p>
     * The default is Genologics' recommendation of a "sweet spot" of 500.
     * </p>
     *
     * @param batchSize The number of objects to fetch or send per call to the API.
     * A maximum of 10000 objects per batch is imposed because performance noticeably
     * drops off long before this number of objects is used. If this argument is less
     * than or equal to zero, all object or links in the collection will be sent at
     * once up to the 10000 per call hard limit.
     *
     * @see #loadAll(Collection)
     * @see #createAll(Collection)
     * @see #updateAll(Collection)
     * @see #deleteAll(Collection)
     */
    void setBulkOperationBatchSize(int batchSize);

    /**
     * Set whether uploads over HTTP are permitted using the {@code files/id/upload}
     * API end point, or whether only SFTP uploads are allowed.
     * <p>
     * Note that as of release 2.23, downloads from the regular file store take place
     * through the {@code files/id/download} end point. This solves any problems for
     * SFTP access to the file store. Files in a HTTP file store may be handled
     * differently (see {@link #setDownloadDirectFromHttpStore(boolean)}).
     * </p>
     * <p>
     * The default is to perform uploads of files no bigger than the limit through
     * HTTP.
     * </p>
     *
     * @param uploadOverHttp true if files can be uploaded using HTTP,
     * false if not.
     *
     * @since 2.23
     */
    void setUploadOverHttp(boolean uploadOverHttp);

    /**
     * Set the maximum size of file that can be uploaded using HTTP.
     * This must be no greater than the corresponding Clarity configuration
     * property {@code api.artifacts.max.resultfile.upload.size}.
     * <p>
     * The default file size limit is 10,485,760 bytes (10MB), which is the
     * default setting for the Clarity configuration property.
     * </p>
     *
     * @param limit The maximum size of file allowed over HTTP.
     *
     * @throws IllegalArgumentException if {@code limit} is less than one.
     *
     * @since 2.23
     */
    void setHttpUploadSizeLimit(long limit);

    /**
     * Set whether the SFTP mechanism can be used as a fall back for uploading files if
     * a file exceeds the HTTP upload size limit.
     * <p>
     * The default is to allow the SFTP upload mechanism to be used if a file is too
     * large for HTTP.
     * </p>
     *
     * @param autoRevertToSFTP true to use SFTP to transfer files that are too large
     * for HTTP, false to fail if such a file is given.
     *
     * @see ClarityAPI#uploadFile(LimsEntityLinkable, URL, boolean)
     *
     * @since 2.23
     */
    void setAutoRevertToSFTPUploads(boolean autoRevertToSFTP);

    /**
     * For files stored in a HTTP file store, set whether to download these
     * files directly from their HTTP server or whether to fetch them via the
     * API's {@code files/id/download} end point (i.e. the same as files in
     * the regular file store).
     * <p>
     * The default is to download directly from the HTTP store rather than
     * going through the API. This will save traffic and load on the LIMS server.
     * </p>
     *
     * @param downloadDirect true to fetch HTTP accessible files directly
     * from their HTTP URL, false to fetch them via the Clarity API.
     *
     * @since 2.23
     */
    void setDownloadDirectFromHttpStore(boolean downloadDirect);


    // Helper methods

    /**
     * Convert the given LIMS id to a full URI for that entity.
     *
     * <p>
     * This method is for the most common entities in the API where
     * the URIs are simply of the form {@code <endpoint>/<limsid>}.
     * </p>
     *
     * @param <E> The type of LIMS entity referred to.
     * @param limsid The LIMS id of the entity required.
     * @param entityClass The class of the entity.
     *
     * @return The full URI to the entity.
     *
     * @throws InvalidURIException if the creation of the URI gives a malformed URI.
     *
     * @throws IllegalArgumentException if either argument is null, or if
     * {@code entityClass} is annotated with a primary section attribute.
     *
     * @see #limsIdToUri(String, String, Class)
     */
    <E extends Locatable>
    URI limsIdToUri(String limsid, Class<E> entityClass);

    /**
     * Convert the given LIMS ids to a full URI for that entity.
     *
     * <p>
     * This method is for use on entities that have been annotated to
     * indicate they are part of a larger entity, for example
     * {@code ProtocolStep} within {@code Protocol} or {@code Stage}
     * within {@code Workflow}.
     * </p>
     *
     * @param <E> The type of LIMS entity referred to.
     * @param outerLimsid The LIMS id of the outer endpoint of the URI.
     * @param innerLimsid The LIMS id of the inner endpoint of the URI.
     * @param entityClass The class of the entity.
     *
     * @return The full URI to the entity.
     *
     * @throws InvalidURIException if the creation of the URI gives a malformed URI.
     *
     * @throws IllegalArgumentException if any argument is null, or if
     * {@code entityClass} is not annotated with a primary section attribute.
     *
     * @since 2.22
     */
    <E extends Locatable>
    URI limsIdToUri(String outerLimsid, String innerLimsid, Class<E> entityClass);

    /**
     * Forces the API to fetch stateful entities according to the rule given for
     * the next API call only.
     *
     * <p>
     * Calling this method only applies the change to the next call to the API on
     * the current thread. After the call, the API returns to normal behaviour
     * (fetching the entity with the state indicated by the URI). If this method
     * is invoked and entities that are not stateful are requested, the call here
     * has no effect. It is not remembered for the next stateful entity request,
     * so make sure the code calls this immediately before the call it is needed
     * for.
     * </p>
     *
     * <p>
     * In practice, this is useful only for the QC flags from artifacts. One would
     * either need the flag as specified by the URI exactly, or would want the
     * flag in its current state rather than the state given in the URI.
     * </p>
     *
     * @param override The behaviour to use in the next call. If null, it will
     * cancel a previously set override.
     *
     * @since 2.24.8
     */
    void overrideStateful(StatefulOverride override);

    // Retrieval methods

    /**
     * List all the objects of the given entity class.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param entityClass The type of entity to list.
     *
     * @return A list of links to the real entities in the LIMS.
     *
     * @throws IllegalArgumentException if {@code entityClass} is annotated to be
     * a part of another entity (its {@code primaryEntity} attribute is set).
     */
    <E extends Locatable> List<LimsLink<E>> listAll(Class<E> entityClass);

    /**
     * List an arbitrary number of objects of the given entity class.
     * This is not all that helpful for most real applications but can be very
     * handy for test cases.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param entityClass The type of entity to list.
     * @param startIndex The index of the first item to fetch. This start number
     * is zero-based.
     * @param number The number of items to fetch.
     *
     * @return A list of links to the real entities in the LIMS. This may be fewer
     * in number than {@code number} if there are too few items to fetch.
     *
     * @throws IllegalArgumentException if {@code entityClass} is annotated to be
     * a part of another entity (its {@code primaryEntity} attribute is set).
     */
    <E extends Locatable> List<LimsLink<E>> listSome(Class<E> entityClass, int startIndex, int number);

    /**
     * Search for entities of the given type based on name value pairs. The query
     * string is built up from the search terms. Where the term value is an array
     * or collection, the term is added multiple times which Clarity interprets as
     * an OR. For example, if the map contains:
     *
     * <pre>
     * "projectname" : [ "projectA", "projectB" ]
     * </pre>
     *
     * then the search will be for entities whose <i>projectname</i> attribute is
     * "{@code projectA}" or "{@code projectB}".
     *
     * @param <E> The type of LIMS entity referred to.
     * @param searchTerms The terms to use for the search.
     * @param entityClass The type of entity to list.
     *
     * @return A list of links to the real entities in the LIMS.
     *
     * @throws IllegalSearchTermException if any term in {@code searchTerms} is
     * found to be illegal. See {@link IllegalSearchTermException} for details of
     * what is illegal.
     *
     * @throws IllegalArgumentException if {@code entityClass} is annotated to be
     * a part of another entity (its {@code primaryEntity} attribute is set).
     */
    <E extends Locatable> List<LimsLink<E>> find(Map<String, ?> searchTerms, Class<E> entityClass);

    /**
     * Retrieve an entity using a String form of its URI.
     *
     * <p>
     * Note that when this method is used (as opposed to its equivalent taking a URI object)
     * Spring's REST template performs escaping on the URI string so that special characters
     * are sent to be literally that character. This is typically what is wanted for a first
     * call, but using this method to navigate from the server's response can cause problems
     * (especially with searches). Subsequent calls using links returned from the server
     * should use the variant of this method that accepts a URI object.
     * </p>
     *
     * @param <E> The type of LIMS entity referred to.
     * @param uri The URI to retrieve, in string form.
     * @param entityClass The type of entity to list.
     *
     * @return The LIMS entity.
     *
     * @throws InvalidURIException if {@code uri} is not a valid URI.
     *
     * @see #retrieve(URI, Class)
     */
    <E extends Locatable> E retrieve(String uri, Class<E> entityClass);

    /**
     * Retrieve an entity by its URI.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param uri The URI to retrieve.
     * @param entityClass The type of entity to list.
     *
     * @return The LIMS entity.
     *
     * @see #retrieve(URI, Class)
     */
    <E extends Locatable> E retrieve(URI uri, Class<E> entityClass);

    /**
     * Load an entity by its LIMS id. The URI of the object is created based on this
     * interface's currently set server address, the type of the object requested and
     * the LIMS id given.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param limsid The LIMS id of the entity.
     * @param entityClass The type of entity to list.
     *
     * @return The LIMS entity.
     */
    <E extends Locatable> E load(String limsid, Class<E> entityClass);

    /**
     * Load an entity by its two required LIMS ids. The URI of the object
     * is created based on this interface's currently set server address,
     * the type of the object requested and the LIMS ids given.
     *
     * <p>
     * This method is for use on entities that have been annotated to
     * indicate they are part of a larger entity, for example
     * {@code ProtocolStep} within {@code Protocol} or {@code Stage}
     * within {@code Workflow}.
     * </p>
     *
     * @param <E> The type of LIMS entity referred to.
     * @param outerLimsid The LIMS id of the outer endpoint of the URI.
     * @param innerLimsid The LIMS id of the inner endpoint of the URI.
     * @param entityClass The type of entity to list.
     *
     * @return The LIMS entity.
     *
     * @since 2.22
     */
    <E extends Locatable> E load(String outerLimsid, String innerLimsid, Class<E> entityClass);

    /**
     * Load an entity using a link to that entity. Typically, the link object will come
     * from a search or another object already fetched from the LIMS.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param link The link to the LIMS entity.
     *
     * @return The LIMS entity.
     */
    <E extends Locatable> E load(LimsLink<E> link);

    /**
     * Fetch a collection of LIMS entities given in the collection. The order of the
     * returned entities is the same as the iteration order of the {@code links}
     * collection.
     *
     * <p>
     * Using this method to fetch several entities at once can be beneficial where
     * the Clarity API allows batch fetching of objects. Rather than making a call
     * for each link, the links are collected into a single call whose response is all
     * the entities for the links. For those entities where this is not possible, a call
     * is made for each link. The end result is the same.
     * </p>
     *
     * @param <E> The type of LIMS entity referred to.
     * @param links The collection of links to the entities.
     *
     * @return A list of the LIMS entities.
     */
    <E extends Locatable> List<E> loadAll(Collection<? extends LimsLink<E>> links);

    /**
     * Reload an entity in-situ. The given object is refreshed from the server.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param entity The LimsEntity to refresh.
     */
    <E extends LimsEntity<E>> void reload(E entity);

    // General create methods.

    /**
     * Create an instance of the given entity on the server.
     *
     * <p>
     * When successful, the entity's fields are all updated based on the values returned
     * in the call's response, so the entity will accurately reflect the object on the server.
     * </p>
     *
     * <p>
     * When used with {@code Sample} objects, those objects must have an initial location
     * specified using their {@link Sample#setCreationLocation(com.genologics.ri.Location)}
     * method.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entity The entity object to create in the LIMS.
     */
    <E extends Locatable> void create(E entity);

    /**
     * Create a batch of entities on the server.
     *
     * <p>
     * When successful, the entities' fields are all updated based on the values returned
     * in the call's response, so each entity will accurately reflect the equivalent object
     * on the server.
     * </p>
     *
     * <p>
     * When used with {@code Sample} objects, those objects must have an initial location
     * specified using their {@link Sample#setCreationLocation(com.genologics.ri.Location)}
     * method.
     * </p>
     *
     * <p>
     * Using this method to create several entities at once can be beneficial where
     * the Clarity API allows batch creation of objects. Rather than making a call
     * for each object, the objects are collected into a single call whose response is
     * information on the entities now present in the server. For those entities where
     * this is not possible, a call is made for each object. The end result is the same.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entities The entity objects to create in the LIMS.
     */
    <E extends Locatable> void createAll(Collection<E> entities);

    // Update methods

    /**
     * Update the given entity on the server. The fields that can be updated differ between
     * entities: refer to the Clarity REST documentation for details.
     *
     * <p>
     * When successful, the entity's fields are all updated based on the values returned in
     * the call's response, so the entity will accurately reflect the object on the server.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entity The entity to update.
     */
    <E extends Locatable> void update(E entity);

    /**
     * Updates a batch of entities.
     *
     * <p>
     * When successful, the entities' fields are all updated based on the values returned
     * in the call's response, so each entity will accurately reflect the equivalent object
     * on the server.
     * </p>
     *
     * <p>
     * Using this method to update several entities at once can be beneficial where
     * the Clarity API allows batch update of objects. Rather than making a call
     * for each object, the objects are collected into a single call whose response is
     * information on the entities now present in the server. For those entities where
     * this is not possible, a call is made for each object. The end result is the same.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entities The entities to update.
     */
    <E extends Locatable> void updateAll(Collection<E> entities);

    // Delete methods

    /**
     * Deletes an entity from the server.
     *
     * <p>
     * When successful, the entity object should no longer be used.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entity The entity to delete.
     */
    <E extends Locatable> void delete(E entity);

    /**
     * Deletes a batch of entities from the server.
     *
     * <p>
     * When successful, the entity objects should no longer be used.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entities The entities to delete.
     */
    <E extends Locatable> void deleteAll(Collection<E> entities);


    // Process execution methods.

    /**
     * Execute a process within the LIMS. The given executable process object needs
     * to be set up carefully for this to work. Refer to the Clarity documentation.
     *
     * @param toExecute The executable process with all the information of what is
     * to run.
     *
     * @return The ClarityProcess object reflecting the process record in the LIMS.
     *
     * @see <a href="https://genologics.zendesk.com/hc/en-us/articles/213976863-Running-a-Process-Step">Genologics
     * guide to running a process through the API</a>
     */
    ClarityProcess executeProcess(ExecutableProcess toExecute);

    /**
     * Start a protocol step as if running through Clarity.
     *
     * @param stepCreation The step creation object. See Genologics' API documentation.
     *
     * @return The {@code ProcessStep} object for the step started.
     *
     * @throws IllegalArgumentException if {@code stepCreation} is null.
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.html#POST">Clarity API documentation</a>
     *
     * @see <a href="https://genologics.zendesk.com/entries/68573603-Starting-a-Protocol-Step-via-the-API">Starting
     * a Protocol Step via the API</a>
     */
    ProcessStep beginProcessStep(StepCreation stepCreation);

    /**
     * Move the process step to its next stage.
     *
     * <p>
     * Note that how the step advances is very dependent on the state it is currently in.
     * For example, one may have to set values on the {@link Actions} object and update
     * that before a step can advance past "next steps". Again, refer to the API documentation.
     * </p>
     *
     * @param step The step to advance. The state of this object is updated in place.
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.limsid.advance.html">Clarity API documentation</a>
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.limsid.actions.html">Actions documentation</a>
     *
     * @see <a href="https://genologics.zendesk.com/entries/69596247-Advancing-Completing-a-Protocol-Step-via-the-API">Advancing
     * and Completing a Step via the API</a>
     */
    void advanceProcessStep(ProcessStep step);

    /**
     * Start execution of an EPP as part of a process step moving through Clarity.
     *
     * @param program The program, as listed in the {@code ProcessStep} object.
     *
     * @return An updated program status structure.
     *
     * @see <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.steps.limsid.trigger.programid.html">Clarity API Documentation</a>
     *
     * @since 2.27.5
     */
    ProgramStatus startProgram(AvailableProgram program);

    /**
     * Get an updated program status from Clarity.
     *
     * @param status The program status to update. The object is updated in place
     * with the latest values.
     *
     * @since 2.27.5
     */
    void currentStatus(ProgramStatus status);


    // File upload methods.

    /**
     * Attach a file to an entity and upload that file to the file store.
     *
     * <p>
     * The client can be configured to use either the existing SFTP mechanism
     * for the upload or the HTTP upload end point added in Clarity 4.0. Care
     * should be taken with this choice, as the older mechanism is either safer
     * or more problematic, depending on one's viewpoint.
     * </p>
     *
     * <p>
     * Uploading over HTTP has the additional issue that adding a file to a
     * completed process fails. This is not a restriction of the SFTP upload.
     * </p>
     *
     * <p>
     * There is a maximum size that Clarity will accept for a file uploaded
     * over HTTP (see {@link #setHttpUploadSizeLimit(long)}). The client can
     * be configured to drop back to SFTP if this size is exceeded. Alternatively
     * it can be configured to never use the HTTP mechanism or to fail if the
     * file is too big.
     * </p>
     *
     * @param <E> The type of LIMS entity the file is being attached to.
     *
     * @param entity The LIMS entity to attach the file to.
     *
     * @param fileURL The URL of the file (relative to the local file system
     * where relevant). This forms the original location part of the file
     * record.
     *
     * @param publishInLablink Whether the file should be displayed in the
     * LabLink interface.
     *
     * @return The file record in the Clarity LIMS.
     *
     * @throws IOException if the file read or write fails.
     *
     * @see #setUploadOverHttp(boolean)
     * @see #setHttpUploadSizeLimit(long)
     * @see #setAutoRevertToSFTPUploads(boolean)
     */
    <E extends LimsEntity<E>>
    ClarityFile uploadFile(LimsEntityLinkable<E> entity, URL fileURL, boolean publishInLablink) throws IOException;

    /**
     * Download the given file from its source. Its content is written out
     * to {@code resultStream}.
     *
     * @param file The file to fetch.
     * @param resultStream The OutputStream to write the file content to.
     *
     * @throws IOException if there is a problem with the download.
     * @throws InvalidURIException if the download URI string internally created to fetch
     * the file isn't a valid URI (shouldn't happen).
     */
    void downloadFile(Linkable<ClarityFile> file, OutputStream resultStream) throws IOException;

    /**
     * Delete a file from the file store and remove its record from the LIMS. If the
     * file is not in the file store, only its record is removed.
     *
     * @param file The file to delete.
     *
     * @throws IOException if there is a problem removing the file from the
     * file store.
     */
    void deleteAndRemoveFile(Linkable<ClarityFile> file) throws IOException;


    // Routing artifacts through workflows.

    /**
     * Issue a routing request for artifacts into workflows.
     * {@code Routing} is a funny object that is post only and doesn't fit
     * with "create".
     *
     * @param routing The Routing object that has been assembled.
     */
    void routeArtifacts(Routing routing);


    // Retrieving artifacts from queues.

    /**
     * List the artifacts in the queue for the given protocol step.
     *
     * @param protocolStep The protocol step of the queue (or a link to it).
     *
     * @return Links to the artifacts currently in the given queue.
     *
     * @since 2.22
     */
    List<LimsEntityLink<Artifact>> listQueue(Linkable<ProtocolStep> protocolStep);

    /**
     * List the artifacts in the queue for the given protocol step, additionally
     * filtering using the parameters available for the "queues" end point.
     *
     * <p>
     * Search terms work the same as for the {@link #find(Map, Class)} method. Also see
     * <a href="https://d10e8rzir0haj8.cloudfront.net/6.0/rest.version.queues.protocolStepId.html">Genologics'
     * documentation of this end point</a> for the options available.
     * </p>
     *
     * @param protocolStep The protocol step of the queue (or a link to it).
     * @param searchTerms The terms to use for the search.
     *
     * @return Links to the artifacts currently in the given queue that meet the
     * criteria of the search terms.
     *
     * @since 2.24.1
     */
    List<LimsEntityLink<Artifact>> listQueue(Linkable<ProtocolStep> protocolStep, Map<String, ?> searchTerms);
}
