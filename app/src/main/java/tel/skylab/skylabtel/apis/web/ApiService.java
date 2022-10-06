package tel.skylab.skylabtel.apis.web;

import static android.content.Context.MODE_PRIVATE;
import static tel.skylab.skylabtel.utils.Constants.skyLabProxyHost;
import static tel.skylab.skylabtel.utils.Constants.skyLabProxyPort;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tel.skylab.skylabtel.utils.Constants;

/** {@link ApiService} */
public abstract class ApiService<U, T extends ApiRequest, L extends ApiResponse> {

    private static final int TIME_OUT = 60;

    /** Use to build Gson object for serialization and deserialization of pojo */
    private static Gson sGson;

    private Context mContext;

    public ApiService(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public final void execute(final T request, final ApiCallback<L> callback) {
        if (!isNetworkAvailable()) {
            callback.onComplete();
            //            Toast.makeText(mContext, R.string.no_internet_available,
            // Toast.LENGTH_SHORT).show();
            return;
        }
        if (callback == null) {
            //            throw new ApiException(ApiException.Kind.NULL, "Callback may not be
            // null");
        } else if (getAPI() == null) {
            callback.onComplete();
            callback.onFailure(
                    new ApiException(ApiException.Kind.NULL, "API Interface may not be null"));
        } else if (request == null) {
            callback.onComplete();
            callback.onFailure(new ApiException(ApiException.Kind.NULL, "Request may not be null"));
            callback.onFailure(new ApiException(ApiException.Kind.NULL, "Request may not be null"));
        } else if (!request.isValid()) {
            callback.onComplete();
            callback.onFailure(
                    new ApiException(ApiException.Kind.INVALID_REQUEST, "Invalid request"));
        } else {
            String baseUrl = request.getBaseUrl();
            if (TextUtils.isEmpty(baseUrl)) {
                callback.onComplete();
                callback.onFailure(
                        new ApiException(ApiException.Kind.NULL, "Base Url may not be null"));
                return;
            }
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(getContext().getCacheDir(), cacheSize);
            /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .proxy(
                                    new Proxy(
                                            Proxy.Type.HTTP,
                                            new InetSocketAddress(
                                                    skyLabProxyHost, skyLabProxyPort)))
                            .proxyAuthenticator(proxyAuthenticator)
                            .build();

            /*  OkHttpClient client =
            new OkHttpClient.Builder()
                    .proxy(
                            new Proxy(
                                    Proxy.Type.HTTP,
                                    new InetSocketAddress(
                                            skyLabProxyHost, skyLabProxyPort)))
                    .proxyAuthenticator(proxyAuthenticator)
                    .build();*/

            /*// For Set Proxy
            java.net.Proxy proxy =
                    new Proxy(
                            Proxy.Type.HTTP, new InetSocketAddress("http://35.176.111.241/", 443));
            OkHttpClient okHttpClient1 = new OkHttpClient.Builder().proxy(proxy).build();*/

            // TODO Set okHttp Client for Https Request
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            final Retrofit retrofit =
                    new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(okHttpClient)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create(getGson()))
                            .build();

            // \ Log.d("jsondata",getGson().toString());
            new Thread(
                            new Runnable() {
                                @Override
                                public void run() {

                                    Call<L> call =
                                            ApiService.this.onExecute(
                                                    retrofit.create(getAPI()), request);
                                    call.enqueue(callback);
                                }
                            })
                    .start();
        }
    }

    private Gson getGson() {
        if (sGson != null) {
            return sGson;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        return sGson = gsonBuilder.create();
    }

    public int getConnectionTimeoutInSeconds() {
        return TIME_OUT;
    }

    public int getReadTimeoutInSeconds() {
        return TIME_OUT;
    }

    protected abstract Class<U> getAPI();

    /**
     * @param api to pass data in request
     * @param request to send on server
     */
    protected abstract Call<L> onExecute(U api, T request);

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    Authenticator proxyAuthenticator =
            new Authenticator() {
                @Override
                public Request authenticate(@Nullable Route route, okhttp3.Response response)
                        throws IOException {
                    SharedPreferences preferences =
                            getContext()
                                    .getSharedPreferences(
                                            Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
                    String user = preferences.getString(Constants.skyLabUsername, "");
                    String pass = preferences.getString(Constants.skyLabPassword, "");
                    String credential = Credentials.basic(user, pass);
                    return response.request()
                            .newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            };
}
