package org.bittube.messenger.jobs;


import androidx.annotation.NonNull;

import org.bittube.messenger.jobmanager.Data;
import org.bittube.messenger.jobmanager.Job;
import org.bittube.messenger.jobmanager.impl.NetworkConstraint;
import org.bittube.messenger.logging.Log;

import org.bittube.messenger.crypto.ProfileKeyUtil;
import org.bittube.messenger.crypto.UnidentifiedAccessUtil;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.util.TextSecurePreferences;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachment;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentStream;
import org.whispersystems.signalservice.api.messages.multidevice.ContactsMessage;
import org.whispersystems.signalservice.api.messages.multidevice.DeviceContact;
import org.whispersystems.signalservice.api.messages.multidevice.DeviceContactsOutputStream;
import org.whispersystems.signalservice.api.messages.multidevice.SignalServiceSyncMessage;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MultiDeviceProfileKeyUpdateJob extends BaseJob implements InjectableType {

  public static String KEY = "MultiDeviceProfileKeyUpdateJob";

  private static final String TAG = MultiDeviceProfileKeyUpdateJob.class.getSimpleName();

  @Inject SignalServiceMessageSender messageSender;

  public MultiDeviceProfileKeyUpdateJob() {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setQueue("MultiDeviceProfileKeyUpdateJob")
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build());
  }

  private MultiDeviceProfileKeyUpdateJob(@NonNull Job.Parameters parameters) {
    super(parameters);
  }

  @Override
  public @NonNull Data serialize() {
    return Data.EMPTY;
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    if (!TextSecurePreferences.isMultiDevice(context)) {
      Log.i(TAG, "Not multi device...");
      return;
    }

    Optional<byte[]>           profileKey = Optional.of(ProfileKeyUtil.getProfileKey(context));
    ByteArrayOutputStream      baos       = new ByteArrayOutputStream();
    DeviceContactsOutputStream out        = new DeviceContactsOutputStream(baos);

    out.write(new DeviceContact(TextSecurePreferences.getLocalNumber(context),
                                Optional.absent(),
                                Optional.absent(),
                                Optional.absent(),
                                Optional.absent(),
                                profileKey, false, Optional.absent()));

    out.close();

    SignalServiceAttachmentStream attachmentStream = SignalServiceAttachment.newStreamBuilder()
                                                                            .withStream(new ByteArrayInputStream(baos.toByteArray()))
                                                                            .withContentType("application/octet-stream")
                                                                            .withLength(baos.toByteArray().length)
                                                                            .build();

    SignalServiceSyncMessage      syncMessage      = SignalServiceSyncMessage.forContacts(new ContactsMessage(attachmentStream, false));

    messageSender.sendMessage(syncMessage, UnidentifiedAccessUtil.getAccessForSync(context));
  }

  @Override
  public boolean onShouldRetry(@NonNull Exception exception) {
    if (exception instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Profile key sync failed!");
  }

  public static final class Factory implements Job.Factory<MultiDeviceProfileKeyUpdateJob> {
    @Override
    public @NonNull MultiDeviceProfileKeyUpdateJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new MultiDeviceProfileKeyUpdateJob(parameters);
    }
  }
}
