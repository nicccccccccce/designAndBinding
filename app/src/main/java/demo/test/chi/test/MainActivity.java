package demo.test.chi.test;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import demo.test.chi.BaseApplication;
import demo.test.chi.adapter.BaseRecyclerAdapter;
import demo.test.chi.http.BaseHttp;
import demo.test.chi.http.HttpAsyTask;
import demo.test.chi.http.ResultCallBack;


public class MainActivity extends BaseHttp implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.backdrop)
    ImageView  imageView;
    @InjectView(R.id.refreshLay)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tab_layout)
    TabLayout mTabLayout;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigation)
    NavigationView mNavigationView;
    @InjectView(R.id.floating)
    FloatingActionButton mFloatingActionButton;
    ActionBarDrawerToggle mDrawerToggle;
    Snackbar sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        mToolbar.setTitle("design");
        mTabLayout.addTab(mTabLayout.newTab().setText("选项一"));
        mTabLayout.addTab(mTabLayout.newTab().setText("选项二"));
        mTabLayout.addTab(mTabLayout.newTab().setText("选项三"));

//        String url = "http://pic4.nipic.com/20090803/2618170_095921092_2.jpg";
//        ImageLoader.getInstance().displayImage(url,imageView, BaseApplication.getInstance().getOptions());
//        Request request = new Request.Builder().url(url).build();
//        new HttpAsyTask().execute(request);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.black, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, 150);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
//        GridLayoutManager manager = new GridLayoutManager(this, 2);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        List<String> lists = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lists.add("第" + i + "项");
        }
        BaseRecyclerAdapter adapter = new BaseRecyclerAdapter(this, lists);
        mRecyclerView.setAdapter(adapter);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.toggle_open, R.string.toggle_close);
        mDrawerLayout.setDrawerListener(new MyDrawerListener());
       sb=Snackbar.make(mToolbar, "connection error", Snackbar.LENGTH_LONG).setAction("retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.dismiss();
            }
        });
        //红色
        sb.getView().setBackgroundColor(0xfff44336);
        sb.show();
//        Animator animator = ViewAnimationUtils.createCircularReveal(
//                mSwipeRefreshLayout,
//                mSwipeRefreshLayout.getWidth() / 2,
//                mSwipeRefreshLayout.getHeight() / 2,
//                mSwipeRefreshLayout.getWidth(),
//                0);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.setDuration(2000);
//        animator.start();

    }

    @Override
    public void onEventMainThread(ResultCallBack back) {
        super.onEventMainThread(back);
        if (back.getResponse() != null && back.getResponse().isSuccessful()) {
            byte[] bytes = null;
            try {
                bytes = back.getResponse().body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytes != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//              imageView.setImageBitmap(bmp);
            }
        } else {
            try {
                throw new IOException("Unexpected code " + back.getResponse());
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * activity创建完成后
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
    }

    /**
     * 菜单键点击的事件处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * 设备配置改变时
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * drawer的监听
     */
    private class MyDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {// 打开drawer
            mDrawerToggle.onDrawerOpened(drawerView);//开关状态改为opened
        }

        @Override
        public void onDrawerClosed(View drawerView) {// 关闭drawer
            mDrawerToggle.onDrawerClosed(drawerView);//开关状态改为closed
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//               List<String> lists = new ArrayList<String>();
//               for (int i = 0; i < 20; i++)
//               {
//                   lists.add("第" + i + "项");
//               }
//               BaseRecyclerAdapter adapter=new BaseRecyclerAdapter(MainActivity.this,lists);
//               mRecyclerView.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}