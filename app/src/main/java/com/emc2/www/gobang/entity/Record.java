package com.emc2.www.gobang.entity;

public class Record {
    private String winner;
    private Integer chessCount;
    private String whitePlayer;
    private String blackPlayer;
    private String time;
    private Integer id;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Integer getChessCount() {
        return chessCount;
    }

    public void setChessCount(Integer chessCount) {
        this.chessCount = chessCount;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public void setWhitePlayer(String whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
