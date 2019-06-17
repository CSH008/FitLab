package com.jq.app.ui.createplanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.app.R;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private final NoteListDialog noteLsitDialog;
    private List<String> list;

    public NoteListAdapter(List<String> list, NoteListDialog noteListDialog) {
        this.list = list;
        this.noteLsitDialog = noteListDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNote;
        ImageView imageViewDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNote = itemView.findViewById(R.id.text_view_note);
            imageViewDelete = itemView.findViewById(R.id.image_view_delete);
            imageViewDelete.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        void bindItem(String item) {
            textViewNote.setText(item);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.image_view_delete) {
                list.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                if (list.isEmpty()) {
                    noteLsitDialog.manageVisbilityOfNoNotes(View.VISIBLE);
                }
            }else{
                noteLsitDialog.showForEdit(getAdapterPosition());
            }
        }
    }
}
