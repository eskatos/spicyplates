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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.codeartisans.java.toolbox.io.IO;

public abstract class AbstractWebResourcesSpicyRepository
        implements SpicyRepository
{

    private final ServletContext servletContext;
    private final SpicyContext globalContext;
    private final SpicyFactory factory;
    private final Map<String, SpicyPlate> templates = new HashMap<String, SpicyPlate>();
    private final Map<String, Long> mtimes = new HashMap<String, Long>();

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
    public final synchronized SpicyPlate get( String name )
    {
        NullArgumentException.ensureNotEmpty( "SpicyPlate name", name );
        SpicyPlate template = null;
        if ( acceptTemplateName( name ) ) {
            String realPath = servletContext.getRealPath( name );
            if ( !StringUtils.isEmpty( realPath ) ) {
                // Templates are on filesystem
                File file = new File( realPath );
                if ( file.exists() ) {
                    Long mtime = mtimes.get( name );
                    if ( mtime == null || mtime < file.lastModified() ) {
                        // First load or template changed
                        template = factory.spicyPlate( globalContext, file );
                        templates.put( name, template );
                        mtimes.put( name, file.lastModified() );
                        SpicyPlate.LOGGER.debug( "WebResourcesSpicyRepository loaded template {} from filesystem", name );

                    } else {
                        // Using template cache
                        template = templates.get( name );
                        SpicyPlate.LOGGER.debug( "WebResourcesSpicyRepository loaded template {} from cache", name );
                    }
                }
            } else {
                // Templates are in classpath, always use cache
                template = templates.get( name );
                if ( template == null ) {
                    InputStream stream = servletContext.getResourceAsStream( "/" + name );
                    if ( stream != null ) {
                        Reader reader = null;
                        try {
                            reader = new InputStreamReader( stream );
                            template = factory.spicyPlate( globalContext, reader );
                        } finally {
                            IO.closeSilently( reader );
                        }
                    }
                    templates.put( name, template );
                    SpicyPlate.LOGGER.debug( "WebResourcesSpicyRepository loaded template {} from classpath", name );
                } else {
                    SpicyPlate.LOGGER.debug( "WebResourcesSpicyRepository loaded template {} from cache", name );
                }
            }
        }
        return template;
    }

}
