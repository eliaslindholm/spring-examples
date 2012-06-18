package elilin.spring.restapp;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;

/**
 * Custom ObjectMapper that supports the domain specific types (MessageId and Signature).
 * 
 * @author Elias Lindholm
 *
 */
public class CustomObjectMapper extends ObjectMapper {
	
	public CustomObjectMapper() {
		SimpleModule module = new SimpleModule("custom-object-mapper", new Version(1, 0, 0, ""));
		module.addSerializer(MessageId.class, new JsonSerializer<MessageId>() {
			@Override
			public void serialize(MessageId value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString(value.toString());
			}
			
		});
		module.addDeserializer(MessageId.class, new JsonDeserializer<MessageId>(){
			@Override
			public MessageId deserialize(JsonParser jp,
					DeserializationContext ctxt) throws IOException,
					JsonProcessingException {
				return new MessageId(Long.valueOf(jp.getText()));
			}
			
		});
		module.addSerializer(Signature.class, new JsonSerializer<Signature>() {
			@Override
			public void serialize(Signature value, JsonGenerator jgen,
					SerializerProvider provider) throws IOException,
					JsonProcessingException {
				jgen.writeString(value.toString());
			}
			
		});
		module.addDeserializer(Signature.class, new JsonDeserializer<Signature>(){
			@Override
			public Signature deserialize(JsonParser jp,
					DeserializationContext ctxt) throws IOException,
					JsonProcessingException {
				return new Signature(jp.getText());
			}
			
		});
		registerModule(module);
	}

}
