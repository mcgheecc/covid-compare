package com.cmg.covidcompare.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Country implements Serializable {

    @Id
    @JsonProperty("iso")
    private String countryCode;
    private String name;
    private Date latestUpdate;
    private int population;
}
