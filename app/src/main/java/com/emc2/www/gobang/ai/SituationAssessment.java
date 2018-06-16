package com.emc2.www.gobang.ai;

/**
 * Created by jerryzheng on 2018/6/4.
 */

public class SituationAssessment {
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


    boolean is成五Chess = false;
    private boolean nextChessIsWin = false;
    private int[][] chessMap;
    private int color = 0;
    private int otherColor = 1;
    private int count活三 = 0, count活二 = 0, count冲四 = 0, count眠三 = 0, count跳活三 = 0, count跳四 = 0;
    private int score = 0;
    int maxSocre = -100000;
    public SituationAssessment(int[][] chessMap){
        this.chessMap=chessMap;
    }
    private void searchMap() {
        count活三 = 0;
        count活二 = 0;
        count冲四 = 0;
        count眠三 = 0;
        count跳四 = 0;
        count跳活三 = 0;
        for (int i = 4; i < 19; i++) {
            for (int j = 4; j < 19; j++) {
                if (chessMap[i][j] == color) {
                    searchLandscape(i, j);
                    searchSlant(i, j);
                    searchPortrait(i, j);
                }
            }
        }
        int count = count跳活三 + count活三 + count跳四 + count冲四;
        if (count >= 2) {
            score = score + SCORE双活三*(1+count跳四 + count冲四);
//            System.out.println(" count跳活三 + count活三 + count跳四 + count冲四: "+count跳活三 +":"+ count活三 +":"+ count跳四 +":"+ count冲四+": "+score);
        }
    }

    private void searchLandscape(int i, int j) {
        int count = 1;
        for (int k = j - 1; k > j - 5; k--) {
            if (chessMap[i][k] == 2 || chessMap[i][k] == otherColor || chessMap[i][k] == 4) {
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
            if (chessMap[i][k - 1] == 2 && (chessMap[i][k + 4] == otherColor || chessMap[i][k + 4] == 4)) {
                is冲四();
            }
            if ((chessMap[i][k - 1] == otherColor || chessMap[i][k - 1] == 4) && chessMap[i][k + 4] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            int k = j - 2;
            if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2) {
                is活三();
            }
            if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && (chessMap[i][k - 1] == otherColor || chessMap[i][k - 1] == 4)) {
                is眠三();
            }
            if (chessMap[i][k - 1] != 4) {
                if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && chessMap[i][k - 1] == 2 && (chessMap[i][k - 2] == otherColor || chessMap[i][k - 2] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2 && (chessMap[i][k + 3] == otherColor || chessMap[i][k + 3] == 4)) {
                is眠三();
            }
            if (chessMap[i][k + 3] != 4) {
                if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2 && chessMap[i][k + 3] == 2 && (chessMap[i][k + 4] == otherColor || chessMap[i][k + 4] == 4)) {
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
            if (chessMap[i][k + 4] == 2 && chessMap[i][k + 3] == 2 && chessMap[i][k + 2] == 2 && (chessMap[i][k - 1] == otherColor || chessMap[i][k - 1] == 4)) {
                is眠二();
            }
            if (chessMap[i][k - 1] == 2 && chessMap[i][k - 2] == 2 && chessMap[i][k - 3] == 2 && (chessMap[i][k + 2] == otherColor || chessMap[i][k + 2] == 4)) {
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

    private void searchPortrait(int i, int j) {
        int count = 1;
        for (int k = i - 1; k > i - 5; k--) {
            if (chessMap[k][j] == 2 || chessMap[k][j] == otherColor || chessMap[k][j] == 4) {
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
            if (chessMap[k - 1][j] == 2 && (chessMap[k + 4][j] == otherColor || chessMap[k + 4][j] == 4)) {
                is冲四();
            }
            if ((chessMap[k - 1][j] == otherColor || chessMap[k - 1][j] == 4) && chessMap[k + 4][j] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            int k = i - 2;
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2) {
                is活三();
            }
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && (chessMap[k + 3][j] == otherColor || chessMap[k + 3][j] == 4)) {
                is眠三();
            }
            if (chessMap[k + 4][j] != 4) {
                if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k + 3][j] == 2 && (chessMap[k + 4][j] == otherColor || chessMap[k + 4][j] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if ((chessMap[k - 1][j] == otherColor || chessMap[k - 1][j] == 4) && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2) {
                is眠三();
            }
            if (chessMap[k - 1][j] != 4) {
                if ((chessMap[k - 2][j] == otherColor || chessMap[k - 2][j] == 4) && chessMap[k - 1][j] == 2 && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2) {
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
            if (chessMap[k - 1][j] == 2 && chessMap[k - 2][j] == 2 && chessMap[k - 3][j] == 2 && (chessMap[k + 2][j] == otherColor || chessMap[k + 2][j] == 4)) {
                is眠二();
            }
            if ((chessMap[k - 1][j] == otherColor || chessMap[k - 1][j] == 4) && chessMap[k + 4][j] == 2 && chessMap[k + 3][j] == 2 && chessMap[k + 2][j] == 2) {
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

    private void searchSlant(int i, int j) {
        int count = 1;
        for (int k = 1; k < 5; k++) {
            if (chessMap[i + k][j + k] == 2 || chessMap[i + k][j + k] == otherColor || chessMap[i + k][j + k] == 4) {
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
            if (chessMap[i - 1][j - 1] == 2 && (chessMap[i + 4][j + 4] == otherColor || chessMap[i + 4][j + 4] == 4)) {
                is冲四();
            }
            if ((chessMap[i - 1][j - 1] == otherColor || chessMap[i - 1][j - 1] == 4) && chessMap[i + 4][j + 4] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2) {
                is活三();
            }
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && (chessMap[i + 3][j + 3] == otherColor || chessMap[i + 3][j + 3] == 4)) {
                is眠三();//1000221
            }
            if (chessMap[i + 3][j + 3] != 4) {
                if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i + 3][j + 3] == 2 && (chessMap[i + 4][j + 4] == otherColor || chessMap[i + 4][j + 4] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if ((chessMap[i - 1][j - 1] == otherColor || chessMap[i - 1][j - 1] == 4) && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2) {
                is眠三();
            }
            if (chessMap[i - 1][j - 1] != 4) {
                if ((chessMap[i - 2][j - 2] == otherColor || chessMap[i - 2][j - 2] == 4) && chessMap[i - 1][j - 1] == 2 && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2) {
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
            if (chessMap[i - 1][j - 1] == 2 && chessMap[i - 2][j - 2] == 2 && chessMap[i - 3][j - 3] == 2 && (chessMap[i + 2][j + 2] == otherColor || chessMap[i + 2][j + 2] == 4)) {
                is眠二();
            }
            if ((chessMap[i - 1][j - 1] == otherColor || chessMap[i - 1][j - 1] == 4) && chessMap[i + 4][j + 4] == 2 && chessMap[i + 3][j + 3] == 2 && chessMap[i + 2][j + 2] == 2) {
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
            if (chessMap[i + k][j - k] == 2 || chessMap[i + k][j - k] == otherColor || chessMap[i + k][j - k] == 4) {
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
            if (chessMap[i - 1][j + 1] == 2 && (chessMap[i + 4][j - 4] == otherColor || chessMap[i + 4][j - 4] == 4)) {
                is冲四();
            }
            if ((chessMap[i - 1][j + 1] == otherColor || chessMap[i - 1][j + 1] == 4) && chessMap[i + 4][j - 4] == 2) {
                is冲四();
            }
        }
        if (count == 3) {
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2) {
                is活三();
            }
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && (chessMap[i + 3][j - 3] == otherColor || chessMap[i + 3][j - 3] == 4)) {
                is眠三();
            }
            if (chessMap[i + 3][j - 3] != 4) {
                if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i + 3][j - 3] == 2 && (chessMap[i + 4][j - 4] == otherColor || chessMap[i + 4][j - 4] == 4)) {
                    is活三();//这种棋形，对方必须应答,但是比普通活三略弱，可能无法形成冲四
                    score = score - 2 * SCORE眠三;
                }
            }
            if ((chessMap[i - 1][j + 1] == otherColor || chessMap[i - 1][j + 1] == 4) && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2) {
                is眠三();
            }
            if (chessMap[i - 1][j + 1] != 4) {
                if ((chessMap[i - 2][j + 2] == otherColor || chessMap[i - 2][j + 2] == 4) && chessMap[i - 1][j + 1] == 2 && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2) {
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
            if (chessMap[i - 1][j + 1] == 2 && chessMap[i - 2][j + 2] == 2 && chessMap[i - 3][j + 3] == 2 && (chessMap[i + 2][j - 2] == otherColor || chessMap[i + 2][j - 2] == 4)) {
                is眠二();
            }
            if ((chessMap[i - 1][j + 1] == otherColor || chessMap[i - 1][j + 1] == 4) && chessMap[i + 4][j - 4] == 2 && chessMap[i + 3][j - 3] == 2 && chessMap[i + 2][j - 2] == 2) {
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
                        otherColor = 1;
                    } else {
                        color = 1;
                        otherColor = 0;
                    }
                    chessMap[k][l] = color;
                    for (int i = 4; i < 19; i++) {
                        for (int j = 4; j < 19; j++) {
                            if (chessMap[i][j] == 2) {
                                chessMap[i][j] = otherColor;

                                searchMap();
                                scoreMe = score;
                                score = 0;

                                int x;
                                x = color;
                                color = otherColor;
                                otherColor = x;

                                searchMap();
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

    public int evaluation(int who) {
        score = 0;
        color = who;
        otherColor = who ^ 1;
        searchMap();
        getPositionScore();
        return score;
    }

    public  boolean isWin(int[][] chessMap){
        this.chessMap=chessMap;
        color = 1;
        otherColor = 0;
        searchMap();
        color = 0;
        otherColor = 1;
        searchMap();
        return is成五Chess;
    }

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

    public boolean getNextChessIsWin(){
        return nextChessIsWin;
    }

    public void setNextChessIsWin(Boolean nextChessIsWin){
        this.nextChessIsWin=nextChessIsWin;
    }

    public boolean getIs成五Chess() {
        return is成五Chess;
    }

    public void setIs成五Chess(boolean is成五Chess) {
        this.is成五Chess = is成五Chess;
    }
}
