package se.ivankrizsan.java.messagepayloadswiththymeleaf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Tests obtaining different types of message payloads with parameters inserted
 * using Thymeleaf.
 *
 * @author Ivan Krizsan
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessagePayloadsWithThymeleafApplicationTests {
    /* Constant(s): */
    public static final String CURRENCY_SEK = "SEK";
    public static final String CURRENCY_NTD = "NTD";

    protected static final Logger LOGGER = LoggerFactory.getLogger(
        MessagePayloadsWithThymeleafApplicationTests.class);

    /* Instance variable(s): */
    @Autowired
    @Qualifier("messageTemplateEngine")
    protected SpringTemplateEngine mMessageTemplateEngine;

    /**
     * Tests retrieval of an XML message in which two parameter values are inserted.
     * Expected result: The message should be retrieved with the parameter values
     * inserted into it.
     */
    @Test
    public void xmlMessageTest() {
        /* Retrieve XML message with inserted parameter values. */
        final Context theContext = new Context();
        theContext.setVariable(ThymeleafConfiguration.TEMPLATE_FROMCURRENCY_PARAM, CURRENCY_SEK);
        theContext.setVariable(ThymeleafConfiguration.TEMPLATE_TOCURRENCY_PARAM, CURRENCY_NTD);
        final String theXmlMessage =
            mMessageTemplateEngine.process("xml/currency_conversion_request", theContext);

        /* Verify the XML message. */
        final XmlPath theXmlMessageXmlPath = XmlPath.with(theXmlMessage);
        assertThat(theXmlMessageXmlPath.getString("Envelope.Body.ConversionRate.FromCurrency"),
            is(CURRENCY_SEK));
        assertThat(theXmlMessageXmlPath.getString("Envelope.Body.ConversionRate.ToCurrency"),
            is(CURRENCY_NTD)
        );

        /* Log the message to the console for visual feedback. */
        LOGGER.info(theXmlMessage);
    }

    /**
     * Tests retrieval of an JSON message in which two parameter values are inserted.
     * Expected result: The message should be retrieved with the parameter values
     * inserted into it.
     */
    @Test
    public void jsonMessageTest() {
        /* Retrieve JSON message with inserted parameter values. */
        final Context theContext = new Context();
        theContext.setVariable(ThymeleafConfiguration.TEMPLATE_FROMCURRENCY_PARAM, CURRENCY_SEK);
        theContext.setVariable(ThymeleafConfiguration.TEMPLATE_TOCURRENCY_PARAM, CURRENCY_NTD);
        final String theJsonMessage =
            mMessageTemplateEngine.process("json/currency_conversion_request", theContext);

        /* Verify the JSON message. */
        final JsonPath theJsonMessageJsonPath = JsonPath.with(theJsonMessage);
        assertThat(theJsonMessageJsonPath.getString("conversion_rate.from_currency"),
            is(CURRENCY_SEK));
        assertThat(theJsonMessageJsonPath.getString("conversion_rate.to_currency"),
            is(CURRENCY_NTD)
        );

        /* Log the message to the console for visual feedback. */
        LOGGER.info(theJsonMessage);
    }

    /**
     * Tests retrieval of a text message in which two parameter values are inserted.
     * Expected result: The message should be retrieved with the parameter values
     * inserted into it.
     */
    @Test
    public void textMessageTest() {
	    /* Retrieve text message with inserted parameter values. */
        final Context theContext = new Context();
        theContext.setVariable(ThymeleafConfiguration.TEMPLATE_FROMCURRENCY_PARAM, CURRENCY_SEK);
        theContext.setVariable(ThymeleafConfiguration.TEMPLATE_TOCURRENCY_PARAM, CURRENCY_NTD);
        final String theTextMessage =
            mMessageTemplateEngine.process("text/currency_conversion_request", theContext);

        /* Verify the text message. */
        assertThat(theTextMessage, containsString(CURRENCY_SEK));
        assertThat(theTextMessage, containsString(CURRENCY_NTD));

        /* Log the message to the console for visual feedback. */
        LOGGER.info(theTextMessage);
    }
}
