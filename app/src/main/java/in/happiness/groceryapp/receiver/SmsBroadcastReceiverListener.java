package in.happiness.groceryapp.receiver;

import android.content.Intent;

public interface SmsBroadcastReceiverListener{

    void onSuccess(Intent intent);

    void onFailure();


}