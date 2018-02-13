package com.furyviewer.service.TheMovieDB.Repository;

import com.furyviewer.service.dto.TheMovieDB.Company.CompleteCompanyTmdbDTO;
import com.furyviewer.service.dto.TheMovieDB.Company.SimpleCompanyTmdbDTO;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Repositorio encargado de conectar con la api de TheMovieDB para recuperar la información de Company.
 * @author IFriedkin
 */
public interface CompanyTmdbDTORepository {
    /**
     * Devuelve la información simple de una Company proporcionada por la API.
     * @param apikey String | Key requerida por la api para poder hacer peticiones.
     * @param companyName String | Nombre de la Company que se quiere buscar.
     * @return Call<SimpleCompanyTmdbDTO> | Contiene toda la información simple de la Company devuelta por la api.
     */
    @GET("/3/search/company")
    Call<SimpleCompanyTmdbDTO> getSimpleCompany(@Query("api_key") String apikey, @Query("query") String companyName);

    /**
     * Devuelve la información completa de una Company proporcionada por la API.
     * @param id int | id interno de la API para reconocer la Company.
     * @param apikey String | Key requerida por la api para poder hacer peticiones.
     * @return Call<CompleteCompanyTmdbDTO> | Contiene toda la información completa de la Company devuelta por la api.
     */
    @GET("/3/company/{id}")
    Call<CompleteCompanyTmdbDTO> getCompleteCompany(@Path("id") int id, @Query("api_key") String apikey);

    public static String url = " https://api.themoviedb.org/";
    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
}
