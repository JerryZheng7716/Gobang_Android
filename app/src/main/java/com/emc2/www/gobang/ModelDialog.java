package com.emc2.www.gobang;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 74011 on 2018/5/1.
 */

public class ModelDialog {
    private View view;
    private RadioButton radioAiBlack, radioHumanBlack, radioAiWhite, radioHumanWhite;
    private TextView textViewWhiteLevel, textViewBlackLevel;
    private Spinner spinnerBlack, spinnerWhite;
    private MainActivity mainActivity;
    private AlertDialog dlg;
    private ImageView imageViewBlackPlayer, imageViewWhitePlayer;

    public ModelDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        view = LayoutInflater.from(mainActivity).inflate(R.layout.model_dialog, null);
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
        textViewBlackLevel.setVisibility(View.GONE);
        spinnerBlack.setVisibility(View.GONE);
        textViewWhiteLevel.setVisibility(View.GONE);
        spinnerWhite.setVisibility(View.GONE);
        clickListener();
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
                    textViewBlackLevel.setVisibility(View.GONE);
                    spinnerBlack.setVisibility(View.GONE);
                    mainActivity.isBlackAi = false;
                }
            }
        });

        radioHumanWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!radioAiWhite.isChecked()) {
                    textViewWhiteLevel.setVisibility(View.GONE);
                    spinnerWhite.setVisibility(View.GONE);
                    mainActivity.isWhiteAi = false;
                }
            }
        });
    }

    public AlertDialog getModelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
// 设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        builder.setIcon(R.drawable.set_model);//设置标题图标
        if (mainActivity.isBlackAi) {
            radioAiBlack.setChecked(true);
        }
        if (mainActivity.isWhiteAi) {
            radioAiWhite.setChecked(true);
        }
        if (!mainActivity.isBlackAi) {
            radioHumanBlack.setChecked(true);
        }
        if (!mainActivity.isWhiteAi) {
            radioHumanWhite.setChecked(true);
        }
        builder.setTitle(R.string.set_model);//设置标题内容
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
        //确认按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (mainActivity.isBlackAi) {
                    imageViewBlackPlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.left_robot));
                }
                if (mainActivity.isWhiteAi) {
                    imageViewWhitePlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.right_robot));
                }
                if (!mainActivity.isBlackAi) {
                    imageViewBlackPlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.left_people));
                }
                if (!mainActivity.isWhiteAi) {
                    imageViewWhitePlayer.setImageBitmap(mainActivity.readBitMap(R.drawable.right_people));
                }
                mainActivity.levelBlackAi=spinnerBlack.getSelectedItemPosition();
                mainActivity.levelWhiteAi=spinnerWhite.getSelectedItemPosition();
                Toast.makeText(mainActivity, "当前黑色AI级别:"+mainActivity.getAiLevel(MainActivity.BLACK_CHESS)+"，"+"当前白色AI级别:"+mainActivity.getAiLevel(MainActivity.WHITE_CHESS), Toast.LENGTH_SHORT).show();
            }
        });
        //取消
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        dlg = builder.create();
        dlg.show();
        setDialogWidth();
        return dlg;
    }

    private void setDialogWidth() {
        WindowManager m = mainActivity.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dlg.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.7);    //宽度设置为屏幕的0.7
        dlg.getWindow().setAttributes(p);     //设置生效
    }
}
