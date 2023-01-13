package com.zcyi.rorschach.Mapper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseRequest {
    @POST("api")
    @FormUrlEncoded
    Call<ResponseBody> getTranslation(
            @Field("q") String q,
            @Field("from") String from,
            @Field("to") String to,
            @Field("salt") String salt,
            @Field("sign") String sign,
            @Field("curtime") String curtime,
            @Field("signType") String signType,
            @Field("appKey") String appKey);
}