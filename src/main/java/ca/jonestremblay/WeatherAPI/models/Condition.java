package ca.jonestremblay.WeatherAPI.models;

import lombok.Data;

@Data
public class Condition {
    private String text;
    private String icon;
    private int code;
}
