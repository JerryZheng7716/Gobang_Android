package com.emc2.www.gobang.view;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.CustomDialog;

public class GiveUpDialog {
    private MainActivity mainActivity;
    private CustomDialog dialog;
    private WinDialog winDialog;

    public GiveUpDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void getModelDialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.ok_btn:
                        winDialog = new WinDialog(mainActivity, mainActivity.chessView);
                        winDialog.getWinlDialog(ChessView.isBlackPlay);
                        ModelDialog.aiFightFlag=false;
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(mainActivity);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(30)
                .widthDimenRes(70)
                .cancelTouchout(false)
                .view(R.layout.give_up_dialog)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
    }
}
