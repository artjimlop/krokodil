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
      YouTubeExtractor ytEx = new YouTubeExtractor(this) {

        @Override
        public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
            if (ytFiles == null) {
                return;
            }
            // Iterate over itags
            for (int i = 0, itag; i < ytFiles.size(); i++) {
                itag = ytFiles.keyAt(i);
                // ytFile represents one file with its url and meta data
                YtFile ytFile = ytFiles.get(itag);

                // Just add videos in a decent format => height -1 = audio
                if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                    addButtonToMainLayout(vMeta.getTitle(), ytFile);
                }
            }
        }
    };
    //ytEx.setIncludeWebM(false);
    //ytEx.setParseDashManifest(true);
    ytEx.extract(youtubeLink, true, false);
  }

  private void addButtonToMainLayout(final String videoTitle, final YtFile ytFrVideo) {
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
