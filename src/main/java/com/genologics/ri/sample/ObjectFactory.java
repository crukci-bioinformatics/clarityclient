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

package com.genologics.ri.sample;

import static com.genologics.ri.Namespaces.SAMPLE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.sample package.
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

    private final static QName _Sample_QNAME = new QName(SAMPLE_NAMESPACE, "sample");
    private final static QName _Samplecreation_QNAME = new QName(SAMPLE_NAMESPACE, "samplecreation");
    private final static QName _Samples_QNAME = new QName(SAMPLE_NAMESPACE, "samples");
    private final static QName _Details_QNAME = new QName(SAMPLE_NAMESPACE, "details");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.sample
     *
     */
    public ObjectFactory() {
    }

    public SampleCreation createSamplecreation() {
        return new SampleCreation();
    }

    public Sample createSample() {
        return new Sample();
    }

    public SampleBatchFetchResult createDetails() {
        return new SampleBatchFetchResult();
    }

    public ArtifactLink createArtifact() {
        return new ArtifactLink();
    }

    public Samples createSamples() {
        return new Samples();
    }

    public SampleBase createSamplebase() {
        return new SampleBase();
    }

    @Deprecated
    public BioSource createBiosource() {
        return new BioSource();
    }

    public Submitter createSubmitter() {
        return new Submitter();
    }

    public SampleLink createSampleLink() {
        return new SampleLink();
    }

    public ProjectLink createProject() {
        return new ProjectLink();
    }

    public ControlTypeLink createControlTypeLink() {
        return new ControlTypeLink();
    }

    @XmlElementDecl(namespace = SAMPLE_NAMESPACE, name = "sample")
    public JAXBElement<Sample> createSample(Sample value) {
        return new JAXBElement<Sample>(_Sample_QNAME, Sample.class, null, value);
    }

    @XmlElementDecl(namespace = SAMPLE_NAMESPACE, name = "samplecreation")
    public JAXBElement<SampleCreation> createSamplecreation(SampleCreation value) {
        return new JAXBElement<SampleCreation>(_Samplecreation_QNAME, SampleCreation.class, null, value);
    }

    @XmlElementDecl(namespace = SAMPLE_NAMESPACE, name = "samples")
    public JAXBElement<Samples> createSamples(Samples value) {
        return new JAXBElement<Samples>(_Samples_QNAME, Samples.class, null, value);
    }

    @XmlElementDecl(namespace = SAMPLE_NAMESPACE, name = "details")
    public JAXBElement<SampleBatchFetchResult> createDetails(SampleBatchFetchResult value) {
        return new JAXBElement<SampleBatchFetchResult>(_Details_QNAME, SampleBatchFetchResult.class, null, value);
    }
}
