package es.upo.alu.fsaufer.dm.divinglogapp.entity;

/**
 * Clase contenedora de los parámetros de configuración
 */
public class ConfigParameters {
    private boolean demoMode;
    private boolean nitroxUseByDefault;

    public ConfigParameters() {
        demoMode = false;
        nitroxUseByDefault = false;
    }

    public boolean isDemoMode() {
        return demoMode;
    }

    public void setDemoMode(boolean demoMode) {
        this.demoMode = demoMode;
    }

    public boolean isNitroxUseByDefault() {
        return nitroxUseByDefault;
    }

    public void setNitroxUseByDefault(boolean nitroxUseByDefault) {
        this.nitroxUseByDefault = nitroxUseByDefault;
    }
}
