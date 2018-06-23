package com.emc2.www.gobang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.db.RecordDao;
import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.CustomDialog;
import com.emc2.www.gobang.util.FormatDate;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WinDialog {
    private Context context;
    private CustomDialog dialog;
    private ChessView chessView;
    public static final String AI_LEVEL_ONE ="AI(初级)";
    public static final String AI_LEVEL_TWO ="AI(中级)";
    public static final String AI_LEVEL_THREE ="AI(高级)";
    public static final String HUMAN ="人类";

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
                .heightDimenRes(30)
                .widthDimenRes(70)
                .cancelTouchout(false)
                .view(R.layout.win_dialog)
                .addViewOnclick(R.id.return_and_check, listener)
                .addViewOnclick(R.id.restart, listener)
                .build();
        dialog.show();
        TextView textView;
        textView = dialog.findViewById(R.id.win_text);
        Date day=new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time =sdf.format(day);
        MainActivity mainActivity = (MainActivity)context;
        String blackPlayer,whitePlayer;
        switch (mainActivity.getAiLevel(Chess.BLACK_CHESS)){
            case -1:
                blackPlayer=HUMAN;
                break;
            case 0:
                blackPlayer=AI_LEVEL_ONE;
                break;
            case 1:
                blackPlayer=AI_LEVEL_TWO;
                break;
            case 2:
                blackPlayer=AI_LEVEL_THREE;
                break;
                default:
                    blackPlayer="ERROR";
        }

        switch (mainActivity.getAiLevel(Chess.WHITE_CHESS)){
            case -1:
                whitePlayer=HUMAN;
                break;
            case 0:
                whitePlayer=AI_LEVEL_ONE;
                break;
            case 1:
                whitePlayer=AI_LEVEL_TWO;
                break;
            case 2:
                whitePlayer=AI_LEVEL_THREE;
                break;
            default:
                whitePlayer="ERROR";
        }
        String winner;
        if (isBlackWin) {
            textView.setText("恭喜！黑方获胜！！！");
            winner="持黑";
        } else {
            textView.setText("恭喜！白方获胜！！！");
            winner="持白";
        }
        List<Point> mEveryPlay = mainActivity.chessView.mEveryPlay;
        int hand=0;
        Point point;
        StringBuilder chessMap= new StringBuilder();
        while (hand< mEveryPlay.size()) {
            point = mEveryPlay.get(hand);
            Rect mSrcRect, mDestRect;
            int x=point.x;
            int y =point.y;
            chessMap.append(" ").append(x).append(" ").append(y);
            hand++;
        }
        RecordDao.insertRecords(time,blackPlayer,whitePlayer,mEveryPlay.size(),winner,chessMap.toString(),mainActivity);
    }
}
