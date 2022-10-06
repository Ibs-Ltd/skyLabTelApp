package tel.skylab.skylabtel.apis.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ServerResponseImage implements Serializable {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

    @SerializedName("response_data")
    public Data response_data = null;

    public class Data {
        @SerializedName("picture")
        public String picture;

        @SerializedName("document_file_name")
        public String document_file_name;

        @SerializedName("file_name")
        public String file_name;

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getDocument_file_name() {
            return document_file_name;
        }

        public void setDocument_file_name(String document_file_name) {
            this.document_file_name = document_file_name;
        }

        public String getFile_name() {
            return file_name;
        }

        public void setFile_name(String file_name) {
            this.file_name = file_name;
        }
    }
}
