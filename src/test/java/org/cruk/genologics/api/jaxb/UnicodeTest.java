package org.cruk.genologics.api.jaxb;

import static com.genologics.ri.userdefined.UDF.getUDFValue;
import static com.genologics.ri.userdefined.UDF.setUDF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.cruk.genologics.api.GenologicsAPI;
import org.cruk.genologics.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.genologics.api.jaxb.JaxbAnnotationTest.XmlDiffIgnoreNamespaces;
import org.cruk.genologics.api.jaxb.UnicodeTest.UnicodeTestConfiguration;
import org.cruk.genologics.api.unittests.CRUKCICheck;
import org.cruk.genologics.api.unittests.ClarityClientTestConfiguration;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.ResourceAccessException;

import com.genologics.ri.container.Container;
import com.genologics.ri.containertype.ContainerType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnicodeTestConfiguration.class)
public class UnicodeTest
{
    /**
     * This test needs to ensure it has a "pure" configuration, where it
     * can actually talk to the server.
     */
    @Configuration
    public static class UnicodeTestConfiguration extends ClarityClientTestConfiguration
    {
    }

    @Autowired
    @Qualifier("genologicsClientHttpRequestFactory")
    protected AuthenticatingClientHttpRequestFactory httpRequestFactory;

    @Autowired
    protected Jaxb2Marshaller marshaller;

    @Autowired
    protected GenologicsAPI api;

    protected File unicodeEntityFile = new File("src/test/xml/unicode.xml");


    public UnicodeTest()
    {
    }

    @Test
    public void testRemarshallUnicode() throws Throwable
    {
        final String originalXml = FileUtils.readFileToString(unicodeEntityFile, StandardCharsets.UTF_8);

        Object unmarshalled = marshaller.unmarshal(new StreamSource(new StringReader(originalXml)));

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
                FileUtils.write(new File("target/unicode-original.xml"), originalXml, StandardCharsets.UTF_8);
                FileUtils.write(new File("target/unicode-marshalled.xml"), marshalledXml, StandardCharsets.UTF_8);
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
        Assume.assumeTrue("Could not set credentials for the API, which is needed for this test. " +
                          "Put a \"testcredentials.properties\" file on the class path.",
                          httpRequestFactory.getCredentials() != null);
    }

    @Test
    public void testPostUnicode() throws Throwable
    {
        CRUKCICheck.assumeInCrukCI();
        checkCredentialsSet();

        Object unmarshalled = marshaller.unmarshal(new StreamSource(unicodeEntityFile));
        String unicodeComment = getUDFValue(unmarshalled, "Comments");
        assertNotNull("Cannot find comment", unicodeComment);

        try
        {
            ContainerType tubeType = api.load("2", ContainerType.class);

            Container c = new Container();
            c.setContainerType(tubeType);
            c.setName("Unicode Unit Test Tube");
            c.setUserDefinedType("SLX Container");
            setUDF(c.getUserDefinedType(), "Special Sequencing Instructions", unicodeComment);

            api.create(c);
            assertNotNull("New container URI not set", c.getUri());

            try
            {
                Container copy = api.retrieve(c.getUri(), Container.class);

                String retrievedComment = getUDFValue(copy.getUserDefinedType(), "Special Sequencing Instructions", true);

                assertEquals("Original unicode text and that retrieved from the server don't match.", unicodeComment, retrievedComment);
            }
            finally
            {
                api.delete(c);
            }
        }
        catch (ResourceAccessException e)
        {
            Assume.assumeTrue(api.getServer() + " is not available.", false);
        }
    }
}
