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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.codeartisans.java.toolbox.StringUtils;

/**
 * This class is a bridge between Servlet & Filter.
 */
final class SpicyServletSupport
{

    static interface Delegate
    {

        SpicyRepository classpathRepository( String rootPackage );

        SpicyRepository directoryRepository( File rootDirectory );

        SpicyRepository webResourcesRepository( ServletContext servletContext );

    }

    private static class ConfigWrapper
    {

        private final ServletConfig servletConfig;
        private final FilterConfig filterConfig;

        private ConfigWrapper( ServletConfig servletConfig )
        {
            this.servletConfig = servletConfig;
            this.filterConfig = null;
        }

        private ConfigWrapper( FilterConfig filterConfig )
        {
            this.servletConfig = null;
            this.filterConfig = filterConfig;
        }

        private Enumeration<String> getInitParameterNames()
        {
            if ( servletConfig != null ) {
                return servletConfig.getInitParameterNames();
            }
            return filterConfig.getInitParameterNames();
        }

        private String getInitParameter( String name )
        {
            if ( servletConfig != null ) {
                return servletConfig.getInitParameter( name );
            }
            return filterConfig.getInitParameter( name );
        }

        private String logName()
        {
            if ( servletConfig != null ) {
                return "SpicyServlet";
            }
            return "SpicyFilter";
        }

        private ServletContext getServletContext()
        {
            if ( servletConfig != null ) {
                return servletConfig.getServletContext();
            }
            return filterConfig.getServletContext();
        }

    }

    private static final String PARAM_PACKAGES = "packages";
    private static final String PARAM_DIRECTORIES = "directories";
    private static final String PARAM_WEB_RESOURCES = "web-resources";
    private static final String PARAM_ALLOW_QUERY = "allow-query";
    private static final String[] PARAMETERS = { PARAM_PACKAGES, PARAM_DIRECTORIES, PARAM_WEB_RESOURCES, PARAM_ALLOW_QUERY };

    static boolean allowQuery( ServletConfig config )
    {
        return allowQuery( new ConfigWrapper( config ) );
    }

    static boolean allowQuery( FilterConfig config )
    {
        return allowQuery( new ConfigWrapper( config ) );
    }

    private static boolean allowQuery( ConfigWrapper config )
    {
        boolean allowQuery = Boolean.valueOf( config.getInitParameter( PARAM_ALLOW_QUERY ) );
        if ( allowQuery ) {
            SpicyPlate.LOGGER.warn( "{} will put query strings in templates context, you certainly don't want that in production!", config.logName() );
        }
        return allowQuery;
    }

    static SpicyContext buildGlobalContext( ServletConfig config )
    {
        return buildGlobalContext( new ConfigWrapper( config ) );
    }

    static SpicyContext buildGlobalContext( FilterConfig config )
    {
        return buildGlobalContext( new ConfigWrapper( config ) );
    }

    private static SpicyContext buildGlobalContext( ConfigWrapper config )
    {
        SpicyContext globalContext = new SimpleSpicyContext();
        Enumeration<String> parameters = config.getInitParameterNames();
        while ( parameters.hasMoreElements() ) {
            String eachParameter = parameters.nextElement();
            if ( Arrays.binarySearch( PARAMETERS, eachParameter ) < 0 ) {
                globalContext.put( eachParameter, config.getInitParameter( eachParameter ) );
            }
        }
        return globalContext;
    }

    static SpicyRepository buildRepository( ServletConfig config, Delegate delegate )
    {
        return buildRepository( new ConfigWrapper( config ), delegate );
    }

    static SpicyRepository buildRepository( FilterConfig config, Delegate delegate )
    {
        return buildRepository( new ConfigWrapper( config ), delegate );
    }

    private static SpicyRepository buildRepository( ConfigWrapper config, Delegate delegate )
    {
        List<SpicyRepository> repositories = new ArrayList<SpicyRepository>();

        // Classpath repositories
        String packagesParam = config.getInitParameter( PARAM_PACKAGES );
        if ( !StringUtils.isEmpty( packagesParam ) ) {
            String[] packages = packagesParam.split( "," );
            for ( String eachPackage : packages ) {
                repositories.add( delegate.classpathRepository( eachPackage ) );
            }
        }

        // Directory repositories
        String directoriesParam = config.getInitParameter( PARAM_DIRECTORIES );
        if ( !StringUtils.isEmpty( directoriesParam ) ) {
            String[] directories = directoriesParam.split( "," );
            for ( String eachDirectory : directories ) {
                repositories.add( delegate.directoryRepository( new File( eachDirectory ) ) );
            }
        }

        // WebResources repository
        if ( Boolean.valueOf( config.getInitParameter( PARAM_WEB_RESOURCES ) ) ) {
            repositories.add( delegate.webResourcesRepository( config.getServletContext() ) );
        }

        if ( repositories.isEmpty() ) {
            SpicyPlate.LOGGER.warn( "{} initialized without any template repository, check the init parameters", config.logName() );
        }
        return new MultiSpicyRepository( repositories );
    }

    private SpicyServletSupport()
    {
    }

}
