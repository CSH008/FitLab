package com.jq.app.ui.noteList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.InputCallback;
import com.google.gson.JsonObject;
import com.jq.app.App;
import com.jq.app.R;
import com.jq.app.network.ApiCallback;
import com.jq.app.network.ApiClient;
import com.jq.app.network.ApiStores;
import com.jq.app.util.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoteFragment extends BaseFragment {
    static String mdateFrom = "2019-01-01";
    static String mdateTo = "2019-12-29";
    Context context;
    private ArrayList<DataItem> mValue = new ArrayList();
    NoteAdapter noteAdapter;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    /* renamed from: com.jq.app.ui.noteList.NoteFragment$1 */
    class C16221 implements OnclickNote {
        C16221() {
        }

        public void onClickEdit(final String note_id, String note_txt) {
            new Builder(NoteFragment.this.context).title("Enter Note").inputType(1).input("Write here...", note_txt, new InputCallback() {
                public void onInput(MaterialDialog dialog, CharSequence input) {
                    if (input == null || input.toString().isEmpty()) {
                        Toast.makeText(NoteFragment.this.context, "Please enter snote", Toast.LENGTH_SHORT).show();
                    } else {
                        NoteFragment.this.editNotesAPI(note_id, input.toString());
                    }
                }
            }).cancelable(false).show();
        }

        public void onClickDelete(String id) {
            NoteFragment.this.deleteNotesAPI(id);
        }
    }

    /* renamed from: com.jq.app.ui.noteList.NoteFragment$2 */
    class C16232 extends ApiCallback<Response> {
        C16232() {
        }

        public void onSuccess(Response jsonObject) {
            try {
                if (jsonObject.getData().size() > 0) {
                    NoteFragment.this.mValue.clear();
                    NoteFragment.this.mValue.addAll(jsonObject.getData());
                    NoteFragment.this.noteAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NoteFragment.this.context, jsonObject.getMessage(), Toast.LENGTH_SHORT).show();
                }
                NoteFragment.this.progressDialog.dismiss();
            } catch (Exception e) {
                NoteFragment.this.progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            NoteFragment.this.progressDialog.dismiss();
        }

        public void onTokenExpire() {
        }
    }

    /* renamed from: com.jq.app.ui.noteList.NoteFragment$3 */
    class C16243 extends ApiCallback<JsonObject> {
        C16243() {
        }

        public void onSuccess(JsonObject jsonObject) {
            try {
                NoteFragment.this.progressDialog.dismiss();
            } catch (Exception e) {
                NoteFragment.this.progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            NoteFragment.this.progressDialog.dismiss();
            NoteFragment.this.getNote(NoteFragment.mdateFrom, NoteFragment.mdateTo);
        }

        public void onTokenExpire() {
        }
    }

    /* renamed from: com.jq.app.ui.noteList.NoteFragment$4 */
    class C16254 extends ApiCallback<JsonObject> {
        C16254() {
        }

        public void onSuccess(JsonObject jsonObject) {
            try {
                NoteFragment.this.progressDialog.dismiss();
            } catch (Exception e) {
                NoteFragment.this.progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        public void onFailure(String msg) {
            Log.e("onFailure", msg);
        }

        public void onFinish() {
            NoteFragment.this.progressDialog.dismiss();
            NoteFragment.this.getNote(NoteFragment.mdateFrom, NoteFragment.mdateTo);
        }

        public void onTokenExpire() {
        }
    }

    public static NoteFragment getInstance(String dateFrom, String dateTo) {
        mdateFrom = dateFrom;
        mdateTo = dateTo;
        return new NoteFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        this.context = getContext();
        this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setMessage("please wait");
        this.recyclerView = (RecyclerView) view.findViewById(R.id.note_list);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        this.noteAdapter = new NoteAdapter(this.mValue, new C16221());

        recyclerView.setAdapter(noteAdapter);

        getNote(mdateFrom, mdateTo);
        return view;
    }

    private void getNote(String dateFrom, String dateTo) {
        this.progressDialog.show();
        ApiCallback apiCallback = new C16232();
        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);
        addCall(((ApiStores) ApiClient.retrofit(this.context).create(ApiStores.class)).getNotes(map), apiCallback);
    }

    private void editNotesAPI(String notes_id, String note) {
        this.progressDialog.show();
        ApiCallback apiCallback = new C16243();
        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("notes_id", notes_id);
        map.put("notes", note);
        addCall(((ApiStores) ApiClient.retrofit(this.context).create(ApiStores.class)).editNotes(map), apiCallback);
    }

    private void deleteNotesAPI(String notes_id) {
        this.progressDialog.show();
        ApiCallback apiCallback = new C16254();
        Map<String, String> map = new HashMap();
        map.put("email", App.my_email);
        map.put("notes_id", notes_id);
        addCall(((ApiStores) ApiClient.retrofit(this.context).create(ApiStores.class)).deleteNotes(map), apiCallback);
    }
}
