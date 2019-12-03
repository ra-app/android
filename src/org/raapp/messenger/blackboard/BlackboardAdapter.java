package org.raapp.messenger.blackboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.raapp.messenger.R;
import org.raapp.messenger.client.datamodel.Note;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static org.raapp.messenger.blackboard.BlacboardActivity.NOTE_TYPE_CALENDAR;

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
        public ImageView mTriagleIV;
        public View mNoteRL;
        public ViewHolder(View v){
            super(v);
            mTitle = v.findViewById(R.id.note_title);
            mBody =  v.findViewById(R.id.note_body);
            mTriagleIV = v.findViewById(R.id.triangle);
            mNoteRL = v.findViewById(R.id.rl_note);
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
            Note note = (Note) mNotes.get(position);
            ((ViewHolder)holder).mTitle.setText(note.getTitle());
            ((ViewHolder)holder).mBody.setText(note.getContent());
            holder.itemView.setOnClickListener(view -> ((BlackboardInterface) mContext).onNoteClick(note));

            ((ViewHolder)holder).mNoteRL.setBackgroundColor(mContext.getResources().getColor(R.color.ra_yellow_note));
            ((ViewHolder)holder).mTriagleIV.setColorFilter(ContextCompat.getColor(mContext, R.color.ra_yellow_dark_note), android.graphics.PorterDuff.Mode.SRC_IN);
            if(note.getNoteType()!= null && NOTE_TYPE_CALENDAR.equals(note.getNoteType())){
                ((ViewHolder)holder).mNoteRL.setBackgroundColor(mContext.getResources().getColor(R.color.ra_blue_note));
                ((ViewHolder)holder).mTriagleIV.setColorFilter(ContextCompat.getColor(mContext, R.color.ra_blue_dark_note), android.graphics.PorterDuff.Mode.SRC_IN);
            }

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

