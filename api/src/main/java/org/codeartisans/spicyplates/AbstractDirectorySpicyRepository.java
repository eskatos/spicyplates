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
import java.util.HashMap;
import java.util.Map;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;

public abstract class AbstractDirectorySpicyRepository
        implements SpicyRepository
{

    private final File rootDir;
    private final SpicyContext globalContext;
    private final SpicyFactory factory;
    private final Map<String, SpicyPlate> templates = new HashMap<String, SpicyPlate>();
    private final Map<String, Long> mtimes = new HashMap<String, Long>();

    public AbstractDirectorySpicyRepository( File rootDir, SpicyContext globalContext, SpicyFactory factory )
    {
        NullArgumentException.ensureNotNull( "Templates root directory", rootDir );
        NullArgumentException.ensureNotNull( "SpicyFactory", factory );
        this.rootDir = rootDir;
        if ( globalContext == null ) {
            globalContext = SpicyContexts.EMPTY_CONTEXT;
        }
        this.globalContext = globalContext;
        this.factory = factory;
    }

    protected abstract boolean acceptTemplateName( String name );

    @Override
    public final synchronized SpicyPlate get( String name )
    {
        NullArgumentException.ensureNotEmpty( "SpicyPlate name", name );
        SpicyPlate template = templates.get( name );
        if ( template == null && acceptTemplateName( name ) ) {
            File file = new File( rootDir, name );
            if ( file.exists() ) {
                Long mtime = mtimes.get( name );
                if ( mtime == null || mtime < file.lastModified() ) {
                    // First load or template changed
                    template = factory.spicyPlate( globalContext, file );
                    templates.put( name, template );
                    mtimes.put( name, file.lastModified() );
                    SpicyPlate.LOGGER.debug( "DirectorySpicyRepository loaded template {} from filesystem", name );

                } else {
                    // Using template cache
                    template = templates.get( name );
                    SpicyPlate.LOGGER.debug( "DirectorySpicyRepository loaded template {} from cache", name );
                }
            }
        }
        return template;
    }

}
