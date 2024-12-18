package com.arin.titik_suara;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("post_pengaduan.php")
    Call<ApiResponse> uploadPengaduan(
            @Part("deskripsi") RequestBody deskripsi,
            @Part("kategori") RequestBody kategori,
            @Part MultipartBody.Part gambar
    );
}
