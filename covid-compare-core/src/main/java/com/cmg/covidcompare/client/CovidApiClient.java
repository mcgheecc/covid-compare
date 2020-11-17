package com.cmg.covidcompare.client;

import com.cmg.covidcompare.domain.CountryDataDto;
import com.cmg.covidcompare.domain.CountryDataResponse;
import com.cmg.covidcompare.domain.CountryResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class CovidApiClient {

    private RestTemplate restTemplate = new RestTemplate();
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Value("${covid-api-base-url}regions")
    private String countryUrl;
    @Value("${covid-api-base-url}${covid-api-country-report-uri}")
    private String reportUrl;

    private final static LocalDate UK_DATA_FORMAT_CHANGE = LocalDate.parse("2020-06-10", formatter);
    private final static String UNITED_KINGDOM_COUNTRY_CODE = "GBR";
    private final static String[] UK_COUNTRIES = {"Scotland", "England", "Northern Ireland", "Wales"};


    public CountryResponse getCountries() {
        return restTemplate.getForObject(countryUrl, CountryResponse.class);
    }

    public Optional<CountryDataDto> getCountryStatus(String countryCode, LocalDate date) {
        String url = getUrl(countryCode, date);
        try {
            CountryDataResponse response = restTemplate.getForObject(url, CountryDataResponse.class);
            if (response != null && !response.getData().isEmpty()) {
                List<CountryDataDto> list;
                if (isUnitedKingdom(countryCode) && isAfterUkDataFormatChangeDate(date)) {
                    list = response.getData().stream()
                        .filter(countryDataDto -> Arrays.asList(UK_COUNTRIES).contains(countryDataDto.getRegion().getProvince()))
                        .collect(Collectors.toList());
                } else {
                    list = response.getData();
                }

                CountryDataDto data = response.getData().get(0);
                int totalCasesToday = list.stream().mapToInt(CountryDataDto::getCasesToday).sum();
                data.setCasesToday(totalCasesToday);
                int totalDeathsToday = list.stream().mapToInt(CountryDataDto::getDeathsToday).sum();
                data.setDeathsToday(totalDeathsToday);
                return Optional.of(data);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e);
        }
        return Optional.empty();
    }

    private String getUrl(String countryCode, LocalDate date) {
        String dateStr = date.format(formatter);
        return String.format(reportUrl, dateStr, countryCode);
    }

    private boolean isUnitedKingdom(String countryCode) {
        return UNITED_KINGDOM_COUNTRY_CODE.equals(countryCode);
    }

    private boolean isAfterUkDataFormatChangeDate(LocalDate date) {
        return UK_DATA_FORMAT_CHANGE.isBefore(date);
    }

    void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    void setCountryUrl(String countryUrl) {
        this.countryUrl = countryUrl;
    }

    void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }
}
