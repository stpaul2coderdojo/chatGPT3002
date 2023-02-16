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
