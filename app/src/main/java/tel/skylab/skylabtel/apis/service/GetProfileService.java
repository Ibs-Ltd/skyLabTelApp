package tel.skylab.skylabtel.apis.service;

import android.content.Context;
import android.provider.Settings;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tel.skylab.skylabtel.apis.request.GetProfileRequest;
import tel.skylab.skylabtel.apis.response.GetProfileResponseModel;
import tel.skylab.skylabtel.apis.web.ApiService;
import tel.skylab.skylabtel.apis.web.AppConstant;

public class GetProfileService
        extends ApiService<
                GetProfileService.GetProfileApi, GetProfileRequest, GetProfileResponseModel> {

    public GetProfileService(Context context) {
        super(context);
    }

    @Override
    protected Class<GetProfileApi> getAPI() {
        return GetProfileApi.class;
    }

    @Override
    protected Call<GetProfileResponseModel> onExecute(
            GetProfileApi api, GetProfileRequest request) {
        return api.login(
                request,
                AppConstant.CONTENT_TYPE,
                Settings.Secure.getString(
                        getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public interface GetProfileApi {
        @POST("api/v1/getProfile")
        Call<GetProfileResponseModel> login(
                @Body GetProfileRequest request,
                @Header("Content-Type") String content_type,
                @Header("token") String token);
    }
}
