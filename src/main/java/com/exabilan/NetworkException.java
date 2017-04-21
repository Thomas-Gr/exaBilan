package com.exabilan;

public class NetworkException extends RuntimeException {

    public NetworkException(Exception e) {
        super("Impossible d'accéder au serveur. Veuillez vérifier que vous êtes bien connecté à Internet.", e);
    }

}
