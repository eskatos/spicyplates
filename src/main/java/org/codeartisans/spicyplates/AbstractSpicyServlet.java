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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.java.toolbox.io.IO;

/**
 * Init-parameters:
 *      packages        Coma separated list of root templates packages
 *      directories     Coma separated list of root templates directories
 * All other init parameters are added as global context for all templates
 * Query strings from URLs are added as local context.
 * 
 * Descendent can populate the global context during init and each request context.
 */
public abstract class AbstractSpicyServlet
        extends HttpServlet
{

    private boolean allowQuery = false;
    private SpicyContext globalContext;
    private SpicyRepository repository;

    protected abstract SpicyRepository classpathRepository( SpicyContext globalContext, String rootPackage );

    protected abstract SpicyRepository directoryRepository( SpicyContext globalContext, File rootDirectory );

    protected abstract SpicyRepository webResourcesRepository( SpicyContext globalContext, ServletContext servletContext );

    static class ServletDelegate
            implements SpicyServletSupport.Delegate
    {

        private final AbstractSpicyServlet servlet;

        public ServletDelegate( AbstractSpicyServlet filter )
        {
            this.servlet = filter;
        }

        @Override
        public SpicyRepository classpathRepository( String rootPackage )
        {
            return servlet.classpathRepository( servlet.globalContext, rootPackage );
        }

        @Override
        public SpicyRepository directoryRepository( File rootDirectory )
        {
            return servlet.directoryRepository( servlet.globalContext, rootDirectory );
        }

        @Override
        public SpicyRepository webResourcesRepository( ServletContext servletContext )
        {
            return servlet.webResourcesRepository( servlet.globalContext, servletContext );
        }

    }

    @Override
    public final void init()
            throws ServletException
    {
        allowQuery = SpicyServletSupport.allowQuery( getServletConfig() );
        globalContext = SpicyServletSupport.buildGlobalContext( getServletConfig() );
        populateGlobalContext( globalContext );
        repository = SpicyServletSupport.buildRepository( getServletConfig(), new ServletDelegate( this ) );
        SpicyPlate.LOGGER.info( "SpicyServlet initialized" );
    }

    @Override
    public final void destroy()
    {
        allowQuery = false;
        globalContext = null;
        repository = null;
        SpicyPlate.LOGGER.info( "SpicyServlet destroyed" );
    }

    @Override
    protected final void doGet( HttpServletRequest httpRequest, HttpServletResponse httpResponse )
            throws ServletException, IOException
    {
        String templateName = httpRequest.getPathInfo();
        if ( !StringUtils.isEmpty( templateName ) && templateName.startsWith( "/" ) ) {
            templateName = templateName.substring( 1 );
        }
        templateName = pathInfoToTemplateName( templateName );
        SpicyPlate template = repository.get( templateName );
        if ( template != null ) {
            SpicyPlate.LOGGER.info( "SpicyServlet got a request for {} and a template match", templateName );
            Writer writer = null;
            try {
                writer = new OutputStreamWriter( httpResponse.getOutputStream() );
                SpicyContext requestContext = new SimpleSpicyContext();
                if ( allowQuery ) {
                    requestContext.putAll( httpRequest.getParameterMap() );
                }
                populateRequestContext( httpRequest, requestContext );
                template.render( requestContext, writer );
            } catch ( Exception ex ) {
                String message = "Unable to render template " + templateName;
                SpicyPlate.LOGGER.error( message, ex );
                httpResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message );
            } finally {
                IO.closeSilently( writer );
                IO.closeSilently( httpResponse.getOutputStream() );
            }
        } else {
            SpicyPlate.LOGGER.debug( "SpicyServlet got a request for {} without any template match", templateName );
            httpResponse.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
    }

    protected String pathInfoToTemplateName( String pathInfo )
    {
        return pathInfo;
    }

    protected void populateGlobalContext( SpicyContext globalContext )
            throws ServletException
    {
        // NOOP
    }

    protected void populateRequestContext( HttpServletRequest req, SpicyContext requestContext )
            throws ServletException
    {
        // NOOP
    }

}
