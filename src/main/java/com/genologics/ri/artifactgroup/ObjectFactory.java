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

package com.genologics.ri.artifactgroup;

import static com.genologics.ri.Namespaces.ARTIFACT_GROUP_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.artifactgroup package.
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

    private final static QName _Artifactgroup_QNAME = new QName(ARTIFACT_GROUP_NAMESPACE, "artifactgroup");
    private final static QName _Artifactgroups_QNAME = new QName(ARTIFACT_GROUP_NAMESPACE, "artifactgroups");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.artifactgroup
     *
     */
    public ObjectFactory() {
    }

    public ArtifactGroup createArtifactgroup() {
        return new ArtifactGroup();
    }

    public ArtifactGroupLink createArtifactgroupLink() {
        return new ArtifactGroupLink();
    }

    public Artifacts createArtifacts() {
        return new Artifacts();
    }

    public ArtifactGroups createArtifactgroups() {
        return new ArtifactGroups();
    }

    @XmlElementDecl(namespace = ARTIFACT_GROUP_NAMESPACE, name = "artifactgroup")
    public JAXBElement<ArtifactGroup> createArtifactgroup(ArtifactGroup value) {
        return new JAXBElement<ArtifactGroup>(_Artifactgroup_QNAME, ArtifactGroup.class, null, value);
    }

    @XmlElementDecl(namespace = ARTIFACT_GROUP_NAMESPACE, name = "artifactgroups")
    public JAXBElement<ArtifactGroups> createArtifactgroups(ArtifactGroups value) {
        return new JAXBElement<ArtifactGroups>(_Artifactgroups_QNAME, ArtifactGroups.class, null, value);
    }
}
