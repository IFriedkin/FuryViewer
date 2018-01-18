package com.furyviewer.service.OpenMovieDatabase;

import com.furyviewer.service.dto.OpenMovieDatabase.MovieOmdbDTO;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieOmdbDTORepository {



@GET("/?type=movie")
Call<MovieOmdbDTO> getMovie(@Query("apikey") String apikey, @Query("t") String title);

@GET("/?type=movie")
Call<MovieOmdbDTO> searchMovie(@Query("apikey") String apikey, @Query("s") String title);





    public static String url = "http://www.omdbapi.com/";
    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
}