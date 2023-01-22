package es.upo.alu.fsaufer.dm.divinglogapp.control.db;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.DiveLocation;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.GeoLocation;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.WeatherConditions;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;
import es.upo.alu.fsaufer.dm.divinglogapp.util.DateUtil;

/**
 * Clase que implementa la persistencia de la APP
 */
public class DiveDbHelper extends SQLiteOpenHelper {

    private static final int DEMO_DATA_LOADED_NOTIFICATION_ID = 1;

    private static final String DATABASE_NAME = "DivingLog.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DIVES_TABLE_NAME = "dives";
    private static final String LOCATIONS_TABLE_NAME = "locations";

    private final Context context;
    private SQLiteDatabase divingLogDatabase = null;

    public DiveDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);

        loadDemoData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldDbVersion, int newDbVersion) {
        if ((oldDbVersion == DATABASE_VERSION - 1) && (newDbVersion == DATABASE_VERSION)) {
            db.execSQL("DROP TABLE IF EXISTS " + DIVES_TABLE_NAME);
        }
        createTables(db);

        loadDemoData(db);
    }

    public Map<Integer, DiveLocation> readAllLocations() {
        return readAllLocations(getDivingLogDatabase());
    }

    public Map<Integer, DiveLocation> readAllLocations(SQLiteDatabase db) {
        Map<Integer, DiveLocation> locationMap = null;
        String[] columns = new String[]{
                Constant.LOCATION_ID, Constant.NAME, Constant.LONGITUDE, Constant.LATITUDE
        };

        Cursor cursor = db.query(LOCATIONS_TABLE_NAME, columns, null, null, null, null, Constant.NAME);

        if (cursor.getCount() != 0) {
            locationMap = new HashMap<>();
            DiveLocation diveLocation;

            while (cursor.moveToNext()) {
                diveLocation = new DiveLocation();

                diveLocation.setLocationId(cursor.getInt(0));
                diveLocation.setName(cursor.getString(1));

                Location location = new Location(Constant.LOCATION_PROVIDER);
                diveLocation.setLocation(
                        new GeoLocation(cursor.getFloat(2),
                                cursor.getFloat(3)));

                locationMap.put(diveLocation.getLocationId(), diveLocation);
            }
        }

        cursor.close();

        return locationMap;
    }

    public List<Dive> readAllDives(Map<Integer, DiveLocation> locations) {
        List<Dive> diveList = null;
        String[] columns = new String[]{
                Constant.DIVE_ID, Constant.LOCATION_ID, Constant.SPOT, Constant.DIVE_DATE,
                Constant.MINUTES, Constant.MAX_DEPTH, Constant.WEATHER_CONDITIONS,
                Constant.NITROX_USE, Constant.REMARKS
        };

        Cursor cursor = getDivingLogDatabase().query(DIVES_TABLE_NAME, columns, null, null, null, null, Constant.DIVE_DATE + " DESC");

        if (cursor.getCount() != 0) {
            diveList = new ArrayList<>(cursor.getCount());
            Dive dive;

            while (cursor.moveToNext()) {
                dive = new Dive();

                dive.setDiveId(cursor.getInt(0));
                dive.setLocation(locations.get(cursor.getInt(1)));
                dive.setSpot(cursor.getString(2));
                dive.setDiveDate(DateUtil.parseDate(cursor.getString(3)));
                dive.setMinutes(cursor.getInt(4));
                dive.setMaxDepth(cursor.getFloat(5));
                dive.setWeatherConditions(WeatherConditions.valueOf(cursor.getString(6)));
                dive.setNitroxUse(cursor.getInt(7) != 0);
                dive.setRemarks(cursor.getString(8));

                diveList.add(dive);
            }
        }

        cursor.close();

        return diveList;
    }

    public void save(Dive dive) {
        ContentValues values = getDiveContentValues(dive);

        if (dive.getDiveId() == 0) {
            insertDive(getDivingLogDatabase(), values);
        } else {
            updateDive(getDivingLogDatabase(), values, dive.getDiveId());
        }
    }

    private SQLiteDatabase getDivingLogDatabase() {
        if (divingLogDatabase == null) {
            divingLogDatabase = getWritableDatabase();
        }

        return divingLogDatabase;
    }

    public void delete(int diveId) {
        getDivingLogDatabase().delete(DIVES_TABLE_NAME, Constant.DIVE_ID + "=?", new String[]{Integer.toString(diveId)});
    }

    private void insertLocation(SQLiteDatabase db, ContentValues values) {
        db.insert(LOCATIONS_TABLE_NAME, null, values);
    }

    private void insertDive(SQLiteDatabase db, ContentValues values) {
        db.insert(DIVES_TABLE_NAME, null, values);
    }

    private void updateDive(SQLiteDatabase db, ContentValues values, int diveId) {
        db.update(DIVES_TABLE_NAME, values, Constant.DIVE_ID + "=?", new String[]{Integer.toString(diveId)});
    }

    private ContentValues getLocationContentValues(DiveLocation diveLocation) {
        ContentValues values = new ContentValues();

        values.put(Constant.NAME, diveLocation.getName());
        values.put(Constant.LONGITUDE, diveLocation.getLocation().getLongitude());
        values.put(Constant.LATITUDE, diveLocation.getLocation().getLatitude());

        return values;
    }

    private ContentValues getDiveContentValues(Dive dive) {
        ContentValues values = new ContentValues();

        values.put(Constant.LOCATION_ID, dive.getLocation().getLocationId());
        values.put(Constant.SPOT, dive.getSpot());
        values.put(Constant.DIVE_DATE, dive.getFormattedDiveDate());
        values.put(Constant.MINUTES, dive.getMinutes());
        values.put(Constant.MAX_DEPTH, dive.getMaxDepth());
        values.put(Constant.WEATHER_CONDITIONS, dive.getWeatherConditions().toString());
        values.put(Constant.NITROX_USE, dive.isNitroxUse() ? 1 : 0);
        values.put(Constant.REMARKS, dive.getRemarks());

        return values;
    }

    private void createTables(SQLiteDatabase db) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(LOCATIONS_TABLE_NAME);
        sqlBuilder.append(" (");
        sqlBuilder.append(Constant.LOCATION_ID).append(" integer primary key autoincrement, ");
        sqlBuilder.append(Constant.NAME).append(" text not null, ");
        sqlBuilder.append(Constant.LONGITUDE).append(" real not null, ");
        sqlBuilder.append(Constant.LATITUDE).append(" real not null");
        sqlBuilder.append(")");
        db.execSQL(sqlBuilder.toString());

        sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(DIVES_TABLE_NAME);
        sqlBuilder.append(" (");
        sqlBuilder.append(Constant.DIVE_ID).append(" integer primary key autoincrement, ");
        sqlBuilder.append(Constant.LOCATION_ID).append(" integer not null, ");
        sqlBuilder.append(Constant.SPOT).append(" text not null, ");
        sqlBuilder.append(Constant.DIVE_DATE).append(" text not null, ");
        sqlBuilder.append(Constant.MINUTES).append(" integer not null, ");
        sqlBuilder.append(Constant.MAX_DEPTH).append(" real not null, ");
        sqlBuilder.append(Constant.WEATHER_CONDITIONS).append(" text not null, ");
        sqlBuilder.append(Constant.NITROX_USE).append(" integer not null, ");
        sqlBuilder.append(Constant.REMARKS).append(" text, ");
        sqlBuilder.append("FOREIGN KEY(").append(Constant.LOCATION_ID).append(") REFERENCES ")
                .append(LOCATIONS_TABLE_NAME).append("(").append(Constant.LOCATION_ID).append(")");
        sqlBuilder.append(")");
        db.execSQL(sqlBuilder.toString());
    }

    private void loadDemoData(SQLiteDatabase db) {
        Map<Integer, DiveLocation> locationMap = loadDemoLocations(db);
        loadDemoDives(db, locationMap);

        notifyDemoDataLoaded();
    }

    private Map<Integer, DiveLocation> loadDemoLocations(SQLiteDatabase db) {
        List<DiveLocation> demoDiveLocationList = getDemoLocationList();

        for (DiveLocation diveLocation : demoDiveLocationList) {
            insertLocation(db, getLocationContentValues(diveLocation));
        }

        return readAllLocations(db);
    }

    List<DiveLocation> getDemoLocationList() {
        List<DiveLocation> demoDiveLocationList = new ArrayList<>();
        DiveLocation diveLocation;

        diveLocation = new DiveLocation("Algeciras", new GeoLocation(-5.4928738, 36.1310078));
        demoDiveLocationList.add(diveLocation);

        diveLocation = new DiveLocation("Ceuta", new GeoLocation(-5.3360354, 35.8890551));
        demoDiveLocationList.add(diveLocation);

        diveLocation = new DiveLocation("Tarifa", new GeoLocation(-5.6090361, 36.0144551));
        demoDiveLocationList.add(diveLocation);

        return demoDiveLocationList;
    }

    private void loadDemoDives(SQLiteDatabase db, Map<Integer, DiveLocation> locationMap) {
        List<Dive> demoDiveList = getDemoDiveList(locationMap);

        for (Dive dive : demoDiveList) {
            insertDive(db, getDiveContentValues(dive));
        }
    }

    private List<Dive> getDemoDiveList(Map<Integer, DiveLocation> locationMap) {
        Map<String, DiveLocation> locationMapByName = transformMapToLookForName(locationMap);
        List<Dive> demoDiveList = new ArrayList<>();
        Dive dive;

        dive = new Dive(locationMapByName.get("Tarifa"), "La Garita", DateUtil.parseDate("2022-01-08"), 55, 15.2f, WeatherConditions.GOOD, false, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "Las piscinas", DateUtil.parseDate("2022-01-24"), 45, 25.1f, WeatherConditions.GOOD, false, "Fuimos a las laminarias");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "Punta marroquí", DateUtil.parseDate("2022-02-10"), 38, 40.2f, WeatherConditions.GOOD, false, "Una pasada");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "La Garita", DateUtil.parseDate("2022-02-28"), 60, 14.1f, WeatherConditions.GOOD, false, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "Poniente", DateUtil.parseDate("2022-03-15"), 48, 20.4f, WeatherConditions.BAD, false, "Bancos de peces");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Ceuta"), "Ciclón de fuera", DateUtil.parseDate("2022-04-01"), 40, 41.1f, WeatherConditions.GOOD, false, "Magnífica inmersión");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Ceuta"), "Ciclón de dentro", DateUtil.parseDate("2022-04-01"), 38, 15.1f, WeatherConditions.GOOD, false, "Mucha vida");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "La Garita", DateUtil.parseDate("2022-05-25"), 62, 15.1f, WeatherConditions.GOOD, false, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "La Garita", DateUtil.parseDate("2022-06-01"), 55, 15.2f, WeatherConditions.TOLERABLE, false, "Bancos de peces");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Algeciras"), "Bajo del Bono", DateUtil.parseDate("2022-06-12"), 48, 38.2f, WeatherConditions.GOOD, true, "Magnífica inmersión");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "La Garita", DateUtil.parseDate("2022-07-24"), 55, 15.2f, WeatherConditions.GOOD, false, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive(locationMapByName.get("Tarifa"), "San Andrés", DateUtil.parseDate("2022-08-06"), 38, 45.3f, WeatherConditions.GOOD, false, "Espectacular inmersión");
        demoDiveList.add(dive);

        return demoDiveList;
    }

    private Map<String, DiveLocation> transformMapToLookForName(Map<Integer, DiveLocation> locationMap) {
        Map<String, DiveLocation> newLocationMap = new HashMap<>();

        for (DiveLocation diveLocation : locationMap.values()) {
            newLocationMap.put(diveLocation.getName(), diveLocation);
        }

        return newLocationMap;
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