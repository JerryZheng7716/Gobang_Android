package com.emc2.www.gobang.ai;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.HandlerMessage;
import com.emc2.www.gobang.view.ChessView;
import com.emc2.www.gobang.util.PlayAudio;

public class AiTread implements Runnable {
    private ChessView chessView;
    private int player;
    private Handler handler;
    public AiTread(ChessView chessView,int player) {
        this.chessView = chessView;
        this.player=player;
        this.handler=chessView.mainActivity.handler;
        AlphaBetaCutBranch.setRunningFlag(true);
    }


    public void run() {
        if (!AlphaBetaCutBranch.isRunningFlag()){
            return;
        }
        ChessView.isAiRuning =true;
        AI ai = new AI();
        ai.Ai(chessView,player);
//        chessView.chessCount = 0;
        if (chessView.mEveryPlay.size() == 0) {
            Point point = new Point(7,7);
            chessView.setChessState(point);
            // 记录下每步操作，方便悔棋操作
            chessView.mEveryPlay.add(point);
            ChessView.isBlackPlay = !ChessView.isBlackPlay;
        }else if (AI.xChess != -1 && AI.yChess != -1) {
            Point point = new Point(AI.xChess, AI.yChess);
            chessView.setChessState(point);
            // 记录下每步操作，方便悔棋操作
            chessView.mEveryPlay.add(point);
//使用Ai算法内的的算法判断是否有人获胜了
            AlphaBetaCutBranch alphaBetaCutBranch = new AlphaBetaCutBranch(0, 2,1, -999990000, 999990000, 1,chessView);
            if (alphaBetaCutBranch.isWin()) {
                Message message = handler.obtainMessage(300);
                message.arg1 = 2;
                handler.sendMessage(message);
            }
            AI.xChess = -1;
            AI.yChess = -1;
            ChessView.isBlackPlay = !ChessView.isBlackPlay;
            chessView.invalidate();//重新绘制
            PlayAudio playChessSound;
            playChessSound=PlayAudio.getInstance(chessView.getContext());
            playChessSound.play("chess_sound.wav",false);
        }
        ChessView.isAiRuning =false;
        Message message = handler.obtainMessage(300);
        if (player== Chess.BLACK_CHESS)
            message.arg1 = HandlerMessage.JUMP_WHITE;
        else
            message.arg1 = HandlerMessage.JUMP_BLACK;
        handler.sendMessage(message);
    }

}