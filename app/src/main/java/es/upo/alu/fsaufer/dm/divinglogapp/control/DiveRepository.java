package es.upo.alu.fsaufer.dm.divinglogapp.control;

import android.content.Context;

import java.util.List;

import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;

/**
 * Clase repositorio de la APP
 */
public class DiveRepository {
    private static List<Dive> diveList;

    private static DiveDbHelper diveDbHelper;

    public static void init(Context context) {
        diveDbHelper = new DiveDbHelper(context);

        refreshDiveList();
    }

    public static List<Dive> getDiveList() {
        return diveList;
    }

    public static void save(Dive dive) {
        diveDbHelper.save(dive);

        refreshDiveList();
    }

    public static void delete(Dive dive) {
        diveDbHelper.delete(dive.getDiveId());

        refreshDiveList();
    }

    public static void refreshDiveList() {
        diveList = diveDbHelper.readAllData();
    }
}
