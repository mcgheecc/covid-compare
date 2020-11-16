package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"date", "country_country_code"})
})
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryData implements Comparable<CountryData> {

    @Id
    @GeneratedValue(generator = "country_data_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="country_data_generator", sequenceName = "country_data_seq", allocationSize=1)
    private Long id;
    private LocalDate date;
    private int confirmed;
    private int deaths;
    private int recovered;
    @JsonProperty("confirmed_diff")
    private int casesToday;
    @JsonProperty("deaths_diff")
    private int deathsToday;
    @JsonProperty("recovered_diff")
    private int recoveredToday;
    @JsonProperty("last_update")
    private String lastUpdated;
    private int active;
    @JsonProperty("active_diff")
    private int activeDiff;
    @JsonProperty("fatality_rate")
    private double fatalityRate;
    @JsonProperty("region")
    @OneToOne(cascade = CascadeType.MERGE)
    private Country country;
    private int casesPerHundredThousand;

    @Override
    public int compareTo(CountryData countryData) {
        return countryData.getDate().compareTo(this.getDate());
    }
}
