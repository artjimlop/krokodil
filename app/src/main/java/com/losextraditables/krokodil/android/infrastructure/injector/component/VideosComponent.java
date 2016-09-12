/**
 * Copyright (C) 2016 Arturo Open Source Project <p/> Licensed under the Apache License, Version
 * 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.injector.component;

import com.losextraditables.krokodil.android.infrastructure.injector.PerActivity;
import com.losextraditables.krokodil.android.infrastructure.injector.module.ActivityModule;
import com.losextraditables.krokodil.android.infrastructure.injector.module.VideoModule;
import com.losextraditables.krokodil.android.views.activities.MainActivity;
import com.losextraditables.krokodil.android.views.activities.SearchActivity;
import com.losextraditables.krokodil.android.views.activities.VideoDetailActivity;
import dagger.Component;

@PerActivity @Component(dependencies = ApplicationComponent.class, modules = {
    ActivityModule.class, VideoModule.class
}) public interface VideosComponent extends ActivityComponent {

  void inject(MainActivity mainActivity);

  void inject(SearchActivity searchActivity);

  void inject(VideoDetailActivity videoDetailActivity);
}
