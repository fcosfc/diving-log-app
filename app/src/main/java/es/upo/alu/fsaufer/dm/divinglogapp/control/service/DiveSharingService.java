package es.upo.alu.fsaufer.dm.divinglogapp.control.service;

import android.content.Context;
import android.content.Intent;

import es.upo.alu.fsaufer.dm.divinglogapp.R;
import es.upo.alu.fsaufer.dm.divinglogapp.entity.Dive;

/**
 * Clase para compartir datos con otras aplicaciones
 */
public class DiveSharingService {

    private static final String SHARE_TYPE = "text/plain";

    public static void share(Context context, Dive dive) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getDiveData(context, dive));
        sendIntent.setType(SHARE_TYPE);

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }

    private static String getDiveData(Context context, Dive dive) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.dive_share_message)).append(":\n");
        builder.append(context.getString(R.string.dive_location)).append(": ").append(dive.getLocation().getName()).append("\n");
        builder.append(context.getString(R.string.spot)).append(": ").append(dive.getSpot()).append("\n");
        builder.append(context.getString(R.string.dive_date)).append(": ").append(dive.getFormattedDiveDate()).append("\n");
        builder.append(context.getString(R.string.weather_conditions)).append(": ");
        switch (dive.getWeatherConditions()) {
            case GOOD:
                builder.append(context.getString(R.string.good));
                break;
            case TOLERABLE:
                builder.append(context.getString(R.string.tolerable));
                break;
            case BAD:
                builder.append(context.getString(R.string.bad));
                break;
        }
        builder.append("\n");
        builder.append(context.getString(R.string.minutes)).append(": ").append(dive.getMinutes()).append("\n");
        builder.append(context.getString(R.string.max_depth)).append(": ").append(dive.getMaxDepth()).append("\n");
        builder.append(dive.isNitroxUse() ? context.getString(R.string.nitrox_use) : context.getString(R.string.without_nitrox_use)).append("\n");
        builder.append(context.getString(R.string.remarks)).append(": ").append(dive.getRemarks());

        return builder.toString();
    }

}
