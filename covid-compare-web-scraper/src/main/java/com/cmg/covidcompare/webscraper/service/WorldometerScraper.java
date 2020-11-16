package com.cmg.covidcompare.webscraper.service;

import com.cmg.covidcompare.webscraper.model.CountryDayData;
import com.cmg.covidcompare.webscraper.worldometer.WorldometerMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Data
public class WorldometerScraper {

    private static final String POPULATION_DIV_XPATH = "//div[@class='col-md-8 country-pop-description']";

    private static final String ELEMENT_PREFIX = "newsdate";

    @Value("${urls.worldometer.country.coronavirus}")
    private String worldometersCoronavirusUrl = "https://www.worldometers.info/coronavirus/country/";

    @Value("${urls.worldometer.country.population}")
    private String worldometersPopulationUrl = "";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final WebClient client;

    private final WorldometerMapper mapper;

    private Map<String, String> countryNameMap;

    @Autowired
    public WorldometerScraper(WebClient client, WorldometerMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public Optional<Integer> getPopulation(String countryName) {
        try {
            Optional<Integer> element = getPopulationElement(countryName);
            if (element.isPresent()) {
                return element;
            }
        } catch(Exception e){
            log.error("Unable to retrieve population for country {} : {}", countryName, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<CountryDayData> getStats(String countryName, LocalDate date) {
        try {
            Optional<DomElement> element = getNewsDateElement(countryName, date);
            if (element.isPresent()) {
                CountryDayData countryDayData = mapper.mapCountryDayData(element.get(), countryName, date);
                return Optional.of(countryDayData);
            }
        } catch(Exception e){
           log.error(e);
        }
        return Optional.empty();
    }

    private Optional<DomElement> getNewsDateElement(String countryName, LocalDate date) throws IOException {
        String url = worldometersCoronavirusUrl + parseCountryName(countryName);
        log.info("Attempting to get daily stats from {}", url);
        HtmlPage page = getPage(url);
        return getNewsDateElement(page, date);
    }

    private Optional<DomElement> getNewsDateElement(HtmlPage page, LocalDate date) {
        if (page != null) {
            var elementId = String.format("%s%s", ELEMENT_PREFIX, formatter.format(date));
            DomElement element = page.getElementById(elementId);
            if (element != null) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> getPopulationElement(String countryName) throws IOException {
        String url = String.format(worldometersPopulationUrl, parseCountryName(countryName));
        HtmlPage page = getPage(url);
        return getPopulationElement(page, countryName);
    }

    private Optional<Integer> getPopulationElement(HtmlPage page, String countryName) {
        if (page != null) {
            List<DomElement> elements = page.getByXPath(POPULATION_DIV_XPATH);
            if (elements != null) {
                DomElement element = elements.get(0);
                try {
                    return getPopulation(element, countryName);
                } catch (Exception notImportantSoCanJustLogAndIgnore) {
                    log.info("Unable to extract population for {}", countryName, notImportantSoCanJustLogAndIgnore);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> getPopulation(DomElement element, String countryName) {
        String text = element.getElementsByTagName("strong").get(1).getTextContent().replaceAll(",", "");
        Integer population = Integer.parseInt(text);
        log.debug("Extracted population {} : {}", countryName, population);
        return Optional.of(population);
    }

    private HtmlPage getPage(String url) throws IOException {
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        return client.getPage(url);
    }

    private String parseCountryName(String countryName) {
        return mapName(countryName).toLowerCase().replaceAll(" ", "-");
    }

    private String mapName(String countryName) {
        return countryNameMap.getOrDefault(countryName, countryName);
    }

    @PostConstruct
    void initMap() {
        countryNameMap = new HashMap<>();
        countryNameMap.put("Korea, South", "south-korea");
        countryNameMap.put("United Kingdom", "uk");
        countryNameMap.put("Faroe Islands", "faeroe-islands");
        countryNameMap.put("Brunei", "brunei-darussalam");
        countryNameMap.put("Macao SAR", "china-macao-sar");
        countryNameMap.put("Taiwan*", "taiwan");
        countryNameMap.put("Congo (Kinshasa)", "congo");
        countryNameMap.put("Jersey", "channel-islands");
        countryNameMap.put("Eswatini", "swaziland");
        countryNameMap.put("Guernsey", "channel-islands");
        countryNameMap.put("Congo (Brazzaville)", "congo");
        countryNameMap.put("Czechia", "czech-republic");
    }
}
