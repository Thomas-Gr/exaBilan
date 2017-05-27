package com.exabilan.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.exabilan.TestBase;
import com.exabilan.interfaces.ExalangManager;
import com.exabilan.types.exalang.ExaLang;
import com.exabilan.types.exalang.Module;
import com.exabilan.types.exalang.Question;
import com.google.common.collect.ImmutableList;

import org.junit.Test;

public class JacksonExalangManagerIntegrationTest extends TestBase {

    @Inject private ExalangManager exalangManager;
    @Inject @Named("exalangNames") private ImmutableList<String> exalangNames;

    @Test
    public void givenAllExalangs_whenParse_thenCanBeParsed() throws Exception {
        // WHEN
        Set<ExaLang> allExalangs = exalangManager.getAllExalangs();

        // THEN
        assertThat(allExalangs).hasSameSizeAs(exalangNames);
        assertThatQuestionsAreNumberedInCorrectOrder(allExalangs);
    }

    private static void assertThatQuestionsAreNumberedInCorrectOrder(Set<ExaLang> allExalangs) {
        for (ExaLang exalang : allExalangs) {
            int questionNumber = 0;
            for (Module module : exalang.getModules()) {
                for (Question question : module.getQuestions()) {
                    assertThat(question.getNumber()).isEqualTo(++questionNumber);
                }
            }
        }
    }

}