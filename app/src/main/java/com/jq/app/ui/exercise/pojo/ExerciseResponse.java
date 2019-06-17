package com.jq.app.ui.exercise.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class ExerciseResponse {
    ArrayList<Data> data = new ArrayList();
    String message;
    String planner_id;
    String status;
    String user_id;

    public class Data implements Serializable {
        String body_part_code;
        int cat_seq;
        String[] header;
        String id;
        ArrayList<Video> video = new ArrayList();
        int work_out_code = 0;
        String workout_name;

        public class Video implements Serializable {
            String description;
            String[] header_values;
            String thumbnail_url;
            String title;
            String url;
            int video_id;
            int work_out_seq;

            public int getWork_out_seq() {
                return this.work_out_seq;
            }

            public void setWork_out_seq(int work_out_seq) {
                this.work_out_seq = work_out_seq;
            }

            public int getVideo_id() {
                return this.video_id;
            }

            public void setVideo_id(int video_id) {
                this.video_id = video_id;
            }

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDescription() {
                return this.description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumbnail_url() {
                return this.thumbnail_url;
            }

            public void setThumbnail_url(String thumbnail_url) {
                this.thumbnail_url = thumbnail_url;
            }

            public String[] getHeader_values() {
                return this.header_values;
            }

            public void setHeader_values(String[] header_values) {
                this.header_values = header_values;
            }
        }

        public int getCat_seq() {
            return this.cat_seq;
        }

        public void setCat_seq(int cat_seq) {
            this.cat_seq = cat_seq;
        }

        public ArrayList<Video> getVideo() {
            return this.video;
        }

        public void setVideo(ArrayList<Video> video) {
            this.video = video;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBody_part_code() {
            return this.body_part_code;
        }

        public void setBody_part_code(String body_part_code) {
            this.body_part_code = body_part_code;
        }

        public int getWork_out_code() {
            return this.work_out_code;
        }

        public void setWork_out_code(int work_out_code) {
            this.work_out_code = work_out_code;
        }

        public String getWorkout_name() {
            return this.workout_name;
        }

        public void setWorkout_name(String workout_name) {
            this.workout_name = workout_name;
        }

        public String[] getHeader() {
            return this.header;
        }

        public void setHeader(String[] header) {
            this.header = header;
        }
    }

    class SortByWorkoutNo implements Comparator<Data.Video> {
        SortByWorkoutNo() {
        }

        public int compare(Data.Video a, Data.Video b) {
            return a.getWork_out_seq() - b.getWork_out_seq();
        }
    }

    public String getPlanner_id() {
        return this.planner_id;
    }

    public void setPlanner_id(String planner_id) {
        this.planner_id = planner_id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Data> getData() {
        return this.data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
