package com.execution;

import com.util.DbUtil;
import com.model.Account;
import com.model.Profile;

public class Executor {
    private static final int ROW_NUMBER = 500;
    private final Account account = new Account();
    private final Profile profile = new Profile();
    private final DbUtil dbUtil = DbUtil.getINSTANCE();
    private Account petroAccount;
    private Profile petroProfile;
    private Account vasilAccount;
    private Profile vasilProfile;
    private Profile profileFromTable;
    private Account accountFromTable;

    public void startProcessing() {
        createPetroAccountAndProfile();
        insertAccountAndProfileIntoTable(petroAccount, petroProfile);
        printAccountsAndProfiles();

        createVasilAccountAndProfile();
        updateAccountAndProfileById(vasilAccount, vasilProfile);
        printAccountsAndProfiles();

        removeAccountAndProfile(vasilAccount, vasilProfile);
        printAccountsAndProfiles();

        getAccountAndProfileFromTables(ROW_NUMBER);
        printAccountAndProfileInformation(accountFromTable, profileFromTable);
    }

    private void printAccountAndProfileInformation(Account account, Profile profile) {
        if (account != null && profile != null) {
            System.out.println(accountFromTable.toString());
            System.out.println(profileFromTable.toString());
        } else {
            System.out.println("There no such a rows inside tables");
        }
    }

    private void getAccountAndProfileFromTables(int rowNumber) {
        accountFromTable = (Account) dbUtil.selectDataFromTable(rowNumber, account);
        profileFromTable = (Profile) dbUtil.selectDataFromTable(rowNumber, profile);
    }

    private void updateAccountAndProfileById(Account account, Profile profile) {
        dbUtil.updateModelInDB(account);
        dbUtil.updateModelInDB(profile);
    }

    private void removeAccountAndProfile(Account account, Profile profile) {
        dbUtil.removeModelFromDB(account);
        dbUtil.removeModelFromDB(profile);
    }

    private void printAccountsAndProfiles() {
        dbUtil.printTableIntoConsole(account);
        dbUtil.printTableIntoConsole(profile);
    }

    private void insertAccountAndProfileIntoTable(Account account, Profile profile) {
        dbUtil.insertModelIntoDB(account);
        dbUtil.insertModelIntoDB(profile);
    }

    private void createPetroAccountAndProfile() {
        petroAccount = new Account(1001, "petro", "Petro",
                "Kim", "Kiev", "male");
        petroProfile = new Profile(1001, "petro", "Technician",
                "Engineering", "Centimia", "ZoomInfo");
    }

    private void createVasilAccountAndProfile() {
        vasilAccount = new Account(1001, "vasil", "Vasil",
                "Ten", "Lviv", "male");
        vasilProfile = new Profile(1001, "vasil", "IT",
                "Accounting", "VDN", "Java");
    }
}