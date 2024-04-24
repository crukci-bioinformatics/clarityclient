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

package com.genologics.ri.project;

import static com.genologics.ri.Namespaces.PROJECT_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.project package.
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

    private final static QName _Project_QNAME = new QName(PROJECT_NAMESPACE, "project");
    private final static QName _Projects_QNAME = new QName(PROJECT_NAMESPACE, "projects");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.project
     *
     */
    public ObjectFactory() {
    }

    public ProjectLink createProjectLink() {
        return new ProjectLink();
    }

    public ResearcherLink createResearcher() {
        return new ResearcherLink();
    }

    public Projects createProjects() {
        return new Projects();
    }

    public Project createProject() {
        return new Project();
    }

    @XmlElementDecl(namespace = PROJECT_NAMESPACE, name = "project")
    public JAXBElement<Project> createProject(Project value) {
        return new JAXBElement<Project>(_Project_QNAME, Project.class, null, value);
    }

    @XmlElementDecl(namespace = PROJECT_NAMESPACE, name = "projects")
    public JAXBElement<Projects> createProjects(Projects value) {
        return new JAXBElement<Projects>(_Projects_QNAME, Projects.class, null, value);
    }
}
