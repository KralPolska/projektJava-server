package org.example;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class characterPolicy {
    private static String string;

    public characterPolicy(String string)
    {
        characterPolicy.string = string;
    }

    public static int password_Validation()
    {
        if(string.length()>=8)
        {
            if(string.length()>=25)
            {
                return -2;
            }
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(string);
            Matcher hasDigit = digit.matcher(string);
            Matcher hasSpecial = special.matcher(string);


            if(hasLetter.find() && hasDigit.find() && hasSpecial.find())
                return 1;
            else return 0;
        }
        else return -1;
    }
}
