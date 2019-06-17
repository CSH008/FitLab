package com.jq.app.ui.PlannerNew.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hasnain on 22-Feb-18.
 */

public class Planner implements Parcelable{
    private String workout_name;
    private String work_out_code;
    private String body_part_code;
    private String comment;
    private String url;
    private String thumbnail_url;
    private String title;
    private String description;
    private String id;
    private String is_like;

    public Planner(String workout_name, String work_out_code, String body_part_code, String comment, String url, String thumbnail_url, String title, String description, String id, String is_like) {
        this.workout_name = workout_name;
        this.work_out_code = work_out_code;
        this.body_part_code = body_part_code;
        this.comment = comment;
        this.url = url;
        this.thumbnail_url = thumbnail_url;
        this.title = title;
        this.description = description;
        this.id = id;
        this.is_like = is_like;
    }

    public String getWorkout_name() {
        return workout_name;
    }

    public String getWork_out_code() {
        return work_out_code;
    }

    public String getBody_part_code() {
        return body_part_code;
    }

    public String getComment() {
        return comment;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getIs_like() {
        return is_like;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    private Planner(Parcel in) {
        workout_name = in.readString();
        work_out_code = in.readString();
        body_part_code = in.readString();
        comment = in.readString();
        url = in.readString();
        thumbnail_url = in.readString();
        title = in.readString();
        description = in.readString();
        id = in.readString();
        is_like = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workout_name);
        dest.writeString(work_out_code);
        dest.writeString(body_part_code);
        dest.writeString(comment);
        dest.writeString(url);
        dest.writeString(thumbnail_url);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(id);
        dest.writeString(is_like);

    }


    public static final Parcelable.Creator<Planner> CREATOR = new Parcelable.Creator<Planner>() {
        public Planner createFromParcel(Parcel in) {
            return new Planner(in);
        }

        public Planner[] newArray(int size) {
            return new Planner[size];

        }
    };
}
