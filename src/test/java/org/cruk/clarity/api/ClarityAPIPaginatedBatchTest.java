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
import static org.mockito.Mockito.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.cruk.clarity.api.debugging.RestClientSnoopingAspect;
import org.cruk.clarity.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.clarity.api.impl.ClarityAPIImpl;
import org.cruk.clarity.api.unittests.ClarityClientTestConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.sample.Sample;
import com.genologics.ri.sample.Samples;

@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class ClarityAPIPaginatedBatchTest
{
    @Autowired
    ClarityAPIImpl localApi;

    @Autowired
    Jaxb2Marshaller marshaller;

    @Autowired
    @Qualifier("clarityRestTemplate")
    RestTemplate restTemplate;


    File[] pageFiles;
    ResponseEntity<Samples> response1, response2, response3;

    public ClarityAPIPaginatedBatchTest()
    {
    }

    @PostConstruct
    public void setup()
    {
        pageFiles = new File[] {
                new File("src/test/xml/multipagefetch-1.xml"),
                new File("src/test/xml/multipagefetch-2.xml"),
                new File("src/test/xml/multipagefetch-3.xml")
        };

        Samples page1 = (Samples)marshaller.unmarshal(new StreamSource(pageFiles[0]));
        response1 = new ResponseEntity<Samples>(page1, HttpStatus.OK);
        Samples page2 = (Samples)marshaller.unmarshal(new StreamSource(pageFiles[1]));
        response2 = new ResponseEntity<Samples>(page2, HttpStatus.OK);
        Samples page3 = (Samples)marshaller.unmarshal(new StreamSource(pageFiles[2]));
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

        final URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?projectname=Run%201030");
        final URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");
        final URI pageThree = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030");

        HttpGet getOne = new HttpGet(pageOne);
        HttpGet getTwo = new HttpGet(pageOne);
        HttpGet getThree = new HttpGet(pageOne);

        HttpResponse responseOne = createMultipageFetchResponse(pageFiles[0]);
        HttpResponse responseTwo = createMultipageFetchResponse(pageFiles[1]);
        HttpResponse responseThree = createMultipageFetchResponse(pageFiles[2]);

        Class<?> requestClass = Class.forName("org.springframework.http.client.HttpComponentsClientHttpRequest");
        Constructor<?> constructor = requestClass.getDeclaredConstructor(HttpClient.class, HttpUriRequest.class, HttpContext.class);
        constructor.setAccessible(true);

        ClientHttpRequest reqOne = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getOne, httpContext);
        ClientHttpRequest reqTwo = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getTwo, httpContext);
        ClientHttpRequest reqThree = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getThree, httpContext);

        when(mockRequestFactory.createRequest(pageOne, HttpMethod.GET)).thenReturn(reqOne);
        when(mockRequestFactory.createRequest(pageTwo, HttpMethod.GET)).thenReturn(reqTwo);
        when(mockRequestFactory.createRequest(pageThree, HttpMethod.GET)).thenReturn(reqThree);

        when(mockHttpClient.execute(getOne, httpContext)).thenReturn(responseOne);
        when(mockHttpClient.execute(getTwo, httpContext)).thenReturn(responseTwo);
        when(mockHttpClient.execute(getThree, httpContext)).thenReturn(responseThree);

        var links = localApi.find(terms, Sample.class);

        assertEquals(1150, links.size(), "Expected 1150 sample links returned");

        verify(mockRequestFactory, times(1)).createRequest(pageOne, HttpMethod.GET);
        verify(mockRequestFactory, times(1)).createRequest(pageTwo, HttpMethod.GET);
        verify(mockRequestFactory, times(1)).createRequest(pageThree, HttpMethod.GET);

        verify(mockHttpClient, times(1)).execute(getOne, httpContext);
        verify(mockHttpClient, times(1)).execute(getTwo, httpContext);
        verify(mockHttpClient, times(1)).execute(getThree, httpContext);
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

        URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=0");
        URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");

        HttpGet getOne = new HttpGet(pageOne);
        HttpGet getTwo = new HttpGet(pageTwo);

        HttpResponse responseOne = createMultipageFetchResponse(pageFiles[0]);
        HttpResponse responseTwo = createMultipageFetchResponse(pageFiles[1]);

        Class<?> requestClass = Class.forName("org.springframework.http.client.HttpComponentsClientHttpRequest");
        Constructor<?> constructor = requestClass.getDeclaredConstructor(HttpClient.class, HttpUriRequest.class, HttpContext.class);
        constructor.setAccessible(true);

        ClientHttpRequest reqOne = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getOne, httpContext);
        ClientHttpRequest reqTwo = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getTwo, httpContext);

        when(mockRequestFactory.createRequest(pageOne, HttpMethod.GET)).thenReturn(reqOne);
        when(mockRequestFactory.createRequest(pageTwo, HttpMethod.GET)).thenReturn(reqTwo);

        when(mockHttpClient.execute(getOne, httpContext)).thenReturn(responseOne);
        when(mockHttpClient.execute(getTwo, httpContext)).thenReturn(responseTwo);

        var links = localApi.listSome(Sample.class, 0, 750);

        assertEquals(750, links.size(), "Expected 750 sample links returned");

        verify(mockRequestFactory, times(1)).createRequest(pageOne, HttpMethod.GET);
        verify(mockRequestFactory, times(1)).createRequest(pageTwo, HttpMethod.GET);

        verify(mockHttpClient, times(1)).execute(getOne, httpContext);
        verify(mockHttpClient, times(1)).execute(getTwo, httpContext);
    }


    private HttpResponse createMultipageFetchResponse(File responseFile)
    {
        HttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
        response.setEntity(new HttpEntityForMultipageFetch(responseFile));
        return response;
    }

    private static class HttpEntityForMultipageFetch extends AbstractHttpEntity
    {
        private Logger logger = LoggerFactory.getLogger(RestClientSnoopingAspect.class);

        private File responseFile;

        public HttpEntityForMultipageFetch(File responseFile)
        {
            this.responseFile = responseFile;
        }

        @Override
        public InputStream getContent() throws IOException
        {
            logger.info("Returning body from file, NOT from the live API");
            return new BufferedInputStream(new FileInputStream(responseFile));
        }

        @Override
        public Header getContentType()
        {
            return new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
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
    }
}
