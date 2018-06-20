package com.emc2.www.gobang.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.ai.AiTread;
import com.emc2.www.gobang.ai.AlphaBetaCutBranch;
import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.CustomDialog;

/**
 * Created by 74011 on 2018/5/1.
 */

public class ModelDialog {
    private RadioButton radioAiBlack, radioHumanBlack, radioAiWhite, radioHumanWhite;
    private TextView textViewWhiteLevel, textViewBlackLevel;
    private Spinner spinnerBlack, spinnerWhite;
    private MainActivity mainActivity;
    private ImageView imageViewBlackPlayer, imageViewWhitePlayer;
    public Thread thread;
    private CustomDialog dialog;
    public static boolean aiFightFlag = true;

    public ModelDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void findView(CustomDialog view) {
        radioAiBlack = view.findViewById(R.id.radio_ai_black);
        radioHumanBlack = view.findViewById(R.id.radio_human_black);
        radioAiWhite = view.findViewById(R.id.radio_ai_white);
        radioHumanWhite = view.findViewById(R.id.radio_human_white);
        textViewBlackLevel = view.findViewById(R.id.black_level_text);
        textViewWhiteLevel = view.findViewById(R.id.white_level_text);
        spinnerBlack = view.findViewById(R.id.black_level);
        spinnerWhite = view.findViewById(R.id.white_level);
        imageViewBlackPlayer = mainActivity.findViewById(R.id.image_player_black);
        imageViewWhitePlayer = mainActivity.findViewById(R.id.image_player_white);
    }

    private void clickListener() {
        radioAiBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioAiBlack.isChecked()) {
                    textViewBlackLevel.setVisibility(View.VISIBLE);
                    spinnerBlack.setVisibility(View.VISIBLE);
                    mainActivity.isBlackAi = true;
                }
            }
        });

        radioAiWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioAiWhite.isChecked()) {
                    textViewWhiteLevel.setVisibility(View.VISIBLE);
                    spinnerWhite.setVisibility(View.VISIBLE);
                    mainActivity.isWhiteAi = true;
                }
            }
        });

        radioHumanBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!radioAiBlack.isChecked()) {
                    textViewBlackLevel.setVisibility(View.INVISIBLE);
                    spinnerBlack.setVisibility(View.INVISIBLE);
                    mainActivity.isBlackAi = false;
                }
            }
        });

        radioHumanWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!radioAiWhite.isChecked()) {
                    textViewWhiteLevel.setVisibility(View.INVISIBLE);
                    spinnerWhite.setVisibility(View.INVISIBLE);
                    mainActivity.isWhiteAi = false;
                }
            }
        });
    }

    /**
     * 显示dialog
     *
     * @return dialog
     */
    public void getModelDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
//        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
//// 设置我们自己定义的布局文件作为弹出框的Content
//        builder.setView(view);
//        builder.setIcon(R.drawable.set_model);//设置标题图标
//        if (mainActivity.isBlackAi) {
//            radioAiBlack.setChecked(true);
//        }
//        if (mainActivity.isWhiteAi) {
//            radioAiWhite.setChecked(true);
//        }
//        if (!mainActivity.isBlackAi) {
//            radioHumanBlack.setChecked(true);
//        }
//        if (!mainActivity.isWhiteAi) {
//            radioHumanWhite.setChecked(true);
//        }
//        builder.setTitle(R.string.set_model);//设置标题内容
//        if (radioAiBlack.isChecked()) {
//            textViewBlackLevel.setVisibility(View.VISIBLE);
//            spinnerBlack.setVisibility(View.VISIBLE);
//            mainActivity.isBlackAi = true;
//        }
//        if (radioAiWhite.isChecked()) {
//            textViewWhiteLevel.setVisibility(View.VISIBLE);
//            spinnerWhite.setVisibility(View.VISIBLE);
//            mainActivity.isWhiteAi = true;
//        }
//        //确认按钮
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                if (mainActivity.isBlackAi) {
//                    imageViewBlackPlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.left_robot));
//                }
//                if (mainActivity.isWhiteAi) {
//                    imageViewWhitePlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.right_robot));
//                }
//                if (!mainActivity.isBlackAi) {
//                    imageViewBlackPlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.left_people));
//                }
//                if (!mainActivity.isWhiteAi) {
//                    imageViewWhitePlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.right_people));
//                }
//                mainActivity.levelBlackAi = spinnerBlack.getSelectedItemPosition();
//                mainActivity.levelWhiteAi = spinnerWhite.getSelectedItemPosition();
//                Toast.makeText(mainActivity, "当前黑色AI级别:" + mainActivity.getAiLevel(Chess.BLACK_CHESS)
//                        + "，" + "当前白色AI级别:" + mainActivity.getAiLevel(Chess.WHITE_CHESS), Toast.LENGTH_SHORT).show();
//                if (mainActivity.getAiLevel(Chess.BLACK_CHESS) != -1 && mainActivity.getAiLevel(Chess.WHITE_CHESS) != -1) {
//                    aiFightFlag = true;
//                    ChessView.isAiRuning = true;
//                    int who;
//                    who = ChessView.isBlackPlay? Chess.BLACK_CHESS : Chess.WHITE_CHESS;
//                    AiFightThread aiFightThread = new AiFightThread(who);//启动黑棋AI
//                    thread = new Thread(aiFightThread);//启动AI
//                    thread.start();
//                } else
//                    aiFightFlag = false;
//                if (ChessView.isBlackPlay && mainActivity.getAiLevel(Chess.BLACK_CHESS) != -1 && !ChessView.isAiRuning && !aiFightFlag) {
//                    //如果现在是黑棋回合，且黑棋是机器人持有，且没有ai线程在运行
//                    runAi(Chess.BLACK_CHESS);
//                }
//                if (!ChessView.isBlackPlay && mainActivity.getAiLevel(Chess.WHITE_CHESS) != -1 && !ChessView.isAiRuning && !aiFightFlag) {
//                    //如果现在是白棋回合，且白棋是机器人持有，且没有ai线程在运行
//                    runAi(Chess.WHITE_CHESS);
//                }
//            }
//        });
//        //取消
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        dlg = builder.create();
//        dlg.show();
//        setDialogWidth();
        add_dialog();
    }

    private void add_dialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        if (mainActivity.isBlackAi) {
                            imageViewBlackPlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.left_robot));
                        }
                        if (mainActivity.isWhiteAi) {
                            imageViewWhitePlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.right_robot));
                        }
                        if (!mainActivity.isBlackAi) {
                            imageViewBlackPlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.girl));
                        }
                        if (!mainActivity.isWhiteAi) {
                            imageViewWhitePlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.boy));
                        }
                        mainActivity.levelBlackAi = spinnerBlack.getSelectedItemPosition();
                        mainActivity.levelWhiteAi = spinnerWhite.getSelectedItemPosition();
                        Toast.makeText(mainActivity, "当前黑色AI级别:" + mainActivity.getAiLevel(Chess.BLACK_CHESS)
                                + "，" + "当前白色AI级别:" + mainActivity.getAiLevel(Chess.WHITE_CHESS), Toast.LENGTH_SHORT).show();
                        if (mainActivity.getAiLevel(Chess.BLACK_CHESS) != -1 && mainActivity.getAiLevel(Chess.WHITE_CHESS) != -1) {
                            aiFightFlag = true;
                            ChessView.isAiRuning = true;
                            int who;
                            who = ChessView.isBlackPlay ? Chess.BLACK_CHESS : Chess.WHITE_CHESS;
                            AiFightThread aiFightThread = new AiFightThread(who);//启动黑棋AI
                            thread = new Thread(aiFightThread);//启动AI
                            thread.start();
                        } else
                            aiFightFlag = false;
                        if (ChessView.isBlackPlay && mainActivity.getAiLevel(Chess.BLACK_CHESS) != -1 && !ChessView.isAiRuning && !aiFightFlag) {
                            //如果现在是黑棋回合，且黑棋是机器人持有，且没有ai线程在运行
                            runAi(Chess.BLACK_CHESS);
                        }
                        if (!ChessView.isBlackPlay && mainActivity.getAiLevel(Chess.WHITE_CHESS) != -1 && !ChessView.isAiRuning && !aiFightFlag) {
                            //如果现在是白棋回合，且白棋是机器人持有，且没有ai线程在运行
                            runAi(Chess.WHITE_CHESS);
                        }
                        if (radioAiBlack.isChecked()) {
                            textViewBlackLevel.setVisibility(View.VISIBLE);
                            spinnerBlack.setVisibility(View.VISIBLE);
                            mainActivity.isBlackAi = true;
                        }
                        if (radioAiWhite.isChecked()) {
                            textViewWhiteLevel.setVisibility(View.VISIBLE);
                            spinnerWhite.setVisibility(View.VISIBLE);
                            mainActivity.isWhiteAi = true;
                        }
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(mainActivity);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(1100)
                .widthDimenRes(700)
                .cancelTouchout(false)
                .view(R.layout.model_dialog)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
        findView(dialog);
        if (mainActivity.isBlackAi) {
            radioAiBlack.setChecked(true);
        }
        if (mainActivity.isWhiteAi) {
            radioAiWhite.setChecked(true);
        }
        if (!mainActivity.isBlackAi) {
            radioHumanBlack.setChecked(true);
            textViewBlackLevel.setVisibility(View.INVISIBLE);
            spinnerBlack.setVisibility(View.INVISIBLE);
        }
        if (!mainActivity.isWhiteAi) {
            radioHumanWhite.setChecked(true);
            textViewWhiteLevel.setVisibility(View.INVISIBLE);
            spinnerWhite.setVisibility(View.INVISIBLE);
        }
        int levelBlack=mainActivity.getAiLevel(Chess.BLACK_CHESS);
        int levelWhite=mainActivity.getAiLevel(Chess.WHITE_CHESS);
        spinnerBlack.setSelection(levelBlack);
        spinnerWhite.setSelection(levelWhite);
        clickListener();
    }

//    /**
//     * 设置Dialog的高度
//     */
//    private void setDialogWidth() {
//        WindowManager m = mainActivity.getWindowManager();
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//        android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.width = (int) (d.getWidth() * 0.7);    //宽度设置为屏幕的0.7
//        dlg.getWindow().setAttributes(p);     //设置生效
//    }

    /**
     * 设置A对战
     *
     * @param who 轮到哪个颜色落子
     */
    public void aiFight(int who) {
        if (!aiFightFlag)
            return;
        if (who == Chess.BLACK_CHESS) {
            Thread thread = runAi(Chess.BLACK_CHESS);//启动黑棋AI
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //使用Ai算法内的的算法判断是否有人获胜了
            AlphaBetaCutBranch alphaBetaCutBranch = new AlphaBetaCutBranch(0, 2, 1, -999990000, 999990000, 1, mainActivity.chessView);
            if (alphaBetaCutBranch.isWin()||mainActivity.chessView.mEveryPlay.size()==225) {
                return;
            }
            aiFight(Chess.WHITE_CHESS);
        }
        if (who == Chess.WHITE_CHESS) {
            Thread thread = runAi(Chess.WHITE_CHESS);//启动白棋AI
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //使用Ai算法内的的算法判断是否有人获胜了
            AlphaBetaCutBranch alphaBetaCutBranch = new AlphaBetaCutBranch(0, 2, 1, -999990000, 999990000, 1, mainActivity.chessView);
            if (alphaBetaCutBranch.isWin()||mainActivity.chessView.mEveryPlay.size()==225) {
                return;
            }
            aiFight(Chess.BLACK_CHESS);
        }
    }

    /**
     * 创建新的线程，进行ai对战
     */
    class AiFightThread implements Runnable {
        private int who;

        public AiFightThread(int who) {
            this.who = who;
        }

        public void run() {
            if (aiFightFlag)
                aiFight(who);
        }
    }

    private Thread runAi(int who) {
        AiTread aiTread = new AiTread(mainActivity.chessView, who);//启动黑棋AI
        Thread thread = new Thread(aiTread);//启动AI
        thread.start();
        return thread;
    }
}
