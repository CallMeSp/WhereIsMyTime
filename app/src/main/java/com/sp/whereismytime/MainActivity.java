package com.sp.whereismytime;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nkzawa.socketio.client.Socket;
import com.sp.whereismytime.Service.ServiceListener;
import com.sp.whereismytime.adapter.MainRecyclerAdapter;
import com.sp.whereismytime.base.LogUtil;
import com.sp.whereismytime.model.OneDayInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static boolean isLogin=false;
    private static final String TAG = "MainActivity";
    private MainRecyclerAdapter adapter;
    private ArrayList<String> list=new ArrayList<>();
    private Tencent mTencent;
    private IUiListener listener;
    private ImageView avatar;
    private Socket socket=null;
    @BindView(R.id.img_background)ImageView imageView;
    @BindView(R.id.today_count)TextView textView_count;
    @BindView(R.id.thisusetate)TextView textView_thisusestate;
    @BindView(R.id.recyclerview)RecyclerView recyclerView;
    @BindView(R.id.nav_view)NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Glide.with(this)
                .load(R.mipmap.background)
                .into(imageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action???", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RecyclerView
        adapter=new MainRecyclerAdapter(this,get());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        //QQ登陆
        mTencent=Tencent.createInstance("222222",getApplicationContext());

        View headView=navigationView.getHeaderView(0);
        avatar=(ImageView)headView.findViewById(R.id.imageView_head);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        //连接服务器

    }
    private void doLogin(){
        listener= new IUiListener() {
            @Override
            public void onComplete(Object o) {
                LogUtil.log(TAG,"login complete"+o.toString());
                try {
                    JSONObject jsonObject=new JSONObject(o.toString());
                    String openid=jsonObject.getString("openid");
                    LogUtil.log(TAG,"openid:"+openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                QQToken qqToken = mTencent.getQQToken();
                UserInfo info = new UserInfo(getApplicationContext(), qqToken);
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        LogUtil.log(TAG,"get complete"+o.toString());
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
            @Override
            public void onError(UiError uiError) {
                LogUtil.log(TAG,"login error");
            }

            @Override
            public void onCancel() {
                LogUtil.log(TAG,"login cancle");
            }
        };
        mTencent.login(this, "all",listener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 官方文档没没没没没没没没没没没这句代码, 但是很很很很很很重要, 不然不会回调!
        LogUtil.log(TAG,"onActvityResult");
        //Tencent.onActivityResultData(requestCode, resultCode, data,listener);
        Tencent.handleResultData(data,listener);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_history) {
            ShowHistoryActivity.Start(this);
        } else if (id == R.id.nav_setting) {
            SettingActivity.Start(this);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_Syncronize) {
            //从服务器向客户端同步
            OneDayInfo oneDayInfo=new OneDayInfo();
            oneDayInfo.setCount(getCount());
            oneDayInfo.setDate(getDate());
            binder.synchronizeToClient(oneDayInfo, new ServiceListener() {
                @Override
                public void updateCount(int count) {
                    LogUtil.log(TAG,"count="+count);
                    setCount(count);
                    makeAnimationTextcount(count);
                }
            });

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        makeAnimationTextcount(getCount());
        LogUtil.log("???",get().toString()+getDate());
        adapter.notifyDataSetChanged();
        textView_thisusestate.setText("本次使用开始时间："+getThisUseStartTime());

    }

    //数值变化的动画效果
    private void makeAnimationTextcount(final int count){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ValueAnimator numanimator=ValueAnimator.ofInt(0,count);
                numanimator.setDuration(500);
                numanimator.setRepeatCount(0);
                numanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int current=(int)animation.getAnimatedValue();
                        textView_count.setText(""+current);
                    }
                });
                numanimator.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
