package org.raapp.messenger.jobs;

import androidx.annotation.NonNull;

import org.raapp.messenger.ApplicationContext;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.StickerDatabase;
import org.raapp.messenger.database.model.IncomingSticker;
import org.raapp.messenger.dependencies.InjectableType;
import org.raapp.messenger.jobmanager.Data;
import org.raapp.messenger.jobmanager.Job;
import org.raapp.messenger.jobmanager.JobManager;
import org.raapp.messenger.jobmanager.impl.NetworkConstraint;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.util.Hex;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.signalservice.api.SignalServiceMessageReceiver;
import org.whispersystems.signalservice.api.messages.SignalServiceStickerManifest;
import org.whispersystems.signalservice.api.messages.SignalServiceStickerManifest.StickerInfo;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class StickerPackDownloadJob extends BaseJob implements InjectableType {

  public static final String KEY = "StickerPackDownloadJob";

  private static final String TAG = Log.tag(StickerPackDownloadJob.class);

  private static final String KEY_PACK_ID        = "pack_key";
  private static final String KEY_PACK_KEY       = "pack_id";
  private static final String KEY_REFERENCE_PACK = "reference_pack";

  private final String  packId;
  private final String  packKey;
  private final boolean isReferencePack;

  @Inject SignalServiceMessageReceiver receiver;

  public StickerPackDownloadJob(@NonNull String packId, @NonNull String packKey, boolean isReferencePack)
  {
    this(new Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setQueue("StickerPackDownloadJob_" + packKey)
                           .build(),
        packId,
        packKey,
        isReferencePack);
  }

  private StickerPackDownloadJob(@NonNull Parameters parameters,
                                 @NonNull String packId,
                                 @NonNull String packKey,
                                 boolean isReferencePack)
  {
    super(parameters);
    this.packId          = packId;
    this.packKey         = packKey;
    this.isReferencePack = isReferencePack;
  }

  @Override
  protected void onRun() throws IOException, InvalidMessageException {
    if (isReferencePack && !DatabaseFactory.getAttachmentDatabase(context).containsStickerPackId(packId)) {
      Log.w(TAG, "There are no attachments with the requested packId present for this reference pack. Skipping.");
      return;
    }

    if (isReferencePack && DatabaseFactory.getStickerDatabase(context).isPackAvailableAsReference(packId)) {
      Log.i(TAG, "Sticker pack already available for reference. Skipping.");
      return;
    }

    JobManager                   jobManager      = ApplicationContext.getInstance(context).getJobManager();
    StickerDatabase              stickerDatabase = DatabaseFactory.getStickerDatabase(context);
    byte[]                       packIdBytes     = Hex.fromStringCondensed(packId);
    byte[]                       packKeyBytes    = Hex.fromStringCondensed(packKey);
    SignalServiceStickerManifest manifest        = receiver.retrieveStickerManifest(packIdBytes, packKeyBytes);

    if (manifest.getStickers().isEmpty()) {
      Log.w(TAG, "No stickers in  pack!");
      return;
    }

    if (!isReferencePack && stickerDatabase.isPackAvailableAsReference(packId)) {
      stickerDatabase.markPackAsInstalled(packId);
    }

    StickerInfo      cover = manifest.getCover().or(manifest.getStickers().get(0));
    JobManager.Chain chain = jobManager.startChain(new StickerDownloadJob(new IncomingSticker(packId,
                                                                                              packKey,
                                                                                              manifest.getTitle().or(""),
                                                                                              manifest.getAuthor().or(""),
                                                                                              cover.getId(),
                                                                                              "",
                                                                                              true,
                                                                                              !isReferencePack)));



    if (!isReferencePack) {
      List<Job> jobs = new ArrayList<>(manifest.getStickers().size());

      for (StickerInfo stickerInfo : manifest.getStickers()) {
        jobs.add(new StickerDownloadJob(new IncomingSticker(packId,
                                                            packKey,
                                                            manifest.getTitle().or(""),
                                                            manifest.getAuthor().or(""),
                                                            stickerInfo.getId(),
                                                            stickerInfo.getEmoji(),
                                                            false,
                                                            true)));
      }

      chain.then(jobs);
    }

    chain.enqueue();
  }

  @Override
  protected boolean onShouldRetry(@NonNull Exception e) {
    return e instanceof PushNetworkException;
  }

  @Override
  public @NonNull Data serialize() {
    return new Data.Builder().putString(KEY_PACK_ID, packId)
                             .putString(KEY_PACK_KEY, packKey)
                             .putBoolean(KEY_REFERENCE_PACK, isReferencePack)
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to download manifest with pack_id: " + packId);
  }

  public static final class Factory implements Job.Factory<StickerPackDownloadJob> {
    @Override
    public @NonNull
    StickerPackDownloadJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new StickerPackDownloadJob(parameters,
                                            data.getString(KEY_PACK_ID),
                                            data.getString(KEY_PACK_KEY),
                                            data.getBoolean(KEY_REFERENCE_PACK));
    }
  }
}
