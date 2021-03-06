/**
 * Copyright (C) 2016 Arturo Open Source Project <p/> Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.injector.component;

import com.losextraditables.krokodil.android.infrastructure.data.repositories.VideoDataSource;
import com.losextraditables.krokodil.android.infrastructure.injector.module.ApplicationModule;
import com.losextraditables.krokodil.android.views.activities.BaseActivity;
import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  void inject(BaseActivity baseActivity);

  ThreadExecutor getThreadExecutor();

  PostExecutionThread getPostExecutionThread();

  VideoRepository getVideoRepository();

  VideoDataSource getVideoDataSource();
}
