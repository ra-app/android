package org.raapp.messenger.conversation.admin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.raapp.messenger.ConversationListActivity;
import org.raapp.messenger.R;
import org.raapp.messenger.client.Client;
import org.raapp.messenger.client.datamodel.CompanyRole;
import org.raapp.messenger.client.datamodel.Request.RequestTicket;
import org.raapp.messenger.client.datamodel.Responses.ResponseCompanyList;
import org.raapp.messenger.client.datamodel.Responses.ResponseTicketDetail;
import org.raapp.messenger.client.datamodel.Responses.ResponseTickets;
import org.raapp.messenger.client.datamodel.Ticket;
import org.raapp.messenger.client.datamodel.TicketDetail;
import org.raapp.messenger.client.datamodel.TicketEvent;
import org.raapp.messenger.logging.Log;
import org.raapp.messenger.recipients.Recipient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminConversationFragment extends Fragment implements AdminConversationAdapter.HeaderListener {

    private static final String TAB_POSITION = "tab_position";
    private static final String ADDRESS_EXTRA = "address";
    private RecyclerView conversationRV;
    private AdminConversationAdapter adapter;
    private ProgressBar progressBar;
    private Context context;
    private int position;
    private String address;
    private List<Object> tickets;

    public static AdminConversationFragment newInstance(int position, String address) {

        Bundle args = new Bundle();
        args.putInt(TAB_POSITION, position);
        args.putString(ADDRESS_EXTRA, address);

        AdminConversationFragment fragment = new AdminConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        final View view = inflater.inflate(R.layout.fragment_admin_conversation, container, false);

        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(TAB_POSITION);
            address = args.getString(ADDRESS_EXTRA);
        }

        context = getActivity();
        conversationRV = view.findViewById(R.id.rv_admin_conversation);
        progressBar = view.findViewById(R.id.progress_bar);
        conversationRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        tickets = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Client.buildBase(context);
        getTicketsInTab(position + 1);
    }


    private void getTicketsInTab(int tab) {
        RequestTicket requestTicket = new RequestTicket(15, 0, tab);
        progressBar.setVisibility(View.VISIBLE);
        Client.getTickets(new Callback<ResponseTickets>() {
            @Override
            public void onResponse(Call<ResponseTickets> call, Response<ResponseTickets> response) {
                if (response.isSuccessful()) {
                    ResponseTickets resp = response.body();

                    if (resp != null && resp.getSuccess()) {
                        if (resp.getTickets() != null)
                            tickets.addAll(resp.getTickets());
                        adapter = new AdminConversationAdapter(getContext(), new ArrayList<>(tickets), AdminConversationFragment.this);
                        conversationRV.setAdapter(adapter);
                    } else{
                        String errorMsg = resp !=null ? resp.getError() : "Unexpected error";
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseTickets> call, Throwable t) {
                Toast.makeText(context, "HTTP error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, requestTicket, address);
    }

    @Override
    public void onClick(int position, String ticketId) {
        progressBar.setVisibility(View.VISIBLE);
        Client.getTicketDetail(new Callback<ResponseTicketDetail>() {
            @Override
            public void onResponse(Call<ResponseTicketDetail> call, Response<ResponseTicketDetail> response) {
                if (response.isSuccessful()){
                    ResponseTicketDetail resp = response.body();

                    if (resp != null && resp.getSuccess()) {
                        List<TicketEvent> ticketEvents = resp.getDetails().getEvents();
                        tickets.addAll(position + 1, ticketEvents);
                        tickets.add(new Object());
                        adapter = new AdminConversationAdapter(getContext(), new ArrayList<>(tickets), AdminConversationFragment.this);
                        conversationRV.setAdapter(adapter);
                    }
                } else{
                    Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseTicketDetail> call, Throwable t) {
                Toast.makeText(context, "HTTP error", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }, address, ticketId);
    }

    @Override
    public void removeRange(int fromIndex, int toIndex) {
        if (tickets != null) {
            tickets.subList(fromIndex, toIndex).clear();
            adapter = new AdminConversationAdapter(getContext(), new ArrayList<>(tickets), AdminConversationFragment.this);
            conversationRV.setAdapter(adapter);
        }
    }
}
