package com.demo.guava;

/**
 * Created by tbu on 6/25/2014.
 */

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tbu
 */
public class GuavaTest {

    public static void main(String[] arg) throws ApiException {

        GuavaTest gt = new GuavaTest();
        List<String> test = new ArrayList<String>();
        test.add("1");
        test.add("2");
        test.add("3");
        try {
            gt.getElement(test, 5);

            // throws implementation specific exception
        } catch (Exception e) {
            System.out.println("hmmm.." + Throwables.getStackTraceAsString(e));
            throw new ApiException("reason", Throwables.getStackTraceAsString(e));
        }

        String newString = CharMatcher.ascii().retainFrom("aßΣΩbcä");
        System.out.println("output: " + newString);
    }

    public String getElement(List<String> list, int index) {
        Preconditions.checkElementIndex(index, list.size(), "The given index is not valid");
        return list.get(index);
    }
}