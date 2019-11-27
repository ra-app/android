package org.raapp.messenger.conversation.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.Ticket;
import org.raapp.messenger.client.datamodel.TicketEvent;
import org.raapp.messenger.util.ViewUtil;

import java.util.List;


public class AdminConversationAdapter extends RecyclerView.Adapter<AdminConversationAdapter.BaseViewHolder> {
    private static final int VIEWTYPE_HEADER_ITEM = 101;
    private static final int VIEWTYPE_CHAT_RECEIVED_ITEM = 102;
    private static final int VIEWTYPE_CHAT_SENT_ITEM = 103;

    private Context context;

    private List<Object> objects;
    private HeaderListener headerListener;

    //CONSTRUCTOR
    public AdminConversationAdapter(Context context, List<Object> objects, HeaderListener headerListener){
        this.context = context;
        this.objects = objects;
        this.headerListener = headerListener;
    }

    //ADAPTER
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new LinearLayout(context);

        if (viewType == VIEWTYPE_HEADER_ITEM) {
            view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_item);
            return new HeaderViewHolder(view);
        } else if (viewType == VIEWTYPE_CHAT_RECEIVED_ITEM || viewType == VIEWTYPE_CHAT_SENT_ITEM) {
            view = ViewUtil.inflate(LayoutInflater.from(context), parent, R.layout.admin_conversation_message_sent_item);
            return new ChatViewHolder(view);
        }
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (getItemViewType(position) == VIEWTYPE_HEADER_ITEM) {
            Ticket ticket = (Ticket) objects.get(position);
            ((HeaderViewHolder)holder).position = position;
            ((HeaderViewHolder)holder).name.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
            ((HeaderViewHolder)holder).ticketName.setText(ticket.getName() != null ? ticket.getName() : "Unknown");
            ((HeaderViewHolder)holder).date.setText(ticket.getTsCreated() != null ? ticket.getTsCreated() : "Unknown");
            ((HeaderViewHolder)holder).itemView.setOnClickListener(v -> {
                if (ticket.isExpanded()) {
                    deleteTicketEvents(position);
                } else {
                    headerListener.onClick(position, ticket.getUuid());
                }
                ticket.setExpanded(!ticket.isExpanded());
            });
        } else if (getItemViewType(position) == VIEWTYPE_CHAT_RECEIVED_ITEM) {
            TicketEvent ticketEvent = (TicketEvent) objects.get(position);
            ((ChatViewHolder)holder).body.setText(ticketEvent.getJson());
            ViewUtil.updateLayoutParams(((ChatViewHolder) holder).itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (getItemViewType(position) == VIEWTYPE_CHAT_SENT_ITEM) {
            TicketEvent ticketEvent = (TicketEvent) objects.get(position);
            ((ChatViewHolder)holder).body.setText(ticketEvent.getJson());
            ViewUtil.updateLayoutParams(((ChatViewHolder) holder).itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void deleteTicketEvents(Integer position) {
        int firstPos = 0, lastPos = 0;
        boolean firstTime = false;
        for (int i = position + 1; i < objects.size(); i++){
            if (objects.get(i) instanceof TicketEvent){
                if (!firstTime) {
                    firstPos = i;
                    firstTime = true;
                }
            }else{
                lastPos = i;
                break;
            }
        }
        headerListener.removeRange(firstPos, lastPos);
    }

    @Override
    public int getItemCount() {
        return objects != null ? objects.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (objects.get(position) instanceof Ticket) {
            viewType = VIEWTYPE_HEADER_ITEM;
        } else if (objects.get(position) instanceof TicketEvent) {
            viewType = VIEWTYPE_CHAT_RECEIVED_ITEM;
            //TODO Could be RECEIVED or SENT
        }

        return viewType;
    }

    interface HeaderListener {
        void onClick(int position, String ticketId);
        void removeRange(int fromIndex, int toIndex);
    }

    //VIEWHOLDER CLASSESS
    class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public class HeaderViewHolder extends BaseViewHolder {
        private TextView name;
        private TextView ticketName;
        private TextView date;
        private Integer position;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ticketName = itemView.findViewById(R.id.ticket_name);
            date = itemView.findViewById(R.id.date);
        }
    }

    public class ChatViewHolder extends BaseViewHolder {
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
