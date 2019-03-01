package com.yaninfo.jcvideoplayer;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.yaninfo.jcvideoplayer.Adapter.VideoAdapter;

import java.util.ArrayList;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 主活动
 * @author zhangyan
 */
public class MainActivity extends AppCompatActivity {

    //在线视频     
    private String videoUrl = "http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4";
    private String videoUrl1 = "http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv";
    private String videoUrl2 = "https://key002.ku6.com/xy/d7b3278e106341908664638ac5e92802.mp4";
    private String videoUrl3 = "https://key002.ku6.com/xy/d7b3278e106341908664638ac5e92802.mp4";
    private String videoUrl4 = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    private String videoUrl5 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    //本地视频     
    String localUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/video/aaaa.mp4";

    private ListView listView;
    private ArrayList<String> datas;
    private JCVideoPlayerStandard currPlayer;
    private VideoAdapter adapter;
    private ImageView image;
    /**
     * 滑动监听
     */
    private AbsListView.OnScrollListener onScrollListener;
    /**
     * 当前第一个可见的item
     */
    private int firstVisible;
    /**
     * 当前可见的item个数
     */
    private int visibleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        initDatas();
        initListener();
    }

    /**
     * 初始化
     */
    private void initDatas() {
        datas = new ArrayList<>();
        datas.add(videoUrl);
      //  mediaPlayer(videoUrl);
        datas.add(videoUrl1);
        datas.add(videoUrl2);
        datas.add(videoUrl3);
        datas.add(videoUrl4);
        datas.add(videoUrl5);
        datas.add(localUrl);
        adapter = new VideoAdapter(MainActivity.this, datas, R.layout.item_video);
        listView.setAdapter(adapter);
    }

    /**
     * 加载视频第一帧
     */
    private void mediaPlayer(String url) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        //实例化File对象，文件路径为/storage/sdcard/Movies/music1.mp4
        if(!url.isEmpty()){

            media.setDataSource(url);
            Bitmap bitmap = media.getFrameAtTime();
            image = (ImageView)this.findViewById(R.id.player_list_video);

            if(bitmap!=null){
                image.setImageBitmap(bitmap);//设置ImageView显示的图片
            }else{
                //获取视频缩略图失败，弹出消息提示框
                Toast.makeText(MainActivity.this, "获取视频缩略图失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 滑动监听
     */
    private void initListener() {
        onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //滑动停止自动播放视频
                        autoPlayVideo(view);
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisible == firstVisibleItem) {
                    return;
                }

                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;

                //判断是否滚到最后一行
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                   // Toast.makeText(MainActivity.this, "小可爱,已经到了最后一个视频了", Toast.LENGTH_SHORT).show();
                }
            }
        };

        listView.setOnScrollListener(onScrollListener);
    }

    /**
     * 滑动停止自动播放视频
     */
    private void autoPlayVideo(AbsListView view) {

        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.player_list_video) != null) {
                currPlayer = view.getChildAt(i).findViewById(R.id.player_list_video);
                Rect rect = new Rect();
                //获取当前view 的 位置
                    currPlayer.getLocalVisibleRect(rect);
                    int videoheight = currPlayer.getHeight();
                    if (rect.top == 0 && rect.bottom == videoheight) {
                        if (currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                                || currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                            currPlayer.startButton.performClick();
                        }
                        return;
                }
            }
        }
        //释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
