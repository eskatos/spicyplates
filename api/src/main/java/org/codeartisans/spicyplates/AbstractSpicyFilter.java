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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codeartisans.java.toolbox.StringUtils;
import org.codeartisans.java.toolbox.io.IO;

public abstract class AbstractSpicyFilter
        implements Filter
{

    private boolean allowQuery = false;
    private SpicyContext globalContext;
    private SpicyRepository repository;

    protected abstract SpicyRepository classpathRepository( SpicyContext globalContext, String rootPackage );

    protected abstract SpicyRepository directoryRepository( SpicyContext globalContext, File rootDirectory );

    protected abstract SpicyRepository webResourcesRepository( SpicyContext globalContext, ServletContext servletContext );

    static class FilterDelegate
            implements SpicyServletSupport.Delegate
    {

        private final AbstractSpicyFilter filter;

        public FilterDelegate( AbstractSpicyFilter filter )
        {
            this.filter = filter;
        }

        @Override
        public SpicyRepository classpathRepository( String rootPackage )
        {
            return filter.classpathRepository( filter.globalContext, rootPackage );
        }

        @Override
        public SpicyRepository directoryRepository( File rootDirectory )
        {
            return filter.directoryRepository( filter.globalContext, rootDirectory );
        }

        @Override
        public SpicyRepository webResourcesRepository( ServletContext servletContext )
        {
            return filter.webResourcesRepository( filter.globalContext, servletContext );
        }

    }

    @Override
    public final void init( FilterConfig filterConfig )
            throws ServletException
    {
        allowQuery = SpicyServletSupport.allowQuery( filterConfig );
        globalContext = SpicyServletSupport.buildGlobalContext( filterConfig );
        populateGlobalContext( globalContext );
        repository = SpicyServletSupport.buildRepository( filterConfig, new FilterDelegate( this ) );
        SpicyPlate.LOGGER.info( "SpicyFilter initialized" );
    }

    @Override
    public final void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException, ServletException
    {
        HttpServletRequest httpRequest = ( HttpServletRequest ) request;
        HttpServletResponse httpResponse = ( HttpServletResponse ) response;
        String templateName = httpRequest.getRequestURI();
        if ( !StringUtils.isEmpty( templateName ) && templateName.startsWith( "/" ) ) {
            templateName = templateName.substring( 1 );
        }
        templateName = mapTemplateName( templateName );
        SpicyPlate template = repository.get( templateName );
        if ( template != null ) {
            Writer writer = null;
            try {
                writer = new OutputStreamWriter( httpResponse.getOutputStream() );
                SpicyContext requestContext = new SimpleSpicyContext();
                if ( allowQuery ) {
                    requestContext.putAll( httpRequest.getParameterMap() );
                }
                populateRequestContext( httpRequest, requestContext );
                template.render( requestContext, writer );
                SpicyPlate.LOGGER.debug( "SpicyFilter rendered {} template", templateName );
            } catch ( Exception ex ) {
                String message = "SpicyFilter was unable to render template " + templateName;
                SpicyPlate.LOGGER.error( message, ex );
                httpResponse.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message );
            } finally {
                IO.closeSilently( writer );
                IO.closeSilently( httpResponse.getOutputStream() );
            }
        } else {
            SpicyPlate.LOGGER.debug( "SpicyFilter got a request for {} without any template match", templateName );
            chain.doFilter( request, response );
        }
    }

    @Override
    public final void destroy()
    {
        allowQuery = false;
        globalContext = null;
        repository = null;
        SpicyPlate.LOGGER.info( "SpicyFilter destroyed" );
    }

    protected String mapTemplateName( String originalTemplateName )
    {
        return originalTemplateName;
    }

    protected void populateGlobalContext( SpicyContext globalContext )
    {
        // NOOP
    }

    protected void populateRequestContext( HttpServletRequest req, SpicyContext requestContext )
            throws ServletException
    {
        // NOOP
    }

}
