package org.bittube.messenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.bittube.messenger.ApplicationContext;
import org.bittube.messenger.jobs.PushNotificationReceiveJob;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    ApplicationContext.getInstance(context).getJobManager().add(new PushNotificationReceiveJob(context));
  }
}
