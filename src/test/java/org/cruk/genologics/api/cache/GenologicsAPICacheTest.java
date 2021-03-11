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

package org.cruk.genologics.api.cache;

import static org.cruk.genologics.api.cache.GenologicsAPICache.NO_STATE_VALUE;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.cruk.genologics.api.GenologicsAPI;
import org.cruk.genologics.api.StatefulOverride;
import org.cruk.genologics.api.http.AuthenticatingClientHttpRequestFactory;
import org.cruk.genologics.api.unittests.CRUKCICheck;
import org.cruk.genologics.api.unittests.ClarityClientCacheTestConfiguration;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.genologics.ri.LimsEntity;
import com.genologics.ri.LimsLink;
import com.genologics.ri.Locatable;
import com.genologics.ri.artifact.Artifact;
import com.genologics.ri.artifact.OutputType;
import com.genologics.ri.artifact.QCFlag;
import com.genologics.ri.container.Container;
import com.genologics.ri.containertype.ContainerType;
import com.genologics.ri.containertype.ContainerTypeLink;
import com.genologics.ri.file.GenologicsFile;
import com.genologics.ri.process.GenologicsProcess;
import com.genologics.ri.processexecution.ExecutableInputOutputMap;
import com.genologics.ri.processexecution.ExecutableProcess;
import com.genologics.ri.processtype.ProcessType;
import com.genologics.ri.processtype.ProcessTypeAttribute;
import com.genologics.ri.processtype.ProcessTypeLink;
import com.genologics.ri.project.Project;
import com.genologics.ri.project.ResearcherLink;
import com.genologics.ri.researcher.Researcher;
import com.genologics.ri.sample.Sample;
import com.genologics.ri.userdefined.UDF;

import net.sf.ehcache.Element;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ClarityClientCacheTestConfiguration.class)
public class GenologicsAPICacheTest
{
    /**
     * Flag that turns on the full test. This is needed to be explicitly turned on
     * to run against the development server here at CRUK-CI as it makes updates.
     * That test will not run anywhere else.
     *
     * @see #fullTest()
     * @see #readonlyTest()
     */
    public static final String FULL_TEST_SYSTEM_PROPERTY = "live.cache.test";

    /**
     * Need an update key store (trust store) for the tests to work under Java 6.
     */
    private static final File KEYSTORE_FILE = new File("java6-cacerts");

    protected final boolean newerThanJDK6;

    protected Logger logger = LoggerFactory.getLogger(GenologicsAPICacheTest.class);

    @Autowired
    @Qualifier("genologicsClientHttpRequestFactory")
    protected AuthenticatingClientHttpRequestFactory httpRequestFactory;

    @Autowired
    protected GenologicsAPI api;

    @Autowired
    protected GenologicsAPICache cacheAspect;

    @Autowired
    protected RestCallTrackingAspect testAspect;

    private Researcher apiUser;
    private ContainerType plateType;
    private ContainerType tubeType;

    private Project project;
    private Container container;
    private Sample[] samples;
    private Container poolContainer;

    public GenologicsAPICacheTest()
    {
        int spec = Math.round(Float.parseFloat(System.getProperty("java.vm.specification.version")) * 10f);
        newerThanJDK6 = spec > 16;
    }

    @After
    public void cleanup()
    {
        testAspect.setEnabled(false);
        cacheAspect.setStatefulBehaviour(CacheStatefulBehaviour.LATEST);
        cacheAspect.clear();

        if (project != null)
        {
            logger.error("Need to delete {} through operations.", project.getUri());
        }
    }

    @Test
    public void testCreateCacheElement() throws URISyntaxException
    {
        Locatable l = new Artifact();

        String base = "http://limsdev.cri.camres.org:8080/v2/api/";

        l.setUri(new URI(base + "artifacts/234"));

        Element e = cacheAspect.createCacheElement(l);

        assertEquals("Version wrong without state", NO_STATE_VALUE, e.getVersion());

        l.setUri(new URI(base + "artifacts/56-362?name=thing"));

        e = cacheAspect.createCacheElement(l);

        assertEquals("Version wrong with query without state", NO_STATE_VALUE, e.getVersion());

        l.setUri(new URI(base + "artifacts/56-362?state=5432"));

        e = cacheAspect.createCacheElement(l);

        assertEquals("Version wrong with state", 5432L, e.getVersion());
    }

    private void checkCredentialsSet()
    {
        Assume.assumeTrue("Could not set credentials for the API, which is needed for this test. " +
                          "Put a \"testcredentials.properties\" file on the class path.",
                          httpRequestFactory.getCredentials() != null);
    }

    /**
     * Create a mock for Signature that expects getName() zero or one times.
     * The times are for whether debugging is on or off.
     *
     * @return The Signature mock.
     *
     * @see <a href="http://stackoverflow.com/questions/3100859/easymock-how-to-reset-mock-but-maintain-expectations">Stack overflow issue</a>
     */
    private Signature createSignatureMock()
    {
        Signature jpSig = EasyMock.createMock(Signature.class);
        EasyMock.expect(jpSig.getName()).andReturn("retrieve").times(0, 1);
        return jpSig;
    }

    @Test
    public void testLoadOrRetrieveLatest() throws Throwable
    {
        checkCredentialsSet();

        cacheAspect.setStatefulBehaviour(CacheStatefulBehaviour.LATEST);

        //CacheManager mockCacheManager = EasyMock.createMock(CacheManager.class);
        //EasyMock.expect(mockCacheManager.getCache(Artifact.class.getName())).andReturn(value);

        cacheAspect.getCache(Artifact.class).removeAll();

        String base = api.getServerApiAddress();

        Signature jpSig = createSignatureMock();

        Artifact a1 = new Artifact();
        a1.setUri(new URI(base + "/artifacts/2-1771911?state=1294907"));
        a1.setLimsid("2-1771911");

        Object[] a1args = { a1.getUri(), a1.getClass() };

        ProceedingJoinPoint pjp1 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp1.getArgs()).andReturn(a1args).times(3);
        EasyMock.expect(pjp1.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp1.proceed()).andReturn(a1).once();

        EasyMock.replay(pjp1, jpSig);

        Object returned = cacheAspect.retrieve(pjp1);

        EasyMock.verify(pjp1, jpSig);
        assertSame("Did not return a1", a1, returned);

        Artifact a2 = new Artifact();
        a2.setUri(new URI(base + "/artifacts/2-1771911?state=1500000"));
        a2.setLimsid("2-1771911");

        Object[] a2args = { a2.getUri(), a2.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp2 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp2.getArgs()).andReturn(a2args).times(3);
        EasyMock.expect(pjp2.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp2.proceed()).andReturn(a2).once();

        EasyMock.replay(pjp2, jpSig);

        returned = cacheAspect.retrieve(pjp2);
        assertSame("Did not return a2", a2, returned);

        EasyMock.verify(pjp2, jpSig);

        // With an earlier state, expect the later version to be returned regardless.

        Object[] a3args = { base + "/artifacts/2-1771911?state=1101002", a1.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp3 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp3.getArgs()).andReturn(a3args).times(3);

        EasyMock.replay(pjp3, jpSig);

        returned = cacheAspect.retrieve(pjp3);

        EasyMock.verify(pjp3, jpSig);
        assertSame("Did not return a2", a2, returned);

        // With no state, expect whichever version is in the cache.

        Object[] a4args = { base + "/artifacts/2-1771911", Artifact.class };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp4 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp4.getArgs()).andReturn(a4args).times(3);

        EasyMock.replay(pjp4, jpSig);

        returned = cacheAspect.retrieve(pjp4);

        EasyMock.verify(pjp4, jpSig);
        assertSame("Did not return a2", a2, returned);
    }

    @Test
    public void testLoadOrRetrieveExact() throws Throwable
    {
        checkCredentialsSet();

        cacheAspect.setStatefulBehaviour(CacheStatefulBehaviour.EXACT);
        cacheAspect.getCache(Artifact.class).removeAll();

        String base = api.getServerApiAddress();

        Signature jpSig = createSignatureMock();

        Artifact a1 = new Artifact();
        a1.setUri(new URI(base + "/artifacts/2-1771911?state=1294907"));
        a1.setLimsid("2-1771911");

        Object[] a1args = { a1.getUri(), a1.getClass() };

        ProceedingJoinPoint pjp1 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp1.getArgs()).andReturn(a1args).times(3);
        EasyMock.expect(pjp1.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp1.proceed()).andReturn(a1).once();

        EasyMock.replay(pjp1, jpSig);

        Object returned = cacheAspect.retrieve(pjp1);

        EasyMock.verify(pjp1, jpSig);
        assertSame("Did not return a1", a1, returned);

        Artifact a2 = new Artifact();
        a2.setUri(new URI(base + "/artifacts/2-1771911?state=1500000"));
        a2.setLimsid("2-1771911");

        Object[] a2args = { a2.getUri(), a2.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp2 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp2.getArgs()).andReturn(a2args).times(3);
        EasyMock.expect(pjp2.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp2.proceed()).andReturn(a2).once();

        EasyMock.replay(pjp2, jpSig);

        returned = cacheAspect.retrieve(pjp2);
        assertSame("Did not return a2", a2, returned);

        EasyMock.verify(pjp2, jpSig);

        // In exact, this request for an earlier version will require another fetch.

        Artifact a3 = new Artifact();
        a3.setUri(new URI(base + "/artifacts/2-1771911?state=1101002"));
        a3.setLimsid("2-1771911");

        Object[] a3args = { a3.getUri(), a3.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp3 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp3.getArgs()).andReturn(a3args).times(3);
        EasyMock.expect(pjp3.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp3.proceed()).andReturn(a3).once();

        EasyMock.replay(pjp3, jpSig);

        returned = cacheAspect.retrieve(pjp3);

        EasyMock.verify(pjp3, jpSig);
        assertSame("Did not return a3", a3, returned);

        // With no state, expect whichever version is in the cache.

        Object[] a4args = { base + "/artifacts/2-1771911", Artifact.class };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp4 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp4.getArgs()).andReturn(a4args).times(3);

        EasyMock.replay(pjp4, jpSig);

        returned = cacheAspect.retrieve(pjp4);

        EasyMock.verify(pjp4, jpSig);
        assertSame("Did not return a3", a3, returned);
    }

    @Test
    public void testLoadOrRetrieveAny() throws Throwable
    {
        checkCredentialsSet();

        cacheAspect.setStatefulBehaviour(CacheStatefulBehaviour.ANY);
        cacheAspect.getCache(Artifact.class).removeAll();

        String base = api.getServerApiAddress();

        Signature jpSig = createSignatureMock();

        Artifact a1 = new Artifact();
        a1.setUri(new URI(base + "/artifacts/2-1771911?state=1294907"));
        a1.setLimsid("2-1771911");

        Object[] a1args = { a1.getUri(), a1.getClass() };

        ProceedingJoinPoint pjp1 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp1.getArgs()).andReturn(a1args).times(3);
        EasyMock.expect(pjp1.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp1.proceed()).andReturn(a1).once();

        EasyMock.replay(pjp1, jpSig);

        Object returned = cacheAspect.retrieve(pjp1);

        EasyMock.verify(pjp1, jpSig);
        assertSame("Did not return a1", a1, returned);

        // Asking for a later state in ANY mode will just return what it has.

        Object[] a2args = { base + "/artifacts/2-1771911?state=1500000", a1.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp2 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp2.getArgs()).andReturn(a2args).times(3);

        EasyMock.replay(pjp2, jpSig);

        returned = cacheAspect.retrieve(pjp2);
        assertSame("Did not return a1", a1, returned);

        EasyMock.verify(pjp2, jpSig);

        // With an earlier state, it'll be the same object again.

        Object[] a3args = { base + "/artifacts/2-1771911?state=1101002", a1.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp3 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp3.getArgs()).andReturn(a3args).times(3);

        EasyMock.replay(pjp3, jpSig);

        returned = cacheAspect.retrieve(pjp3);

        EasyMock.verify(pjp3, jpSig);
        assertSame("Did not return a1", a1, returned);

        // With no state, expect whichever version is in the cache.

        Object[] a4args = { base + "/artifacts/2-1771911", Artifact.class };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp4 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp4.getArgs()).andReturn(a4args).times(3);

        EasyMock.replay(pjp4, jpSig);

        returned = cacheAspect.retrieve(pjp4);

        EasyMock.verify(pjp4, jpSig);
        assertSame("Did not return a1", a1, returned);
    }

    @Test
    public void testLoadOrRetrieveWithOverride() throws Throwable
    {
        checkCredentialsSet();

        cacheAspect.setStatefulBehaviour(CacheStatefulBehaviour.LATEST);
        cacheAspect.getCache(Artifact.class).removeAll();

        String base = api.getServerApiAddress();

        Signature jpSig = createSignatureMock();

        Artifact a1 = new Artifact();
        a1.setUri(new URI(base + "/artifacts/2-1771911?state=1294907"));
        a1.setLimsid("2-1771911");
        a1.setQCFlag(QCFlag.FAILED);

        Object[] a1args = { a1.getUri(), a1.getClass() };

        ProceedingJoinPoint pjp1 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp1.getArgs()).andReturn(a1args).times(3);
        EasyMock.expect(pjp1.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp1.proceed()).andReturn(a1).once();

        EasyMock.replay(pjp1, jpSig);

        Object returned = cacheAspect.retrieve(pjp1);

        EasyMock.verify(pjp1, jpSig);
        assertSame("Did not return a1", a1, returned);

        // With an earlier state, it would normally return the same object.

        Object[] a2args = { base + "/artifacts/2-1771911?state=1101002", a1.getClass() };

        jpSig = createSignatureMock();
        ProceedingJoinPoint pjp2 = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp2.getArgs()).andReturn(a2args).times(3);

        EasyMock.replay(pjp2, jpSig);

        returned = cacheAspect.retrieve(pjp2);

        EasyMock.verify(pjp2, jpSig);
        assertSame("Did not return a1", a1, returned);

        // With an override, we should get an equivalent object back but with a different state.

        Artifact a3 = new Artifact();
        a3.setUri(new URI(base + "/artifacts/2-1771911?state=1101002"));
        a3.setLimsid("2-1771911");
        a3.setQCFlag(QCFlag.PASSED);

        Object[] a3bargs = { base + "/artifacts/2-1771911?state=1101002", a1.getClass() };

        jpSig = createSignatureMock();

        ProceedingJoinPoint pjp3b = EasyMock.createStrictMock(ProceedingJoinPoint.class);
        EasyMock.expect(pjp3b.getArgs()).andReturn(a3bargs).times(3);
        EasyMock.expect(pjp3b.getSignature()).andReturn(jpSig).times(0, 1);
        EasyMock.expect(pjp3b.proceed()).andReturn(a3).once();

        JoinPoint pjp3c = EasyMock.createStrictMock(JoinPoint.class);
        EasyMock.expect(pjp3c.getSignature()).andReturn(jpSig).once();

        EasyMock.replay(pjp3b, pjp3c, jpSig);

        api.overrideStateful(StatefulOverride.LATEST);
        returned = cacheAspect.retrieve(pjp3b);
        cacheAspect.cancelStatefulOverride(pjp3c);

        // If this fails, it can be because a method that should not cancel the
        // override is not listed in LatestVersionsResetAspect.NO_RESET_METHODS.
        // Add manually if it's not in GenologicsAPIInternal.

        EasyMock.verify(pjp3b, pjp3c, jpSig);
        assertSame("Did not return a3", a3, returned);
    }


    /**
     * This test talks to the CRUK-CI development server to check things are working
     * against a real installation. It won't work elsewhere.
     */
    @Test
    public void readonlyTest() throws Exception
    {
        CRUKCICheck.assumeInCrukCI();
        Assume.assumeTrue("No updated key store available. HTTPS connections will not work, so neither will this test.", newerThanJDK6 || KEYSTORE_FILE.exists());

        checkCredentialsSet();

        // Load reference links

        testAspect.setEnabled(true);

        String researcherURI = api.getServerApiAddress() + "researchers/3";
        String plateURI = api.getServerApiAddress() + "containertypes/1";
        String tubeURI = api.getServerApiAddress() + "containertypes/2";

        testAspect.setAllowedUris(researcherURI, plateURI, tubeURI);

        ResearcherLink researcherLink = new ResearcherLink(new URI(researcherURI));
        apiUser = api.load(researcherLink);

        assertEquals("API user wrong", "apiuser", apiUser.getCredentials().getUsername());

        plateType = api.load(new ContainerTypeLink(new URI(plateURI)));

        assertEquals("Plate type wrong", "96 well plate", plateType.getName());

        tubeType = api.load("2", ContainerType.class);

        assertEquals("Tube type wrong", "Tube", tubeType.getName());

        testAspect.clear();

        apiUser = api.load("3", Researcher.class);
        plateType = api.retrieve(plateURI, ContainerType.class);

    }

    /**
     * This test talks to the CRUK-CI development server to check things are working
     * against a real installation. It won't work elsewhere.
     * It also modifies the database, and so is controlled by the system property
     * "live.cache.test".
     */
    @Test
    public void fullTest() throws Exception
    {
        CRUKCICheck.assumeInCrukCI();
        Assume.assumeTrue("No updated key store available. HTTPS connections will not work, so neither will this test.", newerThanJDK6 || KEYSTORE_FILE.exists());

        checkCredentialsSet();

        boolean runThisTest = Boolean.parseBoolean(System.getProperty(FULL_TEST_SYSTEM_PROPERTY, Boolean.FALSE.toString()));

        Assume.assumeTrue("Not running the test \"GenologicsAPICachingAspectTest.fullTest\". " +
                          "Set the property -D" + FULL_TEST_SYSTEM_PROPERTY + "=true to make it run.",
                          runThisTest);

        ProcessType poolProcessType = null;
        List<LimsLink<ProcessType>> ptLinks = api.listAll(ProcessType.class);
        for (LimsLink<ProcessType> link : ptLinks)
        {
            ProcessTypeLink ptLink = (ProcessTypeLink)link;
            if (ptLink.getName().equals("Pool Accepted SLX"))
            {
                poolProcessType = api.load(ptLink);
                break;
            }
        }

        assertNotNull("Cannot locate 'Pool Accepted SLX' process type", poolProcessType);

        boolean poolingEnabled = false;
        for (ProcessTypeAttribute pta : poolProcessType.getProcessTypeAttributes())
        {
            if (pta.getName().equalsIgnoreCase("enabled"))
            {
                poolingEnabled = Boolean.parseBoolean(pta.getValue());
                break;
            }
        }
        assertTrue("'Pool Accepted SLX' process type is disabled. Turn it on on dev to run this test.", poolingEnabled);

        final String projectName = "Caching Aspect Test";

        if (apiUser == null || plateType == null)
        {
            readonlyTest();
        }

        // Find or create project

        Map<String, String> projectSearch = Collections.singletonMap("name", projectName);

        List<LimsLink<Project>> foundProjects = api.find(projectSearch, Project.class);

        if (foundProjects.isEmpty())
        {
            project = new Project();
            project.setName(projectName);
            project.setResearcher(apiUser);
            project.setOpenDate(new Date());

            api.create(project);
        }
        else
        {
            LimsLink<Project> link = foundProjects.get(0);
            testAspect.setAllowedUris(link.getUri().toString());
            project = api.load(link);
            testAspect.clear();
        }

        testNotReloading(project);


        // Create a container for some samples.

        container = new Container(plateType, projectName + " Plate");

        api.create(container);

        testNotReloading(container);


        // Create some samples in the container.

        samples = new Sample[5];

        for (int i = 0; i < samples.length; i++)
        {
            Sample s = new Sample();
            s.setName(projectName + " sample #" + (i + 1));
            s.setDateReceived(new Date());
            s.setProject(project);
            s.setSubmitter(apiUser);

            UDF.setUDF(s, "Read Length", 50);
            UDF.setUDF(s, "Concentration", 0);
            UDF.setUDF(s, "Library Type", "Other");
            UDF.setUDF(s, "Number of Lanes", 2);
            UDF.setUDF(s, "Pool Size", samples.length);
            UDF.setUDF(s, "Priority Status", "Standard");
            UDF.setUDF(s, "Reference Genome", "Homo sapiens [GRCh37]");
            UDF.setUDF(s, "Sample Type", "DNA");
            UDF.setUDF(s, "SLX Identifier", "SLX-0000");
            UDF.setUDF(s, "Sequencing Type", "Paired End");
            UDF.setUDF(s, "Index Type", "Unspecified (Other)");
            UDF.setUDF(s, "Average Library Length", 10);
            UDF.setUDF(s, "Workflow", "MiSeq Express");
            UDF.setUDF(s, "Billing Information", "SWAG/000");

            s.setCreationLocation(container, (char)('A' + i) + ":" + Integer.valueOf(i + 1));

            samples[i] = s;
        }

        api.createAll(Arrays.asList(samples));

        testNotReloading(samples[0]);


        // Try loading the samples' artifacts by individually and by batch.

        @SuppressWarnings("unchecked")
        LimsLink<Artifact>[] artifactLinks = new LimsLink[samples.length];

        for (int i = 0; i < samples.length; i++)
        {
            artifactLinks[i] = samples[i].getArtifact();
        }

        testAspect.setAllowedUris(artifactLinks[0], artifactLinks[4]);

        Artifact[] artifacts = new Artifact[samples.length];

        artifacts[0] = api.load(artifactLinks[0]);
        artifacts[4] = api.load(artifactLinks[4]);

        testNotReloading(artifacts[0]);

        // Mass fetch. Should only fetch the middle three.

        testAspect.setAllowedUris(artifactLinks[1], artifactLinks[2], artifactLinks[3]);

        api.loadAll(Arrays.asList(artifactLinks)).toArray(artifacts);

        testNotReloading(artifacts[2]);

        // Check fetched artifacts match their original links.

        for (int i = 0; i < samples.length; i++)
        {
            assertEquals("Sample/artifact " + i + " don't match", samples[i].getArtifact().getLimsid(), artifacts[i].getLimsid());
        }

        // Try pooling the artifacts.

        // First, assign reagent labels.
        NumberFormat reagentFormat = NumberFormat.getIntegerInstance();
        reagentFormat.setMinimumIntegerDigits(3);

        for (int i = 0; i < artifacts.length; i++)
        {
            artifacts[i].addReagentLabel("A" + reagentFormat.format(i + 1));
        }
        api.updateAll(Arrays.asList(artifacts));

        // Now the actual pooling.

        poolContainer = new Container(tubeType, projectName + " pool container");

        api.create(poolContainer);

        ExecutableProcess execProcess = new ExecutableProcess(poolProcessType.getName(), apiUser);
        ExecutableInputOutputMap iomap = execProcess.newInputOutputMap();

        iomap.setShared(true);
        iomap.setInputs(Arrays.asList(artifacts));
        iomap.setOutput(OutputType.ANALYTE, poolContainer, "1:1");

        GenologicsProcess poolProcess = api.executeProcess(execProcess);
        assertNotNull("No pool process returned", poolProcess);

        testNotReloading(poolProcess);

        // Try attaching a file to a sample.

        File toUpload = new File("pom.xml").getAbsoluteFile();

        try
        {
            GenologicsFile uploadedFile = api.uploadFile(samples[0], toUpload.toURI().toURL(), false);
            assertNotNull("No file returned", uploadedFile);

            testNotReloading(uploadedFile);

            try
            {
                api.deleteAndRemoveFile(uploadedFile);

                Object fetched = cacheAspect.getCache(GenologicsFile.class).getQuiet(cacheAspect.keyFromLocatable(uploadedFile));
                assertNull("Deleted file is still in the cache", fetched);
            }
            catch (IOException e)
            {
                api.delete(uploadedFile);
            }
        }
        catch (IOException e)
        {
            logger.warn("Could not upload the file to the server: ", e);
        }
    }

    private <E extends LimsEntity<E>> void testNotReloading(E entity)
    {
        @SuppressWarnings("unchecked")
        Class<E> entityClass = (Class<E>)entity.getClass();

        String[] currentlyAllowed = testAspect.getAllowedUris();

        try
        {
            // Test cached.
            testAspect.clear();
            api.load(entity.getLimsid(), entityClass);
            api.retrieve(entity.getUri(), entityClass);
            api.retrieve(entity.getUri().toString(), entityClass);

            // Reload should be allowed through.
            //testAspect.setAllowedUris(entity.getUri().toString());
            //api.reload(entity);
        }
        finally
        {
            testAspect.setAllowedUris(currentlyAllowed);
        }
    }
}
