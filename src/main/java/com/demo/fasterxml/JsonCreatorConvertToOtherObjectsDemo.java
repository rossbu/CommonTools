package com.demo.fasterxml;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonCreatorConvertToOtherObjectsDemo {
    public static void main(String[] args) {
        String json = "{\"state\" : \"OPEN\", \"startDate\" : [2020, 9, 22], \"endDate\" : [2020, 9, 24]}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            Test test = mapper.readValue(json, Test.class);
            System.out.println(test.getStartDate() + "   " + test.getEndDate()
                    + "   " + test.getState());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
class Test {
    private String state;
    private int startDate;
    private int endDate;

    @JsonCreator
    public Test(@JsonProperty("startDate") List<Integer> startDate,
                @JsonProperty("endDate") List<Integer> endDate) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer value : startDate) {
            stringBuilder.append(value);
        }
        this.startDate = Integer.parseInt(stringBuilder.toString());
        stringBuilder.setLength(0);
        for (Integer value : endDate) {
            stringBuilder.append(value);
        }
        this.endDate = Integer.parseInt(stringBuilder.toString());
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the startDate
     */
    public int getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public int getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}