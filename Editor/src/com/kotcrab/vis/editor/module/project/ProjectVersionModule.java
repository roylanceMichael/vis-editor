/*
 * Copyright 2014-2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kotcrab.vis.editor.module.project;

import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.editor.App;
import com.kotcrab.vis.editor.module.InjectModule;

/** @author Kotcrab */
public class ProjectVersionModule extends ProjectModule {
	@InjectModule FileAccessModule fileAccess;

	@Override
	public void init () {
		Json json = new Json();
		json.addClassTag("ProjectVersionDescriptor", ProjectVersionDescriptor.class);
		json.toJson(new ProjectVersionDescriptor(App.VERSION_CODE, App.VERSION), fileAccess.getModuleFolder().child("version.json"));
	}
}