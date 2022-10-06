package tel.skylab.skylabtel.apis.service;

import android.content.Context;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import tel.skylab.skylabtel.apis.request.SendMsgRequest;
import tel.skylab.skylabtel.apis.response.SendMsgResponseModel;
import tel.skylab.skylabtel.apis.web.ApiService;
import tel.skylab.skylabtel.apis.web.AppConstant;

public class SendMsgService
        extends ApiService<SendMsgService.LoginApi, SendMsgRequest, SendMsgResponseModel> {

    public SendMsgService(Context context) {
        super(context);
    }

    @Override
    protected Class<LoginApi> getAPI() {
        return LoginApi.class;
    }

    @Override
    protected Call<SendMsgResponseModel> onExecute(LoginApi api, SendMsgRequest request) {
        return api.login(request, AppConstant.CONTENT_TYPE);
    }

    public interface LoginApi {
        @POST("api/v1/sendMessage")
        Call<SendMsgResponseModel> login(
                @Body SendMsgRequest request, @Header("Content-Type") String content_type);
    }
}
