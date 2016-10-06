package fi.softala.passi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by villeaaltonen on 04/10/16.
 */

public interface PassiClient {

    @GET("user/{userID}")
    Call<Kayttaja> haeKayttaja(
            @Path("userID") String username
    );

    @POST("answer/")
    Call<ResponseBody> tallennaVastaus(@Body Vastaus vastaus);

    @DELETE("answer/1/{userID}")
    Call<ResponseBody> poistaVastaus(@Path("userID") Integer userID);

    @GET("worksheet/{group_id}")
    Call<List<Worksheet>> haeTehtavakortit(@Path("group_id") Integer groupID);
}
