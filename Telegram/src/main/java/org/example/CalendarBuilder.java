package org.example;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarBuilder {

    public static SendMessage buildCalendarMessage(long chatId, int year, int month) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Get the number of days in the specified month and year
        int daysInMonth = getDaysInMonth(year, month);

        // Iterate over the days of the month and create keyboard rows
        int day = 1;
        while (day <= daysInMonth) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                if (day > daysInMonth) {
                    break;
                }
                InlineKeyboardButton dayButton = new InlineKeyboardButton();
                dayButton.setText(String.valueOf(day));
                dayButton.setCallbackData("SELECT_DATE_" + year + "_" + month + "_" + day);
                row.add(dayButton);
                day++;
            }
            keyboard.add(row);
        }

        // Create navigation buttons for previous and next month
        List<InlineKeyboardButton> navigationRow = new ArrayList<>();
        InlineKeyboardButton prevMonthButton = new InlineKeyboardButton();
        prevMonthButton.setText("Previous");
        prevMonthButton.setCallbackData("PREV_MONTH_" + year + "_" + month);
        navigationRow.add(prevMonthButton);

        InlineKeyboardButton nextMonthButton = new InlineKeyboardButton();
        nextMonthButton.setText("Next");
        nextMonthButton.setCallbackData("NEXT_MONTH_" + year + "_" + month);
        navigationRow.add(nextMonthButton);

        keyboard.add(navigationRow);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    private static int getDaysInMonth(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        return date.lengthOfMonth();
    }
}