package com.cmg.covidcompare.repository;

import com.cmg.covidcompare.domain.CountryData;
import java.time.LocalDate;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDataRepository extends JpaRepository<CountryData, Long> {

    @Query("from CountryData where country_country_code =:countryCode and date >= :startDate and date <= :endDate")
    Set<CountryData> getForDatesAndCountry(@Param("countryCode") String countryCode, @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);
}
