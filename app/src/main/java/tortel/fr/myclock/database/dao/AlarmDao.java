package tortel.fr.myclock.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


import tortel.fr.myclock.bean.Alarm;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM alarm WHERE id IN (:alarmIds)")
    List<Alarm> loadAllByIds(int[] alarmIds);

    @Query("SELECT * FROM alarm WHERE name LIKE :first LIMIT 1")
    Alarm findByName(String first);

    @Insert
    void insertAll(Alarm... alarms);

    @Delete
    void delete(Alarm alarm);

    @Update
    void updateAlarms(Alarm... alarms);
}
