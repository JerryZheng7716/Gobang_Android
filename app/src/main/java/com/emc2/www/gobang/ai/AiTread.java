package com.emc2.www.gobang.ai;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.HandlerMessage;
import com.emc2.www.gobang.view.ChessView;
import com.emc2.www.gobang.util.PlayAudio;

public class AiTread implements Runnable {
    private ChessView chessView;
    private int player;
    private Handler handler;

    public AiTread(ChessView chessView, int player) {
        this.chessView = chessView;
        this.player = player;
        this.handler = chessView.mainActivity.handler;
        AlphaBetaCutBranch.setRunningFlag(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
        if (!AlphaBetaCutBranch.isRunningFlag()) {
            return;
        }
        ChessView.isAiRuning = true;
        AI ai = new AI();
        ai.Ai(chessView, player);
//        chessView.chessCount = 0;
        if (chessView.mEveryPlay.size() == 0) {
            Point point = new Point(7, 7);
            chessView.setChessState(point);
            HandlerMessage.sendMessage(handler, HandlerMessage.REPAINT_CHESS);
            // 记录下每步操作，方便悔棋操作
            chessView.mEveryPlay.add(point);
            ChessView.isBlackPlay = !ChessView.isBlackPlay;
        } else if (AI.xChess != -1 && AI.yChess != -1 && chessView.mEveryPlay.size() != 225) {
            Point point = new Point(AI.xChess, AI.yChess);
            chessView.setChessState(point);
            HandlerMessage.sendMessage(handler, HandlerMessage.REPAINT_CHESS);
            // 记录下每步操作，方便悔棋操作
            chessView.mEveryPlay.add(point);
//使用Ai算法内的的算法判断是否有人获胜了
            AlphaBetaCutBranch alphaBetaCutBranch = new AlphaBetaCutBranch(0, 2, 1, -999990000, 999990000, 1, chessView);
            if (alphaBetaCutBranch.isWin()) {
                if (ChessView.isBlackPlay) {
                    HandlerMessage.sendMessage(handler, HandlerMessage.SHOW_WIN_DIALOG_BLACK);
                } else {
                    HandlerMessage.sendMessage(handler, HandlerMessage.SHOW_WIN_DIALOG_WHITE);
                }
            } else if (chessView.mEveryPlay.size() == 225) {
                Message message = handler.obtainMessage(300);
                message.arg1 = HandlerMessage.SHOW_DRAW_DIALOG;
                handler.sendMessage(message);
            }
            AI.xChess = -1;
            AI.yChess = -1;
            ChessView.isBlackPlay = !ChessView.isBlackPlay;
            HandlerMessage.sendMessage(handler, HandlerMessage.REPAINT_CHESS);//重新绘制
            PlayAudio playChessSound;
            playChessSound = PlayAudio.getChessAudioInstance(chessView.getContext());
            if (MainActivity.isSoundOpen)
                playChessSound.play("chess_sound.wav", false);
        }
        ChessView.isAiRuning = false;
        Message message = handler.obtainMessage(300);
        if (player == Chess.BLACK_CHESS)
            message.arg1 = HandlerMessage.JUMP_WHITE;
        else
            message.arg1 = HandlerMessage.JUMP_BLACK;
        handler.sendMessage(message);
    }


}