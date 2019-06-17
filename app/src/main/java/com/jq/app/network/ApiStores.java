package com.jq.app.network;

import com.google.gson.JsonObject;
import com.jq.app.ui.exercise.pojo.ExerciseResponse;
import com.jq.app.ui.exercise.pojo.SaveWorkout;
import com.jq.app.ui.noteList.Response;
import com.jq.app.ui.time_calendar.apiclient.Event;
import com.jq.app.ui.time_calendar.apiclient.Example;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiStores {
    @POST("add_notes.jsp?")
    Observable<JsonObject> addNotes(@QueryMap Map<String, String> map);

    @POST("delete_notes.jsp?")
    Observable<JsonObject> deleteNotes(@QueryMap Map<String, String> map);

    @POST("user_activity.jsp?")
    Observable<JsonObject> saveWorkout(@QueryMap Map<String, String> map);

    @POST("search_planner.jsp?")
    Observable<JsonObject> searchPlanner(@QueryMap Map<String, String> map);

    @POST("edit_notes.jsp?")
    Observable<JsonObject> editNotes(@QueryMap Map<String, String> map);

    @POST("get_notes.jsp?")
    Observable<Response> getNotes(@QueryMap Map<String, String> map);

    @POST("planner_workout.jsp")
    Observable<ExerciseResponse> getPlanner_workout(@QueryMap Map<String, String> map);

    @POST("pre_planner_workout.jsp")
    Observable<ExerciseResponse> prePlannerWorkout(@QueryMap Map<String, String> map);

    @POST("cal_event.jsp")
    Observable<Example> event(@QueryMap Map<String, String> map);
//    Observable<JsonObject> event(@QueryMap Map<String, String> map);

    @POST("add_cal_event.jsp")
    Observable<JsonObject> addCalendarEvent(@QueryMap Map<String, String> map);

    @POST("add_cal_event.jsp")
    Observable<List<Event>> addEvent(@QueryMap Map<String, String> map);

//    @POST("cal_event.jsp")
//    Observable<List<Event>> event(@QueryMap Map<String, String> map);

    @POST("whiteboard_save_planner.jsp")
    @Headers({"Content-Type: application/json"})
    Observable<JsonObject> whiteboard_save(@Body SaveWorkout saveWorkout);
}
