package org.raapp.messenger.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.raapp.messenger.R;
import org.raapp.messenger.attachments.Attachment;
import org.raapp.messenger.attachments.AttachmentId;
import org.raapp.messenger.attachments.DatabaseAttachment;
import org.raapp.messenger.attachments.PointerAttachment;
import org.raapp.messenger.database.AttachmentDatabase;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.dependencies.InjectableType;
import org.raapp.messenger.events.PartProgressEvent;
import org.raapp.messenger.jobmanager.Data;
import org.raapp.messenger.jobmanager.Job;
import org.raapp.messenger.jobmanager.impl.NetworkConstraint;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.MediaConstraints;
import org.raapp.messenger.mms.MediaStream;
import org.raapp.messenger.mms.MmsException;
import org.raapp.messenger.mms.PartAuthority;
import org.raapp.messenger.service.GenericForegroundService;
import org.raapp.messenger.service.NotificationController;
import org.raapp.messenger.transport.UndeliverableMessageException;
import org.raapp.messenger.util.MediaUtil;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachment;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentPointer;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class AttachmentUploadJob extends BaseJob implements InjectableType {

  public static final String KEY = "AttachmentUploadJob";

  private static final String TAG = AttachmentUploadJob.class.getSimpleName();

  private static final String KEY_ROW_ID    = "row_id";
  private static final String KEY_UNIQUE_ID = "unique_id";

  /**
   * Foreground notification shows while uploading attachments above this.
   */
  private static final int FOREGROUND_LIMIT = 10 * 1024 * 1024;

  private AttachmentId               attachmentId;
  @Inject SignalServiceMessageSender messageSender;

  public AttachmentUploadJob(AttachmentId attachmentId) {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build(),
         attachmentId);
  }

  private AttachmentUploadJob(@NonNull Job.Parameters parameters, @NonNull AttachmentId attachmentId) {
    super(parameters);
    this.attachmentId = attachmentId;
  }

  @Override
  public @NonNull Data serialize() {
    return new Data.Builder().putLong(KEY_ROW_ID, attachmentId.getRowId())
                             .putLong(KEY_UNIQUE_ID, attachmentId.getUniqueId())
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws Exception {
    AttachmentDatabase database           = DatabaseFactory.getAttachmentDatabase(context);
    DatabaseAttachment databaseAttachment = database.getAttachment(attachmentId);

    if (databaseAttachment == null) {
      throw new IllegalStateException("Cannot find the specified attachment.");
    }

    MediaConstraints mediaConstraints = MediaConstraints.getPushMediaConstraints();
    Attachment       scaledAttachment = scaleAndStripExif(database, mediaConstraints, databaseAttachment);

    try (NotificationController notification = getNotificationForAttachment(scaledAttachment)) {
      SignalServiceAttachment        localAttachment  = getAttachmentFor(scaledAttachment, notification);
      SignalServiceAttachmentPointer remoteAttachment = messageSender.uploadAttachment(localAttachment.asStream(), databaseAttachment.isSticker());
      Attachment                     attachment       = PointerAttachment.forPointer(Optional.of(remoteAttachment), null, databaseAttachment.getFastPreflightId()).get();

      database.updateAttachmentAfterUpload(databaseAttachment.getAttachmentId(), attachment);
    }
  }

  private @Nullable NotificationController getNotificationForAttachment(@NonNull Attachment attachment) {
    if (attachment.getSize() >= FOREGROUND_LIMIT) {
      return GenericForegroundService.startForegroundTask(context, context.getString(R.string.AttachmentUploadJob_uploading_media));
    } else {
      return null;
    }
  }

  @Override
  public void onCanceled() { }

  @Override
  protected boolean onShouldRetry(@NonNull Exception exception) {
    return exception instanceof IOException;
  }

  private SignalServiceAttachment getAttachmentFor(Attachment attachment, @Nullable NotificationController notification) {
    try {
      if (attachment.getDataUri() == null || attachment.getSize() == 0) throw new IOException("Assertion failed, outgoing attachment has no data!");
      InputStream is = PartAuthority.getAttachmentStream(context, attachment.getDataUri());
      return SignalServiceAttachment.newStreamBuilder()
                                    .withStream(is)
                                    .withContentType(attachment.getContentType())
                                    .withLength(attachment.getSize())
                                    .withFileName(attachment.getFileName())
                                    .withVoiceNote(attachment.isVoiceNote())
                                    .withWidth(attachment.getWidth())
                                    .withHeight(attachment.getHeight())
                                    .withCaption(attachment.getCaption())
                                    .withListener((total, progress) -> {
                                      EventBus.getDefault().postSticky(new PartProgressEvent(attachment, total, progress));
                                      if (notification != null) {
                                        notification.setProgress(total, progress);
                                      }
                                    })
                                    .build();
    } catch (IOException ioe) {
      Log.w(TAG, "Couldn't open attachment", ioe);
    }
    return null;
  }

  private Attachment scaleAndStripExif(@NonNull AttachmentDatabase attachmentDatabase,
                                       @NonNull MediaConstraints constraints,
                                       @NonNull Attachment attachment)
      throws UndeliverableMessageException
  {
    try {
      if (constraints.isSatisfied(context, attachment)) {
        if (MediaUtil.isJpeg(attachment)) {
          MediaStream stripped = constraints.getResizedMedia(context, attachment);
          return attachmentDatabase.updateAttachmentData(attachment, stripped);
        } else {
          return attachment;
        }
      } else if (constraints.canResize(attachment)) {
        MediaStream resized = constraints.getResizedMedia(context, attachment);
        return attachmentDatabase.updateAttachmentData(attachment, resized);
      } else {
        throw new UndeliverableMessageException("Size constraints could not be met!");
      }
    } catch (IOException | MmsException e) {
      throw new UndeliverableMessageException(e);
    }
  }

  public static final class Factory implements Job.Factory<AttachmentUploadJob> {
    @Override
    public @NonNull AttachmentUploadJob create(@NonNull Parameters parameters, @NonNull org.raapp.messenger.jobmanager.Data data) {
      return new AttachmentUploadJob(parameters, new AttachmentId(data.getLong(KEY_ROW_ID), data.getLong(KEY_UNIQUE_ID)));
    }
  }
}
