package com.yaninfo.jcvideoplayer.Adapter;

import android.content.Context;

import com.yaninfo.jcvideoplayer.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 适配器
 *
 * @author zhangyan
 */
public class VideoAdapter extends CommonAdapter<String> {

    private Context mContext;

    /**
     * 构造函数
     *
     * @param context
     * @param datas
     * @param layoutId
     */
    public VideoAdapter(Context context, List<String> datas, int layoutId) {
        super(context, layoutId, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(ViewHolder viewHolder, String url, int position) {
        JCVideoPlayerStandard player = viewHolder.getView(R.id.player_list_video);
        if (player != null) {
            player.release();
        }
        player.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "爸爸去买几个橘子就回");

    }

}
