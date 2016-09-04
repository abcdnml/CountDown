package com.aaa.cd.ui;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.aaa.cd.R;
import com.aaa.cd.util.SystemStatusManager;

import java.io.IOException;

public class GuideActivity extends FragmentActivity {

    Fragment[] fragments;
    private ViewPager vp_guide;
    private FragmentManager fm = getSupportFragmentManager();
    FragmentPagerAdapter pa = new FragmentPagerAdapter(fm) {
        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    };
    private RadioGroup rg_index;
    private int indexId[] = {R.id.rb_guide_first, R.id.rb_guide_secend, R.id.rb_guide_third, R.id.rb_guide_forth};

    TelephonyManager manager;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        manager.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);

        initView();
        initPage();
        initMediaPlayer();
    }

    private void initView() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        rg_index = (RadioGroup) findViewById(R.id.rg_guide_index_group);
    }

    private void initPage() {
        fragments = new Fragment[4];
        fragments[0] = new GuideFirstFragment();
        fragments[1] = new GuideSecendFragment();
        fragments[2] = new GuideThirdFragment();
        fragments[3] = new GuideForthFragment();
        vp_guide.setOffscreenPageLimit(3);
        vp_guide.setAdapter(pa);
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rg_index.check(indexId[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource(getAssets().openFd("bgm.mp3").getFileDescriptor());
            player.prepare();
            // 为播放器注册
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    player.start();
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class MyListener extends PhoneStateListener {
        boolean hasPhone = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // 音乐播放器暂停
                    pause();
                    hasPhone = true;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    hasPhone = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //这里一注册就收到一条 所以先判断下
                    if (hasPhone) {
                        hasPhone = false;
                        Log.i("aaaaaaaa", "CALL_STATE_IDLE ");
                        // 重新播放音乐
                        replay();
                    }
                    break;
            }
        }
    }

    private void pause() {
        // 判断音乐是否在播放
        if (player != null && player.isPlaying()) {
            // 暂停音乐播放器
            player.pause();
        }
    }

    private void replay() {
        if (player != null && !player.isPlaying()) {
            // 暂停音乐播放器
            player.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        player = null;
    }

    /**
     * 设置状态栏背景状态
     */
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemStatusManager tintManager = new SystemStatusManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(0);//状态栏无背景
    }
}
