package com.losextraditables.krokodil.android.infrastructure.tools;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.SparseArray;
import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Downloader {

  private Context context;
  private ArrayList<YtFragmentedVideo> formatsToShowList;

  public Downloader() {
  }

  public void getYoutubeDownloadUrl(String youtubeLink, Context context) {
    this.context = context;
    YouTubeUriExtractor ytEx = new YouTubeUriExtractor(context) {

      @Override
      public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {
        formatsToShowList = new ArrayList<>();
        for (int i = 0, itag; i < ytFiles.size(); i++) {
          itag = ytFiles.keyAt(i);
          YtFile ytFile = ytFiles.get(itag);

          if (ytFile.getMeta().getHeight() == -1 || ytFile.getMeta().getHeight() >= 360) {
            addFormatToList(ytFile, ytFiles);
          }
        }
        for (YtFragmentedVideo files : formatsToShowList) {
          addButtonToMainLayout(videoTitle, files);
        }
      }
    };
    ytEx.setIncludeWebM(false);
    ytEx.setParseDashManifest(true);
    ytEx.execute(youtubeLink);
  }

  private void addFormatToList(YtFile ytFile, SparseArray<YtFile> ytFiles) {
    int height = ytFile.getMeta().getHeight();
    YtFragmentedVideo frVideo = new YtFragmentedVideo();
    frVideo.height = height;
    if (ytFile.getMeta().isDashContainer()) {
      if (height > 0) {
        frVideo.videoFile = ytFile;
      } else {
        frVideo.audioFile = ytFile;
      }
    } else {
      frVideo.videoFile = ytFile;
    }
    formatsToShowList.add(frVideo);
  }

  private void addButtonToMainLayout(final String videoTitle, final YtFragmentedVideo ytFrVideo) {
    String filename;
    if (videoTitle.length() > 55) {
      filename = videoTitle.substring(0, 55);
    } else {
      filename = videoTitle;
    }
    filename = filename.replaceAll("\\\\|>|<|\"|\\||\\*|\\?|%|:|#|/", "");
    filename += (ytFrVideo.height == -1) ? "" : "-" + ytFrVideo.height + "p";
    String downloadIds = "";
    boolean hideAudioDownloadNotification = false;
    if (ytFrVideo.audioFile != null) {
      downloadIds += downloadFromUrl(ytFrVideo.audioFile.getUrl(), videoTitle,
          filename + "." + ytFrVideo.audioFile.getMeta().getExt(), hideAudioDownloadNotification);
    }
    if (ytFrVideo.audioFile != null) cacheDownloadIds(downloadIds);
  }

  private long downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName,
      boolean hide) {
    Uri uri = Uri.parse(youtubeDlUrl);
    DownloadManager.Request request = new DownloadManager.Request(uri);
    request.setTitle(downloadTitle);
    if (hide) {
      request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
      request.setVisibleInDownloadsUi(false);
    } else {
      request.setNotificationVisibility(
          DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    }

    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

    DownloadManager manager =
        (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    return manager.enqueue(request);
  }

  private void cacheDownloadIds(String downloadIds) {
    File dlCacheFile = new File(context.getCacheDir().getAbsolutePath() + "/" + downloadIds);
    try {
      dlCacheFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private class YtFragmentedVideo {
    int height;
    YtFile audioFile;
    YtFile videoFile;
  }
}
