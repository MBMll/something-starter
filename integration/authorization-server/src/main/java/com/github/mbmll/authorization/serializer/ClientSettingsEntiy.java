package com.github.mbmll.authorization.serializer;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ClientSettingsEntiy implements Serializable {
    private Map<String, Object> settings;
}
