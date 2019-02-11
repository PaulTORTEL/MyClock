package tortel.fr.myclock.listener;

import java.util.List;

import tortel.fr.myclock.bean.Alarm;

public interface DatabaseListener {
    void onAlarmsFetched(List<Alarm> listAlarm);
    void onAlarmsSaved();
    void onAlarmDeleted();
    void onAlarmUpdated();
}
