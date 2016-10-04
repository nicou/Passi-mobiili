package fi.softala.passi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by villeaaltonen on 04/10/16.
 */

public interface PassiClient {

    @GET("user/{username}")
    Call<Kayttaja> haeKayttaja(
            @Path("username") String username
    );

    @POST("answer")
    Call<Vastaus> tallennaVastaus(@Body Vastaus vastaus);


}
