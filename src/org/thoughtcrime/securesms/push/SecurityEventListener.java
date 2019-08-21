package org.bittube.messenger.push;

import android.content.Context;

import org.bittube.messenger.crypto.SecurityEvent;
import org.bittube.messenger.database.Address;
import org.bittube.messenger.database.DatabaseFactory;
import org.bittube.messenger.database.RecipientDatabase;
import org.bittube.messenger.recipients.Recipient;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

public class SecurityEventListener implements SignalServiceMessageSender.EventListener {

  private static final String TAG = SecurityEventListener.class.getSimpleName();

  private final Context context;

  public SecurityEventListener(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void onSecurityEvent(SignalServiceAddress textSecureAddress) {
    SecurityEvent.broadcastSecurityUpdateEvent(context);
  }
}
