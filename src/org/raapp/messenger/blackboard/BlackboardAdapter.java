package org.raapp.messenger.blackboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.Note;
import org.raapp.messenger.recipients.Recipient;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlackboardAdapter extends RecyclerView.Adapter<BlackboardAdapter.ViewHolder>{
    private List<Note> mNotes;
    private Context mContext;

    public BlackboardAdapter(Context context, List<Note> notes){
        mNotes = notes;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle, mBody;
        public ViewHolder(View v){
            super(v);
            mTitle = v.findViewById(R.id.note_title);
            mBody =  v.findViewById(R.id.note_body);
        }
    }

    @Override
    public BlackboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.pin_note_blackboard_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.mTitle.setText(mNotes.get(position).getTitle());
        holder.mBody.setText(mNotes.get(position).getContent());
        holder.itemView.setOnClickListener(view -> ((BlackboardInterface)mContext).onNoteClick(mNotes.get(position)));
    }

    @Override
    public int getItemCount(){
        return mNotes.size();
    }

}

