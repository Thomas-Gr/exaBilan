package com.exabilan.component.helper;

import static java.time.LocalDate.now;
import static com.exabilan.component.helper.Helpers.computeAge;
import static com.exabilan.component.helper.Helpers.displayNumber;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class HelpersTest {

    @Test
    public void givenValueWithNoDecimal_whenDisplayNumber_thenNoDecimalReturned() {
        assertThat(displayNumber(42)).isEqualTo("42");
    }

    @Test
    public void givenValueWithTwoDecimals_whenDisplayNumber_thenTwoDecimalAreReturned() {
        assertThat(displayNumber(3.14)).isEqualTo("3,14");
    }

    @Test
    public void givenValueWithMoreThanTwoDecimals_whenDisplayNumber_thenTwoDecimalAreReturned() {
        assertThat(displayNumber(3.1415)).isEqualTo("3,14");
    }

    @Test
    public void givenDifferenceInYearsExactly_whenConputeAge_thenReturnsOnlyNumberOfYears() {
        assertThat(computeAge(now().minusYears(20))).isEqualTo("20 ans");
    }

    @Test
    public void givenDifferenceInYearsAndMonths_whenConputeAge_thenReturnsOnlyNumberOfYears() {
        assertThat(computeAge(now().minusYears(20).minusMonths(3))).isEqualTo("20 ans et 3 mois");
    }

}