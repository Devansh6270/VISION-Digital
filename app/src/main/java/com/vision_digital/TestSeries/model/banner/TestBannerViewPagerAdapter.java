package com.vision_digital.TestSeries.model.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.vision_digital.R;

import java.util.List;

public class TestBannerViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<ItemTestBanner> testBannerList;

    public TestBannerViewPagerAdapter(Context mContext, List<ItemTestBanner> testBannerList) {
        this.mContext = mContext;
        this.testBannerList = testBannerList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.test_image_banner_layout,null);

        ImageView bannerImage = layoutScreen.findViewById(R.id.imageBanner);

        Glide.with(mContext).load(testBannerList.get(position).getImageBanner()).into(bannerImage);

        container.addView(layoutScreen);
        return layoutScreen;
    }


    @Override
    public int getCount() {
        return testBannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
