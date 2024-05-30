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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.ClassUtils;
import org.cruk.clarity.api.ClarityException;
import org.cruk.clarity.api.unittests.ClarityClientTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
import com.genologics.ri.step.ProcessStep;
import com.genologics.ri.step.ProgramStatus;
import com.genologics.ri.step.ReagentLots;
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
 * it into objects, serializes it and reads the serialized object back.
 * They should be equivalent.
 */
@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class SerializationTest
{
    static final int modifierMask = Modifier.TRANSIENT | Modifier.STATIC | Modifier.FINAL;

    @Autowired
    protected Unmarshaller unmarshaller;

    protected DocumentBuilder docBuilder;

    protected File exampleDirectory = new File("src/test/jaxb");

    public SerializationTest() throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        docBuilder = factory.newDocumentBuilder();
    }

    @Test
    public void testArtifact() throws Throwable
    {
        fetchMarshalAndSerialize(Artifact.class);
    }

    @Test
    public void testArtifactBatchRetrieve() throws Throwable
    {
        fetchMarshalAndSerialize(ArtifactBatchFetchResult.class);
    }

    @Test
    public void testArtifactGroup() throws Throwable
    {
        fetchMarshalAndSerialize(ArtifactGroup.class);
    }

    @Test
    public void testAutomation() throws Throwable
    {
        fetchMarshalAndSerialize(Automation.class);
    }

    @Test
    public void testAutomations() throws Throwable
    {
        fetchMarshalAndSerialize(Automations.class);
    }

    @Test
    public void testContainer() throws Throwable
    {
        fetchMarshalAndSerialize(Container.class);
    }

    @Test
    public void testContainerBatchRetrieve() throws Throwable
    {
        fetchMarshalAndSerialize(ContainerBatchFetchResult.class);
    }

    @Test
    public void testContainerType() throws Throwable
    {
        fetchMarshalAndSerialize(ContainerType.class);
    }

    @Test
    public void testControlTypes() throws Throwable
    {
        fetchMarshalAndSerialize(ControlTypes.class);
    }

    @Test
    public void testControlType() throws Throwable
    {
        fetchMarshalAndSerialize(ControlType.class);
    }

    @Test
    public void testDemux() throws Throwable
    {
        fetchMarshalAndSerialize(Demux.class);
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
            fetchMarshalAndSerialize(com.genologics.ri.exception.Exception.class);
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
            fetchMarshalAndSerialize(com.genologics.ri.exception.Exception.class);
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
    public void testFile() throws Throwable
    {
        fetchMarshalAndSerialize(ClarityFile.class);
    }

    @Test
    public void testInstrument() throws Throwable
    {
        fetchMarshalAndSerialize(Instrument.class);
    }

    @Test
    public void testInstrumentType() throws Throwable
    {
        fetchMarshalAndSerialize(InstrumentType.class);
    }

    @Test
    public void testInstrumentTypes() throws Throwable
    {
        fetchMarshalAndSerialize(InstrumentTypes.class);
    }

    @Test
    public void testLab() throws Throwable
    {
        fetchMarshalAndSerialize(Lab.class);
    }

    @Test
    public void testProcess() throws Throwable
    {
        fetchMarshalAndSerialize(ClarityProcess.class);
    }

    @Test
    public void testExecutableProcess() throws Throwable
    {
        fetchMarshalAndSerialize(ExecutableProcess.class);
    }

    @Test
    public void testProcessTemplate() throws Throwable
    {
        fetchMarshalAndSerialize(ProcessTemplate.class);
    }

    @Test
    public void testProcessType() throws Throwable
    {
        fetchMarshalAndSerialize(ProcessType.class);
    }

    @Test
    public void testProject() throws Throwable
    {
        fetchMarshalAndSerialize(Project.class);
    }

    @Test
    public void testReagentType() throws Throwable
    {
        fetchMarshalAndSerialize(ReagentType.class);
    }

    @Test
    public void testResearcher() throws Throwable
    {
        fetchMarshalAndSerialize(Researcher.class);
    }

    @Test
    public void testField() throws Throwable
    {
        fetchMarshalAndSerialize(Field.class);
    }

    @Test
    public void testType() throws Throwable
    {
        fetchMarshalAndSerialize(Type.class);
    }

    @Test
    public void testSample() throws Throwable
    {
        fetchMarshalAndSerialize(Sample.class);
    }

    @Test
    public void testSampleCreation() throws Throwable
    {
        fetchMarshalAndSerialize(SampleCreation.class);
    }

    @Test
    public void testVersions() throws Throwable
    {
        fetchMarshalAndSerialize(Versions.class);
    }

    // Clarity 2.5 additions.

    @Test
    public void testProcessStep() throws Throwable
    {
        fetchMarshalAndSerialize(ProcessStep.class);
    }

    @Test
    public void testStepActions() throws Throwable
    {
        fetchMarshalAndSerialize(Actions.class);
    }

    @Test
    public void testPlacements() throws Throwable
    {
        fetchMarshalAndSerialize(Placements.class);
    }

    @Test
    public void testPools() throws Throwable
    {
        fetchMarshalAndSerialize(Pools.class);
    }

    @Test
    public void testProgramStatus() throws Throwable
    {
        fetchMarshalAndSerialize(ProgramStatus.class);
    }

    @Test
    public void testReagents() throws Throwable
    {
        fetchMarshalAndSerialize(Reagents.class);
    }

    @Test
    public void testProtocols() throws Throwable
    {
        fetchMarshalAndSerialize(Protocols.class);
    }

    @Test
    public void testProtocol() throws Throwable
    {
        fetchMarshalAndSerialize(Protocol.class);
    }

    @Test
    public void testProtocolStep() throws Throwable
    {
        fetchMarshalAndSerialize(ProtocolStep.class);
    }

    @Test
    public void testWorkflows() throws Throwable
    {
        fetchMarshalAndSerialize(Workflows.class);
    }

    @Test
    public void testWorkflow() throws Throwable
    {
        fetchMarshalAndSerialize(Workflow.class);
    }

    @Test
    public void testStage() throws Throwable
    {
        fetchMarshalAndSerialize(Stage.class);
    }

    @Test
    public void testRouting() throws Throwable
    {
        fetchMarshalAndSerialize(Routing.class);
    }

    // Clarity 3 additions

    @Test
    public void testFileBatchRetrieve() throws Throwable
    {
        fetchMarshalAndSerialize(ClarityFileBatchFetchResult.class);
    }

    @Test
    public void testSampleBatchRetrieve() throws Throwable
    {
        fetchMarshalAndSerialize(SampleBatchFetchResult.class);
    }

    @Test
    public void testReagentKit() throws Throwable
    {
        fetchMarshalAndSerialize(ReagentKit.class);
    }

    @Test
    public void testReagentLot() throws Throwable
    {
        fetchMarshalAndSerialize(ReagentLot.class);
    }

    @Test
    public void testStepDetails() throws Throwable
    {
        fetchMarshalAndSerialize(StepDetails.class);
    }

    @Test
    public void testStepSetup() throws Throwable
    {
        fetchMarshalAndSerialize(StepSetup.class);
    }

    @Test
    public void testReagentLots() throws Throwable
    {
        fetchMarshalAndSerialize(ReagentLots.class);
    }

    @Test
    public void testStepCreation() throws Throwable
    {
        fetchMarshalAndSerialize(StepCreation.class);
    }

    // Clarity 3.1 additions

    @Test
    public void testPermissions() throws Throwable
    {
        fetchMarshalAndSerialize(Permissions.class);
    }

    @Test
    public void testPermission() throws Throwable
    {
        fetchMarshalAndSerialize(Permission.class);
    }

    @Test
    public void testQueue() throws Throwable
    {
        fetchMarshalAndSerialize(Queue.class);
    }

    @Test
    public void testRoles() throws Throwable
    {
        fetchMarshalAndSerialize(Roles.class);
    }

    @Test
    public void testRole() throws Throwable
    {
        fetchMarshalAndSerialize(Role.class);
    }


    private void fetchMarshalAndSerialize(Class<?> entityClass) throws Throwable
    {
        final String className = ClassUtils.getShortClassName(entityClass);

        XmlRootElement rootElementAnno = entityClass.getAnnotation(XmlRootElement.class);
        if (rootElementAnno == null)
        {
            fail(className + " has no XmlRootElement annotation");
        }

        File exampleFile = new File(exampleDirectory, className.toLowerCase() + ".xml");

        Object unmarshalled = unmarshaller.unmarshal(new StreamSource(new FileReader(exampleFile)));

        // In the case where the simple serialisation test for the exception
        // hasn't got the unmarshalling aspect around it.
        // See JaxbUnmarshallingAspect.

        if (unmarshalled instanceof JAXBElement<?> element)
        {
            unmarshalled = element.getValue();
        }

        ByteArrayOutputStream bstream = new ByteArrayOutputStream(65536);
        try
        {
            ObjectOutputStream ostream = new ObjectOutputStream(bstream);
            ostream.writeObject(unmarshalled);
            ostream.close();
        }
        catch (NotSerializableException e)
        {
            fail(e.getMessage() + " is not serializable.");
        }

        ObjectInputStream istream = new ObjectInputStream(new ByteArrayInputStream(bstream.toByteArray()));
        Object deserialized = istream.readObject();

        compareObjects(unmarshalled, deserialized);
    }

    private void compareObjects(Object original, Object serialized) throws Throwable
    {
        assertNotNull(original, "Original object is null");
        assertNotNull(serialized, "Serialized object is null");

        assertEquals(original.getClass(), serialized.getClass(), "Object classes are not the same class");

        Class<?> objClass = original.getClass();

        List<java.lang.reflect.Field> fields = new ArrayList<java.lang.reflect.Field>(100);
        Class<?> current = objClass;
        while (!current.equals(Object.class))
        {
            for (java.lang.reflect.Field f : current.getDeclaredFields())
            {
                if ((f.getModifiers() & modifierMask) == 0)
                {
                    f.setAccessible(true);
                    fields.add(f);
                }
            }
            current = current.getSuperclass();
        }

        for (java.lang.reflect.Field f : fields)
        {
            String className = ClassUtils.getShortClassName(f.getDeclaringClass());

            Object ovalue = f.get(original);
            Object svalue = f.get(serialized);

            if (ovalue == null)
            {
                assertNull(svalue,
                        "Field " + f.getName() + " in " + className +
                        " is null in the original but is not null in the deserialised.");
            }
            else
            {
                assertNotNull(svalue,
                        "Field " + f.getName() + " in " + className +
                        " is not null in the original but is null in the deserialised.");
            }

            if (Collection.class.isAssignableFrom(f.getType()))
            {
                if (ovalue != null)
                {
                    Collection<?> ocoll = (Collection<?>)ovalue;
                    Collection<?> scoll = (Collection<?>)svalue;

                    assertEquals(ocoll.size(), scoll.size(),
                            "Collection field " + f.getName() + " has different content size");

                    Iterator<?> oiter = ocoll.iterator();
                    Iterator<?> siter = scoll.iterator();
                    int index = 0;
                    while (oiter.hasNext())
                    {
                        Object o = oiter.next();
                        Object s = siter.next();

                        if (o.getClass().getName().startsWith("com.genologics.ri"))
                        {
                            compareObjects(o, s);
                        }
                        else
                        {
                            assertEquals(o, s,
                                    "Object[" + index + "] in collection " + f.getName() + " in " + className + " are not the same");
                        }

                        ++index;
                    }
                }
            }
            else
            {
                if (ovalue != null)
                {
                    if (f.getType().getName().startsWith("com.genologics.ri") &&
                            !Enum.class.isAssignableFrom(f.getType()))
                    {
                        compareObjects(ovalue, svalue);
                    }
                    else
                    {
                        assertEquals(ovalue, svalue,
                                "Objects in field " + f.getName() + " in " + className + " are not the same");
                    }
                }
            }
        }
    }
}
