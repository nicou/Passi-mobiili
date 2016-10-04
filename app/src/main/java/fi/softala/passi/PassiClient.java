package fi.softala.passi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by villeaaltonen on 04/10/16.
 */

public interface PassiClient {

    @GET("/user/{username}")
    Call<Kayttaja> kayttaja(
            @Path("username") String username
    );


}
