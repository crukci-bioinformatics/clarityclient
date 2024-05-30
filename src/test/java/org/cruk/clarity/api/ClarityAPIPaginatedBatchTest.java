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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import jakarta.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.Method;
import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.cruk.clarity.api.debugging.RestClientSnoopingAspect;
import org.cruk.clarity.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.clarity.api.impl.ClarityAPIImpl;
import org.cruk.clarity.api.spring.ClarityClientConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.sample.Sample;
import com.genologics.ri.sample.Samples;

/*
 * Can't use the wired standard configuration as it's all changed around with the mocks.
 */
@SpringJUnitConfig(ClarityAPIPaginatedBatchTest.MyConfiguration.class)
public class ClarityAPIPaginatedBatchTest
{
    @Autowired
    Unmarshaller unmarshaller;

    @Autowired
    @Qualifier("clarityFullRestTemplate")
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("clarityAPIImpl")
    ClarityAPIImpl localApi;

    File[] pageFiles;
    ResponseEntity<Samples> response1, response2, response3;

    public ClarityAPIPaginatedBatchTest()
    {
    }

    @PostConstruct
    public void setupAPI() throws MalformedURLException, IOException
    {
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        pageFiles = new File[] {
                new File("src/test/xml/multipagefetch-1.xml"),
                new File("src/test/xml/multipagefetch-2.xml"),
                new File("src/test/xml/multipagefetch-3.xml")
        };

        Samples page1 = (Samples)unmarshaller.unmarshal(new StreamSource(pageFiles[0]));
        response1 = new ResponseEntity<Samples>(page1, HttpStatus.OK);
        Samples page2 = (Samples)unmarshaller.unmarshal(new StreamSource(pageFiles[1]));
        response2 = new ResponseEntity<Samples>(page2, HttpStatus.OK);
        Samples page3 = (Samples)unmarshaller.unmarshal(new StreamSource(pageFiles[2]));
        response3 = new ResponseEntity<Samples>(page3, HttpStatus.OK);
    }

    @Test
    public void testMultipageFetchFind1() throws Exception
    {
        // Part one - mock the rest template to return the already parsed objects.

        RestOperations restMock = mock(RestOperations.class);

        final String uri1 = "http://lims.cri.camres.org:8080/api/v2/samples?projectname=Run 1030";
        final URI uri2 = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");
        final URI uri3 = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030");

        when(restMock.getForEntity(uri1, Samples.class)).thenReturn(response1);
        when(restMock.getForEntity(uri2, Samples.class)).thenReturn(response2);
        when(restMock.getForEntity(uri3, Samples.class)).thenReturn(response3);


        localApi.setRestClient(restMock);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        Map<String, Object> terms = new HashMap<>();
        terms.put("projectname", "Run 1030");

        var links = localApi.find(terms, Sample.class);

        assertEquals(1150, links.size(), "Expected 1150 sample links returned");
        verify(restMock, times(1)).getForEntity(uri1, Samples.class);
        verify(restMock, times(1)).getForEntity(uri2, Samples.class);
        verify(restMock, times(1)).getForEntity(uri3, Samples.class);
    }

    @Test
    public void testMultipageFetchFind2() throws Exception
    {
        // Part two - mock the HTTP client and request factory to ensure that the URIs are
        // being presented as expected without character cludging.

        Map<String, Object> terms = new HashMap<>();
        terms.put("projectname", "Run 1030");

        HttpContext httpContext = HttpClientContext.create();
        HttpClient mockHttpClient = mock(HttpClient.class);
        AuthenticatingClientHttpRequestFactory mockRequestFactory = mock(AuthenticatingClientHttpRequestFactory.class);

        restTemplate.setRequestFactory(mockRequestFactory);

        localApi.setHttpClient(mockHttpClient);
        localApi.setRestClient(restTemplate);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        final URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?projectname=Run%201030");
        final URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");
        final URI pageThree = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030");

        ClassicHttpRequest getOne = new BasicClassicHttpRequest(Method.GET, pageOne);
        ClassicHttpRequest getTwo = new BasicClassicHttpRequest(Method.GET, pageTwo);
        ClassicHttpRequest getThree = new BasicClassicHttpRequest(Method.GET, pageThree);

        ClassicHttpResponse responseOne = createMultipageFetchResponse(pageFiles[0]);
        ClassicHttpResponse responseTwo = createMultipageFetchResponse(pageFiles[1]);
        ClassicHttpResponse responseThree = createMultipageFetchResponse(pageFiles[2]);

        Class<?> requestClass = Class.forName("org.springframework.http.client.HttpComponentsClientHttpRequest");
        Constructor<?> constructor = requestClass.getDeclaredConstructor(HttpClient.class, ClassicHttpRequest.class, HttpContext.class);
        constructor.setAccessible(true);

        ClientHttpRequest reqOne = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getOne, httpContext);
        ClientHttpRequest reqTwo = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getTwo, httpContext);
        ClientHttpRequest reqThree = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getThree, httpContext);

        when(mockRequestFactory.createRequest(eq(pageOne), eq(HttpMethod.GET))).thenReturn(reqOne);
        when(mockRequestFactory.createRequest(eq(pageTwo), eq(HttpMethod.GET))).thenReturn(reqTwo);
        when(mockRequestFactory.createRequest(eq(pageThree), eq(HttpMethod.GET))).thenReturn(reqThree);

        when(mockHttpClient.executeOpen(null, getOne, httpContext)).thenReturn(responseOne);
        when(mockHttpClient.executeOpen(null, getTwo, httpContext)).thenReturn(responseTwo);
        when(mockHttpClient.executeOpen(null, getThree, httpContext)).thenReturn(responseThree);

        var links = localApi.find(terms, Sample.class);

        assertEquals(1150, links.size(), "Expected 1150 sample links returned");

        verify(mockRequestFactory, times(1)).createRequest(pageOne, HttpMethod.GET);
        verify(mockRequestFactory, times(1)).createRequest(pageTwo, HttpMethod.GET);
        verify(mockRequestFactory, times(1)).createRequest(pageThree, HttpMethod.GET);

        verify(mockHttpClient, times(1)).executeOpen(null, getOne, httpContext);
        verify(mockHttpClient, times(1)).executeOpen(null, getTwo, httpContext);
        verify(mockHttpClient, times(1)).executeOpen(null, getThree, httpContext);
    }

    @Test
    public void testMultipageFetchListSome1() throws Exception
    {
        // Note - reusing the files from above means that the subsequent pages
        // with have "&projectname=Run+1030" as part of the URL. This can be ignored
        // for this test.

        final String uri1 = "http://lims.cri.camres.org:8080/api/v2/samples?start-index=0";
        final URI uri2 = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");
        final URI uri3 = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030");

        // Part one - mock the rest template to return the already parsed objects.

        RestOperations restMock = mock(RestOperations.class);

        when(restMock.getForEntity(uri1, Samples.class)).thenReturn(response1);
        when(restMock.getForEntity(uri2, Samples.class)).thenReturn(response2);
        // Should get as far as response 3 in this test.

        localApi.setRestClient(restMock);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        var links = localApi.listSome(Sample.class, 0, 750);

        assertEquals(750, links.size(), "Expected 750 sample links returned");

        verify(restMock, times(1)).getForEntity(uri1, Samples.class);
        verify(restMock, times(1)).getForEntity(uri2, Samples.class);
        verify(restMock, never()).getForEntity(uri3, Samples.class);
    }

    @Test
    public void testMultipageFetchListSome2() throws Exception
    {
        // Part two - mock the HTTP client and request factory to ensure that the URIs are
        // being presented as expected without character cludging.

        HttpContext httpContext = HttpClientContext.create();
        HttpClient mockHttpClient = mock(HttpClient.class);
        ClientHttpRequestFactory mockRequestFactory = mock(ClientHttpRequestFactory.class);

        restTemplate.setRequestFactory(mockRequestFactory);

        localApi.setHttpClient(mockHttpClient);
        localApi.setRestClient(restTemplate);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=0");
        URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");

        ClassicHttpRequest getOne = new BasicClassicHttpRequest(Method.GET, pageOne);
        ClassicHttpRequest getTwo = new BasicClassicHttpRequest(Method.GET, pageTwo);

        ClassicHttpResponse responseOne = createMultipageFetchResponse(pageFiles[0]);
        ClassicHttpResponse responseTwo = createMultipageFetchResponse(pageFiles[1]);

        Class<?> requestClass = Class.forName("org.springframework.http.client.HttpComponentsClientHttpRequest");
        Constructor<?> constructor = requestClass.getDeclaredConstructor(HttpClient.class, ClassicHttpRequest.class, HttpContext.class);
        constructor.setAccessible(true);

        ClientHttpRequest reqOne = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getOne, httpContext);
        ClientHttpRequest reqTwo = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getTwo, httpContext);

        when(mockRequestFactory.createRequest(eq(pageOne), eq(HttpMethod.GET))).thenReturn(reqOne);
        when(mockRequestFactory.createRequest(eq(pageTwo), eq(HttpMethod.GET))).thenReturn(reqTwo);

        when(mockHttpClient.executeOpen(null, getOne, httpContext)).thenReturn(responseOne);
        when(mockHttpClient.executeOpen(null, getTwo, httpContext)).thenReturn(responseTwo);

        var links = localApi.listSome(Sample.class, 0, 750);

        assertEquals(750, links.size(), "Expected 750 sample links returned");

        verify(mockRequestFactory, times(1)).createRequest(pageOne, HttpMethod.GET);
        verify(mockRequestFactory, times(1)).createRequest(pageTwo, HttpMethod.GET);

        verify(mockHttpClient, times(1)).executeOpen(null, getOne, httpContext);
        verify(mockHttpClient, times(1)).executeOpen(null, getTwo, httpContext);
    }


    private ClassicHttpResponse createMultipageFetchResponse(File responseFile)
    {
        BasicClassicHttpResponse response = new BasicClassicHttpResponse(200, "OK");
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        response.setEntity(new HttpEntityForMultipageFetch(responseFile));
        return response;
    }

    private static class HttpEntityForMultipageFetch extends AbstractHttpEntity
    {
        private Logger logger = LoggerFactory.getLogger(RestClientSnoopingAspect.class);

        private File responseFile;

        private InputStream contentStream;

        public HttpEntityForMultipageFetch(File responseFile)
        {
            super(MediaType.APPLICATION_XML_VALUE, "UTF-8");
            this.responseFile = responseFile;
        }

        @Override
        public InputStream getContent() throws IOException
        {
            logger.debug("Returning body from file, NOT from the live API");
            contentStream = new BufferedInputStream(new FileInputStream(responseFile));
            return contentStream;
        }

        @Override
        public boolean isRepeatable()
        {
            return false;
        }

        @Override
        public long getContentLength()
        {
            return responseFile.length();
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException
        {
            IOUtils.copyLarge(getContent(), outstream);
        }

        @Override
        public boolean isStreaming()
        {
            return false;
        }

        @Override
        public void close() throws IOException
        {
            if (contentStream != null)
            {
                contentStream.close();
            }
        }
    }

    @Configuration
    static class MyConfiguration extends ClarityClientConfiguration
    {
        @Bean
        public RestTemplate clarityFullRestTemplate()
        {
            return createRestTemplate();
        }

        @Bean
        public ClarityAPIImpl clarityAPIImpl()
        {
            ClarityAPIImpl apiImpl = new ClarityAPIImpl();
            apiImpl.setRestClient(clarityFullRestTemplate());
            apiImpl.setJaxbConfig(clarityJaxbClasses());
            return apiImpl;
        }
    }
}
