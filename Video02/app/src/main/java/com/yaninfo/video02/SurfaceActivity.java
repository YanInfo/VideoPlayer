package com.yaninfo.video02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

/**
 * SurfaceView测试
 */

public class SurfaceActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, Runnable {

    private int width = 2;
    private int height = 2;
    private MediaPlayer mMediaPlayer = null;
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder holder = null;
    private String path = "";
    private final int UPDATE_SB = 1;
    private SeekBar mSeekBar;
    private RelativeLayout frame;
    private static boolean isFullScreen = false;
    private Context mContext;
    private Button play;
    private Button stop;
    private Button drop;
    private Button screen;

    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surface);
        thread = new Thread(this);
        mContext = SurfaceActivity.this;

        initView();

        // 设置风格
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        //SurfaceView屏幕监听
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }

        });

    }

    /**
     * 初始化
     */
    private void initView() {

        mSurfaceView = this.findViewById(R.id.surfaceVideo_surfaceView);
        frame = findViewById(R.id.frame);
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        width = mMediaPlayer.getVideoWidth();
        height = mMediaPlayer.getVideoHeight();
        if (width != 0 && height != 0) {
            // 设置视频高宽
            holder.setFixedSize(width, height);
            mMediaPlayer.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(holder);

        //设置显示视频显示在SurfaceView上
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video/fssx.mp4";
        //String str = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        while (mMediaPlayer.isPlaying()) {
            Message message = new Message();
            message.what = UPDATE_SB;
            handler.sendMessage(message);
            SystemClock.sleep(300);
            if (mMediaPlayer == null) {
                break;
            }
        }
    }


    /**
     * 接收消息
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case UPDATE_SB:
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());

                    break;
            }
        }

    };


    /**
     * 全屏显示
     */
    public void doScreen(View view) {
        //非全屏的时候
        if (!isFullScreen) {
            isFullScreen = true;
            // 手动横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            frame.setLayoutParams(lp);

        }
        //退出全屏
        else {


        }

    }

    /**
     * \
     * 设置SurfaceView悬浮框
     *
     * @param v
     */
    private void initPopWindow(View v) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.buttons, null, false);
        mSeekBar = view.findViewById(R.id.seekBar);
        if (mMediaPlayer != null) {
            mSeekBar.setMax(mMediaPlayer.getDuration());
        }
        mSeekBar.setProgress(mSeekBar.getProgress());
        //构造函数，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;

            }
        });

        //要为popWindow设置一个背景才有效
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (mMediaPlayer != null) {
                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

        });

        //播放按钮
        play = view.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    thread.start();
                } else {
                    Toast.makeText(SurfaceActivity.this, "播放已经开始", Toast.LENGTH_LONG).show();
                }
            }
        });

        //暂停按钮
        stop = view.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();//暂停
                    ((Button) view).setText("继续播放");
                } else if (mMediaPlayer != null && mMediaPlayer.isPlaying() == false) {
                    mMediaPlayer.start();//播放

                    //启动线程
                    thread.start();
                    ((Button) view).setText("暂停");
                }

            }
        });

        //停止按钮
        drop = view.findViewById(R.id.drop);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMediaPlayer.pause();
                mMediaPlayer.stop();
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mSeekBar.setProgress(0);
            }
        });


    }


    /**
     * 待机的时候 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();

        }
    }

    /**
     * 手机亮屏后，重新唤醒
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() == false) {
            mMediaPlayer.start();
        }
    }

}
