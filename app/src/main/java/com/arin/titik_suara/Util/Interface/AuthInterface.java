package com.arin.titik_suara.Util.Interface;


import com.arin.titik_suara.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthInterface {
    // login
    @FormUrlEncoded
    @POST("auth/login")
    Call<UserModel> login(
            @Field("username") String username,
            @Field("password") String password
    );

    // get data user
    @GET("user/user/getUserById")
    Call<List<UserModel>> getUserById(
            @Query("user_id") String userId
    );
}
