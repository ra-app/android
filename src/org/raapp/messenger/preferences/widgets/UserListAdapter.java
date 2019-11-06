package org.raapp.messenger.preferences.widgets;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.raapp.messenger.R;
import org.raapp.messenger.RecipientPreferenceActivity;
import org.raapp.messenger.contacts.avatars.ProfileContactPhoto;
import org.raapp.messenger.contacts.avatars.ResourceContactPhoto;
import org.raapp.messenger.mms.GlideApp;
import org.raapp.messenger.mms.GlideRequests;
import org.raapp.messenger.recipients.Recipient;
import org.raapp.messenger.recipients.RecipientExporter;
import org.raapp.messenger.util.TextSecurePreferences;
import org.raapp.messenger.util.Util;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<Recipient> recipients;
    private GlideRequests glideRequests;

    public UserListAdapter(Context context, List<Recipient> recipients) {
        this.context = context;
        this.recipients = recipients;
        this.glideRequests = GlideApp.with(context);
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.preference_user_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipient recipient = recipients.get(position);
        String name;
        if (isLocalNumber(recipient)) {
            name = context.getString(R.string.GroupMembersDialog_me);
        } else {
            name = recipient.toShortString();

            if (recipient.getName() == null && !TextUtils.isEmpty(recipient.getProfileName())) {
                name += " ~" + recipient.getProfileName();
            }
        }
        holder.profileNameTV.setText(name);
        holder.numberTV.setText(recipient.getAddress().toPhoneString());
        //TODO: Set role from recipient
        //holder.roleTV.setText(recipient.getRole());

        glideRequests.load(isLocalNumber(recipient) ? new ProfileContactPhoto(recipient.getAddress(), String.valueOf(TextSecurePreferences.getProfileAvatarId(context))) : recipient.getContactPhoto())
                .fallback(recipient.getFallbackContactPhoto().asCallCard(context))
                .error(new ResourceContactPhoto(R.drawable.ic_profile_default).asDrawable(context, context.getResources().getColor(R.color.grey_400)))
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.avatarIV);

        holder.itemView.setOnClickListener(v -> {
            if (recipient.getContactUri() != null) {
                Intent intent = new Intent(context, RecipientPreferenceActivity.class);
                intent.putExtra(RecipientPreferenceActivity.ADDRESS_EXTRA, recipient.getAddress());
                context.startActivity(intent);
            } else if (isLocalNumber(recipient)) {
                //TODO
                //Intent intent = new Intent(context, ProfilePreference.class);
                //context.startActivity(intent);
                context.startActivity(RecipientExporter.export(recipient).asAddContactIntent());
            } else {
                context.startActivity(RecipientExporter.export(recipient).asAddContactIntent());
            }
        });
    }

    private boolean isLocalNumber(Recipient recipient) {
        return Util.isOwnNumber(context, recipient.getAddress());
    }

    @Override
    public int getItemCount() {
        return recipients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView profileNameTV;
        TextView numberTV;
        TextView roleTV;
        ImageView avatarIV;

        ViewHolder(View itemView) {
            super(itemView);
            profileNameTV = itemView.findViewById(R.id.profile_name);
            numberTV = itemView.findViewById(R.id.number);
            roleTV = itemView.findViewById(R.id.tv_role);
            avatarIV = itemView.findViewById(R.id.avatar);
        }
    }
}
