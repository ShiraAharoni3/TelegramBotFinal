package org.example;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import java.util.*;

public class MyBot extends TelegramLongPollingBot {
    private final String BOT_TOKEN = "6171486691:AAFBLTw-Wu63jjdmzjGsuj00ltqtAlLAaVo";
    private final String BOT_USERNAME = "shiraAharoniBot";
    private static Map<Long, Lead> LeadMap;
    private List<Long> chatId;
    private boolean isMenuSent ;

    public static Map<Long, Lead> getLeadMap()
    {
        return LeadMap;
    }

    public MyBot() {
        this.chatId = new ArrayList<>();
        this.LeadMap = new HashMap<>();
        this.isMenuSent = false ;
    }

    public boolean isMenuSent() {
        return isMenuSent;
    }

    public void setMenuSent(boolean menuSent) {
        isMenuSent = menuSent;
    }

    private void sendMenuMessage(long chatId , String value1 , String value2 , String value3 , String value4)
    {
        Lead lead = LeadMap.get(chatId);
        if (lead != null && !isMenuSent())
        {
            sendOptionToUser( chatId , value1 , value2 , value3 , value4) ;
            setMenuSent(true);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }



    @Override
    public void onUpdateReceived(Update update)
    {
        long chatId = this.getChatId(update);
        Lead lead = this.LeadMap.get(chatId);

        if (lead == null) {
            sendInitialMessage(chatId);
            lead = new Lead(chatId);
            this.LeadMap.put(chatId, lead);
            return;
        }
        if (update.hasCallbackQuery())
        {
            // Processing callback queries
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            if (!lead.isChoiceKnown())
            {
                lead.Interest(callbackData);
            }
            else if (lead.isNumbersInformation() )
            {
                sendMenuMessage(chatId, Utils.TRIVIA, Utils.MATH, Utils.DATE, Utils.YEAR) ;
                lead.numbersChoose(callbackData);
                if (lead.isChosenYear()) {
                    sendMessageToUser(chatId, "Please enter a year about which you would like to receive information:");
                } else if (lead.isDateSelected()) {
                    sendMessageToUser(chatId, "Choose a date. Note that the date will appear in the following format : \"month/day\"");
                } else if (lead.isChosenTrivia()) {
                    sendMessageToUser(chatId, "Select a number for which you would like to receive information");
                } else if (lead.isChosenMath()) {
                    sendMessageToUser(chatId, "Select a number for which you would like to receive information");
                }

            } else if (lead.isCountriesInformation() )
            {
                sendMenuMessage(chatId, Utils.SUBREGION, Utils.POPULATION, Utils.CAPITAL, Utils.REGION) ;
                //sendOptionToUser(chatId, Utils.SUBREGION, Utils.POPULATION, Utils.CAPITAL, Utils.REGION);
                lead.CountryChoice(callbackData);
                if (lead.isChosenCapital()) {
                    sendMessageToUser(chatId, Utils.COUNTRY_MESSAGE);
                } else if (lead.isChosenRegion()) {
                    sendMessageToUser(chatId, Utils.COUNTRY_MESSAGE);
                } else if (lead.isChosenSubregion()) {
                    sendMessageToUser(chatId, Utils.COUNTRY_MESSAGE);
                } else if (lead.isChosenPopulation()) {
                    sendMessageToUser(chatId, Utils.COUNTRY_MESSAGE);
                }
            }
            else if (lead.isJoke() )
            {
                sendMenuMessage(chatId, Utils.PUN, Utils.ANY, Utils.SPOOKY, Utils.MISC) ;
                // sendOptionToUser(chatId, Utils.PUN, Utils.ANY, Utils.SPOOKY, Utils.MISC);
                lead.jokeChoice(callbackData);
                if (lead.isChosenMisc()) {
                    try {
                        String joke = joke("misc");
                        sendMessageToUser(chatId, joke);
                        lead.setApiRequest(chatId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Sleep();
                    restartBot(chatId);
                    setMenuSent(false);
                    lead.setJokeChoice();

                } else if (lead.isChosenPun()) {
                    System.out.println("pun");
                    try {
                        //String Joke = joke(Utils.PUN) ;
                        sendMessageToUser(chatId, joke("Pun"));
                        lead.setApiRequest(chatId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Sleep();
                    restartBot(chatId);
                    setMenuSent(false);
                    lead.setJokeChoice();

                } else if (lead.isChosenSpooky()) {
                    System.out.println("spooky");
                    try {
                        String Joke = joke("Spooky");
                        sendMessageToUser(chatId, Joke);
                        lead.setApiRequest(chatId);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Sleep();
                    restartBot(chatId);
                    setMenuSent(false);
                    lead.setJokeChoice();

                } else if (lead.isChosenAny()) {
                    System.out.println("any");
                    try {
                        String Joke = joke("any");
                        sendMessageToUser(chatId, Joke);
                        lead.setApiRequest(chatId);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Sleep();
                    restartBot(chatId);
                    setMenuSent(false);
                    lead.setJokeChoice();
                }

            }
        }

        if (update.hasMessage() && update.getMessage().hasText())
        {
            Message message = update.getMessage();
            String response = message.getText();

            if (lead.isChosenYear())
            {
                System.out.println(response);
                boolean result = validYear(response);
                System.out.println(result);
                boolean userResponse = true;
                try {
                    String year = numbersInformation(response,"year");
                    sendMessageToUser(chatId, year);
                    lead.setApiRequest(chatId);
                    response = null ;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setNumberChoice();
                }
            else if (lead.isDateSelected())
            {
               if (isValidDate(response) == true)
               {
                  try
                  {
                    String date = numbersInformation(numberLessThan10(response) , "date");
                    sendMessageToUser(chatId, date);
                      lead.setApiRequest(chatId);
                      response = "" ;
                  }
                  catch (Exception e)
                  {
                    throw new RuntimeException(e);
                  }
                   Sleep();
                   restartBot(chatId) ;
                   response = "" ;
                   setMenuSent(false);
                   lead.setNumberChoice();
               }
            }
            else if(lead.isChosenTrivia())
            {
                try
                {
                    String trivia = numbersInformation(response , "trivia");
                    sendMessageToUser(chatId, trivia) ;
                    lead.setApiRequest(chatId);
                    //response = "" ;
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setNumberChoice();

            }
            else if (lead.isChosenMath())
            {

                try
                {
                    String Math = numbersInformation(response , "math");
                    sendMessageToUser(chatId, Math) ;
                    lead.setApiRequest(chatId);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setNumberChoice();

            } else if (lead.isChosenCapital())
            {
                try
                {
                    String city = Capital(countryInformation(response)) ;
                    sendMessageToUser(chatId, "The capital city of - " + response + " is -" + city ) ;
                    lead.setApiRequest(chatId);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setCountryChoice();
            }
            else if (lead.isChosenRegion())
            {
                try
                {
                    String region = Region(countryInformation(response)) ;
                    sendMessageToUser(chatId, "The region of - " + response + " is -" + region ) ;
                    lead.setApiRequest(chatId);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setCountryChoice();
            }
            else if (lead.isChosenSubregion())
            {
                try
                {
                    String subregion = Subregion(countryInformation(response)) ;
                    sendMessageToUser(chatId, "The subregion of: " + response + " is -" + subregion ) ;
                    lead.setApiRequest(chatId);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setCountryChoice();
            }
            else if (lead.isChosenPopulation())
            {
                try
                {
                  String Population = String.valueOf(Population(countryInformation(response)));
                  sendMessageToUser(chatId, "The amount of population found in " + response + " is - " + Population);
                  lead.setApiRequest(chatId);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                Sleep();
                restartBot(chatId) ;
                setMenuSent(false);
                lead.setCountryChoice();
            }


        }


    }
    private void sendOptionToUser (long chatId , String value1  , String value2 , String value3 , String value4 ) //This function is responsible for creating the button menu
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        InlineKeyboardButton value1Button = new InlineKeyboardButton(value1);
        value1Button.setCallbackData(value1);
        InlineKeyboardButton value2Button = new InlineKeyboardButton(value2);
        value2Button.setCallbackData(value2);
        InlineKeyboardButton value3Button = new InlineKeyboardButton(value3);
        value3Button.setCallbackData(value3);
        InlineKeyboardButton value4Button = new InlineKeyboardButton(value4);
        value4Button.setCallbackData(value4);
        List<InlineKeyboardButton> firstRow = Arrays.asList(value2Button, value3Button);
        List<InlineKeyboardButton> lastRow = Arrays.asList(value1Button , value4Button);
        List<List<InlineKeyboardButton>> keyboard = Arrays.asList(firstRow, lastRow);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("Choose a topic, about which you would like to receive information: ");
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendInitialMessage(long chatId)  //This function is responsible for creating the button main menu
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        InlineKeyboardButton NumbersButton = new InlineKeyboardButton("Number's Information");
        NumbersButton.setCallbackData("N");
        InlineKeyboardButton CountryButton = new InlineKeyboardButton("Country's Information");
        CountryButton.setCallbackData("C");
        InlineKeyboardButton WeatherButton = new InlineKeyboardButton("Joke");
        WeatherButton.setCallbackData("J");
        List<InlineKeyboardButton> topRow = Arrays.asList(CountryButton, WeatherButton);
        List<InlineKeyboardButton> bottomRow = Collections.singletonList(NumbersButton);
        List<List<InlineKeyboardButton>> keyboard = Arrays.asList(topRow, bottomRow);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("What are you interested in?");
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Sleep ()
    {
        try {
            // Sleep for 3 seconds
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // Handle the exception if sleep is interrupted
            e.printStackTrace();
        }
    }
    private void restartBot(long chatId)
    {
        Lead lead = LeadMap.get(chatId);

        if (lead != null)
        {
            lead.reset();
        }
        sendInitialMessage(chatId);

    }
    public static long Population (List<CountryModel> countryModels) // Handles a population api request
    {
        long population = 0 ;
        for (CountryModel country : countryModels) {
            population = country.getPopulation();
        }
        return population ;
    }
    public static String Subregion (List<CountryModel> countryModels) //Handles a subregion api request
    {
        String subregion = null ;
        for (CountryModel country : countryModels) {
            subregion = country.getSubregion();
        }
        return subregion ;
    }
    public static String Region (List<CountryModel> countryModels) //Handles a region api request
    {
        String region = null ;
        for (CountryModel country : countryModels) {
            region = country.getRegion();
        }
        return region ;
    }
    public static String Capital (List<CountryModel> countryModels) // Handles a region api request
    {
        String capital = null ;
        for (CountryModel country : countryModels) {
            capital = country.getCapital();
        }
        return capital ;
    }


    public static List<CountryModel> countryInformation(String countryName) throws Exception //A function creates a countryModel object by making an Api request
    {
        List<CountryModel> countryModel = new ArrayList<>() ;
        try {
            String url = Utils.COUNTRY_URL_API + countryName;
            GetRequest getRequest1 = Unirest.get(url);
            HttpResponse<String> response = getRequest1.asString();
            String json = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<CountryModel> NewCountryModel = objectMapper.readValue(json, new TypeReference<>() {
            });
            countryModel = NewCountryModel ;
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        return countryModel;
    }

    public static String joke(String category) throws Exception // Handles a joke api request
    {
        String joke = null;
        try {
            String url = Utils.JOKE_URL_API + category;
            GetRequest getRequest = Unirest.get(url);
            HttpResponse<String> response = getRequest.asString();
            String json = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            Joke jokeObject = objectMapper.readValue(json, Joke.class);
            joke = jokeObject.getSetup() + "\n" + jokeObject.getDelivery();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        return joke;
    }

    public String numbersInformation(String number, String type) throws Exception // Handles a numbers api request
    {
       String numberValue = null;
       try {
           // Replace with the desired type (e.g., "trivia", "math", "year", etc.)

           URL url = new URL("http://numbersapi.com/" + number + "/" + type);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");

           BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
           String line;
           StringBuilder response = new StringBuilder();
           while ((line = reader.readLine()) != null) {
               response.append(line);
           }
           reader.close();
           numberValue = response.toString();

           System.out.println(response.toString());
       } catch (Exception e) {
           e.printStackTrace();
       }
       return numberValue ;
   }
   public String numberLessThan10 (String value)
   {
       String date = value ;
       String[] Date = splitDate(date) ;
       int day = Integer.parseInt(Date[1]) ;
       int month = Integer.parseInt(Date[0]) ;
       if (day < Utils.TEN )
       {
           if (month < Utils.TEN)
           {
               date = "0"+ Date[0] + "/" + "0" + Date[1] ;
           }
           else
           {
               date = Date[0] + "/" + "0" + Date[1];
           }
       }
       return value ;
   }
   public boolean isValidDate (String date)
   {
       String[] Date = splitDate(date) ;
       boolean isValidDate = false ;
       if (validDay(Date[1]) == true)
       {
           if (validMonth(Date[0]) == true)
           {
               isValidDate = true ;
           }
       }
       return isValidDate ;
   }
   public String[] splitDate (String date)
   {
       String[] Date = date.split("/");
       return Date ;
   }
    public boolean validDay (String day )
    {
        boolean isValid = false ;
        int Day = Integer.parseInt(day);
        if (Day >= Utils.FIRST_DAY )
        {
            if (Day <= Utils.LAST_DAY)
            {
                isValid = true ;
            }
        }
        return isValid ;
    }
   public boolean validMonth (String month )
   {
       boolean isValid = false ;
       int Month = Integer.parseInt(month);
       if (Month >= Utils.FIRST_MONTH )
       {
           if( Month <= Utils.LAST_MONTH )
           {
               isValid = true ;
           }
       }
       return isValid ;
   }

    public void sendMessageToUser(long chatId, String message) //This function is responsible for sending the message to the user
    {
        String userResponse ;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            sendMessage.validate();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public long getChatId(Update update)
    {
        long chatId = 0;
        if (update.getMessage() != null) {
            chatId = update.getMessage().getChatId(); // if the user send me text!
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId(); // if the user button pressed!
        }
        return chatId ;
    }


    public static boolean validYear (String year)
    {
        boolean isValid = false ;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(containsOnlyNumbers(year))
        {
            int Year = Integer.parseInt(year);
            if ((Year > 0) && (Year <= currentYear))
            {
                isValid = true;
            }
        }
        return isValid ;
    }
    public static boolean containsOnlyNumbers(String text)
    {
        String regex = "^[0-9]+$";
        return text.matches(regex);
    }

}