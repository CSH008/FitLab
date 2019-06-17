package com.jq.app.ui.noteList;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;

import java.util.ArrayList;

public class NoteAdapter extends Adapter<NoteAdapter.ViewHolder> {
    String TAG = NoteAdapter.class.getName();
    private Context context;
    OnclickNote mOnclickNote;
    private ArrayList<DataItem> mValues;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public final ImageView delete;
        public final ImageView edit;
        public final TextView note;

        public ViewHolder(View view) {
            super(view);
            this.note = (TextView) view.findViewById(R.id.note);
            this.delete = (ImageView) view.findViewById(R.id.delete);
            this.edit = (ImageView) view.findViewById(R.id.edit);
        }
    }

    public NoteAdapter(ArrayList<DataItem> items, OnclickNote onclickNote) {
        this.mValues = items;
        this.mOnclickNote = onclickNote;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_note, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            holder.note.setText(((DataItem) this.mValues.get(position)).getNotes());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnclickNote.onClickEdit(mValues.get(position).getNotesId()+"",mValues.get(position).getNotes());
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnclickNote.onClickDelete(mValues.get(position).getNotesId()+"");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.mValues.size();
    }
}
