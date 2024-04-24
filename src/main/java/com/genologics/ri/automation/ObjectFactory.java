/*
 * CRUK-CI Clarity REST API Java Client.
 * Copyright (C) 2018 Cancer Research UK Cambridge Institute.
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

package com.genologics.ri.automation;

import static com.genologics.ri.Namespaces.AUTOMATION_NAMESPACE;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.genologics.ri.automation package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory
{
    private final static QName _Automation_QNAME = new QName(AUTOMATION_NAMESPACE, "automation");
    private final static QName _Automations_QNAME = new QName(AUTOMATION_NAMESPACE, "automations");

    public ObjectFactory()
    {
    }

    public ProcessTypeLink createProcessTypeLink()
    {
        return new ProcessTypeLink();
    }

    public AutomationLink createAutomationLink()
    {
        return new AutomationLink();
    }

    public Automations createAutomations()
    {
        return new Automations();
    }

    public Automation createAutomation()
    {
        return new Automation();
    }

    @XmlElementDecl(namespace = AUTOMATION_NAMESPACE, name = "automation")
    public JAXBElement<Automation> createAutomation(Automation value)
    {
        return new JAXBElement<Automation>(_Automation_QNAME, Automation.class, null, value);
    }

    @XmlElementDecl(namespace = AUTOMATION_NAMESPACE, name = "automations")
    public JAXBElement<Automations> createAutomations(Automations value)
    {
        return new JAXBElement<Automations>(_Automations_QNAME, Automations.class, null, value);
    }
}
