package com.github.mbmll.authorization.serializer;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorizationGrantTypeEntity implements Serializable {
    private String value;
}
