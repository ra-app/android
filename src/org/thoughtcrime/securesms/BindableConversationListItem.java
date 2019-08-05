package org.raapp.messenger;

import android.support.annotation.NonNull;

import org.raapp.messenger.crypto.MasterSecret;
import org.raapp.messenger.database.model.ThreadRecord;
import org.raapp.messenger.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationListItem extends Unbindable {

  public void bind(@NonNull ThreadRecord thread,
                   @NonNull GlideRequests glideRequests, @NonNull Locale locale,
                   @NonNull Set<Long> selectedThreads, boolean batchMode);
}
