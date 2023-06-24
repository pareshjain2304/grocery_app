package in.happiness.groceryapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppSharedPreferences {

    public static final String MyPREFERENCES = "GROCERYUSER";
    public static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    ArrayList<String> arrPackage;

    public AppSharedPreferences(Context context) {

        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        arrPackage = new ArrayList<>();
    }

    public void doSaveArrayToSharedPreferences(String key, Set<String> arrayList) {
        Set<String> set = new HashSet<String>();
        set.addAll(arrayList);
        editor.putStringSet(key, set);
        editor.apply();
        editor.commit();
    }

    public Set<String> doGetArrayToSharedPreferences(String key) {
        Set<String> set = sharedPreferences.getStringSet(key, null);
        if (set.size()>0) {
            arrPackage.addAll(set);
        }
        return set;
    }

    public void doSaveToSharedPreferences(String key, String value) {
        editor.putString(key, value);
        editor.apply();
        editor.commit();

    }

    public String doGetFromSharedPreferences(String key) {
        String value = "";
        value = sharedPreferences.getString(key, "");
        return value;
    }


}
