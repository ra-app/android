/*
 * Copyright (C) 2012 Moxie Marlinspike
 * Copyright (C) 2013-2017 Open Whisper Systems
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
package org.raapp.messenger.database.model;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import org.raapp.messenger.R;
import org.raapp.messenger.database.MmsSmsColumns;
import org.raapp.messenger.database.SmsDatabase;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.util.ExpirationUtil;

/**
 * The message record model which represents thread heading messages.
 *
 * @author Moxie Marlinspike
 *
 */
public class ThreadRecord extends DisplayRecord {

  private @Nullable final Uri     snippetUri;
  private           final long    count;
  private           final int     unreadCount;
  private           final int     distributionType;
  private           final boolean archived;
  private           final long    expiresIn;
  private           final long    lastSeen;

  public ThreadRecord(@NonNull String body, @Nullable Uri snippetUri,
                      @NonNull Recipient recipient, long date, long count, int unreadCount,
                      long threadId, int deliveryReceiptCount, int status, long snippetType,
                      int distributionType, boolean archived, long expiresIn, long lastSeen,
                      int readReceiptCount)
  {
    super(body, recipient, date, date, threadId, status, deliveryReceiptCount, snippetType, readReceiptCount);
    this.snippetUri       = snippetUri;
    this.count            = count;
    this.unreadCount      = unreadCount;
    this.distributionType = distributionType;
    this.archived         = archived;
    this.expiresIn        = expiresIn;
    this.lastSeen         = lastSeen;
  }

  public @Nullable Uri getSnippetUri() {
    return snippetUri;
  }

  @Override
  public SpannableString getDisplayBody(@NonNull Context context) {
    return getDisplayBodyExtended(context).spannableString;
  }

  public DisplayBodyExtended getDisplayBodyExtended(@NonNull Context context){

    DisplayBodyExtended result = new DisplayBodyExtended();
    result.isRegularMessage = false;

    if (isGroupUpdate()) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_group_updated));
    } else if (isGroupQuit()) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_left_the_group));
    } else if (isKeyExchange()) {
      result.spannableString = emphasisAdded(context.getString(R.string.ConversationListItem_key_exchange_message));
    } else if (SmsDatabase.Types.isFailedDecryptType(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.MessageDisplayHelper_bad_encrypted_message));
    } else if (SmsDatabase.Types.isNoRemoteSessionType(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.MessageDisplayHelper_message_encrypted_for_non_existing_session));
    } else if (SmsDatabase.Types.isEndSessionType(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_secure_session_reset));
    } else if (MmsSmsColumns.Types.isLegacyType(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.MessageRecord_message_encrypted_with_a_legacy_protocol_version_that_is_no_longer_supported));
    } else if (MmsSmsColumns.Types.isDraftMessageType(type)) {
      String draftText = context.getString(R.string.ThreadRecord_draft);
      result.spannableString = emphasisAdded(draftText + " " + getBody(), 0, draftText.length());
    } else if (SmsDatabase.Types.isOutgoingCall(type)) {
      result.spannableString = emphasisAdded(context.getString(org.raapp.messenger.R.string.ThreadRecord_called));
    } else if (SmsDatabase.Types.isIncomingCall(type)) {
      result.spannableString = emphasisAdded(context.getString(org.raapp.messenger.R.string.ThreadRecord_called_you));
    } else if (SmsDatabase.Types.isMissedCall(type)) {
      result.spannableString = emphasisAdded(context.getString(org.raapp.messenger.R.string.ThreadRecord_missed_call));
    } else if (SmsDatabase.Types.isJoinedType(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_s_is_on_signal, getRecipient().toShortString()));
    } else if (SmsDatabase.Types.isExpirationTimerUpdate(type)) {
      int seconds = (int)(getExpiresIn() / 1000);
      if (seconds <= 0) {
        result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_disappearing_messages_disabled));
      }
      String time = ExpirationUtil.getExpirationDisplayValue(context, seconds);
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_disappearing_message_time_updated_to_s, time));
    } else if (SmsDatabase.Types.isIdentityUpdate(type)) {
      if (getRecipient().isGroupRecipient()) result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_safety_number_changed));
      else                                   result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_your_safety_number_with_s_has_changed, getRecipient().toShortString()));
    } else if (SmsDatabase.Types.isIdentityVerified(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_you_marked_verified));
    } else if (SmsDatabase.Types.isIdentityDefault(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_you_marked_unverified));
    } else if (SmsDatabase.Types.isUnsupportedMessageType(type)) {
      result.spannableString = emphasisAdded(context.getString(R.string.ThreadRecord_message_could_not_be_processed));
    } else {
      if (TextUtils.isEmpty(getBody())) {
        result.spannableString = new SpannableString(emphasisAdded(context.getString(R.string.ThreadRecord_media_message)));
      } else {
        result.spannableString = new SpannableString(getBody());
      }
      result.isRegularMessage = true;
    }

    return result;
  }



  private SpannableString emphasisAdded(String sequence) {
    return emphasisAdded(sequence, 0, sequence.length());
  }

  private SpannableString emphasisAdded(String sequence, int start, int end) {
    SpannableString spannable = new SpannableString(sequence);
    spannable.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC),
                      start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  public long getCount() {
    return count;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public long getDate() {
    return getDateReceived();
  }

  public boolean isArchived() {
    return archived;
  }

  public int getDistributionType() {
    return distributionType;
  }

  public long getExpiresIn() {
    return expiresIn;
  }

  public long getLastSeen() {
    return lastSeen;
  }


  public static class DisplayBodyExtended{
    SpannableString spannableString;
    boolean isRegularMessage;

    public SpannableString getSpannableString() {
      return spannableString;
    }

    public void setSpannableString(SpannableString spannableString) {
      this.spannableString = spannableString;
    }

    public boolean isRegularMessage() {
      return isRegularMessage;
    }

    public void setRegularMessage(boolean regularMessage) {
      isRegularMessage = regularMessage;
    }
  }

}


