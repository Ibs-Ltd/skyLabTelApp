package tel.skylab.skylabtel.apis.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponseOBJ implements Serializable {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    public Data data = null;

    public class Data {
        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("username")
        public String username;

        @SerializedName("email")
        public String email;

        @SerializedName("image")
        public String image;

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("address_one")
        public String address_one;

        @SerializedName("address_two")
        public String address_two;

        @SerializedName("status")
        public String status;

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
