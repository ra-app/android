/*
 * Copyright (C) 2014-2017 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.raapp.messenger;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.raapp.messenger.components.AlertView;
import org.raapp.messenger.components.AvatarImageView;
import org.raapp.messenger.components.DeliveryStatusView;
import org.raapp.messenger.components.FromTextView;
import org.raapp.messenger.components.ThumbnailView;
import org.raapp.messenger.components.TypingIndicatorView;
import org.raapp.messenger.conversation.ConversationItem;
import org.raapp.messenger.database.model.ThreadRecord;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientModifiedListener;
import org.raapp.messenger.search.model.MessageResult;
import org.raapp.messenger.util.DateUtils;
import org.raapp.messenger.util.RoleUtil;
import org.raapp.messenger.util.SearchUtil;
import org.raapp.messenger.util.ThemeUtil;
import org.raapp.messenger.util.Util;
import org.raapp.messenger.util.ViewUtil;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public class ConversationListItem extends RelativeLayout
                                  implements RecipientModifiedListener,
                                             BindableConversationListItem, Unbindable
{
  @SuppressWarnings("unused")
  private final static String TAG = ConversationListItem.class.getSimpleName();

  private final static Typeface  BOLD_TYPEFACE  = Typeface.create("sans-serif-medium", Typeface.NORMAL);
  private final static Typeface  LIGHT_TYPEFACE = Typeface.create("sans-serif", Typeface.NORMAL);

  private static final int MAX_SNIPPET_LENGTH = 500;

  private Set<Long>           selectedThreads;
  private Recipient           recipient;
  private long                threadId;
  private GlideRequests       glideRequests;
  private View                subjectContainer;
  private TextView            subjectView;
  private TypingIndicatorView typingView;
  private FromTextView        fromView;
  private TextView            dateView;
  private TextView            archivedView;
  private DeliveryStatusView  deliveryStatusIndicator;
  private AlertView           alertView;
  private TextView            unreadIndicator;
  private ImageView           blacBoardIcon;
  private long                lastSeen;

  private int             unreadCount;
  private AvatarImageView contactPhotoImage;
  private ThumbnailView   thumbnailView;

  private int distributionType;

  public ConversationListItem(Context context) {
    this(context, null);
  }

  public ConversationListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    this.subjectContainer        = findViewById(R.id.subject_container);
    this.subjectView             = findViewById(R.id.subject);
    this.typingView              = findViewById(R.id.typing_indicator);
    this.fromView                = findViewById(R.id.from);
    this.dateView                = findViewById(R.id.date);
    this.deliveryStatusIndicator = findViewById(R.id.delivery_status);
    this.alertView               = findViewById(R.id.indicators_parent);
    this.contactPhotoImage       = findViewById(R.id.contact_photo_image);
    this.thumbnailView           = findViewById(R.id.thumbnail);
    this.archivedView            = findViewById(R.id.archived);
    this.unreadIndicator         = findViewById(R.id.unread_indicator);
    this.blacBoardIcon           = findViewById(R.id.iv_blackboard);
    thumbnailView.setClickable(false);

    ViewUtil.setTextViewGravityStart(this.fromView, getContext());
    ViewUtil.setTextViewGravityStart(this.subjectView, getContext());
  }

  @Override
  public void bind(@NonNull ThreadRecord thread,
                   @NonNull GlideRequests glideRequests,
                   @NonNull Locale locale,
                   @NonNull Set<Long> typingThreads,
                   @NonNull Set<Long> selectedThreads,
                   boolean batchMode)
  {
    bind(thread, glideRequests, locale, typingThreads, selectedThreads, batchMode, null);
  }

  public void bind(@NonNull ThreadRecord thread,
                   @NonNull GlideRequests glideRequests,
                   @NonNull Locale locale,
                   @NonNull Set<Long> typingThreads,
                   @NonNull Set<Long> selectedThreads,
                   boolean batchMode,
                   @Nullable String highlightSubstring)
  {
    this.selectedThreads  = selectedThreads;
    this.recipient        = thread.getRecipient();
    this.threadId         = thread.getThreadId();
    this.glideRequests    = glideRequests;
    this.unreadCount      = thread.getUnreadCount();
    this.distributionType = thread.getDistributionType();
    this.lastSeen         = thread.getLastSeen();

    this.recipient.addListener(this);
    if (highlightSubstring != null) {
      String name = recipient.isLocalNumber() ? getContext().getString(R.string.note_to_self) : recipient.getName();

      this.fromView.setText(SearchUtil.getHighlightedSpan(locale, () -> new StyleSpan(Typeface.BOLD), name, highlightSubstring));
    } else {
      this.fromView.setText(recipient, unreadCount == 0);
    }



    if (typingThreads.contains(threadId)) {
      this.subjectView.setVisibility(INVISIBLE);

      this.typingView.setVisibility(VISIBLE);
      this.typingView.startAnimation();
    } else {
      this.typingView.setVisibility(GONE);
      this.typingView.stopAnimation();

      this.subjectView.setVisibility(VISIBLE);
      ThreadRecord.DisplayBodyExtended displayBodyExtended = thread.getDisplayBodyExtended(getContext());
      String message = displayBodyExtended.getSpannableString().toString();

      if (message.contains(ConversationItem.MAGIC_MSG)) {
        message = message.replaceAll(ConversationItem.MAGIC_MSG_REGEX, "");
      }

      this.subjectView.setText(getTrimmedSnippet(new SpannableString(message)));
      this.subjectView.setTypeface(unreadCount == 0 ? LIGHT_TYPEFACE : BOLD_TYPEFACE);
      this.subjectView.setTextColor(unreadCount == 0 ? ThemeUtil.getThemedColor(getContext(), R.attr.conversation_list_item_subject_color)
                                                     : ThemeUtil.getThemedColor(getContext(), R.attr.conversation_list_item_unread_color));

      // Hide updated messages in broadcast for regular user; Check thread.getDisplayBody alternative to get message type. Only show regular messages
     if(recipient.isGroupRecipient() && !RoleUtil.isAdminInCompany(getContext(), recipient.getAddress().toString()) && !displayBodyExtended.isRegularMessage()){
        subjectView.setVisibility(GONE);
      }else {
        subjectView.setVisibility(VISIBLE);
      }


    }

    if (thread.getDate() > 0) {
      CharSequence date = DateUtils.getBriefRelativeTimeSpanString(getContext(), locale, thread.getDate());
      dateView.setText(date);
      dateView.setTypeface(unreadCount == 0 ? LIGHT_TYPEFACE : BOLD_TYPEFACE);
      //dateView.setTextColor(unreadCount == 0 ? ThemeUtil.getThemedColor(getContext(), R.attr.conversation_list_item_date_color) : ThemeUtil.getThemedColor(getContext(), R.attr.conversation_list_item_unread_color));
    }

    if (thread.isArchived()) {
      this.archivedView.setVisibility(View.VISIBLE);
    } else {
      this.archivedView.setVisibility(View.GONE);
    }

    setStatusIcons(thread);
    setThumbnailSnippet(thread);
    setBatchState(batchMode);
    setRippleColor(recipient);
    setUnreadIndicator(thread);
    this.contactPhotoImage.setAvatar(glideRequests, recipient, true);

    if(recipient.isOfficeApp()){
      blacBoardIcon.setVisibility(VISIBLE);
    }
  }

  public void bind(@NonNull  Recipient     contact,
                   @NonNull  GlideRequests glideRequests,
                   @NonNull  Locale        locale,
                   @Nullable String        highlightSubstring)
  {
    this.selectedThreads = Collections.emptySet();
    this.recipient       = contact;
    this.glideRequests   = glideRequests;

    this.recipient.addListener(this);

    String name = recipient.isLocalNumber() ? getContext().getString(R.string.note_to_self) : recipient.getName();

    fromView.setText(SearchUtil.getHighlightedSpan(locale, () -> new StyleSpan(Typeface.BOLD), name, highlightSubstring));
    subjectView.setText(SearchUtil.getHighlightedSpan(locale, () -> new StyleSpan(Typeface.BOLD), contact.getAddress().toString(), highlightSubstring));
    dateView.setText("");
    archivedView.setVisibility(GONE);
    unreadIndicator.setVisibility(GONE);
    deliveryStatusIndicator.setNone();
    alertView.setNone();
    thumbnailView.setVisibility(GONE);

    setBatchState(false);
    setRippleColor(contact);
    contactPhotoImage.setAvatar(glideRequests, recipient, true);
  }

  public void bind(@NonNull  MessageResult messageResult,
                   @NonNull  GlideRequests glideRequests,
                   @NonNull  Locale        locale,
                   @Nullable String        highlightSubstring)
  {
    this.selectedThreads = Collections.emptySet();
    this.recipient       = messageResult.conversationRecipient;
    this.glideRequests   = glideRequests;

    this.recipient.addListener(this);

    fromView.setText(recipient, true);
    subjectView.setText(SearchUtil.getHighlightedSpan(locale, () -> new StyleSpan(Typeface.BOLD), messageResult.bodySnippet, highlightSubstring));
    dateView.setText(DateUtils.getBriefRelativeTimeSpanString(getContext(), locale, messageResult.receivedTimestampMs));
    archivedView.setVisibility(GONE);
    unreadIndicator.setVisibility(GONE);
    deliveryStatusIndicator.setNone();
    alertView.setNone();
    thumbnailView.setVisibility(GONE);

    setBatchState(false);
    setRippleColor(recipient);
    contactPhotoImage.setAvatar(glideRequests, recipient, true);
  }

  @Override
  public void unbind() {
    if (this.recipient != null) {
      this.recipient.removeListener(this);
      this.recipient = null;
      contactPhotoImage.setAvatar(glideRequests, null, true);
    }
  }

  private void setBatchState(boolean batch) {
    setSelected(batch && selectedThreads.contains(threadId));
  }

  public Recipient getRecipient() {
    return recipient;
  }

  public long getThreadId() {
    return threadId;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public int getDistributionType() {
    return distributionType;
  }

  public long getLastSeen() {
    return lastSeen;
  }

  private @NonNull CharSequence getTrimmedSnippet(@NonNull CharSequence snippet) {
    return snippet.length() <= MAX_SNIPPET_LENGTH ? snippet
                                                  : snippet.subSequence(0, MAX_SNIPPET_LENGTH);
  }

  private void setThumbnailSnippet(ThreadRecord thread) {
    if (thread.getSnippetUri() != null) {
      this.thumbnailView.setVisibility(View.VISIBLE);
      this.thumbnailView.setImageResource(glideRequests, thread.getSnippetUri());

      LayoutParams subjectParams = (RelativeLayout.LayoutParams)this.subjectContainer .getLayoutParams();
      subjectParams.addRule(RelativeLayout.LEFT_OF, R.id.thumbnail);
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        subjectParams.addRule(RelativeLayout.START_OF, R.id.thumbnail);
      }
      this.subjectContainer.setLayoutParams(subjectParams);
      this.post(new ThumbnailPositioner(thumbnailView, archivedView, deliveryStatusIndicator, dateView));
    } else {
      this.thumbnailView.setVisibility(View.GONE);

      LayoutParams subjectParams = (RelativeLayout.LayoutParams)this.subjectContainer.getLayoutParams();
      subjectParams.addRule(RelativeLayout.LEFT_OF, R.id.status);
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
        subjectParams.addRule(RelativeLayout.START_OF, R.id.status);
      }
      this.subjectContainer.setLayoutParams(subjectParams);
    }
  }

  private void setStatusIcons(ThreadRecord thread) {
    if (!thread.isOutgoing() || thread.isOutgoingCall() || thread.isVerificationStatusChange()) {
      deliveryStatusIndicator.setNone();
      alertView.setNone();
    } else if (thread.isFailed()) {
      deliveryStatusIndicator.setNone();
      alertView.setFailed();
    } else if (thread.isPendingInsecureSmsFallback()) {
      deliveryStatusIndicator.setNone();
      alertView.setPendingApproval();
    } else {
      alertView.setNone();

      if      (thread.isPending())    deliveryStatusIndicator.setPending();
      else if (thread.isRemoteRead()) deliveryStatusIndicator.setRead();
      else if (thread.isDelivered())  deliveryStatusIndicator.setDelivered();
      else                            deliveryStatusIndicator.setSent();
    }
  }

  private void setRippleColor(Recipient recipient) {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      try{
        ((RippleDrawable)(getBackground()).mutate()).setColor(ColorStateList.valueOf(recipient.getColor().toConversationColor(getContext())));
      }catch (Exception e){
        // Invalid cast
      }
    }
  }

  private void setUnreadIndicator(ThreadRecord thread) {
    if (thread.isOutgoing() || thread.getUnreadCount() == 0) {
      unreadIndicator.setVisibility(View.GONE);
      return;
    }

    unreadIndicator.setText(String.valueOf(unreadCount));
    unreadIndicator.setVisibility(View.VISIBLE);
  }

  @Override
  public void onModified(final Recipient recipient) {
    Util.runOnMain(() -> {
      if (this.recipient == recipient) {
        fromView.setText(recipient, unreadCount == 0);
        contactPhotoImage.setAvatar(glideRequests, recipient, true);
        setRippleColor(recipient);
      }
    });
  }

  private static class ThumbnailPositioner implements Runnable {

    private final View thumbnailView;
    private final View archivedView;
    private final View deliveryStatusView;
    private final View dateView;

    ThumbnailPositioner(View thumbnailView, View archivedView, View deliveryStatusView, View dateView) {
      this.thumbnailView      = thumbnailView;
      this.archivedView       = archivedView;
      this.deliveryStatusView = deliveryStatusView;
      this.dateView           = dateView;
    }

    @Override
    public void run() {
      LayoutParams thumbnailParams = (RelativeLayout.LayoutParams)thumbnailView.getLayoutParams();

      if (archivedView.getVisibility() == View.VISIBLE &&
          (archivedView.getWidth() + deliveryStatusView.getWidth()) > dateView.getWidth())
      {
        thumbnailParams.addRule(RelativeLayout.LEFT_OF, R.id.status);
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
          thumbnailParams.addRule(RelativeLayout.START_OF, R.id.status);
        }
      } else {
        thumbnailParams.addRule(RelativeLayout.LEFT_OF, R.id.date);
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
          thumbnailParams.addRule(RelativeLayout.START_OF, R.id.date);
        }
      }

      thumbnailView.setLayoutParams(thumbnailParams);
    }
  }

}
