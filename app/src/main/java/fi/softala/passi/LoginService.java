package fi.softala.passi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by villeaaltonen on 04/10/16.
 */

public interface LoginService {
    @GET("user/{username}")
    Call<Kayttaja> kayttaja(
            @Path("username") String username
    );

}
