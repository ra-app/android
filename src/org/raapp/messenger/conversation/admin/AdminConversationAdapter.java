package org.raapp.messenger.conversation.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.Ticket;
import org.raapp.messenger.util.ViewUtil;

import java.util.List;


public class AdminConversationAdapter extends RecyclerView.Adapter<AdminConversationAdapter.BaseViewHolder> {

    private static final int HEADER_ITEM = 0;
    private static final int CHAT_ITEM = 1;

    private Context context;
    private List<Ticket> tickets;

    public AdminConversationAdapter(Context context, List<Ticket> tickets){
        this.context = context;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_item);
        if (viewType == HEADER_ITEM) {
            return new ViewHolder(view);
        } else if (viewType == CHAT_ITEM) {
            view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_message_sent_item);
            return new ChatViewHolder(view);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        if (getItemViewType(position) == HEADER_ITEM) {
            ((ViewHolder)holder).name.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
            ((ViewHolder)holder).ticketName.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
            ((ViewHolder)holder).date.setText(ticket.getTsCreated() != null ? ticket.getTsCreated() : "Unknown");
        } else if (getItemViewType(position) == CHAT_ITEM) {
            //TODO: Bind data to chat item here
            //if (isSent()) {
            //
            // } else {
            //
            // }
            ViewUtil.updateLayoutParams(((ChatViewHolder) holder).itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public int getItemCount() {
        return tickets != null ? tickets.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return HEADER_ITEM;
        }
        return CHAT_ITEM;
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends BaseViewHolder {

        TextView name, ticketName, date;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ticketName = itemView.findViewById(R.id.ticket_name);
            date = itemView.findViewById(R.id.date);
        }
    }

    public class ChatViewHolder extends BaseViewHolder {

        View viewLeft, viewRight;
        TextView sender, body, date;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.tv_sender);
            body = itemView.findViewById(R.id.tv_body);
            date = itemView.findViewById(R.id.tv_date);
        }
    }
}
