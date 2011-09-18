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
import java.io.Reader;

public interface SpicyFactory
{

    SpicyFactory withDefaultGlobalContext( SpicyContext defaultGlobalContext );

    SpicyPlate spicyPlate( File templateFile );

    SpicyPlate spicyPlate( Reader templateReader );

    SpicyPlate spicyPlate( String template );

    SpicyPlate spicyPlate( SpicyContext globalContext, File templateFile );

    SpicyPlate spicyPlate( SpicyContext globalContext, Reader templateReader );

    SpicyPlate spicyPlate( SpicyContext globalContext, String template );

}