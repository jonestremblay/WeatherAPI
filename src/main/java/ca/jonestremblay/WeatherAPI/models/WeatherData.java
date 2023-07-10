package ca.jonestremblay.WeatherAPI.models;


import lombok.Data;

@Data
public class WeatherData {
    private Location location;
    private CurrentWeather current;

    // Getters and setters

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }


}

