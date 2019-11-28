package org.raapp.messenger.blackboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.Note;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlackboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEWTYPE_NOTE = 0;
    private static final int VIEWTYPE_OBJECT = 1;

    private List<Object> mNotes;
    private Context mContext;

    public BlackboardAdapter(Context context, List<Object> notes){
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

    public static class ObjViewHolder extends RecyclerView.ViewHolder{
        public ObjViewHolder(View v){
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = new LinearLayout(mContext);
        if (viewType == VIEWTYPE_NOTE) {
            v = LayoutInflater.from(mContext).inflate(R.layout.pin_note_blackboard_item, parent, false);
            return new ViewHolder(v);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.pin_add_blackboard_item, parent, false);
            return new ObjViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if (getItemViewType(position) == VIEWTYPE_NOTE) {
            ((ViewHolder)holder).mTitle.setText(((Note) mNotes.get(position)).getTitle());
            ((ViewHolder)holder).mBody.setText(((Note) mNotes.get(position)).getContent());
            holder.itemView.setOnClickListener(view -> ((BlackboardInterface) mContext).onNoteClick(mNotes.get(position)));
        } else {
            holder.itemView.setOnClickListener(view -> {
                ((BlackboardInterface) mContext).onAddNoteClick();
            });
        }
    }

    @Override
    public int getItemCount(){
        return mNotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mNotes.get(position) instanceof Note) {
            return VIEWTYPE_NOTE;
        } else {
            return VIEWTYPE_OBJECT;
        }
    }
}

