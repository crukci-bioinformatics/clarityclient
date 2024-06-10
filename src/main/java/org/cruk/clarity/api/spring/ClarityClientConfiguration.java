package org.cruk.clarity.api.spring;

import static jakarta.xml.bind.Marshaller.JAXB_ENCODING;
import static jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.http.ClarityFailureResponseErrorHandler;
import org.cruk.clarity.api.http.HttpComponentsClientHttpRequestFactoryBasicAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Main Spring configuration for the Clarity Client.
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false)  // See the note at the bottom of the class about proxies.
@ComponentScan({"org.cruk.clarity.api.debugging",
                "org.cruk.clarity.api.http",
                "org.cruk.clarity.api.impl",
                "org.cruk.clarity.api.jaxb"})
@SuppressWarnings("exports")
public class ClarityClientConfiguration
{
    private Jaxb2Marshaller jaxb2;

    public ClarityClientConfiguration()
    {
        this(ArrayUtils.EMPTY_CLASS_ARRAY);
    }

    protected ClarityClientConfiguration(Class<?>... additionalPackageClasses)
    {
        Module module = ClarityAPI.class.getModule();
        Set<String> packages = module.getPackages().stream()
                .filter(p -> p.startsWith("com.genologics.ri"))
                .collect(Collectors.toSet());
        Stream.of(additionalPackageClasses).map(c -> c.getPackageName()).forEach(p -> packages.add(p));

        Map<String, Object> marshallerProps = new HashMap<>();
        marshallerProps.put(JAXB_FORMATTED_OUTPUT, true);
        marshallerProps.put(JAXB_ENCODING, "UTF-8");

        jaxb2 = new Jaxb2Marshaller();
        jaxb2.setPackagesToScan(packages.toArray(new String[packages.size()]));
        jaxb2.setMarshallerProperties(marshallerProps);

        // Expands the packages into classes.
        jaxb2.getJaxbContext();
        assert ObjectUtils.isNotEmpty(jaxb2.getClassesToBeBound()) : "Classes have not been found from packages.";

        // Cannot have both packages and classes set if afterPropertiesSet() is called more than once.
        jaxb2.setPackagesToScan();
    }

    public int httpConnectTimeout()
    {
        return 15000;
    }

    public int httpSocketTimeout()
    {
        return 0;
    }

    @Bean
    public DefaultSftpSessionFactory clarityFilestoreSFTPSessionFactory()
    {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setAllowUnknownKeys(true);
        return factory;
    }

    @Bean
    @SuppressWarnings("deprecation")
    public RequestConfig clarityRequestConfig()
    {
        var connectBuilder = ConnectionConfig.custom();
        connectBuilder.setConnectTimeout(httpConnectTimeout(), TimeUnit.MILLISECONDS);
        connectBuilder.setSocketTimeout(httpSocketTimeout(), TimeUnit.MILLISECONDS);

        var builder = RequestConfig.custom();
        builder.setAuthenticationEnabled(true);
        builder.setRedirectsEnabled(true);
        builder.setContentCompressionEnabled(false);
        builder.setTargetPreferredAuthSchemes(Collections.singleton("Basic"));
        builder.setConnectTimeout(httpConnectTimeout(), TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Bean
    public HttpClient clarityHttpClient()
    {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(clarityRequestConfig());
        return builder.build();
    }

    @Bean
    public CredentialsProvider clarityCredentialsProvider()
    {
        return CredentialsProviderBuilder.create().build();
    }

    @Bean
    public ClientHttpRequestFactory clarityClientHttpRequestFactory()
    {
        var factory = new HttpComponentsClientHttpRequestFactoryBasicAuth(clarityHttpClient());
        factory.setCredentialsProvider(clarityCredentialsProvider());
        // factory.setBufferRequestBody(false);
        return factory;
    }

    @Bean
    public Marshaller clarityJaxbMarshaller()
    {
        return new PassThroughInvocationHandler<>(jaxb2, Marshaller.class).createProxy();
    }

    @Bean
    public Unmarshaller clarityJaxbUnmarshaller()
    {
        return new PassThroughInvocationHandler<>(jaxb2, Unmarshaller.class).createProxy();
    }

    @Bean
    public List<Class<?>> clarityJaxbClasses()
    {
        return Arrays.asList(jaxb2.getClassesToBeBound());
    }

    @Bean
    public ResponseErrorHandler clarityExceptionErrorHandler()
    {
        return new ClarityFailureResponseErrorHandler(clarityJaxbUnmarshaller());
    }

    protected RestTemplate createRestTemplate()
    {
        var converter = new MarshallingHttpMessageConverter(clarityJaxbMarshaller(), clarityJaxbUnmarshaller());

        List<HttpMessageConverter<?>> converters = Arrays.asList(converter);

        RestTemplate template = new RestTemplate(clarityClientHttpRequestFactory());
        template.setMessageConverters(converters);
        template.setErrorHandler(clarityExceptionErrorHandler());

        return template;
    }

    @Bean
    public RestOperations clarityRestTemplate()
    {
        return createRestTemplate();
    }

    @Bean
    public RestOperations clarityFileUploadTemplate()
    {
        var converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.MULTIPART_FORM_DATA));

        List<HttpMessageConverter<?>> converters = Arrays.asList(converter);

        RestTemplate template = new RestTemplate(clarityClientHttpRequestFactory());
        template.setMessageConverters(converters);
        template.setErrorHandler(clarityExceptionErrorHandler());
        return template;
    }

    /**
     * Proxy handler for a simple pass through proxy. This is used for narrowing Jaxb2Marshaller
     * to a single interface, as it implements both Marshaller and Unmarshaller. It makes things
     * a little easier with Spring autowiring if this is separated out into objects that only
     * implement one of these interfaces. It allows the JaxbMarshaller to be created more than
     * once (which seems to be necessary, even though they are identical) without a clash of there
     * being more than one bean.
     *
     * @param <T> The interface to be proxied.
     * @param <O> An instance of an object that implements T.
     */
    private static class PassThroughInvocationHandler<T, O extends T> implements InvocationHandler
    {
        /**
         * The object proxied.
         */
        private O proxied;

        /**
         * The interface to proxy.
         */
        private Class<T> proxyInterface;

        /**
         * Constructor.
         *
         * @param thing The object to be proxied.
         * @param pi The interface to proxy.
         */
        PassThroughInvocationHandler(O thing, Class<T> pi)
        {
            proxied = thing;
            proxyInterface = pi;
        }

        /**
         * Convenience method to create a proxy for the interface that passes through
         * to the object set.
         *
         * @return The proxy.
         */
        T createProxy()
        {
            Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { proxyInterface }, this);
            return proxyInterface.cast(proxy);
        }

        /**
         * {@inheritDoc}
         */
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            return method.invoke(proxied, args);
        }
    }
}

/*
 * A note from the previous XML configuration about proxy problems.
 * These should have gone, especially as we're now always using interfaces.

    Take care not to include a bean of type
    {@code org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator}.

    This creates JDK proxies around the CGLIB proxies needed for the Jaxb2Marshaller,
    which then cannot be set as they aren't actually a type of Jaxb2Marshaller.

    This took two days to find but five seconds to fix.

    The error one sees is:

    <pre>
    java.lang.IllegalStateException: Cannot convert value of type [com.sun.proxy.$Proxy implementing ...]
    to required type [org.springframework.oxm.jaxb.Jaxb2Marshaller] for property 'jaxbMarshaller':
    no matching editors or conversion strategy found
    </pre>

    The fix is the "proxyTargetClass" attribute.
*/
