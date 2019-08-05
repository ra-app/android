package org.raapp.messenger.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import org.raapp.messenger.BuildConfig;
import org.raapp.messenger.TextSecureExpiredException;
import org.raapp.messenger.attachments.Attachment;
import org.raapp.messenger.crypto.MasterSecret;
import org.raapp.messenger.database.AttachmentDatabase;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.jobmanager.JobParameters;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.MediaConstraints;
import org.raapp.messenger.mms.MediaStream;
import org.raapp.messenger.mms.MmsException;
import org.raapp.messenger.transport.UndeliverableMessageException;
import org.raapp.messenger.util.MediaUtil;
import org.raapp.messenger.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class SendJob extends MasterSecretJob {

  @SuppressWarnings("unused")
  private final static String TAG = SendJob.class.getSimpleName();

  public SendJob(Context context, JobParameters parameters) {
    super(context, parameters);
  }

  @Override
  public final void onRun(MasterSecret masterSecret) throws Exception {
    if (Util.getDaysTillBuildExpiry() <= 0) {
      throw new TextSecureExpiredException(String.format("TextSecure expired (build %d, now %d)",
                                                         BuildConfig.BUILD_TIMESTAMP,
                                                         System.currentTimeMillis()));
    }

    Log.i(TAG, "Starting message send attempt");
    onSend(masterSecret);
    Log.i(TAG, "Message send completed");
  }

  protected abstract void onSend(MasterSecret masterSecret) throws Exception;

  protected void markAttachmentsUploaded(long messageId, @NonNull List<Attachment> attachments) {
    AttachmentDatabase database = DatabaseFactory.getAttachmentDatabase(context);

    for (Attachment attachment : attachments) {
      database.markAttachmentUploaded(messageId, attachment);
    }
  }

  protected List<Attachment> scaleAndStripExifFromAttachments(@NonNull MediaConstraints constraints,
                                                              @NonNull List<Attachment> attachments)
      throws UndeliverableMessageException
  {
    AttachmentDatabase attachmentDatabase = DatabaseFactory.getAttachmentDatabase(context);
    List<Attachment>   results            = new LinkedList<>();

    for (Attachment attachment : attachments) {
      try {
        if (constraints.isSatisfied(context, attachment)) {
          if (MediaUtil.isJpeg(attachment)) {
            MediaStream stripped = constraints.getResizedMedia(context, attachment);
            results.add(attachmentDatabase.updateAttachmentData(attachment, stripped));
          } else {
            results.add(attachment);
          }
        } else if (constraints.canResize(attachment)) {
          MediaStream resized = constraints.getResizedMedia(context, attachment);
          results.add(attachmentDatabase.updateAttachmentData(attachment, resized));
        } else {
          throw new UndeliverableMessageException("Size constraints could not be met!");
        }
      } catch (IOException | MmsException e) {
        throw new UndeliverableMessageException(e);
      }
    }

    return results;
  }
}
