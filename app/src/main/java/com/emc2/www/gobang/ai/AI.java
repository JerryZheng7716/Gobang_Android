package com.emc2.www.gobang.ai;

import android.os.Handler;
import android.os.Message;

import com.emc2.www.gobang.view.ChessView;

import java.util.Random;

public class AI {
    private Handler handler;

    ChessView chessView;
    int xPosition; // 临时变量 x的位置
    int yPosition; // 临时变量 y的位置
    int qiPan = 23;
    int[][] chessMap = new int[qiPan][qiPan];//0 1 2 3||19 20 21 22为墙
    public static int xChess = -1, yChess = -1;
    public static int xChess0 = -1, yChess0 = -1, xChess1 = -1, yChess1 = -1, xChess2 = -1, yChess2 = -1, xChess3 = -1, yChess3 = -1, xChess4 = -1, yChess4 = -1;
    public static int score0 = -999999999, score1 = -999999999, score2 = -999999999, score3 = -999999999, score4 = -999999999;
    int score = -999999999;
    int maxSocre = -100000;

    public void Ai(ChessView chessView, int player) {
        this.chessView = chessView;
        handler = chessView.mainActivity.handler;
        int deep = 0;
        long startTime = System.currentTimeMillis();   //获取开始时间
        int level = chessView.getAiLevel(player);
        if (level == 0 || level == 1) {
            deep = 2;
        } else {
            deep = 4;
        }
        AlphaBetaCutBranch alphaBetaCutBranch1 = new AlphaBetaCutBranch(0, deep, player, -999990000, 999990000, 1, chessView);
        AlphaBetaCutBranch alphaBetaCutBranch2 = new AlphaBetaCutBranch(0, deep, player, -999990000, 999990000, 2, chessView);
        AlphaBetaCutBranch alphaBetaCutBranch3 = new AlphaBetaCutBranch(0, deep, player, -999990000, 999990000, 3, chessView);
        AlphaBetaCutBranch alphaBetaCutBranch4 = new AlphaBetaCutBranch(0, deep, player, -999990000, 999990000, 4, chessView);
        Thread thread1 = new Thread(alphaBetaCutBranch1);//启动一个搜索线程
        thread1.start();
        Thread thread2 = new Thread(alphaBetaCutBranch2);//启动一个搜索线程
        thread2.start();
        Thread thread3 = new Thread(alphaBetaCutBranch3);//启动一个搜索线程
        thread3.start();
        Thread thread4 = new Thread(alphaBetaCutBranch4);//启动一个搜索线程
        thread4.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
        //+alphaBetaCutBranch2.jd+alphaBetaCutBranch3.jd+alphaBetaCutBranch4.jd
        System.out.println("总节点数：" + (alphaBetaCutBranch1.jd));
        xChess = xChess1;
        yChess = yChess1;
        score = score1;
        if (score2 >= score) {
            if (score2 == score) {
                Random random = new Random();
                if (random.nextBoolean()) {
                    score = score2;
                    xChess = xChess2;
                    yChess = yChess2;
                }
            } else {
                score = score2;
                xChess = xChess2;
                yChess = yChess2;
            }
        }
        if (score3 >= score) {
            if (score3 == score) {
                Random random = new Random();
                if (random.nextBoolean()) {
                    score = score3;
                    xChess = xChess3;
                    yChess = yChess3;
                }
            } else {
                score = score3;
                xChess = xChess3;
                yChess = yChess3;
            }
        }
        if (score4 >= score) {
            if (score4 == score) {
                Random random = new Random();
                if (random.nextBoolean()) {
                    score = score4;
                    xChess = xChess4;
                    yChess = yChess4;
                }
            } else {
                score = score4;
                xChess = xChess4;
                yChess = yChess4;
            }
        }
        if (score0 >= score) {
            score = score0;
            xChess = xChess0;
            yChess = yChess0;
        }
        if (chessView.mEveryPlay.size() == 0) {
            xChess = 7;
            yChess = 7;
        }
        score0 = -999999999;
        score1 = -999999999;
        score2 = -999999999;
        score3 = -999999999;
        score4 = -999999999;
        if (score < -300000000) {
            //更新ui（工作线程不能更新UI）
            //((Button)view).setText("下载完成");
            //pBar.setVisibility(View.GONE);//GONE不显示也不占空间
            //Message msg=new Message();
            //发消息给主线程通知更新UI
            Message message = handler.obtainMessage(300);
            message.arg1 = 1;
            handler.sendMessage(message);
//            Toast.makeText(gameCanvas.getContext(), "大佬牛逼！大佬！在下认输了！", Toast.LENGTH_SHORT).show();
        }
        System.out.println("当前Ai得分： " + score);
    }

//    private void getMap(){//调试地图
//        for (xPosition=0; xPosition<15;xPosition++) {
//            for (yPosition=0;yPosition<15;yPosition++){
//                chessMap1[xPosition][yPosition]=2;
//            }
//        }
//        for (int i = 0; i < qiPan; i++) {
//            for (int j = 0; j < qiPan; j++) {
//                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 19 || i == 20 || i == 21 || i == 22) {
//                    chessMap[i][j]=4;
//                }else if (j == 0 || j == 1 || j == 2 || j == 3 || j == 19 || j == 20 || j == 21 || j == 22) {
//                    chessMap[i][j]=4;
//                }else
//                    chessMap[i][j]=chessMap1[j-4][i-4];
//                //System.out.print(chessMap[i][j]);
//            }
//            //System.out.println("");
//        }
//                        chessMap[10][10]=1;
//                    chessMap[11][9]=1;
//                chessMap[12][8]=1;
//            chessMap[13][7]=1;
//        chessMap[14][6]=1;
//    }


    private boolean goCutTree(int i, int cutTree[]) {
        if (i == cutTree[i - 4]) {
            if (i == 4) {
                if (i + 1 == cutTree[i - 4 + 1] && i + 2 == cutTree[i - 4 + 2])
                    return true;
            } else if (i == 5) {
                if (i - 1 == cutTree[i - 4 - 1] && i + 1 == cutTree[i - 4 + 1] && i + 2 == cutTree[i - 4 + 2])
                    return true;
            } else if (i == 18) {
                if (i - 1 == cutTree[i - 4 - 1] && i - 2 == cutTree[i - 4 - 2])
                    return true;
            } else if (i == 17) {
                if (i - 1 == cutTree[i - 4 - 1] && i + 1 == cutTree[i - 4 + 1] && i - 2 == cutTree[i - 4 - 2])
                    return true;
            } else {
                if (i - 1 == cutTree[i - 4 - 1] && i + 1 == cutTree[i - 4 + 1] && i - 2 == cutTree[i - 4 - 2] && i + 2 == cutTree[i - 4 + 2])
                    return true;
            }
        }
        return false;
    }


}
