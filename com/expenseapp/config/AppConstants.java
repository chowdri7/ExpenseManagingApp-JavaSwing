package com.expenseapp.config;

import java.util.Arrays;
import java.util.List;

public class AppConstants {
    public static final String TRANSACTION_IN = "Cash In";
    public static final String TRANSACTION_OUT = "Cash Out";
    

    public static final List<String> CATEGORIES = Arrays.asList(
        "Food", "Rent", "Transport", "Shopping", "Salary", "Investment", "Medical", "Entertainment", "Miscellaneous"
    );

    public static final List<String> MODES_OF_TRANSACTION = Arrays.asList(
        "Cash", "Card", "UPI", "Bank Transfer", "Cheque", "Others"
    );

    public static final String DEFAULT_PASSBOOK_NAME = "Main Passbook";
}
