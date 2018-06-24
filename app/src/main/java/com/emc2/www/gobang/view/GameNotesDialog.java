package com.emc2.www.gobang.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.emc2.www.gobang.R;
import com.emc2.www.gobang.activity.MainActivity;
import com.emc2.www.gobang.db.RecordDao;
import com.emc2.www.gobang.entity.Record;
import com.emc2.www.gobang.util.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameNotesDialog {
    private View view;
    ListView listView;
    Button ok_btn;
    private MainActivity mainActivity;
    private CustomDialog dialog;
    private SimpleAdapter adapter;
    private ImageView one_number, two_number, three_number;
    private int[] number;

    public GameNotesDialog(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        view = LayoutInflater.from(mainActivity).inflate(R.layout.model_dialog, null);
        number = new int[]{R.drawable.zero, R.drawable.one, R.drawable.two, R.drawable.three,
                R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine};
    }

    public CustomDialog getModelDialog() {
        add_dialog();
        return dialog;
    }


    private void add_dialog() {
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

        RecordDao recordDao = new RecordDao(this.mainActivity);


//        List<Record> recordList = recordDao.searchRecord(black,white);
        List<Record> recordList = new ArrayList<Record>();
        for (int i = 0; i < 3; i++) {
            Record record = new Record();
            record.setWinner("11");
            record.setChessCount(i);
            record.setTime("时间" + i);
            recordList.add(record);
        }

        CustomDialog.Builder builder = new CustomDialog.Builder(mainActivity);
        dialog = builder
                .style(R.style.Dialog)
                .heightDimenRes(1600)
                .widthDimenRes(1000)
                .cancelTouchout(false)
                .view(R.layout.record)
                .addViewOnclick(R.id.ok_btn, listener)
                .build();
        dialog.show();
        findView(dialog);
        setRecord(listView);
    }

    private void findView(CustomDialog dialog) {
        one_number = dialog.findViewById(R.id.image_id3_1);
        two_number = dialog.findViewById(R.id.image_id3_2);
        three_number = dialog.findViewById(R.id.image_id3_3);
        listView = dialog.findViewById(R.id.list_view);
    }

    private void setRecord(ListView listView) {
        String[] from = {"id1", "id2", "id3", "time", "winner", "chessCount", "whitePlayer", "blackPlayer"};
        int[] to = {R.id.image_id3_1, R.id.image_id3_2, R.id.image_id3_3, R.id.record_time, R.id.record_winner, R.id.chessCount,
                R.id.record_white_rank, R.id.record_black_rank};
        adapter = new SimpleAdapter(this.mainActivity, getDate(), R.layout.record_item, from, to);
        listView.setAdapter(adapter);
    }

    public ArrayList<Map<String, Object>> getDate() {
        List<Record> recordList = RecordDao.searchAllRecord();
        ArrayList<Map<String, Object>> date = new ArrayList<>();
        if (recordList == null) {
            return date;
        }
        for (int i = 0; i < recordList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            int id = recordList.get(i).getId();
            if (id <= 9) {
                map.put("id1", number[0]);
                map.put("id2", number[0]);
                map.put("id3", number[id]);
            } else if (id <= 99) {
                map.put("id1", number[0]);
                map.put("id2", number[id / 10]);
                map.put("id3", number[id % 10]);
            } else {
                map.put("id1", number[id / 100]);
                map.put("id2", number[id / 10]);
                map.put("id3", number[id % 10]);
            }

            map.put("time", recordList.get(i).getTime());
            map.put("winner", recordList.get(i).getWinner());
            map.put("chessCount", recordList.get(i).getChessCount());
            map.put("whitePlayer", recordList.get(i).getWhitePlayer());
            map.put("blackPlayer", recordList.get(i).getBlackPlayer());
            date.add(map);
        }

        return date;
    }


}
