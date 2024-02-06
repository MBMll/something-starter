package com.github.mbmll.authorization.serializer;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientAuthenticationMethodEntity implements Serializable {
    private String value;
}
