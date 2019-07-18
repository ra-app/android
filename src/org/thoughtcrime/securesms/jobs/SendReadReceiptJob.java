package org.bittube.messenger.jobs;


import android.content.Context;

import org.bittube.messenger.database.Address;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.JobParameters;
import org.bittube.messenger.jobmanager.requirements.NetworkRequirement;
import org.bittube.messenger.logging.Log;
import org.bittube.messenger.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceReceiptMessage;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class SendReadReceiptJob extends ContextJob implements InjectableType {

  private static final long serialVersionUID = 1L;

  private static final String TAG = SendReadReceiptJob.class.getSimpleName();

  @Inject transient SignalServiceMessageSender messageSender;

  private final String     address;
  private final List<Long> messageIds;
  private final long       timestamp;

  public SendReadReceiptJob(Context context, Address address, List<Long> messageIds) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withPersistence()
                                .create());

    this.address    = address.serialize();
    this.messageIds = messageIds;
    this.timestamp  = System.currentTimeMillis();
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    if (!TextSecurePreferences.isReadReceiptsEnabled(context)) return;

    SignalServiceAddress        remoteAddress  = new SignalServiceAddress(address);
    SignalServiceReceiptMessage receiptMessage = new SignalServiceReceiptMessage(SignalServiceReceiptMessage.Type.READ, messageIds, timestamp);

    messageSender.sendReceipt(remoteAddress, receiptMessage);
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    if (e instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to send read receipts to: " + address);
  }
}
