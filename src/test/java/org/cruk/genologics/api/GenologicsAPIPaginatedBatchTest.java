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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import java.util.List;
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
import org.cruk.genologics.api.debugging.RestClientSnoopingAspect;
import org.cruk.genologics.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.genologics.api.impl.GenologicsAPIImpl;
import org.cruk.genologics.api.unittests.ClarityClientTestConfiguration;
import org.easymock.EasyMock;
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

import com.genologics.ri.LimsLink;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.sample.Samples;

@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class GenologicsAPIPaginatedBatchTest
{
    @Autowired
    GenologicsAPIImpl localApi;

    @Autowired
    Jaxb2Marshaller marshaller;

    @Autowired
    @Qualifier("genologicsRestTemplate")
    RestTemplate restTemplate;


    File[] pageFiles;
    ResponseEntity<Samples> response1, response2, response3;

    public GenologicsAPIPaginatedBatchTest()
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
    public void testMultipageFetchFind() throws Exception
    {
        // Part one - mock the rest template to return the already parsed objects.

        RestOperations restMock = EasyMock.createStrictMock(RestOperations.class);

        EasyMock.expect(restMock.getForEntity("http://lims.cri.camres.org:8080/api/v2/samples?projectname=Run 1030", Samples.class))
                .andReturn(response1).once();
        EasyMock.expect(restMock.getForEntity(new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030"), Samples.class))
                .andReturn(response2).once();
        EasyMock.expect(restMock.getForEntity(new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030"), Samples.class))
                 .andReturn(response3).once();


        localApi.setRestClient(restMock);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        EasyMock.replay(restMock);

        Map<String, Object> terms = new HashMap<String, Object>();
        terms.put("projectname", "Run 1030");

        List<LimsLink<Sample>> links = localApi.find(terms, Sample.class);

        EasyMock.verify(restMock);

        assertEquals(1150, links.size(), "Expected 1150 sample links returned");


        // Part two - mock the HTTP client and request factory to ensure that the URIs are
        // being presented as expected without character cludging.


        HttpContext httpContext = HttpClientContext.create();
        HttpClient mockHttpClient = EasyMock.createMock(HttpClient.class);
        AuthenticatingClientHttpRequestFactory mockRequestFactory = EasyMock.createMock(AuthenticatingClientHttpRequestFactory.class);

        restTemplate.setRequestFactory(mockRequestFactory);

        localApi.setHttpClient(mockHttpClient);
        localApi.setRestClient(restTemplate);

        URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?projectname=Run%201030");
        URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");
        URI pageThree = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030");

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

        EasyMock.expect(mockRequestFactory.createRequest(pageOne, HttpMethod.GET)).andReturn(reqOne).once();
        EasyMock.expect(mockRequestFactory.createRequest(pageTwo, HttpMethod.GET)).andReturn(reqTwo).once();
        EasyMock.expect(mockRequestFactory.createRequest(pageThree, HttpMethod.GET)).andReturn(reqThree).once();

        EasyMock.expect(mockHttpClient.execute(getOne, httpContext)).andReturn(responseOne).once();
        EasyMock.expect(mockHttpClient.execute(getTwo, httpContext)).andReturn(responseTwo).once();
        EasyMock.expect(mockHttpClient.execute(getThree, httpContext)).andReturn(responseThree).once();

        EasyMock.replay(mockHttpClient, mockRequestFactory);

        links = localApi.find(terms, Sample.class);

        EasyMock.verify(mockHttpClient, mockRequestFactory);

        assertEquals(1150, links.size(), "Expected 1150 sample links returned");
    }

    @Test
    public void testMultipageFetchListSome() throws Exception
    {
        // Note - reusing the files from above means that the subsequent pages
        // with have "&projectname=Run+1030" as part of the URL. This can be ignored
        // for this test.

        // Part one - mock the rest template to return the already parsed objects.

        RestOperations restMock = EasyMock.createStrictMock(RestOperations.class);

        EasyMock.expect(restMock.getForEntity("http://lims.cri.camres.org:8080/api/v2/samples?start-index=0", Samples.class))
                .andReturn(response1).once();
        EasyMock.expect(restMock.getForEntity(new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030"), Samples.class))
                .andReturn(response2).once();
        // Should get as far as response 3 in this test.

        localApi.setRestClient(restMock);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        EasyMock.replay(restMock);

        List<LimsLink<Sample>> links = localApi.listSome(Sample.class, 0, 750);

        EasyMock.verify(restMock);

        assertEquals(750, links.size(), "Expected 750 sample links returned");


        // Part two - mock the HTTP client and request factory to ensure that the URIs are
        // being presented as expected without character cludging.


        HttpContext httpContext = HttpClientContext.create();
        HttpClient mockHttpClient = EasyMock.createMock(HttpClient.class);
        ClientHttpRequestFactory mockRequestFactory = EasyMock.createMock(ClientHttpRequestFactory.class);

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

        EasyMock.expect(mockRequestFactory.createRequest(pageOne, HttpMethod.GET)).andReturn(reqOne).once();
        EasyMock.expect(mockRequestFactory.createRequest(pageTwo, HttpMethod.GET)).andReturn(reqTwo).once();

        EasyMock.expect(mockHttpClient.execute(getOne, httpContext)).andReturn(responseOne).once();
        EasyMock.expect(mockHttpClient.execute(getTwo, httpContext)).andReturn(responseTwo).once();

        EasyMock.replay(mockHttpClient, mockRequestFactory);

        links = localApi.listSome(Sample.class, 0, 750);

        EasyMock.verify(mockHttpClient, mockRequestFactory);

        assertEquals(750, links.size(), "Expected 750 sample links returned");
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
