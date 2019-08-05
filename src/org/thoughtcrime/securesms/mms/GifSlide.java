package org.raapp.messenger.mms;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.raapp.messenger.attachments.Attachment;
import org.raapp.messenger.util.MediaUtil;

public class GifSlide extends ImageSlide {

  public GifSlide(Context context, Attachment attachment) {
    super(context, attachment);
  }

  public GifSlide(Context context, Uri uri, long size, int width, int height) {
    super(context, constructAttachmentFromUri(context, uri, MediaUtil.IMAGE_GIF, size, width, height, true, null, false, false));
  }

  @Override
  @Nullable
  public Uri getThumbnailUri() {
    return getUri();
  }
}
