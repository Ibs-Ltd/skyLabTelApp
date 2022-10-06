package tel.skylab.skylabtel.apis.web;

import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.json.JSONException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tel.skylab.skylabtel.R;

/** {@link ApiCallback} */
public abstract class ApiCallback<T extends ApiResponse> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        onComplete();
        int status = response.code();

        // check for error body
        if (!response.isSuccessful()) {
            onFailure(getAPIException(response, null));
            return;
        }

        T apiResponse = response.body();

        if (apiResponse == null) {
            onFailure(new ApiException(ApiException.Kind.NULL, status, "IResponse can't be null"));
            return;
        }

        String apiStatus = apiResponse.getMessage();
        //        if (TextUtils.isEmpty(apiStatus) /*&& apiResponse.getResponseCode() != 200 &&
        // apiResponse.getResponseCode() != 201 apiStatus/*&&
        // apiStatus.equalsIgnoreCase("failure")*/) {
        //            onFailure(new ApiException(ApiException.Kind.INVALID_RESPONSE,
        // apiResponse.getMessage()));
        //            return;
        //        }

        if (!apiResponse.isValid()) {
            ApiException validationError =
                    new ApiException(
                            ApiException.Kind.INVALID_RESPONSE,
                            status,
                            "Validation error for the given scenario");
            onFailure(validationError);
            //            return;
        }
        onSuccess(call, apiResponse);
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable cause) {
        onComplete();
        ApiException apiException = getAPIException(null, cause);
        onFailure(apiException);
    }

    private ApiException getAPIException(Response<T> response, Throwable cause) {
        if (cause != null) {
            if (cause instanceof SocketTimeoutException) {
                return new ApiException(
                        ApiException.Kind.SOCKET_TIMEOUT,
                        "Internet Connection is slow, Try Again.",
                        cause);
            } else if (cause instanceof JSONException) {
                return new ApiException(ApiException.Kind.CONVERSION, "Conversion Error", cause);
            } else if (cause instanceof IOException) {
                return new ApiException(
                        ApiException.Kind.NETWORK, String.valueOf(R.string.network_error), cause);
            } else if (cause instanceof ApiException) {
                return (ApiException) cause;
            } else {
                return new ApiException(ApiException.Kind.UNEXPECTED, "Unknown", cause);
            }
        } else if (response.code() == 400) {
            Gson gson = new GsonBuilder().create();
            ApiResponse mError;
            try {
                mError = gson.fromJson(response.errorBody().string(), ApiResponse.class);
                return new ApiException(ApiException.Kind.HTTP, mError.getMessage(), cause);
            } catch (IOException e) {
                // handle failure to read error
            }
        }
        return new ApiException(ApiException.Kind.UNEXPECTED, "Unknown Error");
    }

    public abstract void onSuccess(Call<T> call, T response);

    public abstract void onComplete();

    public abstract void onFailure(ApiException e);
}
