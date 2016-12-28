package com.example.dreader.devilreader.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;


public class StoryAuthenticator extends AbstractAccountAuthenticator {


    public StoryAuthenticator(Context context) {

        super(context);
    }


    @Override
    public Bundle editProperties(AccountAuthenticatorResponse r, String s) {

        throw new UnsupportedOperationException();
    }


    @Override
    public Bundle addAccount(AccountAuthenticatorResponse r, String s, String s2, String[] strings, Bundle b)
            throws NetworkErrorException {

        return null;
    }


    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse r, Account a, Bundle b) {

        return null;
    }


    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse r, Account a, String s, Bundle b)
            throws NetworkErrorException {

        throw new UnsupportedOperationException();
    }


    @Override
    public String getAuthTokenLabel(String s) {

        throw new UnsupportedOperationException();
    }


    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse r,
                                    Account a, String s, Bundle b) throws NetworkErrorException {

        throw new UnsupportedOperationException();
    }


    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse r, Account a, String[] strings)
            throws NetworkErrorException {

        throw new UnsupportedOperationException();
    }
}
