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

    requires transitive java.annotation;
    requires transitive java.xml.bind;
    requires commons.beanutils;
    requires ehcache;
    requires jsch;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.aspectj.runtime;
    requires org.slf4j;
    requires spring.beans;
    requires spring.core;
    requires spring.context;
    requires spring.integration.core;
    requires spring.integration.file;
    requires spring.integration.sftp;
    requires spring.oxm;
    requires spring.web;

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
    exports org.cruk.clarity.api.cache;
    exports org.cruk.clarity.api.debugging;
    exports org.cruk.clarity.api.http;
    exports org.cruk.clarity.api.impl;
    exports org.cruk.clarity.api.jaxb;
}
