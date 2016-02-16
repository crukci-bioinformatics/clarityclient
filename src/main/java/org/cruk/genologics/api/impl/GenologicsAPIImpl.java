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

package org.cruk.genologics.api.impl;

import static org.apache.commons.lang3.ClassUtils.getShortClassName;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.cruk.genologics.api.GenologicsAPI;
import org.cruk.genologics.api.GenologicsException;
import org.cruk.genologics.api.GenologicsUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.Batch;
import com.genologics.ri.BatchUpdate;
import com.genologics.ri.GenologicsBatchRetrieveResult;
import com.genologics.ri.GenologicsEntity;
import com.genologics.ri.GenologicsQueryResult;
import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsEntityLink;
import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Link;
import com.genologics.ri.Linkable;
import com.genologics.ri.Links;
import com.genologics.ri.Locatable;
import com.genologics.ri.Location;
import com.genologics.ri.PaginatedBatch;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.file.GenologicsFile;
import com.genologics.ri.process.GenologicsProcess;
import com.genologics.ri.processexecution.ExecutableProcess;
import com.genologics.ri.queue.Queue;
import com.genologics.ri.routing.Routing;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.StepCreation;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;


/**
 * Implementation of GenologicsAPI using the Clarity REST operations.
 * Uses the Spring REST template for the calls with a Spring Jaxb2Marshaller
 * object for XML/object transformations and a Commons HttpClient to do the
 * actual calls.
 *
 * @see RestTemplate
 * @see Jaxb2Marshaller
 * @see HttpClient
 */
public class GenologicsAPIImpl implements GenologicsAPI
{
    /**
     * The first part of the path for API calls.
     * This project is supporting version two of the Genologics API.
     */
    protected static final String API_PATH_BASE = "/api/v2/";

    /**
     * Combined mask for fields that need to be copied from updated objects to the
     * originals provided for PUT and POST operations.
     *
     * @see #reflectiveUpdate(Object, Object)
     */
    private static final int REFLECTIVE_UPDATE_MODIFIER_MASK = Modifier.TRANSIENT | Modifier.STATIC | Modifier.FINAL;

    /**
     * Hard limit on the maximum number of links or objects per batch
     * for the bulk operations.
     */
    private static final int BULK_OPERATION_BATCH_SIZE_HARD_LIMIT = 10000;

    /**
     * The protocols in URIs and URLs for HTTP (includes HTTPS).
     */
    private static final Set<String> HTTP_PROTOCOLS =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("http", "https")));

    /**
     * The protocol in URIs and URLs for SFTP.
     */
    private static final String SFTP_PROTOCOL = "sftp";


    /**
     * Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(GenologicsAPI.class);

    /**
     * The JAXB marshaller. Or rather, the Spring helper around the actual JAXB tools.
     */
    protected Jaxb2Marshaller jaxbMarshaller;

    /**
     * The Spring REST client.
     */
    protected RestOperations restClient;

    /**
     * The HTTP client.
     */
    protected HttpClient httpClient;

    /**
     * The request factory for direct communication with the HTTP client.
     */
    protected ClientHttpRequestFactory httpRequestFactory;

    /**
     * Adapted REST client for uploading files through the HTTP mechanism.
     */
    protected RestOperations fileUploadClient;

    /**
     * Session factory for JSch connections to the file store over SFTP.
     */
    protected DefaultSftpSessionFactory filestoreSessionFactory = new DefaultSftpSessionFactory();

    /**
     * Reflection access to {@code DefaultSftpSessionFactory}'s private {@code host} field.
     * Needed to return the host name for the file store.
     *
     * @see #getFilestoreServer()
     */
    private java.lang.reflect.Field filestoreSessionFactoryHostField;

    /**
     * The properties object passed in through construction or through setConfiguration
     * during Spring initialisation.
     */
    private Properties initialisingConfiguration;

    /**
     * Flag indicating that all Spring initialisation has been completed.
     */
    private boolean initialisationComplete;

    /**
     * The root URL to the Clarity server.
     */
    protected URL serverAddress;

    /**
     * The root path for all API calls to Clarity server.
     */
    protected String apiRoot;

    /**
     * User name and password credentials for accessing the Clarity API.
     */
    protected UsernamePasswordCredentials apiCredentials;

    /**
     * User name and password credentials for accessing the file store.
     */
    protected UsernamePasswordCredentials filestoreCredentials;

    /**
     * The number of objects that will be fetched, updated or created in each
     * call the Clarity API for the bulk operations (those that use a collection of
     * links or objects).
     *
     * @see GenologicsAPI#setBulkOperationBatchSize(int)
     */
    private int bulkOperationBatchSize = 500;

    /**
     * Whether files can be uploaded using HTTP to the {@code files/id/upload} API end point.
     *
     * @see GenologicsAPI#setUploadOverHttp(boolean)
     */
    protected boolean uploadOverHttp = false;

    /**
     * The maximum size of file that can be uploaded using HTTP.
     *
     * @see GenologicsAPI#setHttpUploadSizeLimit(long)
     */
    protected long httpUploadSizeLimit = 10485760L;

    /**
     * Whether the SFTP mechanism can be used as a fall back for uploading files if
     * a file exceeds the HTTP upload size limit.
     *
     * @see GenologicsAPI#setAutoRevertToSFTPUploads(boolean)
     */
    protected boolean autoRevertToSFTP = true;

    /**
     * Whether to download files that have an HTTP URL directly from their
     * store, or whether to download via the {@code files/id/download} API end point.
     *
     * @see GenologicsAPI#setDownloadDirectFromHttpStore(boolean)
     */
    protected boolean downloadDirectFromHttpStore = true;

    /**
     * Map of Locatable class to the class that provides the list of links
     * returned from listing or searching for objects of that type.
     *
     * <p>The key in the pair should be the entity class (e.g. {@code Artifact}
     * and the value should be a class that implements the {@link Batch}
     * interface and is annotated with the {@link GenologicsQueryResult}
     * annotation for that entity class (e.g. {@link com.genologics.ri.artifact.Artifacts}).
     * </p>
     */
    protected Map<Class<? extends Locatable>, Class<?>> entityToListClassMap;

    /**
     * Map of Locatable class to the class that provides the mass fetch or update
     * operation. Such bulk fetches and updates are only available for a small number
     * of classes.
     *
     * <p>The key in the pair should be the entity class (e.g. {@code Artifact}
     * and the value should be a class that implements the {@link BatchUpdate}
     * interface and is annotated with the {@link GenologicsBatchRetrieveResult}
     * annotation for that entity class (e.g. {@link com.genologics.ri.artifact.ArtifactBatchFetchResult}).
     * </p>
     */
    protected Map<Class<? extends Locatable>, Class<?>> entityToBatchRetrieveClassMap;

    /**
     * Cache of fields to update by reflective examination of an updated object
     * from the server. Maps the artifact class to a map of field names to
     * {@code java.lang.reflect.Field} objects.
     */
    protected Map<Class<?>, Map<String, java.lang.reflect.Field>> updaterFields =
            Collections.synchronizedMap(new HashMap<Class<?>, Map<String, java.lang.reflect.Field>>());


    /**
     * Standard constructor.
     */
    public GenologicsAPIImpl()
    {
        try
        {
            filestoreSessionFactoryHostField = DefaultSftpSessionFactory.class.getDeclaredField("host");
            filestoreSessionFactoryHostField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            throw new AssertionError("Apparently no 'host' field in DefaultSftpSessionFactory, but this field is known to exist.");
        }
    }

    /**
     * Initialise with an initial configuration from a properties file.
     *
     * @param configuration The properties file.
     *
     * @throws MalformedURLException if the server URL is set and the value cannot form
     * a valid URL.
     *
     * @see #setConfiguration(Properties)
     */
    public GenologicsAPIImpl(Properties configuration) throws MalformedURLException
    {
        this();
        setConfiguration(configuration);
    }

    /**
     * Set the file store session factory for SFTP connections.
     * This bean in optional if no additional configuration of the factory is required.
     *
     * @param filestoreSessionFactory The SFTP session factory.
     */
    public void setFilestoreSessionFactory(DefaultSftpSessionFactory filestoreSessionFactory)
    {
        this.filestoreSessionFactory = filestoreSessionFactory;
    }

    /**
     * Set the Jaxb marshaller.
     *
     * <p>This operation also immediately scans the classes managed by the marshaller
     * to find those supporting classes for retrieving lists of links to a given entity
     * and classes that allow batch fetch and update of entities.
     * </p>
     *
     * @param jaxbMarshaller The Jaxb marshaller.
     */
    @Required
    public void setJaxbMarshaller(Jaxb2Marshaller jaxbMarshaller)
    {
        this.jaxbMarshaller = jaxbMarshaller;

        entityToListClassMap = new HashMap<Class<? extends Locatable>, Class<?>>();
        entityToBatchRetrieveClassMap = new HashMap<Class<? extends Locatable>, Class<?>>();

        for (Class<?> possibleClass : jaxbMarshaller.getClassesToBeBound())
        {
            GenologicsQueryResult queryAnno = possibleClass.getAnnotation(GenologicsQueryResult.class);
            GenologicsBatchRetrieveResult batchAnno = possibleClass.getAnnotation(GenologicsBatchRetrieveResult.class);

            if (queryAnno != null)
            {
                Class<? extends Locatable> entityClass = queryAnno.entityClass().asSubclass(Locatable.class);

                @SuppressWarnings("rawtypes")
                Class<? extends Batch> listClass = possibleClass.asSubclass(Batch.class);

                entityToListClassMap.put(entityClass, listClass);

                if (logger.isDebugEnabled())
                {
                    logger.debug("Results class {} mapped as query results for {}",
                                 getShortClassName(listClass), getShortClassName(entityClass));
                }
            }

            if (batchAnno != null)
            {
                Class<? extends Locatable> entityClass = batchAnno.entityClass().asSubclass(Locatable.class);

                @SuppressWarnings("rawtypes")
                Class<? extends BatchUpdate> detailsClass = possibleClass.asSubclass(BatchUpdate.class);

                entityToBatchRetrieveClassMap.put(entityClass, detailsClass);

                if (logger.isDebugEnabled())
                {
                    logger.debug("Batch retrieve class {} mapped as entity holder for {}",
                                 getShortClassName(detailsClass), getShortClassName(entityClass));
                }
            }
        }
    }

    /**
     * Set the REST client.
     *
     * @param restClient The REST client.
     */
    @Required
    public void setRestClient(RestOperations restClient)
    {
        this.restClient = restClient;
    }

    /**
     * Set the REST client used for file uploads over HTTP.
     *
     * @param fileUploadClient The REST client configured for file upload.
     */
    @Required
    public void setFileUploadClient(RestOperations fileUploadClient)
    {
        this.fileUploadClient = fileUploadClient;
    }

    /**
     * Set the HTTP client. If the credentials are already known at this point,
     * those are set on the client.
     *
     * @param httpClient The HTTP client.
     */
    @Required
    public void setHttpClient(HttpClient httpClient)
    {
        this.httpClient = httpClient;
        if (apiCredentials != null)
        {
            setCredentials(apiCredentials);
        }
    }

    /**
     * Set the factory used for obtaining HTTP requests.
     *
     * @param httpRequestFactory The HTTP request factory.
     */
    @Required
    public void setHttpRequestFactory(ClientHttpRequestFactory httpRequestFactory)
    {
        this.httpRequestFactory = httpRequestFactory;
    }

    @Override
    public URL getServer()
    {
        return serverAddress;
    }

    @Override
    public void setServer(URL serverAddress)
    {
        if (serverAddress == null)
        {
            throw new IllegalArgumentException("serverAddress cannot be set to null");
        }

        String currentHost = this.serverAddress == null ? null : this.serverAddress.getHost();

        this.serverAddress = serverAddress;

        String addr = serverAddress.toExternalForm();
        addr = org.springframework.util.StringUtils.trimTrailingCharacter(addr, '/');

        apiRoot = addr + API_PATH_BASE;

        String filestoreHostAddress = getFilestoreServer();

        if (filestoreHostAddress == null || filestoreHostAddress.equals(currentHost))
        {
            setFilestoreServer(serverAddress.getHost());
        }
    }

    @Override
    public String getServerApiAddress()
    {
        return apiRoot;
    }

    @Override
    public String getUsername()
    {
        return apiCredentials.getUserName();
    }

    @Override
    public void setCredentials(String username, String password)
    {
        setCredentials(new UsernamePasswordCredentials(username, password));
    }

    @Override
    public void setCredentials(Credentials httpCredentials)
    {
        if (httpClient != null)
        {
            httpClient.getState().setCredentials(AuthScope.ANY, httpCredentials);
        }
        if (httpCredentials instanceof UsernamePasswordCredentials)
        {
            apiCredentials = (UsernamePasswordCredentials)httpCredentials;
        }
    }

    @Override
    public void setFilestoreServer(String host)
    {
        if (host == null)
        {
            throw new IllegalArgumentException("host cannot be null");
        }
        filestoreSessionFactory.setHost(host);
    }

    @Override
    public void setFilestoreCredentials(String username, String password)
    {
        if (username == null)
        {
            throw new IllegalArgumentException("username cannot be null");
        }

        filestoreCredentials = new UsernamePasswordCredentials(username, password);

        filestoreSessionFactory.setUser(username);
        filestoreSessionFactory.setPassword(password);
    }

    /**
     * Get the host currently set on the file store.
     *
     * @return The file store host (may be null).
     */
    protected String getFilestoreServer()
    {
        String filestoreHostAddress = null;
        try
        {
            filestoreHostAddress =
                    (String)filestoreSessionFactoryHostField.get(filestoreSessionFactory);
        }
        catch (Exception e)
        {
            // Ignore.
        }
        return filestoreHostAddress;
    }

    @Override
    public void setConfiguration(Properties configuration) throws MalformedURLException
    {
        if (configuration != null)
        {
            if (initialisationComplete)
            {
                String apiServer = configuration.getProperty("api.server");
                String apiUser = configuration.getProperty("api.user");
                String apiPass = configuration.getProperty("api.pass");
                String filestoreServer = configuration.getProperty("filestore.server");
                String filestoreUser = configuration.getProperty("filestore.user");
                String filestorePass = configuration.getProperty("filestore.pass");

                String batchSize = configuration.getProperty("batch.size");
                String httpUpload = configuration.getProperty("http.upload");
                String httpUploadLimit = configuration.getProperty("http.upload.maximum");
                String revertToSftp = configuration.getProperty("revert.to.sftp.upload");
                String httpDirect = configuration.getProperty("http.direct.download");

                if (StringUtils.isNotBlank(apiServer))
                {
                    setServer(new URL(apiServer));
                }
                if (StringUtils.isNotBlank(apiUser))
                {
                    setCredentials(apiUser, apiPass);
                }
                if (StringUtils.isNotBlank(filestoreServer))
                {
                    setFilestoreServer(filestoreServer);
                }
                if (StringUtils.isNotBlank(filestoreUser))
                {
                    setFilestoreCredentials(filestoreUser, filestorePass);
                }

                if (StringUtils.isNotBlank(batchSize))
                {
                    try
                    {
                        setBulkOperationBatchSize(Integer.parseInt(batchSize));
                    }
                    catch (NumberFormatException e)
                    {
                        logger.warn("Configuration property 'batch.size' is not a number.");
                    }
                }
                if (StringUtils.isNotBlank(httpUpload))
                {
                    setUploadOverHttp(Boolean.parseBoolean(httpUpload));
                }
                if (StringUtils.isNotBlank(httpUploadLimit))
                {
                    try
                    {
                        setHttpUploadSizeLimit(Long.parseLong(httpUploadLimit));
                    }
                    catch (NumberFormatException e)
                    {
                        logger.warn("Configuration property 'http.upload.maximum' is not a number.");
                    }
                }
                if (StringUtils.isNotBlank(revertToSftp))
                {
                    setAutoRevertToSFTPUploads(Boolean.parseBoolean(revertToSftp));
                }
                if (StringUtils.isNotBlank(httpDirect))
                {
                    setDownloadDirectFromHttpStore(Boolean.parseBoolean(httpDirect));
                }
            }
            else
            {
                initialisingConfiguration = configuration;
            }
        }
    }

    /**
     * Get the size of each batch of objects fetched, updated or created in
     * a bulk operation.
     *
     * @return The number of objects sent or retrieved in each batch.
     *
     * @see GenologicsAPI#setBulkOperationBatchSize(int)
     */
    public int getBulkOperationBatchSize()
    {
        return bulkOperationBatchSize;
    }

    @Override
    public void setBulkOperationBatchSize(int batchSize)
    {
        bulkOperationBatchSize =
                batchSize <= 0
                    ? BULK_OPERATION_BATCH_SIZE_HARD_LIMIT
                    : Math.min(batchSize, BULK_OPERATION_BATCH_SIZE_HARD_LIMIT);
    }

    @Override
    public void setUploadOverHttp(boolean uploadOverHttp)
    {
        this.uploadOverHttp = uploadOverHttp;
    }

    /**
     * Get the maximum size of file that can be uploaded using HTTP.
     *
     * @return The maximum size of file allowed over HTTP.
     *
     * @since 2.23
     *
     * @see GenologicsAPI#setHttpUploadSizeLimit(long)
     */
    public long getHttpUploadSizeLimit()
    {
        return httpUploadSizeLimit;
    }

    @Override
    public void setHttpUploadSizeLimit(long limit)
    {
        if (limit < 1)
        {
            throw new IllegalArgumentException("HTTP upload size limit must be positive.");
        }
        httpUploadSizeLimit = limit;
    }

    @Override
    public void setAutoRevertToSFTPUploads(boolean autoRevertToSFTP)
    {
        this.autoRevertToSFTP = autoRevertToSFTP;
    }

    @Override
    public void setDownloadDirectFromHttpStore(boolean downloadDirect)
    {
        this.downloadDirectFromHttpStore = downloadDirect;
    }

    /**
     * When this bean has finished Spring set up (all properties set), the
     * configuration that may have been supplied during start up needs to
     * be applied correctly to this bean and some objects it depends on.
     *
     * @throws MalformedURLException if the {@code api.server} property is set to
     * an invalid URL.
     */
    @PostConstruct
    public void afterInitialisation() throws MalformedURLException
    {
        initialisationComplete = true;
        setConfiguration(initialisingConfiguration);
        initialisingConfiguration = null;
    }

    // Internal consistency methods.

    /**
     * Get the class that holds a list of links for the given entity when returned
     * from a list or search operation.
     *
     * <p>For example, this is the {@link com.genologics.ri.artifact.Artifacts}
     * class for the {@link com.genologics.ri.artifact.Artifact} entity.
     * </p>
     *
     * @param entityClass The class of the entity.
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that holds the list of links to these entities.
     *
     * @return The list of links class for the given class of entity.
     *
     * @throws IllegalArgumentException if {@code entityClass} has no associated
     * list of links class.
     */
    protected <E extends Locatable, BH extends Batch<? extends LimsLink<E>>>
    Class<BH> getQueryResultsClassForEntity(Class<E> entityClass)
    {
        @SuppressWarnings("unchecked")
        Class<BH> listClass = (Class<BH>)entityToListClassMap.get(entityClass);
        if (listClass == null)
        {
            throw new IllegalArgumentException(entityClass.getName() + " is not returned by any known Batch class.");
        }
        return listClass;
    }

    /**
     * Get the class that allow mass retrieve or update operations for the
     * given entity class.
     *
     * <p>For example, this is the {@link com.genologics.ri.artifact.ArtifactBatchFetchResult}
     * class for the {@link com.genologics.ri.artifact.Artifact} entity.
     * </p>
     *
     * @param entityClass The class of the entity.
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that contains the list of entities from a bulk fetch.
     *
     * @return The bulk retrieval/update class for the given class of entity. Will return
     * {@code null} if the entity class has no mechanism for bulk operations.
     */
    protected <E extends Locatable, BH extends Batch<E>>
    Class<BH> getBatchRetrieveClassForEntity(Class<E> entityClass)
    {
        @SuppressWarnings("unchecked")
        Class<BH> listClass = (Class<BH>)entityToBatchRetrieveClassMap.get(entityClass);
        return listClass;
    }

    /**
     * Check that the class given as an entity class is annotated with the
     * {@code GenologicsEntity} annotation.
     *
     * @param entityClass The class to check.
     *
     * @return The GenologicsEntity annotation found.
     *
     * @throws IllegalArgumentException if {@code entityClass} is null or is not
     * annotated.
     */
    protected GenologicsEntity checkEntityAnnotated(Class<?> entityClass)
    {
        if (entityClass == null)
        {
            throw new IllegalArgumentException("entityClass cannot be null");
        }

        GenologicsEntity entityAnno = entityClass.getAnnotation(GenologicsEntity.class);
        if (entityAnno == null)
        {
            throw new IllegalArgumentException(
                    "The class " + entityClass.getName() + " has not been annotated with the GenologicsEntity annotation.");
        }
        return entityAnno;
    }

    /**
     * Check the server's URL has been set.
     *
     * @throws IllegalStateException if the URL has not been set.
     */
    protected void checkServerSet()
    {
        if (serverAddress == null)
        {
            throw new IllegalStateException("The server URL has not been set.");
        }
    }

    /**
     * Check the file store's host name and credentials have been set.
     *
     * @throws IllegalStateException if either the host name or the credentials
     * are not set.
     */
    protected void checkFilestoreSet()
    {
        if (getFilestoreServer() == null)
        {
            throw new IllegalStateException("File store server has not been set.");
        }
        if (filestoreCredentials == null || filestoreCredentials.getUserName() == null)
        {
            throw new IllegalStateException("File store credentials have not been set.");
        }
    }

    /**
     * Check that the given collection contains no nulls, that each item either has
     * a URI or does not have a URI (depending on {@code requireUri}, that all objects
     * in the collection are of the same class, and that, if {@code requireUri} is true,
     * there are no repeated URIs in the collection (ignoring state).
     *
     * @param <E> The type of entity in the collection.
     * @param entities The collection of entities to check.
     * @param requireUri Whether these objects are expected to already have a URI set
     * on them or not.
     *
     * @return The class of the entities in the collection. Will return null if the
     * {@code entities} collection is empty.
     *
     * @throws IllegalArgumentException if {@code entities} contains a null element;
     * if an object in @{code entities} has a URI when it should not or does not have
     * a URI when it should; if the classes of the objects in {@code entities} are not
     * all the same; if an entity appears more than once in the collection (if
     * {@code requireUri} is true).
     */
    protected <E extends Locatable>
    Class<E> checkCollectionHomogeneousAndUnique(Collection<E> entities, boolean requireUri)
    {
        Class<E> entityClass = null;
        Set<String> entityPaths = null;
        if (requireUri)
        {
            entityPaths = new HashSet<String>();
        }

        for (E entity : entities)
        {
            if (entity == null)
            {
                throw new IllegalArgumentException("entities contains a null");
            }
            if (!requireUri && entity.getUri() != null)
            {
                throw new IllegalArgumentException("entity has a URI set. This indicates it is already in the LIMS.");
            }
            if (requireUri && entity.getUri() == null)
            {
                throw new IllegalArgumentException("entity has no URI set. It may need to be created first.");
            }

            if (entityClass == null)
            {
                @SuppressWarnings("unchecked")
                Class<E> tempClass = (Class<E>)entity.getClass();
                entityClass = tempClass;
            }
            else
            {
                if (!entityClass.equals(entity.getClass()))
                {
                    throw new IllegalArgumentException("entities contains objects of different classes. The collection must be homogeneous.");
                }
            }

            if (requireUri)
            {
                String path = entity.getUri().getPath();
                if (entityPaths.contains(path))
                {
                    throw new IllegalArgumentException(path + " appears in the collection more than once.");
                }
                entityPaths.add(path);
            }
        }

        return entityClass;
    }

    /**
     * Check that the given collection of links contains no nulls, that each link has
     * a URI, that all links in the collection are links to the same entity class, and that
     * there are no repeated URIs in the collection (ignoring state).
     *
     * @param <E> The type of entity linked to.
     * @param <L> The type of link object.
     * @param links The collection of links to check.
     *
     * @return The class of the entities the links in the collection refer to. Will return
     * null if the {@code links} collection is empty.
     *
     * @throws IllegalArgumentException if {@code links} contains a null element;
     * if a link in @{code links} has no URI set; if the type of the entities indicated by
     * the links in {@code links} are not all the same; if an entity is referred to more than
     * once in {@code links}.
     */
    protected <E extends Locatable, L extends LimsLink<E>>
    Class<E> checkLinkCollectionHomogeneousAndUnique(Collection<L> links)
    {
        Class<E> entityClass = null;
        Set<String> entityPaths = new HashSet<String>();

        for (L link : links)
        {
            if (link == null)
            {
                throw new IllegalArgumentException("links contains a null");
            }
            if (link.getUri() == null)
            {
                throw new IllegalArgumentException("link has no URI set. It is required to retrieve links.");
            }

            if (entityClass == null)
            {
                entityClass = link.getEntityClass();
            }
            else
            {
                if (!entityClass.equals(link.getEntityClass()))
                {
                    throw new IllegalArgumentException("links contains links to entities of different classes. The collection must be homogeneous.");
                }
            }

            String path = link.getUri().getPath();
            if (entityPaths.contains(path))
            {
                throw new IllegalArgumentException(path + " appears in the collection more than once.");
            }
            entityPaths.add(path);
        }

        return entityClass;
    }

    // Public helper methods.

    @Override
    public <E extends Locatable>
    URI limsIdToUri(String limsid, Class<E> entityClass)
    throws URISyntaxException
    {
        return new URI(makeUri(limsid, entityClass, "limsIdToUri"));
    }

    /**
     * Convert the given LIMS id to a full URI for that entity
     * without creating a URI object.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param limsid The LIMS id of the entity required.
     * @param entityClass The class of the entity.
     * @param method The name of the calling method.
     *
     * @return The full URI to the entity as a string.
     *
     * @throws IllegalArgumentException if either argument is null, or if
     * {@code entityClass} is annotated with a primary section attribute.
     *
     * @see GenologicsAPI#limsIdToUri(String, Class)
     */
    protected <E extends Locatable>
    String makeUri(String limsid, Class<E> entityClass, String method)
    {
        if (StringUtils.isEmpty(limsid))
        {
            throw new IllegalArgumentException("limsid cannot be null or empty");
        }

        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        if (entityAnno.primaryEntity() != void.class)
        {
            throw new IllegalArgumentException(
                    entityClass.getName() + " has a double section endpoint in the API. " +
                    "Use " + method + "(String, String, Class) for this type.");
        }

        checkServerSet();

        String uri;
        if (entityAnno.processStepComponent())
        {
            uri = apiRoot + "steps/" + limsid + '/' + entityAnno.uriSection();
        }
        else
        {
            uri = apiRoot + entityAnno.uriSection() + '/' + limsid;
        }

        return uri;
    }

    @Override
    public <E extends Locatable>
    URI limsIdToUri(String outerLimsid, String innerLimsid, Class<E> entityClass)
    throws URISyntaxException
    {
        return new URI(makeUri(outerLimsid, innerLimsid, entityClass, "limsIdToUri"));
    }

    /**
     * Convert the given LIMS ids to a full URI for that entity
     * without creating a URI object.
     *
     * @param <E> The type of LIMS entity referred to.
     * @param outerLimsid The LIMS id of the outer endpoint of the URI.
     * @param innerLimsid The LIMS id of the inner endpoint of the URI.
     * @param entityClass The class of the entity.
     * @param method The name of the calling method.
     *
     * @return The full URI to the entity as a string.
     *
     * @throws IllegalArgumentException if any argument is null, or if
     * {@code entityClass} is not annotated with a primary section attribute.
     *
     * @see GenologicsAPI#limsIdToUri(String, String, Class)
     */
    protected <E extends Locatable>
    String makeUri(String outerLimsid, String innerLimsid, Class<E> entityClass, String method)
    {
        if (StringUtils.isEmpty(outerLimsid))
        {
            throw new IllegalArgumentException("outerLimsid cannot be null or empty");
        }
        if (StringUtils.isEmpty(innerLimsid))
        {
            throw new IllegalArgumentException("innerLimsid cannot be null or empty");
        }

        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        if (entityAnno.primaryEntity() == void.class)
        {
            throw new IllegalArgumentException(
                    entityClass.getName() + " has a single section endpoint in the API. " +
                    "Use " + method + "(String, Class) for this type.");
        }

        checkServerSet();

        GenologicsEntity primaryAnno = checkEntityAnnotated(entityAnno.primaryEntity());

        String uri = apiRoot + primaryAnno.uriSection() + '/' + outerLimsid +
                     '/' + entityAnno.uriSection() + '/' + innerLimsid;

        return uri;
    }

    // General fetch methods.

    @Override
    public <E extends Locatable>
    List<LimsLink<E>> listAll(Class<E> entityClass)
    {
        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        checkServerSet();

        String startUri = apiRoot + entityAnno.uriSection();

        return doList(startUri, entityClass, Integer.MAX_VALUE);
    }

    @Override
    public <E extends Locatable>
    List<LimsLink<E>> listSome(Class<E> entityClass, int startIndex, int number)
    {
        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        checkServerSet();

        String startUri = apiRoot + entityAnno.uriSection() + "?start-index=" + startIndex;

        return doList(startUri, entityClass, number);
    }

    @Override
    public <E extends Locatable>
    List<LimsLink<E>> find(Map<String, ?> searchTerms, Class<E> entityClass)
    {
        if (searchTerms == null)
        {
            searchTerms = Collections.emptyMap();
        }

        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        if (entityAnno.primaryEntity() != void.class)
        {
            String entityClassName = getShortClassName(entityClass);
            String primaryName = getShortClassName(entityAnno.primaryEntity());

            throw new IllegalArgumentException(
                    "Cannot search for " + entityClassName + "s as they are part of " + primaryName + ". " +
                    "A " + primaryName + " should supply a list of its relevant " + entityClassName + "s.");
        }

        checkServerSet();

        StringBuilder query = new StringBuilder(1024);

        for (Map.Entry<String, ?> term : searchTerms.entrySet())
        {
            Object value = term.getValue();
            if (value != null)
            {
                if (value.getClass().isArray())
                {
                    Object[] values = (Object[])value;

                    for (Object v : values)
                    {
                        appendQueryTerm(query, term.getKey(), v);
                    }
                }
                else if (value instanceof Iterable)
                {
                    Iterable<?> values = (Iterable<?>)value;

                    for (Object v : values)
                    {
                        appendQueryTerm(query, term.getKey(), v);
                    }
                }
                else
                {
                    appendQueryTerm(query, term.getKey(), value);
                }
            }
        }

        StringBuilder uri = new StringBuilder(1024);
        uri.append(apiRoot).append(entityAnno.uriSection());
        if (query.length() > 0)
        {
            uri.append('?').append(query);
        }

        return doList(uri.toString(), entityClass, Integer.MAX_VALUE);
    }

    /**
     * Helper method to {@code find}: builds up a query string with
     * joining ampersands and converts the value given into a string.
     *
     * @param query The StringBuilder which is building up the query.
     * @param argument The search parameter.
     * @param value The value to search for.
     *
     * @see #find(Map, Class)
     */
    private void appendQueryTerm(StringBuilder query, String argument, Object value)
    {
        if (value != null)
        {
            String strValue = ConvertUtils.convert(value);

            if (query.length() > 0)
            {
                query.append('&');
            }
            query.append(argument);
            query.append('=');
            query.append(strValue);
        }
    }

    /**
     * Perform a list operation for obtaining links to entities. These lists may
     * be a simple "list all in system" call or from a "find" operation.
     *
     * <p>
     * Deals with the pagination mechanism employed by the API to bring
     * back the required number of links regardless of the number of "pages"
     * the API returns them in.
     * </p>
     *
     * @param uri The URI to use for the list.
     * @param entityClass The type of entities required (or rather links to such entities).
     * @param number The maximum number of entities required. Calling code should
     * use {@code Integer.MAX_VALUE} to return all.
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that holds the list of links to these entities.
     *
     * @return A list of links to the entities found.
     */
    private <E extends Locatable, BH extends Batch<? extends LimsLink<E>>>
    List<LimsLink<E>> doList(String uri, Class<E> entityClass, int number)
    {
        Class<BH> batchClass = getQueryResultsClassForEntity(entityClass);

        return doList(uri, entityClass, batchClass, number);
    }

    /**
     * Perform a list operation for obtaining links to entities using a specific
     * batch fetch class. These lists may  be a simple "list all in system" call
     * or from a "find" operation.
     *
     * <p>
     * Deals with the pagination mechanism employed by the API to bring
     * back the required number of links regardless of the number of "pages"
     * the API returns them in.
     * </p>
     *
     * @param uri The URI to use for the list.
     * @param entityClass The type of entities required (or rather links to such entities).
     * @param batchClass The type of object to use for fetching the links.
     * @param number The maximum number of entities required. Calling code should
     * use {@code Integer.MAX_VALUE} to return all.
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the batch fetch object that holds the list of links
     * to these entities.
     *
     * @return A list of links to the entities found.
     *
     * @throws IllegalArgumentException if {@code entityClass} is annotated to be
     * a part of another entity (its {@code primaryEntity} attribute is set).
     */
    private <E extends Locatable, BH extends Batch<? extends LimsLink<E>>>
    List<LimsLink<E>> doList(String uri, Class<E> entityClass, Class<BH> batchClass, int number)
    {
        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        String entityClassName = getShortClassName(entityClass);

        if (entityAnno.primaryEntity() != void.class)
        {
            String primaryName = getShortClassName(entityAnno.primaryEntity());

            throw new IllegalArgumentException(
                    "Cannot list all " + entityClassName + "s as they are part of " + primaryName + ". " +
                    "A " + primaryName + " should supply a list of its relevant " + entityClassName + "s.");
        }

        ArrayList<LimsLink<E>> allLinks = new ArrayList<LimsLink<E>>(1024);

        // Note that it is important here to prevent the Spring escaping system
        // from encoding subsequent page URIs and turning, for example, plus signs
        // into %2B escaped characters. So for subsequent pages, take the URI
        // as given from the response.

        URI nextPageUri = null;
        do
        {
            ResponseEntity<BH> response;
            if (nextPageUri == null)
            {
                logger.debug("Fetching first batch of {} links from {}", entityClassName, uri);

                // First page
                response = restClient.getForEntity(uri, batchClass);
            }
            else
            {
                logger.debug("Fetching further batch of {} links from {}", entityClassName, nextPageUri);

                // Later batches.
                response = restClient.getForEntity(nextPageUri, batchClass);
            }

            BH batch = response.getBody();

            Iterator<? extends LimsLink<E>> iter = batch.iterator();
            int toAdd = Math.min(batch.getSize(), number - allLinks.size());

            allLinks.ensureCapacity(allLinks.size() + toAdd);

            for (; iter.hasNext() && toAdd > 0; toAdd--)
            {
                allLinks.add(iter.next());
            }

            nextPageUri = null;
            if (PaginatedBatch.class.isAssignableFrom(batchClass))
            {
                PaginatedBatch<?> paginatedBatch = (PaginatedBatch<?>)batch;
                if (paginatedBatch.getNextPage() != null)
                {
                    nextPageUri = paginatedBatch.getNextPage().getUri();
                }
            }
        }
        while (nextPageUri != null && allLinks.size() < number);

        allLinks.trimToSize();
        return allLinks;
    }

    @Override
    public <E extends Locatable>
    E retrieve(String uri, Class<E> entityClass)
    {
        if (StringUtils.isEmpty(uri))
        {
            throw new IllegalArgumentException("uri cannot be null or empty");
        }

        checkEntityAnnotated(entityClass);

        ResponseEntity<E> response = restClient.getForEntity(uri, entityClass);
        return response.getBody();
    }

    @Override
    public <E extends Locatable>
    E retrieve(URI uri, Class<E> entityClass)
    {
        if (uri == null)
        {
            throw new IllegalArgumentException("uri cannot be null");
        }

        checkEntityAnnotated(entityClass);

        ResponseEntity<E> response = restClient.getForEntity(uri, entityClass);
        return response.getBody();
    }

    @Override
    public <E extends Locatable>
    E load(String limsid, Class<E> entityClass)
    {
        return retrieve(makeUri(limsid, entityClass, "load"), entityClass);
    }

    @Override
    public <E extends Locatable>
    E load(String outerLimsid, String innerLimsid, Class<E> entityClass)
    {
        return retrieve(makeUri(outerLimsid, innerLimsid, entityClass, "load"), entityClass);
    }

    @Override
    public <E extends Locatable>
    E load(LimsLink<E> link)
    {
        if (link == null)
        {
            throw new IllegalArgumentException("link cannot be null");
        }
        return retrieve(link.getUri(), link.getEntityClass());
    }

    @Override
    public <E extends Locatable>
    List<E> loadAll(Collection<? extends LimsLink<E>> links)
    {
        List<E> entities;
        if (links == null || links.isEmpty())
        {
            entities = Collections.<E>emptyList();
        }
        else
        {
            Class<E> entityClass = checkLinkCollectionHomogeneousAndUnique(links);

            assert entityClass != null : "entityClass is null when collection is not empty";

            Class<Batch<E>> batchFetchResultClass = getBatchRetrieveClassForEntity(entityClass);

            entities = new ArrayList<E>(links.size());

            if (batchFetchResultClass != null && links.size() > 1)
            {
                // No step component has a batch fetch operation.

                GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);
                checkServerSet();
                String uri = apiRoot + entityAnno.uriSection() + "/batch/retrieve";

                final int batchCapacity = Math.min(bulkOperationBatchSize, links.size());
                List<LimsLink<E>> batch = new ArrayList<LimsLink<E>>(batchCapacity);

                Iterator<? extends LimsLink<E>> linkIter = links.iterator();

                while (linkIter.hasNext())
                {
                    batch.clear();

                    while (linkIter.hasNext() && batch.size() < batchCapacity)
                    {
                        batch.add(linkIter.next());
                    }

                    ResponseEntity<Batch<E>> response = restClient.postForEntity(uri, toLinks(batch), batchFetchResultClass);

                    entities.addAll(response.getBody().getList());
                }

                reorderBatchFetchList(links, entities);
            }
            else
            {
                for (LimsLink<E> limsLink : links)
                {
                    E entity = retrieve(limsLink.getUri(), limsLink.getEntityClass());
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    @Override
    public <E extends LimsEntity<E>>
    void reload(E entity)
    {
        if (entity != null)
        {
            if (entity.getUri() == null)
            {
                throw new IllegalArgumentException("entity has no URI set. It has not yet been created.");
            }

            @SuppressWarnings("unchecked")
            E newCopy = (E)retrieve(entity.getUri(), entity.getClass());

            reflectiveUpdate(entity, newCopy);
        }
    }


    // Create methods.

    @Override
    public <E extends Locatable>
    void create(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("entity cannot be null");
        }
        if (entity.getUri() != null)
        {
            throw new IllegalArgumentException("entity has a URI set. This indicates it is already in the LIMS.");
        }

        Class<?> entityClass = entity.getClass();
        GenologicsEntity entityAnno = checkEntityAnnotated(entity.getClass());

        if (!entityAnno.creatable())
        {
            throw new GenologicsUpdateException(getShortClassName(entityClass) + " cannot be created.");
        }

        checkServerSet();

        String uri;
        if (entityAnno.processStepComponent())
        {
            try
            {
                Link step = (Link)PropertyUtils.getProperty(entity, "step");
                if (step == null || step.getUri() == null)
                {
                    throw new IllegalArgumentException("entity does not have its Step URI set. This is needed to post a new " +
                                                       getShortClassName(entityClass) + ".");
                }
                uri = step.getUri().toString() + '/' + entityAnno.uriSection();
            }
            catch (NoSuchMethodException e)
            {
                throw new AssertionError(entityClass.getName() + " is tagged as a Process Step Component object, but has no getStep method.");
            }
            catch (IllegalAccessException e)
            {
                throw new AssertionError("Cannot call getStep() on " + entityClass.getName());
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException("Exception while fetching Step from " + getShortClassName(entityClass), e.getTargetException());
            }
        }
        else
        {
            uri = apiRoot + entityAnno.uriSection();
        }

        doCreateSingle(entity, uri);
    }

    /**
     * Helper for the creation methods when they create one entity with one call
     * to the API. Handles the case when the entity being created has an alternative
     * object to send for creation.
     *
     * <p>On success, the original entity is updated with the copy returned by the LIMS.</p>
     *
     * @param entity The entity to create.
     * @param uri The URI to post to to create the entity in the LIMS.
     */
    private <E extends Locatable>
    void doCreateSingle(E entity, String uri)
    {
        Class<?> entityClass = entity.getClass();
        GenologicsEntity entityAnno = checkEntityAnnotated(entity.getClass());

        assert entityAnno.creatable() : "Somehow got to doCreateSingle for a class that cannot be created.";

        assert entityAnno.primaryEntity() == void.class :
            entityClass.getName() + " has a primary entity set, but such things cannot be created through the API.";

        // See if the entity class has a creationClass attribute set. If so,
        // creation is done by creating those objects.

        ResponseEntity<?> response;
        Class<?> creationClass = entityAnno.creationClass();
        if (creationClass != void.class)
        {
            try
            {
                Constructor<?> constructor = creationClass.getConstructor(entityClass);
                Object creationObject = constructor.newInstance(entity);
                response = restClient.postForEntity(uri, creationObject, entity.getClass());
            }
            catch (NoSuchMethodException e)
            {
                throw new AssertionError(
                        "There is no constructor taking a " + getShortClassName(entityClass) +
                        " argument on " + creationClass.getName());
            }
            catch (IllegalAccessException e)
            {
                throw new AssertionError(
                        "The constructor taking a " + getShortClassName(entityClass) +
                        " argument on " + creationClass.getName() + " is not accessible.");
            }
            catch (InstantiationException e)
            {
                throw new AssertionError(creationClass.getName() + " is not a concrete class.");
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(
                        "Exception while creating a " + getShortClassName(creationClass) + " from " + getShortClassName(entityClass),
                        e.getTargetException());
            }
        }
        else
        {
            response = restClient.postForEntity(uri, entity, entity.getClass());
        }

        reflectiveUpdate(entity, response.getBody());
    }

    @Override
    public <E extends Locatable>
    void createAll(Collection<E> entities)
    {
        doCreateAll(entities);
    }

    /**
     * Create entities in the API for the entity objects given here.
     *
     * <p>
     * This method will use the batch create mechanism if there is a batch
     * retrieve class for the type of entity and that class is annotated
     * to say batch creates are allowed.
     * </p>
     *
     * @param entities The collection of entities to create.
     *
     * @throws IllegalArgumentException if {@code entities} contains a null value or
     * an entity that already exists in the API (i.e. has a URI).
     *
     * @throws GenologicsUpdateException if the entities cannot be created via the API
     * (as determined by the {@link GenologicsEntity#creatable()} flag).
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that holds the list of links to these entities.
     *
     * @see GenologicsEntity#creatable()
     * @see GenologicsBatchRetrieveResult#batchCreate()
     */
    private <E extends Locatable, BH extends BatchUpdate<E>>
    void doCreateAll(Collection<E> entities)
    {
        if (entities != null && !entities.isEmpty())
        {
            Class<E> entityClass = checkCollectionHomogeneousAndUnique(entities, false);

            GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

            if (!entityAnno.creatable())
            {
                throw new GenologicsUpdateException(getShortClassName(entityClass) + " cannot be created.");
            }

            assert entityAnno.primaryEntity() == void.class :
                entityClass.getName() + " has a primary entity set, but such things cannot be created through the API.";

            Class<BH> batchRetrieveClass = getBatchRetrieveClassForEntity(entityClass);

            checkServerSet();

            boolean doBatchCreates = false;

            if (batchRetrieveClass != null)
            {
                GenologicsBatchRetrieveResult batchAnnotation =
                        batchRetrieveClass.getAnnotation(GenologicsBatchRetrieveResult.class);
                assert batchAnnotation != null : "No GenologicsBatchRetrieveResult annotation on result class " + entityClass.getName();

                doBatchCreates = batchAnnotation.batchCreate() && entities.size() > 1;
            }

            if (doBatchCreates)
            {
                assert !entityAnno.processStepComponent() : "Have bulk create for process step component. This is not supported.";

                try
                {
                    List<E> createdEntities = new ArrayList<E>(entities.size());
                    Links createdLinks = new Links(entities.size());

                    final int batchCapacity = Math.min(bulkOperationBatchSize, entities.size());
                    List<E> batch = new ArrayList<E>(batchCapacity);

                    Iterator<E> entityIter = entities.iterator();

                    while (entityIter.hasNext())
                    {
                        batch.clear();

                        while (entityIter.hasNext() && batch.size() < batchCapacity)
                        {
                            batch.add(entityIter.next());
                        }

                        BH details = batchRetrieveClass.newInstance();

                        details.addForCreate(batch);

                        String url = apiRoot + entityAnno.uriSection() + "/batch/create";

                        ResponseEntity<Links> createReply =
                                restClient.exchange(url, HttpMethod.POST, new HttpEntity<BH>(details), Links.class);
                        Links replyLinks = createReply.getBody();

                        assert replyLinks.getSize() == batch.size() :
                            "Have " + replyLinks.getSize() + " links returned for " + batch.size() + " submitted entities.";

                        // Need to record the links as they are returned from the create call
                        // in the order they are returned (see below).

                        createdLinks.addAll(replyLinks);

                        // Fetch the new objects to make sure all the properties are correct.

                        url = apiRoot + entityAnno.uriSection() + "/batch/retrieve";

                        ResponseEntity<BH> reloadReply =
                                restClient.exchange(url, HttpMethod.POST, new HttpEntity<Links>(replyLinks), batchRetrieveClass);

                        createdEntities.addAll(reloadReply.getBody().getList());
                    }

                    if (Sample.class.equals(entityClass))
                    {
                        // Special case for samples because we've found in tests that these do
                        // not have their URIs returned in the correct order from the batch
                        // create call. We can though use their location to match up the originals
                        // to the new copies.

                        updateFromNewSamples(entities, createdEntities);
                    }
                    else
                    {
                        // The fetch of the entities using the Links may not bring them back in
                        // the order originally requested, so sort based on the order defined in
                        // the createdLinks object (now a collection of the links from all the
                        // batch call replies).

                        reorderBatchFetchList(createdLinks.getLinks(), createdEntities);

                        // We must assume that the order of the URIs returned in the Links object
                        // received after the creation POST is the same order as the original
                        // objects were submitted. The entities returned after the load of those
                        // objects may not be. So use the order of the Links URIs to update the
                        // original entities.
                        // This seems to hold true for containers, where every test (so far) sees
                        // them coming back in the right order.

                        reflectiveCollectionUpdate(entities, createdEntities);
                    }
                }
                catch (IllegalAccessException e)
                {
                    logger.error("Cannot access the default constructor on {}", batchRetrieveClass.getName());
                }
                catch (InstantiationException e)
                {
                    logger.error("Cannot create a new {}: {}", batchRetrieveClass.getName(), e.getMessage());
                }
            }
            else
            {
                String uri = apiRoot + entityAnno.uriSection();

                for (E entity : entities)
                {
                    doCreateSingle(entity, uri);
                }
            }
        }
    }

    /**
     * Special case for updating the original {@code Sample} objects submitted
     * for a bulk create with the newly created server side versions.
     *
     * <p>
     * The order of the new objects is not going to be the same as those originally
     * provided, so to make sure we update the right objects we need to match
     * by location (container and well position) which should be unique.
     * So, we can use the location to match the original Sample objects with those
     * returned from the server after creation. This does involve fetching their
     * initial Artifact objects, which hold the location, then matching by location
     * to the original's {@code creationLocation}.
     * </p>
     *
     * <p>
     * This method performs the update of the original objects from those now
     * existing in the server.
     * </p>
     *
     * @param <E> The type of the entity (in this case, expected to be Sample).
     *
     * @param entities The original Sample objects the user has submitted for creation.
     * @param created The list of Sample objects in the LIMS that were created from
     * {@code entities}.
     */
    private <E extends Locatable>
    void updateFromNewSamples(Collection<E> entities, List<E> created)
    {
        @SuppressWarnings("unchecked")
        Collection<Sample> userSamples = (Collection<Sample>)entities;

        @SuppressWarnings("unchecked")
        List<Sample> newSamples = (List<Sample>)created;

        // Assemble the new sample's artifacts links into a Links object for batch fetch.

        Links artifactLinks = new Links();

        for (Sample s : newSamples)
        {
            assert s.getArtifact() != null : "No artifact set on new Sample.";

            artifactLinks.add(s.getArtifact());
        }

        // Batch fetch the samples' artifacts.

        GenologicsEntity artifactEntityAnno = checkEntityAnnotated(Artifact.class);

        String url = apiRoot + artifactEntityAnno.uriSection() + "/batch/retrieve";

        Class<Batch<Artifact>> artifactFetchClass = getBatchRetrieveClassForEntity(Artifact.class);

        assert artifactFetchClass != null : "No batch fetch class returned for Artifact";

        ResponseEntity<Batch<Artifact>> response =
                restClient.exchange(url, HttpMethod.POST, new HttpEntity<Links>(artifactLinks), artifactFetchClass);

        List<Artifact> sampleArtifacts = response.getBody().getList();

        assert newSamples.size() == sampleArtifacts.size() : "Size of sample and artifact lists differ";

        // Order the returned artifacts into the same order as the original request,
        // so they match their equivalent sample in newSamples.

        reorderBatchFetchList(artifactLinks.getLinks(), sampleArtifacts);

        // Put the new sample objects into a map based on their location.

        Map<Location, Sample> newSamplesByLocation = new HashMap<Location, Sample>();
        Iterator<Sample> siter = newSamples.iterator();
        Iterator<Artifact> aiter = sampleArtifacts.iterator();

        while (siter.hasNext())
        {
            Sample s = siter.next();
            Artifact a = aiter.next();

            Sample clash = newSamplesByLocation.put(a.getLocation(), s);
            if (clash != null)
            {
                // Should never happen - locations must be unoccupied, so two samples
                // cannot be put into the same location.
                throw new AssertionError("Already have a sample in " + a.getLocation());
            }
        }
        assert !aiter.hasNext();

        // Loop through the original samples and find their replacement in the new
        // set based on their location and update those original objects from the new ones.

        for (Sample original : userSamples)
        {
            assert original.getCreationLocation() != null : "The original sample has lost its creation location.";

            Sample newSample = newSamplesByLocation.get(original.getCreationLocation());
            if (newSample == null)
            {
                throw new AssertionError("Don't have any sample from the server in " + original.getCreationLocation());
            }

            reflectiveUpdate(original, newSample);
        }
    }


    // Update methods.

    @Override
    public <E extends Locatable>
    void update(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("entity cannot be null");
        }
        if (entity.getUri() == null)
        {
            throw new IllegalArgumentException("entity has no URI set. It may need to be created first.");
        }

        Class<? extends Locatable> entityClass = entity.getClass();
        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        if (!entityAnno.updateable())
        {
            throw new GenologicsUpdateException(getShortClassName(entityClass) + " cannot be updated.");
        }

        assert entityAnno.primaryEntity() == void.class :
            entityClass.getName() + " has a primary entity set, but such things cannot be created through the API.";

        ResponseEntity<? extends Locatable> response =
                restClient.exchange(entity.getUri(), HttpMethod.PUT, new HttpEntity<Locatable>(entity), entity.getClass());

        reflectiveUpdate(entity, response.getBody());
    }

    @Override
    public <E extends Locatable>
    void updateAll(Collection<E> entities)
    {
        doUpdateAll(entities);
    }

    /**
     * Update entities in Clarity with the entity objects given here. The objects are
     * updated in-situ, so any changes made in the server will be pushed into the
     * objects in the collection.
     *
     * <p>Uses the bulk create mechanism where it is available for the entity.</p>
     *
     * @param entities The collection of entities to update.
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that is sent to perform the bulk update.
     *
     * @throws IllegalArgumentException if {@code entities} contains a null value or
     * an entity that already exists in the API (i.e. has a URI).
     *
     * @throws GenologicsUpdateException if the entities cannot be updated via the API
     * (as determined by the {@link GenologicsEntity#updateable()} flag).
     */
    private <E extends Locatable, BH extends BatchUpdate<E>>
    void doUpdateAll(Collection<E> entities)
    {
        if (entities != null && !entities.isEmpty())
        {
            Class<E> entityClass = checkCollectionHomogeneousAndUnique(entities, true);

            GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

            if (!entityAnno.updateable())
            {
                throw new GenologicsUpdateException(getShortClassName(entityClass) + " cannot be updated.");
            }

            assert entityAnno.primaryEntity() == void.class :
                entityClass.getName() + " has a primary entity set, but such things cannot be updated through the API.";

            Class<BH> batchUpdateClass = getBatchRetrieveClassForEntity(entityClass);

            boolean doBatchUpdates = false;

            if (batchUpdateClass != null)
            {
                GenologicsBatchRetrieveResult batchAnnotation =
                        batchUpdateClass.getAnnotation(GenologicsBatchRetrieveResult.class);
                assert batchAnnotation != null : "No GenologicsBatchRetrieveResult annotation on result class " + entityClass.getName();

                doBatchUpdates = batchAnnotation.batchUpdate() && entities.size() > 1;
            }

            checkServerSet();

            if (doBatchUpdates)
            {
                assert !entityAnno.processStepComponent() : "Have bulk update for process step component. This is not supported.";

                try
                {
                    List<E> updatedEntities = new ArrayList<E>(entities.size());

                    final int batchCapacity = Math.min(bulkOperationBatchSize, entities.size());
                    List<E> batch = new ArrayList<E>(batchCapacity);

                    Iterator<E> entityIter = entities.iterator();

                    while (entityIter.hasNext())
                    {
                        batch.clear();

                        while (entityIter.hasNext() && batch.size() < batchCapacity)
                        {
                            batch.add(entityIter.next());
                        }

                        BH details = batchUpdateClass.newInstance();
                        details.addForUpdate(batch);

                        String url = apiRoot + entityAnno.uriSection() + "/batch/update";

                        ResponseEntity<Links> updateReply =
                                restClient.exchange(url, HttpMethod.POST, new HttpEntity<BH>(details), Links.class);
                        Links replyLinks = updateReply.getBody();

                        assert replyLinks.getSize() == batch.size() :
                            "Have " + replyLinks.getSize() + " links returned for " + batch.size() + " submitted entities.";

                        // Fetch the updated objects to make sure all the properties are correct.
                        // Some may be disallowed or just not updated in the LIMS.

                        url = apiRoot + entityAnno.uriSection() + "/batch/retrieve";

                        ResponseEntity<BH> reloadReply =
                                restClient.exchange(url, HttpMethod.POST, new HttpEntity<Links>(replyLinks), batchUpdateClass);

                        updatedEntities.addAll(reloadReply.getBody().getList());
                    }

                    // The fetch of the entities using the Links object may not bring them back in
                    // the order originally requested, so sort based on the order of the original
                    // entities. As these entities already existed, they will all have URIs to
                    // compare. We can then update the originals.

                    reorderBatchFetchList(entities, updatedEntities);

                    reflectiveCollectionUpdate(entities, updatedEntities);
                }
                catch (IllegalAccessException e)
                {
                    logger.error("Cannot access the default constructor on {}", batchUpdateClass.getName());
                }
                catch (InstantiationException e)
                {
                    logger.error("Cannot create a new {}: {}", batchUpdateClass.getName(), e.getMessage());
                }
            }
            else
            {
                for (E entity : entities)
                {
                    if (entity != null)
                    {
                        ResponseEntity<? extends Locatable> response =
                                restClient.exchange(entity.getUri(), HttpMethod.PUT, new HttpEntity<E>(entity), entity.getClass());

                        reflectiveUpdate(entity, response.getBody());
                    }
                }
            }
        }
    }

    @Override
    public <E extends Locatable>
    void delete(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("entity cannot be null");
        }
        if (entity.getUri() == null)
        {
            throw new IllegalArgumentException("entity has no URI set.");
        }

        doDelete(entity.getUri(), entity.getClass());
    }

    @Override
    public <E extends Locatable>
    void deleteAll(Collection<E> entities)
    {
        if (entities != null && !entities.isEmpty())
        {
            checkCollectionHomogeneousAndUnique(entities, true);

            // There is no batch delete.
            for (E entity : entities)
            {
                assert entity != null : "Have null entity after check";
                doDelete(entity.getUri(), entity.getClass());
            }
        }
    }

    /**
     * Remove an entity from Clarity.
     *
     * @param uri The URI of the entity to delete.
     * @param entityClass The type of entity to delete.
     *
     * @param <E> The type of the entity.
     *
     * @throws GenologicsUpdateException if the entities cannot be deleted via the API
     * (as determined by the {@link GenologicsEntity#removable()} flag).
     */
    private <E extends Locatable>
    void doDelete(URI uri, Class<E> entityClass)
    {
        GenologicsEntity entityAnno = checkEntityAnnotated(entityClass);

        if (!entityAnno.removable())
        {
            throw new GenologicsUpdateException(getShortClassName(entityClass) + " cannot be deleted.");
        }

        assert entityAnno.primaryEntity() == void.class :
            entityClass.getName() + " has a primary entity set, but such things cannot be deleted through the API.";

        restClient.delete(uri);
    }


    // Process execution

    @Override
    public GenologicsProcess executeProcess(ExecutableProcess toExecute)
    {
        if (toExecute == null)
        {
            throw new IllegalArgumentException("toExecute cannot be null");
        }

        checkServerSet();

        String uri = apiRoot + "processes";

        ResponseEntity<GenologicsProcess> response = restClient.postForEntity(uri, toExecute, GenologicsProcess.class);
        return response.getBody();
    }

    @Override
    public ProcessStep beginProcessStep(StepCreation stepCreation)
    {
        if (stepCreation == null)
        {
            throw new IllegalArgumentException("stepCreation cannot be null");
        }

        checkServerSet();

        String url = apiRoot + "steps";

        ResponseEntity<ProcessStep> response = restClient.postForEntity(url, stepCreation, ProcessStep.class);
        return response.getBody();
    }


    // File upload

    @Override
    public <E extends LimsEntity<E>>
    GenologicsFile uploadFile(LimsEntityLinkable<E> entity, URL fileURL, boolean publishInLablink)
    throws IOException
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("entity cannot be null");
        }
        if (fileURL == null)
        {
            throw new IllegalArgumentException("fileURL cannot be null");
        }

        checkServerSet();

        GenologicsFile storageRequest;

        URLInputStreamResource fileURLResource = new URLInputStreamResource(fileURL);
        try
        {
            long length = fileURLResource.contentLength();

            if (length < 0)
            {
                logger.warn("Cannot determine size of file from the " + fileURL.getProtocol() + " protocol.");
            }

            // Post a request to the "glsstorage" to create a new GenologicsFile object to
            // hold the uploaded file. Requirements are for the holding entity and the
            // original location to be set.
            // See http://www.genologics.com/files/permanent/API/latest/rest.version.glsstorage.html

            storageRequest = new GenologicsFile();
            storageRequest.setAttachedTo(entity);
            storageRequest.setOriginalLocation(fileURL.toExternalForm());

            String storageUri = apiRoot + "glsstorage";

            ResponseEntity<GenologicsFile> response = restClient.postForEntity(storageUri, storageRequest, GenologicsFile.class);

            storageRequest = response.getBody();

            // May as well set the "Publish to LabLink" flag on the file now.
            // By either upload mechanism, this object will be posted back at some point.
            // (Setting it initially is ignored.)

            storageRequest.setPublished(publishInLablink);

            // See which protocol the resulting file gives. If it is "sftp", we can upload.
            // Anything else cannot allow an upload (haven't seen anything else so far).

            URL targetURL = new URL(null, storageRequest.getContentLocation().toString(), NullURLStreamHandler.INSTANCE);

            if (!SFTP_PROTOCOL.equals(targetURL.getProtocol()))
            {
                throw new GenologicsUpdateException("File upload to the file store for links giving the " +
                        targetURL.getProtocol().toUpperCase() + " protocol is not supported.");
            }

            if (uploadOverHttp)
            {
                if (length >= 0 && length <= httpUploadSizeLimit)
                {
                    // Have a length and it's within the set limit. Use HTTP.

                    uploadViaHTTP(fileURLResource, storageRequest);
                }
                else if (autoRevertToSFTP)
                {
                    // Could not get the length, or the file is too big. Allowed to
                    // revert to SFTP, so use that.

                    if (length < 0)
                    {
                        logger.info("Size of {} cannot be determined, so reverting to SFTP.", fileURL);
                    }
                    else
                    {
                        logger.info("Upload of {} is too large to be uploaded through the HTTP mechanism. Reverting to SFTP.", fileURL);
                    }

                    uploadViaSFTP(fileURLResource, storageRequest);
                }
                else
                {
                    // Could not get the length, or the file is too big. Not allowed to
                    // revert to SFTP, so fail.

                    if (length < 0)
                    {
                        throw new GenologicsUpdateException("Cannot upload " + fileURL +
                                " - cannot determine its size, so it may exceed the maximum HTTP upload size of " +
                                httpUploadSizeLimit);
                    }
                    else
                    {
                        throw new GenologicsUpdateException("Cannot upload " + fileURL +
                                " - the content exceeds the maximum HTTP upload size of " + httpUploadSizeLimit);
                    }
                }
            }
            else
            {
                // Not using HTTP upload at all, so straight to SFTP.

                uploadViaSFTP(fileURLResource, storageRequest);
            }
        }
        finally
        {
            fileURLResource.close();
        }

        try
        {
            PropertyUtils.setProperty(entity, "file", storageRequest);
        }
        catch (Exception e)
        {
            // Quietly leave the file property of the entity as it was,
            // or ignore if there was no file property.
        }

        return storageRequest;
    }

    /**
     * Upload a file to the Genologics file store. This always uses the HTTP
     * protocol with the {@code file/id/upload} end point.
     *
     * @param fileURLResource The URL resource of the file on the local machine.
     * @param targetFile The GenologicsFile object that holds the reference to the
     * uploaded file, which was newly created using the API.
     *
     * @throws GenologicsException if the server reports a problem with the upload.
     * @throws IllegalStateException if {@code targetFile} does not have a LIMS id.
     * @throws IOException if there is a problem with the transfer.
     */
    protected void uploadViaHTTP(URLInputStreamResource fileURLResource, GenologicsFile targetFile)
    throws IOException
    {
        GenologicsEntity entityAnno = checkEntityAnnotated(GenologicsFile.class);

        if (targetFile.getLimsid() == null)
        {
            // Need to post the file back to the LIMS to obtain a URI and LIMS
            // id for the file object.

            String filesUrl = getServerApiAddress() + entityAnno.uriSection();

            ResponseEntity<GenologicsFile> response = restClient.postForEntity(filesUrl, targetFile, GenologicsFile.class);

            reflectiveUpdate(targetFile, response.getBody());

            assert targetFile.getLimsid() != null : "Still no LIMS id on GenologicsFile object.";
        }

        boolean uploadedOk = false;
        try
        {
            URI uploadURI;
            try
            {
                uploadURI = new URI(getServerApiAddress() + entityAnno.uriSection() + "/" + targetFile.getLimsid() + "/upload");
            }
            catch (URISyntaxException e)
            {
                throw new IOException("File LIMS id " + targetFile.getLimsid() + " produces an invalid URI for upload.", e);
            }

            logger.info("Uploading {} over {} to {} on {}",
                        fileURLResource.getURL().getPath(),
                        uploadURI.getScheme().toUpperCase(),
                        targetFile.getContentLocation().getPath(),
                        targetFile.getContentLocation().getHost());

            HttpEntity<MultiValueMap<String, Resource>> requestEntity =
                    new HttpEntity<MultiValueMap<String, Resource>>(new LinkedMultiValueMap<String, Resource>(1));

            requestEntity.getBody().add("file", fileURLResource);

            ResponseEntity<String> uploadEntity =
                    fileUploadClient.exchange(uploadURI, HttpMethod.POST, requestEntity, String.class);

            uploadedOk = true;

            if (logger.isDebugEnabled())
            {
                if (uploadEntity.hasBody())
                {
                    logger.debug("Upload of file returned a {}: {}",
                                 ClassUtils.getShortClassName(uploadEntity.getBody().getClass()),
                                 uploadEntity.getBody());
                }
                else
                {
                    logger.debug("Upload of file succeeded but returned nothing.");
                }
            }
        }
        finally
        {
            if (!uploadedOk)
            {
                try
                {
                    delete(targetFile);
                }
                catch (Exception e)
                {
                    logger.warn("Failed to clean up GenologicsFile object {} after upload failure:", targetFile.getLimsid(), e);
                }
            }
        }

        if (!uploadedOk)
        {
            // I don't think the code can get here as other exceptions should
            // have been thrown. To make sure though...

            throw new GenologicsUpdateException("Failed to upload " + fileURLResource.getURL());
        }
    }

    /**
     * Upload a file to the Genologics file store. This always uses the SFTP protocol.
     *
     * @param fileURLResource The URL resource of the file on the local machine.
     * @param targetFile The GenologicsFile object that holds the reference to the
     * uploaded file, which was newly created using the API.
     *
     * @throws IOException if there is a problem with the transfer.
     * @throws IllegalStateException if the file store host name or credentials
     * are not set.
     */
    protected void uploadViaSFTP(URLInputStreamResource fileURLResource, GenologicsFile targetFile)
    throws IOException
    {
        GenologicsEntity entityAnno = checkEntityAnnotated(GenologicsFile.class);

        checkFilestoreSet();

        Session<LsEntry> session = filestoreSessionFactory.getSession();
        try
        {
            URI targetURL = targetFile.getContentLocation();

            logger.info("Uploading {} over SFTP to {} on {}",
                        fileURLResource.getURL().getPath(),
                        targetURL.getPath(),
                        targetURL.getHost());

            String directory = FilenameUtils.getFullPathNoEndSeparator(targetURL.getPath());

            if (!session.exists(directory))
            {
                String[] directoryParts = directory.split("/+");

                StringBuilder incrementalPath = new StringBuilder(directory.length());

                for (int i = 1; i < directoryParts.length; i++)
                {
                    incrementalPath.append('/').append(directoryParts[i]);

                    String currentPath = incrementalPath.toString();

                    if (!session.exists(currentPath))
                    {
                        boolean made = session.mkdir(currentPath);
                        if (!made)
                        {
                            throw new IOException("Could not create file store directory " + directory);
                        }
                    }
                }
            }

            session.write(fileURLResource.getInputStream(), targetURL.getPath());
        }
        finally
        {
            session.close();
        }

        // Post the targetFile object back to the server to set the
        // "publish in lablink" flag and get the LIMS id and URI for the
        // file object.

        String filesUrl = getServerApiAddress() + entityAnno.uriSection();

        ResponseEntity<GenologicsFile> response = restClient.postForEntity(filesUrl, targetFile, GenologicsFile.class);

        reflectiveUpdate(targetFile, response.getBody());
    }

    @Override
    public void downloadFile(Linkable<GenologicsFile> file, OutputStream resultStream) throws IOException
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (resultStream == null)
        {
            throw new IllegalArgumentException("resultStream cannot be null");
        }

        GenologicsEntity entityAnno = checkEntityAnnotated(GenologicsFile.class);

        GenologicsFile realFile;
        if (file instanceof GenologicsFile)
        {
            realFile = (GenologicsFile)file;
            if (realFile.getContentLocation() == null)
            {
                // Don't know where the actual file is, so fetch to get the full info.
                realFile = retrieve(file.getUri(), GenologicsFile.class);
            }
        }
        else
        {
            realFile = retrieve(file.getUri(), GenologicsFile.class);
        }

        URI fileURL;
        if (downloadDirectFromHttpStore && HTTP_PROTOCOLS.contains(realFile.getContentLocation().getScheme()))
        {
            fileURL = realFile.getContentLocation();
        }
        else
        {
            try
            {
                fileURL = new URI(getServerApiAddress() + entityAnno.uriSection() + "/" + realFile.getLimsid() + "/download");
            }
            catch (URISyntaxException e)
            {
                throw new IllegalArgumentException("File LIMS id " + realFile.getLimsid() + " produces an invalid URI for download.", e);
            }
        }

        logger.info("Downloading {}", fileURL);

        ClientHttpRequest request = httpRequestFactory.createRequest(fileURL, HttpMethod.GET);

        ClientHttpResponse response = request.execute();

        switch (response.getStatusCode().series())
        {
            case SUCCESSFUL:
                InputStream in = response.getBody();
                try
                {
                    byte[] buffer = new byte[8192];
                    IOUtils.copyLarge(in, resultStream, buffer);
                }
                finally
                {
                    IOUtils.closeQuietly(in);
                    resultStream.flush();
                }
                logger.debug("{} download successful.", fileURL);
                break;

            default:
                logger.debug("{} download failed. HTTP {}", fileURL, response.getStatusCode());
                throw new IOException("Could not download file " + realFile.getLimsid() +
                                      " (HTTP " + response.getStatusCode() + "): " + response.getStatusText());
        }
    }

    @Override
    public void deleteAndRemoveFile(Linkable<GenologicsFile> file) throws IOException
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file cannot be null");
        }

        GenologicsFile realFile;
        if (file instanceof GenologicsFile)
        {
            realFile = (GenologicsFile)file;
            if (realFile.getContentLocation() == null)
            {
                // Don't know where the actual file is, so fetch to get the full info.
                realFile = retrieve(file.getUri(), GenologicsFile.class);
            }
        }
        else
        {
            realFile = retrieve(file.getUri(), GenologicsFile.class);
        }

        URL targetURL = new URL(null, realFile.getContentLocation().toString(), NullURLStreamHandler.INSTANCE);

        if ("sftp".equalsIgnoreCase(targetURL.getProtocol()))
        {
            logger.info("Deleting file {} from file store on {}", targetURL.getPath(), targetURL.getHost());

            checkFilestoreSet();

            Session<LsEntry> session = filestoreSessionFactory.getSession();
            try
            {
                session.remove(targetURL.getPath());
            }
            catch (NestedIOException e)
            {
                // Don't want things to fail if the file doesn't exist on the file store,
                // just a warning. This handling code deals with this.

                try
                {
                    if (e.getCause() != null)
                    {
                        throw e.getCause();
                    }
                    else
                    {
                        // There is an error in line 71 of SftpSession, where instead of the
                        // SftpException being the cause, its own message is appended to the
                        // detail message for the outer exception with a +.
                        // Bug raised with Spring Integrations as issue INT-3954.
                        if ("Failed to remove file: 2: No such file".equals(e.getMessage()))
                        {
                            throw new SftpException(2, e.getMessage());
                        }

                        throw e;
                    }
                }
                catch (SftpException se)
                {
                    // See if it's just a "file not found".
                    if (se.id == 2)
                    {
                        logger.warn("File {} does not exist on {}", targetURL.getPath(), targetURL.getHost());
                    }
                    else
                    {
                        throw e;
                    }
                }
                catch (Throwable t)
                {
                    throw e;
                }
            }
            finally
            {
                session.close();
            }
        }
        else
        {
            logger.info("File {} is not in the file store, so just removing its record.", targetURL.getPath());
        }

        delete(realFile);
    }


    // Routing artifacts

    @Override
    public void routeArtifacts(Routing routing)
    {
        if (routing == null)
        {
            throw new IllegalArgumentException("routing cannot be null");
        }

        checkServerSet();

        String url = apiRoot + "route/artifacts";

        ResponseEntity<Routing> response = restClient.postForEntity(url, routing, Routing.class);

        reflectiveUpdate(routing, response.getBody());
    }


    // Retrieving artifacts from queues.

    public List<LimsEntityLink<Artifact>> listQueue(Linkable<ProtocolStep> protocolStep)
    {
        if (protocolStep == null)
        {
            throw new IllegalArgumentException("protocolStep cannot be null");
        }
        if (protocolStep.getUri() == null)
        {
            throw new IllegalArgumentException("protocolStep must have its URI set");
        }

        checkServerSet();

        Matcher m = ProtocolStep.ID_EXTRACTOR_PATTERN.matcher(protocolStep.getUri().toString());
        if (!m.matches())
        {
            throw new IllegalArgumentException(
                    "Protocol step URI does not match the expected pattern of /" +
                    ProtocolStep.ID_EXTRACTOR_PATTERN.pattern() +
                    "/ (is \"" + protocolStep.getUri() + "\").");
        }

        String uri = apiRoot + "queues/" + m.group(2);

        // The results list will always contain links that are LimsEntityLinks,
        // actually com.genologics.ri.queue.ArtifactLink
        // It is safe to recast the type of this list without copying.

        List<?> results = doList(uri, Artifact.class, Queue.class, Integer.MAX_VALUE);

        @SuppressWarnings("unchecked")
        List<LimsEntityLink<Artifact>> properLinks = (List<LimsEntityLink<Artifact>>)results;

        return properLinks;
    }


    // Supporting helper methods

    /**
     * Create a {@code Links} object containing the URIs of the link objects
     * given. Use in batch fetch operations.
     *
     * @param entityLinks The links to the entities.
     *
     * @return A Links object containing the URIs.
     */
    protected Links toLinks(Collection<? extends Linkable<?>> entityLinks)
    {
        Links links = new Links(entityLinks.size());

        for (Linkable<?> limsLink : entityLinks)
        {
            links.add(limsLink);
        }

        return links;
    }

    /**
     * Update a collection of original entities with the fields from an equivalent
     * list of freshly retrieved versions of the same entities.
     *
     * <p>
     * Callers should expect that the collections really do contain the same entities
     * in the same order in both collections. The method will fail if the collections
     * are in a different order (comparison made on URI path).
     * </p>
     *
     * @param originals The collection of original entities.
     * @param retrieved The freshly retrieved list of the same entities.
     *
     * @param <E> The type of locatable entity.
     *
     * @throws IllegalArgumentException if either {@code originals} or
     * {@code retrieved} are null; if the size of these two collections is not
     * the same; if the order of items in {@code retrieved} is not the same as the
     * order in {@code originals}.
     *
     * @see #reflectiveUpdate(Object, Object)
     * @see #reorderBatchFetchList(Collection, List)
     */
    protected <E extends Locatable>
    void reflectiveCollectionUpdate(Collection<E> originals, List<E> retrieved)
    {
        if (originals == null)
        {
            throw new IllegalArgumentException("originals cannot be null");
        }
        if (retrieved == null)
        {
            throw new IllegalArgumentException("originals cannot be null");
        }
        if (originals.size() != retrieved.size())
        {
            throw new IllegalArgumentException("originals and retrieved are expected to be exactly the same size");
        }

        Iterator<E> originalIter = originals.iterator();
        Iterator<E> retrievedIter = retrieved.iterator();

        while (originalIter.hasNext())
        {
            E original = originalIter.next();
            E fresh = retrievedIter.next();

            if (original.getUri() != null)
            {
                String originalPath = original.getUri().getPath();
                String freshPath = fresh.getUri().getPath();

                if (!originalPath.equals(freshPath))
                {
                    throw new AssertionError("Original and retrieved collections are not in the same order.");
                }
            }

            reflectiveUpdate(original, fresh);
        }
    }

    /**
     * Reflectively set all the attributes in {@code original} to the values given
     * by {@code updated}. This has the effect of making {@code original} the
     * same as {@code updated} but without requiring the client code to change the
     * object reference to {@code original}, which may be referenced in many places.
     *
     * <p>
     * Where a field is a Collection, the existing collection is emptied and all the
     * objects from that field in {@code updated} are added in the same order to the
     * collection in {@code original}. Whether this order is maintained depends on
     * the type of collection in {@code original} (a list will maintain order, a set
     * typically won't).
     * </p>
     *
     * <p>
     * Fields that are static, transient or final are ignored, as are any fields annotated
     * with the {@code @XmlTransient} annotation.
     * </p>
     *
     * <p>
     * Note that fields within the original object that are objects themselves (as opposed to
     * primitives) are replaced with the new versions. References to sub objects are therefore
     * no longer valid.
     * </p>
     *
     * @param original The original object that was provided in the call and needs updating.
     * @param updated The version of the object returned from the LIMS with the current state.
     *
     * @throws IllegalArgumentException if either {@code original} or {@code updated}
     * are null, or are of different classes.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void reflectiveUpdate(Object original, Object updated)
    {
        if (original == null)
        {
            throw new IllegalArgumentException("original cannot be null");
        }
        if (updated == null)
        {
            throw new IllegalArgumentException("updated cannot be null");
        }

        if (!original.getClass().equals(updated.getClass()))
        {
            throw new IllegalArgumentException("original and updated are of different classes");
        }

        Class<?> clazz = original.getClass();

        do
        {
            Map<String, java.lang.reflect.Field> fieldMap = updaterFields.get(clazz);
            if (fieldMap == null)
            {
                fieldMap = Collections.synchronizedMap(new HashMap<String, java.lang.reflect.Field>());
                updaterFields.put(clazz, fieldMap);

                Class<?> currentClass = clazz;
                while (!Object.class.equals(currentClass))
                {
                    for (java.lang.reflect.Field field : currentClass.getDeclaredFields())
                    {
                        // Skip transient and XmlTransient fields.
                        if ((field.getModifiers() & REFLECTIVE_UPDATE_MODIFIER_MASK) == 0 &&
                                field.getAnnotation(XmlTransient.class) == null)
                        {
                            field.setAccessible(true);
                            java.lang.reflect.Field clash = fieldMap.put(field.getName(), field);
                            if (clash != null)
                            {
                                throw new AssertionError("There is more than one field with the name '" + field.getName() +
                                             " in the class hierarchy of " + clazz.getName() +
                                             " (" + getShortClassName(field.getDeclaringClass()) + " and " +
                                             getShortClassName(clash.getDeclaringClass()) + ")");
                            }
                        }
                    }
                    currentClass = currentClass.getSuperclass();
                }
            }

            for (java.lang.reflect.Field field : fieldMap.values())
            {
                try
                {
                    Object originalValue = field.get(original);
                    Object updatedValue = field.get(updated);

                    if (Collection.class.isAssignableFrom(field.getDeclaringClass()))
                    {
                        Collection originalCollection = (Collection)originalValue;
                        Collection updatedCollection = (Collection)updatedValue;

                        if (originalCollection != null)
                        {
                            originalCollection.clear();
                            if (updatedCollection != null)
                            {
                                originalCollection.addAll(updatedCollection);
                            }
                        }
                        else
                        {
                            if (updatedCollection != null)
                            {
                                // Getting as a property should create the collection object.
                                originalCollection = (Collection)PropertyUtils.getProperty(original, field.getName());
                                originalCollection.addAll(updatedCollection);
                            }
                        }
                    }
                    else if (Map.class.isAssignableFrom(field.getDeclaringClass()))
                    {
                        throw new AssertionError("I didn't think we'd be dealing with maps: field " + field.getName() +
                                                 " on class " + field.getDeclaringClass().getName());
                    }
                    else
                    {
                        field.set(original, updatedValue);
                    }
                }
                catch (IllegalAccessException e)
                {
                    logger.error("Cannot access the property {} on the class {}",
                                 field.getName(), field.getDeclaringClass().getName());
                    fieldMap.remove(field.getName());
                }
                catch (NoSuchMethodException e)
                {
                    logger.error("There is no getter method for the property {} on the class {}",
                                 field.getName(), field.getDeclaringClass().getName());
                    fieldMap.remove(field.getName());
                }
                catch (InvocationTargetException e)
                {
                    logger.error("Error while getting collection property {}", field.getName(), e.getTargetException());
                }
                catch (ClassCastException e)
                {
                    logger.error("Cannot cast a {} to a Collection.", e.getMessage());
                }
            }

            clazz = clazz.getSuperclass();
        }
        while (!Object.class.equals(clazz));
    }

    /**
     * Reorder a collection of entities into the same order given by their request
     * objects. This is important as the batch fetch doesn't guarantee that the
     * entities are returned in the same order as they were requested.
     *
     * <p>
     * The {@code entities} list is updated <i>in-situ</i>, and when this method
     * returns will be in the same order as {@code requestLinks}.
     * </p>
     *
     * @param requestLinks The locatable links that were used to obtain the entities.
     * @param entities The fresh entities returned from the batch call.
     *
     * @param <E> The type of locatable entity.
     */
    protected <E extends Locatable>
    void reorderBatchFetchList(Collection<? extends Locatable> requestLinks, List<E> entities)
    {
        if (requestLinks.size() != entities.size())
        {
            throw new IllegalArgumentException("The request links collection differs in size from the result collection.");
        }

        Map<String, E> entityMap = new HashMap<String, E>();
        for (E entity : entities)
        {
            String path = entity.getUri().getPath();
            E clash = entityMap.put(path, entity);
            if (clash != null)
            {
                throw new IllegalArgumentException("Have more than one entity in the result with the path " + path);
            }
        }

        List<E> sortedList = new ArrayList<E>(entities.size());

        for (Locatable link : requestLinks)
        {
            String path = link.getUri().getPath();

            E entity = entityMap.remove(path);

            if (entity == null)
            {
                throw new AssertionError("No entity was returned for " + link.getUri());
            }

            sortedList.add(entity);
        }

        if (!entityMap.isEmpty())
        {
            throw new AssertionError("Have " + entityMap.size() + " entities left over from request link sorting: " +
                                     StringUtils.join(entityMap.keySet(), ","));
        }

        assert sortedList.size() == entities.size() : "Sorted list differs in size from the original entity list";

        entities.clear();
        entities.addAll(sortedList);
    }

    /**
     * Get an identifier for a locatable object from its URI. This is the
     * last part of the path of the URI and excludes the query string.
     *
     * @param thing The Locatable object.
     *
     * @return The id for the object from the URI.
     */
    protected String idFromUri(Locatable thing)
    {
        String id = thing.getUri().getPath();
        int lastSlash = id.lastIndexOf('/');
        if (lastSlash >= 0)
        {
            id = id.substring(lastSlash + 1);
        }
        return id;
    }

    /**
     * Class for creating URLs where we don't want any connection to actually take
     * place. This is to support the SFTP URLs returned when uploading files. These
     * URLs are valid but because the {@code sftp} protocol is not supported
     * (easily) by Java, we never want to create connections in this manner but also
     * we never want to get {@code MalformedURLException} because of this.
     */
    private static class NullURLStreamHandler extends URLStreamHandler
    {
        /**
         * Singleton instance.
         */
        public static final URLStreamHandler INSTANCE = new NullURLStreamHandler();

        /**
         * Overridden method to never try to open a connection to the URL.
         *
         * @return {@code null}, always.
         */
        @Override
        protected URLConnection openConnection(URL u) throws IOException
        {
            return null;
        }
    }
}
