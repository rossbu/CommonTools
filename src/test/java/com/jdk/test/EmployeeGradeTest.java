package com.jdk.test;

import static junit.framework.TestCase.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeGradeTest {

    public enum Grade {
        A, B, C, OTHER;

        @JsonCreator
        public static Grade safeValueOf(String string) {
            try {
                return Grade.valueOf(string);
            } catch (IllegalArgumentException e) {
                return OTHER;
            }
        }
    }

    @Test
    public void deserialize() throws IOException {
        assertEquals(Grade.A, new ObjectMapper().readValue("\"A\"", Grade.class));
    }

    @Test
    public void deserializeNewValue() throws IOException {
        assertEquals(Grade.OTHER, new ObjectMapper().readValue("\"D\"", Grade.class));
    }
}