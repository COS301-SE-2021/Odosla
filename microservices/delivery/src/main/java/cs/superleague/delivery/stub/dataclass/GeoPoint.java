package cs.superleague.delivery.stub.dataclass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "geoPointTable")
public class GeoPoint {
    private Double latitude;
    private Double longitude;
    private String address;
    @Id
    @GeneratedValue
    private Long geoID;

    public GeoPoint(Double latitude, Double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public GeoPoint() {

    }

    public void setGeoID(Long geoID) {
        this.geoID = geoID;
    }

    public Long getGeoID() {
        return geoID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
