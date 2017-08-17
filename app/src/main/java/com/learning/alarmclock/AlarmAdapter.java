package com.learning.alarmclock;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.HashMap;
import java.util.List;

public class AlarmAdapter extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public AlarmAdapter(Context context, int alarmId, List<String> alarms) {
        super(context, alarmId, alarms);

        for (int i = 0; i < alarms.size(); ++i) {
            mIdMap.put(alarms.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        String item = getItem(position);
        return mIdMap.get(item);
    }
}
