package org.raapp.messenger.jobs;

import androidx.annotation.NonNull;

import com.annimon.stream.Stream;

import org.raapp.messenger.crypto.UnidentifiedAccessUtil;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.dependencies.InjectableType;
import org.raapp.messenger.jobmanager.Data;
import org.raapp.messenger.jobmanager.Job;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.GroupUtil;
import org.raapp.messenger.util.TextSecurePreferences;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UnidentifiedAccessPair;
import org.whispersystems.signalservice.api.messages.SignalServiceTypingMessage;
import org.whispersystems.signalservice.api.messages.SignalServiceTypingMessage.Action;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class TypingSendJob extends BaseJob implements InjectableType {

  public static final String KEY = "TypingSendJob";

  private static final String TAG = TypingSendJob.class.getSimpleName();

  private static final String KEY_THREAD_ID = "thread_id";
  private static final String KEY_TYPING    = "typing";

  private long    threadId;
  private boolean typing;

  @Inject SignalServiceMessageSender messageSender;

  public TypingSendJob(long threadId, boolean typing) {
    this(new Job.Parameters.Builder()
                           .setQueue("TYPING_" + threadId)
                           .setMaxAttempts(1)
                           .setLifespan(TimeUnit.SECONDS.toMillis(5))
                           .build(),
         threadId,
         typing);
  }

  private TypingSendJob(@NonNull Job.Parameters parameters, long threadId, boolean typing) {
    super(parameters);

    this.threadId = threadId;
    this.typing   = typing;
  }


  @Override
  public @NonNull Data serialize() {
    return new Data.Builder().putLong(KEY_THREAD_ID, threadId)
                             .putBoolean(KEY_TYPING, typing)
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws Exception {
    if (!TextSecurePreferences.isTypingIndicatorsEnabled(context)) {
      return;
    }

    Log.d(TAG, "Sending typing " + (typing ? "started" : "stopped") + " for thread " + threadId);

    Recipient recipient = DatabaseFactory.getThreadDatabase(context).getRecipientForThreadId(threadId);

    if (recipient == null) {
      throw new IllegalStateException("Tried to send a typing indicator to a non-existent thread.");
    }

    List<Recipient>  recipients = Collections.singletonList(recipient);
    Optional<byte[]> groupId    = Optional.absent();

    if (recipient.isGroupRecipient()) {
      recipients = DatabaseFactory.getGroupDatabase(context).getGroupMembers(recipient.getAddress().toGroupString(), false);
      groupId    = Optional.of(GroupUtil.getDecodedId(recipient.getAddress().toGroupString()));
    }

    List<SignalServiceAddress>             addresses          = Stream.of(recipients).map(r -> new SignalServiceAddress(r.getAddress().serialize())).toList();
    List<Optional<UnidentifiedAccessPair>> unidentifiedAccess = Stream.of(recipients).map(r -> UnidentifiedAccessUtil.getAccessFor(context, r)).toList();
    SignalServiceTypingMessage             typingMessage      = new SignalServiceTypingMessage(typing ? Action.STARTED : Action.STOPPED, System.currentTimeMillis(), groupId);

    messageSender.sendTyping(addresses, unidentifiedAccess, typingMessage);
  }

  @Override
  public void onCanceled() {
  }

  @Override
  protected boolean onShouldRetry(@NonNull Exception exception) {
    return false;
  }

  public static final class Factory implements Job.Factory<TypingSendJob> {
    @Override
    public @NonNull TypingSendJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new TypingSendJob(parameters, data.getLong(KEY_THREAD_ID), data.getBoolean(KEY_TYPING));
    }
  }
}
