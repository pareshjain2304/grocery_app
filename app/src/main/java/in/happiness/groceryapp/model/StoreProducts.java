package in.happiness.groceryapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class StoreProducts implements Serializable {
    @SerializedName("shop_products")
    @Expose
    private ArrayList<ShopProduct> shopProducts;
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories;
    @SerializedName("subcategories")
    @Expose
    private ArrayList<SubCategory> subcategories;
    @SerializedName("related_products")
    @Expose
    private ArrayList<ShopProduct> related_products;
    @SerializedName("product_details")
    @Expose
    private ShopProd product_details;

    public ArrayList<ShopProduct> getShopProducts() {
        return shopProducts;
    }

    public void setShopProducts(ArrayList<ShopProduct> shopProducts) {
        this.shopProducts = shopProducts;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<ShopProduct> getRelated_products() {
        return related_products;
    }

    public void setRelated_products(ArrayList<ShopProduct> related_products) {
        this.related_products = related_products;
    }

    public ShopProd getProduct_details() {
        return product_details;
    }

    public void setProduct_details(ShopProd product_details) {
        this.product_details = product_details;
    }

    public ArrayList<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }
}
