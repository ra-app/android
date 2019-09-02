package org.raapp.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import org.raapp.messenger.contactshare.Contact;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.model.MessageRecord;
import org.raapp.messenger.database.model.MmsMessageRecord;
import org.raapp.messenger.linkpreview.LinkPreview;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.stickers.StickerLocator;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface BindableConversationItem extends Unbindable {
  void bind(@NonNull MessageRecord           messageRecord,
            @NonNull Optional<MessageRecord> previousMessageRecord,
            @NonNull Optional<MessageRecord> nextMessageRecord,
            @NonNull GlideRequests           glideRequests,
            @NonNull Locale                  locale,
            @NonNull Set<MessageRecord>      batchSelected,
            @NonNull Recipient               recipients,
            @Nullable String                 searchQuery,
                     boolean                 pulseHighlight);

  MessageRecord getMessageRecord();

  void setEventListener(@Nullable EventListener listener);

  interface EventListener {
    void onQuoteClicked(MmsMessageRecord messageRecord);
    void onLinkPreviewClicked(@NonNull LinkPreview linkPreview);
    void onMoreTextClicked(@NonNull Address conversationAddress, long messageId, boolean isMms);
    void onStickerClicked(@NonNull StickerLocator stickerLocator);
    void onSharedContactDetailsClicked(@NonNull Contact contact, @NonNull View avatarTransitionView);
    void onAddToContactsClicked(@NonNull Contact contact);
    void onMessageSharedContactClicked(@NonNull List<Recipient> choices);
    void onInviteSharedContactClicked(@NonNull List<Recipient> choices);
  }
}
