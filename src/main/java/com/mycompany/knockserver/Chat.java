package com.mycompany.knockserver;

/**
 * Created by okhoruzhenko on 3/15/17.
 */
public interface Chat {
    void post(Chattable member, String message);
    void broadCastMessage();
}
