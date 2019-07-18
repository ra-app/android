package org.bittube.messenger;

import android.support.annotation.NonNull;

import org.bittube.messenger.crypto.MasterSecret;
import org.bittube.messenger.database.model.ThreadRecord;
import org.bittube.messenger.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationListItem extends Unbindable {

  public void bind(@NonNull ThreadRecord thread,
                   @NonNull GlideRequests glideRequests, @NonNull Locale locale,
                   @NonNull Set<Long> selectedThreads, boolean batchMode);
}
