package com.yaninfo.jcvideoplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest extends Activity {

    private String url = "http://112.253.22.157/17/z/z/y/u/" +
            "zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv";
    private ImageView mImageView;
    private Bitmap mBitmap;

    @Test
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        init();
    }


    private void init() {
        mImageView = findViewById(R.id.image);
        Bitmap bitmap = getNetVideoBitmap(url);
        mImageView.setImageBitmap(bitmap);
    }
    /** 获取视频第一帧 */
    public Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取第一帧
            retriever.setDataSource(videoUrl, new HashMap());
            //获取本地视频的第一帧
            retriever.setDataSource("/sdcard/03.mp4");
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
            //视频第一帧的压缩
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(new File(getExternalCacheDir().getAbsolutePath() + "/" + "视频" + ".jpg"));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, outStream);
            outStream.close();
        } catch
                (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            retriever.release();
        } return bitmap;
    }
}
