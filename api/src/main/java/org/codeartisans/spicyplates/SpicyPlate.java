/*
 * Copyright (c) 2011, Paul Merlin. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.codeartisans.spicyplates;

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface SpicyPlate
{

    /**
     * SpicyPlate main {@link Logger}.
     * 
     * For easy diagnostics all spicyplates code use this logger.
     */
    Logger LOGGER = LoggerFactory.getLogger( SpicyPlate.class.getPackage().getName() );

    /**
     * Render the underlying template to the given {@link Writer}.
     * 
     * @param context   {@link SpicyContext} to use during this render
     * @param output    This is where the template result will be written
     */
    void render( SpicyContext context, Writer output );

}
