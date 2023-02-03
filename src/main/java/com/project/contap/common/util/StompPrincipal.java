package com.project.contap.common.util;

import java.security.Principal;

public class StompPrincipal implements Principal {

    public StompPrincipal(String name) { this.name = name; }
    private String name;

    @Override
    public String getName() { return name;}
}
