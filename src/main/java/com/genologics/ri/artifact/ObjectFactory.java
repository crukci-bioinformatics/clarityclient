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

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
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

    public DemuxDetails createDemuxDetails() {
        return new DemuxDetails();
    }

    public PoolStep createPoolStep() {
        return new PoolStep();
    }

    public ArtifactGroupLink createArtifactgroup() {
        return new ArtifactGroupLink();
    }

    public SampleLink createSample() {
        return new SampleLink();
    }

    public Demux createDemux() {
        return new Demux();
    }

    public ReagentLabel createReagentLabel() {
        return new ReagentLabel();
    }

    public DemuxSourceArtifact createDemuxSourceArtifact() {
        return new DemuxSourceArtifact();
    }

    public DemuxLink createDemuxLink() {
        return new DemuxLink();
    }

    public DemuxArtifactSample createDemuxArtifactSample() {
        return new DemuxArtifactSample();
    }

    public DemuxArtifact createDemuxArtifact() {
        return new DemuxArtifact();
    }

    public ParentProcessLink createParentProcess() {
        return new ParentProcessLink();
    }

    public Artifact createArtifact() {
        return new Artifact();
    }

    public ArtifactLink createArtifactLink() {
        return new ArtifactLink();
    }

    public ControlTypeLink createControlTypeLink() {
        return new ControlTypeLink();
    }

    public ArtifactBatchFetchResult createDetails() {
        return new ArtifactBatchFetchResult();
    }

    public Artifacts createArtifacts() {
        return new Artifacts();
    }

    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "artifacts")
    public JAXBElement<Artifacts> createArtifacts(Artifacts value) {
        return new JAXBElement<Artifacts>(_Artifacts_QNAME, Artifacts.class, null, value);
    }

    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "details")
    public JAXBElement<ArtifactBatchFetchResult> createDetails(ArtifactBatchFetchResult value) {
        return new JAXBElement<ArtifactBatchFetchResult>(_Details_QNAME, ArtifactBatchFetchResult.class, null, value);
    }

    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "artifact")
    public JAXBElement<Artifact> createArtifact(Artifact value) {
        return new JAXBElement<Artifact>(_Artifact_QNAME, Artifact.class, null, value);
    }

    @XmlElementDecl(namespace = ARTIFACT_NAMESPACE, name = "demux-details")
    public JAXBElement<DemuxDetails> createDemuxDetails(DemuxDetails value) {
        return new JAXBElement<DemuxDetails>(_DemuxDetails_QNAME, DemuxDetails.class, null, value);
    }

    @XmlElementDecl(namespace = "http://genologics.com/ri/artifact", name = "demux")
    public JAXBElement<Demux> createDemux(Demux value) {
        return new JAXBElement<Demux>(_Demux_QNAME, Demux.class, null, value);
    }
}
