package org.raapp.messenger.database.loaders;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import org.raapp.messenger.attachments.AttachmentId;
import org.raapp.messenger.database.AttachmentDatabase;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.mms.PartAuthority;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.AsyncLoader;

public class PagingMediaLoader extends AsyncLoader<Pair<Cursor, Integer>> {

  @SuppressWarnings("unused")
  private static final String TAG = PagingMediaLoader.class.getSimpleName();

  private final Recipient recipient;
  private final Uri       uri;
  private final boolean   leftIsRecent;

  public PagingMediaLoader(@NonNull Context context, @NonNull Recipient recipient, @NonNull Uri uri, boolean leftIsRecent) {
    super(context);
    this.recipient    = recipient;
    this.uri          = uri;
    this.leftIsRecent = leftIsRecent;
  }

  @Nullable
  @Override
  public Pair<Cursor, Integer> loadInBackground() {
    long   threadId = DatabaseFactory.getThreadDatabase(getContext()).getThreadIdFor(recipient);
    Cursor cursor   = DatabaseFactory.getMediaDatabase(getContext()).getGalleryMediaForThread(threadId);

    while (cursor != null && cursor.moveToNext()) {
      AttachmentId attachmentId  = new AttachmentId(cursor.getLong(cursor.getColumnIndexOrThrow(AttachmentDatabase.ROW_ID)), cursor.getLong(cursor.getColumnIndexOrThrow(AttachmentDatabase.UNIQUE_ID)));
      Uri          attachmentUri = PartAuthority.getAttachmentDataUri(attachmentId);

      if (attachmentUri.equals(uri)) {
        return new Pair<>(cursor, leftIsRecent ? cursor.getPosition() : cursor.getCount() - 1 - cursor.getPosition());
      }
    }

    return null;
  }
}