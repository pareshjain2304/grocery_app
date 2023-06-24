package in.happiness.groceryapp.adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import in.happiness.groceryapp.R;
import in.happiness.groceryapp.utils.AppConstant;


public class ViewPagerAdapter extends PagerAdapter {
    Context c;
    private List<String> _imagePaths;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context c, List<String> imagePaths) {
        this._imagePaths = imagePaths;
        this.c = c;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;

        inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.pager_item, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.image);

        Glide.with(c).load(AppConstant.IMAGE_URL+_imagePaths.get(position)).into(imgDisplay);
        (container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((RelativeLayout) object);

    }
}