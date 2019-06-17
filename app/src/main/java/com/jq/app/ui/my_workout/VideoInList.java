package com.jq.app.ui.my_workout;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hasnain on 23-Feb-18.
 */

public class VideoInList implements Parcelable {
    private String id;
    private String url;
    private String body_part_code;
    private String work_out_code;
    private String thumbnail_url;

    public VideoInList(String id, String url, String body_part_code, String work_out_code, String thumbnail_url) {
        this.id = id;
        this.url = url;
        this.body_part_code = body_part_code;
        this.work_out_code = work_out_code;
        this.thumbnail_url = thumbnail_url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getBody_part_code() {
        return body_part_code;
    }

    public String getWork_out_code() {
        return work_out_code;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private VideoInList(Parcel in) {
        id = in.readString();
        url = in.readString();
        body_part_code = in.readString();
        work_out_code = in.readString();
        thumbnail_url = in.readString();


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(body_part_code);
        dest.writeString(work_out_code);
        dest.writeString(thumbnail_url);


    }


    public static final Parcelable.Creator<VideoInList> CREATOR = new Parcelable.Creator<VideoInList>() {
        public VideoInList createFromParcel(Parcel in) {
            return new VideoInList(in);
        }

        public VideoInList[] newArray(int size) {
            return new VideoInList[size];

        }
    };
}
