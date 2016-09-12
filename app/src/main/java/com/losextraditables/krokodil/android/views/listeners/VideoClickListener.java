package com.losextraditables.krokodil.android.views.listeners;

public interface VideoClickListener {
  void onClick(String thumbnail, String time, String title, String author, String visits,
      String description, String url);
}