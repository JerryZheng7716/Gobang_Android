package com.emc2.www.gobang.util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
@SuppressLint("Registered")
public class PlayAudio{
    private static PlayAudio playAudio = null;
    private static final String TAG = "Bg_Music";
    private float mLeftVolume;
    private float mRightVolume;
    private Context mContext;
    private MediaPlayer mBackgroundMediaPlayer;
    private String mCurrentPath;

    private PlayAudio(Context context) {
        this.mContext = context;
        initData();
    }

    public static PlayAudio getInstance(Context context) {
        if (playAudio == null) {
            playAudio = new PlayAudio(context);
        }
        return playAudio;
    }

    // 初始化一些数据
    private void initData() {
        mLeftVolume = 0.5f;
        mRightVolume = 0.5f;
        mBackgroundMediaPlayer = null;;
        mCurrentPath = null;
    }

    /**
     * 根据path路径播放背景音乐
     *
     * @param path
     *            :assets中的音频路径
     * @param isLoop
     *            :是否循环播放
     */
    public void play(String path, boolean isLoop) {
        if (mCurrentPath == null) {
            // 这是第一次播放背景音乐--- it is the first time to play background music
            // 或者是执行end()方法后，重新被叫---or end() was called
            mBackgroundMediaPlayer = createMediaplayerFromAssets(path);
            mCurrentPath = path;
        } else {
            if (!mCurrentPath.equals(path)) {
                // 播放一个新的背景音乐--- play new background music
                // 释放旧的资源并生成一个新的----release old resource and create a new one
                if (mBackgroundMediaPlayer != null) {
                    mBackgroundMediaPlayer.release();
                }
                mBackgroundMediaPlayer = createMediaplayerFromAssets(path);
                // 记录这个路径---record the path
                mCurrentPath = path;
            }
        }

        if (mBackgroundMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null");
        } else {
            // 若果音乐正在播放或已近中断，停止它---if the music is playing or paused, stop it
            mBackgroundMediaPlayer.stop();
            mBackgroundMediaPlayer.setLooping(isLoop);
            try {
                mBackgroundMediaPlayer.prepare();
                mBackgroundMediaPlayer.seekTo(0);
                mBackgroundMediaPlayer.start();
            } catch (Exception e) {
                Log.e(TAG, "playBackgroundMusic: error state");
            }
        }
    }

    /**
     * 停止播放音乐
     */
    public void stop() {
        if (mBackgroundMediaPlayer != null) {
            mBackgroundMediaPlayer.stop();
            // should set the state, if not , the following sequence will be
            // error
            // play -> pause -> stop -> resume
        }
    }

    /**
     * create mediaplayer for music
     *
     * @param path
     *            the path relative to assets
     * @return
     */
    private MediaPlayer createMediaplayerFromAssets(String path) {
        MediaPlayer mediaPlayer = null;
        try {
            AssetFileDescriptor assetFileDescritor = mContext.getAssets()
                    .openFd(path);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(),
                    assetFileDescritor.getStartOffset(),
                    assetFileDescritor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(mLeftVolume, mRightVolume);
        } catch (Exception e) {
            mediaPlayer = null;
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
        return mediaPlayer;
    }
}
