package tel.skylab.skylabtel.apis.service;

import android.content.Context;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tel.skylab.skylabtel.apis.request.GetMsgRequest;
import tel.skylab.skylabtel.apis.response.GetMsgResponseModel;
import tel.skylab.skylabtel.apis.web.ApiService;
import tel.skylab.skylabtel.apis.web.AppConstant;

public class GetMsgService
        extends ApiService<GetMsgService.LoginApi, GetMsgRequest, GetMsgResponseModel> {

    public GetMsgService(Context context) {
        super(context);
    }

    @Override
    protected Class<LoginApi> getAPI() {
        return LoginApi.class;
    }

    @Override
    protected Call<GetMsgResponseModel> onExecute(LoginApi api, GetMsgRequest request) {
        return api.login(request, AppConstant.CONTENT_TYPE, AppConstant.Authorization);
    }

    public interface LoginApi {
        @POST("api/v1/getMessages")
        Call<GetMsgResponseModel> login(
                @Body GetMsgRequest request,
                @Header("Content-Type") String content_type,
                @Header("Authorization") String authorization);
    }
}
