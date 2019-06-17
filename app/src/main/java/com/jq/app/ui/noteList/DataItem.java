package com.jq.app.ui.noteList;

public class DataItem {
    private String notes;
    private int notes_id;


    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotesId(int notesId) {
        this.notes_id = notes_id;
    }

    public int getNotesId() {
        return this.notes_id;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DataItem{notes = '");
        stringBuilder.append(this.notes);
        stringBuilder.append('\'');
        stringBuilder.append(",notes_id = '");
        stringBuilder.append(this.notes_id);
        stringBuilder.append('\'');
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
