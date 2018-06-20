package com.emc2.www.gobang.ai;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;

public class SortNode {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sortNodeByScore(ArrayList<Node> nodes) {//【注意】final咯
        nodes.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                //以下如果改变顺序则调换一下参数位置
                if (o1.getScore() < o2.getScore())
                    return 1;
                else if (o1.getScore() > o2.getScore())
                    return -1;
                else return 0;
            }
        });
    }
}
