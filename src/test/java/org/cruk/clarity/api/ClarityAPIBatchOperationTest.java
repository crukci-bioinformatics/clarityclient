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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jakarta.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.cruk.clarity.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.clarity.api.impl.ClarityAPIImpl;
import org.cruk.clarity.api.spring.ClarityClientConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Link;
import com.genologics.ri.Links;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.ArtifactBatchFetchResult;
import com.genologics.ri.artifact.ArtifactLink;

/*
 * Can't use the wired standard configuration as it's all changed around with the mocks.
 */
@SpringJUnitConfig(ClarityAPIBatchOperationTest.MyConfiguration.class)
public class ClarityAPIBatchOperationTest
{
    private ClarityAPI api;

    @Autowired
    @Qualifier("clarityClientHttpRequestFactory")
    private AuthenticatingClientHttpRequestFactory httpRequestFactory;

    @Autowired
    @Qualifier("clarityFullRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("clarityJaxbUnmarshaller")
    private Unmarshaller unmarshaller;

    @Autowired
    @Qualifier("clarityJaxbMarshaller")
    private Marshaller marshaller;

    @Autowired
    @Qualifier("clarityJaxbClasses")
    private List<Class<?>> jaxbClasses;

    public ClarityAPIBatchOperationTest()
    {
    }

    @PostConstruct
    public void setup() throws MalformedURLException
    {
        var impl = new ClarityAPIImpl();
        impl.setRestClient(restTemplate);
        impl.setHttpRequestFactory(httpRequestFactory);
        impl.setJaxbConfig(jaxbClasses);
        api = impl;

        api.setServer(new URL("http://limsdev.cri.camres.org:8080"));
    }

    @Test
    public void testArtifactBatchDuplicate() throws Exception
    {
        List<LimsLink<Artifact>> links = new ArrayList<LimsLink<Artifact>>();

        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000624")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000622?state=10101")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000622?state=10121")));

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);
        restTemplate.setRequestFactory(mockFactory);

        try
        {
            api.loadAll(links);
            fail("Fetching many artifacts with duplicate links succeeded.");
        }
        catch (IllegalArgumentException e)
        {
            if (!e.getMessage().endsWith("appears in the collection more than once."))
            {
                throw e;
            }
            // Otherwise what we expect.
        }

        verifyNoInteractions(mockFactory);
    }

    @Test
    public void testArtifactBatchFetch() throws Exception
    {
        List<LimsLink<Artifact>> links = new ArrayList<LimsLink<Artifact>>();

        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000624")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000622")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000605")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000623")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000625")));

        File expectedResultFile = new File("src/test/xml/batchtestreordering-artifacts.xml");
        String expectedReply = FileUtils.readFileToString(expectedResultFile, UTF_8);

        URI uri = new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/batch/retrieve");

        // Note: need PushbackInputStream to prevent the call to getBody() being made more than once.
        // See MessageBodyClientHttpResponseWrapper.

        InputStream responseStream = new PushbackInputStream(new ByteArrayInputStream(expectedReply.getBytes()));

        HttpHeaders headers = new HttpHeaders();

        ClientHttpResponse httpResponse = mock(ClientHttpResponse.class);
        when(httpResponse.getStatusCode()).thenReturn(HttpStatus.OK);
        when(httpResponse.getHeaders()).thenReturn(headers);
        when(httpResponse.getBody()).thenReturn(responseStream);

        ClientHttpRequest httpRequest = mock(ClientHttpRequest.class);
        when(httpRequest.getHeaders()).thenReturn(headers);
        when(httpRequest.getBody()).thenReturn(NullOutputStream.INSTANCE);
        when(httpRequest.execute()).thenReturn(httpResponse);

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);
        when(mockFactory.createRequest(uri, HttpMethod.POST)).thenReturn(httpRequest);

        restTemplate.setRequestFactory(mockFactory);

        List<Artifact> artifacts = api.loadAll(links);

        assertEquals(links.size(), artifacts.size(), "Wrong number of artifacts");

        for (int i = 0; i < links.size(); i++)
        {
            assertTrue(artifacts.get(i).getUri().toString().startsWith(links.get(i).getUri().toString()),
                       "Artifact " + i + " wrong: " + artifacts.get(i).getUri());
        }

        verify(httpResponse, times(1)).getBody();
        verify(httpResponse, times(1)).close();
        verify(httpRequest, atMost(2)).getBody();
    }

    @Test
    public void testArtifactBatchUpdate() throws Exception
    {
        File expectedResultFile = new File("src/test/xml/batchtestreordering-artifacts.xml");
        String expectedReply = FileUtils.readFileToString(expectedResultFile, UTF_8);

        ArtifactBatchFetchResult updateArtifactsFetch =
                (ArtifactBatchFetchResult)unmarshaller.unmarshal(new StreamSource(expectedResultFile));

        List<Artifact> artifacts = updateArtifactsFetch.getArtifacts();

        // Rearrange these to a different order to that in the file.
        // This means the original XML file loaded above will return in an order
        // not matching either the Links object (below) or the original order
        // of the artifacts.
        Collections.shuffle(artifacts, new Random(997));

        // Copy the URI order as it is now to make sure it doesn't differ
        // after updating the artifacts.
        List<URI> uriOrder = new ArrayList<URI>();

        Links confirmLinks = new Links();
        for (Artifact a : artifacts)
        {
            uriOrder.add(a.getUri());
            confirmLinks.getLinks().add(new Link(a));
        }

        // The Links object that (would) come back should be in a different order.
        Collections.shuffle(confirmLinks.getLinks(), new Random(1024));

        StringWriter linksXML = new StringWriter();
        marshaller.marshal(confirmLinks, new StreamResult(linksXML));

        // Two calls to make here.

        URI updateUri = new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/batch/update");
        URI retrieveUri = new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/batch/retrieve");

        // Note: need PushbackInputStream to prevent the call to getBody() being made more than once.
        // See MessageBodyClientHttpResponseWrapper.

        InputStream response1Stream = new PushbackInputStream(new ByteArrayInputStream(linksXML.toString().getBytes()));
        InputStream response2Stream = new PushbackInputStream(new ByteArrayInputStream(expectedReply.getBytes()));

        HttpHeaders headers = new HttpHeaders();

        ClientHttpResponse httpResponse1 = mock(ClientHttpResponse.class);
        when(httpResponse1.getStatusCode()).thenReturn(HttpStatus.OK);
        when(httpResponse1.getHeaders()).thenReturn(headers);
        when(httpResponse1.getBody()).thenReturn(response1Stream);

        ClientHttpRequest httpRequest1 = mock(ClientHttpRequest.class);
        when(httpRequest1.getHeaders()).thenReturn(headers);
        when(httpRequest1.getBody()).thenReturn(NullOutputStream.INSTANCE);
        when(httpRequest1.execute()).thenReturn(httpResponse1);

        ClientHttpResponse httpResponse2 = mock(ClientHttpResponse.class);
        when(httpResponse2.getStatusCode()).thenReturn(HttpStatus.OK);
        when(httpResponse2.getHeaders()).thenReturn(headers);
        when(httpResponse2.getBody()).thenReturn(response2Stream);

        ClientHttpRequest httpRequest2 = mock(ClientHttpRequest.class);
        when(httpRequest2.getHeaders()).thenReturn(headers);
        when(httpRequest2.getBody()).thenReturn(NullOutputStream.INSTANCE);
        when(httpRequest2.execute()).thenReturn(httpResponse2);

        ClientHttpRequestFactory mockFactory = mock(ClientHttpRequestFactory.class);
        when(mockFactory.createRequest(updateUri, HttpMethod.POST)).thenReturn(httpRequest1);
        when(mockFactory.createRequest(retrieveUri, HttpMethod.POST)).thenReturn(httpRequest2);

        restTemplate.setRequestFactory(mockFactory);

        api.updateAll(artifacts);

        assertEquals(uriOrder.size(), artifacts.size(), "Wrong number of artifacts");

        for (int i = 0; i < uriOrder.size(); i++)
        {
            assertEquals(uriOrder.get(i), artifacts.get(i).getUri(), "Artifact " + i + " wrong:");
        }

        verify(httpResponse1, times(1)).getBody();
        verify(httpResponse1, times(1)).close();
        verify(httpRequest1, atMost(2)).getBody();
        verify(httpRequest1, times(1)).execute();

        verify(httpResponse2, times(1)).getBody();
        verify(httpResponse2, times(1)).close();
        verify(httpRequest2, atMost(2)).getBody();
        verify(httpRequest2, times(1)).execute();

        verify(mockFactory, times(1)).createRequest(updateUri, HttpMethod.POST);
        verify(mockFactory, times(1)).createRequest(retrieveUri, HttpMethod.POST);
    }

    @Configuration
    static class MyConfiguration extends ClarityClientConfiguration
    {
        @Bean
        public RestTemplate clarityFullRestTemplate()
        {
            return createRestTemplate();
        }
    }
}
