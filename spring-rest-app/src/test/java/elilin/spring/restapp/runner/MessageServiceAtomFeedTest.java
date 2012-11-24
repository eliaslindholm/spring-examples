package elilin.spring.restapp.runner;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class MessageServiceAtomFeedTest {

	private static WebAppRunner runner;

	@BeforeClass
	public static void startWebapp() throws Exception {
		runner = new WebAppRunner();
		runner.start();
	}
	
	@AfterClass
	public static void stopWebapp() throws Exception {
		runner.stop();
	}
	
	@Test
	public void feedPublicationDateIsSameAsCreationTimeForLastMessage() throws Exception {

		RestTemplate restTemplate = new RestTemplate();
		URI msgUri = restTemplate.postForLocation(runner.getAbsoluteUrl("/messages"), new MessageDto("a message", "foppa"));
		
		MessageDto foppasMessage = restTemplate.getForObject(msgUri, MessageDto.class);
		
        URL feedUrl = new URL(runner.getAbsoluteUrl("/messages/notifications"));
        
        

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));
        assertEquals(1, feed.getEntries().size());
        
        SyndEntry entry = (SyndEntry) feed.getEntries().get(0);
        SyndContent content = (SyndContent) entry.getContents().get(0);
        assertEquals("application/json", content.getType());
        
        MessageDto latestMessage = new ObjectMapper().readValue(content.getValue(), MessageDto.class);
        assertEquals(latestMessage.getMessage(), foppasMessage.getMessage());
        assertEquals(latestMessage.getId(), foppasMessage.getId());
        assertEquals(latestMessage.getCreated(), foppasMessage.getCreated());
        assertEquals(latestMessage.getSignature(), foppasMessage.getSignature());
        
        assertEquals(foppasMessage.getCreated().getTime() / 1000, entry.getPublishedDate().getTime() / 1000); // Atom date-time representation does not include milliseconds
	}
	
}
