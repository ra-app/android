package org.raapp.messenger.conversation.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.TicketEvent;
import org.raapp.messenger.util.DateUtils;
import org.raapp.messenger.util.ViewUtil;

import java.util.List;

public class AdminChatConversationAdapter extends RecyclerView.Adapter<AdminChatConversationAdapter.ChatViewHolder> {

    private static final int VIEWTYPE_CHAT_RECEIVED_ITEM = 102;
    private static final int VIEWTYPE_CHAT_SENT_ITEM = 103;

    private Context context;

    private List<TicketEvent> objects;

    public AdminChatConversationAdapter(Context context, List<TicketEvent> objects) {
        this.context = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public AdminChatConversationAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_message_sent_item);
        return new AdminChatConversationAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminChatConversationAdapter.ChatViewHolder holder, int position) {
        TicketEvent ticketEvent = objects.get(position);
        try {
            JSONObject obj = new JSONObject(ticketEvent.getJson());
            holder.body.setText(obj.getString("body"));
            String date = DateUtils.getDate(Long.parseLong(obj.getString("timestamp")));
            holder.date.setText(date);
        } catch (Throwable t) {

        }
        if (getItemViewType(position) == VIEWTYPE_CHAT_RECEIVED_ITEM) {
            ViewUtil.updateLayoutParams(holder.itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (getItemViewType(position) == VIEWTYPE_CHAT_SENT_ITEM) {
            ViewUtil.updateLayoutParams(holder.itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public int getItemCount() {
        return objects != null ? objects.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTYPE_CHAT_RECEIVED_ITEM;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView sender;
        private TextView body;
        private TextView date;
        private View itemView;

        View viewLeft, viewRight;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            sender = itemView.findViewById(R.id.tv_sender);
            body = itemView.findViewById(R.id.tv_body);
            date = itemView.findViewById(R.id.tv_date);
        }
    }
}
