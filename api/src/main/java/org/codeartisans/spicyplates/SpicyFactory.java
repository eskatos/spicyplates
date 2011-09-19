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

import java.io.File;
import java.io.Reader;

/**
 * A factory for creating new {@link SpicyPlate}s instances.
 * 
 * {@link SpicyFactory} implementations holds a default global {@link SpicyContext}
 * used when creating new {@link SpicyPlate} instances without giving one.
 */
public interface SpicyFactory
{

    /**
     * @param defaultGlobalContext {@link SpicyContext} to use as global context when none is provided
     * @return This very {@link SpicyFactory} for fluent use
     */
    SpicyFactory withDefaultGlobalContext( SpicyContext defaultGlobalContext );

    /**
     * @param templateFile File containing the template's data
     * @return A new {@link SpicyPlate} using the default global context
     * @throws SpicyPlatesFailure when unable to load/parse the underlying template
     */
    SpicyPlate spicyPlate( File templateFile )
            throws SpicyPlatesFailure;

    /**
     * @param templateReader {@link Reader} of the template's data
     * @return A new {@link SpicyPlate} using the default global context
     * @throws SpicyPlatesFailure when unable to load/parse the underlying template
     */
    SpicyPlate spicyPlate( Reader templateReader )
            throws SpicyPlatesFailure;

    /**
     * @param template {@link String} containing the template's data
     * @return A new {@link SpicyPlate} using the default global context
     * @throws SpicyPlatesFailure when unable to load/parse the underlying template
     */
    SpicyPlate spicyPlate( String template )
            throws SpicyPlatesFailure;

    /**
     * @param globalContext The {@link SpicyContext} to use as global context for this {@link SpicyPlate}
     * @param templateFile File containing the template's data
     * @return A new {@link SpicyPlate}
     * @throws SpicyPlatesFailure when unable to load/parse the underlying template
     */
    SpicyPlate spicyPlate( SpicyContext globalContext, File templateFile )
            throws SpicyPlatesFailure;

    /**
     * @param globalContext The {@link SpicyContext} to use as global context for this {@link SpicyPlate}
     * @param templateReader {@link Reader} of the template's data
     * @return A new {@link SpicyPlate}
     * @throws SpicyPlatesFailure when unable to load/parse the underlying template
     */
    SpicyPlate spicyPlate( SpicyContext globalContext, Reader templateReader )
            throws SpicyPlatesFailure;

    /**
     * @param template {@link String} containing the template's data
     * @param globalContext The {@link SpicyContext} to use as global context for this {@link SpicyPlate}
     * @return A new {@link SpicyPlate}
     * @throws SpicyPlatesFailure when unable to load/parse the underlying template
     */
    SpicyPlate spicyPlate( SpicyContext globalContext, String template )
            throws SpicyPlatesFailure;

}
