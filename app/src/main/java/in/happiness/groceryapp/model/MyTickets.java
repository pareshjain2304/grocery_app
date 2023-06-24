package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTickets implements Serializable {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private ArrayList<Tickets> data;
    @SerializedName("ticket")
    @Expose
    private Tickets ticket;
    @SerializedName("reply")
    @Expose
    private ArrayList<ReplyTicket> reply;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Tickets> getData() {
        return data;
    }

    public void setData(ArrayList<Tickets> data) {
        this.data = data;
    }

    public Tickets getTicket() {
        return ticket;
    }

    public void setTicket(Tickets ticket) {
        this.ticket = ticket;
    }

    public ArrayList<ReplyTicket> getReply() {
        return reply;
    }

    public void setReply(ArrayList<ReplyTicket> reply) {
        this.reply = reply;
    }
}
