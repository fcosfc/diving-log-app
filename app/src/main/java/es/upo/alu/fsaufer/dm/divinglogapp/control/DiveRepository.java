package es.upo.alu.fsaufer.dm.divinglogapp.control;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Location;

/**
 * Clase repositorio de la APP
 */
public class DiveRepository {
    private static Map<Integer, Location> locationMapById;
    private static Map<String, Location> locationMapByName;

    private static List<String> locationList;

    private static List<Dive> diveList;

    private static DiveDbHelper diveDbHelper;

    public static void init(Context context) {
        diveDbHelper = new DiveDbHelper(context);

        refreshLocationList();
        refreshDiveList();
    }

    public static List<String> getLocationList() { return locationList; }

    public static Location getLocation(String name) {
        return locationMapByName.get(name);
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

    public static void delete(Dive dive) {
        diveDbHelper.delete(dive.getDiveId());

        refreshDiveList();
    }

    public static void refreshDiveList() {
        diveList = diveDbHelper.readAllDives(locationMapById);
    }

    private static List<String> getLocationListFromMap(Map<Integer, Location> input) {
        List<String> output = new ArrayList<>();

        for(Location location : input.values()) {
            output.add(location.getName());
        }

        return output;
    }

    private static Map<String, Location> getLocationMapByName(Map<Integer, Location> input) {
        Map<String, Location> output = new HashMap<>();

        for(Location location : input.values()) {
            output.put(location.getName(), location);
        }

        return output;
    }
}
