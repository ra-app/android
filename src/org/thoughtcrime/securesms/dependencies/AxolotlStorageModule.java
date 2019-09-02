package org.raapp.messenger.dependencies;

import android.content.Context;

import org.raapp.messenger.crypto.storage.SignalProtocolStoreImpl;
import org.raapp.messenger.jobs.CleanPreKeysJob;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import dagger.Module;
import dagger.Provides;

@Module (complete = false, injects = {CleanPreKeysJob.class})
public class AxolotlStorageModule {

  private final Context context;

  public AxolotlStorageModule(Context context) {
    this.context = context;
  }

  @Provides SignedPreKeyStoreFactory provideSignedPreKeyStoreFactory() {
    return new SignedPreKeyStoreFactory() {
      @Override
      public SignedPreKeyStore create() {
        return new SignalProtocolStoreImpl(context);
      }
    };
  }

  public static interface SignedPreKeyStoreFactory {
    public SignedPreKeyStore create();
  }
}
