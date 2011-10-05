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
package org.codeartisans.spicyplates.velocity;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.codeartisans.java.toolbox.exceptions.NullArgumentException;
import org.codeartisans.spicyplates.AbstractSpicyFactory;
import org.codeartisans.spicyplates.SpicyContext;
import org.codeartisans.spicyplates.SpicyPlate;
import org.codeartisans.spicyplates.SpicyPlatesFailure;

public class VelocitySpicyFactory
        extends AbstractSpicyFactory
{

    private final VelocityEngine engine;

    public VelocitySpicyFactory()
    {
        engine = new VelocityEngine();
        engine.setProperty( Velocity.RUNTIME_LOG_LOGSYSTEM, new VelocitySlf4jBridge() );
        engine.init();
    }

    @Override
    public SpicyPlate spicyPlate( SpicyContext globalContext, String template )
            throws SpicyPlatesFailure
    {
        NullArgumentException.ensureNotNull( "Global context", globalContext );
        NullArgumentException.ensureNotEmpty( "Template", template );
        return new VelocitySpicyPlate( globalContext, template, engine );
    }

    /**
     * Bridge to SLF4J for Velocity logging system.
     * 
     * Default logging level is WARN.
     */
    private static class VelocitySlf4jBridge
            implements LogChute
    {

        @Override
        public void init( RuntimeServices rs )
                throws Exception
        {
            // NOOP
        }

        @Override
        public void log( int level, String message )
        {
            log( level, message, null );
        }

        @Override
        public void log( int level, String message, Throwable t )
        {
            switch ( level ) {
                case LogChute.DEBUG_ID:
                    SpicyPlate.LOGGER.debug( message, t );
                    break;
                case LogChute.ERROR_ID:
                    SpicyPlate.LOGGER.error( message, t );
                    break;
                case LogChute.INFO_ID:
                    SpicyPlate.LOGGER.info( message, t );
                    break;
                case LogChute.TRACE_ID:
                    SpicyPlate.LOGGER.trace( message, t );
                    break;
                case LogChute.WARN_ID:
                default:
                    SpicyPlate.LOGGER.warn( message, t );
                    break;
            }
        }

        @Override
        public boolean isLevelEnabled( int level )
        {
            switch ( level ) {
                case LogChute.DEBUG_ID:
                    return SpicyPlate.LOGGER.isDebugEnabled();
                case LogChute.ERROR_ID:
                    return SpicyPlate.LOGGER.isErrorEnabled();
                case LogChute.INFO_ID:
                    return SpicyPlate.LOGGER.isInfoEnabled();
                case LogChute.TRACE_ID:
                    return SpicyPlate.LOGGER.isTraceEnabled();
            }
            return SpicyPlate.LOGGER.isWarnEnabled();
        }

    }

}
