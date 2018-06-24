package com.emc2.www.gobang.view;
import android.content.Context;
import android.view.View;
import com.emc2.www.gobang.R;
import com.emc2.www.gobang.util.CustomDialog;

public class EggDialog {
    private Context context;
    private CustomDialog dialog;

    public EggDialog(Context context) {
        this.context = context;
    }

    public void getEggDialog() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ok_btn:
                        dialog.dismiss();
                        break;
                }
            }
        };

        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(38)
                .widthDimenRes(70)
                .cancelTouchout(false)
                .view(R.layout.easter_egg)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
    }
}
