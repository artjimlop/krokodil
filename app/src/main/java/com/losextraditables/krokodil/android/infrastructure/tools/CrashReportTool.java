package com.losextraditables.krokodil.android.infrastructure.tools;

public interface CrashReportTool {

  void logException(Throwable error);

  void logException(String message);
}
