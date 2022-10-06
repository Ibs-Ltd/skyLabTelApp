package tel.skylab.skylabtel.apis.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API_Interface {

    /* @FormUrlEncoded
    @POST("api/common/get_cancel_reasons")
    Call<ServerResponse> get_reasons(@Field("email") String email);*/

    /* @FormUrlEncoded
    @POST("api/booking/booking_cancle")
    Call<ServerResponse> get_Cancellation(@Field("booking_id") String booking_id, @Field("user_id") String user_id,
                                         @Field("car_id") String car_id, @Field("cancellation_reason") String cancellation_reason);*/

    /*@FormUrlEncoded
    @POST("api/booking/booking_cancle")
    Call<ServerResponse> Cancellation(@Body CancelRequest request);

    @POST("api/booking/checkout")
    Call<CheckOutResponse> checkOutApi(@Body CheckoutRequest request);*/
    /*
    @Multipart
    @POST("api/user/change_profile_photo")
    Call<ServerResponseImage> upload_image(@Part MultipartBody.Part image,
                                           @Part("user_id") RequestBody user_id);*/

    /*@Multipart
    @POST("api/document/add_document")
    Call<ServerResponseImage> upload_user_doc(@Part MultipartBody.Part document,
                                              @Part("user_id") RequestBody user_id,
                                              @Part("document_name") RequestBody document_name,
                                              @Part("document_type") RequestBody document_type,
                                              @Part("document_file_key") RequestBody document_file_key,
                                              @Part("rel_type") RequestBody rel_type);*/

    /* @Multipart
    @POST("api/booking/bookings_document_upload")
    Call<ServerResponseImage> upload_host_car_img(@Part MultipartBody.Part document,
                                                  @Part("booking_id") RequestBody booking_id,
                                                  @Part("upload_by") RequestBody upload_by,
                                                  @Part("rel_id") RequestBody rel_id,
                                                  @Part("rel_type") RequestBody rel_type,
                                                  @Part("screen_type") RequestBody screen_type);*/

    @GET("v1/isConnected")
    Call<ServerResponse> getCotegory();
}
