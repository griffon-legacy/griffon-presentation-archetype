/*
 * Copyright 2010-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */
class PresentationGriffonArchetype {
    String version = '1.1'
    String griffonVersion = '0.9.5 > *'
    String license = 'Apache Software License 2.0'
    String documentation = ''
    String source = 'https://github.com/griffon/griffon-presentation-archetype'
    List authors = [
            [
                    name: 'Andres Almiray',
                    email: 'aalmiray@yahoo.com'
            ],
            [
                    name: 'Alexander Klein',
                    email: 'info@aklein.org'
            ]
    ]
    String title = 'Creates interactive presentations'
    String description = '''
Creates interactive presentations where each slide is a Swing based View. You
can even run code inside the slides.

Usage
----
Simply specify the name of the archetype (*presentation*) when invoking the `create-app`
command, like this

    griffon create-app sample -archetype=presentation
    
This will configure a default startup MVC group in `griffon-app/conf/Application.groovy`
plus installing the [slideware][1] plugin and all of its dependencies.

Refer to the documentation of the [slideware][1] plugin to learn more.
 
Configuration
-------------
No additional configuration required.

[1]: http://griffon.codehaus.org/Slideware+Plugin
'''
}
