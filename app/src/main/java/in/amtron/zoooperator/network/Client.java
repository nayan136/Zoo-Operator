package in.amtron.zoooperator.network;


import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Client {

    @Headers({"Accept: application/json"})
    @GET("master_data.php")
    Call<JsonObject> getMasterData(
            @Query("API_KEY") String apiKey,
            @Query("ip") String ip,
            @Query("os") String os,
            @Query("imei") String imei

    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("ticketCounterLogin.php")
    Call<JsonObject> login(
            @Field("API_KEY") String apiKey,
            @Field("ip") String ip,
            @Field("os") String os,
            @Field("imei") String imei,
            @Field("mobileNo") String mobile,
            @Field("password") String password
    );

    @Headers({"Accept: application/json"})
    @GET("server_status.php")
    Call<JsonObject> getServerStatus(
            @Query("API_KEY") String apiKey,
            @Query("ip") String ip,
            @Query("os") String os,
            @Query("imei") String imei
    );

    @Headers({"Content-Type: text/plain"})
//    @FormUrlEncoded
    @POST("posTicketBooking.php")
    Call<JsonObject> syncData(
            @Body RequestBody body
//            @Field("API_KEY") String apiKey,
//            @Field("ip") String ip,
//            @Field("os") String os,
//            @Field("imei") String imei,
//            @Field("zooUserId") String zooUserId,
//            @Field("data") String data
    );
}
