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

public class SpicyPlatesFailure
        extends RuntimeException
{

    public SpicyPlatesFailure( String message )
    {
        super( message );
    }

    public SpicyPlatesFailure( Throwable cause )
    {
        super( cause.getMessage(), cause );
    }

    public SpicyPlatesFailure( String message, Throwable cause )
    {
        super( message, cause );
    }

    public SpicyPlatesFailure andLogError( String details )
    {
        SpicyPlate.LOGGER.error( details, this );
        return this;
    }

}
