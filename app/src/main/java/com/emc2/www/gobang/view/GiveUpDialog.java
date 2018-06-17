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
                        mainActivity.showWinDialog(!ChessView.isBlackPlay);
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(mainActivity);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(600)
                .widthDimenRes(700)
                .cancelTouchout(false)
                .view(R.layout.give_up_dialog)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
    }
}
