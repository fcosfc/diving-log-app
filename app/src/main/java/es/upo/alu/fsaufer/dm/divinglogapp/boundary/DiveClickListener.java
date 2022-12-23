package es.upo.alu.fsaufer.dm.divinglogapp.boundary;

import android.view.View;

/**
 * Interfaz para escuchadores de eventos de pulsación sobre líneas de inmersión
 */
public interface DiveClickListener {
    void onClick(View view, int position);
}
