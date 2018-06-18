package com.emc2.www.gobang.ai;

public class Node {
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int score;
    private int x;
    private int y;

    public Node(int score, int x, int y) {
        this.score = score;
        this.x = x;
        this.y = y;
    }
}
