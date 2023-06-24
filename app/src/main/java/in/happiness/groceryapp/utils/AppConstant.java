package in.happiness.groceryapp.utils;

import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.MainData;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.model.UserProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AppConstant {
    public static String versionCode="43";
//    public static final String BASE_URL = "http://test.thehappinessindia.com/api/v1/";
    public static final String BASE_URL = "https://thehappinessindia.com/api/v1/";
//    public static final String IMAGE_URL = "http://test.thehappinessindia.com/storage/app/public/";
    public static final String IMAGE_URL = "https://thehappinessindia.com/storage/app/public/";
    public static String token="tokenKey";
    public static String firstTimeLaunch = "firstTimeLaunch";
    public static String isLoggedIn="isLoggedIn";
    public static String userToken = "userToken";
    public static HashSet<Integer> product_id;
    public static String isNewUser="isNewUser";
    public static String userLat = "userLat";
    public static String userLong = "userLong";
    public static String userAddress = "userAddress";
    public static String userCity= "userCity";
    public static String userLocality= "userLocality";
    public static String userPincode="userPincode";
    public static User user;
    public static UserProfile userProfile;
    public static List<Category> categoryList;
    public static Map<Integer,List<SubCategory>> subCategoryList;
    public static ArrayList<CartItem> cart_item;
    public static String userState="userState";
    public static List<String> imageUrl;
    public static String click_action = "click_action";
    public static String locationAvailable="locationAvailable";
    public static String lastDate="lastDate";
    public static MainData response;
    public static boolean validate = false;
    public static String lastSynchDate="lastSynchDate";
}
