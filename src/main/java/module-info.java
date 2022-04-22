import org.cruk.genologics.api.GenologicsAPI;
import org.cruk.genologics.api.impl.GenologicsAPIImpl;

/**
 * The Clarity Java client for Clarity 6+.
 */
module org.cruk.clarity.api
{
    provides GenologicsAPI with GenologicsAPIImpl;

    requires transitive java.annotation;
    requires transitive java.xml.bind;
    requires commons.beanutils;
    requires transitive ehcache;
    requires jsch;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires transitive org.apache.httpcomponents.httpclient;
    requires transitive org.apache.httpcomponents.httpcore;
    requires transitive org.aspectj.runtime;
    requires org.slf4j;
    requires transitive spring.beans;
    requires transitive spring.core;
    requires transitive spring.context;
    requires transitive spring.integration.core;
    requires transitive spring.integration.file;
    requires transitive spring.integration.sftp;
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

    exports org.cruk.genologics.api;
    exports org.cruk.genologics.api.cache;
    exports org.cruk.genologics.api.debugging;
    exports org.cruk.genologics.api.http;
    exports org.cruk.genologics.api.impl;
    exports org.cruk.genologics.api.jaxb;
}
