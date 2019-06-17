package com.jq.app.ui.createplanner;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jq.app.R;

import java.util.ArrayList;
import java.util.List;

public class NoteListDialog extends Dialog {
    private final Context context;
    Callback callback;
    private List<String> notes;
    private NoteListAdapter adapter;
    private View noNote;
    private int positionForedit;

    public NoteListDialog(@NonNull Context context, List<String> notes, Callback callback) {
        super(context);
        this.callback = callback;
        this.notes = notes;
        this.context = context;
        init(context);
    }

    private void init(final Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_note_list);
        noNote = findViewById(R.id.no_notes);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        findViewById(R.id.text_view_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                callback.onNotesList(notes);
            }
        });
        findViewById(R.id.text_view_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNote(false, "");
            }
        });
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new NoteListAdapter(notes, this);
        recyclerView.setAdapter(adapter);
        if (notes.size() < 1) {
            noNote.setVisibility(View.VISIBLE);
        } else {
            noNote.setVisibility(View.GONE);
        }
    }

    private void showAddNote(final boolean isEdit, String text) {
        new InputNoteDialog(context, text, new InputNoteDialog.Callback() {
            @Override
            public void onSave(String text) {
                if (!text.isEmpty()) {
                    if (isEdit) {
                        notes.set(positionForedit, text);
                        adapter.notifyItemChanged(positionForedit);
                    } else {
                        notes.add(text);
                        adapter.notifyItemInserted(notes.size());
                        manageVisbilityOfNoNotes(View.GONE);
                    }
                }
            }
        }).show();
    }

    public void showForEdit(int position) {
        this.positionForedit = position;
        showAddNote(true, notes.get(position));

    }

    public interface Callback {
        void onNotesList(List<String> notes);
    }

    public void manageVisbilityOfNoNotes(int visiblity) {
        noNote.setVisibility(visiblity);
    }
}
