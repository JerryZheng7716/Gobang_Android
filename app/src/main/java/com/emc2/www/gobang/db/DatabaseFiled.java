package com.emc2.www.gobang.db;

public final class DatabaseFiled {
    public static final String DATABASE_NAME = "gobang.db";
    public static final int DATABASE_VERSION = 1;

    public static final class Tables {
        public static final String Record = "record";
    }

    public static final class Record {
        public static final String WINNER = "winner";
        public static final String CHESS_COUNT = "chessCount";
        public static final String WHITE_PAYER = "whitePlayer";
        public static final String BLACK_PLAYER = "blackPlayer";
        public static final String CHESS_MAP = "chess_map";
        public static final String TIME = "time";
        public static final String ID = "id";
    }
}
