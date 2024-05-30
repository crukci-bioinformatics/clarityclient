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

package org.cruk.clarity.api.jaxb;

import static jakarta.xml.bind.Marshaller.JAXB_ENCODING;
import static jakarta.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ClassUtils;
import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.unittests.ClarityClientTestConfiguration;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.ArtifactBatchFetchResult;
import com.genologics.ri.artifact.Demux;
import com.genologics.ri.artifactgroup.ArtifactGroup;
import com.genologics.ri.automation.Automation;
import com.genologics.ri.automation.Automations;
import com.genologics.ri.configuration.Field;
import com.genologics.ri.configuration.Type;
import com.genologics.ri.container.Container;
import com.genologics.ri.container.ContainerBatchFetchResult;
import com.genologics.ri.containertype.ContainerType;
import com.genologics.ri.controltype.ControlType;
import com.genologics.ri.controltype.ControlTypes;
import com.genologics.ri.file.ClarityFile;
import com.genologics.ri.file.ClarityFileBatchFetchResult;
import com.genologics.ri.instrument.Instrument;
import com.genologics.ri.instrumenttype.InstrumentType;
import com.genologics.ri.instrumenttype.InstrumentTypes;
import com.genologics.ri.lab.Lab;
import com.genologics.ri.permission.Permission;
import com.genologics.ri.permission.Permissions;
import com.genologics.ri.process.ClarityProcess;
import com.genologics.ri.processexecution.ExecutableProcess;
import com.genologics.ri.processtemplate.ProcessTemplate;
import com.genologics.ri.processtype.ProcessType;
import com.genologics.ri.project.Project;
import com.genologics.ri.protocolconfiguration.Protocol;
import com.genologics.ri.protocolconfiguration.Protocols;
import com.genologics.ri.queue.Queue;
import com.genologics.ri.reagentkit.ReagentKit;
import com.genologics.ri.reagentlot.ReagentLot;
import com.genologics.ri.reagenttype.ReagentType;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.role.Role;
import com.genologics.ri.role.Roles;
import com.genologics.ri.routing.Routing;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.sample.SampleBatchFetchResult;
import com.genologics.ri.sample.SampleCreation;
import com.genologics.ri.stage.Stage;
import com.genologics.ri.step.Actions;
import com.genologics.ri.step.Placements;
import com.genologics.ri.step.Pools;
import com.genologics.ri.step.ProcessState;
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.ProgramStatus;
import com.genologics.ri.step.Reagents;
import com.genologics.ri.step.StepCreation;
import com.genologics.ri.step.StepDetails;
import com.genologics.ri.step.StepSetup;
import com.genologics.ri.stepconfiguration.ProtocolStep;
import com.genologics.ri.version.Versions;
import com.genologics.ri.workflowconfiguration.Workflow;
import com.genologics.ri.workflowconfiguration.Workflows;


/**
 * This set of tests fetches one of each of the ClarityEntity classes, unmarshalls
 * marshalls it back to XML and compares the original XML with that produced by JAXB.
 * They should be equivalent (except for the namespace attributes): if they are not,
 * then something isn't annotated correctly.
 */
@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class JaxbAnnotationTest
{
    @Autowired
    protected Marshaller marshaller;

    @Autowired
    protected Unmarshaller unmarshaller;

    protected DocumentBuilder docBuilder;

    protected File exampleDirectory = new File("src/test/jaxb");

    public JaxbAnnotationTest() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        docBuilder = factory.newDocumentBuilder();
    }

    @Test
    public void testActions() throws Throwable
    {
        fetchMarshalAndCompare(Actions.class);
    }

    @Test
    public void testArtifact() throws Throwable
    {
        fetchMarshalAndCompare(Artifact.class);
    }

    @Test
    public void testArtifactBatchRetrieve() throws Throwable
    {
        fetchMarshalAndCompare(ArtifactBatchFetchResult.class);
    }

    @Test
    public void testArtifactGroup() throws Throwable
    {
        fetchMarshalAndCompare(ArtifactGroup.class);
    }

    @Test
    public void testAutomation() throws Throwable
    {
        fetchMarshalAndCompare(Automation.class);
    }

    @Test
    public void testAutomations() throws Throwable
    {
        fetchMarshalAndCompare(Automations.class);
    }

    @Test
    public void testContainer() throws Throwable
    {
        fetchMarshalAndCompare(Container.class);
    }

    @Test
    public void testContainerBatchRetrieve() throws Throwable
    {
        fetchMarshalAndCompare(ContainerBatchFetchResult.class);
    }

    @Test
    public void testContainerType() throws Throwable
    {
        fetchMarshalAndCompare(ContainerType.class);
    }

    @Test
    public void testControlTypes() throws Throwable
    {
        fetchMarshalAndCompare(ControlTypes.class);
    }

    @Test
    public void testControlType() throws Throwable
    {
        fetchMarshalAndCompare(ControlType.class);
    }

    @Test
    public void testDemux() throws Throwable
    {
        fetchMarshalAndCompare(Demux.class);
    }

    @Test
    public void testExceptionSimple() throws Throwable
    {
        Map<String, Object> marshallerProperties = new HashMap<>();
        marshallerProperties.put(JAXB_FORMATTED_OUTPUT, true);
        marshallerProperties.put(JAXB_ENCODING, "UTF-8");

        // Cannot use configured because of aspects.
        Jaxb2Marshaller exceptionMarshaller = new Jaxb2Marshaller();
        exceptionMarshaller.setPackagesToScan("com.genologics.ri.exception");
        exceptionMarshaller.setMarshallerProperties(marshallerProperties);

        Unmarshaller original = unmarshaller;
        try
        {
            unmarshaller = exceptionMarshaller;
            fetchMarshalAndCompare(com.genologics.ri.exception.Exception.class);
        }
        finally
        {
            unmarshaller = original;
        }
    }

    @Test
    public void testExceptionGuarded() throws Throwable
    {
        try
        {
            fetchMarshalAndCompare(com.genologics.ri.exception.Exception.class);
            fail("No ClarityException thrown when unmarshalling an exception.");
        }
        catch (ClarityException e)
        {
            // Correct.
            assertEquals("Something has gone very wrong.", e.getMessage(), "Message wrong");
            assertEquals("Get on the phone to Genologics.", e.getSuggestedActions(), "Suggested actions wrong");
            assertEquals("BAD", e.getCategory(), "Category wrong");
            assertEquals("BROKEN", e.getCode(), "Code wrong");
        }
    }

    @Test
    public void testExecutableProcess() throws Throwable
    {
        fetchMarshalAndCompare(ExecutableProcess.class);
    }

    @Test
    public void testField() throws Throwable
    {
        fetchMarshalAndCompare(Field.class);
    }

    @Test
    public void testFile() throws Throwable
    {
        fetchMarshalAndCompare(ClarityFile.class);
    }

    @Test
    public void testFileBatchRetrieve() throws Throwable
    {
        fetchMarshalAndCompare(ClarityFileBatchFetchResult.class);
    }

    @Test
    public void testInstrument() throws Throwable
    {
        fetchMarshalAndCompare(Instrument.class);
    }

    @Test
    public void testInstrumentType() throws Throwable
    {
        fetchMarshalAndCompare(InstrumentType.class);
    }

    @Test
    public void testInstrumentTypes() throws Throwable
    {
        fetchMarshalAndCompare(InstrumentTypes.class);
    }

    @Test
    public void testLab() throws Throwable
    {
        fetchMarshalAndCompare(Lab.class);
    }

    @Test
    public void testPermissions() throws Throwable
    {
        fetchMarshalAndCompare(Permissions.class);
    }

    @Test
    public void testPermission() throws Throwable
    {
        fetchMarshalAndCompare(Permission.class);
    }

    @Test
    public void testPlacements() throws Throwable
    {
        fetchMarshalAndCompare(Placements.class);
    }

    @Test
    public void testPools() throws Throwable
    {
        fetchMarshalAndCompare(Pools.class);
    }

    @Test
    public void testProcess() throws Throwable
    {
        fetchMarshalAndCompare(ClarityProcess.class);
    }

    @Test
    public void testProcessStep() throws Throwable
    {
        ProcessStep step = fetchMarshalAndCompare(ProcessStep.class);
        assertEquals(ProcessState.RECORD_DETAILS, step.getCurrentState(), "Process state wrong.");
    }

    @Test
    public void testProcessTemplate() throws Throwable
    {
        fetchMarshalAndCompare(ProcessTemplate.class);
    }

    @Test
    public void testProcessType() throws Throwable
    {
        fetchMarshalAndCompare(ProcessType.class);
    }

    @Test
    public void testProgramStatus() throws Throwable
    {
        fetchMarshalAndCompare(ProgramStatus.class);
    }

    @Test
    public void testProject() throws Throwable
    {
        fetchMarshalAndCompare(Project.class);
    }

    @Test
    public void testProtocols() throws Throwable
    {
        fetchMarshalAndCompare(Protocols.class);
    }

    @Test
    public void testProtocol() throws Throwable
    {
        fetchMarshalAndCompare(Protocol.class);
    }

    @Test
    public void testProtocolStep() throws Throwable
    {
        fetchMarshalAndCompare(ProtocolStep.class);
    }

    @Test
    public void testQueue() throws Throwable
    {
        fetchMarshalAndCompare(Queue.class);
    }

    @Test
    public void testReagents() throws Throwable
    {
        fetchMarshalAndCompare(Reagents.class);
    }

    @Test
    public void testReagentKit() throws Throwable
    {
        fetchMarshalAndCompare(ReagentKit.class);
    }

    @Test
    public void testReagentLot() throws Throwable
    {
        fetchMarshalAndCompare(ReagentLot.class);
    }

    @Test
    public void testReagentType() throws Throwable
    {
        fetchMarshalAndCompare(ReagentType.class);
    }

    @Test
    public void testResearcher() throws Throwable
    {
        fetchMarshalAndCompare(Researcher.class);
    }

    @Test
    public void testRole() throws Throwable
    {
        fetchMarshalAndCompare(Role.class);
    }

    @Test
    public void testRoles() throws Throwable
    {
        fetchMarshalAndCompare(Roles.class);
    }

    @Test
    public void testRouting() throws Throwable
    {
        fetchMarshalAndCompare(Routing.class);
    }

    @Test
    public void testSample() throws Throwable
    {
        fetchMarshalAndCompare(Sample.class);
    }

    @Test
    public void testSampleBatchRetrieve() throws Throwable
    {
        fetchMarshalAndCompare(SampleBatchFetchResult.class);
    }

    @Test
    public void testSampleCreation() throws Throwable
    {
        fetchMarshalAndCompare(SampleCreation.class);
    }

    @Test
    public void testStepCreation() throws Throwable
    {
        fetchMarshalAndCompare(StepCreation.class);
    }

    @Test
    public void testStepDetails() throws Throwable
    {
        fetchMarshalAndCompare(StepDetails.class);
    }

    @Test
    public void testStepSetup() throws Throwable
    {
        fetchMarshalAndCompare(StepSetup.class);
    }

    @Test
    public void testStage() throws Throwable
    {
        fetchMarshalAndCompare(Stage.class);
    }

    @Test
    public void testType() throws Throwable
    {
        fetchMarshalAndCompare(Type.class);
    }

    @Test
    public void testVersions() throws Throwable
    {
        fetchMarshalAndCompare(Versions.class);
    }

    @Test
    public void testWorkflows() throws Throwable
    {
        fetchMarshalAndCompare(Workflows.class);
    }

    @Test
    public void testWorkflow() throws Throwable
    {
        fetchMarshalAndCompare(Workflow.class);
    }


    private <T> T fetchMarshalAndCompare(Class<T> entityClass) throws Throwable
    {
        final String className = ClassUtils.getShortClassName(entityClass);

        XmlRootElement rootElementAnno = entityClass.getAnnotation(XmlRootElement.class);
        if (rootElementAnno == null)
        {
            fail(className + " has no XmlRootElement annotation");
        }

        File exampleFile = new File(exampleDirectory, className.toLowerCase() + ".xml");

        final String originalXml = FileUtils.readFileToString(exampleFile, UTF_8);

        T thing = unmarshal(entityClass, originalXml);

        StringWriter writer = new StringWriter();

        marshaller.marshal(thing, new StreamResult(writer));

        final String marshalledXml = writer.toString();

        try
        {
            Diff diff1 = new Diff(originalXml, marshalledXml);
            Diff diff2 = new XmlDiffIgnoreNamespaces(diff1);
            XMLAssert.assertXMLEqual("Remarshalled " + className + " does not match the original", diff2, true);
        }
        catch (Throwable e)
        {
            try
            {
                FileUtils.write(new File("target/" + className + "-original.xml"), originalXml, UTF_8);
                FileUtils.write(new File("target/" + className + "-marshalled.xml"), marshalledXml, UTF_8);
            }
            catch (IOException io)
            {
                // ignore.
            }

            throw e;
        }

        return thing;
    }

    private <T> T unmarshal(Class<T> entityClass, String xml) throws Throwable
    {
        try (Reader reader = new StringReader(xml))
        {
            Object unmarshalled = unmarshaller.unmarshal(new StreamSource(reader));

            if (unmarshalled instanceof JAXBElement<?> element)
            {
                unmarshalled = element.getValue();
            }

            assertEquals(entityClass, unmarshalled.getClass(), "Class of unmarshalled object doesn't match expected.");

            return entityClass.cast(unmarshalled);
        }
    }

    @SuppressWarnings("unused")
    private String reformat(String xml) throws Exception
    {
        byte[] xmlBytes = xml.getBytes("UTF8");
        Document doc = docBuilder.parse(new ByteArrayInputStream(xmlBytes));

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.toString();
    }

    @SuppressWarnings("exports")
    public static class XmlDiffIgnoreNamespaces extends DetailedDiff
    {
        public XmlDiffIgnoreNamespaces(Diff prototype)
        {
            super(prototype);
        }

        @Override
        public int differenceFound(Difference difference)
        {
            if ("number of element attributes".equals(difference.getDescription()))
            {
                Node original = difference.getControlNodeDetail().getNode();
                Node test = difference.getTestNodeDetail().getNode();

                int originalCount = countNonNamespaceAttributes(original);
                int testCount = countNonNamespaceAttributes(test);

                if (originalCount == testCount)
                {
                    return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
                }
            }
            else if ("sequence of attributes".equals(difference.getDescription()))
            {
                // Don't care about order.
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            }
            else if ("attribute name".equals(difference.getDescription()))
            {
                if ("null".equals(difference.getControlNodeDetail().getValue()) &&
                        difference.getTestNodeDetail().getValue().startsWith("xmlns:"))
                {
                    // Missing namespace attribute. Can skip this one.
                    return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
                }
            }
            else if ("text value".equals(difference.getDescription()))
            {
                if (isBlank(difference.getControlNodeDetail().getValue()) &&
                        isBlank(difference.getTestNodeDetail().getValue()))
                {
                    // Don't care about white space.
                    return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
                }
            }
            return super.differenceFound(difference);
        }

        private int countNonNamespaceAttributes(Node node)
        {
            int count = 0;
            NamedNodeMap map = node.getAttributes();
            for (int i = map.getLength() - 1; i >= 0; i--)
            {
                Node attr = map.item(i);
                if (!attr.getNodeName().startsWith("xmlns:"))
                {
                    ++count;
                }
            }
            return count;
        }
    }
}
