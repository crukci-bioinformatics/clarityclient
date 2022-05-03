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

package com.genologics.ri.artifact;

import static com.genologics.ri.Namespaces.ARTIFACT_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.artifact package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Artifacts_QNAME = new QName(ARTIFACT_NAMESPACE, "artifacts");
    private final static QName _Details_QNAME = new QName(ARTIFACT_NAMESPACE, "details");
    private final static QName _Artifact_QNAME = new QName(ARTIFACT_NAMESPACE, "artifact");
    private final static QName _Demux_QNAME = new QName(ARTIFACT_NAMESPACE, "demux");
    private final static QName _DemuxDetails_QNAME = new QName(ARTIFACT_NAMESPACE, "demux-details");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.artifact
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DemuxDetails }
     *
     */
    public DemuxDetails createDemuxDetails() {
        return new DemuxDetails();
    }

    /**
     * Create an instance of {@link PoolStep }
     *
     */
    public PoolStep createPoolStep() {
        return new PoolStep();
    }

    /**
     * Create an instance of {@link ArtifactGroupLink }
     *
     */
    public ArtifactGroupLink createArtifactgroup() {
        return new ArtifactGroupLink();
    }

    /**
     * Create an instance of {@link SampleLink }
     *
     */
    public SampleLink createSample() {
        return new SampleLink();
    }

    /**
     * Create an instance of {@link Demux }
     *
     */
    public Demux createDemux() {
        return new Demux();
    }

    /**
     * Create an instance of {@link ReagentLabel }
     *
     */
    public ReagentLabel createReagentLabel() {
        return new ReagentLabel();
    }

    /**
     * Create an instance of {@link DemuxSourceArtifact }
     *
     */
    public DemuxSourceArtifact createDemuxSourceArtifact() {
        return new DemuxSourceArtifact();
    }

    /**
     * Create an instance of {@link DemuxLink }
     *
     */
    public DemuxLink createDemuxLink() {
        return new DemuxLink();
    }

    /**
     * Create an instance of {@link DemuxArtifactSample }
     *
     */
    public DemuxArtifactSample createDemuxArtifactSample() {
        return new DemuxArtifactSample();
    }

    /**
     * Create an instance of {@link DemuxArtifact }
     *
     */
    public DemuxArtifact createDemuxArtifact() {
        return new DemuxArtifact();
    }

    /**
     * Create an instance of {@link ParentProcessLink }
     *
     */
    public ParentProcessLink createParentProcess() {
        return new ParentProcessLink();
    }

    /**
     * Create an instance of {@link Artifact }
     *
     */
    public Artifact createArtifact() {
        return new Artifact();
    }

    /**
     * Create an instance of {@link ArtifactLink }
     *
     */
    public ArtifactLink createArtifactLink() {
        return new ArtifactLink();
    }

    /**
     * Create an instance of {@link ControlTypeLink }
     *
     */
    public ControlTypeLink createControlTypeLink() {
        return new ControlTypeLink();
    }

    /**
     * Create an instance of {@link ArtifactBatchFetchResult }
     *
     */
    public ArtifactBatchFetchResult createDetails() {
        return new ArtifactBatchFetchResult();
    }

    /**
     * Create an instance of {@link Artifacts }
     *
     */
    public Artifacts createArtifacts() {
        return new Artifacts();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Artifacts }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "artifacts")
    public JAXBElement<Artifacts> createArtifacts(Artifacts value) {
        return new JAXBElement<Artifacts>(_Artifacts_QNAME, Artifacts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArtifactBatchFetchResult }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "details")
    public JAXBElement<ArtifactBatchFetchResult> createDetails(ArtifactBatchFetchResult value) {
        return new JAXBElement<ArtifactBatchFetchResult>(_Details_QNAME, ArtifactBatchFetchResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Artifact }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "artifact")
    public JAXBElement<Artifact> createArtifact(Artifact value) {
        return new JAXBElement<Artifact>(_Artifact_QNAME, Artifact.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DemuxDetails }{@code >}}
     *
     */
    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "demux-details")
    public JAXBElement<DemuxDetails> createDemuxDetails(DemuxDetails value) {
        return new JAXBElement<DemuxDetails>(_DemuxDetails_QNAME, DemuxDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Demux }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://genologics.com/ri/artifact", name = "demux")
    public JAXBElement<Demux> createDemux(Demux value) {
        return new JAXBElement<Demux>(_Demux_QNAME, Demux.class, null, value);
    }
}
