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

import org.cruk.clarity.api.ClarityAPI;
import org.cruk.clarity.api.automation.ClarityProcessAutomation;
import org.cruk.clarity.api.automation.impl.ClarityProcessAutomationImpl;
import org.cruk.clarity.api.impl.ClarityAPIImpl;

/**
 * The Clarity Java client for Clarity 6+.
 */
module org.cruk.clarity.api
{
    provides ClarityAPI with ClarityAPIImpl;
    provides ClarityProcessAutomation with ClarityProcessAutomationImpl;

    requires transitive jakarta.annotation;
    requires transitive jakarta.xml.bind;
    requires org.apache.commons.beanutils;
    requires org.apache.commons.collections4;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.core5.httpcore5.h2;
    requires org.apache.sshd.osgi;
    requires org.apache.sshd.sftp;
    requires org.aspectj.weaver;
    requires org.slf4j;
    requires transitive spring.beans;
    requires transitive spring.core;
    requires transitive spring.context;
    requires spring.integration.core;
    requires spring.integration.file;
    requires spring.integration.sftp;
    requires transitive spring.oxm;
    requires transitive spring.web;

    exports com.genologics.ri;
    exports com.genologics.ri.artifact;
    exports com.genologics.ri.artifactgroup;
    exports com.genologics.ri.automation;
    exports com.genologics.ri.configuration;
    exports com.genologics.ri.container;
    exports com.genologics.ri.containertype;
    exports com.genologics.ri.controltype;
    exports com.genologics.ri.exception;
    exports com.genologics.ri.file;
    exports com.genologics.ri.instrument;
    exports com.genologics.ri.instrumenttype;
    exports com.genologics.ri.lab;
    exports com.genologics.ri.permission;
    exports com.genologics.ri.process;
    exports com.genologics.ri.processexecution;
    exports com.genologics.ri.processtemplate;
    exports com.genologics.ri.processtype;
    exports com.genologics.ri.project;
    exports com.genologics.ri.property;
    exports com.genologics.ri.protocolconfiguration;
    exports com.genologics.ri.queue;
    exports com.genologics.ri.reagentkit;
    exports com.genologics.ri.reagentlot;
    exports com.genologics.ri.reagenttype;
    exports com.genologics.ri.researcher;
    exports com.genologics.ri.role;
    exports com.genologics.ri.routing;
    exports com.genologics.ri.sample;
    exports com.genologics.ri.stage;
    exports com.genologics.ri.step;
    exports com.genologics.ri.stepconfiguration;
    exports com.genologics.ri.userdefined;
    exports com.genologics.ri.version;
    exports com.genologics.ri.workflowconfiguration;

    exports org.cruk.clarity.api;
    exports org.cruk.clarity.api.automation;
    exports org.cruk.clarity.api.automation.impl;
    exports org.cruk.clarity.api.debugging;
    exports org.cruk.clarity.api.http;
    exports org.cruk.clarity.api.impl;
    exports org.cruk.clarity.api.jaxb;
    exports org.cruk.clarity.api.spring;
}
