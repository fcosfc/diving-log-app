package es.upo.alu.fsaufer.dm.divinglogapp.control;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;
import es.upo.alu.fsaufer.dm.divinglogapp.util.DateUtil;

/**
 * Clase que implementa la persistencia de la APP
 */
public class DiveDbHelper extends SQLiteOpenHelper {

    private static final int DEMO_DATA_LOADED_NOTIFICATION_ID = 1;

    private static final String DATABASE_NAME = "DivingLog.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "dives";

    private final Context context;

    public DiveDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(TABLE_NAME);
        sqlBuilder.append(" (");
        sqlBuilder.append(Constant.DIVE_ID).append(" integer primary key autoincrement, ");
        sqlBuilder.append(Constant.LOCATION).append(" text not null, ");
        sqlBuilder.append(Constant.SPOT).append(" text not null, ");
        sqlBuilder.append(Constant.DIVE_DATE).append(" text not null, ");
        sqlBuilder.append(Constant.MINUTES).append(" integer not null, ");
        sqlBuilder.append(Constant.MAX_DEPTH).append(" real not null, ");
        sqlBuilder.append(Constant.REMARKS).append(" text");
        sqlBuilder.append(")");

        db.execSQL(sqlBuilder.toString());

        loadDemoData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public List<Dive> readAllData() {
        List<Dive> diveList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = new String[]{
                Constant.DIVE_ID, Constant.LOCATION, Constant.SPOT, Constant.DIVE_DATE, Constant.MINUTES, Constant.MAX_DEPTH, Constant.REMARKS
        };

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, Constant.DIVE_DATE + " DESC");

        if (cursor.getCount() != 0) {
            diveList = new ArrayList<>(cursor.getCount());
            Dive dive;

            while (cursor.moveToNext()) {
                dive = new Dive();

                dive.setDiveId(cursor.getInt(0));
                dive.setLocation(cursor.getString(1));
                dive.setSpot(cursor.getString(2));
                dive.setDiveDate(DateUtil.parseDate(cursor.getString(3)));
                dive.setMinutes(cursor.getInt(4));
                dive.setMaxDepth(cursor.getFloat(5));
                dive.setRemarks(cursor.getString(6));

                diveList.add(dive);
            }
        }

        cursor.close();
        db.close();

        return diveList;
    }

    public void save(Dive dive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(dive);

        if (dive.getDiveId() == 0) {
            insertDive(db, values);
        } else {
            updateDive(db, values, dive.getDiveId());
        }
    }

    private void insertDive(SQLiteDatabase db, ContentValues values) {
        db.insert(TABLE_NAME, null, values);
    }

    private void updateDive(SQLiteDatabase db, ContentValues values, int diveId) {
        db.update(TABLE_NAME, values, Constant.DIVE_ID + "=", new String[]{Integer.toString(diveId)});
    }

    private ContentValues getContentValues(Dive dive) {
        ContentValues values = new ContentValues();

        values.put(Constant.LOCATION, dive.getLocation());
        values.put(Constant.SPOT, dive.getSpot());
        values.put(Constant.DIVE_DATE, dive.getFormatedDiveDate());
        values.put(Constant.MINUTES, dive.getMinutes());
        values.put(Constant.MAX_DEPTH, dive.getMaxDepth());
        values.put(Constant.REMARKS, dive.getRemarks());

        return values;
    }

    private void loadDemoData(SQLiteDatabase db) {
        List<Dive> demoDiveList = getDemoDiveList();

        for (Dive dive : demoDiveList) {
            insertDive(db, getContentValues(dive));
        }

        notifyDemoDataLoaded();
    }

    private List<Dive> getDemoDiveList() {
        List<Dive> demoDiveList = new ArrayList<>();
        Dive dive;

        dive = new Dive("Tarifa", "La Garita", DateUtil.parseDate("2022-01-08"), 55, 15.2f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Las piscinas", DateUtil.parseDate("2022-01-24"), 45, 25.1f, "Fuimos a las laminarias");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Punta marroquí", DateUtil.parseDate("2022-02-10"), 38, 40.2f, "Una pasada");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", DateUtil.parseDate("2022-02-28"), 60, 14.1f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Poniente", DateUtil.parseDate("2022-03-15"), 48, 20.4f, "Bancos de peces");
        demoDiveList.add(dive);
        dive = new Dive("Ceuta", "Ciclón de fuera", DateUtil.parseDate("2022-04-01"), 40, 41.1f, "Magnífica inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Ceuta", "Ciclón de dentro", DateUtil.parseDate("2022-04-01"), 38, 15.1f, "Mucha vida");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", DateUtil.parseDate("2022-05-25"), 62, 15.1f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", DateUtil.parseDate("2022-06-01"), 55, 15.2f, "Bancos de peces");
        demoDiveList.add(dive);
        dive = new Dive("Algeciras", "Bajo del Bono", DateUtil.parseDate("2022-06-12"), 48, 38.2f, "Magnífica inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", DateUtil.parseDate("2022-07-24"), 55, 15.2f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "San Andrés", DateUtil.parseDate("2022-08-06"), 38, 45.3f, "Espectacular inmersión");
        demoDiveList.add(dive);

        return demoDiveList;
    }

    private void notifyDemoDataLoaded() {
        createNotificationChannel();

        NotificationManagerCompat.from(context).notify(DEMO_DATA_LOADED_NOTIFICATION_ID, buildNotification());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Constant.DIVING_LOG_CHANNEL_ID,
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.notification_channel_description));
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.DIVING_LOG_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.notification_demo_data_loaded_title))
                .setContentText(context.getString(R.string.notification_demo_data_loaded_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

}