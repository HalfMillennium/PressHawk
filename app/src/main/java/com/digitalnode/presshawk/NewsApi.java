package com.digitalnode.presshawk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {
    @GET("v2/top-headlines/")
    Call<Articles> getArticles();
}
