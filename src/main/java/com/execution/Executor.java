package com.execution;

import com.database.DbUtil;
import com.model.Account;
import com.model.Profile;

public class Executor {
    private Account accounts = new Account();
    private Profile profiles = new Profile();
    private Account petroAccount;
    private Profile petroProfile;
    private Account vasilAccount;
    private Profile vasilProfile;
    private Profile profileFromTable;
    private Account accountFromTable;
    private DbUtil dbUtil = DbUtil.getINSTANCE();

    public void startProcessing() {
        createPetroAccountAndProfile();
        insertAccountAndProfileIntoTable(petroAccount, petroProfile);
        printAccountsAndProfiles();

        createVasilAccountAndProfile();
        updateAccountAndProfileById(vasilAccount, vasilProfile);
        printAccountsAndProfiles();

        removeAccountAndProfile(vasilAccount, vasilProfile);
        printAccountsAndProfiles();
        getAccountAndProfileFromTables(500);
        tryToPrintAccountAndProfileInformation(accountFromTable, profileFromTable);
    }

    private void tryToPrintAccountAndProfileInformation(Account account, Profile profile) {
        if (account != null && profile != null) {
            System.out.println(accountFromTable.toString());
            System.out.println(profileFromTable.toString());
        } else {
            System.out.println("There no such a rows inside tables");
        }
    }

    private void getAccountAndProfileFromTables(int rowNumber) {
        accountFromTable = (Account) dbUtil.selectDataFromTable(rowNumber, accounts);
        profileFromTable = (Profile) dbUtil.selectDataFromTable(rowNumber, profiles);
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
        dbUtil.printTableIntoConsole(accounts);
        dbUtil.printTableIntoConsole(profiles);
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