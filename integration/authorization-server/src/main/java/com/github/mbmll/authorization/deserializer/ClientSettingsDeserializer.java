package com.github.mbmll.authorization.deserializer;

/*-
 * #%L
 * spring-security-oauth2-authorization-server-redis
 * %%
 * Copyright (C) 2022 - 2023 徐晓伟工作室
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.io.IOException;
import java.util.Map;

import static org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Client.TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM;

/**
 * {@link ClientSettings} 反序列化
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
public class ClientSettingsDeserializer extends StdDeserializer<ClientSettings> {

	public final ObjectMapper objectMapper = new ObjectMapper();

	public ClientSettingsDeserializer() {
		super(ClientSettings.class);
	}

	@Override
	public ClientSettings deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		Map<String, Object> settings = objectMapper.readValue(p.getCodec().readTree(p).get("settings").toString(),
				new TypeReference<Map<String, Object>>() {
				});

		tokenEndpointAuthenticationSigningAlgorithm(settings);

		ClientSettings.Builder builder = ClientSettings.withSettings(settings);
		return builder.build();
	}

	private void tokenEndpointAuthenticationSigningAlgorithm(Map<String, Object> settings) {
		Object tokenEndpointAuthenticationSigningAlgorithmObj = settings
			.get(TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM);
		if (tokenEndpointAuthenticationSigningAlgorithmObj instanceof String) {
			String tokenEndpointAuthenticationSigningAlgorithmStr = (String) tokenEndpointAuthenticationSigningAlgorithmObj;
			MacAlgorithm macAlgorithm = MacAlgorithm.from(tokenEndpointAuthenticationSigningAlgorithmStr);
			SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm
				.from(tokenEndpointAuthenticationSigningAlgorithmStr);
			if (macAlgorithm != null) {
				settings.put(TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM, macAlgorithm);
			}
			else if (signatureAlgorithm != null) {
				settings.put(TOKEN_ENDPOINT_AUTHENTICATION_SIGNING_ALGORITHM, signatureAlgorithm);
			}
		}
	}

}
