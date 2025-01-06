package com.ssl.noaa_species.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssl.noaa_species.model.Species;
import com.ssl.noaa_species.service.SpeciesDataService;

@RestController
@RequestMapping("api/v1")
public class SpeciesDataController {
    private final SpeciesDataService speciesDataService;

    public SpeciesDataController(SpeciesDataService speciesDataService) {
        this.speciesDataService = speciesDataService;
    }

    private final Map<String, String> CATEGORY_URLS = Map.of(
        "fish-sharks", "https://www.fisheries.noaa.gov/fish-sharks#by-species",
        "whales", "https://www.fisheries.noaa.gov/whales#by-species",
        "dolphins-porpoises", "https://www.fisheries.noaa.gov/dolphins-porpoises#by-species",
        "invertebrates", "https://www.fisheries.noaa.gov/invertebrates#by-species",
        "sea-turtles", "https://www.fisheries.noaa.gov/sea-turtles#by-species",
        "seals-sea-lions", "https://www.fisheries.noaa.gov/seals-sea-lions#by-species"
    );

    @GetMapping
    public List<Species> getSpeciesData(@RequestParam String category) {
        String url = CATEGORY_URLS.get(category.toLowerCase());

        if (url == null) {
            throw new IllegalArgumentException("Category not found: " + category);
        }

        return speciesDataService.scrapeSpeciesData(url);
    }
}
