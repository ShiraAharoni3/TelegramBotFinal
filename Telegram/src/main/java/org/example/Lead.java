package org.example;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Lead
{
    private long chatId;
    private int apiRequest ;
    private int firstChoice ;
    private int countryChoice ;
    private int JokeChoice ;
    private int numberChoice ;

    private boolean waitingForYearInput;
    public  static final int KNOWN_CHOICE = 0 ;

    public  static final int INFORMATION_ABOUT_NUMBERS = 1 ;
    public  static final int INFORMATION_ABOUT_COUNTRIES = 2 ;
    public  static final int JOKE = 3 ;
    public  static final int YEAR = 4 ;
    public  static final int DATA = 5 ;
    public  static final int MATH = 6 ;
    public  static final int TRIVIA = 7 ;
    public  static final int CAPITAL = 8 ;
    public  static final int SUBREGION = 9 ;
    public  static final int POPULATION = 10 ;
    public  static final int REGION = 11 ;
    public  static final int MISC = 12;
    public  static final int PUN = 13 ;
    public  static final int SPOOKY = 14 ;
    public  static final int ANY = 15 ;



    public Lead(long chatId)
    {
        this.chatId = chatId;
        this.firstChoice = KNOWN_CHOICE ;
        this.numberChoice = KNOWN_CHOICE ;
        this.waitingForYearInput = false;
        this.apiRequest = 0 ;
    }

    public void setFirstChoice() {
        this.firstChoice = KNOWN_CHOICE;
    }

    public void setCountryChoice() {
        this.countryChoice = KNOWN_CHOICE;
    }

    public void setJokeChoice() {
        JokeChoice = KNOWN_CHOICE;
    }

    public void setNumberChoice() {
        this.numberChoice = KNOWN_CHOICE;
    }

    public long getChatId() {
        return chatId;
    }

    public int getApiRequest() {
        return apiRequest;
    }
    public void setApiRequest( long chatId )
    {
        this.apiRequest++ ;
    }

    public boolean isChosenMisc ()
    {
        return this.JokeChoice == MISC ;
    }
    public boolean isChosenPun ()
    {
        return this.JokeChoice == PUN ;
    }
    public boolean isChosenSpooky ()
    {
        return this.JokeChoice == SPOOKY ;
    }
    public boolean isChosenAny ()
    {
        return this.JokeChoice == ANY ;
    }


    public boolean isChoiceKnown ()
    {
        return this.firstChoice != KNOWN_CHOICE ;
    }


    public boolean isNumbersInformation ()
    {
        return this.firstChoice == INFORMATION_ABOUT_NUMBERS ;
    }
    public boolean isChosenYear ()
    {
        return this.numberChoice == YEAR ;
    }
    public boolean isDateSelected ()
    {
        return this.numberChoice == DATA ;
    }
    public boolean isChosenTrivia ()
    {
        return this.numberChoice == TRIVIA ;
    }
    public boolean isChosenMath ()
    {
        return this.numberChoice == MATH ;

    }
    public boolean isChosenCapital ()
    {
        return this.countryChoice == CAPITAL;
    }
    public boolean isChosenSubregion ()
    {
        return this.countryChoice == SUBREGION ;
    }
    public boolean isChosenPopulation ()
    {
        return this.countryChoice == POPULATION ;
    }
    public boolean isChosenRegion ()
    {
        return this.countryChoice == REGION ;
    }

    public boolean isCountriesInformation ()
    {
        return this.firstChoice == INFORMATION_ABOUT_COUNTRIES ;
    }

    public boolean isJoke ()
    {
        return this.firstChoice == JOKE ;
    }
    public void reset() // this method rest the
    {
        this.firstChoice = KNOWN_CHOICE ;
    }


    public void Interest(String status)
    {
        switch (status) {
            case "N":
                this.firstChoice = INFORMATION_ABOUT_NUMBERS;
                break;
            case "C":
                this.firstChoice = INFORMATION_ABOUT_COUNTRIES;
                break;
            case "J":
                this.firstChoice = JOKE;
                break;
        }

    }
    public void numbersChoose (String status)
    {
        switch (status) {
            case "Trivia":
                this.numberChoice = TRIVIA;
                break;
            case "Math":
                this.numberChoice = MATH;
                break;
            case "Date":
                this.numberChoice = DATA;
                break;
            case "Year":
                this.numberChoice = YEAR;
                break;

        }
    }

    public void CountryChoice (String status)
    {
        switch (status) {
            case "Capital":
                this.countryChoice = CAPITAL;
                break;
            case "Subregion":
                this.countryChoice = SUBREGION ;
                break;
            case "Population":
                this.countryChoice = POPULATION ;
                break;
            case "Region":
                this.countryChoice = REGION ;
                break;

        }
    }
    public void jokeChoice (String status)
    {
        switch (status) {
            case "Misc":
                this.JokeChoice = MISC;
                break;
            case "Pun":
                this.JokeChoice = PUN ;
                break;
            case "Spooky":
                this.JokeChoice = SPOOKY ;
                break;
            case "Any":
                this.JokeChoice = ANY ;
                break;
        }
    }
}