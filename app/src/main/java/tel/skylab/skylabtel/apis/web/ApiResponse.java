package tel.skylab.skylabtel.apis.web;

import android.os.Parcel;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

/** {@link ApiResponse} */
public class ApiResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("code")
    private int codes;

    @SerializedName("error")
    private boolean error;

    public boolean isValid() {
        return true;
    }

    protected void writeMap(Parcel out, Map<String, String> map) {
        if (map == null) {
            return;
        }
        out.writeInt(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            out.writeString(entry.getKey());
            out.writeString(entry.getValue());
        }
    }

    protected void readMap(Parcel in, @NonNull Map<String, String> map) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            map.put(key, value);
        }
    }

    public String getMessage() {
        return message;
    }

    public int getCodes() {
        return codes;
    }

    public boolean isError() {
        return error;
    }
}
