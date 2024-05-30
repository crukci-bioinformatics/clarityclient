package org.cruk.clarity.api.jaxb;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.clarity.api.jaxb.JaxbAnnotationTest.XmlDiffIgnoreNamespaces;
import org.cruk.clarity.api.unittests.CRUKCICheck;
import org.cruk.clarity.api.unittests.ClarityClientTestConfiguration;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.ResourceAccessException;

import com.genologics.ri.container.Container;
import com.genologics.ri.containertype.ContainerType;
import com.genologics.ri.process.ClarityProcess;

@SpringJUnitConfig(classes = ClarityClientTestConfiguration.class)
public class UnicodeTest
{
    @Autowired
    @Qualifier("clarityClientHttpRequestFactory")
    protected AuthenticatingClientHttpRequestFactory httpRequestFactory;

    @Autowired
    protected Marshaller marshaller;

    @Autowired
    protected Unmarshaller unmarshaller;

    @Autowired
    protected ClarityAPI api;

    protected File unicodeEntityFile = new File("src/test/xml/unicode.xml");


    public UnicodeTest()
    {
    }

    @Test
    public void testRemarshallUnicode() throws Throwable
    {
        final String originalXml = FileUtils.readFileToString(unicodeEntityFile, UTF_8);

        ClarityProcess unmarshalled = (ClarityProcess)unmarshaller.unmarshal(new StreamSource(new StringReader(originalXml)));

        StringWriter writer = new StringWriter();

        marshaller.marshal(unmarshalled, new StreamResult(writer));

        final String marshalledXml = writer.toString();

        try
        {
            Diff diff1 = new Diff(originalXml, marshalledXml);
            Diff diff2 = new XmlDiffIgnoreNamespaces(diff1);
            XMLAssert.assertXMLEqual("Remarshalled unicode entity does not match the original", diff2, true);
        }
        catch (Throwable e)
        {
            try
            {
                FileUtils.write(new File("target/unicode-original.xml"), originalXml, UTF_8);
                FileUtils.write(new File("target/unicode-marshalled.xml"), marshalledXml, UTF_8);
            }
            catch (IOException io)
            {
                // ignore.
            }

            throw e;
        }
    }

    private void checkCredentialsSet()
    {
        assumeTrue(httpRequestFactory.getCredentials() != null,
                "Could not set credentials for the API, which is needed for this test. " +
                "Put a \"testcredentials.properties\" file on the class path.");
    }

    @Test
    public void testPostUnicode() throws Throwable
    {
        CRUKCICheck.assumeInCrukCI();
        checkCredentialsSet();

        ClarityProcess unmarshalled = (ClarityProcess)unmarshaller.unmarshal(new StreamSource(unicodeEntityFile));
        String unicodeComment = unmarshalled.getUDFValue("Comments");
        assertNotNull(unicodeComment, "Cannot find comment");

        try
        {
            ContainerType tubeType = api.load("2", ContainerType.class);

            Container c = new Container();
            c.setContainerType(tubeType);
            c.setName("Unicode Unit Test Tube");
            c.setUserDefinedType("SLX Container");
            c.getUserDefinedType().setUDF("Special Sequencing Instructions", unicodeComment);

            api.create(c);
            assertNotNull(c.getUri(), "New container URI not set");

            try
            {
                Container copy = api.retrieve(c.getUri(), Container.class);

                String retrievedComment = copy.getUserDefinedType().getUDFValue("Special Sequencing Instructions", true);

                assertEquals(unicodeComment, retrievedComment, "Original unicode text and that retrieved from the server don't match.");
            }
            finally
            {
                api.delete(c);
            }
        }
        catch (ResourceAccessException e)
        {
            assumeTrue(false, api.getServer() + " is not available.");
        }
    }
}
