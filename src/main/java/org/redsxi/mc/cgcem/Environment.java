package org.redsxi.mc.cgcem;

public enum Environment {
    CLIENT(true),
    SERVER(false);

    private final boolean isClient;

    Environment(boolean isCli) {
        isClient = isCli;
    }

    public boolean isClient() {
        return isClient;
    }

    public boolean isServer() {
        return !isClient;
    }

    private static boolean environmentIsFreezed;
    private static Environment environment = null;

    public static void setEnvironment(Environment env) {
        if(environmentIsFreezed) throw new IllegalStateException("Change the environment when environment is frozen");
        environment = env;
    }

    public static Environment getEnvironment() {
        if(environment == null) throw new IllegalStateException("Access the environment when the environment not set");
        return environment;
    }
}
