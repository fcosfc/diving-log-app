package es.upo.alu.fsaufer.dm.divinglogapp.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;

/**
 * Clase que implementa la persistencia de la APP
 */
public class DiveDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DivingLog.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TABLE_NAME = "dives";
    private static final String COL_DIVE_ID = "dive_id";
    private static final String COL_LOCATION = "location";
    private static final String COL_SPOT = "spot";
    private static final String COL_DIVE_DATE = "dive_date";
    private static final String COL_MINUTES = "minutes";
    private static final String COL_MAX_DEPTH = "max_depth";
    private static final String COL_REMARKS = "remarks";

    private final Context context;

    public DiveDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(TABLE_NAME);
        sqlBuilder.append("(");
        sqlBuilder.append(COL_DIVE_ID).append("primary key autoincrement, ");
        sqlBuilder.append(COL_LOCATION).append(" text not null, ");
        sqlBuilder.append(COL_SPOT).append(" text not null, ");
        sqlBuilder.append(COL_DIVE_DATE).append(" text not null, ");
        sqlBuilder.append(COL_MINUTES).append(" integer not null, ");
        sqlBuilder.append(COL_MAX_DEPTH).append(" real not null, ");
        sqlBuilder.append(COL_REMARKS).append(" text");
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
        String[] columns = new String[] {
            COL_DIVE_ID, COL_LOCATION, COL_SPOT, COL_DIVE_DATE, COL_MINUTES, COL_MAX_DEPTH, COL_REMARKS    
        };

        Cursor cursor = db.query(TABLE_NAME, columns, null, null,null, null, COL_DIVE_DATE + " DESC");

        if (cursor.getCount() != 0) {
            diveList = new ArrayList<>(cursor.getCount());
            Dive dive;

            while (cursor.moveToNext()) {
                dive = new Dive();

                dive.setDiveId(cursor.getInt(0));
                dive.setLocation(cursor.getString(1));
                dive.setSpot(cursor.getString(2));
                dive.setDiveDate(getDate(cursor.getString(3)));
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

    private void loadDemoData(SQLiteDatabase db) {
        List<Dive> demoDiveList = getDemoDiveList();

        for (Dive dive : demoDiveList) {
            insertDemoDive(db, dive);
        }

        Toast.makeText(context.getApplicationContext(), "Datos de ejemplo cargados", Toast.LENGTH_LONG).show();
    }

    private void insertDemoDive(SQLiteDatabase db, Dive dive) {
        ContentValues values = new ContentValues();

        values.put(COL_LOCATION, dive.getLocation());
        values.put(COL_SPOT, dive.getSpot());
        values.put(COL_DIVE_DATE, dive.getFormatedDiveDate());
        values.put(COL_MINUTES, dive.getMinutes());
        values.put(COL_MAX_DEPTH, dive.getMaxDepth());
        values.put(COL_REMARKS, dive.getRemarks());

        db.insert(TABLE_NAME, null, values);
    }

    private List<Dive> getDemoDiveList() {
        List<Dive> demoDiveList = new ArrayList<>();
        Dive dive;

        dive = new Dive("Tarifa", "La Garita", getDate("2022-01-08"), 55, 15.2f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Las piscinas", getDate("2022-01-24"), 45, 25.1f, "Fuimos a las laminarias");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Punta marroquí", getDate("2022-02-10"), 38, 40.2f, "Una pasada");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", getDate("2022-02-28"), 60, 14.1f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Poniente", getDate("2022-03-15"), 48, 20.4f, "Bancos de peces");
        demoDiveList.add(dive);
        dive = new Dive("Ceuta", "Ciclón de fuera", getDate("2022-04-01"), 40, 41.1f, "Magnífica inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "Ciclón de dentro", getDate("2022-04-01"), 38, 15.1f, "Mucha vida");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", getDate("2022-05-25"), 62, 15.1f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", getDate("2022-06-01"), 55, 15.2f, "Bancos de peces");
        demoDiveList.add(dive);
        dive = new Dive("Algeciras", "Bajo del Bono", getDate("2022-06-12"), 48, 38.2f, "Magnífica inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "La Garita", getDate("2022-07-24"), 55, 15.2f, "Buena inmersión");
        demoDiveList.add(dive);
        dive = new Dive("Tarifa", "San Andrés", getDate("2022-08-06"), 38, 45.3f, "Espectacular inmersión");
        demoDiveList.add(dive);
        
        return demoDiveList;
    }

    private Date getDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

}