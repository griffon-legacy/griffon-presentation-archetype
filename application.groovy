/*
 * Copyright 2008-2012 the original author or authors.
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

import griffon.util.GriffonNameUtils
import griffon.util.Metadata

includeTargets << griffonScript('CreateMvc')

target(name: 'createApplicationProject',
       description: 'Creates a new application project',
       prehook: null, posthook: null) {
    createProjectWithDefaults()

    new File("${basedir}/griffon-app/conf/Application.groovy").text = '''
application {
    title = '@griffon.project.name@'
    startupGroups = ['DeckLauncher']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true
}
mvcGroups {
}
'''

    new File("${basedir}/griffon-app/conf/Config.groovy").append '''
presentation {
    screenWidth = 1024
    screenHeight = 768
    template = 'Default'
    order = []
}
'''
    ant.copy(todir: "${basedir}/griffon-app/resources", overwrite: true, force: true) {
        fileset(dir: "${archetypeDirPath}/griffon-app/resources")
    }

    ant.copy(todir: "${basedir}/griffon-app/slides", overwrite: true, force: true) {
        fileset(dir: "${archetypeDirPath}/griffon-app/slides")
    }

    ant.replace(dir: "${basedir}/griffon-app/conf") {
        replacefilter(token: "@griffon.project.name@", value: GriffonNameUtils.capitalize(griffonAppName))
    }

    Metadata md = Metadata.getInstance(new File("${basedir}/application.properties"))
    installPluginsLatest md, ['swing', 'slideware', "gfx-builder"]
}
setDefaultTarget(createApplicationProject)
