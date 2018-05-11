package com.emc2.www.gobang;

public class AlphaBetaCutBranch implements Runnable {
    private static final int SCORE成五 = 9999999;
    private static final int SCORE活四 = 1000000;
    private static final int SCORE冲四 = 200;
    private static final int SCORE跳四 = 120;
    private static final int SCORE活三 = 200;
    private static final int SCORE跳活三 = 30;
    private static final int SCORE眠三 = 15;
    private static final int SCORE活二 = 20;
    private static final int SCORE跳活二 = 15;
    private static final int SCORE眠二 = 5;
    private static final int SCORE双活三 = 10000;
    private static final int SCORE双冲四 = 10000;
    private static final int SCORE冲四活三 = 10000;
    private static final int SCORE双活二 = 40;
    private static final int SCORE活二眠二 = 20;
    ChessView gameCanvas;
    int xPosition; // 临时变量 x的位置
    int yPosition; // 临时变量 y的位置
    int h;
    int deep;
    int player;
    int alpha;
    int beta;
    int block;
    AI ai = new AI();
    boolean is成五Chess = false;
    int qiPan = 23;
    int color = 0;
    int othetColor = 1;
    int count活三 = 0, count活二 = 0, count冲四 = 0, count眠三 = 0, count跳活三 = 0, count跳四 = 0;
    int[][] chessMap = new int[qiPan][qiPan];//0 1 2 3||19 20 21 22为墙
    int[][] chessMap1 = new int[15][15];
    int score = 0;
    int maxSocre = -100000;
    int xChess, yChess;
    int lastScore;
    public int jd=0;
    boolean nextChessIsWin=false;

    public AlphaBetaCutBranch(int h, int deep,int player, int alpha, int beta, int block, ChessView gameCanvas) {//block代表四个区块，分别是1，2，3，4
        this.alpha = alpha;
        this.h = h;
        this.deep=deep;
        this.beta = beta;
        this.block = block;
        this.player = player;
        this.gameCanvas = gameCanvas;
    }

    @Override
    public void run() {
        getMap();
        int[][] calculationPoint = getCalculationPoint(chessMap);
        int length = calculationPoint[0][0];
        int[] range1 = new int[2], range2 = new int[2], range3 = new int[2], range4 = new int[2];
        range1[0] = 1;
        range1[1] = length / 4;
        range2[0] = range1[1] + 1;
        range2[1] = length / 2;
        range3[0] = range2[1] + 1;
        range3[1] = (range2[1] + 1 + length) / 2;
        range4[0] = range3[1] + 1;
        range4[1] = length;
        range4[1] = length+4;
        int[] firstRange={1,length};
        alphaBetaCutBranch(0, 2,player, alpha, beta, calculationPoint, firstRange);
        if(nextChessIsWin){
            AI.score0 = lastScore;
            AI.xChess0 = xChess;
            AI.yChess0 = yChess;
            nextChessIsWin=false;
            return;
        }
        calculationPoint[0][0]=length+4;
        calculationPoint[length+1][0]=calculationPoint[range1[0]][0];
        calculationPoint[length+1][1]=calculationPoint[range1[0]][1];
        calculationPoint[length+2][0]=calculationPoint[range2[0]][0];
        calculationPoint[length+2][1]=calculationPoint[range2[0]][1];
        calculationPoint[length+3][0]=calculationPoint[range3[0]][0];
        calculationPoint[length+3][1]=calculationPoint[range3[0]][1];
        calculationPoint[length+4][0]=calculationPoint[range4[0]][0];
        calculationPoint[length+4][1]=calculationPoint[range4[0]][1];
        calculationPoint[range1[0]][0]=yChess+4;
        calculationPoint[range1[0]][1]=xChess+4;
        calculationPoint[range2[0]][0]=yChess+4;
        calculationPoint[range2[0]][1]=xChess+4;
        calculationPoint[range3[0]][0]=yChess+4;
        calculationPoint[range3[0]][1]=xChess+4;
        calculationPoint[range4[0]][0]=yChess+4;
        calculationPoint[range4[0]][1]=xChess+4;
        int[] rangex={1,length};
        if (block == 1) {
            System.out.println("第一层次一共有" + length + "个节点");
            alphaBetaCutBranch(h, deep,player, alpha, beta, calculationPoint, range1);
            AI.score1 = lastScore;
            AI.xChess1 = xChess;
            AI.yChess1 = yChess;
            System.out.println("第一个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
            gameCanvas.testX1 = 0;
            gameCanvas.testY1 = 0;
            gameCanvas.invalidate();//重新绘制
        }
        if (block == 2) {
            alphaBetaCutBranch(h, deep,player, alpha, beta, calculationPoint, range2);
            AI.score2 = lastScore;
            AI.xChess2 = xChess;
            AI.yChess2 = yChess;
            System.out.println("第二个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
            gameCanvas.testX2 = 0;
            gameCanvas.testY2 = 0;
            gameCanvas.invalidate();//重新绘制
        }
        if (block == 3) {
            alphaBetaCutBranch(h, deep,player, alpha, beta, calculationPoint, range3);
            AI.score3 = lastScore;
            AI.xChess3 = xChess;
            AI.yChess3 = yChess;
            System.out.println("第三个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
            gameCanvas.testX3 = 0;
            gameCanvas.testY3 = 0;
            gameCanvas.invalidate();//重新绘制
        }
        if (block == 4) {
            alphaBetaCutBranch(h, deep,player, alpha, beta, calculationPoint, range4);
            AI.score4 = lastScore;
            AI.xChess4 = xChess;
            AI.yChess4 = yChess;
            System.out.println("第四个线程得到棋子位置是：" + xChess + ": " + yChess + ",分数：" + lastScore);
            gameCanvas.testX4 = 0;
            gameCanvas.testY4 = 0;
            gameCanvas.invalidate();//重新绘制
        }
    }
    int county=0;
    private int alphaBetaCutBranch(int h,int deep, int player, int alpha, int beta, int[][] calculationPoint, int[] range) { //h搜索深度，player=1表示自己,player=0表示对手,range代表范围，用数组表示，分别是i（行）的开始结束，j（列）的开始结束
        int p,p2;
        p = juShiPingGu(player); p2 = juShiPingGu(player ^ 1);
        if (h == deep || is成五Chess)   //若到达深度 或是出现胜负
        {
            if (is成五Chess&&h!=0) {        //若是胜负返回-inf 或+inf
                is成五Chess = false;
                if (this.player == player) {
                    return -999900000/ h + p - p2 ;
                } else
                    return 999900000 / h + p - p2;
            } else {
                return p - p2;   //否则返回此局面的评价值
            }
        }
        int i, j;
        if (player == this.player) {//AI
            for (int k = range[0]; k <= range[1]; k++) {
                i = calculationPoint[k][0];
                j = calculationPoint[k][1];

                if (chessMap[i][j] == 2) {
                    jd++;
                    chessMap[i][j] = player;
                    int[] nextRange = new int[2];
                    int[][] nextCalculationPoint = getCalculationPoint(chessMap);
                    nextRange[0] = 1;
                    nextRange[1] = nextCalculationPoint[0][0];
                    int ans = alphaBetaCutBranch(h + 1, deep,player ^ 1, alpha, beta, nextCalculationPoint, nextRange);
                    if (ans > alpha) {    //通过向上传递的子节点beta值修正alpha值
                        alpha = ans;
                        if (h == 0) {
                            lastScore = ans;
                            xChess = j - 4;       //记录位置
                            yChess = i - 4;
                        }
                        switch (block) {
                            case 1:
                                gameCanvas.testX1 = j - 4;
                                gameCanvas.testY1 = i - 4;
                                gameCanvas.invalidate();//重新绘制
                                break;
                            case 2:
                                gameCanvas.testX2 = j - 4;
                                gameCanvas.testY2 = i - 4;
                                gameCanvas.invalidate();//重新绘制
                                break;
                            case 3:
                                gameCanvas.testX3 = j - 4;
                                gameCanvas.testY3 = i - 4;
                                gameCanvas.invalidate();//重新绘制
                                break;
                            case 4:
                                gameCanvas.testX4 = j - 4;
                                gameCanvas.testY4 = i - 4;
                                gameCanvas.invalidate();//重新绘制
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
                    int ans = alphaBetaCutBranch(h + 1, deep,player ^ 1, alpha, beta, nextCalculationPoint, nextRange);
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
                        chessMap1[xPosition][yPosition] = MainActivity.BLACK_CHESS;
                        break;
                    case WHITE:
                        chessMap1[xPosition][yPosition] = MainActivity.WHITE_CHESS;
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

    private void seachMap() {
        count活三 = 0;
        count活二 = 0;
        count冲四 = 0;
        count眠三 = 0;
        count跳四 = 0;
        count跳活三 = 0;
        for (int i = 4; i < 19; i++) {
            for (int j = 4; j < 19; j++) {
                if (chessMap[i][j] == color) {
                    SeachLandscape(i, j);
                    SeachSlant(i, j);
                    SeachPortrait(i, j);
                }
            }
        }
        int count = count跳活三 + count活三 + count跳四 + count冲四;
        if (count >= 2) {
            score = score + SCORE双活三*(1+count跳四 + count冲四);
//            System.out.println(" count跳活三 + count活三 + count跳四 + count冲四: "+count跳活三 +":"+ count活三 +":"+ count跳四 +":"+ count冲四+": "+score);
        }
    }

    private void SeachLandscape(int i, int j) {
        int count = 1;
        for (int k = j - 1; k > j - 5; k--) {
            if (chessMap[i][k] == 2 || chessMap[i][k] == othetColor || chessMap[i][k] == 4) {
                break;//找到非本子，跳出循环
            }
            if (chessMap[i][k] == color) {
                count++;//找到本子。count++
            }
        }
        if (count == 4) {
            int k = j - 3;
            if (chessMap[i][k - 1] == 2 && chessMap[i][k + 4] == 2) {
                is活四();
            }
            if (chessMap[i][k - 1] == 2 && (chessMap[i][k + 4] == othetColor || chessMap[i][k + 4] == 4)) {
                is冲四();
            }
            if ((chessMap[i][k - 1] == othetColor || chessMap[i][k - 1] == 4) && chessMap[i][k + 4] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            int k = j - 2;
            if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2) {
                is活三();
            }
            if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && (chessMap[i][k - 1] == othetColor || chessMap[i][k - 1] == 4)) {
                is眠三();
            }
            if (chessMap[i][k - 1] != 4) {
                if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && chessMap[i][k - 1] == 2 && (chessMap[i][k - 2] == othetColor || chessMap[i][k - 2] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2 && (chessMap[i][k + 3] == othetColor || chessMap[i][k + 3] == 4)) {
                is眠三();
            }
            if (chessMap[i][k + 3] != 4) {
                if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2 && chessMap[i][k + 3] == 2 && (chessMap[i][k + 4] == othetColor || chessMap[i][k + 4] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == color) {
                is跳四();
            }
            if (chessMap[i][k + 3] == 2 && chessMap[i][k + 4] == color) {
                is跳四();
            }
        }

        if (count == 2) {
            int k = j - 1;
            if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && chessMap[i][k + 2] == 2 && (chessMap[i][k - 1] == othetColor || chessMap[i][k - 1] == 4)) {
                is眠二();
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2 && chessMap[i][k - 3] == 2 && (chessMap[i][k + 2] == othetColor || chessMap[i][k + 2] == 4)) {
                is眠二();
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == color && chessMap[i][k - 3] == 2 && chessMap[i][k + 2] == 2) {
                is跳活三();
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k + 2] == 2 && chessMap[i][k + 3] == color && chessMap[i][k + 4] == 2) {
                is跳活三();
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == color && chessMap[i][k - 3] == color) {
                is跳四();
            }
        }
        if (count == 5) {
            is成五();
        }
    }//横向搜索

    private void SeachPortrait(int i, int j) {
        int count = 1;
        for (int k = i - 1; k > i - 5; k--) {
            if (chessMap[k][j] == 2 || chessMap[k][j] == othetColor || chessMap[k][j] == 4) {
                break;
            }
            if (chessMap[k][j] == color) {
                count++;
            }
        }
        if (count == 4) {
            int k = i - 3;
            if (chessMap[k - 1][j] == 2 && chessMap[k + 4][j] == 2) {
                is活四();
            }
            if (chessMap[k - 1][j] == 2 && (chessMap[k + 4][j] == othetColor || chessMap[k + 4][j] == 4)) {
                is冲四();
            }
            if ((chessMap[k - 1][j] == othetColor || chessMap[k - 1][j] == 4) && chessMap[k + 4][j] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            int k = i - 2;
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2) {
                is活三();
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && (chessMap[k + 3][j] == othetColor || chessMap[k + 3][j] == 4)) {
                is眠三();
            }
            if (chessMap[k + 4][j] != 4) {
                if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k + 3][j] == 2 && (chessMap[k + 4][j] == othetColor || chessMap[k + 4][j] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if ((chessMap[k - 1][j] == othetColor || chessMap[k - 1][j] == 4) && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2) {
                is眠三();
            }
            if (chessMap[k - 1][j] != 4) {
                if ((chessMap[k - 2][j] == othetColor || chessMap[k - 2][j] == 4) && chessMap[k - 1][j] == 2 && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == color) {
                is跳四();
            }
            if (chessMap[k + 3][j] == 2 && chessMap[k + 4][j] == color) {
                is跳四();
            }
        }

        if (count == 2) {
            int k = i - 1;
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k - 3][j] == 2 && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2 && chessMap[k + 2][j] == 2) {
                is活二();
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k - 3][j] == 2 && (chessMap[k + 2][j] == othetColor || chessMap[k + 2][j] == 4)) {
                is眠二();
            }
            if ((chessMap[k - 1][j] == othetColor || chessMap[k - 1][j] == 4) && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2 && chessMap[k + 2][j] == 2) {
                is眠二();
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == color && chessMap[k - 3][j] == 2 && chessMap[k + 2][j] == 2) {
                is跳活三();
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k + 2][j] == 2 && chessMap[k + 3][j] == color && chessMap[k + 4][j] == 2) {
                is跳活三();
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == color && chessMap[k - 3][j] == color) {
                is跳四();
            }
        }
        if (count == 5) {
            is成五();
        }
    }//纵向搜索

    private void SeachSlant(int i, int j) {
        int count = 1;
        for (int k = 1; k < 5; k++) {
            if (chessMap[i + k][j + k] == 2 || chessMap[i + k][j + k] == othetColor || chessMap[i + k][j + k] == 4) {
                break;//找到非本子。停止搜索
            }
            if (chessMap[i + k][j + k] == color) {
                count++;
            }
        }
        //计算要注意，此处用的是ij不是k，ij是起始位置，不像很熟搜索用k，k是移动的最后一个子
        if (count == 4) {
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i + 4][j + 4] == 2) {
                is活四();
            }
            if (chessMap[i - 1][j - 1] == 2 && (chessMap[i + 4][j + 4] == othetColor || chessMap[i + 4][j + 4] == 4)) {
                is冲四();
            }
            if ((chessMap[i - 1][j - 1] == othetColor || chessMap[i - 1][j - 1] == 4) && chessMap[i + 4][j + 4] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2) {
                is活三();
            }
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && (chessMap[i + 3][j + 3] == othetColor || chessMap[i + 3][j + 3] == 4)) {
                is眠三();//1000221
            }
            if (chessMap[i + 3][j + 3] != 4) {
                if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i + 3][j + 3] == 2 && (chessMap[i + 4][j + 4] == othetColor || chessMap[i + 4][j + 4] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if ((chessMap[i - 1][j - 1] == othetColor || chessMap[i - 1][j - 1] == 4) && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2) {
                is眠三();
            }
            if (chessMap[i - 1][j - 1] != 4) {
                if ((chessMap[i - 2][j - 2] == othetColor || chessMap[i - 2][j - 2] == 4) && chessMap[i - 1][j - 1] == 2 && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == color) {
                is跳四();
            }
            if (chessMap[i + 3][j + 3] == 2 && chessMap[i + 4][j + 4] == color) {
                is跳四();
            }

        }

        if (count == 2) {
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i - 3][j - 3] == 2 && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2 && chessMap[i + 2][j + 2] == 2) {
                is活二();
            }
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i - 3][j - 3] == 2 && (chessMap[i + 2][j + 2] == othetColor || chessMap[i + 2][j + 2] == 4)) {
                is眠二();
            }
            if ((chessMap[i - 1][j - 1] == othetColor || chessMap[i - 1][j - 1] == 4) && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2 && chessMap[i + 2][j + 2] == 2) {
                is眠二();
            }
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == color && chessMap[i - 3][j - 3] == 2 && chessMap[i + 2][j + 2] == 2) {
                is跳活三();
            }
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i + 2][j + 2] == 2 && chessMap[i + 3][j + 3] == color && chessMap[i + 4][j + 4] == 2) {
                is跳活三();
            }
            if (chessMap[i + 2][j + 2] == 2 && chessMap[i + 3][j + 3] == color && chessMap[i + 4][j + 4] == color) {
                is跳四();
            }
        }
        if (count == 5) {
            is成五();
        }

        //以上右下搜索结束↑↑↑↑↑↑
        //进行左下搜索
        count = 1;
        for (int k = 1; k < 6; k++) {
            if (chessMap[i + k][j - k] == 2 || chessMap[i + k][j - k] == othetColor || chessMap[i + k][j - k] == 4) {
                break;//找到非本子。停止搜索
            }

            if (chessMap[i + k][j - k] == color) {
                count++;
            }
        }
        if (count == 4) {
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i + 4][j - 4] == 2) {
                is活四();
            }
            if (chessMap[i - 1][j + 1] == 2 && (chessMap[i + 4][j - 4] == othetColor || chessMap[i + 4][j - 4] == 4)) {
                is冲四();
            }
            if ((chessMap[i - 1][j + 1] == othetColor || chessMap[i - 1][j + 1] == 4) && chessMap[i + 4][j - 4] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2) {
                is活三();
            }
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && (chessMap[i + 3][j - 3] == othetColor || chessMap[i + 3][j - 3] == 4)) {
                is眠三();
            }
            if (chessMap[i + 3][j - 3] != 4) {
                if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i + 3][j - 3] == 2 && (chessMap[i + 4][j - 4] == othetColor || chessMap[i + 4][j - 4] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if ((chessMap[i - 1][j + 1] == othetColor || chessMap[i - 1][j + 1] == 4) && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2) {
                is眠三();
            }
            if (chessMap[i - 1][j + 1] != 4) {
                if ((chessMap[i - 2][j + 2] == othetColor || chessMap[i - 2][j + 2] == 4) && chessMap[i - 1][j + 1] == 2 && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == color) {
                is跳四();
            }
            if (chessMap[i + 3][j - 3] == 2 && chessMap[i + 4][j - 4] == color) {
                is跳四();
            }
        }

        if (count == 2) {
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i - 3][j + 3] == 2 && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2 && chessMap[i + 2][j - 2] == 2) {
                is活二();
            }
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i - 3][j + 3] == 2 && (chessMap[i + 2][j - 2] == othetColor || chessMap[i + 2][j - 2] == 4)) {
                is眠二();
            }
            if ((chessMap[i - 1][j + 1] == othetColor || chessMap[i - 1][j + 1] == 4) && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2 && chessMap[i + 2][j - 2] == 2) {
                is眠二();
            }


            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == color && chessMap[i - 3][j + 3] == 2 && chessMap[i + 2][j - 2] == 2) {
                is跳活三();
            }
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i + 2][j - 2] == 2 && chessMap[i + 3][j - 3] == color && chessMap[i + 4][j - 4] == 2) {
                is跳活三();
            }
            if (chessMap[i + 2][j - 2] == 2 && chessMap[i + 3][j - 3] == color && chessMap[i + 4][j - 4] == color) {
                is跳四();
            }
        }
        if (count == 5) {
            is成五();
        }
    }//斜向搜索slant

    private void getPositionScore() {
        for (int i = 4; i < 19; i++) {
            for (int j = 4; j < 19; j++) {
                if (chessMap[i][j] == color) {
                    if (i == 11 && j == 11) {
                        score += 7;
                    } else if (i >= 10 && i <= 12 && j >= 10 && j <= 12) {
                        score += 6;
                    } else if (i >= 9 && i <= 13 && j >= 9 && j <= 13) {
                        score += 5;
                    } else if (i >= 8 && i <= 14 && j >= 8 && j <= 14) {
                        score += 4;
                    } else if (i >= 7 && i <= 15 && j >= 7 && j <= 15) {
                        score += 3;
                    } else if (i >= 6 && i <= 16 && j >= 6 && j <= 16) {
                        score += 2;
                    } else if (i >= 5 && i <= 17 && j >= 5 && j <= 17) {
                        score += 1;
                    }
                }
            }
        }
    }//给不同位置的棋子赋分，越中间分数越高


    private void is成五() {
        score = score + SCORE成五;
        is成五Chess = true;
        nextChessIsWin=true;
    }

    private void is活四() {
        score = score + SCORE活四;
        if (color == 1) {
            score = score + 500;
        }
    }

    private void is冲四() {
        score = score + SCORE冲四;
        count冲四++;
    }

    private void is活三() {
        score = score + SCORE活三;
        if (color == 1) {
            score += 20;
        }
        count活三++;
    }

    private void is跳活三() {
        score = score + SCORE跳活三;
        count跳活三++;
    }

    private void is跳四() {
        score = score + SCORE跳四;
        count跳四++;
    }

    private void is眠三() {
        score = score + SCORE眠三;
        count眠三++;
    }

    private void is活二() {
        score = score + SCORE活二;
        count活二++;
        if (count活二 >= 2) {
            score = score + SCORE双活二;
        }
        if (count活二 >= 4) {
            score = score + SCORE活三;
        }
    }

    private void is眠二() {
        score = score + SCORE眠二;
    }

    private int[] getGoodChess(int who) {
        int scoreMe = 0;
        int scoreHe = 0;
        int xRenChess = 0;
        int yRenChess = 0;
        int goodScore = -1000000000;
        int[] zuoBiao = new int[3];
        for (int k = 4; k < 19; k++) {
            for (int l = 4; l < 19; l++) {
                if (chessMap[k][l] == 2) {
                    if (who == 0) {
                        color = 0;
                        othetColor = 1;
                    } else {
                        color = 1;
                        othetColor = 0;
                    }
                    chessMap[k][l] = color;
                    for (int i = 4; i < 19; i++) {
                        for (int j = 4; j < 19; j++) {
                            if (chessMap[i][j] == 2) {
                                chessMap[i][j] = othetColor;

                                seachMap();
                                scoreMe = score;
                                score = 0;

                                int x;
                                x = color;
                                color = othetColor;
                                othetColor = x;

                                seachMap();
                                scoreHe = score;
                                score = 0;

                                if (scoreMe - scoreHe > goodScore) {
                                    goodScore = scoreMe - scoreHe;
                                    xRenChess = l;
                                    yRenChess = k;
                                }
                                chessMap[k][l] = 2;
                                chessMap[i][j] = 2;
                                score = 0;
                            }
                        }
                    }

                }
            }
        }
        zuoBiao[0] = xRenChess;
        zuoBiao[1] = yRenChess;
        zuoBiao[2] = goodScore;
        return zuoBiao;
    }

    public int juShiPingGu(int who) {
        score = 0;
        color = who;
        othetColor = who ^ 1;
        seachMap();
        getPositionScore();
        return score;
    }

    public  boolean isWin(){
        getMap();
        color = 1;
        othetColor = 0;
        seachMap();
        color = 0;
        othetColor = 1;
        seachMap();
        return is成五Chess;
    }
}
