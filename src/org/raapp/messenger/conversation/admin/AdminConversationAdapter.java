package org.raapp.messenger.conversation.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.Ticket;
import org.raapp.messenger.client.datamodel.TicketEvent;
import org.raapp.messenger.util.ViewUtil;

import java.util.List;


public class AdminConversationAdapter extends RecyclerView.Adapter<AdminConversationAdapter.HeaderViewHolder> {

    private static final int VIEWTYPE_HEADER_ITEM = 101;

    private Context context;

    private List<Ticket> objects;
    private HeaderListener headerListener;

    public AdminConversationAdapter(Context context, List<Ticket> objects, HeaderListener headerListener){
        this.context = context;
        this.objects = objects;
        this.headerListener = headerListener;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new LinearLayout(context);

        if (viewType == VIEWTYPE_HEADER_ITEM) {
            view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_item);
            return new HeaderViewHolder(view);
        }
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        if (getItemViewType(position) == VIEWTYPE_HEADER_ITEM) {
            Ticket ticket = objects.get(position);
            holder.name.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
            holder.ticketName.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
            holder.date.setText(ticket.getTsCreated() != null ? ticket.getTsCreated() : "Unknown");
            holder.itemView.setOnClickListener(v -> {
                if (ticket.isExpanded()) {
                    holder.chatRV.setVisibility(View.GONE);
                } else {
                    headerListener.onClick(position, ticket.getUuid());
                    holder.chatRV.setVisibility(View.VISIBLE);
                }
                ticket.setExpanded(!ticket.isExpanded());
            });

            if (ticket.getTicketEvents() != null && ticket.getTicketEvents().size() > 0) {
                holder.chatRV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                AdminChatConversationAdapter adapter = new AdminChatConversationAdapter(context, objects.get(position).getTicketEvents());
                holder.chatRV.setAdapter(adapter);
            }
        }
    }

    public void setTicketEvents(int position, List<TicketEvent> ticketEvents) {
        objects.get(position).setTicketEvents(ticketEvents);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return objects != null ? objects.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTYPE_HEADER_ITEM;
    }

    interface HeaderListener {
        void onClick(int position, String ticketId);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView ticketName;
        private TextView date;
        private RecyclerView chatRV;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ticketName = itemView.findViewById(R.id.ticket_name);
            date = itemView.findViewById(R.id.date);
            chatRV = itemView.findViewById(R.id.rv_chat_list);
        }
    }
}
