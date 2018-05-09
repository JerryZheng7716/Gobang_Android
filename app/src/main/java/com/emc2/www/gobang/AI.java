package com.emc2.www.gobang;

import android.widget.Toast;

import java.util.Random;

public class AI {

    ChessView gameCanvas;
    int xPosition; // 临时变量 x的位置
    int yPosition; // 临时变量 y的位置
    int qiPan = 23;
    int[][] chessMap = new int[qiPan][qiPan];//0 1 2 3||19 20 21 22为墙
    public static int xChess=-1,yChess=-1;
    public static int xChess0 = -1, yChess0 = -1,xChess1 = -1, yChess1 = -1, xChess2 = -1, yChess2 = -1, xChess3 = -1, yChess3 = -1, xChess4 = -1, yChess4 = -1;
    public static int score0 = -999999999,score1 = -999999999, score2 = -999999999, score3 = -999999999, score4 = -999999999;
    int score = -999999999;
    int maxSocre = -100000;
    public void Ai(ChessView gameCanvas,int player) {
        this.gameCanvas = gameCanvas;
        int deep=0;
        long startTime=System.currentTimeMillis();   //获取开始时间
        int level = gameCanvas.getAiLevel(player);
        if (level==0||level==1){
            deep=2;
        }else {
            deep=4;
        }
        AlphaBetaCutBranch alphaBetaCutBranch1 = new AlphaBetaCutBranch(0, deep,player, -999990000, 999990000, 1,gameCanvas);
        AlphaBetaCutBranch alphaBetaCutBranch2 = new AlphaBetaCutBranch(0, deep,player, -999990000, 999990000, 2,gameCanvas);
        AlphaBetaCutBranch alphaBetaCutBranch3 = new AlphaBetaCutBranch(0, deep,player, -999990000, 999990000, 3,gameCanvas);
        AlphaBetaCutBranch alphaBetaCutBranch4 = new AlphaBetaCutBranch(0, deep,player, -999990000, 999990000, 4,gameCanvas);
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
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        //+alphaBetaCutBranch2.jd+alphaBetaCutBranch3.jd+alphaBetaCutBranch4.jd
        System.out.println("总节点数："+(alphaBetaCutBranch1.jd));
        xChess=xChess1;
        yChess=yChess1;
        score=score1;
        if (score2>=score){
            if (score2==score){
                Random random = new Random();
                if (random.nextBoolean()){
                    score=score2;
                    xChess=xChess2;
                    yChess=yChess2;
                }
            }else{
                score=score2;
                xChess=xChess2;
                yChess=yChess2;
            }
        }
        if (score3>=score){
            if (score3==score){
                Random random = new Random();
                if (random.nextBoolean()){
                    score=score3;
                    xChess=xChess3;
                    yChess=yChess3;
                }
            }else{
                score=score3;
                xChess=xChess3;
                yChess=yChess3;
            }
        }
        if (score4>=score){
            if (score4==score){
                Random random = new Random();
                if (random.nextBoolean()){
                    score=score4;
                    xChess=xChess4;
                    yChess=yChess4;
                }
            }else{
                score=score4;
                xChess=xChess4;
                yChess=yChess4;
            }
        }
        if (score0>=score){
            score=score0;
            xChess=xChess0;
            yChess=yChess0;
        }
        if (gameCanvas.chessCount==0){
            xChess=7;
            yChess=7;
        }
        score0 = -999999999;score1 = -999999999; score2 = -999999999; score3 = -999999999; score4 = -999999999;
        if (score<-300000000){
            Toast.makeText(gameCanvas.getContext(), "大佬牛逼！大佬！在下认输了！", Toast.LENGTH_SHORT).show();
        }
        System.out.println("当前Ai得分： "+score);
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

//    private int alphaBetaCutBranch(int h, int player, int alpha, int beta,int[] range){ //h搜索深度，player=1表示自己,player=0表示对手,range代表范围，用数组表示，分别是i（行）的开始结束，j（列）的开始结束
//        juShiPingGu(player^1);
//        if(h==2 || is成五Chess )   //若到达深度 或是出现胜负
//        {
//            if(is成五Chess){        //若是胜负返回-inf 或+inf
//                is成五Chess=false;
//                if (player==0){
//                    return 999900000;
//                }
//                return -99900000;
//            }
//            else{
//                return juShiPingGu(player)-juShiPingGu(player^1);   //否则返回此局面的评价值
//            }
//        }
//        int i, j;
//        if(player==1){//AI
////            System.out.println("这里执行了AI");
//            for(i=range[0]; i<= range[1]; i++) {
//                for (j = range[2]; j <= range[3]; j++) {
//                    if (chessMap[i][j] == 2) {
//                        chessMap[i][j] = player;
//                        int ans = alphaBetaCutBranch(h + 1, player^1, alpha, beta,getRange(chessMap));
//                        chessMap[i][j] = 2;
//                        if (ans > alpha) {    //通过向上传递的子节点beta值修正alpha值
//                            System.out.println(ans);
//                            alpha = ans;
//                            xChess = j - 4;       //记录位置
//                            yChess = i - 4;
//                        }
//                        if (alpha >= beta)   //发生 alpha剪枝
//                        {
//                            return alpha;
//                        }
//                    }
//                }
//            }
//            return alpha;
//        }
//        else{//对手
//            for(i=range[0]; i<= range[1]; i++) {
//                for (j = range[2]; j <= range[3]; j++) {
//                    if (chessMap[i][j] == 2) {
//                        chessMap[i][j] = player;
//                        int ans = alphaBetaCutBranch(h + 1, player^1, alpha, beta,getRange(chessMap));
//                        chessMap[i][j] = 2;
//                        if (ans < beta) {     //通过向上传递的子节点alpha值修正beta值
//                            beta = ans;
//                        }
//
//                        if (alpha >= beta)   //发生 beta剪枝
//                        {
//                            return beta;
//                        }
//                    }
//                }
//            }
//            return beta;
//        }
//    }

//    public int[] getRange(int[][] map){
//        int[] range={99,-1,99,-1};
//        for (int i = 4; i < 19; i++) {
//            for (int j = 4; j < 19; j++) {
//                if (map[i][j]==0||map[i][j]==1){
//                    if (i<range[0])
//                        range[0]=i;
//                    if (i>range[1])
//                        range[1]=i;
//                    if (j<range[2])
//                        range[2]=j;
//                    if (j>range[3])
//                        range[3]=j;
//                }
//            }
//        }
//        range[0]-=2;range[1]+=2;range[2]-=2;range[3]+=2;
//        if (range[0]<4)
//            range[0]=4;
//        if (range[1]>18)
//            range[1]=18;
//        if (range[2]<4)
//            range[2]=4;
//        if (range[3]>18)
//            range[3]=18;
////        range[0]=4;range[1]=18;range[2]=4;range[3]=18;
////        System.out.println("range:"+range[0]+" "+range[1]+" "+range[2]+" "+range[3]);
//        return range;
//    }

//    public void pressHighChess() {
//        getMap();
//        int goodScore = -1000000000;
//        score = 0;
//        int[] aiChessZuoBiao = new int[3];
//        int[] huManZuoBiao1 = new int[3];
//        int count = 0;
//        for (int i = 4; i < 19; i++) {
//            for (int j = 4; j < 19; j++) {
//                if (chessMap[i][j] == 1 || chessMap[i][j] == 0) {
//                    count++;
//                }
//            }
//        }
//        for (int i = 4; i < 19; i++) {
//            for (int j = 4; j < 19; j++) {
//                if (chessMap[i][j] == 2) {
//                    chessMap[i][j] = 1;
//                    huManZuoBiao1 = getGoodChess(0);
////                    for (int k = 4; k < 19; k++) {
////                        for (int l = 4; l < 19; l++) {
////                            if (chessMap[k][l] == 2) {
////                                chessMap[k][l]=0;
////                                int color=0;int othetColor=1;
////                                SeachLandscape(color,othetColor);
////                                SeachPortrait(color,othetColor);
////                                SeachSlant(color,othetColor);
//////                                System.out.println("ren时候的score"+score);
////                                if (scoreRen < score) {
////                                    scoreRen = score;
////                                    xRenChess = l;
////                                    yRenChess = k;//得出对手最优解
////                                }
////                                chessMap[k][l]=2;
////                                score=0;
////                            }
////                        }
////                    }
//                    chessMap[huManZuoBiao1[1]][huManZuoBiao1[0]] = 0;//将对手最优解输入地图
//                    int fengshu = juShiPingGu(1);
//                    System.out.println((j - 4) + "坐标" + "" + (i - 4) + " 分：" + fengshu);
//                    if (goodScore < fengshu) {
//                        goodScore = fengshu;
//                        xChess = j - 4;
//                        yChess = i - 4;
//                        printMap();
//                    }
//                    chessMap[huManZuoBiao1[1]][huManZuoBiao1[0]] = 2;
//                    chessMap[i][j] = 2;
//                }
//            }
//
//        }
//        System.out.println(xChess + "ai计算后坐标" + "" + yChess + "最高分：" + goodScore);
//
////        for (int i = 4; i < 19; i++) {
////            for (int j = 4; j < 19; j++) {
////                if (chessMap[i][j] == 2) {
////                    chessMap[i][j]=color;
////                    System.out.println("模拟图像");
////                    for (int s = 0; s < qiPan; s++) {
////                        for (int p = 0; p < qiPan; p++) {
////                            System.out.print(chessMap[s][p]);
////                        }
////                        System.out.println("");
////                    }
////                    SeachLandscape();
////                    SeachPortrait();
////                    SeachSlant();
////                    if (score > maxSocre) {
////                        maxSocre = score;
////                        System.out.println("max" + maxSocre);
////                        xChess = j - 4;
////                        yChess = i - 4;
////                    }
////                    chessMap[i][j] = 2;
////                    score = 0;
////                }
////            }
////        }
//        if (count == 1)
//            noAiChessVoid();
//    }
//
//    public void pressLowChess() {
//        getMap();
//        maxSocre = -1000000000;
//        score = 0;
//        is成五Chess = false;
//        int goodScore = -1000000000;
//        int cutTree[] = new int[15];
//        int cutTree2[] = new int[15];
//        int nullChess1 = 0;
//        int nullChess2 = 0;
//        int[] aiChessZuoBiao = new int[2];
//        int[] huManZuoBiao1 = new int[2];
//        int[] huManZuoBiao2 = new int[2];
//        for (int i = 4; i < 19; i++) {
//            nullChess1 = 0;
//            nullChess2 = 0;
//            for (int j = 4; j < 19; j++) {
//                if (chessMap[i][j] == 2) {
//                    nullChess1++;
//                    if (nullChess1 == 15) {
//                        cutTree[i - 4] = i;
//                    }
//                }
//                if (chessMap[j][i] == 2) {
//                    nullChess2++;
//                    if (nullChess2 == 15) {
//                        cutTree2[i - 4] = i;
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < 15; i++) {
//            System.out.print(cutTree[i]);
//            System.out.print(" ");
//        }
//        System.out.println("以下是剪枝列的数值");
//        for (int i = 0; i < 15; i++) {
//            System.out.print(cutTree2[i]);
//            System.out.print(" ");
//        }
//
//
//        for (int i = 4; i < 19; i++) {
//            if (goCutTree(i, cutTree)) {
//                continue;
//            }
////            System.out.println("这里执行了i" + i);
//            for (int j = 4; j < 19; j++) {
//                if (goCutTree(j, cutTree2)) {
//                    continue;
//                }
////                System.out.println("这里执行了j" + j);
//                if (chessMap[i][j] == 2) {
//                    chessMap[i][j] = 1;
//                    huManZuoBiao1 = getGoodChess(0);
////                    for (int k = 4; k < 19; k++) {
////                        for (int l = 4; l < 19; l++) {
////                            if (chessMap[k][l] == 2) {
////                                chessMap[k][l]=0;
////                                int color=0;int othetColor=1;
////                                SeachLandscape(color,othetColor);
////                                SeachPortrait(color,othetColor);
////                                SeachSlant(color,othetColor);
//////                                System.out.println("ren时候的score"+score);
////                                if (scoreRen < score) {
////                                    scoreRen = score;
////                                    xRenChess = l;
////                                    yRenChess = k;//得出对手最优解
////                                }
////                                chessMap[k][l]=2;
////                                score=0;
////                            }
////                        }
////                    }
//                    chessMap[huManZuoBiao1[1]][huManZuoBiao1[0]] = 0;//将对手最优解输入地图
//                    for (int k = 4; k < 19; k++) {
//                        if (goCutTree(k, cutTree)) {
//                            continue;
//                        }
//                        for (int l = 4; l < 19; l++) {
//                            if (goCutTree(l, cutTree)) {
//                                continue;
//                            }
//                            if (chessMap[k][l] == 2) {
//                                chessMap[k][l] = 1;
//                                huManZuoBiao2 = getGoodChess(0);
//                                chessMap[huManZuoBiao2[1]][huManZuoBiao2[0]] = 0;//将对手最优解输入地图
//                                int fengshu = juShiPingGu(1);
//                                if (goodScore < fengshu) {
//                                    goodScore = fengshu;
//                                    System.out.println("AI出第一招：" + (i - 4) + ":" + (j - 4) + "AI出第二招：" + (k - 4) + ":" + (l - 4));
//                                    System.out.println("对手第一手最优解：" + (huManZuoBiao1[1] - 4) + ":" + (huManZuoBiao1[0] - 4) + "对手第二手最优解：" + (huManZuoBiao2[1] - 4) + ":" + (huManZuoBiao2[0] - 4));
//                                    xChess = j - 4;
//                                    yChess = i - 4;
//                                }
////                                int color1=0;int othetColor1=1;
////                                SeachLandscape(color1,othetColor1);
////                                SeachPortrait(color1,othetColor1);
////                                SeachSlant(color1,othetColor1);
//////                                System.out.println("得分"+score);
////                                scoreRen=score;
////                                score=0;
////
////                                int color=1;int othetColor=0;
////                                SeachLandscape(color,othetColor);
////                                SeachPortrait(color,othetColor);
////                                SeachSlant(color,othetColor);
//////                                System.out.println("得分"+score);
////                                scoreAi=score;
////                                score=0;
////
////                                if (scoreAi-scoreRen > maxSocre) {
//////                        System.out.println(scoreAi-scoreRen+"ai-ren最大分"+maxSocre);
////                                    maxSocre = scoreAi-scoreRen;
//////                        System.out.println("max" + maxSocre);
////                                    xAiChess = j;
////                                    yAiChess = i;
////                                }
//                                chessMap[k][l] = 2;
//                                chessMap[huManZuoBiao2[1]][huManZuoBiao2[0]] = 2;
//                            }
//                        }
//                    }
//                    chessMap[huManZuoBiao1[1]][huManZuoBiao1[0]] = 2;
//                    chessMap[i][j] = 2;
//                }
////                    System.out.println("ai得分"+scoreAi+"人类得分"+scoreRen+"max得分"+maxSocre);
////                    chessMap[i][j] = 2;
////                    chessMap[huManZuoBiao1[1]][huManZuoBiao1[0]]=2;
////
////                    chessMap[yAiChess][xAiChess] = 1;
////                    chessMap[yRenChess][xRenChess]=2;
////                    scoreRen=0;
//            }
//
//        }
//        System.out.println(xChess + "ai计算后坐标" + "" + yChess);
////        for (int i = 4; i < 19; i++) {
////            for (int j = 4; j < 19; j++) {
////                if (chessMap[i][j] == 2) {
////                    chessMap[i][j]=color;
////                    System.out.println("模拟图像");
////                    for (int s = 0; s < qiPan; s++) {
////                        for (int p = 0; p < qiPan; p++) {
////                            System.out.print(chessMap[s][p]);
////                        }
////                        System.out.println("");
////                    }
////                    SeachLandscape();
////                    SeachPortrait();
////                    SeachSlant();
////                    if (score > maxSocre) {
////                        maxSocre = score;
////                        System.out.println("max" + maxSocre);
////                        xChess = j - 4;
////                        yChess = i - 4;
////                    }
////                    chessMap[i][j] = 2;
////                    score = 0;
////                }
////            }
////        }
////        noAiChessVoid();
//
//    }

//    private void noAiChessVoid() {
//        noAi:
//        for (int i = 4; i < 19; i++) {
//            for (int j = 4; j < 19; j++) {
//                if (chessMap[i][j] == 0) {//othetColor
//                    System.out.println("在无ai算法中获得第一个黑子坐标" + i + "" + j);
//                    for (int k = j - 1; k > 0; k--) {
//                        if (chessMap[i][k] == 2) {
//                            xChess = k - 4;
//                            yChess = i - 4;
//                            break noAi;
//                        }
//                    }
//                    for (int k = j + 1; k < 19; k++) {
//                        if (chessMap[i][k] == 2) {
//                            xChess = k - 4;
//                            yChess = i - 4;
//                            break noAi;
//                        }
//                    }
//                }
//
//            }
//        }
//    }

}
