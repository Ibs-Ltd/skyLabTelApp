package tel.skylab.skylabtel.apis.service;

import android.content.Context;
import android.provider.Settings;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tel.skylab.skylabtel.apis.request.LoginRequest;
import tel.skylab.skylabtel.apis.response.LoginResponseModel;
import tel.skylab.skylabtel.apis.web.ApiService;
import tel.skylab.skylabtel.apis.web.AppConstant;

public class LoginService
        extends ApiService<LoginService.LoginApi, LoginRequest, LoginResponseModel> {

    public LoginService(Context context) {
        super(context);
    }

    @Override
    protected Class<LoginApi> getAPI() {
        return LoginApi.class;
    }

    @Override
    protected Call<LoginResponseModel> onExecute(LoginApi api, LoginRequest request) {
        return api.login(
                request,
                AppConstant.CONTENT_TYPE,
                Settings.Secure.getString(
                        getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    public interface LoginApi {
        @POST("api/v1/login")
        Call<LoginResponseModel> login(
                @Body LoginRequest request,
                @Header("Content-Type") String content_type,
                @Header("token") String token);
    }
}
