package in.happiness.groceryapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MainData implements Serializable {
    @SerializedName("categories")
    @Expose
    private List<Category> categories;
    @SerializedName("subcategories")
    @Expose
    private List<SubCategory> subcategories;
    @SerializedName("childcategories")
    @Expose
    private List<Category> childcategories;
    @SerializedName("brands")
    @Expose
    private List<Category> brands;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<SubCategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubCategory> subcategories) {
        this.subcategories = subcategories;
    }

    public List<Category> getChildcategories() {
        return childcategories;
    }

    public void setChildcategories(List<Category> childcategories) {
        this.childcategories = childcategories;
    }

    public List<Category> getBrands() {
        return brands;
    }

    public void setBrands(List<Category> brands) {
        this.brands = brands;
    }
}
