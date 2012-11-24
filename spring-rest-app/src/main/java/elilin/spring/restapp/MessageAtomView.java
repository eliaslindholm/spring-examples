package elilin.spring.restapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;

public class MessageAtomView extends AbstractAtomFeedView {
	
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed,
			HttpServletRequest request) {
        feed.setId("urn:uuid:" + UUID.randomUUID().toString());
        feed.setTitle("All messages");
        @SuppressWarnings("unchecked")
        List<Message> messages = (List<Message>)model.get("content");
        for (Message message : messages) {
            Date date = message.getCreated();
            if (feed.getUpdated() == null || date.compareTo(feed.getUpdated()) > 0) {
                feed.setUpdated(date);
            }
        }
    }
 
    @Override
    protected List<Entry> buildFeedEntries(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
 
        @SuppressWarnings("unchecked")
        List<Message> messages = (List<Message>)model.get("content");
        List<Entry> entries = new ArrayList<Entry>(messages.size());
 
        for (Message message : messages) {
            Entry entry = new Entry();
            String date = String.format("%1$tY-%1$tm-%1$td", message.getCreated());
            entry.setId(String.format("tag:elilin.se,%s:%s", date, message.getId().toString()));
            entry.setTitle(String.format("On %s, %s wrote", date, message.getSignature()));
            entry.setPublished(message.getCreated());
 
            Content content = new Content();
            content.setValue(new CustomObjectMapper().writeValueAsString(message));
            content.setType("application/json");
            entry.setContents(Arrays.asList(content));
 
            entries.add(entry);
        }
 
        return entries;

	}

}
