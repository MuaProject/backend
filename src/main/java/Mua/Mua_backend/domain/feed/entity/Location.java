package Mua.Mua_backend.domain.feed.entity;

import Mua.Mua_backend.global.exception.feed.InvalidFeedLocationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    private String address;
    private Double latitude;
    private Double longitude;

    private Location(String address, Double latitude, Double longitude) {
        validate(latitude, longitude);
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location of(String address, Double latitude, Double longitude) {
        return new Location(address, latitude, longitude);
    }

    private void validate(Double latitude, Double longitude) {
        if (latitude != null && (latitude < -90 || latitude > 90)) {
            throw new InvalidFeedLocationException();
        }
        if (longitude != null && (longitude < -180 || longitude > 180)) {
            throw new InvalidFeedLocationException();
        }
    }
}
