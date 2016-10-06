package fi.softala.passi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    @POST("upload/{filename_without_extension}")
    Call<ResponseBody> tallennaKuva(
            @Header("Content-Type") String tyyppi,
            @Path("filename_without_extension") String nimi,
            @Body byte[] byteKuva
    );

    @DELETE("answer/1/{userID}")
    Call<ResponseBody> poistaVastaus(@Path("userID") Integer userID);


}
