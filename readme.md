develop an API in java with spring and spring boot, with database schema including station ID, name, image, pricing and address, and GET request to url should return all stations, GET to /show/id should return details of that id. POST to / should add details of that station, put to /id/edit should edit the details of that id, delete to /delete/id should delete that id and GET to /?10 should output only ten entries and GET to /?sort=asc&param=station_price should return the stations sorted in ascending cost, output in JSON

Sure, here's an example of a Java API using Spring Boot that implements the endpoints and database schema you described:

First, let's create a Station model class with the necessary fields:

java

package com.example.stations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;
    private String image;
    private Double pricing;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPricing() {
        return pricing;
    }

    public void setPricing(Double pricing) {
        this.pricing = pricing;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

This model class includes the id, name, image, pricing, and address fields, with corresponding getters and setters. The @Entity annotation indicates that this class should be mapped to a database table.

Next, we'll create a StationRepository interface that extends JpaRepository:

java

package com.example.stations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
}

This interface provides methods for common database operations on Station entities, such as findAll(), findById(), save(), deleteById(), etc.

Now, let's create a StationController class that defines the API endpoints:

java

package com.example.stations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StationController {

    @Autowired
    private StationRepository repository;

    @GetMapping("/")
    public List<Station> getAllStations(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(sortBy))).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable("id") Long id) {
        Optional<Station> stationData = repository.findById(id);

        if (stationData.isPresent()) {
            return new ResponseEntity<>(stationData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public Station addStation(@RequestBody Station station) {
        return repository.save(station);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable("id") Long id, @RequestBody Station station) {
        Optional<Station> stationData = repository.findById(id);

        if (stationData.isPresent()) {
            Station updatedStation = stationData.get();
            updatedStation.setName(station.getName());
            updatedStation.setImage(station.getImage());
            updatedStation.setPricing(station.getPricing());
            updatedStation.setAddress(station.getAddress());

            return new ResponseEntity<>(repository.save(updatedStation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

