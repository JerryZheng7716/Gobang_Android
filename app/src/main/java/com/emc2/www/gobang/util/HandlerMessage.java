package com.emc2.www.gobang.util;

import android.os.Handler;
import android.os.Message;

public class HandlerMessage {
    public static final int DEFEAT = 1;
    public static final int SHOW_WIN_DIALOG_BLACK = 2;
    public static final int SHOW_WIN_DIALOG_WHITE = 3;
    public static final int JUMP_BLACK = 4;
    public static final int JUMP_WHITE = 5;
    public static final int SHOW_DRAW_DIALOG = 6;
    public static final int REPAINT_CHESS = 7;

    public static void sendMessage(Handler handler,int arg) {
        Message message = handler.obtainMessage(300);
        message.arg1 = arg;
        handler.sendMessage(message);
    }
}
