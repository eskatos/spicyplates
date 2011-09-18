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
package org.codeartisans.spicyplates.multi;

import java.io.File;
import javax.servlet.ServletContext;

import org.codeartisans.spicyplates.AbstractSpicyServlet;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyFactory;
import org.codeartisans.spicyplates.SpicyRepository;
import org.codeartisans.spicyplates.erb.ERBClasspathSpicyRepository;
import org.codeartisans.spicyplates.erb.ERBDirectorySpicyRepository;
import org.codeartisans.spicyplates.erb.ERBSpicyFactory;
import org.codeartisans.spicyplates.erb.ERBWebResourcesSpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STClasspathSpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STDirectorySpicyRepository;
import org.codeartisans.spicyplates.stringtemplate.STSpicyFactory;
import org.codeartisans.spicyplates.stringtemplate.STWebResourcesSpicyRepository;

public class MultiSpicyServlet
        extends AbstractSpicyServlet
{

    private SpicyFactory erbFactory = new ERBSpicyFactory();
    private SpicyFactory stFactory = new STSpicyFactory();

    @Override
    protected SpicyRepository classpathRepository( SpicyContext globalContext, String rootPackage )
    {
        return new MultiSpicyRepository( new ERBClasspathSpicyRepository( rootPackage, globalContext, erbFactory ),
                                         new STClasspathSpicyRepository( rootPackage, globalContext, stFactory ) );
    }

    @Override
    protected SpicyRepository directoryRepository( SpicyContext globalContext, File rootDirectory )
    {
        return new MultiSpicyRepository( new ERBDirectorySpicyRepository( rootDirectory, globalContext, erbFactory ),
                                         new STDirectorySpicyRepository( rootDirectory, globalContext, stFactory ) );
    }

    @Override
    protected SpicyRepository webResourcesRepository( SpicyContext globalContext, ServletContext servletContext )
    {
        return new MultiSpicyRepository( new ERBWebResourcesSpicyRepository( servletContext, globalContext, erbFactory ),
                                         new STWebResourcesSpicyRepository( servletContext, globalContext, stFactory ) );
    }

}
