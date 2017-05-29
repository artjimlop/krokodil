/**
 * Copyright (C) 2016 Arturo Open Source Project <p/> Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.krokodil.AndroidApplication;
import com.losextraditables.krokodil.BuildConfig;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.infrastructure.data.models.version.Version;
import com.losextraditables.krokodil.android.infrastructure.injector.component.ApplicationComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.component.DaggerVideosComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.component.VideosComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.module.ActivityModule;
import com.losextraditables.krokodil.android.infrastructure.injector.module.VideoModule;

public abstract class BaseActivity extends AppCompatActivity {

  private VideosComponent component;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeInjector(getApplicationComponent());
    Firebase.setAndroidContext(this);
    checkUpdateNeeded();
  }

  private void checkUpdateNeeded() {
    Firebase myFirebaseRef = new Firebase(getAndroidApplication().getFirebaseEndpoint());
    myFirebaseRef.child("version").addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot snapshot) {
        GenericTypeIndicator<Version> t = new GenericTypeIndicator<Version>() {
        };
        Version version = snapshot.getValue(t);
        if (BuildConfig.VERSION_CODE < version.getVersion()) {
          showUpdateRequired(version.getDownloadLink());
        }
      }

      @Override public void onCancelled(FirebaseError error) {
      }
    });
  }

  protected void setUpToolbar(boolean showUpButton, String title) {
    //TODO
  }

  private ApplicationComponent getApplicationComponent() {
    return getAndroidApplication().getComponent();
  }

  public AndroidApplication getAndroidApplication() {
    return (AndroidApplication) getApplication();
  }

  private void initializeInjector(ApplicationComponent applicationComponent){
    applicationComponent.inject(this);
    component = DaggerVideosComponent.builder()
        .applicationComponent(applicationComponent)
        .activityModule(new ActivityModule(this))
        .videoModule(new VideoModule())
        .build();
  }

  public VideosComponent getComponent() {
    return component;
  }

  private void showUpdateRequired(String downloadLink) {
    new AlertDialog.Builder(this).setTitle(R.string.update_alert_title)
        .setMessage(R.string.update_alert_message)
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
          dialog.dismiss();
        })
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
          goToWebsite(downloadLink);
          dialog.dismiss();
        })
        .show();
  }

  private void goToWebsite(String downloadLink) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(downloadLink));
    startActivity(i);
  }
}
