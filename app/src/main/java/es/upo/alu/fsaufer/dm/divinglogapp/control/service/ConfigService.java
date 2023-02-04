package es.upo.alu.fsaufer.dm.divinglogapp.control.service;

import android.content.Context;
import android.content.SharedPreferences;

import es.upo.alu.fsaufer.dm.divinglogapp.entity.ConfigParameters;
import es.upo.alu.fsaufer.dm.divinglogapp.util.Constant;

/**
 * Clase para gestionar los parámetros de configuración de la aplicación
 */
public class ConfigService {

    private static ConfigParameters configParameters = null;
    private static SharedPreferences sharedPreferences = null;

    public static ConfigParameters getConfigParameters(Context context) {
        if (configParameters == null) {
            configParameters = loadConfigParameters(context);
        }

        return configParameters;
    }

    public static void saveConfigParameters(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(Constant.CONFIG_PARAM_DEBUB_MODE, getConfigParameters(context).isDemoMode());
        editor.putBoolean(Constant.CONFIG_PARAM_NITROX_USE, getConfigParameters(context).isNitroxUseByDefault());
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Constant.CONFIG_PARAMS_FILE_ID, Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    private static ConfigParameters loadConfigParameters(Context context) {
        configParameters = new ConfigParameters();
        configParameters.setDemoMode(getSharedPreferences(context).getBoolean(Constant.CONFIG_PARAM_DEBUB_MODE, false));
        configParameters.setNitroxUseByDefault(getSharedPreferences(context).getBoolean(Constant.CONFIG_PARAM_NITROX_USE, false));

        return configParameters;
    }

}
