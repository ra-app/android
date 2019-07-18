package org.bittube.messenger.jobs;

import android.content.Context;

import org.bittube.messenger.jobmanager.Job;
import org.bittube.messenger.jobmanager.JobParameters;
import org.bittube.messenger.jobmanager.dependencies.ContextDependent;

public abstract class ContextJob extends Job implements ContextDependent {

  protected transient Context context;

  protected ContextJob(Context context, JobParameters parameters) {
    super(parameters);
    this.context = context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  protected Context getContext() {
    return context;
  }
}
