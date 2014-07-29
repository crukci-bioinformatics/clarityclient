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

package org.cruk.genologics.api.tools;

import static org.cruk.genologics.api.tools.GenologicsAPITools.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cruk.genologics.api.GenologicsAPI;
import org.cruk.genologics.api.unittests.UnitTestApplicationContextFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import au.com.bytecode.opencsv.CSVReader;

import com.genologics.ri.LimsEntityLinkable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.container.Container;
import com.genologics.ri.container.Placement;
import com.genologics.ri.process.GenologicsProcess;

public class GenologicsAPIToolsTest
{
    private static final Pattern artifactToSampleIdPattern = Pattern.compile("^([A-Z]{3}\\d+[A-Z]\\d+).*");

    private Log logger = LogFactory.getLog(getClass());

    protected GenologicsAPI api;
    protected GenologicsAPITools tools;

    public GenologicsAPIToolsTest() throws MalformedURLException
    {
        // Note that to try this with a real server again, just choose the proper beans
        // rather than the test beans.

        ApplicationContext context = UnitTestApplicationContextFactory.getApplicationContext();
        api = context.getBean("toolsTestAPI", GenologicsAPI.class);

        // Doesn't matter for tests, as it's not going to go to the real server anyway.
        api.setServer(new URL("http://limsdev.cruk.cam.ac.uk"));

        tools = new GenologicsAPITools();
        tools.setGenologicsAPI(api);
        tools.setBarcodeCacheFile(new File("src/test/serverexchanges/barcodes.txt"));

        /*
        api = context.getBean("genologicsAPI", GenologicsAPI.class);
        tools = context.getBean("genologicsAPITools", GenologicsAPITools.class);
        UnitTestApplicationContextFactory.setCredentialsOnApi(api);
        */
    }

    @Test
    public void testGetReagentLabelsForSamplesInPoolSLX() throws IOException
    {
        // 24-54774

        GenologicsProcess sequencingProcess = api.load("24-54774", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Artifact lane1 = api.load(flowcell.getPlacements().get(0));

        Map<String, String> sampleReagents = tools.getReagentLabelsForSamplesInPool(lane1);

        if (logger.isInfoEnabled())
        {
            for (Map.Entry<String, String> entry : sampleReagents.entrySet())
            {
                System.out.print(entry.getKey());
                System.out.print(" -> ");
                System.out.println(entry.getValue());
            }
        }

        Map<String, String> expected = loadHiseqSampleSheet("src/test/samplesheets/C26B3ACXX.csv").get("1:1");

        assertEquals("Number of samples wrong", expected.size(), sampleReagents.size());
        for (String sampleId : expected.keySet())
        {
            assertTrue("Did not find sample " + sampleId, sampleReagents.containsKey(sampleId));
            assertEquals("Mismatched reagents on sample " + sampleId, expected.get(sampleId), sampleReagents.get(sampleId));
        }
    }

    @Test
    public void testGetReagentLabelsForSamplesInPoolLPP() throws IOException
    {
        // 24-54760

        GenologicsProcess sequencingProcess = api.load("24-54760", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Artifact lane1 = api.load(flowcell.getPlacements().get(0));

        Map<String, String> sampleReagents = tools.getReagentLabelsForSamplesInPool(lane1);

        if (logger.isInfoEnabled())
        {
            for (Map.Entry<String, String> entry : sampleReagents.entrySet())
            {
                System.out.print(entry.getKey());
                System.out.print(" -> ");
                System.out.println(entry.getValue());
            }
        }

        Map<String, String> expected = loadMiseqSampleSheet("src/test/samplesheets/A4YBN.csv");

        assertEquals("Number of samples wrong", expected.size(), sampleReagents.size());
        for (String sampleId : expected.keySet())
        {
            assertTrue("Did not find sample " + sampleId, sampleReagents.containsKey(sampleId));
            assertEquals("Mismatched reagents on sample " + sampleId, expected.get(sampleId), sampleReagents.get(sampleId));
        }
    }

    @Test
    public void testGetReagentLabelsForSamplesInPoolsSLX() throws IOException
    {
        // 24-54774

        GenologicsProcess sequencingProcess = api.load("24-54774", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Map<String, Map<String, String>> flowcellMap = tools.getReagentLabelsForSamplesInPools(flowcell.getPlacements());

        if (logger.isInfoEnabled())
        {
            for (Placement p : flowcell.getPlacements())
            {
                Map<String, String> sampleReagents = flowcellMap.get(p.getLimsid());
                System.out.print(p.getWellPosition());
                System.out.print(' ');
                System.out.println(p.getLimsid());

                for (Map.Entry<String, String> entry : sampleReagents.entrySet())
                {
                    System.out.print("    ");
                    System.out.print(entry.getKey());
                    System.out.print(" -> ");
                    System.out.println(entry.getValue());
                }
            }
        }

        Map<String, Map<String, String>> hiseqInfo = loadHiseqSampleSheet("src/test/samplesheets/C25U7ACXX.csv");

        for (Placement p : flowcell.getPlacements())
        {
            Map<String, String> expected = hiseqInfo.get(p.getWellPosition());
            assertNotNull("Nothing expected found for position " + p.getWellPosition(), expected);

            Map<String, String> sampleReagents = flowcellMap.get(p.getLimsid());

            assertEquals("Number of samples wrong", expected.size(), sampleReagents.size());
            for (String sampleId : expected.keySet())
            {

                assertTrue("Did not find sample " + sampleId, sampleReagents.containsKey(sampleId));
                assertEquals("Mismatched reagents on sample " + sampleId, expected.get(sampleId), sampleReagents.get(sampleId));
            }
        }
    }

    private Map<String, String> loadMiseqSampleSheet(String file) throws IOException
    {
        CSVReader reader = new CSVReader(new FileReader(file));

        Map<String, String> expected = new HashMap<String, String>();

        boolean data = false;
        String[] line;
        while ((line = reader.readNext()) != null)
        {
            if (!data)
            {
                data = line.length == 1 && "[Data]".equals(line[0]);
                if (data)
                {
                    // One more line
                    reader.readNext();
                }
            }
            else
            {
                expected.put(line[0], line[6]);
            }
        }

        reader.close();

        return expected;
    }

    private Map<String, Map<String, String>> loadHiseqSampleSheet(String file) throws IOException
    {
        Map<String, String> reverseBarcodes = new HashMap<String, String>();
        for (Map.Entry<String, String> pair : tools.getBarcodes(true).entrySet())
        {
            if (StringUtils.isNotEmpty(pair.getValue()))
            {
                reverseBarcodes.put(pair.getValue(), pair.getKey());
            }
        }

        CSVReader reader = new CSVReader(new FileReader(file));

        Map<String, Map<String, String>> expected = new HashMap<String, Map<String, String>>();

        String[] line = reader.readNext();
        while ((line = reader.readNext()) != null)
        {
            String lane = line[1] + ":1";
            Map<String, String> laneMap = expected.get(lane);
            if (laneMap == null)
            {
                laneMap = new HashMap<String, String>();
                expected.put(lane, laneMap);
            }

            String barcodeName = reverseBarcodes.get(line[4]);
            assertNotNull("There is no barcode with the sequence " + line[4], barcodeName);

            Matcher m = artifactToSampleIdPattern.matcher(line[5]);
            assertTrue(line[5] + " is not a normal lims id", m.matches());

            laneMap.put(m.group(1), barcodeName);
        }

        reader.close();

        return expected;
    }

    @Test
    public void testGetIdentifierForArtifactSLX() throws IOException
    {
        // 24-54774

        GenologicsProcess sequencingProcess = api.load("24-54774", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Artifact lane1 = api.load(flowcell.getPlacements().get(0));

        String slxId = tools.getIdentifierForArtifact(SLX_ID_PATTERN, SLX_ID_FIELD, lane1);

        assertEquals("SLX for lane 1 of 24-54774 wrong", "SLX-7647", slxId);
    }

    @Test
    public void testGetIdentifierForArtifactLPP() throws IOException
    {
        // 24-54760

        GenologicsProcess sequencingProcess = api.load("24-54760", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Artifact lane1 = api.load(flowcell.getPlacements().get(0));

        String slxId = tools.getIdentifierForArtifact(SLX_ID_PATTERN, SLX_ID_FIELD, lane1);

        assertEquals("SLX id from lane 1 of 24-54760 wrong", "SLX-7487", slxId);
    }

    @Test
    public void testGetIdentifierForArtifactLPPDoubleSequencing() throws IOException
    {
        // 24-52025 (SLX-7645) & 24-54760 (SLX-7487)

        GenologicsProcess sequencingProcess = api.load("24-52025", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Artifact lane1 = api.load(flowcell.getPlacements().get(0));

        String slxId = tools.getIdentifierForArtifact(SLX_ID_PATTERN, SLX_ID_FIELD, lane1);

        assertEquals("SLX id from lane 1 of 24-52025 wrong", "SLX-7645", slxId);


        sequencingProcess = api.load("24-54760", GenologicsProcess.class);

        start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        lane1 = api.load(flowcell.getPlacements().get(0));

        slxId = tools.getIdentifierForArtifact(SLX_ID_PATTERN, SLX_ID_FIELD, lane1);

        assertEquals("SLX id from lane 1 of 24-54760 wrong", "SLX-7487", slxId);
    }

    @Test
    public void testGetIdentifiersForArtifactsSLX() throws IOException
    {
        // 24-54774

        GenologicsProcess sequencingProcess = api.load("24-54774", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Map<String, String> slxMap = tools.getIdentifiersForArtifacts(SLX_ID_PATTERN, SLX_ID_FIELD, flowcell.getPlacements());

        if (logger.isInfoEnabled())
        {
            for (Placement p : flowcell.getPlacements())
            {
                System.out.print(p.getWellPosition());
                System.out.print(' ');
                System.out.print(p.getLimsid());
                System.out.print(' ');
                System.out.println(slxMap.get(p.getLimsid()));
            }
        }

        assertEquals("Number of SLX ids returned wrong", 8, slxMap.size());

        assertEquals("Lane 1 SLX wrong", "SLX-7647", slxMap.get(flowcell.getPlacements().get(0).getLimsid()));
        assertEquals("Lane 2 SLX wrong", "SLX-7647", slxMap.get(flowcell.getPlacements().get(1).getLimsid()));
        assertEquals("Lane 3 SLX wrong", "SLX-7647", slxMap.get(flowcell.getPlacements().get(2).getLimsid()));
        assertEquals("Lane 4 SLX wrong", "SLX-7647", slxMap.get(flowcell.getPlacements().get(3).getLimsid()));
        assertEquals("Lane 5 SLX wrong", "SLX-6678", slxMap.get(flowcell.getPlacements().get(4).getLimsid()));
        assertEquals("Lane 6 SLX wrong", "SLX-6678", slxMap.get(flowcell.getPlacements().get(5).getLimsid()));
        assertEquals("Lane 7 SLX wrong", "SLX-6678", slxMap.get(flowcell.getPlacements().get(6).getLimsid()));
        assertEquals("Lane 8 SLX wrong", "SLX-6678", slxMap.get(flowcell.getPlacements().get(7).getLimsid()));
    }

    @Test
    public void testFilterArtifactsDerivedFromSingle()
    {
        List<Artifact> inputs = Arrays.asList(
                api.load("2-465039", Artifact.class),
                api.load("2-465038", Artifact.class),
                api.load("2-465037", Artifact.class),
                api.load("2-465036", Artifact.class),
                api.load("2-465035", Artifact.class),
                api.load("2-465041", Artifact.class),
                api.load("2-465040", Artifact.class),
                api.load("2-465042", Artifact.class),
                api.load("MOH54A49SAM1", Artifact.class),
                api.load("MOH54A46SAM1", Artifact.class),
                api.load("MOH54A45SAM1", Artifact.class),
                api.load("2-1715962", Artifact.class),
                api.load("2-1725870", Artifact.class)
            );

        List<Artifact> matchingArtifacts = new ArrayList<Artifact>(tools.filterArtifactsDerivedFrom("2-2157", inputs));
        Collections.sort(matchingArtifacts, new LimsIdComparator<Artifact>());

        assertEquals("Wrong number of child artifacts returned", 4, matchingArtifacts.size());

        assertEquals("First matching artifact wrong", "2-465035", matchingArtifacts.get(0).getLimsid());
        assertEquals("Second matching artifact wrong", "2-465036", matchingArtifacts.get(1).getLimsid());
        assertEquals("Third matching artifact wrong", "2-465037", matchingArtifacts.get(2).getLimsid());
        assertEquals("Fourth matching artifact wrong", "2-465038", matchingArtifacts.get(3).getLimsid());
    }

    @Test
    public void testFilterArtifactsDerivedFromMultiple()
    {
        List<String> parentIds = Arrays.asList("2-2157", "2-1680992", "2-1715996");

        List<Artifact> inputs = Arrays.asList(
                api.load("2-465039", Artifact.class),
                api.load("2-465038", Artifact.class),
                api.load("2-465037", Artifact.class),
                api.load("2-465036", Artifact.class),
                api.load("2-465035", Artifact.class),
                api.load("2-465041", Artifact.class),
                api.load("2-465040", Artifact.class),
                api.load("2-465042", Artifact.class),
                api.load("MOH54A49SAM1", Artifact.class),
                api.load("MOH54A46SAM1", Artifact.class),
                api.load("MOH54A45SAM1", Artifact.class),
                api.load("2-1715962", Artifact.class),
                api.load("2-1725870", Artifact.class)
            );

        Map<String, Collection<Artifact>> allMatchingArtifacts = tools.filterArtifactsDerivedFrom(parentIds, inputs);

        assertEquals("Wrong number of matching parent artifacts", 3, allMatchingArtifacts.size());

        assertTrue("Map does not contain 2-2157", allMatchingArtifacts.containsKey("2-2157"));
        assertTrue("Map does not contain 2-1680992", allMatchingArtifacts.containsKey("2-1680992"));
        assertTrue("Map does not contain 2-1715996", allMatchingArtifacts.containsKey("2-1715996"));

        List<Artifact> matchingArtifacts = new ArrayList<Artifact>(allMatchingArtifacts.get("2-2157"));
        Collections.sort(matchingArtifacts, new LimsIdComparator<Artifact>());

        assertEquals("Wrong number of child artifacts returned", 4, matchingArtifacts.size());
        assertEquals("First matching artifact wrong", "2-465035", matchingArtifacts.get(0).getLimsid());
        assertEquals("Second matching artifact wrong", "2-465036", matchingArtifacts.get(1).getLimsid());
        assertEquals("Third matching artifact wrong", "2-465037", matchingArtifacts.get(2).getLimsid());
        assertEquals("Fourth matching artifact wrong", "2-465038", matchingArtifacts.get(3).getLimsid());

        matchingArtifacts = new ArrayList<Artifact>(allMatchingArtifacts.get("2-1680992"));
        Collections.sort(matchingArtifacts, new LimsIdComparator<Artifact>());

        assertEquals("Wrong number of child artifacts returned", 1, matchingArtifacts.size());
        assertEquals("First matching artifact wrong", "2-1715962", matchingArtifacts.get(0).getLimsid());

        assertEquals("2-1715996 should be empty", 0, allMatchingArtifacts.get("2-1715996").size());
    }

    @Test
    public void testIsDerivedFrom()
    {
        Artifact a1 = api.load("2-465035", Artifact.class);
        Artifact a2 = api.load("2-1715962", Artifact.class);

        assertTrue("2-465035 should be derived from 2-2157", tools.isDerivedFrom("2-2157", a1));
        assertFalse("2-1715962 should not be derived from 2-2157", tools.isDerivedFrom("2-2157", a2));

        assertFalse("2-465035 should be derived from 2-1680992", tools.isDerivedFrom("2-1680992", a1));
        assertTrue("2-1715962 should not be derived from 2-1680992", tools.isDerivedFrom("2-1680992", a2));

    }

    @Test
    public void testGetFormativeArtifactSLX() throws IOException
    {
        // 24-54774

        GenologicsProcess sequencingProcess = api.load("24-54774", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Artifact lane1 = api.load(flowcell.getPlacements().get(0));

        Artifact formativeArtifact = tools.getFormativeArtifact(SLX_ID_PATTERN, true, lane1);

        assertNotNull("No formative artifact of " + lane1.getLimsid(), formativeArtifact);
        assertEquals("Formative artifact of " + lane1.getLimsid() + " wrong", "2-459772", formativeArtifact.getLimsid());
    }

    @Test
    public void testGetFormativeArtifactSLXNoMatch() throws IOException
    {
        Artifact start = api.retrieve("MOH54A39SAM1", Artifact.class);

        Artifact formativeArtifact = tools.getFormativeArtifact(SLX_ID_PATTERN, true, start);

        assertNotNull("No formative artifact of MOH54A39SAM1", formativeArtifact);
        assertEquals("Formative artifact of MOH54A39SAM1 wrong", "MOH54A39SAM1", formativeArtifact.getLimsid());

        formativeArtifact = tools.getFormativeArtifact(SLX_ID_PATTERN, false, start);

        assertNull("Formative artifact of MOH54A39SAM1", formativeArtifact);
    }

    @Test
    public void testGetFormativeArtifactsSLX() throws IOException
    {
        // 24-54774

        GenologicsProcess sequencingProcess = api.load("24-54774", GenologicsProcess.class);

        Artifact start = api.load(sequencingProcess.getInputOutputMaps().get(0).getInput());

        Container flowcell = api.load(start.getLocation().getContainer());

        Collections.sort(flowcell.getPlacements());

        Map<String, Artifact> formativeMap = tools.getFormativeArtifacts(SLX_ID_PATTERN, false, flowcell.getPlacements());

        if (logger.isInfoEnabled())
        {
            for (Placement p : flowcell.getPlacements())
            {
                System.out.print(p.getWellPosition());
                System.out.print(' ');
                System.out.print(p.getLimsid());
                System.out.print(' ');
                System.out.println(formativeMap.get(p.getLimsid()));
            }
        }

        assertEquals("Number of formative artifacts returned wrong", 8, formativeMap.size());

        assertNotNull("Lane 1 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(0).getLimsid()));
        assertEquals("Lane 1 formative artifact wrong", "2-459772", formativeMap.get(flowcell.getPlacements().get(0).getLimsid()).getLimsid());
        assertNotNull("Lane 2 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(1).getLimsid()));
        assertEquals("Lane 2 formative artifact wrong", "2-459772", formativeMap.get(flowcell.getPlacements().get(1).getLimsid()).getLimsid());
        assertNotNull("Lane 3 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(2).getLimsid()));
        assertEquals("Lane 3 formative artifact wrong", "2-459772", formativeMap.get(flowcell.getPlacements().get(2).getLimsid()).getLimsid());
        assertNotNull("Lane 4 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(3).getLimsid()));
        assertEquals("Lane 4 formative artifact wrong", "2-459772", formativeMap.get(flowcell.getPlacements().get(3).getLimsid()).getLimsid());
        assertNotNull("Lane 5 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(4).getLimsid()));
        assertEquals("Lane 5 formative artifact wrong", "2-2157", formativeMap.get(flowcell.getPlacements().get(4).getLimsid()).getLimsid());
        assertNotNull("Lane 6 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(5).getLimsid()));
        assertEquals("Lane 6 formative artifact wrong", "2-2157", formativeMap.get(flowcell.getPlacements().get(5).getLimsid()).getLimsid());
        assertNotNull("Lane 7 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(6).getLimsid()));
        assertEquals("Lane 7 formative artifact wrong", "2-2157", formativeMap.get(flowcell.getPlacements().get(6).getLimsid()).getLimsid());
        assertNotNull("Lane 8 formative artifact not found", formativeMap.get(flowcell.getPlacements().get(7).getLimsid()));
        assertEquals("Lane 8 formative artifact wrong", "2-2157", formativeMap.get(flowcell.getPlacements().get(7).getLimsid()).getLimsid());
    }

    private static class LimsIdComparator<T extends LimsEntityLinkable<?>> implements Comparator<T>
    {
        private Comparator<Object> nameCompare = Collator.getInstance();

        @Override
        public int compare(T o1, T o2)
        {
            return nameCompare.compare(o1.getLimsid(), o2.getLimsid());
        }
    }

}
