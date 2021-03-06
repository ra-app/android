package org.raapp.messenger.stickers;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import org.raapp.messenger.R;
import org.raapp.messenger.database.model.StickerRecord;
import org.raapp.messenger.mms.DecryptableStreamUriLoader.DecryptableUri;
import org.raapp.messenger.mms.GlideRequests;


/**
 * A popup that shows a given sticker fullscreen.
 */
final class StickerPreviewPopup extends PopupWindow {

  private final GlideRequests glideRequests;
  private final ImageView     image;

  StickerPreviewPopup(@NonNull Context context, @NonNull GlideRequests glideRequests) {
    super(LayoutInflater.from(context).inflate(R.layout.sticker_preview_popup, null),
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);
    this.glideRequests = glideRequests;
    this.image         = getContentView().findViewById(R.id.sticker_popup_image);

    setTouchable(false);
  }

  void presentSticker(@NonNull StickerRecord stickerRecord) {
    glideRequests.load(new DecryptableUri(stickerRecord.getUri()))
                 .into(image);
  }
}
