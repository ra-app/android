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


public class AdminConversationAdapter extends RecyclerView.Adapter<AdminConversationAdapter.ViewHolder> {

    private Context context;
    private List<Ticket> tickets;

    public AdminConversationAdapter(Context context, List<Ticket> tickets){
        this.context = context;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_item);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);

        holder.name.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
        holder.ticketName.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
        holder.date.setText(ticket.getTsCreated() != null ? ticket.getTsCreated() : "Unknown");
    }

    @Override
    public int getItemCount() {
        return tickets != null ? tickets.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, ticketName, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ticketName = itemView.findViewById(R.id.ticket_name);
            date = itemView.findViewById(R.id.date);
        }
    }
}
