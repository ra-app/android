package org.bittube.messenger.notifications;

import android.content.Context;
import androidx.annotation.NonNull;

import org.bittube.messenger.database.RecipientDatabase;
import org.bittube.messenger.recipients.Recipient;
import org.bittube.messenger.util.TextSecurePreferences;

public enum ReplyMethod {

  GroupMessage,
  SecureMessage,
  UnsecuredSmsMessage;

  public static @NonNull ReplyMethod forRecipient(Context context, Recipient recipient) {
    if (recipient.isGroupRecipient()) {
      return ReplyMethod.GroupMessage;
    } else if (TextSecurePreferences.isPushRegistered(context) && recipient.getRegistered() == RecipientDatabase.RegisteredState.REGISTERED && !recipient.isForceSmsSelection()) {
      return ReplyMethod.SecureMessage;
    } else {
      return ReplyMethod.UnsecuredSmsMessage;
    }
  }
}
