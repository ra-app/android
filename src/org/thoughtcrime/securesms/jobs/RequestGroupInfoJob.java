package org.bittube.messenger.jobs;

import androidx.annotation.NonNull;

import org.bittube.messenger.crypto.UnidentifiedAccessUtil;
import org.bittube.messenger.database.Address;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.Data;
import org.bittube.messenger.jobmanager.Job;
import org.bittube.messenger.jobmanager.impl.NetworkConstraint;
import org.bittube.messenger.util.GroupUtil;
import org.bittube.messenger.recipients.Recipient;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceDataMessage;
import org.whispersystems.signalservice.api.messages.SignalServiceGroup;
import org.whispersystems.signalservice.api.messages.SignalServiceGroup.Type;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class RequestGroupInfoJob extends BaseJob implements InjectableType {

  public static final String KEY = "RequestGroupInfoJob";

  @SuppressWarnings("unused")
  private static final String TAG = RequestGroupInfoJob.class.getSimpleName();

  private static final String KEY_SOURCE   = "source";
  private static final String KEY_GROUP_ID = "group_id";

  @Inject SignalServiceMessageSender messageSender;

  private String source;
  private byte[] groupId;

  public RequestGroupInfoJob(@NonNull String source, @NonNull byte[] groupId) {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build(),
         source,
         groupId);

  }

  private RequestGroupInfoJob(@NonNull Job.Parameters parameters, @NonNull String source, @NonNull byte[] groupId) {
    super(parameters);

    this.source  = source;
    this.groupId = groupId;
  }

  @Override
  public @NonNull Data serialize() {
    return new Data.Builder().putString(KEY_SOURCE, source)
                             .putString(KEY_GROUP_ID, GroupUtil.getEncodedId(groupId, false))
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    SignalServiceGroup       group   = SignalServiceGroup.newBuilder(Type.REQUEST_INFO)
                                                         .withId(groupId)
                                                         .build();

    SignalServiceDataMessage message = SignalServiceDataMessage.newBuilder()
                                                               .asGroupMessage(group)
                                                               .withTimestamp(System.currentTimeMillis())
                                                               .build();

    messageSender.sendMessage(new SignalServiceAddress(source),
                              UnidentifiedAccessUtil.getAccessFor(context, Recipient.from(context, Address.fromExternal(context, source), false)),
                              message);
  }

  @Override
  public boolean onShouldRetry(@NonNull Exception e) {
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {

  }

  public static final class Factory implements Job.Factory<RequestGroupInfoJob> {

    @Override
    public @NonNull RequestGroupInfoJob create(@NonNull Parameters parameters, @NonNull Data data) {
      try {
        return new RequestGroupInfoJob(parameters,
                                       data.getString(KEY_SOURCE),
                                       GroupUtil.getDecodedId(data.getString(KEY_GROUP_ID)));
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
  }
}
