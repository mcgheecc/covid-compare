package com.cmg.covidcompare.repository;

import com.cmg.covidcompare.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {

}
