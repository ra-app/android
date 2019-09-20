package org.raapp.messenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.raapp.messenger.ApplicationContext;
import org.raapp.messenger.jobs.PushNotificationReceiveJob;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    ApplicationContext.getInstance(context).getJobManager().add(new PushNotificationReceiveJob(context));
  }
}
