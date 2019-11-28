package com.yxst.mes.net;


import com.yxst.mes.database.model.Place;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RestService {
    @GET("webapi/api/v1/inspection/GetDevicePlace")
    Call<List<Place>> getplace();
    @GET
    Call<String> get(@Url String url);
    @POST
    @FormUrlEncoded
    Call<String> post(@Url String url, @FieldMap Map<String ,Object> params);
}
