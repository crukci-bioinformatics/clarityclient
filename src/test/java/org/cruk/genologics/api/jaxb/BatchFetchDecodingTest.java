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

package org.cruk.genologics.api.jaxb;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import org.cruk.genologics.api.unittests.ClarityClientTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.genologics.ri.artifact.ArtifactBatchFetchResult;
import com.genologics.ri.container.ContainerBatchFetchResult;
import com.genologics.ri.file.GenologicsFileBatchFetchResult;
import com.genologics.ri.sample.SampleBatchFetchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ClarityClientTestConfiguration.class)
public class BatchFetchDecodingTest
{
    @Autowired
    protected Jaxb2Marshaller marshaller;

    public BatchFetchDecodingTest()
    {
    }

    @Test
    public void testDecodeBatchFetchArtifactResult()
    {
        File resultFile = new File("src/test/xml/batchfetchdecodingtest-artifacts.xml");

        Object result = marshaller.unmarshal(new StreamSource(resultFile));

        assertEquals("Decoded result is unexpected", ArtifactBatchFetchResult.class, result.getClass());

        ArtifactBatchFetchResult fetch = (ArtifactBatchFetchResult)result;

        assertEquals("Wrong number of artifacts decoded", 5, fetch.getSize());
    }

    @Test
    public void testDecodeBatchFetchContainerResult()
    {
        File resultFile = new File("src/test/xml/batchfetchdecodingtest-containers.xml");

        Object result = marshaller.unmarshal(new StreamSource(resultFile));

        assertEquals("Decoded result is unexpected", ContainerBatchFetchResult.class, result.getClass());

        ContainerBatchFetchResult fetch = (ContainerBatchFetchResult)result;

        assertEquals("Wrong number of containers decoded", 5, fetch.getSize());
    }

    @Test
    public void testDecodeBatchFetchSampleResult()
    {
        File resultFile = new File("src/test/xml/batchfetchdecodingtest-samples.xml");

        Object result = marshaller.unmarshal(new StreamSource(resultFile));

        assertEquals("Decoded result is unexpected", SampleBatchFetchResult.class, result.getClass());

        SampleBatchFetchResult fetch = (SampleBatchFetchResult)result;

        assertEquals("Wrong number of samples decoded", 4, fetch.getSize());
    }

    @Test
    public void testDecodeBatchFetchGenologicsFileResult()
    {
        File resultFile = new File("src/test/xml/batchfetchdecodingtest-files.xml");

        Object result = marshaller.unmarshal(new StreamSource(resultFile));

        assertEquals("Decoded result is unexpected", GenologicsFileBatchFetchResult.class, result.getClass());

        GenologicsFileBatchFetchResult fetch = (GenologicsFileBatchFetchResult)result;

        assertEquals("Wrong number of files decoded", 7, fetch.getSize());
    }

}
