package org.raapp.messenger.groups;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.protobuf.ByteString;

import org.raapp.messenger.attachments.Attachment;
import org.raapp.messenger.attachments.UriAttachment;
import org.raapp.messenger.database.Address;
import org.raapp.messenger.database.AttachmentDatabase;
import org.raapp.messenger.database.DatabaseFactory;
import org.raapp.messenger.database.GroupDatabase;
import org.raapp.messenger.database.ThreadDatabase;
import org.raapp.messenger.mms.OutgoingGroupMediaMessage;
import org.raapp.messenger.providers.BlobProvider;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.sms.MessageSender;
import org.raapp.messenger.util.BitmapUtil;
import org.raapp.messenger.util.GroupUtil;
import org.raapp.messenger.util.MediaUtil;
import org.raapp.messenger.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.util.InvalidNumberException;
import org.whispersystems.signalservice.internal.push.SignalServiceProtos.GroupContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GroupManager {

  public static @NonNull GroupActionResult createGroup(@NonNull  Context        context,
                                                       @NonNull  Set<Recipient> members,
                                                       @Nullable Bitmap         avatar,
                                                       @Nullable String         name,
                                                                 boolean        mms)
  {
    final byte[]        avatarBytes     = BitmapUtil.toByteArray(avatar);
    final GroupDatabase groupDatabase   = DatabaseFactory.getGroupDatabase(context);
    final String        groupId         = GroupUtil.getEncodedId(groupDatabase.allocateGroupId(), mms);
    final Recipient     groupRecipient  = Recipient.from(context, Address.fromSerialized(groupId), false);
    final Set<Address>  memberAddresses = getMemberAddresses(members);

    memberAddresses.add(Address.fromSerialized(TextSecurePreferences.getLocalNumber(context)));
    groupDatabase.create(groupId, name, new LinkedList<>(memberAddresses), null, null);

    if (!mms) {
      groupDatabase.updateAvatar(groupId, avatarBytes);
      DatabaseFactory.getRecipientDatabase(context).setProfileSharing(groupRecipient, true);
      return sendGroupUpdate(context, groupId, memberAddresses, name, avatarBytes);
    } else {
      long threadId = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(groupRecipient, ThreadDatabase.DistributionTypes.CONVERSATION);
      return new GroupActionResult(groupRecipient, threadId);
    }
  }

  public static GroupActionResult updateGroup(@NonNull  Context        context,
                                              @NonNull  String         groupId,
                                              @NonNull  Set<Recipient> members,
                                              @Nullable Bitmap         avatar,
                                              @Nullable String         name)
      throws InvalidNumberException
  {
    final GroupDatabase groupDatabase   = DatabaseFactory.getGroupDatabase(context);
    final Set<Address>  memberAddresses = getMemberAddresses(members);
    final byte[]        avatarBytes     = BitmapUtil.toByteArray(avatar);

    memberAddresses.add(Address.fromSerialized(TextSecurePreferences.getLocalNumber(context)));
    groupDatabase.updateMembers(groupId, new LinkedList<>(memberAddresses));
    groupDatabase.updateTitle(groupId, name);
    groupDatabase.updateAvatar(groupId, avatarBytes);

    if (!GroupUtil.isMmsGroup(groupId)) {
      return sendGroupUpdate(context, groupId, memberAddresses, name, avatarBytes);
    } else {
      Recipient groupRecipient = Recipient.from(context, Address.fromSerialized(groupId), true);
      long      threadId       = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(groupRecipient);
      return new GroupActionResult(groupRecipient, threadId);
    }
  }

  private static GroupActionResult sendGroupUpdate(@NonNull  Context      context,
                                                   @NonNull  String       groupId,
                                                   @NonNull  Set<Address> members,
                                                   @Nullable String       groupName,
                                                   @Nullable byte[]       avatar)
  {
    try {
      Attachment avatarAttachment = null;
      Address    groupAddress     = Address.fromSerialized(groupId);
      Recipient  groupRecipient   = Recipient.from(context, groupAddress, false);

      List<String> numbers = new LinkedList<>();

      for (Address member : members) {
        numbers.add(member.serialize());
      }

      GroupContext.Builder groupContextBuilder = GroupContext.newBuilder()
                                                             .setId(ByteString.copyFrom(GroupUtil.getDecodedId(groupId)))
                                                             .setType(GroupContext.Type.UPDATE)
                                                             .addAllMembers(numbers);
      if (groupName != null) groupContextBuilder.setName(groupName);
      GroupContext groupContext = groupContextBuilder.build();

      if (avatar != null) {
        Uri avatarUri = BlobProvider.getInstance().forData(avatar).createForSingleUseInMemory();
        avatarAttachment = new UriAttachment(avatarUri, MediaUtil.IMAGE_PNG, AttachmentDatabase.TRANSFER_PROGRESS_DONE, avatar.length, null, false, false, null, null);
      }

      OutgoingGroupMediaMessage outgoingMessage = new OutgoingGroupMediaMessage(groupRecipient, groupContext, avatarAttachment, System.currentTimeMillis(), 0, null, Collections.emptyList(), Collections.emptyList());
      long                      threadId        = MessageSender.send(context, outgoingMessage, -1, false, null);

      return new GroupActionResult(groupRecipient, threadId);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  private static Set<Address> getMemberAddresses(Collection<Recipient> recipients) {
    final Set<Address> results = new HashSet<>();
    for (Recipient recipient : recipients) {
      results.add(recipient.getAddress());
    }

    return results;
  }

  public static class GroupActionResult {
    private Recipient groupRecipient;
    private long      threadId;

    public GroupActionResult(Recipient groupRecipient, long threadId) {
      this.groupRecipient = groupRecipient;
      this.threadId       = threadId;
    }

    public Recipient getGroupRecipient() {
      return groupRecipient;
    }

    public long getThreadId() {
      return threadId;
    }
  }
}
