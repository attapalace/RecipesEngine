package com.examples.apps.atta.recipesengine.Retrofit;

import com.examples.apps.atta.recipesengine.Model.JsonResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RecipeInterface  {

    @GET("search")
    Call<JsonResponse> getResponse(@Query("q") String s, @Query("app_id") String app_id,
                                   @Query("app_key") String app_key ,@Query("diet") String balanced);

    @GET("search")
    Call<JsonResponse> getSearchResponse(@Query("q") String searchParam , @Query("app_id") String app_id,
                                         @Query("app_key") String app_key,
                                         @QueryMap Map<String,String> options);
}
