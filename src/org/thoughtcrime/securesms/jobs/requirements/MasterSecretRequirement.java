package org.bittube.messenger.jobs.requirements;

import android.content.Context;

import org.bittube.messenger.jobmanager.dependencies.ContextDependent;
import org.bittube.messenger.jobmanager.requirements.Requirement;
import org.bittube.messenger.jobmanager.requirements.SimpleRequirement;
import org.bittube.messenger.service.KeyCachingService;

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
