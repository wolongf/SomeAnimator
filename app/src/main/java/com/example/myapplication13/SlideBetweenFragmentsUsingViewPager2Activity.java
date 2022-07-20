package com.example.myapplication13;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SlideBetweenFragmentsUsingViewPager2Activity extends FragmentActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_between_fragments_using_view_pager2);

        // 设置不同的 Fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(new ScreenSlidePageFragment(getResources().getString(R.string.a_happy_excursion_1)));
        fragmentList.add(new ScreenSlidePageFragment(getResources().getString(R.string.a_happy_excursion_2)));
        fragmentList.add(new ScreenSlidePageFragment(getResources().getString(R.string.a_happy_excursion_3)));
        fragmentList.add(new ScreenSlidePageFragment(getResources().getString(R.string.a_happy_excursion_4)));
        fragmentList.add(new ScreenSlidePageFragment(getResources().getString(R.string.a_happy_excursion_5)));
        fragmentList.add(new ScreenSlidePageFragment(getResources().getString(R.string.a_happy_excursion_6)));

        // 初始化 ViewPager2 和 PagerAdapter
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);

        // 设置适配器
        viewPager.setAdapter(pagerAdapter);

        // 缩小页面转换器
        findViewById(R.id.zoomOutPageTransformer).setOnClickListener(v -> {
            viewPager.setPageTransformer(new ZoomOutPageTransformer());
            Toast.makeText(this, "切换为缩小页面转换器", Toast.LENGTH_SHORT).show();
        });

        // 深度页面转换器
        findViewById(R.id.depthPageTransformer).setOnClickListener(v -> {
            viewPager.setPageTransformer(new DepthPageTransformer());
            Toast.makeText(this, "切换为深度页面转换器", Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * 返回
     */
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // 当 viewPager 是第一页时，允许返回上一个 activity
            super.onBackPressed();
        } else {
            // 否则，返回到 viewPager 上一页
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * 设置 FragmentStateAdapter
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        /**
         * 创建 Fragment
         *
         * @param position
         * @return
         */
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        /**
         * 获取页数
         *
         * @return
         */
        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }

    /**
     * 缩小页面转换器
     */
    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        /**
         * 页面过渡动画
         * 视图位置：前一个视图（-1） --- 当前视图（0） --- 后一个视图（1）
         * 左划时，所有视图的 position 都在减小，方法会记录进入视图和退出视图的 position，变化情况分别是 1 -> 0 、 0 -> -1
         * 右划时，所有视图的 position 都在增大，方法会记录进入视图和退出视图的 position，变化情况分别是 -1 -> 0 、 0 -> 1
         *
         * @param view
         * @param position
         */
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) {
                // position 范围为 [-Infinity,-1)
                // 当前视图完整显示，此时处于 position 位置的视图在当前视图的左边，其已完全退出，因此需隐藏该视图
                view.setAlpha(0f);

            } else if (position <= 1) {
                // position 范围为 [-1,1]
                // 修改默认的位移和缩放动画

                // 根据 position 计算缩放比例，并有最低值限制
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                // 根据缩放比例计算竖直和水平方向的位移量
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                // 设置进入和退出视图的偏移量（不设置的话也是有空隙的，只不过空隙较大，以下做法只是缩小空隙）
                if (position < 0) {
                    // 让退出视图向右偏移，偏移量 < horzMargin，如果偏移量 = horzMargin，那么两个视图就贴在一起了
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    // 让进入视图向左偏移，偏移量 > horzMargin，如果偏移量 = horzMargin，那么两个视图就贴在一起了
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // 缩放视图
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // 根据视图的大小调整透明度
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else {
                // position 范围为 (1,+Infinity]
                // 当前视图完整显示，此时处于 position 位置的视图在当前视图的右边，其已完全退出，因此需隐藏该视图
                view.setAlpha(0f);
            }
        }
    }

    /**
     * 深度页面转换器
     */
    @RequiresApi(21)
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        /**
         * 页面过渡动画
         * 视图位置：前一个视图（-1）--- 当前视图（0） --- 后一个视图（1）
         * 左划时，所有视图的 position 都在减小，方法会记录 进入视图 和 退出视图 的 position，变化情况分别是 1 -> 0 、 0 -> -1
         * 右划时，所有视图的 position 都在增大，方法会记录 进入视图 和 退出视图 的 position，变化情况分别是 -1 -> 0 、 0 -> 1
         *
         * @param view
         * @param position
         */
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                // position 范围为 [-Infinity,-1)
                // 当前视图完整显示，此时处于 position 位置的视图在当前视图的左边，其已完全退出，因此需隐藏该视图
                view.setAlpha(0f);

            } else if (position <= 0) {
                // position 范围为 [-1,0]
                // 修改默认的透明度、位移和缩放动画

                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) {
                // position 范围为 (0,1]
                // 设置透明度
                view.setAlpha(1 - position);

                // 在深度动画期间，默认动画（屏幕滑动）仍然会发生，因此必须使用负 X 平移抵消屏幕滑动
                view.setTranslationX(pageWidth * -position);
                // 设置视图在底层
                view.setTranslationZ(-1f);

                // 缩放视图
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else {
                // position 范围为 (1,+Infinity]
                // 当前视图完整显示，此时处于 position 位置的视图在当前视图的右边，其已完全退出，因此需隐藏该视图
                view.setAlpha(0f);
            }
        }
    }
}