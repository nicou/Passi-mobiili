package fi.softala.passi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by villeaaltonen on 04/10/16.
 */

public interface KayttajaService {
    @POST("answer")
    Call<Vastaus> tallenna(@Body Vastaus vastaus);
}
