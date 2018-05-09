package com.emc2.www.gobang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.InputStream;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    public static final int BLACK_CHESS=0,WHITE_CHESS=1;
    ImageView btnRestart, btnBackMove, btnAiHelp, btnSound, btnMusic, btnGiveUp;
    public boolean isBlackAi = false, isWhiteAi = false;
    boolean isMusicOpen = false;
    boolean isSoundOpen = true;
    public int levelBlackAi=-1,levelWhiteAi=-1;
    ImageView imageViewWhiteChess, imageViewBlackChess;
    Window window;
    PlayAudio playBtnSound,playBackgroundMusic,playChessSound;
    private ChessView chessView;
    AnimationDrawable animationDrawableWhite ;
    AnimationDrawable animationDrawableBlack ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        init();
        clickBtn();
        doJumpAnimation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @SuppressLint({"WrongViewCast", "CutPasteId"})
    private void init() {
        setBarColor();
        btnRestart = findViewById(R.id.btn_restart);
        btnBackMove = findViewById(R.id.btn_move_back);
        btnAiHelp = findViewById(R.id.btn_ai_help);
        btnSound = findViewById(R.id.btn_sound);
        btnMusic = findViewById(R.id.btn_music);
        btnGiveUp = findViewById(R.id.btn_give_up);
        btnRestart.setImageBitmap(readBitMap(R.drawable.btn_restart_release));
        btnBackMove.setImageBitmap(readBitMap(R.drawable.btn_backmove_release));
        btnAiHelp.setImageBitmap(readBitMap(R.drawable.btn_aihelp_release));
        btnSound.setImageBitmap(readBitMap(R.drawable.btn_closesound_release));
        btnMusic.setImageBitmap(readBitMap(R.drawable.btn_openmusic_release));
        btnGiveUp.setImageBitmap(readBitMap(R.drawable.btn_giveup_release));
        imageViewWhiteChess = findViewById(R.id.chess_white);
        imageViewBlackChess = findViewById(R.id.chess_black);
        ImageView backround = findViewById(R.id.background);
        backround.setImageBitmap(readBitMap(R.drawable.background));
        ImageView chessBoard = findViewById(R.id.chess_board);
        chessBoard.setImageBitmap(readBitMap(R.drawable.chess_borad));
        LinearLayout playerLayout = findViewById(R.id.player_layout);
        LinearLayout btnLayout = findViewById(R.id.btn_layout);
        chessView = (ChessView) findViewById(R.id.chessView);

        imageViewWhiteChess.setImageResource(R.drawable.wihte_chess_jump);
        animationDrawableWhite = (AnimationDrawable) imageViewWhiteChess.getDrawable();
        imageViewBlackChess.setImageResource(R.drawable.black_chess_jump);
        animationDrawableBlack = (AnimationDrawable) imageViewBlackChess.getDrawable();
        if (!checkDeviceHasNavigationBar(this)) {//适配没有虚拟按键的设备
            playerLayout.setPadding(0, 20, 0, 10);
            chessBoard.setPadding(0, 20, 0, 0);
            btnLayout.setPadding(0, 60, 0, 0);
        }
        playBackgroundMusic = PlayAudio.getInstance(this);
        playChessSound=PlayAudio.getInstance(this);
        playBtnSound=PlayAudio.getInstance(this);
    }

    /**
     * 创建各类按钮的监听
     * 按压样式变化
     * 绑定各自功能
     */
    @SuppressLint("ClickableViewAccessibility")
    public void clickBtn() {
        btnRestart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        btnRestart.setImageBitmap(readBitMap(R.drawable.btn_restart_release));
                        chessView.resetChessBoard();
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        btnRestart.setImageBitmap(readBitMap(R.drawable.btn_restart_press));
                        if (isSoundOpen){
                            playBtnSound.play("button_sound.wav",false);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        btnAiHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        btnAiHelp.setImageBitmap(readBitMap(R.drawable.btn_aihelp_release));
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        btnAiHelp.setImageBitmap(readBitMap(R.drawable.btn_aihelp_press));
                        if (isSoundOpen){
                            playBtnSound.play("button_sound.wav",false);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        btnBackMove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        btnBackMove.setImageBitmap(readBitMap(R.drawable.btn_backmove_release));
                        chessView.retract();
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        btnBackMove.setImageBitmap(readBitMap(R.drawable.btn_backmove_press));
                        if (isSoundOpen){
                            playBtnSound.play("button_sound.wav",false);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        btnGiveUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        btnGiveUp.setImageBitmap(readBitMap(R.drawable.btn_giveup_release));
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        btnGiveUp.setImageBitmap(readBitMap(R.drawable.btn_giveup_press));
                        if (isSoundOpen){
                            playBtnSound.play("button_sound.wav",false);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        btnMusic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        if (isMusicOpen){
                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_openmusic_release));
                            playBackgroundMusic.stop();
                        }
                        else{
                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_closemusic_release));
                            playBackgroundMusic.play("back_music.wav",true);
                        }
                        isMusicOpen = !isMusicOpen;
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        if (isSoundOpen){
                            playBtnSound.play("button_sound.wav",false);
                        }
                        if (!isMusicOpen)
                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_closemusic_press));
                        else
                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_openmusic_press));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        btnSound.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        if (isSoundOpen){
                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_opensound_release));
                        }
                        else{
                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_closesound_release));
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        if (!isSoundOpen){
                            playBtnSound.play("button_sound.wav",false);
                        }
                        if (!isSoundOpen)
                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_closesound_press));
                        else
                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_opensound_press));
                        isSoundOpen = !isSoundOpen;
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId
     * @return Bitmap
     */
    public Bitmap readBitMap(int resId) {
        Context context;
        context = this;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //  获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public void setBarColor() {
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.parseColor("#4a61f5"));
        decorViewGroup.addView(statusBarView);
    }

    private static int getStatusBarHeight(Context context) {//获取状态栏高度
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_model:
                ModelDialog modelDialog = new ModelDialog(this);
                modelDialog.getModelDialog();
                break;
            case R.id.menu_games_notes:

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 让图片跳起来
     *
     * @param who 0=黑棋跳，1=白棋跳
     */

    public void doJumpAnimation(int who) {



        if (who == BLACK_CHESS) {
            imageViewBlackChess.setImageResource(R.drawable.black_chess_jump);
            animationDrawableBlack = (AnimationDrawable) imageViewBlackChess.getDrawable();
            animationDrawableBlack.start();
            animationDrawableWhite.stop();
            imageViewBlackChess.setPadding(0,0,0,0);
            imageViewWhiteChess.setPadding(0,80,0,0);
            imageViewWhiteChess.setImageBitmap(readBitMap(R.drawable.white_chess));
        }
        if (who == WHITE_CHESS) {
            imageViewWhiteChess.setImageResource(R.drawable.wihte_chess_jump);
            animationDrawableWhite = (AnimationDrawable) imageViewWhiteChess.getDrawable();
            animationDrawableWhite.start();
            animationDrawableBlack.stop();
            imageViewBlackChess.setPadding(0,80,0,0);
            imageViewWhiteChess.setPadding(0,0,0,0);
            imageViewBlackChess.setImageBitmap(readBitMap(R.drawable.black_chess));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }
        return false;
    }
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 获取ai的棋力，和此颜色是否ai持有
     *
     * @param who 0黑色，1白色
     * @return int -1表示此颜色费Ai持有，0初级。1中级，2高级, -2没有这个颜色
     */
    public int getAiLevel(int who){
        if(who==BLACK_CHESS){
            if (isBlackAi){
                return levelBlackAi;
            }
            else return -1;
        }
        else if(who==WHITE_CHESS){
            if (isWhiteAi){
                return levelWhiteAi;
            }
            else return -1;
        }
        else return -2;
    }
}
