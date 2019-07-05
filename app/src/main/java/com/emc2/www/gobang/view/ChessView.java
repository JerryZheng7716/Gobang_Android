package com.emc2.www.gobang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.PlayAudio;
import com.emc2.www.gobang.util.ReadImage;
import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.ai.AiTread;
import com.emc2.www.gobang.ai.AlphaBetaCutBranch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerryzheng on 2018/5/8.
 * 自定义五子棋棋盘控件
 */
public class ChessView extends View {

    private static final String TAG = "ChessView";
    public static boolean isBlackPlay = true;
    public boolean isLocked = false;
    public static boolean isAiRuning = false;
    private Paint mBoardPaint;
    private Paint mChessPaint;
    private Paint mBgPaint;
    private Paint mLastChessPointPaint;
    private Paint mAidPointPaint;
    private Paint mNumberPaint;
    private Paint mTextPaint;
    public Chess[][] mChessArray;
    public List<Point> mEveryPlay;
    public int testX1 = 0, testY1 = 0, testX2 = 0, testY2 = 0, testX3 = 0, testY3 = 0, testX4 = 0, testY4 = 0;
    private String[] textLetter, textNumber;
    private ReadImage readImage = new ReadImage();
    public MainActivity mainActivity;
    private WinDialog winDialog;
    int avg = 0;

    public ChessView(Context context) {
        this(context, null);
    }

    public ChessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mainActivity = (MainActivity) context;
        textNumber = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        textLetter = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
        initEveryPlay();
        initChess();
        initBoardPaint();
        initChessPaint();
        initLastChessPointPaint();
        initBgPaint();
        initNumberPaint();
        initTextPaint();
        initAidPointPaint();
    }

    private void initEveryPlay() {
        // 初始化 List 大小，此方法不影响 list.size() 返回值
        mEveryPlay = new ArrayList<>(225);
    }

    private void initChess() {
        mChessArray = new Chess[15][15];
        for (int i = 0; i < mChessArray.length; i++) {
            for (int j = 0; j < mChessArray[i].length; j++) {
                mChessArray[i][j] = new Chess();
            }
        }
    }

    private void initChessPaint() {
        mChessPaint = new Paint();
        mChessPaint.setColor(Color.GREEN);
        mChessPaint.setAntiAlias(true);
    }

    private void initLastChessPointPaint() {
        mLastChessPointPaint = new Paint();
        mLastChessPointPaint.setColor(Color.RED);
        mLastChessPointPaint.setStrokeWidth(2);
    }

    private void initBoardPaint() {
        mBoardPaint = new Paint();
        mBoardPaint.setColor(android.graphics.Color.BLACK);
        mBoardPaint.setStrokeWidth(2);
    }

    private void initBgPaint() {
        mBgPaint = new Paint();
        mBgPaint.setColor(android.graphics.Color.GRAY);
        mBgPaint.setAntiAlias(true);
    }

    private void initNumberPaint() {
        mNumberPaint = new Paint();
        int x = getMeasuredWidth();
        mNumberPaint.setTextSize((float) (x * 0.03));
        mNumberPaint.setColor(Color.BLACK);
        mNumberPaint.setStrokeWidth(2);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        int x = getMeasuredWidth();
        mTextPaint.setTextSize((float) (x * 0.03));
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(2);
    }

    private void initAidPointPaint() {
        mAidPointPaint = new Paint();
        mAidPointPaint.setColor(Color.BLACK);
        mAidPointPaint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        initTextPaint();
        initNumberPaint();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int min = widthSize < heightSize ? widthSize : heightSize;
        min = min / 16 * 16;

        setMeasuredDimension(min + 15, min + 15);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int height = getMeasuredHeight() + 48;
        int width = getMeasuredWidth() + 48;
        avg = (height) / 16;

//        canvas.drawRect(0, 0, width, height, mBgPaint);
        for (int i = 1; i < 16; i++) {
            // 画竖线
            canvas.drawLine(avg * i, avg, avg * i, height - avg - 13, mBoardPaint);
            // 画横线
            canvas.drawLine(avg, avg * i, width - avg - 13, avg * i, mBoardPaint);
        }
        //绘制9个辅助点
        canvas.drawCircle(avg * (3 + 1), avg * (3 + 1), 8, mAidPointPaint);
        canvas.drawCircle(avg * (7 + 1), avg * (3 + 1), 8, mAidPointPaint);
        canvas.drawCircle(avg * (11 + 1), avg * (3 + 1), 8, mAidPointPaint);

        canvas.drawCircle(avg * (3 + 1), avg * (7 + 1), 8, mAidPointPaint);
        canvas.drawCircle(avg * (7 + 1), avg * (7 + 1), 8, mAidPointPaint);
        canvas.drawCircle(avg * (11 + 1), avg * (7 + 1), 8, mAidPointPaint);

        canvas.drawCircle(avg * (3 + 1), avg * (11 + 1), 8, mAidPointPaint);
        canvas.drawCircle(avg * (7 + 1), avg * (11 + 1), 8, mAidPointPaint);
        canvas.drawCircle(avg * (11 + 1), avg * (11 + 1), 8, mAidPointPaint);
        int hand = 0;
        Point point;
        int x = 15;
        //绘制左边和上边的数字、字母坐标
        for (int i = 0; i < 15; i++) {
            if (i < 9) {
                canvas.drawText(textNumber[i], avg - (2 * x), avg * (i + 1) + x, mTextPaint);
            } else {
                canvas.drawText(textNumber[i], avg - (3 * x), avg * (i + 1) + x, mTextPaint);
            }

            canvas.drawText(textLetter[i], avg * (i + 1) - x, avg - x, mTextPaint);
        }


        while (hand < mEveryPlay.size()) {
            point = mEveryPlay.get(hand);
            Rect mSrcRect, mDestRect;
            int size = 3;
            mSrcRect = new Rect(0, 0, (int) (avg * size), (int) (avg * size));
            float size2 = 0.5f;
            System.out.println(point.x+"---"+point.y);
            //棋子的直径，就是网格间距，x0.98是为了稍微留点缝隙
            int chessSize= (int) (avg*0.98);
            //下面两个变量标注了棋子的绘制位置，也很好理解，棋子在棋盘的坐标x网格的间距+棋子直径的一半
            int chessPositionX = (point.x)*avg+chessSize/2;
            int chessPositionY = (point.y)*avg+chessSize/2;
            mDestRect = new Rect(chessPositionX,chessPositionY,chessPositionX+chessSize,chessPositionY+chessSize);
            if (hand % 2 == 0) {//绘制黑棋
                canvas.drawBitmap(ReadImage.readBitMap(R.drawable.black_chess, getContext()), mSrcRect, mDestRect, mChessPaint);
                mNumberPaint.setColor(Color.WHITE);
            } else {//绘制白棋
                canvas.drawBitmap(ReadImage.readBitMap(R.drawable.white_chess, getContext()), mSrcRect, mDestRect, mChessPaint);
                mNumberPaint.setColor(Color.BLACK);
            }
            if (hand < 9) {//绘制棋子序号0-9
                canvas.drawText(String.valueOf(hand), avg * (point.x + 1) - 10, avg * (point.y + 1) + 12, mNumberPaint);
            } else if (hand < 99) {//绘制棋子序号10-99
                canvas.drawText(String.valueOf(hand), avg * (point.x + 1) - 20, avg * (point.y + 1) + 12, mNumberPaint);
            } else {//绘制棋子序号>99
                canvas.drawText(String.valueOf(hand), avg * (point.x + 1) - 30, avg * (point.y + 1) + 12, mNumberPaint);
            }

            hand++;
            if (hand == mEveryPlay.size()) {//绘制最后落子的棋子上加一个红点
                point = mEveryPlay.get(mEveryPlay.size() - 1);
                canvas.drawCircle(avg * (point.x + 1), avg * (point.y + 1), 8, mLastChessPointPaint);
            }
        }
        canvas.drawCircle(avg * (testX1+1), avg * (testY1+1), 5, mChessPaint);//绘制AI搜索的测试点
        canvas.drawCircle(avg * (testX2+1), avg * (testY2+1), 5, mChessPaint);
        canvas.drawCircle(avg * (testX3+1), avg * (testY3+1), 5, mChessPaint);
        canvas.drawCircle(avg * (testX4+1), avg * (testY4+1), 5, mChessPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAiRuning) {//如果ai真正执行，则禁止用户下棋
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果棋盘被锁定（即胜负已分，返回查看棋局的时候）
                // 此时只允许查看，不允许落子了
                if (isLocked) {
                    return true;
                }
                float x = event.getX();
                float y = event.getY();
                // 以点击的位置为中心，新建一个小矩形
                Rect rect = getLittleRect(x, y);
                // 获得上述矩形包含的棋盘上的点
                Point point = getContainPoint(rect);
                if (point != null && mEveryPlay.size() != 225) {
                    // 若点不为空，且棋盘没有下满，则刷新对应位置棋子的属性
                    setChessState(point);
                    invalidate();
                    // 记录下每步操作，方便悔棋操作
                    mEveryPlay.add(point);
                    boolean egg = true;
                    if (mEveryPlay.size() == 9) {//侦测是否触发彩蛋
                        for (int i = 0; i < mEveryPlay.size(); i++) {
                            point = mEveryPlay.get(i);
                            boolean b = inEgg(point.x, point.y);
                            if (!b)
                                egg = false;
                        }
                        if (egg) {
                            EggDialog eggDialog = new EggDialog(mainActivity);
                            eggDialog.getEggDialog();
                        }
                    }
                    //播放下棋的声音
                    PlayAudio playChessSound;
                    playChessSound = PlayAudio.getChessAudioInstance(mainActivity);
                    if (MainActivity.isSoundOpen)
                        playChessSound.play("chess_sound.wav", false);
                    //使用Ai算法内的的算法判断是否有人获胜了
                    AlphaBetaCutBranch alphaBetaCutBranch = new AlphaBetaCutBranch(0, 2, 1, -999990000, 999990000, 1, this);
                    if (alphaBetaCutBranch.isWin()) {
                        winDialog = new WinDialog(getContext(), this);
                        winDialog.getWinlDialog(isBlackPlay);
                    } else if (mEveryPlay.size() == 225) {//棋盘如果满了就显示和棋
                        mainActivity.showDrawDialog();
                    }
                    // 更改游戏玩家
                    isBlackPlay = !isBlackPlay;
                    if (getAiLevel(Chess.BLACK_CHESS) != -1 && isBlackPlay) {//如果当前颜色是AI持有的那么启动AI
                        isAiRuning = true;
                        AiTread aiTread = new AiTread(this, Chess.BLACK_CHESS);//启动黑棋AI
                        Thread thread = new Thread(aiTread);//启动AI
                        thread.start();
                    }

                    if (getAiLevel(Chess.WHITE_CHESS) != -1 && !isBlackPlay) {//如果当前颜色是AI持有的那么启动AI
                        isAiRuning = true;
                        AiTread aiTread = new AiTread(this, Chess.WHITE_CHESS);//启动白棋AI
                        Thread thread = new Thread(aiTread);//启动AI
                        thread.start();
                    }
                    if (isBlackPlay)//改变人物旁边跳动的棋子的
                        mainActivity.doJumpAnimation(Chess.BLACK_CHESS);
                    else
                        mainActivity.doJumpAnimation(Chess.WHITE_CHESS);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }


    /**
     * 重新设定用户所点位置的棋子状态
     *
     * @param point 棋子的位置
     */
    public void setChessState(Point point) {
        if (isBlackPlay) {
            mChessArray[point.x][point.y].setColor(Chess.Color.BLACK);
        } else {
            mChessArray[point.x][point.y].setColor(Chess.Color.WHITE);
        }
    }

    /**
     * 以传入点为中心，获得一个矩形
     *
     * @param x 传入点 x 坐标
     * @param y 传入点 y 坐标
     * @return 所得矩形
     */
    private Rect getLittleRect(float x, float y) {
        float side = (getMeasuredHeight() + 48) / 16;
        int left = (int) (x - side / 2);
        int top = (int) (y - side / 2);
        int right = (int) (x + side / 2);
        int bottom = (int) (y + side / 2);
        return new Rect(left, top, right, bottom);
    }

    /**
     * 获取包含在 rect 中并且是能够下棋的位置的点
     *
     * @param rect 矩形
     * @return 返回包含的点，若没有包含任何点或者包含点已有棋子返回 null
     */
    private Point getContainPoint(Rect rect) {
        int avg = (getMeasuredHeight() + 48) / 16;
        for (int i = 1; i < 16; i++) {
            for (int j = 1; j < 16; j++) {
                if (rect.contains(avg * i, avg * j)) {
                    Point point = new Point(i - 1, j - 1);
                    // 包含点没有棋子才返回 point
                    if (mChessArray[point.x][point.y].getColor() == Chess.Color.NONE) {
                        return point;
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * 悔棋，实现思路为：记录每一步走棋的坐标，若点击了悔棋，
     * 则拿出最后记录的坐标，对 mChessArray 里面对应坐标的
     * 棋子进行处理（设置颜色为 NONE），并移除集合里面最后
     * 一个元素
     */
    public void retract() {
        if (mEveryPlay.isEmpty()) {
            return;
        }
        Point point = mEveryPlay.get(mEveryPlay.size() - 1);
        mChessArray[point.x][point.y].setColor(Chess.Color.NONE);
        mEveryPlay.remove(mEveryPlay.size() - 1);
        if (mainActivity.getAiLevel(Chess.WHITE_CHESS) * mainActivity.getAiLevel(Chess.BLACK_CHESS) <= 0) {
            if (!(mainActivity.getAiLevel(Chess.WHITE_CHESS) == 0 && mainActivity.getAiLevel(Chess.BLACK_CHESS) == 0))
                if (mEveryPlay.size() != 0) {
                    point = mEveryPlay.get(mEveryPlay.size() - 1);
                    mChessArray[point.x][point.y].setColor(Chess.Color.NONE);
                    mEveryPlay.remove(mEveryPlay.size() - 1);//如果是人机博弈，那么悔棋一次悔两步。
                    isBlackPlay = !isBlackPlay;
                }
        }
        isLocked = false;
        isBlackPlay = !isBlackPlay;
        invalidate();
    }

    /**
     * 重置棋盘
     */
    public void resetChessBoard() {
        for (Chess[] chessRow : mChessArray) {
            for (Chess chess : chessRow) {
                chess.setColor(Chess.Color.NONE);
            }
        }
        mEveryPlay.clear();
        isBlackPlay = true;
        isLocked = false;
        invalidate();
    }

    public int getAiLevel(int who) {
        return mainActivity.getAiLevel(who);
    }

    private boolean inEgg(int x, int y) {//棋子是否在彩蛋的位置
        int[][] points = new int[][]{{3, 3}, {3, 7}, {3, 11}, {7, 3}, {7, 7}, {7, 11}, {11, 3}, {11, 7}, {11, 11}};
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            if (points[i][0] == x && points[i][1] == y) {
                flag = true;
            }
        }
        return flag;
    }

}