package in.happiness.groceryapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.happiness.groceryapp.model.CartItem;
import in.happiness.groceryapp.model.Category;
import in.happiness.groceryapp.model.ShopProduct;
import in.happiness.groceryapp.model.Stores;
import in.happiness.groceryapp.model.SubCategory;
import in.happiness.groceryapp.model.User;
import in.happiness.groceryapp.model.Variant;

import java.util.ArrayList;
import java.util.List;

public class AppDBHelper  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "happiness_india_user";

    private static final String TABLE_DATE_SYS = "date_sys";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_SUB_CATEGORY = "sub_category";
    private static final String TABLE_CHILD_CATEGORY = "child_category";
    private static final String TABLE_BRAND = "brands";
    private static final String TABLE_BANNER = "banner";
    private static final String TABLE_NEARBY_SHOP = "nearby_shop";
    private static final String TABLE_USER = "user";
    private static final String TABLE_PRODUCT = "product";
    private static final String TABLE_SHOP_PRODUCT = "shop_product";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_VARIANT = "variant";

    //sys_date table
    private static final String KEY_DATE_ID = "id";
    private static final String KEY_DATE_DATE = "date";


    //category table
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_CATEGORY_IMAGE = "category_image";
    private static final String KEY_CATEGORY_STATUS = "status";

    //sub category table
    private static final String KEY_SUBCATEGORY_ID = "subcategory_id";
    private static final String KEY_SUBCATEGORY_NAME = "subcategory_name";
    private static final String KEY_SUBCATEGORY_IMAGE = "subcategory_image";
    private static final String KEY_SUBCATEGORY_STATUS = "status";
    private static final String KEY_SUBCATEGORY_COMMISION = "subcategory_commision";
    private static final String KEY_SUBCATEGORY_CATID = "category_id";

    //child category table
    private static final String KEY_CHILDCATEGORY_ID = "childcategory_id";
    private static final String KEY_CHILDCATEGORY_NAME = "subcategory_name";
    private static final String KEY_CHILDCATEGORY_IMAGE = "subcategory_image";
    private static final String KEY_CHILDCATEGORY_STATUS = "status";
    private static final String KEY_CHILDCATEGORY_SUBCATID = "subcategory_id";

    //brands table
    private static final String KEY_BRAND_ID = "brand_id";
    private static final String KEY_BRAND_NAME = "brand_name";
    private static final String KEY_BRAND_IMAGE = "brand_image";
    private static final String KEY_BRAND_STATUS = "status";

    //banner table
    private static final String KEY_BANNER_IMAGE = "banner_image";

    //shop table
    private static final String KEY_NEARBY_SHOP_nearby_shop_id = "nearby_shop_id";
    private static final String KEY_NEARBY_SHOP_id = "id";
    private static final String KEY_NEARBY_SHOP_name = "name";
    private static final String KEY_NEARBY_SHOP_email = "email";
    private static final String KEY_NEARBY_SHOP_mobile = "mobile";
    private static final String KEY_NEARBY_SHOP_image = "image";
    private static final String KEY_NEARBY_SHOP_shop_name = "shop_name";
    private static final String KEY_NEARBY_SHOP_shop_address = "shop_address";
    private static final String KEY_NEARBY_SHOP_city_id = "city_id";
    private static final String KEY_NEARBY_SHOP_area_id = "area_id";
    private static final String KEY_NEARBY_SHOP_pincode = "pincode";
    private static final String KEY_NEARBY_SHOP_whatsapp = "whatsapp";
    private static final String KEY_NEARBY_SHOP_primary_mobile = "primary_mobile";
    private static final String KEY_NEARBY_SHOP_shop_image = "shop_image";
    private static final String KEY_NEARBY_SHOP_latitude = "latitude";
    private static final String KEY_NEARBY_SHOP_longitude = "longitude";
    private static final String KEY_NEARBY_SHOP_local_latitude = "local_latitude";
    private static final String KEY_NEARBY_SHOP_local_longitude = "local_longitude";
    private static final String KEY_NEARBY_SHOP_shop_approved = "shop_approved";
    private static final String KEY_NEARBY_SHOP_shop_online = "shop_online";
    private static final String KEY_NEARBY_SHOP_created_at = "created_at";
    private static final String KEY_NEARBY_SHOP_updated_at = "updated_at";
    private static final String KEY_NEARBY_SHOP_distance = "distance";

    //user table
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_USER_IMAGE = "user_image";
    private static final String KEY_USER_LATITUDE = "user_latitude";
    private static final String KEY_USER_LONGITUDE = "user_longitude";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_COUPONCODE = "user_ccode";
    private static final String KEY_USER_REFERALCODE = "user_rcode";
    private static final String KEY_userAddress = "userAddress";
    private static final String KEY_userCity= "userCity";
    private static final String KEY_userLocality= "userLocality";
    private static final String KEY_userPincode="userPincode";
    private static final String KEY_locationAvailable="locationAvailable";

    //product table
    private static final String KEY_PRODUCT_id = "product_id";
    private static final String KEY_PRODUCT_user_id = "user_id";
    private static final String KEY_PRODUCT_category_id = "category_id";
    private static final String KEY_PRODUCT_subcategory_id = "subcategory_id";
    private static final String KEY_PRODUCT_in_stock = "in_stock";
    private static final String KEY_PRODUCT_created_at = "created_at";
    private static final String KEY_PRODUCT_updated_at = "updated_at";
    private static final String KEY_PRODUCT_product_name = "product_name";
    private static final String KEY_PRODUCT_description = "description";
    private static final String KEY_PRODUCT_vendor_description = "vendor_description";
    private static final String KEY_PRODUCT_product_image = "product_image";
    private static final String KEY_PRODUCT_is_available = "is_available";

    //shop product table
    private static final String KEY_SHOP_PRODUCT_id = "id";
    private static final String KEY_SHOP_PRODUCT_user_id = "user_id";
    private static final String KEY_SHOP_PRODUCT_category_id = "category_id";
    private static final String KEY_SHOP_PRODUCT_subcategory_id = "subcategory_id";
    private static final String KEY_SHOP_PRODUCT_product_id = "product_id";
    private static final String KEY_SHOP_PRODUCT_in_stock = "in_stock";
    private static final String KEY_SHOP_PRODUCT_created_at = "created_at";
    private static final String KEY_SHOP_PRODUCT_updated_at = "updated_at";
    private static final String KEY_SHOP_PRODUCT_product_name = "product_name";
    private static final String KEY_SHOP_PRODUCT_description = "description";
    private static final String KEY_SHOP_PRODUCT_vendor_description = "vendor_description";
    private static final String KEY_SHOP_PRODUCT_product_image = "product_image";
    private static final String KEY_SHOP_PRODUCT_is_available = "is_available";

    //cart Table
    private static final String KEY_CART_id = "id";
    private static final String KEY_CART_VARIANT_id="variant_id";
    private static final String KEY_CART_product_id = "product_id";
    private static final String KEY_CART_product_name = "product_name";
    private static final String KEY_CART_product_quantity = "product_quantity";
    private static final String KEY_CART_product_price = "product_price";
    private static final String KEY_CART_product_total = "product_total";
    private static final String KEY_CART_vendrod_id = "vendor_id";
    private static final String KEY_CART_category_id = "category_id";
    private static final String KEY_CART_subcategory_id = "subcategory_id";
    private static final String KEY_CART_product_unit = "unit";

    //variant table
    private static final String KEY_VARIANT_ID = "id";
    private static final String KEY_VARIANT_PRODUCT_ID = "product_id";
    private static final String KEY_VARIANT_PRODUCT_UNIT = "unit";
    private static final String KEY_VARIANT_PRODUCT_QUANTITY = "quantity";
    private static final String KEY_VARIANT_PRODUCT_PRICE = "price";
    private static final String KEY_VARIANT_PRODUCT_SELLING_PRICE = "selling_price";
    private static final String KEY_VARIANT_CREATED_AT = "created_at";
    private static final String KEY_VARIANT_UPDATED_AT = "updated_at";

    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_DATE_SYS_TABLE = "CREATE TABLE " + TABLE_DATE_SYS + "("
                + KEY_DATE_ID + " INTEGER PRIMARY KEY," + KEY_DATE_DATE + " TEXT"+")";
        db.execSQL(CREATE_DATE_SYS_TABLE);

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_CATEGORY_IMAGE + " TEXT,"+ KEY_CATEGORY_STATUS + " TEXT"+")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        String CREATE_SUBCATEGORY_TABLE = "CREATE TABLE " + TABLE_SUB_CATEGORY + "("
                + KEY_SUBCATEGORY_ID + " INTEGER PRIMARY KEY," + KEY_SUBCATEGORY_NAME + " TEXT,"
                + KEY_SUBCATEGORY_IMAGE + " TEXT,"+ KEY_SUBCATEGORY_STATUS + " TEXT,"
                + KEY_SUBCATEGORY_COMMISION + " TEXT,"+ KEY_SUBCATEGORY_CATID + " INTEGER,"
                + " FOREIGN KEY ("+KEY_SUBCATEGORY_CATID+") REFERENCES "+TABLE_CATEGORY+"("+KEY_CATEGORY_ID+"))";
        db.execSQL(CREATE_SUBCATEGORY_TABLE);

        String CREATE_CHILDCATEGORY_TABLE = "CREATE TABLE " + TABLE_CHILD_CATEGORY + "("
                + KEY_CHILDCATEGORY_ID + " INTEGER PRIMARY KEY," + KEY_CHILDCATEGORY_NAME + " TEXT,"
                + KEY_CHILDCATEGORY_IMAGE + " TEXT,"+ KEY_CHILDCATEGORY_STATUS + " TEXT,"
                + KEY_CHILDCATEGORY_SUBCATID + " INTEGER," + " FOREIGN KEY ("+KEY_CHILDCATEGORY_SUBCATID+") REFERENCES "+TABLE_SUB_CATEGORY+"("+KEY_SUBCATEGORY_ID+"))";
        db.execSQL(CREATE_CHILDCATEGORY_TABLE);

        String CREATE_BRAND_TABLE = "CREATE TABLE " + TABLE_BRAND + "("
                + KEY_BRAND_ID + " INTEGER PRIMARY KEY," + KEY_BRAND_NAME + " TEXT,"
                + KEY_BRAND_IMAGE + " TEXT,"+ KEY_BRAND_STATUS + " TEXT"+")";
        db.execSQL(CREATE_BRAND_TABLE);

        String CREATE_BANNER_TABLE = "CREATE TABLE " + TABLE_BANNER + "("
                + KEY_BANNER_IMAGE + " TEXT"+")";
        db.execSQL(CREATE_BANNER_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"+ KEY_USER_MOBILE + " TEXT,"
                + KEY_USER_IMAGE + " TEXT,"+ KEY_USER_LATITUDE + " TEXT,"
                + KEY_USER_LONGITUDE + " TEXT,"+ KEY_USER_TOKEN + " TEXT,"
                + KEY_USER_COUPONCODE + " TEXT,"+ KEY_USER_REFERALCODE + " TEXT,"
                + KEY_userAddress + " TEXT,"+ KEY_userCity + " TEXT,"
                + KEY_userLocality + " TEXT,"+ KEY_userPincode + " TEXT,"
                + KEY_locationAvailable + " TEXT"+ ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_NEARBY_SHOP_TABLE = "CREATE TABLE " + TABLE_NEARBY_SHOP + "("
                + KEY_NEARBY_SHOP_nearby_shop_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_NEARBY_SHOP_id + " INTEGER," + KEY_NEARBY_SHOP_name + " TEXT,"
                + KEY_NEARBY_SHOP_email + " TEXT,"+ KEY_NEARBY_SHOP_mobile + " TEXT,"
                + KEY_NEARBY_SHOP_image + " TEXT,"+ KEY_NEARBY_SHOP_shop_name + " TEXT,"
                + KEY_NEARBY_SHOP_shop_address + " TEXT,"+ KEY_NEARBY_SHOP_city_id + " INTEGER,"
                + KEY_NEARBY_SHOP_area_id + " INTEGER,"+ KEY_NEARBY_SHOP_pincode + " TEXT,"
                + KEY_NEARBY_SHOP_whatsapp + " TEXT,"+ KEY_NEARBY_SHOP_primary_mobile + " TEXT,"
                + KEY_NEARBY_SHOP_shop_image + " TEXT,"+ KEY_NEARBY_SHOP_latitude + " TEXT,"
                + KEY_NEARBY_SHOP_longitude + " TEXT,"+ KEY_NEARBY_SHOP_local_latitude + " TEXT,"
                + KEY_NEARBY_SHOP_local_longitude + " TEXT,"+ KEY_NEARBY_SHOP_shop_approved + " TEXT,"
                + KEY_NEARBY_SHOP_shop_online + " TEXT,"+ KEY_NEARBY_SHOP_created_at + " TEXT,"
                + KEY_NEARBY_SHOP_updated_at + " TEXT,"+ KEY_NEARBY_SHOP_distance + " TEXT"+")";
        db.execSQL(CREATE_NEARBY_SHOP_TABLE);

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_PRODUCT_id + " INTEGER PRIMARY KEY," + KEY_PRODUCT_user_id + " TEXT,"
                + KEY_PRODUCT_category_id + " INTEGER,"+ KEY_PRODUCT_subcategory_id + " INTEGER,"
                + KEY_PRODUCT_in_stock + " TEXT,"+ KEY_PRODUCT_created_at + " TEXT,"
                + KEY_PRODUCT_updated_at + " TEXT,"+ KEY_PRODUCT_product_name + " TEXT,"
                + KEY_PRODUCT_description + " TEXT,"+ KEY_PRODUCT_vendor_description + " TEXT,"
                + KEY_PRODUCT_product_image + " TEXT,"+ KEY_PRODUCT_is_available + " TEXT,"
                + "FOREIGN KEY("+KEY_PRODUCT_category_id+") REFERENCES "+TABLE_CATEGORY+"("+KEY_CATEGORY_ID+"))";
        db.execSQL(CREATE_PRODUCT_TABLE);

        String CREATE_SHOP_PRODUCT_TABLE = "CREATE TABLE " + TABLE_SHOP_PRODUCT + "("
                + KEY_SHOP_PRODUCT_id + "  INTEGER PRIMARY KEY, "
                + KEY_SHOP_PRODUCT_user_id + " INTEGER," + KEY_SHOP_PRODUCT_category_id + " INTEGER,"
                + KEY_SHOP_PRODUCT_subcategory_id + " INTEGER,"+ KEY_SHOP_PRODUCT_product_id + " INTEGER,"
                + KEY_SHOP_PRODUCT_in_stock + " TEXT,"+ KEY_SHOP_PRODUCT_created_at + " TEXT,"
                + KEY_SHOP_PRODUCT_updated_at + " TEXT,"+ KEY_SHOP_PRODUCT_product_name + " INTEGER,"
                + KEY_SHOP_PRODUCT_description + " INTEGER,"+ KEY_SHOP_PRODUCT_vendor_description + " TEXT,"
                + KEY_SHOP_PRODUCT_product_image + " TEXT,"+ KEY_SHOP_PRODUCT_is_available + " TEXT,"
                + "FOREIGN KEY ("+ KEY_SHOP_PRODUCT_category_id+") REFERENCES "+TABLE_CATEGORY+"("+KEY_CATEGORY_ID+"),"
                + "FOREIGN KEY ("+ KEY_SHOP_PRODUCT_subcategory_id+") REFERENCES "+TABLE_SUB_CATEGORY+"("+KEY_SUBCATEGORY_ID+"))";
        db.execSQL(CREATE_SHOP_PRODUCT_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_CART_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_CART_VARIANT_id + " INTEGER,"+KEY_CART_product_unit+" TEXT,"
                + KEY_CART_product_id + " INTEGER," + KEY_CART_product_name + " TEXT,"
                + KEY_CART_product_quantity + " INTEGER,"+ KEY_CART_product_price + " REAL,"
                + KEY_CART_product_total + " REAL,"+ KEY_CART_vendrod_id + " INTEGER,"
                + KEY_CART_category_id + " INTEGER," + KEY_CART_subcategory_id + " INTEGER,"+ "FOREIGN KEY("+KEY_CART_category_id+") REFERENCES "+TABLE_CATEGORY+"("+KEY_CATEGORY_ID+"),"
                + "FOREIGN KEY ("+KEY_CART_product_id+") REFERENCES "+TABLE_SHOP_PRODUCT+"("+KEY_SHOP_PRODUCT_id+"))";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_VARIANT_TABLE = "CREATE TABLE " + TABLE_VARIANT + "("
                + KEY_VARIANT_ID + " INTEGER PRIMARY KEY," + KEY_VARIANT_PRODUCT_ID + " INTEGER,"
                + KEY_VARIANT_PRODUCT_UNIT + " TEXT,"+ KEY_VARIANT_PRODUCT_QUANTITY + " TEXT,"
                + KEY_VARIANT_PRODUCT_PRICE + " REAL,"+ KEY_VARIANT_PRODUCT_SELLING_PRICE + " REAL,"
                + KEY_VARIANT_CREATED_AT + " TEXT,"+ KEY_VARIANT_UPDATED_AT + " TEXT,"
                + "FOREIGN KEY ("+KEY_VARIANT_PRODUCT_ID+") REFERENCES "+TABLE_SHOP_PRODUCT+"("+KEY_SHOP_PRODUCT_id+"))";
        db.execSQL(CREATE_VARIANT_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE_SYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEARBY_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VARIANT);
        onCreate(db);
    }

    public void addDate(String id, String date, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE_DATE, date);
        // Inserting Row
        if (key.equals("add")) {
            values.put(KEY_DATE_ID, id);
            db.insert(TABLE_NEARBY_SHOP, null, values);
        } else {
            db.update(TABLE_NEARBY_SHOP, values, KEY_DATE_ID + "=?", new String[]{id});
        }
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public String getDate(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DATE_SYS, new String[]{ KEY_DATE_DATE},
                KEY_DATE_ID + "=? " , new String[]{id}, null, null,
                null, null);
        String date = null;
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return date;
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_ID, category.getId());
        if (category.getName()!=null) {
            values.put(KEY_CATEGORY_NAME, category.getName());
        } else {
            values.put(KEY_CATEGORY_NAME, "");
        }
        if (category.getImage()!=null) {
            values.put(KEY_CATEGORY_IMAGE, category.getImage());
        } else {
            values.put(KEY_CATEGORY_IMAGE, "");
        }
        if (category.getStatus()!=null) {
            values.put(KEY_CATEGORY_STATUS, category.getStatus());
        } else {
            values.put(KEY_CATEGORY_STATUS, "");
        }
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(0)));
                category.setName(cursor.getString(1));
                category.setImage(cursor.getString(2));
                category.setStatus(cursor.getString(3));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    public void addSubCategory(SubCategory category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBCATEGORY_ID, category.getId());
        if (category.getName()!=null) {
            values.put(KEY_SUBCATEGORY_NAME, category.getName());
        } else {
            values.put(KEY_SUBCATEGORY_NAME, "");
        }
        if (category.getImage()!=null) {
            values.put(KEY_SUBCATEGORY_IMAGE, category.getImage());
        } else {
            values.put(KEY_SUBCATEGORY_IMAGE, "");
        }
        if (category.getStatus()!=null) {
            values.put(KEY_SUBCATEGORY_STATUS, category.getStatus());
        } else {
            values.put(KEY_SUBCATEGORY_STATUS, "");
        }
        values.put(KEY_SUBCATEGORY_CATID, category.getCategoryId());
        db.insert(TABLE_SUB_CATEGORY, null, values);
        db.close();
    }

    public List<SubCategory> getAllSubCategory(Integer id) {
        List<SubCategory> subcategoryList = new ArrayList<SubCategory>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SUB_CATEGORY, new String[] { KEY_SUBCATEGORY_ID,
                        KEY_SUBCATEGORY_NAME, KEY_SUBCATEGORY_IMAGE, KEY_SUBCATEGORY_CATID,
                        KEY_SUBCATEGORY_STATUS}, KEY_SUBCATEGORY_CATID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                SubCategory subcategory = new SubCategory();
                subcategory.setId(Integer.parseInt(cursor.getString(0)));
                subcategory.setName(cursor.getString(1));
                subcategory.setImage(cursor.getString(2));
                subcategory.setCategoryId(Integer.parseInt(cursor.getString(3)));
                subcategory.setStatus(cursor.getString(4));
                // Adding contact to list
                subcategoryList.add(subcategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subcategoryList;
    }

    public void addChildCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHILDCATEGORY_ID, category.getId());
        if (category.getName()!=null) {
            values.put(KEY_CHILDCATEGORY_NAME, category.getName());
        } else {
            values.put(KEY_CHILDCATEGORY_NAME, "");
        }
        if (category.getImage()!=null) {
            values.put(KEY_CHILDCATEGORY_IMAGE, category.getImage());
        } else {
            values.put(KEY_CHILDCATEGORY_IMAGE, "");
        }
        if (category.getStatus()!=null) {
            values.put(KEY_CHILDCATEGORY_STATUS, category.getStatus());
        } else {
            values.put(KEY_CHILDCATEGORY_STATUS, "");
        }
        if (category.getCategory_id()!=null) {
            values.put(KEY_CHILDCATEGORY_SUBCATID, category.getSubcategory_id());
        } else {
            values.put(KEY_CHILDCATEGORY_SUBCATID, 0);
        }
        // Inserting Row
        db.insert(TABLE_CHILD_CATEGORY, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void addBrands(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND_ID, category.getId());
        if (category.getName()!=null) {
            values.put(KEY_BRAND_NAME, category.getName());
        } else {
            values.put(KEY_BRAND_NAME, "");
        }
        if (category.getImage()!=null) {
            values.put(KEY_BRAND_IMAGE, category.getImage());
        } else {
            values.put(KEY_BRAND_IMAGE, "");
        }
        if (category.getStatus()!=null) {
            values.put(KEY_BRAND_STATUS, category.getStatus());
        } else {
            values.put(KEY_BRAND_STATUS, "");
        }
        db.insert(TABLE_BRAND, null, values);
        db.close();
    }

    public void addUser(User user, String token) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getId());
        if (user.getName()!=null) {
            values.put(KEY_USER_NAME, user.getName());
        } else {
            values.put(KEY_USER_NAME, "");
        }
        if (user.getEmail()!=null) {
            values.put(KEY_USER_EMAIL, user.getEmail());
        } else {
            values.put(KEY_USER_EMAIL, "");
        }
        if (user.getMobile()!=null) {
            values.put(KEY_USER_MOBILE, user.getMobile());
        } else {
            values.put(KEY_USER_MOBILE, "");
        }
        if (user.getImage()!=null) {
            values.put(KEY_USER_IMAGE, user.getImage());
        } else {
            values.put(KEY_USER_IMAGE, "");
        }
        if (user.getLatitude()!=null) {
            values.put(KEY_USER_LATITUDE, user.getLatitude());
        } else {
            values.put(KEY_USER_LATITUDE, "");
        }
        if (user.getLongitude()!=null) {
            values.put(KEY_USER_LONGITUDE, user.getLongitude());
        } else {
            values.put(KEY_USER_LONGITUDE, "");
        }
        values.put(KEY_USER_TOKEN, token);
        if (user.getCouponCode()!=null) {
            values.put(KEY_USER_COUPONCODE, user.getCouponCode());
        } else {
            values.put(KEY_USER_COUPONCODE, "");
        }
        if (user.getReferalCode()!=null) {
            values.put(KEY_USER_REFERALCODE, user.getReferalCode());
        } else {
            values.put(KEY_USER_REFERALCODE, "");
        }
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public User getUser(String token) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_USER_ID,
                        KEY_USER_NAME, KEY_USER_EMAIL, KEY_USER_IMAGE, KEY_USER_LATITUDE,
                        KEY_USER_LONGITUDE, KEY_USER_COUPONCODE, KEY_USER_REFERALCODE, KEY_USER_MOBILE},
                KEY_USER_TOKEN + "=?",
                new String[] { token }, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setImage(cursor.getString(3));
                user.setLatitude(cursor.getString(4));
                user.setLongitude(cursor.getString(5));
                user.setCouponCode(cursor.getString(6));
                user.setReferalCode(cursor.getString(7));
                user.setMobile(cursor.getString(8));
            }
        }
        db.close();
        return user;
    }

    public User getUser(String token, String mobile_number) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_USER_ID,
                        KEY_USER_NAME, KEY_USER_EMAIL, KEY_USER_IMAGE, KEY_USER_LATITUDE,
                        KEY_USER_LONGITUDE, KEY_USER_COUPONCODE, KEY_USER_REFERALCODE},
                KEY_USER_TOKEN + "=?"+" AND "+ KEY_USER_MOBILE +"=?",
                new String[] { token, mobile_number }, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setImage(cursor.getString(3));
                user.setLatitude(cursor.getString(4));
                user.setLongitude(cursor.getString(5));
                user.setCouponCode(cursor.getString(6));
                user.setReferalCode(cursor.getString(7));
            }
        }
        db.close();
        return user;
    }

    public void truncateData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CATEGORY);
        db.execSQL("DELETE FROM " + TABLE_SUB_CATEGORY);
        db.execSQL("DELETE FROM " + TABLE_CHILD_CATEGORY);
        db.execSQL("DELETE FROM " + TABLE_BRAND);
        db.close();
    }

    public void truncateUserData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BANNER);
        db.execSQL("DELETE FROM " + TABLE_NEARBY_SHOP);
        db.close();
    }

    public void addBanerImage(String s) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BANNER_IMAGE, s);
        // Inserting Row
        db.insert(TABLE_BANNER, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public ArrayList<String> getBanner() {
        ArrayList<String> images = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_BANNER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String img = cursor.getString(0);
                images.add(img);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images;
    }

    public void addShops(Stores s, String latitude, String longitude, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NEARBY_SHOP_id, s.getId());
        values.put(KEY_NEARBY_SHOP_local_latitude,latitude);
        values.put(KEY_NEARBY_SHOP_local_longitude,longitude);
        if (s.getName()!=null) {
            values.put(KEY_NEARBY_SHOP_name, s.getName());
        } else {
            values.put(KEY_NEARBY_SHOP_name, "");
        }
        if (s.getEmail()!=null) {
            values.put(KEY_NEARBY_SHOP_email, s.getEmail());
        } else {
            values.put(KEY_NEARBY_SHOP_email, "");
        }
        if (s.getMobile()!=null) {
            values.put(KEY_NEARBY_SHOP_mobile, s.getMobile());
        } else {
            values.put(KEY_NEARBY_SHOP_mobile, "");
        }
        if (s.getImage()!=null) {
            values.put(KEY_NEARBY_SHOP_image, s.getImage());
        } else {
            values.put(KEY_NEARBY_SHOP_image, "");
        }
        if (s.getShopName()!=null) {
            values.put(KEY_NEARBY_SHOP_shop_name, s.getShopName());
        } else {
            values.put(KEY_NEARBY_SHOP_shop_name, "");
        }
        if (s.getShopAddress()!=null) {
            values.put(KEY_NEARBY_SHOP_shop_address, s.getShopAddress());
        } else {
            values.put(KEY_NEARBY_SHOP_shop_address, "");
        }
        values.put(KEY_NEARBY_SHOP_city_id, s.getCityId());
        values.put(KEY_NEARBY_SHOP_area_id, s.getAreaId());
        if (s.getPincode()!=null) {
            values.put(KEY_NEARBY_SHOP_pincode, s.getPincode());
        } else {
            values.put(KEY_NEARBY_SHOP_pincode, "");
        }
        if (s.getWhatsapp()!=null) {
            values.put(KEY_NEARBY_SHOP_whatsapp, s.getWhatsapp());
        } else {
            values.put(KEY_NEARBY_SHOP_whatsapp, "");
        }
        if (s.getPrimaryMobile()!=null) {
            values.put(KEY_NEARBY_SHOP_primary_mobile, s.getPrimaryMobile());
        } else {
            values.put(KEY_NEARBY_SHOP_primary_mobile, "");
        }
        if (s.getShopImage()!=null) {
            values.put(KEY_NEARBY_SHOP_shop_image, s.getShopImage());
        } else {
            values.put(KEY_NEARBY_SHOP_shop_image, "");
        }
        if (s.getLatitude()!=null) {
            values.put(KEY_NEARBY_SHOP_latitude, s.getLatitude());
        } else {
            values.put(KEY_NEARBY_SHOP_latitude, "");
        }
        if (s.getLongitude()!=null) {
            values.put(KEY_NEARBY_SHOP_longitude, s.getLongitude());
        } else {
            values.put(KEY_NEARBY_SHOP_longitude, "");
        }
        if (s.getShopApproved()!=null) {
            values.put(KEY_NEARBY_SHOP_shop_approved, s.getShopApproved());
        } else {
            values.put(KEY_NEARBY_SHOP_shop_approved, "");
        }
        if (s.getShopOnline()!=null) {
            values.put(KEY_NEARBY_SHOP_shop_online, s.getShopOnline());
        } else {
            values.put(KEY_NEARBY_SHOP_shop_online, "");
        }
        if (s.getCreatedAt()!=null) {
            values.put(KEY_NEARBY_SHOP_created_at, s.getCreatedAt());
        } else {
            values.put(KEY_NEARBY_SHOP_created_at, "");
        }
        if (s.getUpdatedAt()!=null) {
            values.put(KEY_NEARBY_SHOP_updated_at, s.getUpdatedAt());
        } else {
            values.put(KEY_NEARBY_SHOP_updated_at, "");
        }
        if (s.getDistance()!=null) {
            values.put(KEY_NEARBY_SHOP_distance, s.getDistance());
        }
        // Inserting Row
        if (key.equals("add")) {
            db.insert(TABLE_NEARBY_SHOP, null, values);
        } else {
            db.update(TABLE_NEARBY_SHOP, values, KEY_NEARBY_SHOP_id + "=?", new String[]{String.valueOf(s.getId())});
        }
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public ArrayList<Stores> shopList(String key, String latitude, String longitude) {
        ArrayList<Stores> shops = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (key.equals("all")) {
            cursor = db.query(TABLE_NEARBY_SHOP, new String[]{KEY_NEARBY_SHOP_id, KEY_NEARBY_SHOP_name, KEY_NEARBY_SHOP_email,
                            KEY_NEARBY_SHOP_mobile, KEY_NEARBY_SHOP_image, KEY_NEARBY_SHOP_shop_name, KEY_NEARBY_SHOP_shop_address,
                            KEY_NEARBY_SHOP_city_id, KEY_NEARBY_SHOP_area_id, KEY_NEARBY_SHOP_pincode, KEY_NEARBY_SHOP_whatsapp,
                            KEY_NEARBY_SHOP_primary_mobile, KEY_NEARBY_SHOP_shop_image, KEY_NEARBY_SHOP_latitude, KEY_NEARBY_SHOP_longitude,
                            KEY_NEARBY_SHOP_shop_approved, KEY_NEARBY_SHOP_shop_online, KEY_NEARBY_SHOP_created_at,
                            KEY_NEARBY_SHOP_updated_at, KEY_NEARBY_SHOP_distance}, KEY_NEARBY_SHOP_local_latitude + "=?"+" AND "+ KEY_NEARBY_SHOP_local_longitude +"=?",
                    new String[] { latitude, longitude }, null, null, null, null);
        } else if (key.equals("local")) {
            String limitBy = " 8";
            cursor = db.query(TABLE_NEARBY_SHOP, new String[]{KEY_NEARBY_SHOP_id, KEY_NEARBY_SHOP_name, KEY_NEARBY_SHOP_email,
                    KEY_NEARBY_SHOP_mobile, KEY_NEARBY_SHOP_image, KEY_NEARBY_SHOP_shop_name, KEY_NEARBY_SHOP_shop_address,
                    KEY_NEARBY_SHOP_city_id, KEY_NEARBY_SHOP_area_id, KEY_NEARBY_SHOP_pincode, KEY_NEARBY_SHOP_whatsapp,
                    KEY_NEARBY_SHOP_primary_mobile, KEY_NEARBY_SHOP_shop_image, KEY_NEARBY_SHOP_latitude, KEY_NEARBY_SHOP_longitude,
                    KEY_NEARBY_SHOP_shop_approved, KEY_NEARBY_SHOP_shop_online, KEY_NEARBY_SHOP_created_at,
                    KEY_NEARBY_SHOP_updated_at, KEY_NEARBY_SHOP_distance}, KEY_NEARBY_SHOP_local_latitude + "=? AND "
                    + KEY_NEARBY_SHOP_local_longitude +"=?" ,
                    new String[] { latitude, longitude }, null, null, null, limitBy);
        }
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Stores shop = new Stores();
                shop.setId(cursor.getInt(0));
                shop.setName(cursor.getString(1));
                shop.setEmail(cursor.getString(2));
                shop.setMobile(cursor.getString(3));
                shop.setShopImage(cursor.getString(4));
                shop.setShopName(cursor.getString(5));
                shop.setShopAddress(cursor.getString(6));
                shop.setCityId(cursor.getInt(7));
                shop.setAreaId(cursor.getInt(8));
                shop.setPincode(cursor.getString(9));
                shop.setWhatsapp(cursor.getString(10));
                shop.setPrimaryMobile(cursor.getString(11));
                shop.setShopImage(cursor.getString(12));
                shop.setLatitude(cursor.getString(13));
                shop.setLongitude(cursor.getString(14));
                shop.setShopApproved(cursor.getString(15));
                shop.setShopOnline(cursor.getString(16));
                shop.setCreatedAt(cursor.getString(17));
                shop.setUpdatedAt(cursor.getString(18));
                shop.setDistance(cursor.getString(19));
                shops.add(shop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return shops;
    }

    public int shopListById(long storeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String rawQuery = "SELECT COUNT(*) FROM " + TABLE_NEARBY_SHOP
                + " WHERE " +  KEY_NEARBY_SHOP_id + " = "+storeId;
        Cursor cursor = db.rawQuery(
                rawQuery, null);

        cursor.moveToFirst();
        int count= cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public void addProducts(ShopProduct sp, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHOP_PRODUCT_id, sp.getId());
        values.put(KEY_SHOP_PRODUCT_user_id,sp.getUserId());
        values.put(KEY_SHOP_PRODUCT_category_id,sp.getCategoryId());
        values.put(KEY_SHOP_PRODUCT_subcategory_id,sp.getSubcategory_id());
        values.put(KEY_SHOP_PRODUCT_product_id,sp.getProductId());
        values.put(KEY_SHOP_PRODUCT_in_stock,sp.getInStock());
        if (sp.getCreatedAt()!=null) {
            values.put(KEY_SHOP_PRODUCT_created_at,sp.getCreatedAt());
        } else {
            values.put(KEY_SHOP_PRODUCT_created_at, "");
        }
        if (sp.getUpdatedAt()!=null) {
            values.put(KEY_SHOP_PRODUCT_updated_at,sp.getUpdatedAt());
        } else {
            values.put(KEY_SHOP_PRODUCT_updated_at, "");
        }
        if (sp.getProductName()!=null) {
            values.put(KEY_SHOP_PRODUCT_product_name, sp.getProductName());
        } else {
            values.put(KEY_SHOP_PRODUCT_product_name, "");
        }
        if (sp.getDescription()!=null) {
            values.put(KEY_SHOP_PRODUCT_description, sp.getDescription());
        } else {
            values.put(KEY_SHOP_PRODUCT_description, "");
        }
        if (sp.getVendor_description()!=null) {
            values.put(KEY_SHOP_PRODUCT_vendor_description, sp.getVendor_description());
        } else {
            values.put(KEY_SHOP_PRODUCT_vendor_description, "");
        }
        if (sp.getProduct_image()!=null) {
            values.put(KEY_SHOP_PRODUCT_product_image, sp.getProduct_image());
        } else {
            values.put(KEY_SHOP_PRODUCT_product_image, "");
        }
        values.put(KEY_SHOP_PRODUCT_is_available, sp.getIs_available());
        if (key.equals("update")) {
            db.update(TABLE_SHOP_PRODUCT, values, KEY_SHOP_PRODUCT_id + "=?", new String[]{String.valueOf(sp.getUserId())});
        } else {
            db.insert(TABLE_SHOP_PRODUCT, null, values);
        }
        db.close();
    }

    public void addVariants(Variant v, String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VARIANT_ID, v.getId());
        values.put(KEY_VARIANT_PRODUCT_ID,v.getProduct_user_id());
        values.put(KEY_VARIANT_PRODUCT_QUANTITY,v.getQuantity());
        values.put(KEY_VARIANT_PRODUCT_UNIT,v.getUnit());
        values.put(KEY_VARIANT_PRODUCT_PRICE,v.getPrice());
        values.put(KEY_VARIANT_PRODUCT_SELLING_PRICE,v.getSelling_price());
        if (v.getCreated_at()!=null) {
            values.put(KEY_VARIANT_CREATED_AT,v.getCreated_at());
        } else {
            values.put(KEY_VARIANT_CREATED_AT, "");
        }
        if (v.getUpdated_at()!=null) {
            values.put(KEY_VARIANT_UPDATED_AT,v.getUpdated_at());
        } else {
            values.put(KEY_VARIANT_UPDATED_AT, "");
        }
        if (key.equals("update")) {
            db.update(TABLE_VARIANT, values, KEY_VARIANT_ID + "=?", new String[]{String.valueOf(v.getId())});
        } else {
            db.insert(TABLE_VARIANT, null, values);
        }
        db.close();
    }

    public ArrayList<ShopProduct> productList(String id, String storeId) {
        ArrayList<ShopProduct> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor;
        if (id.equals("0")) {
            cursor = db.query(TABLE_SHOP_PRODUCT, new String[]{KEY_SHOP_PRODUCT_id, KEY_SHOP_PRODUCT_user_id, KEY_SHOP_PRODUCT_category_id,
                            KEY_SHOP_PRODUCT_product_id, KEY_SHOP_PRODUCT_in_stock, KEY_SHOP_PRODUCT_created_at,
                            KEY_SHOP_PRODUCT_updated_at, KEY_SHOP_PRODUCT_product_name, KEY_SHOP_PRODUCT_description, KEY_SHOP_PRODUCT_vendor_description,
                            KEY_SHOP_PRODUCT_product_image, KEY_SHOP_PRODUCT_is_available, KEY_SHOP_PRODUCT_subcategory_id}, KEY_SHOP_PRODUCT_user_id + "=? AND "
                            +KEY_SHOP_PRODUCT_in_stock+" =?",
                    new String[]{storeId, String.valueOf(1)}, null, null, null, null);
        } else {
            cursor = db.query(TABLE_SHOP_PRODUCT, new String[]{KEY_SHOP_PRODUCT_id, KEY_SHOP_PRODUCT_user_id, KEY_SHOP_PRODUCT_category_id,
                            KEY_SHOP_PRODUCT_product_id, KEY_SHOP_PRODUCT_in_stock, KEY_SHOP_PRODUCT_created_at,
                            KEY_SHOP_PRODUCT_updated_at, KEY_SHOP_PRODUCT_product_name, KEY_SHOP_PRODUCT_description, KEY_SHOP_PRODUCT_vendor_description,
                            KEY_SHOP_PRODUCT_product_image, KEY_SHOP_PRODUCT_is_available, KEY_SHOP_PRODUCT_subcategory_id}, KEY_SHOP_PRODUCT_user_id + "=? AND "
                            +KEY_SHOP_PRODUCT_id+ "=? ",
                    new String[]{storeId, id}, null, null, null, null);
        }
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShopProduct sp = new ShopProduct();
                sp.setId(cursor.getInt(0));
                sp.setUserId(cursor.getInt(1));
                sp.setCategoryId(cursor.getInt(2));
                sp.setProductId(cursor.getInt(3));
                sp.setInStock(cursor.getString(4));
                sp.setCreatedAt(cursor.getString(5));
                sp.setUpdatedAt(cursor.getString(6));
                sp.setProductName(cursor.getString(7));
                sp.setDescription(cursor.getString(8));
                sp.setVendor_description(cursor.getString(9));
                sp.setProduct_image(cursor.getString(10));
                sp.setIs_available(cursor.getInt(11));
                sp.setSubcategory_id(cursor.getInt(12));
                String productId = cursor.getString(0);
                sp.variants=new ArrayList<>();
                Cursor cursorVariant = db.query(TABLE_VARIANT, new String[]{KEY_VARIANT_ID, KEY_VARIANT_PRODUCT_ID, KEY_VARIANT_PRODUCT_QUANTITY,
                                KEY_VARIANT_PRODUCT_UNIT, KEY_VARIANT_PRODUCT_PRICE, KEY_VARIANT_PRODUCT_SELLING_PRICE,
                                KEY_VARIANT_CREATED_AT, KEY_VARIANT_UPDATED_AT}, KEY_VARIANT_PRODUCT_ID + "=?",
                        new String[]{productId}, null, null, null, null);
                if (cursorVariant.moveToFirst()) {
                    do {
                        Variant variant = new Variant();
                        variant.setId(cursorVariant.getString(0));
                        variant.setProduct_user_id(cursorVariant.getString(1));
                        variant.setQuantity(cursorVariant.getString(2));
                        variant.setUnit(cursorVariant.getString(3));
                        variant.setPrice(cursorVariant.getString(4));
                        variant.setSelling_price(cursorVariant.getString(5));
                        variant.setCreated_at(cursorVariant.getString(6));
                        variant.setUpdated_at(cursorVariant.getString(7));
                        sp.variants.add(variant);
                    } while (cursorVariant.moveToNext());
                }
                cursorVariant.close();
                products.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public ArrayList<ShopProduct> productList1(String id, String storeId) {
        ArrayList<ShopProduct> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor;
        if (id.equals("0")) {
            cursor = db.query(TABLE_SHOP_PRODUCT, new String[]{KEY_SHOP_PRODUCT_id, KEY_SHOP_PRODUCT_user_id, KEY_SHOP_PRODUCT_category_id,
                            KEY_SHOP_PRODUCT_product_id, KEY_SHOP_PRODUCT_in_stock, KEY_SHOP_PRODUCT_created_at,
                            KEY_SHOP_PRODUCT_updated_at, KEY_SHOP_PRODUCT_product_name, KEY_SHOP_PRODUCT_description, KEY_SHOP_PRODUCT_vendor_description,
                            KEY_SHOP_PRODUCT_product_image, KEY_SHOP_PRODUCT_is_available, KEY_SHOP_PRODUCT_subcategory_id}, KEY_SHOP_PRODUCT_user_id + "=? AND "
                    +KEY_SHOP_PRODUCT_in_stock+" =?",
                    new String[]{storeId, String.valueOf(1)}, null, null, null, null);
        } else {
            cursor = db.query(TABLE_SHOP_PRODUCT, new String[]{KEY_SHOP_PRODUCT_id, KEY_SHOP_PRODUCT_user_id, KEY_SHOP_PRODUCT_category_id,
                            KEY_SHOP_PRODUCT_product_id, KEY_SHOP_PRODUCT_in_stock, KEY_SHOP_PRODUCT_created_at,
                            KEY_SHOP_PRODUCT_updated_at, KEY_SHOP_PRODUCT_product_name, KEY_SHOP_PRODUCT_description, KEY_SHOP_PRODUCT_vendor_description,
                            KEY_SHOP_PRODUCT_product_image, KEY_SHOP_PRODUCT_is_available, KEY_SHOP_PRODUCT_subcategory_id}, KEY_SHOP_PRODUCT_user_id + "=? AND "
                            +KEY_SHOP_PRODUCT_id+ "=? ",
                    new String[]{storeId, id}, null, null, null, null);
        }
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShopProduct sp = new ShopProduct();
                sp.setId(cursor.getInt(0));
                sp.setUserId(cursor.getInt(1));
                sp.setCategoryId(cursor.getInt(2));
                sp.setProductId(cursor.getInt(3));
                sp.setInStock(cursor.getString(4));
                sp.setCreatedAt(cursor.getString(5));
                sp.setUpdatedAt(cursor.getString(6));
                sp.setProductName(cursor.getString(7));
                sp.setDescription(cursor.getString(8));
                sp.setVendor_description(cursor.getString(9));
                sp.setProduct_image(cursor.getString(10));
                sp.setIs_available(cursor.getInt(11));
                sp.setSubcategory_id(cursor.getInt(12));
                String productId = cursor.getString(0);
                sp.variants=new ArrayList<>();
                Cursor cursorVariant = db.query(TABLE_VARIANT, new String[]{KEY_VARIANT_ID, KEY_VARIANT_PRODUCT_ID, KEY_VARIANT_PRODUCT_QUANTITY,
                                KEY_VARIANT_PRODUCT_UNIT, KEY_VARIANT_PRODUCT_PRICE, KEY_VARIANT_PRODUCT_SELLING_PRICE,
                                KEY_VARIANT_CREATED_AT, KEY_VARIANT_UPDATED_AT}, KEY_VARIANT_PRODUCT_ID + "=?",
                        new String[]{productId}, null, null, null, null);
                if (cursorVariant.moveToFirst()) {
                    do {
                        Variant variant = new Variant();
                        variant.setId(cursorVariant.getString(0));
                        variant.setProduct_user_id(cursorVariant.getString(1));
                        variant.setQuantity(cursorVariant.getString(2));
                        variant.setUnit(cursorVariant.getString(3));
                        variant.setPrice(cursorVariant.getString(4));
                        variant.setSelling_price(cursorVariant.getString(5));
                        variant.setCreated_at(cursorVariant.getString(6));
                        variant.setUpdated_at(cursorVariant.getString(7));
                        sp.variants.add(variant);
                    } while (cursorVariant.moveToNext());
                }
                cursorVariant.close();
                products.add(sp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return products;
    }

    public ArrayList<Variant> getVariant(String id, String key) {
        ArrayList<Variant> variants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor;
        if (key.equals("variant_id")) {
            cursor = db.query(TABLE_VARIANT, new String[]{KEY_VARIANT_ID, KEY_VARIANT_PRODUCT_ID, KEY_VARIANT_PRODUCT_QUANTITY,
                            KEY_VARIANT_PRODUCT_UNIT, KEY_VARIANT_PRODUCT_PRICE, KEY_VARIANT_PRODUCT_SELLING_PRICE,
                            KEY_VARIANT_CREATED_AT, KEY_VARIANT_UPDATED_AT}, KEY_VARIANT_ID + "=?",
                    new String[]{id}, null, null, null, null);
        } else {
            cursor = db.query(TABLE_VARIANT, new String[]{KEY_VARIANT_ID, KEY_VARIANT_PRODUCT_ID, KEY_VARIANT_PRODUCT_QUANTITY,
                            KEY_VARIANT_PRODUCT_UNIT, KEY_VARIANT_PRODUCT_PRICE, KEY_VARIANT_PRODUCT_SELLING_PRICE,
                            KEY_VARIANT_CREATED_AT, KEY_VARIANT_UPDATED_AT}, KEY_VARIANT_PRODUCT_ID + "=?",
                    new String[]{id}, null, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do {
                Variant variant = new Variant();
                variant.setId(cursor.getString(0));
                variant.setProduct_user_id(cursor.getString(1));
                variant.setQuantity(cursor.getString(2));
                variant.setUnit(cursor.getString(3));
                variant.setPrice(cursor.getString(4));
                variant.setSelling_price(cursor.getString(5));
                variant.setCreated_at(cursor.getString(6));
                variant.setUpdated_at(cursor.getString(7));
                variants.add(variant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return variants;
    }

    public ArrayList<SubCategory> getAllSubCategory(String storeId) {
        ArrayList<SubCategory> subcategoryList = new ArrayList<SubCategory>();
        SQLiteDatabase db = this.getReadableDatabase();
        String rawQuery = "SELECT Distinct s.* FROM " + TABLE_SUB_CATEGORY + " s INNER JOIN " + TABLE_SHOP_PRODUCT
                + " p ON s." + KEY_SUBCATEGORY_ID + " = "+"p." + KEY_SHOP_PRODUCT_subcategory_id
                + " WHERE " + "p." + KEY_SHOP_PRODUCT_user_id + " = ?";
        Cursor cursor = db.rawQuery(
                rawQuery,
                new String[]{storeId}
        );
        if (cursor.moveToFirst()) {

            do {
                SubCategory subcategory = new SubCategory();
                subcategory.setId(Integer.parseInt(cursor.getString(0)));
                subcategory.setName(cursor.getString(1));
                subcategory.setImage(cursor.getString(2));
                subcategory.setStatus(cursor.getString(3));
                subcategory.setCommision(cursor.getString(4));
                subcategory.setCategoryId(cursor.getInt(5));
                // Adding contact to list
                subcategoryList.add(subcategory);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return subcategoryList;
    }

    public CartItem getCartItem(String productId, String variant_id) {
        CartItem cartItem = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String limitBy = " 1";
        Cursor cursor = db.query(TABLE_CART, new String[]{KEY_CART_id, KEY_CART_VARIANT_id, KEY_CART_product_id,
                        KEY_CART_product_name, KEY_CART_product_quantity, KEY_CART_product_price,
                        KEY_CART_product_total, KEY_CART_vendrod_id},
                KEY_CART_product_id + "=? AND " + KEY_CART_VARIANT_id + "=?" , new String[]{productId, variant_id}, null, null,
                null, limitBy);
        if (cursor.moveToFirst()) {
            do {
                cartItem = new CartItem();
                cartItem.setId(cursor.getInt(0));
                cartItem.setVariant_id(cursor.getInt(1));
                cartItem.setProduct_user_id(cursor.getInt(2));
                cartItem.setProduct_name(cursor.getString(3));
                cartItem.setQuantity(cursor.getInt(4));
                cartItem.setPrice(cursor.getFloat(5));
                cartItem.setSubtotal(cursor.getFloat(6));
                cartItem.setVendor_id(cursor.getInt(7));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItem;
    }

    public ArrayList<CartItem> getCartItem() {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CART, new String[]{KEY_CART_id, KEY_CART_VARIANT_id, KEY_CART_product_id,
                        KEY_CART_product_name, KEY_CART_product_quantity, KEY_CART_product_price,
                        KEY_CART_product_total, KEY_CART_vendrod_id, KEY_CART_product_unit},
                null , null, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem();
                cartItem.setId(cursor.getInt(0));
                cartItem.setVariant_id(cursor.getInt(1));
                cartItem.setProduct_user_id(cursor.getInt(2));
                cartItem.setProduct_name(cursor.getString(3));
                cartItem.setQuantity(cursor.getInt(4));
                cartItem.setPrice(cursor.getFloat(5));
                cartItem.setSubtotal(cursor.getFloat(6));
                cartItem.setVendor_id(cursor.getInt(7));
                cartItem.setUnit(cursor.getString(8));
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }

    public CartItem getPriceCategory(String variant_id) {
        String selectQuery = "SELECT  sp."+KEY_SHOP_PRODUCT_category_id+", sp."+KEY_SHOP_PRODUCT_subcategory_id+
                ", sp."+KEY_SHOP_PRODUCT_product_name+ ", sp."+KEY_SHOP_PRODUCT_user_id+ ", v." +KEY_VARIANT_PRODUCT_SELLING_PRICE+
                ", v."+KEY_VARIANT_PRODUCT_UNIT+" FROM " + TABLE_SHOP_PRODUCT+" sp INNER JOIN "+ TABLE_VARIANT +
                " v ON sp." +KEY_SHOP_PRODUCT_id+ " = v."+KEY_VARIANT_PRODUCT_ID+ " where v."+KEY_VARIANT_ID+" = ? limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{variant_id});
        CartItem cartItem = new CartItem();
        if (cursor.moveToFirst()) {
            do {
                cartItem.setCategory_id(cursor.getInt(0));
                cartItem.setSubcategory_id(cursor.getInt(1));
                cartItem.setProduct_name(cursor.getString(2));
                cartItem.setVendor_id(cursor.getInt(3));
                cartItem.setPrice(cursor.getFloat(4));
                cartItem.setUnit(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItem;
    }

    public int addToCart(String key,String productId,int qty,String variant_id,float total,CartItem cI) {
        int count=0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CART_product_id, productId);
        values.put(KEY_CART_product_quantity,qty);
        values.put(KEY_CART_product_total,total);
        values.put(KEY_CART_product_price,cI.getPrice());
        values.put(KEY_CART_product_name,cI.getProduct_name());
        values.put(KEY_CART_category_id,cI.getCategory_id());
        values.put(KEY_CART_subcategory_id,cI.getSubcategory_id());
        values.put(KEY_CART_VARIANT_id, variant_id);
        values.put(KEY_CART_vendrod_id,cI.getVendor_id());
        values.put(KEY_CART_product_unit,cI.getUnit());
        if (key.equals("update")) {
            db.update(TABLE_CART, values, KEY_CART_VARIANT_id + "=? AND " + KEY_CART_product_id +" =?", new String[]{variant_id, productId});
            ++count;
        } else {
            db.insert(TABLE_CART, null, values);
            ++count;
        }
        db.close();
        return count;
    }

    public String getQuantity(String productId, String variant_id) {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{ KEY_CART_product_quantity},
                KEY_CART_product_id + "=? AND " + KEY_CART_VARIANT_id + "=?" , new String[]{productId, variant_id}, null, null,
                null, null);
        String quantity = "0";
        if (cursor.moveToFirst()) {
            do {
                quantity = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quantity;
    }

    public int deleteProduct(String productId, String variantId) {
        int count = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_CART, KEY_CART_product_id+"=? AND "+KEY_CART_VARIANT_id+"=?", new String[]{productId,
                    variantId});
            db.close();
            count = 1;
        } catch (Exception e) {
            count = 0;
        }
        return count;
    }

    public int getSubTotal() {
        int subTotal=0;
        String selectQuery = "SELECT SUM("+KEY_CART_product_total+") FROM "+TABLE_CART;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                subTotal = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return subTotal;
    }

    public void truncateCart() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_CART_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + KEY_CART_VARIANT_id + " INTEGER,"+KEY_CART_product_unit+" TEXT,"
                + KEY_CART_product_id + " INTEGER," + KEY_CART_product_name + " TEXT,"
                + KEY_CART_product_quantity + " INTEGER,"+ KEY_CART_product_price + " REAL,"
                + KEY_CART_product_total + " REAL,"+ KEY_CART_vendrod_id + " INTEGER,"
                + KEY_CART_category_id + " INTEGER," + KEY_CART_subcategory_id + " INTEGER,"+ "FOREIGN KEY("+KEY_CART_category_id+") REFERENCES "+TABLE_CATEGORY+"("+KEY_CATEGORY_ID+"),"
                + "FOREIGN KEY ("+KEY_CART_product_id+") REFERENCES "+TABLE_SHOP_PRODUCT+"("+KEY_SHOP_PRODUCT_id+"))";
        db.execSQL(CREATE_CART_TABLE);
        db.close();
    }
}