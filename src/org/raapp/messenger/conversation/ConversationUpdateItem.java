package org.raapp.messenger.conversation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.raapp.messenger.BindableConversationItem;
import org.raapp.messenger.R;
import org.raapp.messenger.VerifyIdentityActivity;
import org.raapp.messenger.crypto.IdentityKeyParcelable;
import org.raapp.messenger.database.IdentityDatabase;
import org.raapp.messenger.database.IdentityDatabase.IdentityRecord;
import org.raapp.messenger.database.model.MessageRecord;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientModifiedListener;
import org.raapp.messenger.util.DateUtils;
import org.raapp.messenger.util.ExpirationUtil;
import org.raapp.messenger.util.GroupUtil;
import org.raapp.messenger.util.IdentityUtil;
import org.raapp.messenger.util.Util;
import org.raapp.messenger.util.concurrent.ListenableFuture;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class ConversationUpdateItem extends LinearLayout
    implements RecipientModifiedListener, BindableConversationItem
{
  private static final String TAG = ConversationUpdateItem.class.getSimpleName();

  private Set<MessageRecord> batchSelected;

  private ImageView     icon;
  private TextView      title;
  private TextView      body;
  private TextView      date;
  private Recipient     sender;
  private MessageRecord messageRecord;
  private Locale        locale;

  public ConversationUpdateItem(Context context) {
    super(context);
  }

  public ConversationUpdateItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onFinishInflate() {
    super.onFinishInflate();

    this.icon  = findViewById(R.id.conversation_update_icon);
    this.title = findViewById(R.id.conversation_update_title);
    this.body  = findViewById(R.id.conversation_update_body);
    this.date  = findViewById(R.id.conversation_update_date);

    this.setOnClickListener(new InternalClickListener(null));
  }

  @Override
  public void bind(@NonNull MessageRecord           messageRecord,
                   @NonNull Optional<MessageRecord> previousMessageRecord,
                   @NonNull Optional<MessageRecord> nextMessageRecord,
                   @NonNull GlideRequests           glideRequests,
                   @NonNull Locale                  locale,
                   @NonNull Set<MessageRecord>      batchSelected,
                   @NonNull Recipient               conversationRecipient,
                   @Nullable String                 searchQuery,
                            boolean                 pulseUpdate)
  {
    this.batchSelected = batchSelected;

    bind(messageRecord, locale);
  }

  @Override
  public void setEventListener(@Nullable EventListener listener) {
    // No events to report yet
  }

  @Override
  public MessageRecord getMessageRecord() {
    return messageRecord;
  }

  private void bind(@NonNull MessageRecord messageRecord, @NonNull Locale locale) {
    this.messageRecord = messageRecord;
    this.sender        = messageRecord.getIndividualRecipient();
    this.locale        = locale;

    this.sender.addListener(this);

    if      (messageRecord.isGroupAction())           setGroupRecord(messageRecord);
    else if (messageRecord.isCallLog())               setCallRecord(messageRecord);
    else if (messageRecord.isJoined())                setJoinedRecord(messageRecord);
    else if (messageRecord.isExpirationTimerUpdate()) setTimerRecord(messageRecord);
    else if (messageRecord.isEndSession())            setEndSessionRecord(messageRecord);
    else if (messageRecord.isIdentityUpdate())        setIdentityRecord(messageRecord);
    else if (messageRecord.isIdentityVerified() ||
             messageRecord.isIdentityDefault())       setIdentityVerifyUpdate(messageRecord);
    else                                              throw new AssertionError("Neither group nor log nor joined.");

    if (batchSelected.contains(messageRecord)) setSelected(true);
    else                                       setSelected(false);
  }

  private void setCallRecord(MessageRecord messageRecord) {
    if      (messageRecord.isIncomingCall()) icon.setImageResource(R.drawable.ic_call_received_grey600_24dp);
    else if (messageRecord.isOutgoingCall()) icon.setImageResource(R.drawable.ic_call_made_grey600_24dp);
    else                                     icon.setImageResource(R.drawable.ic_call_missed_grey600_24dp);

    body.setText(messageRecord.getDisplayBody(getContext()));
    date.setText(DateUtils.getExtendedRelativeTimeSpanString(getContext(), locale, messageRecord.getDateReceived()));

    title.setVisibility(GONE);
    body.setVisibility(VISIBLE);
    date.setVisibility(View.VISIBLE);
  }

  private void setTimerRecord(final MessageRecord messageRecord) {
    if (messageRecord.getExpiresIn() > 0) {
      icon.setImageResource(R.drawable.ic_timer);
      icon.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.MULTIPLY));
    } else {
      icon.setImageResource(R.drawable.ic_timer_disabled);
      icon.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.MULTIPLY));
    }

    title.setText(ExpirationUtil.getExpirationDisplayValue(getContext(), (int)(messageRecord.getExpiresIn() / 1000)));
    body.setText(messageRecord.getDisplayBody(getContext()));

    title.setVisibility(VISIBLE);
    body.setVisibility(VISIBLE);
    date.setVisibility(GONE);
  }

  private void setIdentityRecord(final MessageRecord messageRecord) {
    icon.setImageResource(R.drawable.ic_security_white_24dp);
    icon.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.MULTIPLY));
    body.setText(messageRecord.getDisplayBody(getContext()));

    title.setVisibility(GONE);
    body.setVisibility(VISIBLE);
    date.setVisibility(GONE);
  }

  private void setIdentityVerifyUpdate(final MessageRecord messageRecord) {
    if (messageRecord.isIdentityVerified()) icon.setImageResource(R.drawable.ic_check_white_24dp);
    else                                    icon.setImageResource(R.drawable.ic_info_outline_white_24dp);

    icon.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.MULTIPLY));
    body.setText(messageRecord.getDisplayBody(getContext()));

    title.setVisibility(GONE);
    body.setVisibility(VISIBLE);
    date.setVisibility(GONE);
  }

  private void setGroupRecord(MessageRecord messageRecord) {
    icon.setImageResource(R.drawable.ic_group_grey600_24dp);
    icon.clearColorFilter();

    GroupUtil.getDescription(getContext(), messageRecord.getBody()).addListener(this);
    body.setText(messageRecord.getDisplayBody(getContext()));

    title.setVisibility(GONE);
    body.setVisibility(VISIBLE);
    date.setVisibility(GONE);
  }

  private void setJoinedRecord(MessageRecord messageRecord) {
    icon.setImageResource(R.drawable.ic_favorite_grey600_24dp);
    icon.clearColorFilter();
    body.setText(messageRecord.getDisplayBody(getContext()));

    title.setVisibility(GONE);
    body.setVisibility(VISIBLE);
    date.setVisibility(GONE);
  }

  private void setEndSessionRecord(MessageRecord messageRecord) {
    icon.setImageResource(R.drawable.ic_refresh_white_24dp);
    icon.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#757575"), PorterDuff.Mode.MULTIPLY));
    body.setText(messageRecord.getDisplayBody(getContext()));

    title.setVisibility(GONE);
    body.setVisibility(VISIBLE);
    date.setVisibility(GONE);
  }
  
  @Override
  public void onModified(Recipient recipient) {
    Util.runOnMain(() -> bind(messageRecord, locale));
  }

  @Override
  public void setOnClickListener(View.OnClickListener l) {
    super.setOnClickListener(new InternalClickListener(l));
  }

  @Override
  public void unbind() {
    if (sender != null) {
      sender.removeListener(this);
    }
  }

  private class InternalClickListener implements View.OnClickListener {

    @Nullable private final View.OnClickListener parent;

    InternalClickListener(@Nullable View.OnClickListener parent) {
      this.parent = parent;
    }

    @Override
    public void onClick(View v) {
      if ((!messageRecord.isIdentityUpdate()  &&
           !messageRecord.isIdentityDefault() &&
           !messageRecord.isIdentityVerified()) ||
          !batchSelected.isEmpty())
      {
        if (parent != null) parent.onClick(v);
        return;
      }

      final Recipient sender = ConversationUpdateItem.this.sender;

      IdentityUtil.getRemoteIdentityKey(getContext(), sender).addListener(new ListenableFuture.Listener<Optional<IdentityRecord>>() {
        @Override
        public void onSuccess(Optional<IdentityRecord> result) {
          if (result.isPresent()) {
            Intent intent = new Intent(getContext(), VerifyIdentityActivity.class);
            intent.putExtra(VerifyIdentityActivity.ADDRESS_EXTRA, sender.getAddress());
            intent.putExtra(VerifyIdentityActivity.IDENTITY_EXTRA, new IdentityKeyParcelable(result.get().getIdentityKey()));
            intent.putExtra(VerifyIdentityActivity.VERIFIED_EXTRA, result.get().getVerifiedStatus() == IdentityDatabase.VerifiedStatus.VERIFIED);

            getContext().startActivity(intent);
          }
        }

        @Override
        public void onFailure(ExecutionException e) {
          Log.w(TAG, e);
        }
      });
    }
  }

}
