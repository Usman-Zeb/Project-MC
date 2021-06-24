package com.example.project_mc.models;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization: key=" + "AAAAy7Kdcfk:APA91bH_BBmv0NF1HvkwqFsuZipjBwWZgyAFHI44_yHDgUjWwerERa73km2raaZXytMS8vjjlkoCpmrag7GQpYwVmmGaDrVdOrrzHK8ndAn31yJefxRTvKMn8TqbsqCSS0rlWMzFkD3C", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RootModel root);

}
