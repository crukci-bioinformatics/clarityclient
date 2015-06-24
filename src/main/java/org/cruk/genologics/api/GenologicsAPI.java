/*
 * CRUK-CI Genologics REST API Java Client.
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

package org.cruk.genologics.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.Credentials;
import org.springframework.web.client.HttpClientErrorException;

import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Linkable;
import com.genologics.ri.Locatable;
import com.genologics.ri.file.GenologicsFile;
import com.genologics.ri.process.GenologicsProcess;
import com.genologics.ri.processexecution.ExecutableProcess;
import com.genologics.ri.routing.Routing;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.StepCreation;


/**
 * Interface to the Genologics Clarity API version 2 via REST.
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
 *
 * <ul>
 * <li>
 * {@link GenologicsException} - thrown when the server sends back a response that
 * indicates an exception being raised on the server. The reply is automatically
 * unmarshalled and thrown as this equivalent Java exception.
 * </li>
 * <li>
 * {@link GenologicsUpdateException} - indicates more an error in client code, where
 * an update is attempted on an entity that does not allow such an update. This can
 * apply to create, update and delete methods. Refer to the Genologics REST documentation
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
 * </p>
 *
 * @see <a href="http://genologics.com/developer">Genologic's REST documentation</a>
 */
public interface GenologicsAPI
{
    // Configuration methods

    /**
     * Get the base URL of the Genologics server. This does not include the path to the API,
     * just the protocol, host and port.
     *
     * @return The server base URL.
     */
    URL getServer();

    /**
     * Set the base URL to the Genologics server.
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
     * <li>{@code api.server} - The base URL of the Genologics REST API (see {@link #setServer(URL)}).</li>
     * <li>{@code api.user} - The user to access the API as (see {@link #setCredentials(String, String)}).</li>
     * <li>{@code api.pass} - The password for {@code api.user}.</li>
     * <li>{@code filestore.server} - The host name for the file store (see {@link #setFilestoreServer(String)}).</li>
     * <li>{@code filestore.user} - The user to access the file store as (see {@link #setFilestoreCredentials(String, String)}).</li>
     * <li>{@code filestore.pass} - The password for {@code filestore.user}.</li>
     * </ul>
     *
     * <p>
     * The properties file does not have to contain all of these values. Any that are
     * missing are simply ignored.
     * </p>
     *
     * @param configuration The properties file to configure from.
     *
     * @throws MalformedURLException if the {@code api.server} property is set to
     * an invalid URL.
     */
    void setConfiguration(Properties configuration) throws MalformedURLException;

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
     * @param batchSize The number of objects to fetch or send per call to the API.
     * A maximum of 2000 objects per batch is imposed because performance noticeably
     * drops off long before this number of objects is used. If this argument is less
     * than or equal to zero, all object or links in the collection will be sent at
     * once up to the 2000 per call hard limit.
     *
     * @see #loadAll(Collection)
     * @see #createAll(Collection)
     * @see #updateAll(Collection)
     * @see #deleteAll(Collection)
     */
    void setBulkOperationBatchSize(int batchSize);


    // Helper methods

    /**
     * Convert the given LIMS id to a full URI for that entity.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param limsid The LIMS id of the entity required.
     * @param entityClass The class of the entity.
     *
     * @return The full URI to the entity.
     *
     * @throws URISyntaxException if the creation of the URI gives a malformed URI.
     */
    <E extends LimsEntity<E>> URI limsIdToUri(String limsid, Class<E> entityClass) throws URISyntaxException;

    // Retrieval methods

    /**
     * List all the objects of the given entity class.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param entityClass The type of entity to list.
     *
     * @return A list of links to the real entities in the LIMS.
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
     * the Genologics API allows batch fetching of objects. Rather than making a call
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
     * the Genologics API allows batch creation of objects. Rather than making a call
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
     * entities: refer to the Genologics REST documentation for details.
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
     * the Genologics API allows batch update of objects. Rather than making a call
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
     * to be set up carefully for this to work. Refer to the Genologics documentation.
     *
     * @param toExecute The executable process with all the information of what is
     * to run.
     *
     * @return The GenologicsProcess object reflecting the process record in the LIMS.
     *
     * @see <a href="http://developer.genologics.com/display/CBOOK1/Running+a+process">Genologics
     * guide to running a process through the API</a>
     */
    GenologicsProcess executeProcess(ExecutableProcess toExecute);

    /**
     * Start execution of process step.
     *
     * @param stepCreation The step creation object.
     *
     * @return The {@code ProcessStep} created by the call.
     */
    ProcessStep beginProcessStep(StepCreation stepCreation);

    // File upload methods.

    /**
     * Attach a file to an entity and upload that file to the file store.
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
     * @return The file record in the Genologics LIMS.
     *
     * @throws IOException if the file read or write fails.
     */
    <E extends LimsEntity<E>>
    GenologicsFile uploadFile(LimsEntityLinkable<E> entity, URL fileURL, boolean publishInLablink) throws IOException;

    /**
     * Download the given file from its source. Its content is written out
     * to {@code resultStream}.
     *
     * @param file The file to fetch.
     * @param resultStream The OutputStream to write the file content to.
     *
     * @throws IOException if there is a problem with the download.
     */
    void downloadFile(Linkable<GenologicsFile> file, OutputStream resultStream) throws IOException;

    /**
     * Delete a file from the file store and remove its record from the LIMS. If the
     * file is not in the file store, only its record is removed.
     *
     * @param file The file to delete.
     *
     * @throws IOException if there is a problem removing the file from the
     * file store.
     */
    void deleteAndRemoveFile(Linkable<GenologicsFile> file) throws IOException;

    // Routing artifacts through workflows.

    /**
     * Issue a routing request for artifacts into workflows.
     * {@code Routing} is a funny object that is post only and doesn't fit
     * with "create".
     *
     * @param routing The Routing object that has been assembled.
     */
    void routeArtifacts(Routing routing);
}
