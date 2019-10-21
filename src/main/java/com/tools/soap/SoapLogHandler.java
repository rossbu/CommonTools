package com.tools.soap;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author tbu
 */
public class SoapLogHandler implements SOAPHandler<SOAPMessageContext> {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SoapLogHandler.class);

    public String getInboundResponseXml() {
        return inboundResponseXml;
    }

    public void setInboundResponseXml(String inboundResponseXml) {
        this.inboundResponseXml = inboundResponseXml;
    }

    String inboundResponseXml = "";

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty) {
            Map<String, List<String>> requestHeaders = (Map<String, List<String>>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
            printHttpHeader(requestHeaders, "outbound");
        } else {
            Map<String, List<String>> responseHeaders = (Map<String, List<String>>) messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
            printHttpHeader(responseHeaders, "inbound");
        }

        SOAPMessage soapMessage = messageContext.getMessage();


        if (outboundProperty) {
            if (logger.isDebugEnabled()) {
                logSOAPMessage(soapMessage, "Outbound Message");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logSOAPMessage(soapMessage, "Inbound  Message");
            }
        }
        return true;
    }

    private void printHttpHeader(Map<String, List<String>> headers, String boundValue) {
        if (headers != null) {
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String name = entry.getKey();
                List<String> values = entry.getValue();
                System.out.println(boundValue + " --  name is : " + name + " and values is : " + values);
            }
        } else {
            System.err.println("headers is null");

        }
    }

    private void logSOAPMessage(final SOAPMessage soapMessage, final String direction) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            debugSoapMessage(soapMessage);
            soapMessage.writeTo(bout);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msg = null;
        try {
            msg = bout.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        logger.debug(":: " + direction + " :: " + msg);
        System.out.println(":: " + direction + " :: " + msg + "\n");
        inboundResponseXml = msg;
    }

    private void debugSoapMessage(SOAPMessage message) throws SOAPException {
        SOAPEnvelope soapEnv = message.getSOAPPart().getEnvelope();
        SOAPBody soapBody = message.getSOAPBody();
        SOAPHeader soapHeader = message.getSOAPHeader();
        SOAPPart soapPart = message.getSOAPPart();

        // soapBody prints
        Iterator sentIt = soapBody.getChildElements(); //create an iterator on elements
        int counter=0;
        while(sentIt.hasNext())
        {
            SOAPBodyElement sentSBE = (SOAPBodyElement)sentIt.next();
            Iterator sentIt2 = sentSBE.getChildElements();
            SOAPElement sentSE = (SOAPElement)sentIt2.next();
            String sentID = sentSE.getNodeName();  //result is HotelListResponse
            counter++;
            System.out.println("sentID:"+sentID);
        }
        System.out.println("counter"+counter); //result is 1

        String textContent = soapBody.getTextContent();
        System.out.println("soapBody getTextContent : "+textContent); //result is 1

    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {
    }


}