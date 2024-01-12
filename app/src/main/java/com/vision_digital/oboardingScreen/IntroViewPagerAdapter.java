package com.vision_digital.oboardingScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.vision_digital.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<ScreenItem> screenItemList;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> screenItemList) {
        this.mContext = mContext;
        this.screenItemList = screenItemList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_scroll,null);

        ImageView screenImage = layoutScreen.findViewById(R.id.imageView);
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView description = layoutScreen.findViewById(R.id.intro_desc);

        title.setText(screenItemList.get(position).getTitle());
        description.setText(screenItemList.get(position).getDescription());
        screenImage.setImageResource(screenItemList.get(position).getScreenImage());
//        screenImage.setAnimation(screenItemList.get(position).getScreenImage());

        container.addView(layoutScreen);
        return layoutScreen;

    }

    @Override
    public int getCount() {
        return screenItemList.size();
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
