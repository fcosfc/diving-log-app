package es.upo.alu.fsaufer.dm.divinglogapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherServiceResponse {

    private String name;
    private MainWeatherData main;
    private WindData wind;
    private CloudData clouds;

    public WeatherServiceResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainWeatherData getMain() {
        return main;
    }

    public void setMain(MainWeatherData main) {
        this.main = main;
    }

    public WindData getWind() {
        return wind;
    }

    public void setWind(WindData wind) {
        this.wind = wind;
    }

    public CloudData getClouds() {
        return clouds;
    }

    public void setClouds(CloudData clouds) {
        this.clouds = clouds;
    }

    @Override
    public String toString() {
        return "WeatherServiceResponse{" +
                "name='" + name + '\'' +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class MainWeatherData {

        @JsonProperty("feels_like")
        private float temperature;
        private float humidity;

        public MainWeatherData() {
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public float getHumidity() {
            return humidity;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }

        @Override
        public String toString() {
            return "MainWeatherData{" +
                    "temperature=" + temperature +
                    ", humidity=" + humidity +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class WindData {

        private float speed;

        public WindData() {
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        @Override
        public String toString() {
            return "WindData{" +
                    "speed=" + speed +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class CloudData {

        @JsonProperty("all")
        private int percentaje;

        public CloudData() {
        }

        public int getPercentaje() {
            return percentaje;
        }

        public void setPercentaje(int percentaje) {
            this.percentaje = percentaje;
        }

        @Override
        public String toString() {
            return "CloudData{" +
                    "percentaje=" + percentaje +
                    '}';
        }
    }

}
