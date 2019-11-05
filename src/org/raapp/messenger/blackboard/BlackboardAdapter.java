package org.raapp.messenger.blackboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.raapp.messenger.R;
import org.raapp.messenger.recipients.Recipient;


import androidx.recyclerview.widget.RecyclerView;

public class BlackboardAdapter extends RecyclerView.Adapter<BlackboardAdapter.ViewHolder>{
    private String[] mDataSet;
    private Context mContext;

    public BlackboardAdapter(Context context,String[] DataSet){
        mDataSet = DataSet;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle, mBody;
        public ViewHolder(View v){
            super(v);
            mTitle = v.findViewById(R.id.note_title);
            mBody =  v.findViewById(R.id.note_title);
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
        holder.mTitle.setText(mDataSet[position]);
        holder.itemView.setOnClickListener(view -> ((BlackboardInterface)mContext).onNoteClick(mDataSet[position]));
    }

    @Override
    public int getItemCount(){
        return mDataSet.length;
    }


}

