package org.bittube.messenger.jobs.requirements;

import android.content.Context;

import org.bittube.messenger.jobmanager.dependencies.ContextDependent;
import org.bittube.messenger.jobmanager.requirements.NetworkRequirement;
import org.bittube.messenger.jobmanager.requirements.Requirement;
import org.bittube.messenger.jobmanager.requirements.SimpleRequirement;

public class NetworkOrServiceRequirement extends SimpleRequirement implements ContextDependent {

  private transient Context context;

  public NetworkOrServiceRequirement(Context context) {
    this.context = context;
  }

  @Override
  public void setContext(Context context) {
    this.context = context;
  }

  @Override
  public boolean isPresent() {
    NetworkRequirement networkRequirement = new NetworkRequirement(context);
    ServiceRequirement serviceRequirement = new ServiceRequirement(context);

    return networkRequirement.isPresent() || serviceRequirement.isPresent();
  }
}
