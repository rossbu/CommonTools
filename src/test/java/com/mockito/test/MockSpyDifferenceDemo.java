package com.mockito.test;


import java.util.concurrent.TimeUnit;

import com.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/*
    A Mock if not stubbed will return a null value, whereas a Spy if not stubbed will call the implemented method inside of the concrete class.
    mock is used if we want to mock all the methods of a class.
    spy is used if we want to mock some methods and for remaining methods actual call has to be made.

 */
public class MockSpyDifferenceDemo {

    @Mock
    EmailService emailServiceMock;
    @Spy
    EmailService emailServiceSpy;

    private final String testEmail = "randomuser@domain.com";
    private final String success = "SUCCESS";

    @Test
    @Timeout(value = 60, unit = TimeUnit.SECONDS)
    @Description("When mock is called, we can return any response we like")
    public void simpleTest1() {
        when(emailServiceMock.sendMail(testEmail)).thenReturn(success);
        assertEquals(success, emailServiceMock.sendMail(testEmail));
    }

    @Test
    @Description("When mock is called but not stubbed, we receive a null value")
    public void simpleTest2() {
        assertNull(emailServiceMock.sendMail(testEmail));
    }

    @Test
    @Description("When a spy is called but not stubbed, the concrete impl is called")
    public void simpleTest3() {
        assertTrue(emailServiceSpy.sendMail(testEmail).contains(testEmail));
    }

    @Test
    @Description("When a spy is called and stubbed, stubbed value is returned")
    public void simpleTest4() {
        when(emailServiceSpy.sendMail(testEmail)).thenReturn(success);
        assertEquals(success, emailServiceSpy.sendMail(testEmail));
    }
}