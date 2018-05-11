package com.emc2.www.gobang;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

public class AiTread implements Runnable {
    private ChessView chessView;
    int player;
    Handler handler;
    public AiTread(ChessView chessView,int player) {
        this.chessView = chessView;
        this.player=player;
        this.handler=chessView.mainActivity.handler;


    }


    public void run() {
        AI ai = new AI();
        ai.Ai(chessView,player);
//        chessView.chessCount = 0;
        if (chessView.chessCount == 0) {
            Point point = new Point(7,7);
            chessView.setChessState(point);
            // 记录下每步操作，方便悔棋操作
            chessView.mEveryPlay.add(point);
            chessView.chessCount++;
            chessView.isBlackPlay = !chessView.isBlackPlay;
        }else if (ai.xChess != -1 && ai.yChess != -1) {
            Point point = new Point(ai.xChess,ai.yChess);
            chessView.setChessState(point);
            // 记录下每步操作，方便悔棋操作
            chessView.mEveryPlay.add(point);
            chessView.chessCount++;
//使用Ai算法内的的算法判断是否有人获胜了
            AlphaBetaCutBranch alphaBetaCutBranch = new AlphaBetaCutBranch(0, 2,1, -999990000, 999990000, 1,chessView);
            if (alphaBetaCutBranch.isWin()) {
                Message message = handler.obtainMessage(300);
                message.arg1 = 2;
                handler.sendMessage(message);
            }
            ai.xChess = -1;
            ai.yChess = -1;
            chessView.isBlackPlay = !chessView.isBlackPlay;
            chessView.invalidate();//重新绘制
            PlayAudio playChessSound;
            playChessSound=PlayAudio.getInstance(chessView.getContext());
            playChessSound.play("chess_sound.wav",false);
        }
        chessView.isAiRuning=false;
    }
}