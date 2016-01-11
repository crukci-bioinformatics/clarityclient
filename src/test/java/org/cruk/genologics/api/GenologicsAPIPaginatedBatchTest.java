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

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderGroup;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.cruk.genologics.api.debugging.RestClientSnoopingAspect;
import org.cruk.genologics.api.impl.GenologicsAPIImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.LimsLink;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.sample.Samples;

public class GenologicsAPIPaginatedBatchTest
{
    ConfigurableApplicationContext context;
    Jaxb2Marshaller marshaller;
    File[] pageFiles;
    ResponseEntity<Samples> response1, response2, response3;

    public GenologicsAPIPaginatedBatchTest() throws MalformedURLException
    {
        pageFiles = new File[] {
                new File("src/test/xml/multipagefetch-1.xml"),
                new File("src/test/xml/multipagefetch-2.xml"),
                new File("src/test/xml/multipagefetch-3.xml")
        };

        context = new ClassPathXmlApplicationContext("/org/cruk/genologics/api/genologics-client-context.xml");

        marshaller = context.getBean("genologicsJaxbMarshaller", Jaxb2Marshaller.class);


        Samples page1 = (Samples)marshaller.unmarshal(new StreamSource(pageFiles[0]));
        response1 = new ResponseEntity<Samples>(page1, HttpStatus.OK);
        Samples page2 = (Samples)marshaller.unmarshal(new StreamSource(pageFiles[1]));
        response2 = new ResponseEntity<Samples>(page2, HttpStatus.OK);
        Samples page3 = (Samples)marshaller.unmarshal(new StreamSource(pageFiles[2]));
        response3 = new ResponseEntity<Samples>(page3, HttpStatus.OK);
    }

    @Override
    protected void finalize()
    {
        context.close();
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


        GenologicsAPIImpl localApi = context.getBean("genologicsAPI", GenologicsAPIImpl.class);
        localApi.setRestClient(restMock);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        EasyMock.replay(restMock);

        Map<String, Object> terms = new HashMap<String, Object>();
        terms.put("projectname", "Run 1030");

        List<LimsLink<Sample>> links = localApi.find(terms, Sample.class);

        EasyMock.verify(restMock);

        assertEquals("Expected 1150 sample links returned", 1150, links.size());


        // Part two - mock the HTTP client and request factory to ensure that the URIs are
        // being presented as expected without character cludging.


        HttpClient mockHttpClient = EasyMock.createMock(HttpClient.class);
        ClientHttpRequestFactory mockRequestFactory = EasyMock.createMock(ClientHttpRequestFactory.class);

        RestTemplate restTemplate = context.getBean("genologicsRestTemplate", RestTemplate.class);
        restTemplate.setRequestFactory(mockRequestFactory);

        localApi.setHttpClient(mockHttpClient);
        localApi.setRestClient(restTemplate);

        URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?projectname=Run%201030");
        URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");
        URI pageThree = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=1000&projectname=Run+1030");

        GetMethod getOne = new GetMethodForMultipageFetch(pageOne.toString(), pageFiles[0]);
        GetMethod getTwo = new GetMethodForMultipageFetch(pageTwo.toString(), pageFiles[1]);
        GetMethod getThree = new GetMethodForMultipageFetch(pageThree.toString(), pageFiles[2]);

        Class<?> requestClass = Class.forName("org.springframework.http.client.CommonsClientHttpRequest");
        Constructor<?> constructor = requestClass.getDeclaredConstructor(HttpClient.class, HttpMethodBase.class);
        constructor.setAccessible(true);

        ClientHttpRequest reqOne = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getOne);
        ClientHttpRequest reqTwo = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getTwo);
        ClientHttpRequest reqThree = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getThree);

        EasyMock.expect(mockRequestFactory.createRequest(pageOne, HttpMethod.GET)).andReturn(reqOne).once();
        EasyMock.expect(mockRequestFactory.createRequest(pageTwo, HttpMethod.GET)).andReturn(reqTwo).once();
        EasyMock.expect(mockRequestFactory.createRequest(pageThree, HttpMethod.GET)).andReturn(reqThree).once();

        EasyMock.expect(mockHttpClient.executeMethod(getOne)).andReturn(HttpStatus.OK.value());
        EasyMock.expect(mockHttpClient.executeMethod(getTwo)).andReturn(HttpStatus.OK.value());
        EasyMock.expect(mockHttpClient.executeMethod(getThree)).andReturn(HttpStatus.OK.value());

        EasyMock.replay(mockHttpClient, mockRequestFactory);

        links = localApi.find(terms, Sample.class);

        EasyMock.verify(mockHttpClient, mockRequestFactory);

        assertEquals("Expected 1150 sample links returned", 1150, links.size());
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

        GenologicsAPIImpl localApi = context.getBean("genologicsAPI", GenologicsAPIImpl.class);
        localApi.setRestClient(restMock);
        localApi.setServer(new URL("http://lims.cri.camres.org:8080"));

        EasyMock.replay(restMock);

        List<LimsLink<Sample>> links = localApi.listSome(Sample.class, 0, 750);

        EasyMock.verify(restMock);

        assertEquals("Expected 750 sample links returned", 750, links.size());


        // Part two - mock the HTTP client and request factory to ensure that the URIs are
        // being presented as expected without character cludging.


        HttpClient mockHttpClient = EasyMock.createMock(HttpClient.class);
        ClientHttpRequestFactory mockRequestFactory = EasyMock.createMock(ClientHttpRequestFactory.class);

        RestTemplate restTemplate = context.getBean("genologicsRestTemplate", RestTemplate.class);
        restTemplate.setRequestFactory(mockRequestFactory);

        localApi.setHttpClient(mockHttpClient);
        localApi.setRestClient(restTemplate);

        URI pageOne = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=0");
        URI pageTwo = new URI("http://lims.cri.camres.org:8080/api/v2/samples?start-index=500&projectname=Run+1030");

        GetMethod getOne = new GetMethodForMultipageFetch(pageOne.toString(), pageFiles[0]);
        GetMethod getTwo = new GetMethodForMultipageFetch(pageTwo.toString(), pageFiles[1]);

        Class<?> requestClass = Class.forName("org.springframework.http.client.CommonsClientHttpRequest");
        Constructor<?> constructor = requestClass.getDeclaredConstructor(HttpClient.class, HttpMethodBase.class);
        constructor.setAccessible(true);

        ClientHttpRequest reqOne = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getOne);
        ClientHttpRequest reqTwo = (ClientHttpRequest)constructor.newInstance(mockHttpClient, getTwo);

        EasyMock.expect(mockRequestFactory.createRequest(pageOne, HttpMethod.GET)).andReturn(reqOne).once();
        EasyMock.expect(mockRequestFactory.createRequest(pageTwo, HttpMethod.GET)).andReturn(reqTwo).once();

        EasyMock.expect(mockHttpClient.executeMethod(getOne)).andReturn(HttpStatus.OK.value());
        EasyMock.expect(mockHttpClient.executeMethod(getTwo)).andReturn(HttpStatus.OK.value());

        EasyMock.replay(mockHttpClient, mockRequestFactory);

        links = localApi.listSome(Sample.class, 0, 750);

        EasyMock.verify(mockHttpClient, mockRequestFactory);

        assertEquals("Expected 750 sample links returned", 750, links.size());
    }


    private static class GetMethodForMultipageFetch extends GetMethod
    {
        private File responseFile;
        private HeaderGroup responseHeaders = new HeaderGroup();

        private Logger logger = LoggerFactory.getLogger(RestClientSnoopingAspect.class);

        public GetMethodForMultipageFetch(String uri, File file)
        {
            super(uri);
            responseFile = file;
            responseHeaders.addHeader(new Header("Content-type", "application/xml"));
        }

        @Override
        public int getStatusCode()
        {
            return HttpStatus.OK.value();
        }

        @Override
        protected HeaderGroup getResponseHeaderGroup()
        {
            return responseHeaders;
        }

        @Override
        public byte[] getResponseBody() throws IOException
        {
            logger.info("Returning body from file, NOT from the live API");
            return FileUtils.readFileToByteArray(responseFile);
        }

        @Override
        public InputStream getResponseBodyAsStream() throws IOException
        {
            logger.info("Returning body from file, NOT from the live API");
            return new BufferedInputStream(new FileInputStream(responseFile));
        }

        @Override
        public String getResponseBodyAsString() throws IOException
        {
            logger.info("Returning body from file, NOT from the live API");
            return FileUtils.readFileToString(responseFile, "UTF-8");
        }

    }
}
