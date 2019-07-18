package org.bittube.messenger.jobs.requirements;

import android.content.Context;

import org.bittube.messenger.jobmanager.dependencies.ContextDependent;
import org.bittube.messenger.jobmanager.requirements.Requirement;
import org.bittube.messenger.jobmanager.requirements.SimpleRequirement;
import org.bittube.messenger.sms.TelephonyServiceState;

public class ServiceRequirement extends SimpleRequirement implements ContextDependent {

  private static final String TAG = ServiceRequirement.class.getSimpleName();

  private transient Context context;

  public ServiceRequirement(Context context) {
    this.context  = context;
  }

  @Override
  public void setContext(Context context) {
    this.context = context;
  }

  @Override
  public boolean isPresent() {
    TelephonyServiceState telephonyServiceState = new TelephonyServiceState();
    return telephonyServiceState.isConnected(context);
  }
}
