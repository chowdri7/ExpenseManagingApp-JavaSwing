package com.expenseapp.model;

import java.util.Date;

public class ExpenseEntry {
    private int id;
    private int passbookId;
    private String type; // Cash In or Cash Out
    private String category;
    private double amount;
    private String remarks;
    private String personName;
    private String contactDetails;
    private String modeOfTransaction;
    private Date date;

    public ExpenseEntry() {}

    public ExpenseEntry(int id, int passbookId, String type, String category, double amount,
                        String remarks, String personName, String contactDetails, String modeOfTransaction, Date date) {
        this.id = id;
        this.passbookId = passbookId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.remarks = remarks;
        this.personName = personName;
        this.contactDetails = contactDetails;
        this.modeOfTransaction = modeOfTransaction;
        this.date = date;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPassbookId() { return passbookId; }
    public void setPassbookId(int passbookId) { this.passbookId = passbookId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }

    public String getContactDetails() { return contactDetails; }
    public void setContactDetails(String contactDetails) { this.contactDetails = contactDetails; }

    public String getModeOfTransaction() { return modeOfTransaction; }
    public void setModeOfTransaction(String modeOfTransaction) { this.modeOfTransaction = modeOfTransaction; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
