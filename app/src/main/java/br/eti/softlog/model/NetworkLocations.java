package br.eti.softlog.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "network_locations"
)
public class NetworkLocations {

    @Id(autoincrement = true)
    Long id;

    @Property(nameInDb = "day")
    int day;

    @Property(nameInDb = "hour")
    int hour;

    @Property(nameInDb = "milliseconds")
    int milliseconds;

    @Property(nameInDb = "latitude")
    Double latitude;

    @Property(nameInDb = "longitude")
    Double longitute;

    @Property(nameInDb = "altitude")
    Double altitude;

    @Property(nameInDb = "gpsStart")
    int gpsStart;

    @Property(nameInDb = "accuracy")
    Double accuracy;

    @Property(nameInDb = "bearing")
    Double bearing;

    @Property(nameInDb = "speed")
    Double speed;

@Generated(hash = 1815962103)
public NetworkLocations(Long id, int day, int hour, int milliseconds,
        Double latitude, Double longitute, Double altitude, int gpsStart,
        Double accuracy, Double bearing, Double speed) {
    this.id = id;
    this.day = day;
    this.hour = hour;
    this.milliseconds = milliseconds;
    this.latitude = latitude;
    this.longitute = longitute;
    this.altitude = altitude;
    this.gpsStart = gpsStart;
    this.accuracy = accuracy;
    this.bearing = bearing;
    this.speed = speed;
}

@Generated(hash = 2009594461)
public NetworkLocations() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public int getDay() {
    return this.day;
}

public void setDay(int day) {
    this.day = day;
}

public int getHour() {
    return this.hour;
}

public void setHour(int hour) {
    this.hour = hour;
}

public int getMilliseconds() {
    return this.milliseconds;
}

public void setMilliseconds(int milliseconds) {
    this.milliseconds = milliseconds;
}

public Double getLatitude() {
    return this.latitude;
}

public void setLatitude(Double latitude) {
    this.latitude = latitude;
}

public Double getLongitute() {
    return this.longitute;
}

public void setLongitute(Double longitute) {
    this.longitute = longitute;
}

public Double getAltitude() {
    return this.altitude;
}

public void setAltitude(Double altitude) {
    this.altitude = altitude;
}

public int getGpsStart() {
    return this.gpsStart;
}

public void setGpsStart(int gpsStart) {
    this.gpsStart = gpsStart;
}

public Double getAccuracy() {
    return this.accuracy;
}

public void setAccuracy(Double accuracy) {
    this.accuracy = accuracy;
}

public Double getBearing() {
    return this.bearing;
}

public void setBearing(Double bearing) {
    this.bearing = bearing;
}

public Double getSpeed() {
    return this.speed;
}

public void setSpeed(Double speed) {
    this.speed = speed;
}

}
