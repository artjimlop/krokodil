/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.losextraditables.krokodil.core.infrastructure.executor;

public interface PostExecutionThread {
  void post(Runnable runnable);
}
