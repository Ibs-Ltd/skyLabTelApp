package tel.skylab.skylabtel.apis.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponse implements Serializable {

    @SerializedName("status")
    String status;

    @SerializedName("code")
    int codes;

    @SerializedName("error")
    boolean error;

    @SerializedName("message")
    String message;

    @SerializedName("response_data")
    public ArrayList<Data> response_data = null;

    public class Data {
        @SerializedName("id")
        public String id;

        @SerializedName("reason_type")
        public String reason_type;

        @SerializedName("reason")
        public String reason;

        @SerializedName("status")
        public String status;

        @SerializedName("date_added")
        public String date_added;

        @SerializedName("date_modified")
        public String date_modified;

        @SerializedName("slots_details")
        public ArrayList<Slots> slotsList;

        public class Slots implements Serializable {
            @SerializedName("id")
            public String slotId;

            @SerializedName("slots")
            public String slots;

            @SerializedName("price")
            public String price;

            @SerializedName("time")
            public String venue_time;
        }
    }
}
