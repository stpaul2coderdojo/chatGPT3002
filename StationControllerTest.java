package com.example.stations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class StationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StationRepository repository;

    @BeforeEach
    public void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    public void testGetAllStations() throws Exception {
        Station station1 = new Station();
        station1.setName("Station 1");
        station1.setPricing(1.99);
        station1.setAddress("123 Main St");
        repository.save(station1);

        Station station2 = new Station();
        station2.setName("Station 2");
        station2.setPricing(2.99);
        station2.setAddress("456 Oak St");
        repository.save(station2);

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(station1.getName())))
                .andExpect(jsonPath("$[0].pricing", is(station1.getPricing())))
                .andExpect(jsonPath("$[0].address", is(station1.getAddress())))
                .andExpect(jsonPath("$[1].name", is(station2.getName())))
                .andExpect(jsonPath("$[1].pricing", is(station2.getPricing())))
                .andExpect(jsonPath("$[1].address", is(station2.getAddress())));
    }

    @Test
    public void testGetStationById() throws Exception {
        Station station1 = new Station();
        station1.setName("Station 1");
        station1.setPricing(1.99);
        station1.setAddress("123 Main St");
        repository.save(station1);

        mockMvc.perform(MockMvcRequestBuilders.get("/" + station1.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name", is(station1.getName())))
                .andExpect(jsonPath("$.pricing", is(station1.getPricing())))
                .andExpect(jsonPath("$.address", is(station1.getAddress())));
    }

    @Test
    public void testGetStationByIdNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testAddStation() throws Exception {
        Station station = new Station();
        station.setName("New Station");
        station.setPricing(3.99);
        station.setAddress("789 Maple St");

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType("application/json")
                .content("{\"name\": \"" + station.getName() + "\", \"pricing\": " + station.getPricing() + ", \"address\": \"" + station.getAddress() + "\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk
