package org.bittube.messenger.jobs;

import android.content.Context;

import org.bittube.messenger.crypto.MasterSecret;
import org.bittube.messenger.database.DatabaseFactory;
import org.bittube.messenger.database.RecipientDatabase;
import org.bittube.messenger.database.RecipientDatabase.RecipientReader;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.JobParameters;
import org.bittube.messenger.jobmanager.requirements.NetworkRequirement;
import org.bittube.messenger.jobs.requirements.MasterSecretRequirement;
import org.bittube.messenger.recipients.Recipient;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.multidevice.BlockedListMessage;
import org.whispersystems.signalservice.api.messages.multidevice.SignalServiceSyncMessage;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class MultiDeviceBlockedUpdateJob extends MasterSecretJob implements InjectableType {

  private static final long serialVersionUID = 1L;

  private static final String TAG = MultiDeviceBlockedUpdateJob.class.getSimpleName();

  @Inject transient SignalServiceMessageSender messageSender;

  public MultiDeviceBlockedUpdateJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withRequirement(new MasterSecretRequirement(context))
                                .withGroupId(MultiDeviceBlockedUpdateJob.class.getSimpleName())
                                .withPersistence()
                                .create());
  }

  @Override
  public void onRun(MasterSecret masterSecret)
      throws IOException, UntrustedIdentityException
  {
    RecipientDatabase database = DatabaseFactory.getRecipientDatabase(context);

    try (RecipientReader reader = database.readerForBlocked(database.getBlocked())) {
      List<String> blocked = new LinkedList<>();

      Recipient recipient;

      while ((recipient = reader.getNext()) != null) {
        if (!recipient.isGroupRecipient()) {
          blocked.add(recipient.getAddress().serialize());
        }
      }

      messageSender.sendMessage(SignalServiceSyncMessage.forBlocked(new BlockedListMessage(blocked)));
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onCanceled() {

  }
}
