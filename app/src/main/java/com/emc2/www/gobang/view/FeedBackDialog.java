package com.emc2.www.gobang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.db.RecordDao;
import com.emc2.www.gobang.util.Chess;
import com.emc2.www.gobang.util.CustomDialog;
import com.emc2.www.gobang.util.MailSend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

public class FeedBackDialog {
    private Context context;
    private CustomDialog dialog;

    public FeedBackDialog(Context context) {
        this.context = context;
    }

    public void getFeedBackDialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.cancel_btn:
                        dialog.dismiss();
                        break;
                    case R.id.send_problem:
                        EditText feedback_edit_text;
                        feedback_edit_text = dialog.findViewById(R.id.feedback_edit_text);
                        String feedString;
                        feedString = feedback_edit_text.getText().toString();
                        MailSend mailSend = new MailSend(getHandSetInfo()+"用户反馈信息："+feedString);
                        Thread thread = new Thread(mailSend);
                        thread.start();
                        Toast.makeText(context, "感谢您的反馈，我们会尽快处理您问题", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(43)
                .widthDimenRes(70)
                .cancelTouchout(false)
                .view(R.layout.feedback_dialog)
                .addViewOnclick(R.id.cancel_btn, listener)
                .addViewOnclick(R.id.send_problem, listener)
                .build();
        dialog.show();
    }

    private String getHandSetInfo() {
        String handSetInfo =
                "手机型号: " + android.os.Build.MODEL +
                        ", SDK版本: " + android.os.Build.VERSION.SDK +
                        ", 系统版本: " + android.os.Build.VERSION.RELEASE +
                        ", 软件内部版本号: " +getLocalVersion(context)+
                        ", 软件版本名："+getLocalVersionName(context)+
                        "。 ";
        return handSetInfo;
    }

    /**
     * 获取软件的版本号
     * @param ctx activity
     * @return localVersion
     */
    private static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取软件的版本名称
     * @param ctx activity
     * @return localVersion
     */
    private static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
