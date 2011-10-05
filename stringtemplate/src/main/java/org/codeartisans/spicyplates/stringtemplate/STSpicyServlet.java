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
package org.codeartisans.spicyplates.stringtemplate;

import java.io.File;

import javax.servlet.ServletContext;

import org.codeartisans.spicyplates.AbstractSpicyServlet;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyRepository;

public class STSpicyServlet
        extends AbstractSpicyServlet
{

    private STSpicyFactory factory = new STSpicyFactory();

    @Override
    protected SpicyRepository classpathRepository( SpicyContext globalContext, String rootPackage )
    {
        return new STClasspathSpicyRepository( rootPackage, globalContext, factory );
    }

    @Override
    protected SpicyRepository directoryRepository( SpicyContext globalContext, File rootDirectory )
    {
        return new STDirectorySpicyRepository( rootDirectory, globalContext, factory );
    }

    @Override
    protected SpicyRepository webResourcesRepository( SpicyContext globalContext, ServletContext servletContext )
    {
        return new STWebResourcesSpicyRepository( servletContext, globalContext, factory );
    }

}
