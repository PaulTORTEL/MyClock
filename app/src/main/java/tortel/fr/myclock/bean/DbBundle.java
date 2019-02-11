package tortel.fr.myclock.bean;

import tortel.fr.myclock.listener.DatabaseListener;

public class DbBundle {
    private DatabaseListener listener;
    private Alarm[] alarms;

    public DatabaseListener getListener() {
        return listener;
    }

    public void setListener(DatabaseListener listener) {
        this.listener = listener;
    }

    public Alarm[] getAlarms() {
        return alarms;
    }

    public void setAlarms(Alarm[] alarms) {
        this.alarms = alarms;
    }
}
