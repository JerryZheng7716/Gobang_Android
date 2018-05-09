package com.emc2.www.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

/**
 * Created by jerryzheng on 2018/5/8.
 */

public class PublicFunction {
    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId
     * @return Bitmap
     */
    public  Bitmap readBitMap(int resId,Context context) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //  获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
