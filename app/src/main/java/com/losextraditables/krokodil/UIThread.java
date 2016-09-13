/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 */
package com.losextraditables.krokodil;

import android.os.Handler;
import android.os.Looper;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UIThread implements PostExecutionThread {

  private final Handler handler;

  @Inject
  public UIThread() {
    this.handler = new Handler(Looper.getMainLooper());
  }

  @Override
  public void post(Runnable runnable) {
    handler.post(runnable);
  }
}
