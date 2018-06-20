package com.emc2.www.gobang.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.ai.AiTread;
import com.emc2.www.gobang.ai.AlphaBetaCutBranch;
import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.HandlerMessage;
import com.emc2.www.gobang.util.PlayAudio;
import com.emc2.www.gobang.view.ChessView;
import com.emc2.www.gobang.view.GameNotesDialog;
import com.emc2.www.gobang.view.GiveUpDialog;
import com.emc2.www.gobang.view.ModelDialog;

import java.io.InputStream;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    ImageView btnRestart, btnBackMove, btnAiHelp, btnSound, btnMusic, btnGiveUp;
    public boolean isBlackAi = false, isWhiteAi = false;
    boolean isMusicOpen = false;
    public static boolean isSoundOpen = true;
    public int levelBlackAi = -1, levelWhiteAi = -1;
    private ModelDialog modelDialog;
    ImageView imageViewWhiteChess, imageViewBlackChess;
    Window window;
    PlayAudio playBtnSound, playBackgroundMusic, playChessSound;
    public ChessView chessView;
    AnimationDrawable animationDrawableWhite;
    AnimationDrawable animationDrawableBlack;
    LinearLayout playerLayout;
    LinearLayout btnLayout;
    ImageView background;
    ImageView chessBoard;
    Toolbar toolbar;
    private GameNotesDialog gameNotesDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        findView();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.arg1) {
                case HandlerMessage.DEFEAT:
                    Toast.makeText(MainActivity.this, "大佬牛逼！大佬！在下认输了！", Toast.LENGTH_SHORT).show();
                    break;
                case HandlerMessage.SHOW_WIN_DIALOG:
                    showWinDialog(ChessView.isBlackPlay);
                    break;
                case HandlerMessage.JUMP_BLACK:
                    doJumpAnimation(Chess.BLACK_CHESS);
                    break;
                case HandlerMessage.JUMP_WHITE:
                    doJumpAnimation(Chess.WHITE_CHESS);
                    break;
                case HandlerMessage.SHOW_DRAW_DIALOG:
                    showDrawDialog();
                    break;
            }

        }
    };


    @SuppressLint({"WrongViewCast", "CutPasteId"})
    private void init() {
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        setBarColor();
//        chessBoard.setImageBitmap(readBitMap(R.drawable.ic_chessbord));
        imageViewWhiteChess.setImageResource(R.drawable.wihte_chess_jump);
        animationDrawableWhite = (AnimationDrawable) imageViewWhiteChess.getDrawable();
        imageViewBlackChess.setImageResource(R.drawable.black_chess_jump);
        animationDrawableBlack = (AnimationDrawable) imageViewBlackChess.getDrawable();
        if (!checkDeviceHasNavigationBar(this)) {//适配没有虚拟按键的设备
            playerLayout.setPadding(0, 20, 0, 10);
            chessBoard.setPadding(0, 20, 0, 0);
            btnLayout.setPadding(0, 60, 0, 0);
        }
        playBackgroundMusic = PlayAudio.getMusicInstance(this);
        playChessSound = PlayAudio.getChessAudioInstance(this);
        playBtnSound = PlayAudio.getButtonAudioInstance(this);
        clickBtn();
        doJumpAnimation(Chess.BLACK_CHESS);
    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
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

    /**
     * 获取是否存在虚拟按钮
     *
     * @param context
     * @return
     */
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

    /**
     * 设置状态栏的颜色
     */
    public void setBarColor() {
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(getResources().getColor(R.color.button_background));
        decorViewGroup.addView(statusBarView);
    }

    /**
     * 设置状态栏的高度
     */
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
                modelDialog = new ModelDialog(this);
                modelDialog.getModelDialog();
                break;
            case R.id.menu_games_notes:
                gameNotesDialog = new GameNotesDialog(this);
                gameNotesDialog.getModelDialog();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findView() {
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.icon_menu));
        btnRestart = findViewById(R.id.btn_restart);
        btnBackMove = findViewById(R.id.btn_move_back);
        btnAiHelp = findViewById(R.id.btn_ai_help);
        btnSound = findViewById(R.id.btn_sound);
        btnMusic = findViewById(R.id.btn_music);
        btnGiveUp = findViewById(R.id.btn_give_up);
        btnRestart.setImageBitmap(readBitMap(R.drawable.btn_restart));
        btnBackMove.setImageBitmap(readBitMap(R.drawable.btn_backmove));
        btnAiHelp.setImageBitmap(readBitMap(R.drawable.btn_aihelp));
        btnSound.setImageBitmap(readBitMap(R.drawable.btn_closesound));
        btnMusic.setImageBitmap(readBitMap(R.drawable.btn_openmusic));
        btnGiveUp.setImageBitmap(readBitMap(R.drawable.btn_giveup));
        imageViewWhiteChess = findViewById(R.id.chess_white);
        imageViewBlackChess = findViewById(R.id.chess_black);
        background = findViewById(R.id.background);
        background.setImageBitmap(readBitMap(R.drawable.old_paper));
        chessBoard = findViewById(R.id.chess_board);
        playerLayout = findViewById(R.id.player_layout);
        btnLayout = findViewById(R.id.btn_layout);
        chessView = findViewById(R.id.chessView);
    }

    /**
     * 创建各类按钮的监听
     * 按压样式变化
     * 绑定各自功能
     */
    @SuppressLint("ClickableViewAccessibility")
    public void clickBtn() {
        /*
          重新开始
         */
        btnRestart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        btnRestart.setImageBitmap(readBitMap(R.drawable.btn_restart_release));
                        chessView.resetChessBoard();
                        ChessView.isAiRuning = false;
                        AlphaBetaCutBranch.setRunningFlag(false);
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        btnRestart.setImageBitmap(readBitMap(R.drawable.btn_restart_press));
                        if (isSoundOpen) {
                            playBtnSound.play("button_sound.wav", false);
                        }
                        AlphaBetaCutBranch.setRunningFlag(false);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        /*
          Ai辅助按钮
         */
        btnAiHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        btnAiHelp.setImageBitmap(readBitMap(R.drawable.btn_aihelp_release));
                        AlphaBetaCutBranch.setRunningFlag(false);
                        AiTread aiTread;
                        if (ChessView.isAiRuning)
                            return false;
                        int oldLevelBlack;
                        int oldLevelWhite;
                        oldLevelBlack = levelBlackAi;
                        oldLevelWhite = levelWhiteAi;
                        levelBlackAi = 2;//将ai等级设为高级
                        levelWhiteAi = 2;
                        if (ChessView.isBlackPlay)
                            aiTread = new AiTread(chessView, Chess.BLACK_CHESS);//启动黑棋AI
                        else
                            aiTread = new AiTread(chessView, Chess.WHITE_CHESS);
                        Thread thread = new Thread(aiTread);//启动AI
                        thread.start();
                        levelBlackAi = oldLevelBlack;//还原ai等级
                        levelWhiteAi = oldLevelWhite;
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        btnAiHelp.setImageBitmap(readBitMap(R.drawable.btn_aihelp_press));
                        if (isSoundOpen) {
                            playBtnSound.play("button_sound.wav", false);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        /*
          悔棋按钮
         */
        btnBackMove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
//                        btnBackMove.setImageBitmap(readBitMap(R.drawable.btn_backmove_release));
                        chessView.retract();
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        btnBackMove.setImageBitmap(readBitMap(R.drawable.btn_backmove_press));
                        if (isSoundOpen) {
                            playBtnSound.play("button_sound.wav", false);
                        }
                        AlphaBetaCutBranch.setRunningFlag(false);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        /*
          认输按钮
         */
        btnGiveUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        GiveUpDialog giveUpDialog = new GiveUpDialog(MainActivity.this);
                        giveUpDialog.getModelDialog();
//                        btnGiveUp.setImageBitmap(readBitMap(R.drawable.btn_giveup_release));
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
//                        btnGiveUp.setImageBitmap(readBitMap(R.drawable.btn_giveup_press));
                        if (isSoundOpen) {
                            playBtnSound.play("button_sound.wav", false);
                        }
                        AlphaBetaCutBranch.setRunningFlag(false);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        /*
          音乐按钮
         */
        btnMusic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        if (isMusicOpen) {
                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_openmusic));
                            playBackgroundMusic.stop();
                        } else {
                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_closemusic));
                            playBackgroundMusic.play("back_music.wav", true);
                        }
                        isMusicOpen = !isMusicOpen;
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        if (isSoundOpen) {
                            playBtnSound.play("button_sound.wav", false);
                        }
//                        if (!isMusicOpen)
//                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_closemusic_press));
//                        else
//                            btnMusic.setImageBitmap(readBitMap(R.drawable.btn_openmusic_press));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        /*
          音效按钮
         */
        btnSound.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP://松开事件发生后执行代码的区域
                        if (!isSoundOpen) {
                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_opensound));
                        } else {
                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_closesound));
                        }
                        break;
                    case MotionEvent.ACTION_DOWN://按住事件发生后执行代码的区域
                        isSoundOpen = !isSoundOpen;
                        if (isSoundOpen) {
                            playBtnSound.play("button_sound.wav", false);
                        }
//                        if (!isSoundOpen)
//                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_closesound_press));
//                        else
//                            btnSound.setImageBitmap(readBitMap(R.drawable.btn_opensound_press));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 让图片跳起来
     *
     * @param who 0=黑棋跳，1=白棋跳
     */
    public void doJumpAnimation(int who) {

        if (who == Chess.BLACK_CHESS) {
            imageViewBlackChess.setImageResource(R.drawable.black_chess_jump);
            animationDrawableBlack = (AnimationDrawable) imageViewBlackChess.getDrawable();
            animationDrawableBlack.start();
            animationDrawableWhite.stop();
            imageViewBlackChess.setPadding(0, 0, 0, 0);
            imageViewWhiteChess.setPadding(0, 80, 0, 0);
            imageViewWhiteChess.setImageBitmap(readBitMap(R.drawable.white_chess));
        }
        if (who == Chess.WHITE_CHESS) {
            imageViewWhiteChess.setImageResource(R.drawable.wihte_chess_jump);
            animationDrawableWhite = (AnimationDrawable) imageViewWhiteChess.getDrawable();
            animationDrawableWhite.start();
            animationDrawableBlack.stop();
            imageViewBlackChess.setPadding(0, 80, 0, 0);
            imageViewWhiteChess.setPadding(0, 0, 0, 0);
            imageViewBlackChess.setImageBitmap(readBitMap(R.drawable.black_chess));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    /**
     * 获取ai的棋力，和此颜色是否ai持有
     *
     * @param who 0黑色，1白色
     * @return int -1表示此颜色非Ai持有，0初级。1中级，2高级, -2没有这个颜色
     */
    public int getAiLevel(int who) {
        if (who == Chess.BLACK_CHESS) {
            if (isBlackAi) {
                return levelBlackAi;
            } else return -1;
        } else if (who == Chess.WHITE_CHESS) {
            if (isWhiteAi) {
                return levelWhiteAi;
            } else return -1;
        } else return -2;
    }

    /**
     * 游戏结束，显示对话框
     */
    public void showWinDialog(boolean isBlackWin) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("游戏结束");
        if (isBlackWin) {
            builder.setMessage("恭喜！黑方获胜！！！");
        } else {
            builder.setMessage("恭喜！白方获胜！！！");
        }
        builder.setCancelable(false);
        builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chessView.resetChessBoard();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("返回查看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chessView.isLocked = true;
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 游戏结束，显示对话框
     */
    public void showDrawDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("和棋");
        builder.setMessage("哦我的汤姆森·陈独秀先生，你们居然走出了和棋！！");
        builder.setCancelable(false);
        builder.setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chessView.resetChessBoard();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("返回查看", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chessView.isLocked = true;
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
