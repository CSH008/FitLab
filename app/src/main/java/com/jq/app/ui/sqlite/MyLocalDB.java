//package com.jq.app.ui.sqlite;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import com.jq.app.ui.calendar.CountModel;
//import com.jq.app.ui.calendar.EventModel;
//
//import java.util.ArrayList;
//
///**
// * Created by Hasnain on 17-Feb-18.
// */
//
//public class MyLocalDB extends SQLiteOpenHelper implements DatabaseOperation {
//    private static final String TAG = MyLocalDB.class.getSimpleName();
//    private static final String DATABASE_NAME = "event.db";
//    private static final int DATABASE_VERSION = 1;
//    private static final String TABLE_NAME = "tbl_event";
//
//    private static final String EVENT_ID = "_id";
//    private static final String EVENT_TITLE = "title";
//    private static final String EVENT_SUBJECT = "subject";
//    private static final String EVENT_DESCRIPTION = "description";
//    private static final String EVENT_DATE = "date";
//    private static final String EVENT_TIME = "time";
//
//    private static final String CREATE_TABLE_EVENT = "Create table " + TABLE_NAME + " ("
//            + EVENT_ID + " integer primary key autoincrement, "
//            + EVENT_TITLE + " text, "
//            + EVENT_SUBJECT + " text, "
//            + EVENT_DESCRIPTION + " text, "
//            + EVENT_DATE + " text, "
//            + EVENT_TIME + " text)";
//
//    private static MyLocalDB myLocalDB = null;
//    private Context mContext;
//
//
//    public static MyLocalDB getInstance(Context context) {
//        if (myLocalDB == null)
//            myLocalDB = new MyLocalDB(context);
//        return myLocalDB;
//
//    }
//
//    public MyLocalDB(Context context) {
//        super(context, MyLocalDB.DATABASE_NAME, null, DATABASE_VERSION);
//        this.mContext = context;
//    }
//
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE_TABLE_EVENT);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MyLocalDB.TABLE_NAME);
//        onCreate(sqLiteDatabase);
//
//    }
//
//    @Override
//    public long insert(String title, String subject, String description, String date,String time) {
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(MyLocalDB.EVENT_TITLE, title);
//            values.put(MyLocalDB.EVENT_SUBJECT, subject);
//            values.put(MyLocalDB.EVENT_DESCRIPTION, description);
//            values.put(MyLocalDB.EVENT_DATE, date);
//            values.put(MyLocalDB.EVENT_TIME, time);
//
//            // Inserting Row
//            return db.insert(MyLocalDB.TABLE_NAME, null, values);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return 0;
//    }
//
//    @Override
//    public int update(String event_id) {
//        return 0;
//    }
//
//    @Override
//    public boolean delete(String event_id) {
//        boolean isDelete = false;
//        try {
//            SQLiteDatabase db = getWritableDatabase();
//            isDelete = db.delete(MyLocalDB.TABLE_NAME, MyLocalDB.EVENT_ID + "=" + event_id, null) > 0;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return isDelete;
//    }
//
//
////    @Override
////    public ArrayList<EventModel> getAllEvent() {
////        ArrayList<EventModel> arrayList = new ArrayList<>();
////        try {
////            SQLiteDatabase db = this.getReadableDatabase();
////            /**
////             * get all event record
////             */
////            String[] column = new String[]{MyLocalDB.EVENT_ID, MyLocalDB.EVENT_TITLE, MyLocalDB.EVENT_SUBJECT, MyLocalDB.EVENT_DESCRIPTION, MyLocalDB.EVENT_DATE};
////            Cursor mCursor = db.query(MyLocalDB.TABLE_NAME, column, null, null, null, null, null);
////            if (mCursor.moveToFirst()){
////                do{
////                    arrayList.add(new EventModel(
////                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_ID)),
////                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_TITLE)),
////                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_SUBJECT)),
////                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_DESCRIPTION)),
////                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_DATE))
////                    ));
////
////                }while(mCursor.moveToNext());
////            }
////            mCursor.close();
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return arrayList;
////
////    }
//
//    @Override
//    public ArrayList<EventModel> getEventByDate(String date) {
//        ArrayList<EventModel> arrayList = new ArrayList<>();
//        try {
//            SQLiteDatabase db = this.getReadableDatabase();
//            /**
//             * get all event record
//             */
//            String query = "select * from " + MyLocalDB.TABLE_NAME + " where " + MyLocalDB.EVENT_DATE + " = \"" + date + "\"";
//            Log.e(TAG, "query: " + query);
//
//            Cursor mCursor = db.rawQuery(query, null);
//            if (mCursor.moveToFirst()) {
//                do {
//                    arrayList.add(new EventModel(
//                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_ID)),
//                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_TITLE)),
//                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_SUBJECT)),
//                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_DESCRIPTION)),
//                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_DATE)),
//                            mCursor.getString(mCursor.getColumnIndex(MyLocalDB.EVENT_TIME))
//                    ));
//
//                } while (mCursor.moveToNext());
//            }
//            mCursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arrayList;
//    }
//
//    @Override
//    public ArrayList<CountModel> getCount() {
//        ArrayList<CountModel> arrayList = new ArrayList<>();
//        try {
//            SQLiteDatabase db = this.getReadableDatabase();
//            /**
//             * get date count
//             */
//            String query = "select count(*) as count, " + MyLocalDB.EVENT_DATE + " from " + MyLocalDB.TABLE_NAME + " group by " + MyLocalDB.EVENT_DATE;
//            Log.e(TAG, "query: " + query);
//
//            Cursor mCountCursor = db.rawQuery(query, null);
//            if (mCountCursor.moveToFirst()) {
//                do {
//                    arrayList.add(new CountModel(
//                            mCountCursor.getInt(mCountCursor.getColumnIndex("count")),
//                            mCountCursor.getString(mCountCursor.getColumnIndex("date"))
//                    ));
//
//                } while (mCountCursor.moveToNext());
//            }
//            mCountCursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return arrayList;
//    }
//
//
//}
