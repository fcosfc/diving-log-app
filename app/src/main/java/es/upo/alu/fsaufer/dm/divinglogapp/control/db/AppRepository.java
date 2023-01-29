package es.upo.alu.fsaufer.dm.divinglogapp.control.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.DiveLocation;

/**
 * Repositorio de la APP
 */
public class AppRepository {
    private static Map<Integer, DiveLocation> locationMapById;
    private static Map<String, DiveLocation> locationMapByName;

    private static List<String> locationList;

    private static List<Dive> diveList;

    private static DiveDbHelper diveDbHelper;

    public static void init(Context context) {
        diveDbHelper = new DiveDbHelper(context);

        refreshLocationList();
        refreshDiveList();
    }

    public static List<String> getLocationList() { return locationList; }

    public static DiveLocation getLocation(String name) {
        return locationMapByName.get(name);
    }

    public static Map<String, DiveLocation> getLocationMap() {
        return locationMapByName;
    }

    public static void refreshLocationList() {
        locationMapById = diveDbHelper.readAllLocations();
        locationMapByName = getLocationMapByName(locationMapById);
        locationList = getLocationListFromMap(locationMapById);
    }

    public static List<Dive> getDiveList() {
        return diveList;
    }

    public static void save(Dive dive) {
        diveDbHelper.save(dive);

        refreshDiveList();
    }

    public static void save(DiveLocation diveLocation) throws LocationAlreadyExistsException {
        if (locationMapByName != null && !locationMapByName.isEmpty()) {
            if (locationMapByName.get(diveLocation.getName()) != null) {
                throw new LocationAlreadyExistsException();
            }
        }
        diveDbHelper.save(diveLocation);

        refreshLocationList();
    }

    public static void delete(Dive dive) {
        diveDbHelper.delete(dive.getDiveId());

        refreshDiveList();
    }

    public static void refreshDiveList() {
        diveList = diveDbHelper.readAllDives(locationMapById);
    }

    private static List<String> getLocationListFromMap(Map<Integer, DiveLocation> input) {
        List<String> output = new ArrayList<>();

        if (input != null && !input.isEmpty()) {
            for (DiveLocation diveLocation : input.values()) {
                output.add(diveLocation.getName());
            }
        }

        return output;
    }

    private static Map<String, DiveLocation> getLocationMapByName(Map<Integer, DiveLocation> input) {
        Map<String, DiveLocation> output = new HashMap<>();

        if (input != null && !input.isEmpty()) {
            for (DiveLocation diveLocation : input.values()) {
                output.put(diveLocation.getName(), diveLocation);
            }


        }

        return output;
    }
}
