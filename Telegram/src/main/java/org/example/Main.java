package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot());

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);

        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int totalApiRequests = calculateTotalApiRequests();
                long largestRequestByUserId = calculateLargestRequestByUser() ;
                System.out.println("Total API Requests: " + totalApiRequests);
                System.out.println("The user with the largest number of requests is : " + largestRequestByUserId);
            }
        }, 0, 15000); // Print the result every 15 seconds

        // Keep the program running indefinitely
        while (true) {
            // Perform any other tasks or wait for a specific condition
        }

}

    private static int calculateTotalApiRequests() {
        int totalApiRequests = 0;
        Map<Long , Lead> leads = MyBot.getLeadMap() ;

        // Iterate over the leadMap and sum up the API requests for each Lead
        for (Map.Entry<Long, Lead> entry : leads.entrySet()) {
            Lead lead = entry.getValue();
            totalApiRequests += lead.getApiRequest();
        }

        return totalApiRequests;
    }
    private static long calculateLargestRequestByUser() {
        int LargestRequest = 0  ;
        long LargestRequestByUser = 0 ;
        Map<Long , Lead> leads = MyBot.getLeadMap() ;

        // Iterate over the leadMap and sum up the API requests for each Lead
        for (Map.Entry<Long, Lead> entry : leads.entrySet()) {
            Lead lead = entry.getValue();
            if( lead.getApiRequest()  > LargestRequest)
            {
              LargestRequest = lead.getApiRequest() ;
              LargestRequestByUser = lead.getChatId() ;

            }
        }

        return LargestRequestByUser;
    }
}