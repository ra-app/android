package org.raapp.messenger.jobs.requirements;

import android.content.Context;

import org.raapp.messenger.jobmanager.dependencies.ContextDependent;
import org.raapp.messenger.jobmanager.requirements.Requirement;
import org.raapp.messenger.jobmanager.requirements.SimpleRequirement;
import org.raapp.messenger.service.KeyCachingService;

public class MasterSecretRequirement extends SimpleRequirement implements ContextDependent {

  private transient Context context;

  public MasterSecretRequirement(Context context) {
    this.context = context;
  }

  @Override
  public boolean isPresent() {
    return KeyCachingService.getMasterSecret(context) != null;
  }

  @Override
  public void setContext(Context context) {
    this.context = context;
  }
}
