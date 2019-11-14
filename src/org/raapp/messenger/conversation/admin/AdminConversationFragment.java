package org.raapp.messenger.conversation.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.raapp.messenger.R;


public class AdminConversationFragment extends Fragment {

    private RecyclerView conversationRV;
    private AdminConversationAdapter adapter;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        final View view = inflater.inflate(R.layout.fragment_admin_conversation, container, false);

        conversationRV = view.findViewById(R.id.rv_admin_conversation);
        adapter = new AdminConversationAdapter(getContext());
        conversationRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        conversationRV.setAdapter(adapter);

        return view;
    }
}
