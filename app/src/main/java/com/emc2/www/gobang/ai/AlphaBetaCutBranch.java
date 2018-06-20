package com.emc2.www.gobang.ai;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.HandlerMessage;
import com.emc2.www.gobang.view.ChessView;
import com.emc2.www.gobang.activity.MainActivity;

import java.util.ArrayList;

public class AlphaBetaCutBranch implements Runnable {
    private ChessView gameCanvas;
    private int xPosition; // 临时变量 x的位置
    private int yPosition; // 临时变量 y的位置
    private int h;
    private int deep;
    private int player;
    private int alpha;
    private int beta;
    private int block;
    private int qiPan = 23;
    private int[][] chessMap = new int[qiPan][qiPan];//0 1 2 3||19 20 21 22为墙
    private int[][] chessMap1 = new int[15][15];
    private int xChess, yChess;
    private int lastScore;
    private static boolean runningFlag = true;
    public SituationAssessment sa;
    private ArrayList<Node> nodes = new ArrayList<>();
    private int[][] calculationPoint;
    private boolean isGetNodes=false;

    public static boolean isRunningFlag() {
        return runningFlag;
    }

    public static void setRunningFlag(boolean runningFlag) {
        AlphaBetaCutBranch.runningFlag = runningFlag;
    }

    int jd = 0;
    int county = 0;

    public AlphaBetaCutBranch(int h, int deep, int player, int alpha, int beta, int block, ChessView gameCanvas) {//block代表四个区块，分别是1，2，3，4
        this.alpha = alpha;
        this.h = h;
        this.deep = deep;
        this.beta = beta;
        this.block = block;
        this.player = player;
        this.gameCanvas = gameCanvas;
        sa = new SituationAssessment(chessMap);
    }

    public void setCalculationPoint(int[][] calculationPoint) {
        this.calculationPoint = calculationPoint;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        getMap();
//        int[][] calculationPoint = getCalculationPoint(chessMap);
//        int length = calculationPoint[0][0];
//        int[] range1 = new int[2], range2 = new int[2], range3 = new int[2], range4 = new int[2];
//        range1[0] = 1;
//        range1[1] = length / 4;
//        range2[0] = range1[1] + 1;
//        range2[1] = length / 2;
//        range3[0] = range2[1] + 1;
//        range3[1] = (range2[1] + 1 + length) / 2;
//        range4[0] = range3[1] + 1;
//        range4[1] = length;
//        range4[1] = length + 4;
//        int[] firstRange = {1, length};
//        alphaBetaCutBranch(0, 2, player, alpha, beta, calculationPoint, firstRange);
//        if (sa.getNextChessIsWin()) {
//            AI.score0 = lastScore;
//            AI.xChess0 = xChess;
//            AI.yChess0 = yChess;
//            sa.setNextChessIsWin(false);
//            return;
//        }
//        calculationPoint[0][0] = length + 4;
//        calculationPoint[length + 1][0] = calculationPoint[range1[0]][0];
//        calculationPoint[length + 1][1] = calculationPoint[range1[0]][1];
//        calculationPoint[length + 2][0] = calculationPoint[range2[0]][0];
//        calculationPoint[length + 2][1] = calculationPoint[range2[0]][1];
//        calculationPoint[length + 3][0] = calculationPoint[range3[0]][0];
//        calculationPoint[length + 3][1] = calculationPoint[range3[0]][1];
//        calculationPoint[length + 4][0] = calculationPoint[range4[0]][0];
//        calculationPoint[length + 4][1] = calculationPoint[range4[0]][1];
//        calculationPoint[range1[0]][0] = yChess + 4;
//        calculationPoint[range1[0]][1] = xChess + 4;
//        calculationPoint[range2[0]][0] = yChess + 4;
//        calculationPoint[range2[0]][1] = xChess + 4;
//        calculationPoint[range3[0]][0] = yChess + 4;
//        calculationPoint[range3[0]][1] = xChess + 4;
//        calculationPoint[range4[0]][0] = yChess + 4;
//        calculationPoint[range4[0]][1] = xChess + 4;

        int length = calculationPoint[0][0];
        int[] rangeAll = {1, length};
        alphaBetaCutBranch(h, deep, player, alpha, beta, calculationPoint, rangeAll);
        if (block == 1) {
            AI.score1 = lastScore;
            AI.xChess1 = xChess;
            AI.yChess1 = yChess;

            gameCanvas.testX1 = 0;
            gameCanvas.testY1 = 0;
        }
        if (block == 2) {
            AI.score2 = lastScore;
            AI.xChess2 = xChess;
            AI.yChess2 = yChess;

            gameCanvas.testX2 = 0;
            gameCanvas.testY2 = 0;
        }
        if (block == 3) {
            AI.score3 = lastScore;
            AI.xChess3 = xChess;
            AI.yChess3 = yChess;

            gameCanvas.testX3 = 0;
            gameCanvas.testY3 = 0;
        }
        if (block == 4) {
            AI.score4 = lastScore;
            AI.xChess4 = xChess;
            AI.yChess4 = yChess;

            gameCanvas.testX4 = 0;
            gameCanvas.testY4 = 0;
        }
        System.out.println("第" + block + "个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
        HandlerMessage.sendMessage(gameCanvas.mainActivity.handler,HandlerMessage.REPAINT_CHESS);//重新绘制
//        gameCanvas.invalidate();//重新绘制

//        if (block == 1) {
//            System.out.println("第一层次一共有" + length + "个节点");
//            alphaBetaCutBranch(h, deep, player, alpha, beta, calculationPoint, range1);
//            AI.score1 = lastScore;
//            AI.xChess1 = xChess;
//            AI.yChess1 = yChess;
//            System.out.println("第一个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
//            gameCanvas.testX1 = 0;
//            gameCanvas.testY1 = 0;
//            gameCanvas.invalidate();//重新绘制
//        }
//        if (block == 2) {
//            alphaBetaCutBranch(h, deep, player, alpha, beta, calculationPoint, range2);
//            AI.score2 = lastScore;
//            AI.xChess2 = xChess;
//            AI.yChess2 = yChess;
//            System.out.println("第二个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
//            gameCanvas.testX2 = 0;
//            gameCanvas.testY2 = 0;
//            gameCanvas.invalidate();//重新绘制
//        }
//        if (block == 3) {
//            alphaBetaCutBranch(h, deep, player, alpha, beta, calculationPoint, range3);
//            AI.score3 = lastScore;
//            AI.xChess3 = xChess;
//            AI.yChess3 = yChess;
//            System.out.println("第三个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
//            gameCanvas.testX3 = 0;
//            gameCanvas.testY3 = 0;
//            gameCanvas.invalidate();//重新绘制
//        }
//        if (block == 4) {
//            alphaBetaCutBranch(h, deep, player, alpha, beta, calculationPoint, range4);
//            AI.score4 = lastScore;
//            AI.xChess4 = xChess;
//            AI.yChess4 = yChess;
//            System.out.println("第四个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
//            gameCanvas.testX4 = 0;
//            gameCanvas.testY4 = 0;
//            gameCanvas.invalidate();//重新绘制
//        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Node> getSortNodes() {
        getMap();
        int[][] calculationPoint = getCalculationPoint(chessMap);
        int length = calculationPoint[0][0];
        int[] firstRange = {1, length};
        isGetNodes=true;
        alphaBetaCutBranch(0, 2, player, alpha, beta, calculationPoint, firstRange);
        isGetNodes=false;
        SortNode.sortNodeByScore(nodes);
        return nodes;
    }

    private int alphaBetaCutBranch(int h, int deep, int player, int alpha, int beta, int[][] calculationPoint, int[] range) { //h搜索深度，player=1表示自己,player=0表示对手,range代表范围，用数组表示，分别是i（行）的开始结束，j（列）的开始结束
        if (!runningFlag){
            return -123456789;
        }
        int p, p2;
        p = sa.evaluation(player,true);
        p2 = sa.evaluation(player ^ 1,false);
        if (h == deep || sa.getIs成五Chess())   //若到达深度 或是出现胜负
        {
            if (sa.getIs成五Chess() && h != 0) {        //若是胜负返回-inf 或+inf
                sa.setIs成五Chess(false);
                if (this.player == player) {
                    return -99990000 / h + p - p2;
                } else
                    return 99990000 / h + p - p2;
            } else {
                return p - p2;   //否则返回此局面的评价值
            }
        }
        int i, j;
        if (player == this.player) {//AI
            for (int k = range[0]; k <= range[1]; k++) {
                i = calculationPoint[k][0];
                j = calculationPoint[k][1];
//                if (h==0&&block==2){
//                    System.out.println("+++"+i+" : "+j );
//                }
                if (chessMap[i][j] == 2) {
                    jd++;
                    chessMap[i][j] = player;
                    int[] nextRange = new int[2];
                    int[][] nextCalculationPoint = getCalculationPoint(chessMap);
                    nextRange[0] = 1;
                    nextRange[1] = nextCalculationPoint[0][0];
                    int ans = alphaBetaCutBranch(h + 1, deep, player ^ 1, alpha, beta, nextCalculationPoint, nextRange);
                    if (isGetNodes) {
                        nodes.add(new Node(ans, i, j));
                    }
//                    if (h==0&&block==2){
//                        System.out.println("ans :"+ans+" : "+calculationPoint[0][0]);
//                    }

                    if (ans > alpha ) {    //通过向上传递的子节点beta值修正alpha值
//                        if (block==2&&h==0){
//                            System.out.println("alpha: "+alpha);
//                        }
                        alpha = ans;
                        if (h == 0) {
                            lastScore = ans;
                            xChess = j - 4;       //记录位置
                            yChess = i - 4;
//                            if (block==2){
//                                System.out.println("线程："+block+" 得到一个理论解----------- "+ans+" x: "+xChess+" y："+yChess);
//                            }
                        }
                        switch (block) {
                            case 1:
                                gameCanvas.testX1 = j - 4;
                                gameCanvas.testY1 = i - 4;
//                                gameCanvas.invalidate();//重新绘制
                                HandlerMessage.sendMessage(gameCanvas.mainActivity.handler,HandlerMessage.REPAINT_CHESS);//重新绘制
                                break;
                            case 2:
                                gameCanvas.testX2 = j - 4;
                                gameCanvas.testY2 = i - 4;
//                                gameCanvas.invalidate();//重新绘制
                                HandlerMessage.sendMessage(gameCanvas.mainActivity.handler,HandlerMessage.REPAINT_CHESS);//重新绘制
                                break;
                            case 3:
                                gameCanvas.testX3 = j - 4;
                                gameCanvas.testY3 = i - 4;
//                                gameCanvas.invalidate();//重新绘制
                                HandlerMessage.sendMessage(gameCanvas.mainActivity.handler,HandlerMessage.REPAINT_CHESS);//重新绘制
                                break;
                            case 4:
                                gameCanvas.testX4 = j - 4;
                                gameCanvas.testY4 = i - 4;
//                                gameCanvas.invalidate();//重新绘制
                                HandlerMessage.sendMessage(gameCanvas.mainActivity.handler,HandlerMessage.REPAINT_CHESS);//重新绘制
                                break;
                        }
                    }
                    chessMap[i][j] = 2;
                    if (alpha >= beta)   //发生 alpha剪枝
                    {
                        return alpha;
                    }
                }
            }
//            for (i = range[0]; i <= range[1]; i++) {
//                for (j = range[2]; j <= range[3]; j++) {
//                    if (chessMap[i][j] == 2) {
//                        chessMap[i][j] = player;
//                        int ans = alphaBetaCutBranch(h + 1, player ^ 1, alpha, beta, getRange(chessMap));
//                        if (ans > alpha) {    //通过向上传递的子节点beta值修正alpha值
//                            alpha = ans;
//                            if (h==0){
//                                lastScore = ans;
//                                xChess = j - 4;       //记录位置
//                                yChess = i - 4;
//                            }
//                        }
//                        chessMap[i][j] = 2;
//                        if (alpha >= beta)   //发生 alpha剪枝
//                        {
//                            return alpha;
//                        }
//                    }
//                }
//            }
            return alpha;
        } else {//对手
            for (int k = range[0]; k <= range[1]; k++) {
                i = calculationPoint[k][0];
                j = calculationPoint[k][1];

                if (chessMap[i][j] == 2) {
                    jd++;
//                    county++;
//                    System.out.println("执行"+county);
                    chessMap[i][j] = player;
                    int[] nextRange = new int[2];
                    int[][] nextCalculationPoint = getCalculationPoint(chessMap);
                    nextRange[0] = 1;
                    nextRange[1] = nextCalculationPoint[0][0];
                    int ans = alphaBetaCutBranch(h + 1, deep, player ^ 1, alpha, beta, nextCalculationPoint, nextRange);
                    chessMap[i][j] = 2;
                    if (ans < beta) {     //通过向上传递的子节点alpha值修正beta值
                        beta = ans;
                    }
                    if (alpha >= beta)   //发生 beta剪枝
                    {
                        return beta;
                    }
                }
            }
            return beta;
        }
    }

    public int[][] getCalculationPoint(int[][] map) {
        int[][] newMap = new int[qiPan][qiPan];
//        for (int i = 0; i < qiPan; i++) {
//            System.arraycopy(map[i], 0, newMap[i], 0, newMap[i].length);
//        }
        int[][] calculationPoint = new int[226][2];
//        int count = 0;
//        for (int i = 4; i < 19; i++) {
//            for (int j = 4; j < 19; j++) {
//                count++;
//                calculationPoint[count][0]=i;
//                calculationPoint[count][1]=j;
//            }
//        }
//        calculationPoint[0][0]=225;
        calculationPoint[0][0] = -1;
        int count = 1;
        for (int i = 18; i >= 4; i--) {
            for (int j = 4; j < 19; j++) {
                if (map[i][j] == 0 || map[i][j] == 1) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (map[k][l] == 2 && newMap[k][l] != 3) {
                                calculationPoint[count][0] = k;//记录有必要进行落子的位置
                                calculationPoint[count][1] = l;
                                calculationPoint[0][0] = count;//记录数组终点;
                                count++;
                                newMap[k][l] = 3;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 4; i < 19; i++) {
            for (int j = 18; j >= 4; j--) {
                if (map[i][j] == 0 || map[i][j] == 1) {
                    for (int k = i - 2; k <= i + 2; k++) {
                        if (k == i - 1 || k == i + 1) {
                            continue;
                        }
                        for (int l = j - 2; l <= j + 2; l++) {
                            if (l == j - 1 || l == j + 1) {
                                continue;
                            }
                            if (map[k][l] == 2 && newMap[k][l] != 3) {
                                calculationPoint[count][0] = k;//记录有必要进行落子的位置
                                calculationPoint[count][1] = l;
                                calculationPoint[0][0] = count;//记录数组终点;
                                count++;
                                newMap[k][l] = 3;
                            }
                        }
                    }
                }
            }
        }
        return calculationPoint;
    }

    public int[] getRange(int[][] map) {
        int[] range = {99, -1, 99, -1};
        for (int i = 4; i < 19; i++) {
            for (int j = 4; j < 19; j++) {
                if (map[i][j] == 0 || map[i][j] == 1) {
                    if (i < range[0])
                        range[0] = i;
                    if (i > range[1])
                        range[1] = i;
                    if (j < range[2])
                        range[2] = j;
                    if (j > range[3])
                        range[3] = j;
                }
            }
        }
        range[0] -= 2;
        range[1] += 2;
        range[2] -= 2;
        range[3] += 2;
        if (range[0] < 4)
            range[0] = 4;
        if (range[1] > 18)
            range[1] = 18;
        if (range[2] < 4)
            range[2] = 4;
        if (range[3] > 18)
            range[3] = 18;
//        range[0]=4;range[1]=18;range[2]=4;range[3]=18;
//        System.out.println("range:"+range[0]+" "+range[1]+" "+range[2]+" "+range[3]);
        return range;
    }

    private void printMap() {
        for (int s = 0; s < qiPan; s++) {
            for (int p = 0; p < qiPan; p++) {
                System.out.print(chessMap[s][p]);
            }
            System.out.println("");
        }
    }


    public void getMap() {

        for (xPosition = 0; xPosition < 15; xPosition++) {
            for (yPosition = 0; yPosition < 15; yPosition++) {
                switch (gameCanvas.mChessArray[xPosition][yPosition].getColor()) {
                    case BLACK:
                        chessMap1[xPosition][yPosition] = Chess.BLACK_CHESS;
                        break;
                    case WHITE:
                        chessMap1[xPosition][yPosition] = Chess.WHITE_CHESS;
                        break;
                    case NONE:
                        chessMap1[xPosition][yPosition] = 2;
                        continue;
                }

            }
        }
        for (int i = 0; i < qiPan; i++) {
            for (int j = 0; j < qiPan; j++) {
                if (i == 0 || i == 1 || i == 2 || i == 3 || i == 19 || i == 20 || i == 21 || i == 22) {
                    chessMap[i][j] = 4;
                } else if (j == 0 || j == 1 || j == 2 || j == 3 || j == 19 || j == 20 || j == 21 || j == 22) {
                    chessMap[i][j] = 4;
                } else
                    chessMap[i][j] = chessMap1[j - 4][i - 4];
                //System.out.print(chessMap[i][j]);
            }
            //System.out.println("");
        }
    }

    public boolean isWin() {
        getMap();
        return sa.isWin(chessMap);
    }

}
