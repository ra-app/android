package org.bittube.messenger.jobs;

import android.content.Context;
import org.bittube.messenger.logging.Log;

import org.bittube.messenger.crypto.MasterSecret;
import org.bittube.messenger.crypto.PreKeyUtil;
import org.bittube.messenger.dependencies.InjectableType;
import org.bittube.messenger.jobmanager.JobParameters;
import org.bittube.messenger.jobs.requirements.MasterSecretRequirement;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.push.exceptions.NonSuccessfulResponseCodeException;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static org.bittube.messenger.dependencies.AxolotlStorageModule.SignedPreKeyStoreFactory;

public class CleanPreKeysJob extends MasterSecretJob implements InjectableType {

  private static final String TAG = CleanPreKeysJob.class.getSimpleName();

  private static final long ARCHIVE_AGE = TimeUnit.DAYS.toMillis(7);

  @Inject transient SignalServiceAccountManager accountManager;
  @Inject transient SignedPreKeyStoreFactory signedPreKeyStoreFactory;

  public CleanPreKeysJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withGroupId(CleanPreKeysJob.class.getSimpleName())
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRetryCount(5)
                                .create());
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun(MasterSecret masterSecret) throws IOException {
    try {
      Log.i(TAG, "Cleaning prekeys...");

      int                activeSignedPreKeyId = PreKeyUtil.getActiveSignedPreKeyId(context);
      SignedPreKeyStore  signedPreKeyStore    = signedPreKeyStoreFactory.create();

      if (activeSignedPreKeyId < 0) return;

      SignedPreKeyRecord             currentRecord = signedPreKeyStore.loadSignedPreKey(activeSignedPreKeyId);
      List<SignedPreKeyRecord>       allRecords    = signedPreKeyStore.loadSignedPreKeys();
      LinkedList<SignedPreKeyRecord> oldRecords    = removeRecordFrom(currentRecord, allRecords);

      Collections.sort(oldRecords, new SignedPreKeySorter());

      Log.i(TAG, "Active signed prekey: " + activeSignedPreKeyId);
      Log.i(TAG, "Old signed prekey record count: " + oldRecords.size());

      boolean foundAgedRecord = false;

      for (SignedPreKeyRecord oldRecord : oldRecords) {
        long archiveDuration = System.currentTimeMillis() - oldRecord.getTimestamp();

        if (archiveDuration >= ARCHIVE_AGE) {
          if (!foundAgedRecord) {
            foundAgedRecord = true;
          } else {
            Log.i(TAG, "Removing signed prekey record: " + oldRecord.getId() + " with timestamp: " + oldRecord.getTimestamp());
            signedPreKeyStore.removeSignedPreKey(oldRecord.getId());
          }
        }
      }
    } catch (InvalidKeyIdException e) {
      Log.w(TAG, e);
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception throwable) {
    if (throwable instanceof NonSuccessfulResponseCodeException) return false;
    if (throwable instanceof PushNetworkException)               return true;
    return false;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to execute clean signed prekeys task.");
  }

  private LinkedList<SignedPreKeyRecord> removeRecordFrom(SignedPreKeyRecord currentRecord,
                                                          List<SignedPreKeyRecord> records)

  {
    LinkedList<SignedPreKeyRecord> others = new LinkedList<>();

    for (SignedPreKeyRecord record : records) {
      if (record.getId() != currentRecord.getId()) {
        others.add(record);
      }
    }

    return others;
  }

  private static class SignedPreKeySorter implements Comparator<SignedPreKeyRecord> {
    @Override
    public int compare(SignedPreKeyRecord lhs, SignedPreKeyRecord rhs) {
      if      (lhs.getTimestamp() > rhs.getTimestamp()) return -1;
      else if (lhs.getTimestamp() < rhs.getTimestamp()) return 1;
      else                                              return 0;
    }
  }

}
