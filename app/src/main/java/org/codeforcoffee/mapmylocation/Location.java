package org.codeforcoffee.mapmylocation;

/**
 * Created by codeforcoffee on 7/18/16.
 */
public class Location {

    public String name;
    public String city;
    public String phone;
    public double lat;
    public double lon;

    public Location(String name, String city, String phone, double lat, double lon) {
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
