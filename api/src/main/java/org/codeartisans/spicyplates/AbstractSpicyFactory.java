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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.codeartisans.java.toolbox.Strings;
import org.codeartisans.java.toolbox.exceptions.NullArgumentException;

public abstract class AbstractSpicyFactory
        implements SpicyFactory
{

    protected SpicyContext defaultGlobalContext = SpicyContexts.EMPTY_CONTEXT;

    @Override
    public final SpicyFactory withDefaultGlobalContext( SpicyContext defaultGlobalContext )
    {
        NullArgumentException.ensureNotNull( "Default global context", defaultGlobalContext );
        this.defaultGlobalContext = defaultGlobalContext;
        return this;
    }

    @Override
    public final SpicyPlate spicyPlate( File templateFile )
    {
        return spicyPlate( defaultGlobalContext, templateFile );
    }

    @Override
    public final SpicyPlate spicyPlate( Reader templateReader )
    {
        return spicyPlate( defaultGlobalContext, templateReader );
    }

    @Override
    public final SpicyPlate spicyPlate( String template )
    {
        return spicyPlate( defaultGlobalContext, template );
    }

    @Override
    public final SpicyPlate spicyPlate( SpicyContext globalContext, File templateFile )
    {
        NullArgumentException.ensureNotNull( "Global context", globalContext );
        NullArgumentException.ensureNotNull( "Template file", templateFile );
        try {
            return spicyPlate( globalContext, new FileReader( templateFile ) );
        } catch ( FileNotFoundException ex ) {
            throw new IllegalArgumentException( "Template file does not exist", ex );
        }
    }

    @Override
    public final SpicyPlate spicyPlate( SpicyContext globalContext, Reader templateReader )
    {
        NullArgumentException.ensureNotNull( "Global context", globalContext );
        NullArgumentException.ensureNotNull( "Template reader", templateReader );
        try {

            String template = Strings.toString( templateReader );
            return spicyPlate( globalContext, template );

        } catch ( IOException ex ) {
            throw new SpicyPlatesFailure( ex ).andLogError( "Unable to read template" );
        }
    }

}
