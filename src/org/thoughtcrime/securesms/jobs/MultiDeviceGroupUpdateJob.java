package org.bittube.messenger.jobs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.bittube.messenger.crypto.UnidentifiedAccessUtil;
import org.bittube.messenger.database.Address;
import org.bittube.messenger.database.DatabaseFactory;
import org.bittube.messenger.database.GroupDatabase;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.Data;
import org.bittube.messenger.jobmanager.Job;
import org.bittube.messenger.jobmanager.impl.NetworkConstraint;
import org.bittube.messenger.logging.Log;
import org.bittube.messenger.recipients.Recipient;
import org.bittube.messenger.util.GroupUtil;
import org.bittube.messenger.util.TextSecurePreferences;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachment;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentStream;
import org.whispersystems.signalservice.api.messages.multidevice.DeviceGroup;
import org.whispersystems.signalservice.api.messages.multidevice.DeviceGroupsOutputStream;
import org.whispersystems.signalservice.api.messages.multidevice.SignalServiceSyncMessage;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MultiDeviceGroupUpdateJob extends BaseJob implements InjectableType {

  public static final String KEY = "MultiDeviceGroupUpdateJob";

  private static final String TAG = MultiDeviceGroupUpdateJob.class.getSimpleName();

  @Inject SignalServiceMessageSender messageSender;

  public MultiDeviceGroupUpdateJob() {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setQueue("MultiDeviceGroupUpdateJob")
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build());
  }

  private MultiDeviceGroupUpdateJob(@NonNull Job.Parameters parameters) {
    super(parameters);
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public @NonNull Data serialize() {
    return Data.EMPTY;
  }

  @Override
  public void onRun() throws Exception {
    if (!TextSecurePreferences.isMultiDevice(context)) {
      Log.i(TAG, "Not multi device, aborting...");
      return;
    }

    File                 contactDataFile = createTempFile("multidevice-contact-update");
    GroupDatabase.Reader reader          = null;

    GroupDatabase.GroupRecord record;

    try {
      DeviceGroupsOutputStream out = new DeviceGroupsOutputStream(new FileOutputStream(contactDataFile));

      reader = DatabaseFactory.getGroupDatabase(context).getGroups();

      while ((record = reader.getNext()) != null) {
        if (!record.isMms()) {
          List<String> members = new LinkedList<>();

          for (Address member : record.getMembers()) {
            members.add(member.serialize());
          }

          Recipient         recipient       = Recipient.from(context, Address.fromSerialized(GroupUtil.getEncodedId(record.getId(), record.isMms())), false);
          Optional<Integer> expirationTimer = recipient.getExpireMessages() > 0 ? Optional.of(recipient.getExpireMessages()) : Optional.absent();

          out.write(new DeviceGroup(record.getId(), Optional.fromNullable(record.getTitle()),
                                    members, getAvatar(record.getAvatar()),
                                    record.isActive(), expirationTimer,
                                    Optional.of(recipient.getColor().serialize()),
                                    recipient.isBlocked()));
        }
      }

      out.close();

      if (contactDataFile.exists() && contactDataFile.length() > 0) {
        sendUpdate(messageSender, contactDataFile);
      } else {
        Log.w(TAG, "No groups present for sync message...");
      }

    } finally {
      if (contactDataFile != null) contactDataFile.delete();
      if (reader != null)          reader.close();
    }

  }

  @Override
  public boolean onShouldRetry(@NonNull Exception exception) {
    if (exception instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onCanceled() {

  }

  private void sendUpdate(SignalServiceMessageSender messageSender, File contactsFile)
      throws IOException, UntrustedIdentityException
  {
    FileInputStream               contactsFileStream = new FileInputStream(contactsFile);
    SignalServiceAttachmentStream attachmentStream   = SignalServiceAttachment.newStreamBuilder()
                                                                              .withStream(contactsFileStream)
                                                                              .withContentType("application/octet-stream")
                                                                              .withLength(contactsFile.length())
                                                                              .build();

    messageSender.sendMessage(SignalServiceSyncMessage.forGroups(attachmentStream),
                              UnidentifiedAccessUtil.getAccessForSync(context));
  }


  private Optional<SignalServiceAttachmentStream> getAvatar(@Nullable byte[] avatar) {
    if (avatar == null) return Optional.absent();

    return Optional.of(SignalServiceAttachment.newStreamBuilder()
                                              .withStream(new ByteArrayInputStream(avatar))
                                              .withContentType("image/*")
                                              .withLength(avatar.length)
                                              .build());
  }

  private File createTempFile(String prefix) throws IOException {
    File file = File.createTempFile(prefix, "tmp", context.getCacheDir());
    file.deleteOnExit();

    return file;
  }

  public static final class Factory implements Job.Factory<MultiDeviceGroupUpdateJob> {
    @Override
    public @NonNull MultiDeviceGroupUpdateJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new MultiDeviceGroupUpdateJob(parameters);
    }
  }
}
