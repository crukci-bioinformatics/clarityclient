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

package com.genologics.ri.file;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.genologics.ri.file package.
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

    private final static QName _File_QNAME = new QName(FILE_NAMESPACE, "file");
    private final static QName _Details_QNAME = new QName(FILE_NAMESPACE, "details");
    private final static QName _Files_QNAME = new QName(FILE_NAMESPACE, "files");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.genologics.ri.file
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GenologicsFileBatchFetchResult }
     *
     */
    public GenologicsFileBatchFetchResult createDetails() {
        return new GenologicsFileBatchFetchResult();
    }

    /**
     * Create an instance of {@link GenologicsFile }
     *
     */
    public GenologicsFile createFile() {
        return new GenologicsFile();
    }

    /**
     * Create an instance of {@link GenologicsFiles }
     *
     */
    public GenologicsFiles createFiles() {
        return new GenologicsFiles();
    }

    /**
     * Create an instance of {@link FileLink }
     *
     */
    public FileLink createFileLink() {
        return new FileLink();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenologicsFileBatchFetchResult }{@code >}}
     *
     */
    @XmlElementDecl(namespace = FILE_NAMESPACE, name = "details")
    public JAXBElement<GenologicsFileBatchFetchResult> createDetails(GenologicsFileBatchFetchResult value) {
        return new JAXBElement<GenologicsFileBatchFetchResult>(_Details_QNAME, GenologicsFileBatchFetchResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenologicsFile }{@code >}}
     *
     */
    @XmlElementDecl(namespace = FILE_NAMESPACE, name = "file")
    public JAXBElement<GenologicsFile> createFile(GenologicsFile value) {
        return new JAXBElement<GenologicsFile>(_File_QNAME, GenologicsFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenologicsFiles }{@code >}}
     *
     */
    @XmlElementDecl(namespace = FILE_NAMESPACE, name = "files")
    public JAXBElement<GenologicsFiles> createFiles(GenologicsFiles value) {
        return new JAXBElement<GenologicsFiles>(_Files_QNAME, GenologicsFiles.class, null, value);
    }

}
