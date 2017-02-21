package com.exabilan.core.helper;

import static com.exabilan.core.helper.StyleApplier.applyMostSpecificType;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class StyleApplierTest {
    private static final List<String> ELEMENTS = ImmutableList.of("First", "Second");
    private static final List<String> MORE_ELEMENTS = ImmutableList.of("", "a", "bb", "ccc", "dddd");

    private static final Function<String, Integer> SIMPLE_FUNCTION = String::length;
    private static final Function<String, Integer> FUNCTION_THAT_CREATES_NULL =
            value -> value.length() % 2 == 0 ? null : value.length();
    private static final Function<Integer, Double> INTEGER_CONVERTER = value -> (double) value + 0.1;

    @Mock private static Consumer<Integer> integerConsumerMock;
    @Mock private static Consumer<Double> doubleConsumerMock;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenNoNullAndSimpleFunction_whenApplyMostSpecificTypeForIdentity_thenReturnsExpectedValue() {
        // WHEN
        applyMostSpecificType(ELEMENTS, SIMPLE_FUNCTION, integerConsumerMock);

        // THEN
        verify(integerConsumerMock).accept(6);
    }

    @Test
    public void givenNoNullsButFunctionThatGeneratesNull_whenApplyMostSpecificTypeForIdentity_thenReturnsExpectedValue() {
        // WHEN
        applyMostSpecificType(MORE_ELEMENTS, FUNCTION_THAT_CREATES_NULL, integerConsumerMock);

        // THEN
        verify(integerConsumerMock).accept(3);
    }

    @Test
    public void givenNoNullAndSimpleFunction_whenApplyMostSpecificType_thenReturnsExpectedValue() {
        // WHEN
        applyMostSpecificType(ELEMENTS, SIMPLE_FUNCTION, doubleConsumerMock, INTEGER_CONVERTER);

        // THEN
        verify(doubleConsumerMock).accept(6.1);
    }

    @Test
    public void givenNoNullsButFunctionThatGeneratesNull_whenApplyMostSpecificType_thenReturnsExpectedValue() {
        // WHEN
        applyMostSpecificType(MORE_ELEMENTS, FUNCTION_THAT_CREATES_NULL, doubleConsumerMock, INTEGER_CONVERTER);

        // THEN
        verify(doubleConsumerMock).accept(3.1);
    }

}