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

import static org.junit.jupiter.api.Assertions.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.output.NullOutputStream.NULL_OUTPUT_STREAM;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.cruk.genologics.api.unittests.ClarityClientTestConfiguration;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestTemplate;

import com.genologics.ri.LimsLink;
import com.genologics.ri.Link;
import com.genologics.ri.Links;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.ArtifactBatchFetchResult;
import com.genologics.ri.artifact.ArtifactLink;

@SuppressWarnings("deprecation")
@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class GenologicsAPIBatchOperationTest
{
    private GenologicsAPI api;

    @Autowired
    @Qualifier("genologicsRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private Jaxb2Marshaller marshaller;

    public GenologicsAPIBatchOperationTest() throws MalformedURLException
    {
    }

    @Autowired
    public void setGenologicsAPI(GenologicsAPI api) throws MalformedURLException
    {
        this.api = api;
        api.setServer(new URL("http://limsdev.cri.camres.org:8080"));
    }

    @Test
    public void testArtifactBatchDuplicate() throws Exception
    {
        List<LimsLink<Artifact>> links = new ArrayList<LimsLink<Artifact>>();

        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000624")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000622?state=10101")));
        links.add(new ArtifactLink(new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/2-1000622?state=10121")));

        ClientHttpRequestFactory mockFactory = EasyMock.createMock(ClientHttpRequestFactory.class);
        restTemplate.setRequestFactory(mockFactory);

        EasyMock.replay(mockFactory);

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

        EasyMock.verify(mockFactory);
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
        String expectedReply = FileUtils.readFileToString(expectedResultFile);

        URI uri = new URI("http://limsdev.cri.camres.org:8080/api/v2/artifacts/batch/retrieve");

        // Note: need PushbackInputStream to prevent the call to getBody() being made more than once.
        // See MessageBodyClientHttpResponseWrapper.

        InputStream responseStream = new PushbackInputStream(new ByteArrayInputStream(expectedReply.getBytes()));

        HttpHeaders headers = new HttpHeaders();

        ClientHttpResponse httpResponse = EasyMock.createMock(ClientHttpResponse.class);
        EasyMock.expect(httpResponse.getStatusCode()).andReturn(HttpStatus.OK).anyTimes();
        EasyMock.expect(httpResponse.getRawStatusCode()).andReturn(HttpStatus.OK.value()).anyTimes();
        EasyMock.expect(httpResponse.getHeaders()).andReturn(headers).anyTimes();
        EasyMock.expect(httpResponse.getBody()).andReturn(responseStream).once();
        httpResponse.close();
        EasyMock.expectLastCall().once();

        ClientHttpRequest httpRequest = EasyMock.createMock(ClientHttpRequest.class);
        EasyMock.expect(httpRequest.getHeaders()).andReturn(headers).anyTimes();
        EasyMock.expect(httpRequest.getBody()).andReturn(new NullOutputStream()).times(0, 2);
        EasyMock.expect(httpRequest.execute()).andReturn(httpResponse).once();

        ClientHttpRequestFactory mockFactory = EasyMock.createStrictMock(ClientHttpRequestFactory.class);
        EasyMock.expect(mockFactory.createRequest(uri, HttpMethod.POST)).andReturn(httpRequest).once();

        restTemplate.setRequestFactory(mockFactory);

        EasyMock.replay(httpResponse, httpRequest, mockFactory);

        List<Artifact> artifacts = api.loadAll(links);

        assertEquals(links.size(), artifacts.size(), "Wrong number of artifacts");

        for (int i = 0; i < links.size(); i++)
        {
            assertTrue(artifacts.get(i).getUri().toString().startsWith(links.get(i).getUri().toString()),
                       "Artifact " + i + " wrong: " + artifacts.get(i).getUri());
        }

        EasyMock.verify(httpResponse, httpRequest, mockFactory);
    }

    @Test
    public void testArtifactBatchUpdate() throws Exception
    {
        File expectedResultFile = new File("src/test/xml/batchtestreordering-artifacts.xml");
        String expectedReply = FileUtils.readFileToString(expectedResultFile, UTF_8);

        ArtifactBatchFetchResult updateArtifactsFetch =
                (ArtifactBatchFetchResult)marshaller.unmarshal(new StreamSource(expectedResultFile));

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

        ClientHttpResponse httpResponse1 = EasyMock.createMock(ClientHttpResponse.class);
        EasyMock.expect(httpResponse1.getStatusCode()).andReturn(HttpStatus.OK).anyTimes();
        EasyMock.expect(httpResponse1.getRawStatusCode()).andReturn(HttpStatus.OK.value()).anyTimes();
        EasyMock.expect(httpResponse1.getHeaders()).andReturn(headers).anyTimes();
        EasyMock.expect(httpResponse1.getBody()).andReturn(response1Stream).once();
        httpResponse1.close();
        EasyMock.expectLastCall().once();

        ClientHttpRequest httpRequest1 = EasyMock.createMock(ClientHttpRequest.class);
        EasyMock.expect(httpRequest1.getHeaders()).andReturn(headers).anyTimes();
        EasyMock.expect(httpRequest1.getBody()).andReturn(NULL_OUTPUT_STREAM).times(0, 2);
        EasyMock.expect(httpRequest1.execute()).andReturn(httpResponse1).once();

        ClientHttpResponse httpResponse2 = EasyMock.createMock(ClientHttpResponse.class);
        EasyMock.expect(httpResponse2.getStatusCode()).andReturn(HttpStatus.OK).anyTimes();
        EasyMock.expect(httpResponse2.getRawStatusCode()).andReturn(HttpStatus.OK.value()).anyTimes();
        EasyMock.expect(httpResponse2.getHeaders()).andReturn(headers).anyTimes();
        EasyMock.expect(httpResponse2.getBody()).andReturn(response2Stream).once();
        httpResponse2.close();
        EasyMock.expectLastCall().once();

        ClientHttpRequest httpRequest2 = EasyMock.createMock(ClientHttpRequest.class);
        EasyMock.expect(httpRequest2.getHeaders()).andReturn(headers).anyTimes();
        EasyMock.expect(httpRequest2.getBody()).andReturn(NULL_OUTPUT_STREAM).times(0, 2);
        EasyMock.expect(httpRequest2.execute()).andReturn(httpResponse2).once();

        ClientHttpRequestFactory mockFactory = EasyMock.createStrictMock(ClientHttpRequestFactory.class);
        EasyMock.expect(mockFactory.createRequest(updateUri, HttpMethod.POST)).andReturn(httpRequest1).once();
        EasyMock.expect(mockFactory.createRequest(retrieveUri, HttpMethod.POST)).andReturn(httpRequest2).once();

        restTemplate.setRequestFactory(mockFactory);

        EasyMock.replay(httpResponse1, httpRequest1, httpResponse2, httpRequest2, mockFactory);

        api.updateAll(artifacts);

        assertEquals(uriOrder.size(), artifacts.size(), "Wrong number of artifacts");

        for (int i = 0; i < uriOrder.size(); i++)
        {
            assertEquals(uriOrder.get(i), artifacts.get(i).getUri(), "Artifact " + i + " wrong:");
        }

        EasyMock.verify(httpResponse2, httpRequest2, mockFactory);
    }
}
