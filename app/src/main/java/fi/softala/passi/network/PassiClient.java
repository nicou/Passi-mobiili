package fi.softala.passi.network;

import java.util.List;

import fi.softala.passi.models.Answersheet;
import fi.softala.passi.models.Kayttaja;
import fi.softala.passi.models.Vastaus;
import fi.softala.passi.models.Worksheet;
import okhttp3.RequestBody;
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

    @POST("upload/{filename_without_extension}")
    Call<ResponseBody> tallennaKuva(
            @Path("filename_without_extension") String nimi,
            @Body RequestBody byteKuva
    );

    @DELETE("answer/1/{userID}")
    Call<ResponseBody> poistaVastaus(@Path("userID") Integer userID);

    @GET("worksheet/{group_id}")
    Call<List<Worksheet>> haeTehtavakortit(@Path("group_id") Integer groupID);

    @GET("answer/{worksheet_id}/{group_id}/{user_id}")
    Call<Answersheet> haeOpettajanKommentit(@Path("worksheet_id") Integer worksheetId,
                                            @Path("group_id") Integer groupId,
                                            @Path("user_id") Integer userId);

}
