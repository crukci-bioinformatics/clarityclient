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

package com.genologics.ri.file;

import static com.genologics.ri.Namespaces.FILE_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
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

    public ClarityFileBatchFetchResult createDetails() {
        return new ClarityFileBatchFetchResult();
    }

    public ClarityFile createFile() {
        return new ClarityFile();
    }

    public ClarityFiles createFiles() {
        return new ClarityFiles();
    }

    public FileLink createFileLink() {
        return new FileLink();
    }

    @XmlElementDecl(namespace = FILE_NAMESPACE, name = "details")
    public JAXBElement<ClarityFileBatchFetchResult> createDetails(ClarityFileBatchFetchResult value) {
        return new JAXBElement<ClarityFileBatchFetchResult>(_Details_QNAME, ClarityFileBatchFetchResult.class, null, value);
    }

    @XmlElementDecl(namespace = FILE_NAMESPACE, name = "file")
    public JAXBElement<ClarityFile> createFile(ClarityFile value) {
        return new JAXBElement<ClarityFile>(_File_QNAME, ClarityFile.class, null, value);
    }

    @XmlElementDecl(namespace = FILE_NAMESPACE, name = "files")
    public JAXBElement<ClarityFiles> createFiles(ClarityFiles value) {
        return new JAXBElement<ClarityFiles>(_Files_QNAME, ClarityFiles.class, null, value);
    }
}
