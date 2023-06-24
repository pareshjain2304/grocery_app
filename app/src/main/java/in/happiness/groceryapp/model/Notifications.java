package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Notifications implements Serializable {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<NotificationBody> data;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<NotificationBody> getData() {
        return data;
    }

    public void setData(ArrayList<NotificationBody> data) {
        this.data = data;
    }
}
