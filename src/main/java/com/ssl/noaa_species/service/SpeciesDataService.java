package com.ssl.noaa_species.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.ssl.noaa_species.model.Species;

@Service
public class SpeciesDataService {
    private static final String BASE_URL = "https://www.fisheries.noaa.gov/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36";
    private static final int TIMEOUT_MS = 160000;
    private static final int THREAD_SLEEP_MIN = 500;
    private static final int THREAD_SLEEP_MAX = 1500;

    public Species scrapeSpeciesDetails(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        String name = extractText(doc, ".species-overview__header-name span");
        String scientificName = extractText(doc, ".species-overview__header-subname");
        String imageUrl = BASE_URL + extractAttribute(doc, ".aspect-ratio__content img", "src");
        String alsoKnownAs = extractText(doc, ".species-overview__aka-value");

        Map<String, String> additionalDetails = extractKeyValuePairs(doc, ".species-overview__facts-label", ".species-overview__facts-value");
        Map<String, String> scientificClassification = extractKeyValuePairs(doc, ".species-profile__table-header", ".species-profile__table-data");

        return new Species(name, scientificName, alsoKnownAs, imageUrl, additionalDetails, scientificClassification);
    }

    public List<Species> scrapeSpeciesData(String url) {
        List<Species> speciesList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Species>> futureList = new ArrayList<>();
        Random random = new Random();

        try {
            while (url != null) {
                try {
                    Document mainDoc = Jsoup.connect(url)
                            .userAgent(USER_AGENT)
                            .timeout(TIMEOUT_MS)
                            .followRedirects(true)
                            .get();

                    Elements fishItems = mainDoc.select(".horizontal-list__item .species-card h3");
                    for (Element fishItem : fishItems) {
                        String fishDetailUrl = constructDetailUrl(fishItem.text());
                        Future<Species> future = executor.submit(() -> scrapeSpeciesDetails(fishDetailUrl));
                        futureList.add(future);

                        Thread.sleep(THREAD_SLEEP_MIN + random.nextInt(THREAD_SLEEP_MAX - THREAD_SLEEP_MIN));
                    }

                    url = getNextPageUrl(mainDoc);
                } catch (IOException e) {
                    System.err.println("Error accessing page: " + url);
                    e.printStackTrace();
                    break;
                }
            }

            for (Future<Species> future : futureList) {
                try {
                    speciesList.add(future.get());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Execution interrupted.");
        } finally {
            executor.shutdown();
        }

        return speciesList;
    }

    private String extractText(Document doc, String cssQuery) {
        return doc.select(cssQuery).text();
    }

    private String extractAttribute(Document doc, String cssQuery, String attribute) {
        return doc.select(cssQuery).attr(attribute);
    }

    private Map<String, String> extractKeyValuePairs(Document doc, String keySelector, String valueSelector) {
        Map<String, String> keyValueMap = new HashMap<>();
        Elements keys = doc.select(keySelector);
        Elements values = doc.select(valueSelector);

        for (int i = 0; i < keys.size(); i++) {
            if (i < values.size()) {
                keyValueMap.put(keys.get(i).text().trim(), values.get(i).text().trim());
            }
        }
        return keyValueMap;
    }

    private String constructDetailUrl(String scientificName) {
        return BASE_URL + "species/" + scientificName
                .replace(" of ", "-")
                .replace(" ", "-")
                .replaceAll("[()’']", "")
                .replaceAll("Ō", "o")
                .toLowerCase();
    }

    private String getNextPageUrl(Document doc) {
        Element nextPageLink = doc.selectFirst(".pager__item--next a[href]");
        return nextPageLink != null ? nextPageLink.absUrl("href") : null;
    }

}
