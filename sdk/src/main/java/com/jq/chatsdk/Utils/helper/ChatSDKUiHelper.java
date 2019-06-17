/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.jq.chatsdk.Utils.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jq.chatsdk.R;
import com.jq.chatsdk.Utils.Debug;
import com.jq.chatsdk.activities.ChatSDKChatActivity;
import com.jq.chatsdk.activities.ChatSDKEditProfileActivity;
import com.jq.chatsdk.activities.ChatSDKLocationActivity;
import com.jq.chatsdk.activities.ChatSDKLoginActivity;
import com.jq.chatsdk.activities.ChatSDKMainActivity;
import com.jq.chatsdk.activities.ChatSDKPickFriendsActivity;
import com.jq.chatsdk.activities.ChatSDKSearchActivity;
import com.jq.chatsdk.activities.ChatSDKShareWithContactsActivity;
import com.jq.chatsdk.activities.ChatSDKThreadDetailsActivity;
import com.jq.chatsdk.activities.abstracted.ChatSDKAbstractChatActivity;
import com.jq.chatsdk.activities.abstracted.ChatSDKAbstractLoginActivity;
import com.jq.chatsdk.activities.abstracted.ChatSDKAbstractProfileActivity;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatSDKUiHelper {

    public static final String TAG = ChatSDKUiHelper.class.getSimpleName();
    public static final boolean DEBUG = Debug.UiUtils;

    /** Set up the ui so every view and nested view that is not EditText will listen to touch event and dismiss the keyboard if touched.*/
    public static void setupTouchUIToDismissKeyboard(View view, View.OnTouchListener onTouchListener) {
        setupTouchUIToDismissKeyboard(view, onTouchListener, -1);
    }

    public static void setupTouchUIToDismissKeyboard(View view, View.OnTouchListener onTouchListener, Integer... exceptIDs) {

        List<Integer> ids = new ArrayList<Integer>();
        if (exceptIDs != null)
            ids = Arrays.asList(exceptIDs);

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            if (!ids.isEmpty() && ids.contains(view.getId()))
            {
                return;
            }

            view.setOnTouchListener(onTouchListener);
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupTouchUIToDismissKeyboard(innerView, onTouchListener, exceptIDs);
            }
        }
    }

    /** Hide the Soft Keyboard.*/
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager == null)
            return;

        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static ChatSDKUiHelper instance;

    /** Instance only contains the classes that need to be used, Dont try to use toast from instance.*/
    public static ChatSDKUiHelper getInstance() {
        if (instance == null) throw new NullPointerException("You need to init ui helper before using the sdk, " +
                "You can use default init if you want to test the sdk or use default components." +
                "The use of this class is to make the BaseComponents of the SDK Ui adapt to your LoginActivity, MainActivity and ChatActivity. ");

        return instance;
    }

    private static final int NULL = -1991;


    private WeakReference<Context> context;
    public Class chatActivity, mainActivity, loginActivity,
            searchActivity = ChatSDKSearchActivity.class,
            pickFriendsActivity = ChatSDKPickFriendsActivity.class,
            shareWithFriendsActivity = ChatSDKShareWithContactsActivity.class,
            shareLocationActivity = ChatSDKLocationActivity.class,
            editProfileActivity= ChatSDKEditProfileActivity.class,
            profileActivity = null,
            threadDetailsActivity = ChatSDKThreadDetailsActivity.class;

    public static ChatSDKUiHelper initDefault(){
        instance = new ChatSDKUiHelper(ChatSDKChatActivity.class, ChatSDKMainActivity.class, ChatSDKLoginActivity.class);
        return getInstance();
    }

    public static ChatSDKUiHelper init(Class chatActivity, Class mainActivity, Class loginActivity) {
        instance = new ChatSDKUiHelper(chatActivity, mainActivity, loginActivity);
        return getInstance();
    }

    public ChatSDKUiHelper(Class chatActivity, Class mainActivity, Class loginActivity) {
        this.chatActivity = chatActivity;
        this.mainActivity = mainActivity;
        this.loginActivity = loginActivity;
    }

    public ChatSDKUiHelper(Context context, Class chatActivity, Class mainActivity, Class loginActivity) {
        this.chatActivity = chatActivity;
        this.mainActivity = mainActivity;
        this.loginActivity = loginActivity;
        this.context = new WeakReference<Context>(context);
    }

    public ChatSDKUiHelper(Context context, Class chatActivity, Class mainActivity, Class loginActivity, Class searchActivity, Class pickFriendsActivity, Class shareWithFriendsActivity, Class shareLocationActivity, Class profileActivity) {
        this.context = new WeakReference<Context>(context);
        this.chatActivity = chatActivity;
        this.mainActivity = mainActivity;
        this.loginActivity = loginActivity;
        this.searchActivity = searchActivity;
        this.pickFriendsActivity = pickFriendsActivity;
        this.shareWithFriendsActivity = shareWithFriendsActivity;
        this.shareLocationActivity = shareLocationActivity;
        this.profileActivity = profileActivity;
    }

    public ChatSDKUiHelper get(Context context){
        return new ChatSDKUiHelper(context, chatActivity, mainActivity, loginActivity, searchActivity, pickFriendsActivity, shareWithFriendsActivity, shareLocationActivity, profileActivity);
    }

    /** Start the chat activity for given thread id.*/
    public void startChatActivityForID(long id){
        
        if (colleted())
            return;
        
        Intent intent = new Intent(context.get(), chatActivity);
        intent.putExtra(ChatSDKAbstractChatActivity.THREAD_ID, id);
        
        /**
         * Note
         *
         * * Not sure if do needed
         * 
         * There could be problems when this is activated but i am not sure if the activity should be recreated each time its called.
         * * * *
         **/
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        
        startActivity(intent);
    }

    public void startLoginActivity(boolean loggedOut){
        Intent intent = new Intent(context.get(), loginActivity);
        intent.putExtra(ChatSDKAbstractLoginActivity.FLAG_LOGGED_OUT, loggedOut);
        startActivity(intent);
    }

    public void startMainActivity(){
        startActivity(mainActivity);
    }

    public void startSearchActivity(){
        startActivity(searchActivity);
    }

    public void startPickFriendsActivity(){
        startActivity(pickFriendsActivity);
    }

    public void startShareWithFriendsActivity(){
        if (colleted())
            return;

        startActivity(shareWithFriendsActivity);
    }

    public void startShareLocationActivityActivity(){
        if (colleted())
            return;

        startActivity(shareLocationActivity);
    }

    public boolean startProfileActivity(String entityId){

        if (colleted())
            return false;
        
        if (profileActivity==null)
            return false;

        Intent intent = new Intent(context.get(), profileActivity);
        intent.putExtra(ChatSDKAbstractProfileActivity.USER_ENTITY_ID, entityId);

        startActivity(intent);

        return true;
    }

    public boolean startProfileActivity(long id){

        if (colleted())
            return false;

        if (profileActivity==null)
            return false;

        Intent intent = new Intent(context.get(), profileActivity);
        intent.putExtra(ChatSDKAbstractProfileActivity.USER_ID, id);

        startActivity(intent);

        return true;
    }

    public void startEditProfileActivity(long id){

        if (colleted())
           return;

        if (editProfileActivity==null)
            return;

        Intent intent = new Intent(context.get(), editProfileActivity);
        intent.putExtra(ChatSDKAbstractProfileActivity.USER_ID, id);

        startActivity(intent);
    }



    
    public interface ChatSDKUiHelperInterface{
        /** Start the chat activity for given thread id.*/
        public void startChatActivityForID(long id);

        public void startLoginActivity(boolean loggedOut);

        public void startMainActivity();

        public void startSearchActivity();

        public void startPickFriendsActivity();

        public void startShareWithFriendsActivity();

        public void startShareLocationActivityActivity();
    }

    /** You should pass שמ Activity and not a context if you want to use this.*/
    public void dismissProgressCard(){
        dismissProgressCard(0);
    }

    /** You should pass Activity and not a context if you want to use this.*/
    public void dismissProgressCardWithSmallDelay(){
        dismissProgressCard(1500);
    }

    /** You should pass שמ Activity and not a context if you want to use this.*/
    public void dismissProgressCard(long delay){
        if (mProgressDialog == null)
            return;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        }, delay);
    }

    private ProgressDialog mProgressDialog;
    /** You should pass שמ Activity and not a context if you want to use this.*/
    public void showProgressCard(String text){
        if(context.get()==null) return;

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context.get());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(text);
        }

        mProgressDialog.show();
    }

    /** You should pass שמ Activity and not a context if you want to use this.*/
    public void showProgressCard(@StringRes int resourceId){
        showProgressCard(context.get().getString(resourceId));
    }

    /*Getters and Setters*/
    public void showAlertToast(String text){
        if(context.get()!=null) {
            Toast.makeText(context.get(), text, Toast.LENGTH_LONG).show();
        }
    }

    public void showAlertToast(@StringRes int resourceId){
        if (context.get() == null)
            return;

        showAlertToast(context.get().getString(resourceId));
    }

    public void showToast(String text){
        if(context.get()!=null) {
            Toast.makeText(context.get(), text, Toast.LENGTH_LONG).show();
        }
    }

    public void showToast(@StringRes int resourceId){
        if (context.get() == null)
            return;

        showToast(context.get().getString(resourceId));
    }

    public void setSearchActivity(Class searchActivity) {
        this.searchActivity = searchActivity;
    }

    public void setPickFriendsActivity(Class pickFriendsActivity) {
        this.pickFriendsActivity = pickFriendsActivity;
    }

    public void setShareWithFriendsActivity(Class shareWithFriendsActivity) {
        this.shareWithFriendsActivity = shareWithFriendsActivity;
    }

    public void setShareLocationActivity(Class shareLocationActivity) {
        this.shareLocationActivity = shareLocationActivity;
    }

    public void setProfileActivity(Class profileActivity) {
        this.profileActivity = profileActivity;
    }
    
    private boolean colleted(){
        return context == null || context.get() == null;
        
    }

    private void startActivity(Intent intent){
        if (colleted())
            return;

        context.get().startActivity(intent);
    }
    
    private void startActivity(Class activity){
        if (colleted())
        {
            return;
        }

        startActivity(new Intent(context.get(), activity));
    }
}


