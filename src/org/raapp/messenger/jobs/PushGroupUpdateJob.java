package org.raapp.messenger.jobs;


import androidx.annotation.NonNull;

import org.raapp.messenger.crypto.UnidentifiedAccessUtil;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.GroupDatabase;
import org.raapp.messenger.database.GroupDatabase.GroupRecord;
import org.raapp.messenger.dependencies.InjectableType;
import org.raapp.messenger.jobmanager.Data;
import org.raapp.messenger.jobmanager.Job;
import org.raapp.messenger.jobmanager.impl.NetworkConstraint;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.GroupUtil;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachment;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentStream;
import org.whispersystems.signalservice.api.messages.SignalServiceDataMessage;
import org.whispersystems.signalservice.api.messages.SignalServiceGroup;
import org.whispersystems.signalservice.api.messages.SignalServiceGroup.Type;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class PushGroupUpdateJob extends BaseJob implements InjectableType {

  public static final String KEY = "PushGroupUpdateJob";

  private static final String TAG = PushGroupUpdateJob.class.getSimpleName();

  private static final String KEY_SOURCE   = "source";
  private static final String KEY_GROUP_ID = "group_id";

  @Inject SignalServiceMessageSender messageSender;

  private String source;
  private byte[] groupId;

  public PushGroupUpdateJob(String source, byte[] groupId) {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build(),
        source,
        groupId);
  }

  private PushGroupUpdateJob(@NonNull Job.Parameters parameters, String source, byte[] groupId) {
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
    GroupDatabase           groupDatabase = DatabaseFactory.getGroupDatabase(context);
    Optional<GroupRecord>   record        = groupDatabase.getGroup(GroupUtil.getEncodedId(groupId, false));
    SignalServiceAttachment avatar        = null;

    if (record == null) {
      Log.w(TAG, "No information for group record info request: " + new String(groupId));
      return;
    }

    if (record.get().getAvatar() != null) {
      avatar = SignalServiceAttachmentStream.newStreamBuilder()
                                            .withContentType("image/jpeg")
                                            .withStream(new ByteArrayInputStream(record.get().getAvatar()))
                                            .withLength(record.get().getAvatar().length)
                                            .build();
    }

    List<String> members = new LinkedList<>();

    for (Address member : record.get().getMembers()) {
      members.add(member.serialize());
    }

    SignalServiceGroup groupContext = SignalServiceGroup.newBuilder(Type.UPDATE)
                                                        .withAvatar(avatar)
                                                        .withId(groupId)
                                                        .withMembers(members)
                                                        .withName(record.get().getTitle())
                                                        .build();

    Address   groupAddress   = Address.fromSerialized(GroupUtil.getEncodedId(groupId, false));
    Recipient groupRecipient = Recipient.from(context, groupAddress, false);

    SignalServiceDataMessage message = SignalServiceDataMessage.newBuilder()
                                                               .asGroupMessage(groupContext)
                                                               .withTimestamp(System.currentTimeMillis())
                                                               .withExpiration(groupRecipient.getExpireMessages())
                                                               .build();

    messageSender.sendMessage(new SignalServiceAddress(source),
                              UnidentifiedAccessUtil.getAccessFor(context, Recipient.from(context, Address.fromSerialized(source), false)),
                              message);
  }

  @Override
  public boolean onShouldRetry(@NonNull Exception e) {
    Log.w(TAG, e);
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {

  }

  public static final class Factory implements Job.Factory<PushGroupUpdateJob> {
    @Override
    public @NonNull PushGroupUpdateJob create(@NonNull Parameters parameters, @NonNull org.raapp.messenger.jobmanager.Data data) {
      try {
        return new PushGroupUpdateJob(parameters,
                                      data.getString(KEY_SOURCE),
                                      GroupUtil.getDecodedId(data.getString(KEY_GROUP_ID)));
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
  }
}
