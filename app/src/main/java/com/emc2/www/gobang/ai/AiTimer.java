package com.emc2.www.gobang.ai;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AiTimer {
    private long time;
    private int step;

    public AiTimer(long time, int step) {
        this.time = time;
        this.step = step;
    }

    public void breakAi() {
        new Timer("timer").schedule(new TimerTask() {
            @Override
            public void run() {
                if (AI.getStep() == step) {
                    AlphaBetaCutBranch.setRunningFlag(false);//时间到。结束ai的计算
                    System.out.println(time + ": 时间到。。。。。。。。。。。。。。。。。。。。。" + step);
                }

            }
        }, time);
    }
}
