package com.cmg.covidcompare.batch.updatecountries.job;

import static org.mockito.Mockito.verify;

import com.cmg.covidcompare.domain.Country;
import com.cmg.covidcompare.repository.CountryRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateCountriesWriterTest {

    @InjectMocks
    private UpdateCountriesWriter writer;

    @Spy
    private CountryRepository countryRepository;

    @Test
    void write() {
        Country country = new Country();
        List<Country> list = Collections.singletonList(country);

        writer.write(Collections.singletonList(country));

        verify(countryRepository).saveAll(list);
    }
}
