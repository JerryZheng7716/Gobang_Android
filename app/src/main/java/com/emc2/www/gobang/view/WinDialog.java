package com.emc2.www.gobang.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.util.CustomDialog;

public class WinDialog {
    private Context context;
    private CustomDialog dialog;
    private ChessView chessView;

    public WinDialog(Context context, ChessView chessView) {
        this.context = context;
        this.chessView = chessView;
    }

    public void getWinlDialog(boolean isBlackWin) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.return_and_check:
                        chessView.isLocked = true;
                        dialog.dismiss();
                        break;
                    case R.id.restart:
                        chessView.resetChessBoard();
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(600)
                .widthDimenRes(700)
                .cancelTouchout(false)
                .view(R.layout.win_dialog)
                .addViewOnclick(R.id.return_and_check, listener)
                .addViewOnclick(R.id.restart, listener)
                .build();
        dialog.show();
        TextView textView;
        textView = dialog.findViewById(R.id.win_text);
        if (isBlackWin) {
            textView.setText("恭喜！黑方获胜！！！");
        } else {
            textView.setText("恭喜！白方获胜！！！");
        }
    }
}
