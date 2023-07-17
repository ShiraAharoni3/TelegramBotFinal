package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Joke
{
    private String setup ;
    private String delivery ;

    public String getSetup()
    {
        return setup;
    }

    public String getDelivery()
    {
        return delivery;
    }
}
