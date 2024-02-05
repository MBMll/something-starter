package com.github.mbmll.authorization.configuration;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TokenSettingsEntity implements Serializable {
    private Map<String, Object> settings;
}
