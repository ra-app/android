package org.raapp.messenger.jobs;

import android.content.Context;

import org.raapp.messenger.jobmanager.Job;
import org.raapp.messenger.jobmanager.JobParameters;
import org.raapp.messenger.jobmanager.dependencies.ContextDependent;

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
