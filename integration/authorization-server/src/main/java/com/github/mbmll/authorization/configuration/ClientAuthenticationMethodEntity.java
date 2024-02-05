package com.github.mbmll.authorization.configuration;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientAuthenticationMethodEntity implements Serializable {
    private String value;
}
