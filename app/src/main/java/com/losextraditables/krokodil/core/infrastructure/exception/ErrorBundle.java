/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.losextraditables.krokodil.core.infrastructure.exception;

public interface ErrorBundle {

  Exception getException();

  String getErrorMessage();
}
