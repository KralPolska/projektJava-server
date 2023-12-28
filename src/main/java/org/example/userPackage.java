package org.example;

import java.io.Serializable;

public class userPackage implements Serializable {
    private final String name;
    private final String password;
    private final String confirmPassword;
    private final String email;
    private final String confirmEmail;

    userPackage(String name, String password, String confirmPassword, String email, String confirmEmail)
    {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.confirmEmail = confirmEmail;
    }

    public String getName()
    {
        return this.name;
    }
    public String getPassword()
    {
        return this.password;
    }
    public String getEmail()
    {
        return this.email;
    }

    public boolean checkConfirmPassword()
    {
        return this.password.equals(this.confirmPassword);
    }

    public boolean checkConfirmEmail()
    {
        return this.email.equals(this.confirmEmail);
    }
}
