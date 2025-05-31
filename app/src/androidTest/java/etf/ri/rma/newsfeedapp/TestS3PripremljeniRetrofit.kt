package etf.ri.rma.newsfeedapp

import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.data.network.api.ImagaAPIService
import etf.ri.rma.newsfeedapp.data.network.api.TheNewsAPIService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class TestS3PripremljenRetrofit {
    fun getNewsDAOwithBaseURL(baseURL: String, httpClient: OkHttpClient): NewsDAO {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val newsApiService = retrofit.create(TheNewsAPIService::class.java)
        return NewsDAO(newsApiService)
    }

    fun getImaggaDAOwithBaseURL(baseURL: String, httpClient: OkHttpClient): ImagaDAO {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val imaggaApiService = retrofit.create(ImagaAPIService::class.java)
        return ImagaDAO(imaggaApiService)
    }
}