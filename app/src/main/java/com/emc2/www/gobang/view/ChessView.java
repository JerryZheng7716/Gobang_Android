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
    private Paint mNumberPaint;
    private Paint mTextPaint;
    public Chess[][] mChessArray;
    public List<Point> mEveryPlay;
    public int testX1 = 0, testY1 = 0, testX2 = 0, testY2 = 0, testX3 = 0, testY3 = 0, testX4 = 0, testY4 = 0;
    private String[] textLetter,textNumber;
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
        textNumber=new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
        textLetter=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O"};
        initEveryPlay();
        initChess();
        initBoardPaint();
        initChessPaint();
        initLastChessPointPaint();
        initBgPaint();
        initNumberPaint();
        initTextPaint();
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
        int x=getMeasuredWidth();
        mNumberPaint.setTextSize((float) (x*0.03));
        mNumberPaint.setColor(Color.BLACK);
        mNumberPaint.setStrokeWidth(2);
    }
    private void initTextPaint(){
        mTextPaint = new Paint();
        int x=getMeasuredWidth();
        mTextPaint.setTextSize((float) (x*0.03));
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(2);
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
        int height =getMeasuredHeight() + 48;
        int width = getMeasuredWidth() + 48;
        avg = (height) / 16;

//        canvas.drawRect(0, 0, width, height, mBgPaint);
        for (int i = 1; i < 16; i++) {
            // 画竖线
            canvas.drawLine(avg * i, avg, avg * i, height - avg-13, mBoardPaint);
            // 画横线
            canvas.drawLine(avg, avg * i, width - avg-13, avg * i, mBoardPaint);
        }
        int hand = 0;
        Point point;
        int x=15;
        for (int i = 0; i < 15; i++) {
            if (i<9){
                canvas.drawText(textNumber[i], avg-(2*x) , avg*(i+1)+x, mTextPaint);
            }else {
                canvas.drawText(textNumber[i], avg-(3*x) , avg*(i+1)+x, mTextPaint);
            }

            canvas.drawText(textLetter[i], avg*(i+1)-x , avg-x, mTextPaint);
        }

        while (hand < mEveryPlay.size()) {
            point = mEveryPlay.get(hand);
            Rect mSrcRect, mDestRect;
            int size=3;
            mSrcRect = new Rect(0, 0, (int) (avg*size), (int) (avg*size));
            float size2=0.5f;
            mDestRect = new Rect((int) (avg * (point.x + 1)+avg*size2), (int) (avg * (point.y + 1)+avg*size2),
                    (int) (avg * (point.x  +1)-avg*size2), (int) (avg * (point.y  +1)-avg*size2));
            if (hand % 2 == 0) {
                canvas.drawBitmap(ReadImage.readBitMap(R.drawable.black_chess, getContext()), mSrcRect, mDestRect, mChessPaint);
                mNumberPaint.setColor(Color.WHITE);
            } else {
                canvas.drawBitmap(ReadImage.readBitMap(R.drawable.white_chess, getContext()), mSrcRect, mDestRect, mChessPaint);
                mNumberPaint.setColor(Color.BLACK);
            }
            if (hand < 9) {
                canvas.drawText(String.valueOf(hand), avg * (point.x + 1) - 10, avg * (point.y + 1) + 12, mNumberPaint);
            } else if (hand < 99) {
                canvas.drawText(String.valueOf(hand), avg * (point.x + 1) - 20, avg * (point.y + 1) + 12, mNumberPaint);
            } else {
                canvas.drawText(String.valueOf(hand), avg * (point.x + 1) - 30, avg * (point.y + 1) + 12, mNumberPaint);
            }

            hand++;
            if (hand == mEveryPlay.size()) {
                point = mEveryPlay.get(mEveryPlay.size() - 1);
                canvas.drawCircle(avg * (point.x + 1), avg * (point.y + 1), 8, mLastChessPointPaint);
            }

        }


        canvas.drawCircle(avg * (testY1 + 1), avg * (testX1 + 1), 5, mChessPaint);
//        canvas.drawCircle(avg * testY1, avg * testX1, 5, mChessPaint);
//        canvas.drawCircle(avg * testY1, avg * testX1, 5, mChessPaint);
//        canvas.drawCircle(avg * testY1, avg * testX1, 5, mChessPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAiRuning) {
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
                    // 若点不为空，则刷新对应位置棋子的属性
                    setChessState(point);
                    invalidate();
                    // 记录下每步操作，方便悔棋操作
                    mEveryPlay.add(point);
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
                    } else if (mEveryPlay.size() == 225) {
                        mainActivity.showDrawDialog();
                    }
                    // 更改游戏玩家
                    isBlackPlay = !isBlackPlay;
                    if (getAiLevel(Chess.BLACK_CHESS) != -1 && isBlackPlay) {
                        isAiRuning = true;
                        AiTread aiTread = new AiTread(this, Chess.BLACK_CHESS);//启动黑棋AI
                        Thread thread = new Thread(aiTread);//启动AI
                        thread.start();
                    }

                    if (getAiLevel(Chess.WHITE_CHESS) != -1 && !isBlackPlay) {
                        isAiRuning = true;
                        AiTread aiTread = new AiTread(this, Chess.WHITE_CHESS);//启动白棋AI
                        Thread thread = new Thread(aiTread);//启动AI
                        thread.start();
                    }
                    if (isBlackPlay)
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
        if(mainActivity.getAiLevel(Chess.WHITE_CHESS)*mainActivity.getAiLevel(Chess.BLACK_CHESS)<0){
             mEveryPlay.remove(mEveryPlay.size() - 1);//如果是人机博弈，那么悔棋一次悔两步。
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

}