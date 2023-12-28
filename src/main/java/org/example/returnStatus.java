package org.example;
import java.io.*;
public class returnStatus implements Serializable {
    private final boolean status;
    private final String error;

    public returnStatus(boolean status, String error)
    {
        this.status = status;
        this.error = error;
    }

    public boolean getStatus()
    {
        return this.status;
    }

    public String getError()
    {
        return this.error;
    }
}
