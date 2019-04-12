package com.glqdlt.utill.simpleReader;

/**
 * @author Jhun
 * 2019-03-29
 */
public abstract class AuthenticationStoreAdapter {

    public final AuthenticationStore authenticationStore() {
        return authenticationStoreHolder();
    }

    public abstract AuthenticationStore authenticationStoreHolder();

}
