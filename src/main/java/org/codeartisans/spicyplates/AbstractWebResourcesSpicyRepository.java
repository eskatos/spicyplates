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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.servlet.ServletContext;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.codeartisans.java.toolbox.io.IO;

public abstract class AbstractWebResourcesSpicyRepository
        implements SpicyRepository
{

    private final ServletContext servletContext;
    private final SpicyContext globalContext;
    private final SpicyFactory factory;

    public AbstractWebResourcesSpicyRepository( ServletContext servletContext, SpicyContext globalContext, SpicyFactory factory )
    {
        NullArgumentException.ensureNotNull( "ServletContext", servletContext );
        NullArgumentException.ensureNotNull( "SpicyFactory", factory );
        if ( globalContext == null ) {
            globalContext = SpicyContexts.EMPTY_CONTEXT;
        }
        this.servletContext = servletContext;
        this.globalContext = globalContext;
        this.factory = factory;
    }

    protected abstract boolean acceptTemplateName( String name );

    @Override
    public SpicyPlate get( String name )
    {
        NullArgumentException.ensureNotEmpty( "SpicyPlate name", name );
        if ( acceptTemplateName( name ) ) {
            InputStream stream = servletContext.getResourceAsStream( "/" + name );
            if ( stream != null ) {
                Reader reader = null;
                try {
                    reader = new InputStreamReader( stream );
                    return factory.spicyPlate( globalContext, reader );
                } finally {
                    IO.closeSilently( reader );
                }
            }
        }
        return null;
    }

}
