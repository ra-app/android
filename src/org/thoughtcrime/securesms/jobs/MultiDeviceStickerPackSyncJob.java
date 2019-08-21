package org.bittube.messenger.jobs;

import androidx.annotation.NonNull;

import org.bittube.messenger.crypto.UnidentifiedAccessUtil;
import org.bittube.messenger.database.DatabaseFactory;
import org.bittube.messenger.database.StickerDatabase.StickerPackRecordReader;
import org.bittube.messenger.database.model.StickerPackRecord;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.Data;
import org.bittube.messenger.jobmanager.Job;
import org.bittube.messenger.jobmanager.impl.NetworkConstraint;
import org.bittube.messenger.logging.Log;
import org.bittube.messenger.util.Hex;
import org.bittube.messenger.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.messages.multidevice.SignalServiceSyncMessage;
import org.whispersystems.signalservice.api.messages.multidevice.StickerPackOperationMessage;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Tells a linked desktop about all installed sticker packs.
 */
public class MultiDeviceStickerPackSyncJob extends BaseJob implements InjectableType {

  private static final String TAG = Log.tag(MultiDeviceStickerPackSyncJob.class);

  public static final String KEY = "MultiDeviceStickerPackSyncJob";

  @Inject SignalServiceMessageSender messageSender;

  public MultiDeviceStickerPackSyncJob() {
    this(new Parameters.Builder()
                           .setQueue("MultiDeviceStickerPackSyncJob")
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .build());
  }

  public MultiDeviceStickerPackSyncJob(@NonNull Parameters parameters) {
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
  protected void onRun() throws Exception {
    if (!TextSecurePreferences.isMultiDevice(context)) {
      Log.i(TAG, "Not multi device, aborting...");
      return;
    }

    List<StickerPackOperationMessage> operations = new LinkedList<>();

    try (StickerPackRecordReader reader = new StickerPackRecordReader(DatabaseFactory.getStickerDatabase(context).getInstalledStickerPacks())) {
      StickerPackRecord pack;
      while ((pack = reader.getNext()) != null) {
        byte[] packIdBytes  = Hex.fromStringCondensed(pack.getPackId());
        byte[] packKeyBytes = Hex.fromStringCondensed(pack.getPackKey());

        operations.add(new StickerPackOperationMessage(packIdBytes, packKeyBytes, StickerPackOperationMessage.Type.INSTALL));
      }
    }

    messageSender.sendMessage(SignalServiceSyncMessage.forStickerPackOperations(operations),
                              UnidentifiedAccessUtil.getAccessForSync(context));
  }

  @Override
  protected boolean onShouldRetry(@NonNull Exception e) {
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to sync sticker pack operation!");
  }

  public static class Factory implements Job.Factory<MultiDeviceStickerPackSyncJob> {

    @Override
    public @NonNull
    MultiDeviceStickerPackSyncJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new MultiDeviceStickerPackSyncJob(parameters);
    }
  }
}
