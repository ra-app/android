package org.raapp.messenger.jobmanager;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

public interface ExecutorFactory {
  @NonNull ExecutorService newSingleThreadExecutor(@NonNull String name);
}
