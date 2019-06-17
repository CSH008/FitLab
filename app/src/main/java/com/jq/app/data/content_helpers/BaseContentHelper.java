package com.jq.app.data.content_helpers;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.jq.app.R;
import com.jq.app.data.model.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 10/15/2016.
 */

public class BaseContentHelper {
    public OnDataLoadListener actionListener;

    public static final int REQUEST_LIST = 1;
    public static final int REQUEST_CREATE = 2;
    public static final int REQUEST_EDIT = 3;
    public static final int REQUEST_DELETE = 4;
    public static final int REQUEST_DELETE_ALL = 5;

    public List<BaseModel> ITEMS = new ArrayList<>();
    public Map<String, BaseModel> ITEM_MAP = new HashMap<>();
    public int COUNT = 0;

    public BaseContentHelper(OnDataLoadListener listener) {
        this.actionListener = listener;
    }

    public void getRequestJSONObject(final int action, final String url, final Map<String, String> requestParam, final int index) {
        AndroidNetworking.get(url)
                .addQueryParameter(requestParam)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String tempUrl = url;
                            Map<String, String> param = requestParam;
                            int status = response.getInt("status");
                            if(status==1) {
                                dealResponseObject(action, response);
                                finishedAction(action, index, 0);

                            } else {
                                String errorMsg = response.getString("error");
                                if(errorMsg!=null) {
                                    finishedAction(action, index, errorMsg);

                                } else {
                                    finishedAction(action, index, R.string.msg_failed);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);

                        } catch (IOException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        finishedAction(action, index, R.string.msg_network_error);
                    }
                });
    }

    public void getRequestString(final int action, String url, Map<String, String> requestParam, final int index) {
        AndroidNetworking.get(url)
                .addQueryParameter(requestParam)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resObject = new JSONObject(response);
                            int status = resObject.getInt("status");
                            if(status==1) {
                                dealResponseObject(action, resObject);
                                finishedAction(action, index, 0);

                            } else {
                                String errorMsg = resObject.getString("error");
                                if(errorMsg!=null) {
                                    finishedAction(action, index, errorMsg);

                                } else {
                                    finishedAction(action, index, R.string.msg_no_result);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);

                        } catch (IOException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        finishedAction(action, index, R.string.msg_network_error);
                    }
                });
    }

    public void getRequestJSONArray(final int action, String url, Map<String, String> requestParam, final int index) {
        AndroidNetworking.get(url)
                .addQueryParameter(requestParam)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        try {
                            for(int i=0; i<responseArray.length(); i++) {
                                JSONObject item = responseArray.getJSONObject(i);
                                dealResponseItemObject(action, item);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        finishedAction(action, index, R.string.msg_network_error);
                    }
                });
    }

    public void postRequestJSONObject(final int action, String url, Map<String, String> requestParam, final int index) {
        AndroidNetworking.post(url)
                .addBodyParameter(requestParam)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if(status==1) {
                                dealResponseObject(action, response);
                                finishedAction(action, index, 0);

                            } else {
                                String errorMsg = response.getString("error");
                                if(errorMsg!=null) {
                                    finishedAction(action, index, errorMsg);

                                } else {
                                    finishedAction(action, index, R.string.msg_no_result);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);

                        } catch (IOException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        finishedAction(action, index, R.string.msg_network_error);
                    }
                });
    }

    public void postRequestJSONArray(final int action, String url, Map<String, String> requestParam, final int index) {
        AndroidNetworking.post(url)
                .addBodyParameter(requestParam)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        try {
                            for(int i=0; i<responseArray.length(); i++) {
                                JSONObject item = responseArray.getJSONObject(i);
                                dealResponseItemObject(action, item);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            finishedAction(action, index, R.string.msg_json_error);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        finishedAction(action, index, R.string.msg_network_error);
                    }
                });
    }

    public void dealResponseObject(int action, JSONObject object)  throws JSONException, IOException {}

    public void dealResponseItemObject(int action, JSONObject object) {}

    public void clearItems() {
        ITEMS.clear();
        COUNT = 0;
    }

    public void addItem(BaseModel item) {
        ITEMS.add(item);
        COUNT++;
        ITEM_MAP.put("" + item.id, item);
    }

    public interface OnDataLoadListener {
        // TODO: Update argument type and name
        void onFinishedAction(int action, int index, int errMsg);
        void onFinishedAction(int action, int index, String errMsg);
    }

    public void finishedAction(int action, int index, int errMsg) {
        if(actionListener!=null) {
            actionListener.onFinishedAction(action, index, errMsg);
        }
    }

    public void finishedAction(int action, int index, String errMsg) {
        if(actionListener!=null) {
            actionListener.onFinishedAction(action, index, errMsg);
        }
    }

}
