package org.bittube.messenger.jobs;


import androidx.annotation.NonNull;

import org.bittube.messenger.crypto.UnidentifiedAccessUtil;
import org.bittube.messenger.database.Address;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.Data;
import org.bittube.messenger.jobmanager.Job;
import org.bittube.messenger.jobmanager.impl.NetworkConstraint;
import org.bittube.messenger.logging.Log;
import org.bittube.messenger.recipients.Recipient;
import org.bittube.messenger.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceReceiptMessage;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class SendReadReceiptJob extends BaseJob implements InjectableType {

  public static final String KEY = "SendReadReceiptJob";

  private static final String TAG = SendReadReceiptJob.class.getSimpleName();

  private static final String KEY_ADDRESS     = "address";
  private static final String KEY_MESSAGE_IDS = "message_ids";
  private static final String KEY_TIMESTAMP   = "timestamp";

  @Inject SignalServiceMessageSender messageSender;

  private String     address;
  private List<Long> messageIds;
  private long       timestamp;

  public SendReadReceiptJob(Address address, List<Long> messageIds) {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build(),
         address,
         messageIds,
         System.currentTimeMillis());
  }

  private SendReadReceiptJob(@NonNull Job.Parameters parameters,
                             @NonNull Address address,
                             @NonNull List<Long> messageIds,
                             long timestamp)
  {
    super(parameters);

    this.address    = address.serialize();
    this.messageIds = messageIds;
    this.timestamp  = timestamp;
  }

  @Override
  public @NonNull Data serialize() {
    long[] ids = new long[messageIds.size()];
    for (int i = 0; i < ids.length; i++) {
      ids[i] = messageIds.get(i);
    }

    return new Data.Builder().putString(KEY_ADDRESS, address)
                             .putLongArray(KEY_MESSAGE_IDS, ids)
                             .putLong(KEY_TIMESTAMP, timestamp)
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    if (!TextSecurePreferences.isReadReceiptsEnabled(context) || messageIds.isEmpty()) return;

    SignalServiceAddress        remoteAddress  = new SignalServiceAddress(address);
    SignalServiceReceiptMessage receiptMessage = new SignalServiceReceiptMessage(SignalServiceReceiptMessage.Type.READ, messageIds, timestamp);

    messageSender.sendReceipt(remoteAddress,
                              UnidentifiedAccessUtil.getAccessFor(context, Recipient.from(context, Address.fromSerialized(address), false)),
                              receiptMessage);
  }

  @Override
  public boolean onShouldRetry(@NonNull Exception e) {
    if (e instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to send read receipts to: " + address);
  }

  public static final class Factory implements Job.Factory<SendReadReceiptJob> {
    @Override
    public @NonNull SendReadReceiptJob create(@NonNull Parameters parameters, @NonNull Data data) {
      Address    address    = Address.fromSerialized(data.getString(KEY_ADDRESS));
      long       timestamp  = data.getLong(KEY_TIMESTAMP);
      long[]     ids        = data.hasLongArray(KEY_MESSAGE_IDS) ? data.getLongArray(KEY_MESSAGE_IDS) : new long[0];
      List<Long> messageIds = new ArrayList<>(ids.length);

      for (long id : ids) {
        messageIds.add(id);
      }

      return new SendReadReceiptJob(parameters, address, messageIds, timestamp);
    }
  }
}
