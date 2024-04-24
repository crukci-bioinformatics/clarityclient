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

package org.cruk.clarity.api.impl;

import static org.apache.commons.lang3.ClassUtils.getShortClassName;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.join;

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
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.sshd.sftp.common.SftpConstants;
import org.apache.sshd.sftp.common.SftpException;
import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.ClarityUpdateException;
import org.cruk.clarity.api.IllegalSearchTermException;
import org.cruk.clarity.api.InvalidURIException;
import org.cruk.clarity.api.StatefulOverride;
import org.cruk.clarity.api.cache.CacheStatefulBehaviour;
import org.cruk.clarity.api.http.AuthenticatingClientHttpRequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.Batch;
import com.genologics.ri.BatchUpdate;
import com.genologics.ri.ClarityBatchRetrieveResult;
import com.genologics.ri.ClarityEntity;
import com.genologics.ri.ClarityQueryResult;
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
import com.genologics.ri.file.ClarityFile;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.process.ClarityProcess;
import com.genologics.ri.processexecution.ExecutableProcess;
import com.genologics.ri.queue.Queue;
import com.genologics.ri.routing.Routing;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.step.AvailableProgram;
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.ProgramStatus;
import com.genologics.ri.step.StepCreation;
import com.genologics.ri.stepconfiguration.ProtocolStep;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.annotation.XmlTransient;


/**
 * Implementation of ClarityAPI using the Clarity REST operations.
 * Uses the Spring REST template for the calls with a Spring Jaxb2Marshaller
 * object for XML/object transformations and a Commons HttpClient to do the
 * actual calls.
 *
 * @see RestTemplate
 * @see Jaxb2Marshaller
 * @see HttpClient
 */
public class ClarityAPIImpl implements ClarityAPI, ClarityAPIInternal
{
    /**
     * The first part of the path for API calls.
     * This project is supporting version two of the Clarity API.
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
     * The part of the URI that specifies the state number.
     */
    private static final String STATE_TERM = "state=";

    /**
     * Regular expression for splitting on ampersand.
     * @see #removeStateParameter(URI)
     */
    private static final Pattern AMPERSAND_SPLIT = Pattern.compile("&");


    /**
     * Logger.
     */
    protected Logger logger = LoggerFactory.getLogger(ClarityAPI.class);

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
    protected AuthenticatingClientHttpRequestFactory httpRequestFactory;

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
     * @see ClarityAPI#setBulkOperationBatchSize(int)
     */
    private int bulkOperationBatchSize = 500;

    /**
     * Whether files can be uploaded using HTTP to the {@code files/id/upload} API end point.
     *
     * @see ClarityAPI#setUploadOverHttp(boolean)
     */
    protected boolean uploadOverHttp = false;

    /**
     * The maximum size of file that can be uploaded using HTTP.
     *
     * @see ClarityAPI#setHttpUploadSizeLimit(long)
     */
    protected long httpUploadSizeLimit = 10485760L;

    /**
     * Whether the SFTP mechanism can be used as a fall back for uploading files if
     * a file exceeds the HTTP upload size limit.
     *
     * @see ClarityAPI#setAutoRevertToSFTPUploads(boolean)
     */
    protected boolean autoRevertToSFTP = true;

    /**
     * Whether to download files that have an HTTP URL directly from their
     * store, or whether to download via the {@code files/id/download} API end point.
     *
     * @see ClarityAPI#setDownloadDirectFromHttpStore(boolean)
     */
    protected boolean downloadDirectFromHttpStore = true;

    /**
     * Map of Locatable class to the class that provides the list of links
     * returned from listing or searching for objects of that type.
     *
     * <p>The key in the pair should be the entity class (e.g. {@code Artifact}
     * and the value should be a class that implements the {@link Batch}
     * interface and is annotated with the {@link ClarityQueryResult}
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
     * interface and is annotated with the {@link ClarityBatchRetrieveResult}
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
     * Thread local flag indicating whether the next call on the thread should
     * fetch stateful entities according to a different rule to the normal.
     */
    protected ThreadLocal<StatefulOverride> statefulOverride = new ThreadLocal<StatefulOverride>();


    /**
     * Standard constructor.
     */
    public ClarityAPIImpl()
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
     * @throws InvalidURIException if the server URL is set and the value cannot form
     * a valid URL.
     *
     * @see #setConfiguration(Properties)
     */
    public ClarityAPIImpl(Properties configuration)
    {
        this();
        setConfiguration(configuration);
    }

    /**
     * Set the file store session factory for SFTP connections.
     *
     * @param filestoreSessionFactory The SFTP session factory.
     */
    @Autowired
    @Qualifier("clarityFilestoreSFTPSessionFactory")
    @SuppressWarnings("exports")
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
    @Autowired
    @Qualifier("clarityJaxbMarshaller")
    @SuppressWarnings("exports")
    public void setJaxbMarshaller(Jaxb2Marshaller jaxbMarshaller)
    {
        entityToListClassMap = new HashMap<Class<? extends Locatable>, Class<?>>();
        entityToBatchRetrieveClassMap = new HashMap<Class<? extends Locatable>, Class<?>>();

        for (Class<?> possibleClass : jaxbMarshaller.getClassesToBeBound())
        {
            ClarityQueryResult queryAnno = possibleClass.getAnnotation(ClarityQueryResult.class);
            ClarityBatchRetrieveResult batchAnno = possibleClass.getAnnotation(ClarityBatchRetrieveResult.class);

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
    @Autowired
    @Qualifier("clarityRestTemplate")
    @SuppressWarnings("exports")
    public void setRestClient(RestOperations restClient)
    {
        this.restClient = restClient;
    }

    /**
     * Set the REST client used for file uploads over HTTP.
     *
     * @param fileUploadClient The REST client configured for file upload.
     */
    @Autowired
    @Qualifier("clarityFileUploadTemplate")
    @SuppressWarnings("exports")
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
    @Autowired
    @Qualifier("clarityHttpClient")
    @SuppressWarnings("exports")
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
     * @param httpRequestFactory The HTTP request factory supporting basic authentication.
     */
    @Autowired
    @Qualifier("clarityClientHttpRequestFactory")
    public void setHttpRequestFactory(AuthenticatingClientHttpRequestFactory httpRequestFactory)
    {
        this.httpRequestFactory = httpRequestFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getServer()
    {
        return serverAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServer(URL serverAddress)
    {
        if (serverAddress == null)
        {
            throw new IllegalArgumentException("serverAddress cannot be set to null");
        }
        if (httpRequestFactory == null)
        {
            throw new IllegalStateException("Request factory has not been set.");
        }

        String currentHost = this.serverAddress == null ? null : this.serverAddress.getHost();

        this.serverAddress = serverAddress;

        httpRequestFactory.setCredentials(serverAddress, apiCredentials);

        String addr = serverAddress.toExternalForm();
        addr = org.springframework.util.StringUtils.trimTrailingCharacter(addr, '/');

        apiRoot = addr + API_PATH_BASE;

        String filestoreHostAddress = getFilestoreServer();

        if (filestoreHostAddress == null || filestoreHostAddress.equals(currentHost))
        {
            setFilestoreServer(serverAddress.getHost());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServerApiAddress()
    {
        return apiRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername()
    {
        return apiCredentials.getUserName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCredentials(String username, String password)
    {
        apiCredentials = new UsernamePasswordCredentials(username, password.toCharArray());
        if (serverAddress != null)
        {
            httpRequestFactory.setCredentials(serverAddress, apiCredentials);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("exports")
    public void setCredentials(Credentials httpCredentials)
    {
        if (serverAddress != null)
        {
            httpRequestFactory.setCredentials(serverAddress, httpCredentials);
        }

        if (httpCredentials instanceof UsernamePasswordCredentials)
        {
            apiCredentials = (UsernamePasswordCredentials)httpCredentials;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilestoreServer(String host)
    {
        if (host == null)
        {
            throw new IllegalArgumentException("host cannot be null");
        }
        filestoreSessionFactory.setHost(host);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilestoreCredentials(String username, String password)
    {
        if (username == null)
        {
            throw new IllegalArgumentException("username cannot be null");
        }

        filestoreCredentials = new UsernamePasswordCredentials(username, password.toCharArray());

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfiguration(Properties configuration)
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

                if (isNotBlank(apiServer))
                {
                    try
                    {
                        setServer(new URL(apiServer));
                    }
                    catch (MalformedURLException e)
                    {
                        throw new InvalidURIException("The server address is not a valid URL: ", e);
                    }
                }
                if (isNotBlank(apiUser))
                {
                    setCredentials(apiUser, apiPass);
                }
                if (isNotBlank(filestoreServer))
                {
                    setFilestoreServer(filestoreServer);
                }
                if (isNotBlank(filestoreUser))
                {
                    setFilestoreCredentials(filestoreUser, filestorePass);
                }

                if (isNotBlank(batchSize))
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
                if (isNotBlank(httpUpload))
                {
                    setUploadOverHttp(Boolean.parseBoolean(httpUpload));
                }
                if (isNotBlank(httpUploadLimit))
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
                if (isNotBlank(revertToSftp))
                {
                    setAutoRevertToSFTPUploads(Boolean.parseBoolean(revertToSftp));
                }
                if (isNotBlank(httpDirect))
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
     * @see ClarityAPI#setBulkOperationBatchSize(int)
     */
    public int getBulkOperationBatchSize()
    {
        return bulkOperationBatchSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBulkOperationBatchSize(int batchSize)
    {
        bulkOperationBatchSize =
                batchSize <= 0
                    ? BULK_OPERATION_BATCH_SIZE_HARD_LIMIT
                    : Math.min(batchSize, BULK_OPERATION_BATCH_SIZE_HARD_LIMIT);
    }

    /**
     * {@inheritDoc}
     */
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
     * @see ClarityAPI#setHttpUploadSizeLimit(long)
     */
    public long getHttpUploadSizeLimit()
    {
        return httpUploadSizeLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHttpUploadSizeLimit(long limit)
    {
        if (limit < 1)
        {
            throw new IllegalArgumentException("HTTP upload size limit must be positive.");
        }
        httpUploadSizeLimit = limit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoRevertToSFTPUploads(boolean autoRevertToSFTP)
    {
        this.autoRevertToSFTP = autoRevertToSFTP;
    }

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable, BH extends Batch<? extends LimsLink<E>>>
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
     * {@code ClarityEntity} annotation.
     *
     * @param entityClass The class to check.
     *
     * @return The ClarityEntity annotation found.
     *
     * @throws IllegalArgumentException if {@code entityClass} is null or is not
     * annotated.
     */
    protected ClarityEntity checkEntityAnnotated(Class<?> entityClass)
    {
        if (entityClass == null)
        {
            throw new IllegalArgumentException("entityClass cannot be null");
        }

        ClarityEntity entityAnno = entityClass.getAnnotation(ClarityEntity.class);
        if (entityAnno == null)
        {
            throw new IllegalArgumentException(
                    "The class " + entityClass.getName() + " has not been annotated with the ClarityEntity annotation.");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    URI limsIdToUri(String limsid, Class<E> entityClass)
    {
        try
        {
            return new URI(makeUri(limsid, entityClass, "limsIdToUri"));
        }
        catch (URISyntaxException e)
        {
            throw new InvalidURIException(e);
        }
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
     * @see ClarityAPI#limsIdToUri(String, Class)
     */
    protected <E extends Locatable>
    String makeUri(String limsid, Class<E> entityClass, String method)
    {
        if (isEmpty(limsid))
        {
            throw new IllegalArgumentException("limsid cannot be null or empty");
        }

        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        if (entityAnno.primaryEntity() != void.class)
        {
            throw new IllegalArgumentException(
                    entityClass.getName() + " has a double section endpoint in the API. " +
                    "Use " + method + "(String, String, Class) for this type.");
        }

        checkServerSet();

        // Special case for instrument when asked for its full lims id, eg. "55-10"
        // The URI can only have the last part of this, creating a URI ending with the
        // simple number only, ie. "55-10" becomes "/10" in the URI.
        // See Redmine 7273.

        if (Instrument.class.equals(entityClass) && limsid.startsWith("55-"))
        {
            limsid = limsid.substring(3);
        }

        StringBuilder uri = new StringBuilder(apiRoot);
        uri.append(entityAnno.uriSection()).append('/').append(limsid);
        if (isNotEmpty(entityAnno.uriSubsection()))
        {
            uri.append('/').append(entityAnno.uriSubsection());
        }

        return uri.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    URI limsIdToUri(String outerLimsid, String innerLimsid, Class<E> entityClass)
    {
        try
        {
            return new URI(makeUri(outerLimsid, innerLimsid, entityClass, "limsIdToUri"));
        }
        catch (URISyntaxException e)
        {
            throw new InvalidURIException(e);
        }
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
     * @see ClarityAPI#limsIdToUri(String, String, Class)
     */
    protected <E extends Locatable>
    String makeUri(String outerLimsid, String innerLimsid, Class<E> entityClass, String method)
    {
        if (isEmpty(outerLimsid))
        {
            throw new IllegalArgumentException("outerLimsid cannot be null or empty");
        }
        if (isEmpty(innerLimsid))
        {
            throw new IllegalArgumentException("innerLimsid cannot be null or empty");
        }

        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        if (entityAnno.primaryEntity() == void.class)
        {
            throw new IllegalArgumentException(
                    entityClass.getName() + " has a single section endpoint in the API. " +
                    "Use " + method + "(String, Class) for this type.");
        }

        checkServerSet();

        ClarityEntity primaryAnno = checkEntityAnnotated(entityAnno.primaryEntity());

        String uri = apiRoot + primaryAnno.uriSection() + '/' + outerLimsid +
                     '/' + entityAnno.uriSection() + '/' + innerLimsid;

        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @SuppressWarnings("incomplete-switch")
    public void nextCallCacheOverride(CacheStatefulBehaviour behaviour)
    {
        if (behaviour != null)
        {
            switch (behaviour)
            {
                case EXACT:
                    overrideStateful(StatefulOverride.EXACT);
                    return;

                case LATEST:
                    overrideStateful(StatefulOverride.LATEST);
                    return;
            }
        }

        overrideStateful(null);
    }

    /**
     * Forces the API to fetch stateful entities according to the rule given for
     * the next API call only. Full details on the {@code ClarityAPI} description.
     *
     * @param override The behaviour to use in the next call. If null, it will
     * cancel a previously set override.
     *
     * @see ClarityAPI#overrideStateful(StatefulOverride)
     */
    @Override
    public void overrideStateful(StatefulOverride override)
    {
        statefulOverride.set(override);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StatefulOverride getStatefulOverride()
    {
        return statefulOverride.get();
    }

    /**
     * Convenience method to test whether the latest version of stateful
     * entities is required.
     *
     * @return True if there is an override wanting the latest version,
     * false if not.
     */
    private boolean isFetchLatestVersions()
    {
        return statefulOverride.get() == StatefulOverride.LATEST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelStatefulOverride(String calledMethod)
    {
        boolean hadOverride = statefulOverride.get() != null;

        statefulOverride.set(null);

        if (hadOverride && logger.isDebugEnabled())
        {
            if (calledMethod == null)
            {
                logger.debug("Reverted to normal state version behaviour.");
            }
            else
            {
                logger.debug("Reverted to normal state version behaviour after call to {}.", calledMethod);
            }
        }
    }

    // General fetch methods.

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    List<LimsLink<E>> listAll(Class<E> entityClass)
    {
        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        checkServerSet();

        String startUri = apiRoot + entityAnno.uriSection();

        return doList(startUri, entityClass, Integer.MAX_VALUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    List<LimsLink<E>> listSome(Class<E> entityClass, int startIndex, int number)
    {
        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        checkServerSet();

        String startUri = apiRoot + entityAnno.uriSection() + "?start-index=" + startIndex;

        return doList(startUri, entityClass, number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    List<LimsLink<E>> find(Map<String, ?> searchTerms, Class<E> entityClass)
    {
        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        if (entityAnno.primaryEntity() != void.class)
        {
            String entityClassName = getShortClassName(entityClass);
            String primaryName = getShortClassName(entityAnno.primaryEntity());

            throw new IllegalArgumentException(
                    "Cannot search for " + entityClassName + "s as they are part of " + primaryName + ". " +
                    "A " + primaryName + " should supply a list of its relevant " + entityClassName + "s.");
        }

        checkServerSet();

        StringBuilder query = expandSearchTerms(searchTerms);

        StringBuilder uri = new StringBuilder(256 + query.length());
        uri.append(apiRoot).append(entityAnno.uriSection());
        if (query.length() > 0)
        {
            uri.append('?').append(query);
        }

        return doList(uri.toString(), entityClass, Integer.MAX_VALUE);
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
        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        String entityClassName = getShortClassName(entityClass);

        if (entityAnno.primaryEntity() != void.class)
        {
            String primaryName = getShortClassName(entityAnno.primaryEntity());

            throw new IllegalArgumentException(
                    "Cannot list all " + entityClassName + "s as they are part of " + primaryName + ". " +
                    "A " + primaryName + " should supply a list of its relevant " + entityClassName + "s.");
        }

        ArrayList<LimsLink<E>> allLinks = new ArrayList<>(1024);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    E retrieve(String uri, Class<E> entityClass)
    {
        if (isEmpty(uri))
        {
            throw new IllegalArgumentException("uri cannot be null or empty");
        }

        ClarityEntity anno = checkEntityAnnotated(entityClass);
        if (anno.stateful() && isFetchLatestVersions())
        {
            uri = removeStateParameter(uri);
        }

        ResponseEntity<E> response = restClient.getForEntity(uri, entityClass);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    E retrieve(URI uri, Class<E> entityClass)
    {
        if (uri == null)
        {
            throw new IllegalArgumentException("uri cannot be null");
        }

        ClarityEntity anno = checkEntityAnnotated(entityClass);
        if (anno.stateful() && isFetchLatestVersions())
        {
            uri = removeStateParameter(uri);
        }

        ResponseEntity<E> response = restClient.getForEntity(uri, entityClass);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    E load(String limsid, Class<E> entityClass)
    {
        return retrieve(makeUri(limsid, entityClass, "load"), entityClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    E load(String outerLimsid, String innerLimsid, Class<E> entityClass)
    {
        return retrieve(makeUri(outerLimsid, innerLimsid, entityClass, "load"), entityClass);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

                ClarityEntity entityAnno = checkEntityAnnotated(entityClass);
                checkServerSet();
                String uri = apiRoot + entityAnno.uriSection() + "/batch/retrieve";

                boolean stripState = entityAnno.stateful() && isFetchLatestVersions();

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

                    ResponseEntity<Batch<E>> response = restClient.postForEntity(uri, toLinks(batch, stripState), batchFetchResultClass);

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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
        ClarityEntity entityAnno = checkEntityAnnotated(entity.getClass());

        if (!entityAnno.creatable())
        {
            throw new ClarityUpdateException(getShortClassName(entityClass) + " cannot be created.");
        }

        checkServerSet();

        boolean processStepComponent = "steps".equals(entityAnno.uriSection()) && isNotEmpty(entityAnno.uriSubsection());

        StringBuilder uri = new StringBuilder(100);
        if (processStepComponent)
        {
            try
            {
                Link step = (Link)PropertyUtils.getProperty(entity, "step");
                if (step == null || step.getUri() == null)
                {
                    throw new IllegalArgumentException("entity does not have its Step URI set. This is needed to post a new " +
                                                       getShortClassName(entityClass) + ".");
                }
                uri.append(step.getUri()).append('/').append(entityAnno.uriSubsection());
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
            uri.append(apiRoot).append(entityAnno.uriSection());
        }

        doCreateSingle(entity, uri.toString());
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
        ClarityEntity entityAnno = checkEntityAnnotated(entity.getClass());

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

    /**
     * {@inheritDoc}
     */
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
     * @throws ClarityUpdateException if the entities cannot be created via the API
     * (as determined by the {@link ClarityEntity#creatable()} flag).
     *
     * @param <E> The type of the entity.
     * @param <BH> The type of the object that holds the list of links to these entities.
     *
     * @see ClarityEntity#creatable()
     * @see ClarityBatchRetrieveResult#batchCreate()
     */
    private <E extends Locatable, BH extends BatchUpdate<E>>
    void doCreateAll(Collection<E> entities)
    {
        if (entities != null && !entities.isEmpty())
        {
            Class<E> entityClass = checkCollectionHomogeneousAndUnique(entities, false);

            ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

            if (!entityAnno.creatable())
            {
                throw new ClarityUpdateException(getShortClassName(entityClass) + " cannot be created.");
            }

            assert entityAnno.primaryEntity() == void.class :
                entityClass.getName() + " has a primary entity set, but such things cannot be created through the API.";

            Class<BH> batchRetrieveClass = getBatchRetrieveClassForEntity(entityClass);

            checkServerSet();

            boolean doBatchCreates = false;

            if (batchRetrieveClass != null)
            {
                ClarityBatchRetrieveResult batchAnnotation =
                        batchRetrieveClass.getAnnotation(ClarityBatchRetrieveResult.class);
                assert batchAnnotation != null : "No ClarityBatchRetrieveResult annotation on result class " + entityClass.getName();

                doBatchCreates = batchAnnotation.batchCreate() && entities.size() > 1;
            }

            if (doBatchCreates)
            {
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

                        Constructor<BH> batchRetrieveConstructor = batchRetrieveClass.getConstructor();
                        BH details = batchRetrieveConstructor.newInstance();

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
                catch (NoSuchMethodException e)
                {
                    logger.error("There is no default constructor on {}", batchRetrieveClass.getName());
                }
                catch (IllegalAccessException e)
                {
                    logger.error("Cannot access the default constructor on {}", batchRetrieveClass.getName());
                }
                catch (InstantiationException e)
                {
                    logger.error("Cannot create a new {}: {}", batchRetrieveClass.getName(), e.getMessage());
                }
                catch (InvocationTargetException e)
                {
                    Throwable cause = e.getTargetException();
                    logger.error("{} creating a new {}: {}", ClassUtils.getShortClassName(cause.getClass()), batchRetrieveClass.getName(), cause.getMessage());
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

        ClarityEntity artifactEntityAnno = checkEntityAnnotated(Artifact.class);

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

    /**
     * {@inheritDoc}
     */
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
        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        if (!entityAnno.updateable())
        {
            throw new ClarityUpdateException(getShortClassName(entityClass) + " cannot be updated.");
        }

        assert entityAnno.primaryEntity() == void.class :
            entityClass.getName() + " has a primary entity set, but such things cannot be created through the API.";

        ResponseEntity<? extends Locatable> response =
                restClient.exchange(entity.getUri(), HttpMethod.PUT, new HttpEntity<Locatable>(entity), entity.getClass());

        reflectiveUpdate(entity, response.getBody());
    }

    /**
     * {@inheritDoc}
     */
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
     * @throws ClarityUpdateException if the entities cannot be updated via the API
     * (as determined by the {@link ClarityEntity#updateable()} flag).
     */
    private <E extends Locatable, BH extends BatchUpdate<E>>
    void doUpdateAll(Collection<E> entities)
    {
        if (entities != null && !entities.isEmpty())
        {
            Class<E> entityClass = checkCollectionHomogeneousAndUnique(entities, true);

            ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

            if (!entityAnno.updateable())
            {
                throw new ClarityUpdateException(getShortClassName(entityClass) + " cannot be updated.");
            }

            assert entityAnno.primaryEntity() == void.class :
                entityClass.getName() + " has a primary entity set, but such things cannot be updated through the API.";

            Class<BH> batchUpdateClass = getBatchRetrieveClassForEntity(entityClass);

            boolean doBatchUpdates = false;

            if (batchUpdateClass != null)
            {
                ClarityBatchRetrieveResult batchAnnotation =
                        batchUpdateClass.getAnnotation(ClarityBatchRetrieveResult.class);
                assert batchAnnotation != null : "No ClarityBatchRetrieveResult annotation on result class " + entityClass.getName();

                doBatchUpdates = batchAnnotation.batchUpdate() && entities.size() > 1;
            }

            checkServerSet();

            if (doBatchUpdates)
            {
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

                        Constructor<BH> batchUpdateConstructor = batchUpdateClass.getConstructor();
                        BH details = batchUpdateConstructor.newInstance();
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
                catch (NoSuchMethodException e)
                {
                    logger.error("There is no default constructor on {}", batchUpdateClass.getName());
                }
                catch (IllegalAccessException e)
                {
                    logger.error("Cannot access the default constructor on {}", batchUpdateClass.getName());
                }
                catch (InstantiationException e)
                {
                    logger.error("Cannot create a new {}: {}", batchUpdateClass.getName(), e.getMessage());
                }
                catch (InvocationTargetException e)
                {
                    Throwable cause = e.getTargetException();
                    logger.error("{} creating a new {}: {}", ClassUtils.getShortClassName(cause.getClass()), batchUpdateClass.getName(), cause.getMessage());
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

    // Delete methods.

    /**
     * {@inheritDoc}
     */
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

        doDelete(entity.getUri(), classOfEntity(entity));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Locatable>
    void deleteAll(Collection<E> entities)
    {
        if (entities != null && !entities.isEmpty())
        {
            checkCollectionHomogeneousAndUnique(entities, true);
            Class<? extends Locatable> entityClass = classOfEntity(entities.iterator().next());

            // There is no batch delete.
            for (E entity : entities)
            {
                assert entity != null : "Have null entity after check";
                doDelete(entity.getUri(), entityClass);
            }
        }
    }

    /**
     * Check if the given Locatable object is a LimsLink and, if so, use the class given
     * by the {@code getEntityClass()} method as the type of the entity, not the class of
     * the link itself.
     *
     * <p>
     * This method supports the delete methods to delete entities through links rather than
     * having to load the entity just to delete it.
     * </p>
     *
     * @param <E> The type of LIMS entity.
     * @param entity The entity to delete (might be a link).
     *
     * @return The correct class for the entity for further checks.
     *
     * @see LimsLink#getEntityClass()
     * @see <a href="https://github.com/crukci-bioinformatics/clarityclient/issues/8">Issue 8 on Github</a>
     */
    private <E extends Locatable>
    Class<? extends Locatable> classOfEntity(E entity)
    {
        assert entity != null : "entity cannot be null";

        Class<? extends Locatable> eClass = entity.getClass();
        if (LimsLink.class.isAssignableFrom(eClass))
        {
            eClass = ((LimsLink<?>)entity).getEntityClass();
        }

        return eClass;
    }

    /**
     * Remove an entity from Clarity.
     *
     * @param uri The URI of the entity to delete.
     * @param entityClass The type of entity to delete.
     *
     * @param <E> The type of the entity.
     *
     * @throws ClarityUpdateException if the entities cannot be deleted via the API
     * (as determined by the {@link ClarityEntity#removable()} flag).
     */
    private <E extends Locatable>
    void doDelete(URI uri, Class<E> entityClass)
    {
        ClarityEntity entityAnno = checkEntityAnnotated(entityClass);

        if (!entityAnno.removable())
        {
            throw new ClarityUpdateException(getShortClassName(entityClass) + " cannot be deleted.");
        }

        assert entityAnno.primaryEntity() == void.class :
            entityClass.getName() + " has a primary entity set, but such things cannot be deleted through the API.";

        restClient.delete(uri);
    }


    // Process execution

    /**
     * {@inheritDoc}
     */
    @Override
    public ClarityProcess executeProcess(ExecutableProcess toExecute)
    {
        if (toExecute == null)
        {
            throw new IllegalArgumentException("toExecute cannot be null");
        }

        checkServerSet();

        String uri = apiRoot + "processes";

        ResponseEntity<ClarityProcess> response = restClient.postForEntity(uri, toExecute, ClarityProcess.class);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void advanceProcessStep(ProcessStep step)
    {
        if (step == null)
        {
            throw new IllegalArgumentException("step cannot be null");
        }
        if (step.getUri() == null)
        {
            throw new IllegalArgumentException("step has no URI set.");
        }

        Class<? extends Locatable> entityClass = step.getClass();
        checkEntityAnnotated(entityClass);

        String uri = step.getUri() + "/advance";

        ResponseEntity<? extends Locatable> response =
                restClient.exchange(uri, HttpMethod.POST, new HttpEntity<ProcessStep>(step), step.getClass());

        reflectiveUpdate(step, response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgramStatus startProgram(AvailableProgram program)
    {
        if (program == null)
        {
            throw new IllegalArgumentException("program cannot be null");
        }
        if (program.getUri() == null)
        {
            throw new IllegalArgumentException("program has no URI set.");
        }

        ResponseEntity<ProgramStatus> response = restClient.postForEntity(program.getUri(), null, ProgramStatus.class);

        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void currentStatus(ProgramStatus status)
    {
        if (status == null)
        {
            throw new IllegalArgumentException("status cannot be null");
        }
        if (status.getUri() == null)
        {
            throw new IllegalArgumentException("status has no URI set.");
        }

        ResponseEntity<ProgramStatus> response = restClient.getForEntity(status.getUri(), ProgramStatus.class);

        reflectiveUpdate(status, response.getBody());
    }


    // File upload

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends LimsEntity<E>>
    ClarityFile uploadFile(LimsEntityLinkable<E> entity, URL fileURL, boolean publishInLablink)
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

        ClarityFile storageRequest;

        URLInputStreamResource fileURLResource = new URLInputStreamResource(fileURL);
        try
        {
            long length = fileURLResource.contentLength();

            if (length < 0)
            {
                logger.warn("Cannot determine size of file from the " + fileURL.getProtocol() + " protocol.");
            }

            // Post a request to the "glsstorage" to create a new ClarityFile object to
            // hold the uploaded file. Requirements are for the holding entity and the
            // original location to be set.
            // See http://www.genologics.com/files/permanent/API/latest/rest.version.glsstorage.html

            storageRequest = new ClarityFile();
            storageRequest.setAttachedTo(entity);
            storageRequest.setOriginalLocation(fileURL.toExternalForm());

            String storageUri = apiRoot + "glsstorage";

            ResponseEntity<ClarityFile> response = restClient.postForEntity(storageUri, storageRequest, ClarityFile.class);

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
                throw new ClarityUpdateException("File upload to the file store for links giving the " +
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
                        throw new ClarityUpdateException("Cannot upload " + fileURL +
                                " - cannot determine its size, so it may exceed the maximum HTTP upload size of " +
                                httpUploadSizeLimit);
                    }
                    else
                    {
                        throw new ClarityUpdateException("Cannot upload " + fileURL +
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
     * Upload a file to the Clarity file store. This always uses the HTTP
     * protocol with the {@code file/id/upload} end point.
     *
     * @param fileURLResource The URL resource of the file on the local machine.
     * @param targetFile The ClarityFile object that holds the reference to the
     * uploaded file, which was newly created using the API.
     *
     * @throws ClarityException if the server reports a problem with the upload.
     * @throws IllegalStateException if {@code targetFile} does not have a LIMS id.
     * @throws IOException if there is a problem with the transfer.
     * @throws InvalidURIException if the upload URI string isn't a valid URI (shouldn't happen).
     */
    protected void uploadViaHTTP(URLInputStreamResource fileURLResource, ClarityFile targetFile)
    throws IOException
    {
        ClarityEntity entityAnno = checkEntityAnnotated(ClarityFile.class);

        if (targetFile.getLimsid() == null)
        {
            // Need to post the file back to the LIMS to obtain a URI and LIMS
            // id for the file object.

            String filesUrl = getServerApiAddress() + entityAnno.uriSection();

            ResponseEntity<ClarityFile> response = restClient.postForEntity(filesUrl, targetFile, ClarityFile.class);

            reflectiveUpdate(targetFile, response.getBody());

            assert targetFile.getLimsid() != null : "Still no LIMS id on ClarityFile object.";
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
                throw new InvalidURIException("File LIMS id " + targetFile.getLimsid() + " produces an invalid URI for upload: ", e);
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
                    logger.warn("Failed to clean up ClarityFile object {} after upload failure:", targetFile.getLimsid(), e);
                }
            }
        }

        if (!uploadedOk)
        {
            // I don't think the code can get here as other exceptions should
            // have been thrown. To make sure though...

            throw new ClarityUpdateException("Failed to upload " + fileURLResource.getURL());
        }
    }

    /**
     * Upload a file to the Clarity file store. This always uses the SFTP protocol.
     *
     * @param fileURLResource The URL resource of the file on the local machine.
     * @param targetFile The ClarityFile object that holds the reference to the
     * uploaded file, which was newly created using the API.
     *
     * @throws IOException if there is a problem with the transfer.
     * @throws IllegalStateException if the file store host name or credentials
     * are not set.
     */
    protected void uploadViaSFTP(URLInputStreamResource fileURLResource, ClarityFile targetFile)
    throws IOException
    {
        ClarityEntity entityAnno = checkEntityAnnotated(ClarityFile.class);

        checkFilestoreSet();

        SftpSession session = filestoreSessionFactory.getSession();
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

        ResponseEntity<ClarityFile> response = restClient.postForEntity(filesUrl, targetFile, ClarityFile.class);

        reflectiveUpdate(targetFile, response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void downloadFile(Linkable<ClarityFile> file, OutputStream resultStream) throws IOException
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (resultStream == null)
        {
            throw new IllegalArgumentException("resultStream cannot be null");
        }

        ClarityEntity entityAnno = checkEntityAnnotated(ClarityFile.class);

        ClarityFile realFile;
        if (file instanceof ClarityFile)
        {
            realFile = (ClarityFile)file;
            if (realFile.getContentLocation() == null)
            {
                // Don't know where the actual file is, so fetch to get the full info.
                realFile = retrieve(file.getUri(), ClarityFile.class);
            }
        }
        else
        {
            realFile = retrieve(file.getUri(), ClarityFile.class);
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

        if (response.getStatusCode().is2xxSuccessful())
        {
            try (InputStream in = response.getBody())
            {
                byte[] buffer = new byte[8192];
                IOUtils.copyLarge(in, resultStream, buffer);
            }
            finally
            {
                resultStream.flush();
            }
            logger.debug("{} download successful.", fileURL);
        }
        else
        {
            logger.debug("{} download failed. HTTP {}", fileURL, response.getStatusCode());
            throw new IOException("Could not download file " + realFile.getLimsid() +
                                  " (HTTP " + response.getStatusCode() + "): " + response.getStatusText());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAndRemoveFile(Linkable<ClarityFile> file) throws IOException
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file cannot be null");
        }
        if (file.getUri() == null)
        {
            throw new IllegalArgumentException("file has no URI set.");
        }

        ClarityFile realFile;
        if (file instanceof ClarityFile)
        {
            realFile = (ClarityFile)file;
            if (realFile.getContentLocation() == null)
            {
                // Don't know where the actual file is, so fetch to get the full info.
                realFile = retrieve(file.getUri(), ClarityFile.class);
            }
        }
        else
        {
            realFile = retrieve(file.getUri(), ClarityFile.class);
        }

        URL targetURL = new URL(null, realFile.getContentLocation().toString(), NullURLStreamHandler.INSTANCE);

        if (SFTP_PROTOCOL.equalsIgnoreCase(targetURL.getProtocol()))
        {
            logger.info("Deleting file {} from file store on {}", targetURL.getPath(), targetURL.getHost());

            checkFilestoreSet();

            try (SftpSession session = filestoreSessionFactory.getSession())
            {
                session.remove(targetURL.getPath());
            }
            catch (IOException e)
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
                    if (se.getStatus() == SftpConstants.SSH_FX_NO_SUCH_FILE)
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
        }
        else
        {
            logger.debug("File {} is not in the file store, so just removing its record.", targetURL.getPath());
        }

        doDelete(realFile.getUri(), ClarityFile.class);
    }


    // Routing artifacts

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LimsEntityLink<Artifact>> listQueue(Linkable<ProtocolStep> protocolStep)
    {
        return listQueue(protocolStep, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LimsEntityLink<Artifact>> listQueue(Linkable<ProtocolStep> protocolStep, Map<String, ?> searchTerms)
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

        StringBuilder query = expandSearchTerms(searchTerms);

        StringBuilder uri = new StringBuilder(256 + query.length());
        uri.append(apiRoot).append("queues/").append(m.group(2));
        if (query.length() > 0)
        {
            uri.append('?').append(query);
        }

        // The results list will always contain links that are LimsEntityLinks,
        // actually com.genologics.ri.queue.ArtifactLink
        // It is safe to recast the type of this list without copying.

        List<?> results = doList(uri.toString(), Artifact.class, Queue.class, Integer.MAX_VALUE);

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
     * @param stripState Whether to remove state version parameters from the URIs.
     *
     * @return A Links object containing the URIs.
     */
    protected Links toLinks(Collection<? extends Linkable<?>> entityLinks, boolean stripState)
    {
        Links links = new Links(entityLinks.size());

        for (Linkable<?> limsLink : entityLinks)
        {
            Link l = new Link(limsLink);
            if (stripState)
            {
                l.setUri(removeStateParameter(l.getUri()));
            }
            links.add(l);
        }

        return links;
    }

    /**
     * Expand a map of search terms into a query string suitable for a URI.
     * Handles values that are arrays or collections by repeating the parameter
     * for each value.
     *
     * @param searchTerms The terms to use for the search. A null value here
     * is the same as an empty map.
     *
     * @return The query string created from the search terms.
     *
     * @throws IllegalSearchTermException if any term in {@code searchTerms} is
     * found to be illegal. See {@link IllegalSearchTermException} for details of
     * what is illegal.
     *
     * @see ClarityAPI#find(Map, Class)
     */
    protected StringBuilder expandSearchTerms(Map<String, ?> searchTerms)
    {
        StringBuilder query = new StringBuilder(1024);

        if (searchTerms == null)
        {
            return query;
        }

        for (Map.Entry<String, ?> term : searchTerms.entrySet())
        {
            // Have issues with Groovy GStrings being flagged up as cannot be cast to Strings.
            // We'll translate them into Java strings before using them.
            final Object paramO = term.getKey();
            final String param = paramO.toString();

            Object value = term.getValue();
            if (value == null)
            {
                throw new IllegalSearchTermException(
                        param, "Search term \"" + param + "\" is null.");
            }
            else
            {
                if (value.getClass().isArray())
                {
                    Object[] values = (Object[])value;

                    if (values.length == 0)
                    {
                        throw new IllegalSearchTermException(
                                param, "Search term \"" + param + "\" has no values.");
                    }

                    for (Object v : values)
                    {
                        appendQueryTerm(query, param, v);
                    }
                }
                else if (value instanceof Iterable)
                {
                    Iterable<?> values = (Iterable<?>)value;

                    if (!values.iterator().hasNext())
                    {
                        throw new IllegalSearchTermException(
                                param, "Search term \"" + param + "\" has no values.");
                    }

                    for (Object v : values)
                    {
                        appendQueryTerm(query, param, v);
                    }
                }
                else
                {
                    appendQueryTerm(query, param, value);
                }
            }
        }

        return query;
    }

    /**
     * Helper method to {@code expandSearchTerms}: builds up a query string with
     * joining ampersands and converts the value given into a string.
     *
     * @param query The StringBuilder which is building up the query.
     * @param argument The search parameter.
     * @param value The value to search for.
     *
     * @throws IllegalSearchTermException if {@code value} is null.
     *
     * @see #expandSearchTerms(Map)
     * @see ConvertUtilsBean#convert(Object)
     */
    private void appendQueryTerm(StringBuilder query, String argument, Object value)
    {
        if (value == null)
        {
            // This message is sensible as find() will not call this method if it
            // finds the term's immediate value is null. It will only get here with
            // value == null when looping through an array or collection.

            throw new IllegalSearchTermException(
                    argument, "Search term \"" + argument + "\" contains a null value.");
        }

        String strValue = ConvertUtils.convert(value);

        if (query.length() > 0)
        {
            query.append('&');
        }
        query.append(argument);
        query.append('=');
        query.append(strValue);
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
                                     join(entityMap.keySet(), ","));
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
     * Strip the state parameter from a URI.
     *
     * @param uri The original URI.
     *
     * @return The URI minus the state parameter.
     *
     * @throws InvalidURIException if the newly formed URI is somehow invalid.
     * This shouldn't ever happen.
     */
    protected URI removeStateParameter(URI uri)
    {
        String query = uri.getRawQuery();
        if (isNotEmpty(query))
        {
            StringBuilder newQuery = new StringBuilder(query.length());

            boolean hasStateParameter = false;
            for (String term : AMPERSAND_SPLIT.split(query))
            {
                if (isNotBlank(term))
                {
                    if (term.startsWith(STATE_TERM))
                    {
                        hasStateParameter = true;
                    }
                    else
                    {
                        if (newQuery.length() > 0)
                        {
                            newQuery.append('&');
                        }
                        newQuery.append(term);
                    }
                }
            }

            // Don't need a new URI object if nothing has been removed.
            if (hasStateParameter)
            {
                String nq = null;
                if (newQuery.length() > 0)
                {
                    nq = newQuery.toString();
                }

                try
                {
                    return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                                   uri.getPath(), nq, uri.getFragment());
                }
                catch (URISyntaxException e)
                {
                    // This should never happen, as we're creating the URI from an existing, valid
                    // URI object. It could only happen if something goes wrong with recreating the
                    // query string.
                    throw new InvalidURIException("Could not recreate a URI: ", e);
                }
            }
        }

        return uri;
    }

    /**
     * Strip the state parameter from a URI in string form.
     *
     * @param uri The original URI string.
     *
     * @return The URI minus the state parameter.
     *
     * @throws InvalidURIException if there is a problem parsing {@code uri} into a URI object.
     */
    protected String removeStateParameter(String uri)
    {
        try
        {
            return removeStateParameter(new URI(uri)).toString();
        }
        catch (URISyntaxException e)
        {
            throw new InvalidURIException(e);
        }
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
