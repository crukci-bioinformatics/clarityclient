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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for classes that contain lists of entities fetched from the API's
 * batch retrieve mechanism.
 *
 * <p>
 * Some entities can also be created or updated with a batch call. The
 * attributes of this annotation specify which of these operations can take
 * place like this.
 * </p>
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface ClarityBatchRetrieveResult
{
    /**
     * The class of entity indicated by the links returned in the annotated object.
     *
     * @return The LIMS entity class.
     */
    Class<? extends Locatable> entityClass();

    /**
     * Flag indicating that the annotated entity can be created using a batch
     * creation call.
     *
     * @return {@code true} if entities of the class can be created in batch,
     * {@code false} if not.
     */
    boolean batchCreate() default false;

    /**
     * Flag indicating that the annotated entity can be updated using a batch
     * update call.
     *
     * @return {@code true} if entities of the class can be updated in batch,
     * {@code false} if not.
     */
    boolean batchUpdate() default false;
}
