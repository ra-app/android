package org.raapp.messenger.util.concurrent;

import org.raapp.messenger.util.concurrent.ListenableFuture.Listener;

import java.util.concurrent.ExecutionException;

public abstract class AssertedSuccessListener<T> implements Listener<T> {
  @Override
  public void onFailure(ExecutionException e) {
    throw new AssertionError(e);
  }
}
