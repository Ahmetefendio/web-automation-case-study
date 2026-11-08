package com.nesine.poc.config;

import com.nesine.poc.model.CouponFilter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class TestData {

    private TestData() {
    }

    public static List<CouponFilter> couponFilters() {
        String resource = ConfigReader.get("testdata.filters");
        try (InputStream in = TestData.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Not found on classpath: " + resource);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8).lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(TestData::toCouponFilter)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resource, e);
        }
    }

    // empty maxEvents means no upper limit ("+5 Maç")
    private static CouponFilter toCouponFilter(String line) {
        String[] columns = line.split(",", -1);
        int max = columns[2].isBlank() ? Integer.MAX_VALUE : Integer.parseInt(columns[2].trim());
        return new CouponFilter(columns[0].trim(), Integer.parseInt(columns[1].trim()), max);
    }
}
