package com.github.mbmll.authorization.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.IOException;
import java.util.Map;

public class AuthorizationGrantTypeDeserializer extends StdDeserializer<AuthorizationGrantType> {

	public final ObjectMapper objectMapper = new ObjectMapper();

	public AuthorizationGrantTypeDeserializer() {
		super(AuthorizationGrantType.class);
	}

	@Override
	public AuthorizationGrantType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		Map<String, String> map = objectMapper.readValue(p, new TypeReference<Map<String, String>>() {
		});
		return new AuthorizationGrantType(map.values().stream().findFirst().orElse(null));
	}

}
