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
import java.util.HashMap;
import java.util.Map;

import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.codeartisans.java.toolbox.io.IO;

public abstract class AbstractClasspathSpicyRepository
        implements SpicyRepository
{

    private final ClassLoader classLoader;
    private final String rootPath;
    private final SpicyContext globalContext;
    private final SpicyFactory factory;
    private final Map<String, SpicyPlate> templates = new HashMap<String, SpicyPlate>();

    public AbstractClasspathSpicyRepository( ClassLoader classLoader, String rootPackage, SpicyContext globalContext, SpicyFactory factory )
    {
        NullArgumentException.ensureNotEmpty( "Templates root package", rootPackage );
        NullArgumentException.ensureNotNull( "SpicyFactory", factory );
        if ( classLoader == null ) {
            classLoader = getClass().getClassLoader();
        }
        this.classLoader = classLoader;
        this.rootPath = rootPackage.replace( '.', '/' );
        if ( globalContext == null ) {
            globalContext = SpicyContexts.EMPTY_CONTEXT;
        }
        this.globalContext = globalContext;
        this.factory = factory;
    }

    public AbstractClasspathSpicyRepository( ClassLoader classLoader, String rootPackage, SpicyFactory factory )
    {
        this( classLoader, rootPackage, null, factory );
    }

    public AbstractClasspathSpicyRepository( String rootPackage, SpicyContext globalContext, SpicyFactory factory )
    {
        this( null, rootPackage, globalContext, factory );
    }

    public AbstractClasspathSpicyRepository( String rootPackage, SpicyFactory factory )
    {
        this( null, rootPackage, null, factory );
    }

    protected abstract boolean acceptTemplateName( String name );

    @Override
    public final synchronized SpicyPlate get( String name )
    {
        NullArgumentException.ensureNotEmpty( "SpicyPlate name", name );
        SpicyPlate template = templates.get( name );
        if ( template == null && acceptTemplateName( name ) ) {
            InputStream stream = classLoader.getResourceAsStream( rootPath + "/" + name );
            if ( stream != null ) {
                Reader reader = null;
                try {
                    reader = new InputStreamReader( stream );
                    template = factory.spicyPlate( globalContext, reader );
                } finally {
                    IO.closeSilently( reader );
                }
                templates.put( name, template );
            }
        }
        return template;
    }

}
