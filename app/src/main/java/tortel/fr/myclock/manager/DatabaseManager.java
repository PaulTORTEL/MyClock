package tortel.fr.myclock.manager;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Arrays;

import tortel.fr.myclock.bean.Alarm;
import tortel.fr.myclock.bean.DbBundle;
import tortel.fr.myclock.database.MyClockDatabase;
import tortel.fr.myclock.database.dao.AlarmDao;
import tortel.fr.myclock.listener.DatabaseListener;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Context appContext;

    /** TASKS **/
    private class GetAllAlarmsTask extends AsyncTask<DatabaseListener, Void, Void> {
        @Override
        protected Void doInBackground(DatabaseListener[] params) {

            AlarmDao alarmDao = DatabaseManager.this.getDatabase(DatabaseManager.this.appContext).alarmDao();
            if (params.length != 1)
                return null;

            params[0].onAlarmsFetched(alarmDao.getAll());
            return null;

        }
    }

    private class InsertAlarmsTask extends AsyncTask<DbBundle, Void, DbBundle> {
        @Override
        protected DbBundle doInBackground(DbBundle[] dbBundles) {

            if (dbBundles.length != 1) {
                return null;
            }

            AlarmDao alarmDao = DatabaseManager.this.getDatabase(DatabaseManager.this.appContext).alarmDao();
            alarmDao.insertAll(dbBundles[0].getAlarms());
            return dbBundles[0];
        }

        @Override
        protected void onPostExecute(DbBundle dbBundle) {
            super.onPostExecute(dbBundle);
            dbBundle.getListener().onAlarmsSaved();
        }
    }

    private class UpdateAlarmsTask extends AsyncTask<DbBundle, Void, DbBundle> {
        @Override
        protected DbBundle doInBackground(DbBundle[] dbBundles) {

            if (dbBundles.length != 1) {
                return null;
            }

            AlarmDao alarmDao = DatabaseManager.this.getDatabase(DatabaseManager.this.appContext).alarmDao();
            alarmDao.updateAlarms(dbBundles[0].getAlarms());
            return dbBundles[0];
        }

        @Override
        protected void onPostExecute(DbBundle dbBundle) {
            super.onPostExecute(dbBundle);
            if (dbBundle.getListener() != null) {
                dbBundle.getListener().onAlarmUpdated();
            }
        }
    }

    private class DeleteAlarmsTask extends AsyncTask<DbBundle, Void, DbBundle> {
        @Override
        protected DbBundle doInBackground(DbBundle[] dbBundles) {

            if (dbBundles.length != 1) {
                return null;
            }

            AlarmDao alarmDao = DatabaseManager.this.getDatabase(DatabaseManager.this.appContext).alarmDao();
            alarmDao.delete(dbBundles[0].getAlarms()[0]);
            return dbBundles[0];
        }

        @Override
        protected void onPostExecute(DbBundle dbBundle) {
            super.onPostExecute(dbBundle);
            dbBundle.getListener().onAlarmDeleted();
        }
    }

    /** === **/

    private MyClockDatabase db;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }

    private DatabaseManager() {
    }

    public MyClockDatabase getDatabase(Context appContext) {

        if (db == null) {
            this.appContext = appContext;
            db = Room.databaseBuilder(appContext, MyClockDatabase.class, "my-clock-database").fallbackToDestructiveMigration().build();
        }
        return db;
    }

    public void getAllAlarms(DatabaseListener listener, Context appContext) {

        if (this.appContext == null) {
            this.appContext = appContext;
        }

        new GetAllAlarmsTask().execute(listener);
    }

    public void insertAlarms(DatabaseListener listener, Context appContext, Alarm... alarms) {

        if (this.appContext == null) {
            this.appContext = appContext;
        }
        DbBundle dbBundle = new DbBundle();
        dbBundle.setListener(listener);
        dbBundle.setAlarms(alarms);
        new InsertAlarmsTask().execute(dbBundle);
    }

    public void updateAlarms(DatabaseListener listener, Context appContext, Alarm... alarms) {
        if (this.appContext == null) {
            this.appContext = appContext;
        }
        DbBundle dbBundle = new DbBundle();
        dbBundle.setListener(listener);
        dbBundle.setAlarms(alarms);
        new UpdateAlarmsTask().execute(dbBundle);
    }

    public void deleteAlarm(DatabaseListener listener, Context appContext, Alarm alarm) {
        if (this.appContext == null) {
            this.appContext = appContext;
        }
        DbBundle dbBundle = new DbBundle();
        dbBundle.setListener(listener);
        dbBundle.setAlarms(new Alarm[] {alarm});
        new DeleteAlarmsTask().execute(dbBundle);
    }
}
