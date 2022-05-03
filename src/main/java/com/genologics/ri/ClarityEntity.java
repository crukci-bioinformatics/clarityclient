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

package com.genologics.ri;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.genologics.ri.sample.Sample;
import com.genologics.ri.sample.SampleCreation;

/**
 * Annotation to put on classes that are entities in the Clarity LIMS.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface ClarityEntity
{
    /**
     * If {@code true}, the flag indicates that entities of the annotated type
     * can be created with a POST.
     *
     * @return The creatable flag.
     */
    boolean creatable() default false;

    /**
     * If {@code true}, the flag indicates that entities of the annotated type
     * can be updated with PUT.
     *
     * @return The updateable flag.
     */
    boolean updateable() default false;

    /**
     * If {@code true}, the flag indicates that entities of the annotated type
     * can be removed with DELETE.
     *
     * @return The removable flag.
     */
    boolean removable() default false;

    /**
     * The part of the URL that specifies how to list or load entities of the
     * annotated class.
     *
     * <p>
     * For example, the {@code Sample} class can fetch entities via the path
     * {@code http://blah.com/api/v2/<b>samples</b>/...}. This attribute
     * of the annotation must specify the "samples" part of the URL.
     * </p>
     *
     * <p>
     * This has a slightly different meaning when the object requested
     * is part of another component (steps of protocols, stages of
     * workflow). Then, this value needs to be the innermost part of the
     * URI and the {@code primarySection} attribute needs to be set to
     * the outer part of the API. For example, for a {@code ProtocolStep}
     * this attribute should be set to "steps" and the {@code primarySection}
     * set to the {@code Protocol} class.
     * </p>
     *
     * @return The part of the URL path that yields entities of this type.
     *
     * @see #primaryEntity()
     */
    String uriSection();

    /**
     * Whether the class is a subsection of a base end point. Examples of this
     * are process step information classes and the demux information of artifacts.
     *
     * <p>
     * If this is unset (empty string), then the class is result of the main
     * API end point, not a subsection.
     * </p>
     *
     * @return The end point URI subsection.
     */
    String uriSubsection() default EMPTY;

    /**
     * Whether entities of the class can be cached.
     *
     * @return The cacheable flag.
     */
    boolean cacheable() default true;

    /**
     * Whether the class represents objects that have explicit states (versions)
     * in the Clarity system. Such items will usually be returned with a
     * "{@code state=&lt;number&gt;}" parameter in their URIs.
     *
     * @return The stateful flag.
     */
    boolean stateful() default false;

    /**
     * For some awkward classes that have a similar alternative class for the
     * creation process, this attribute allows that class used in creation to
     * be specified. The class given here needs to have a public constructor
     * that takes a single argument of an instance of the annotated class.
     *
     * <p>
     * The {@code Sample} and {@code SampleCreation} classes are an example of
     * this mechanism.
     * </p>
     *
     * @return The alternative class used to create the objects of the normal
     * type through the API.
     * <p>
     * It is prohibited in Java to assign a null as the default value of an
     * annotation, so here the default is the class of the {@code void} type.
     * Code using this value needs to check that that is not the value set,
     * rather than {@code null}.
     * </p>
     *
     * @see Sample
     * @see SampleCreation
     */
    // Note - cannot use null here as one might expect.
    // See http://stackoverflow.com/questions/1178104/error-setting-a-default-null-value-for-an-annotations-field
    Class<?> creationClass() default void.class;

    /**
     * Specifies the outer entity of the API for which the annotated class
     * is the inner entity. For example, {@code Protocol} when annotating
     * {@code ProtocolStep}, or {@code Workflow} when annotating {@code Stage}.
     *
     * @return The class to which the annotated class is a part of.
     *
     * @since 2.22
     *
     * @see #uriSection()
     */
    Class<?> primaryEntity() default void.class;
}
