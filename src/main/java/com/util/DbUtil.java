package com.util;

import com.model.Account;
import com.model.Model;
import com.model.Profile;

import java.sql.*;

public class DbUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/dev_profiles_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1607";
    private static final String ACCOUNTS_TABLE = "dev_profiles_db.public.accounts";
    private static final String PROFILES_TABLE = "dev_profiles_db.public.profiles";
    private static DbUtil INSTANCE;

    private DbUtil() {
    }

    public static DbUtil getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DbUtil();
        }
        return INSTANCE;
    }

    private Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public void insertModelIntoDB(Model model) {
        if (model instanceof Account) {
            insertAccountIntoDB((Account) model);
        } else {
            insertProfileIntoDB((Profile) model);
        }
    }

    private void insertAccountIntoDB(Account model) {
        String SQL = "INSERT INTO " + ACCOUNTS_TABLE +
                "(id, first_name, last_name, city, gender, username) VALUES(?,?,?,?,?,?)";
        String[] accountData = {model.getFIRST_NAME(), model.getLAST_NAME(),
                model.getCITY(), model.getGENDER(), model.getUSER_NAME(),
                String.valueOf(model.getID())};
        insertIntoDB(accountData, SQL);
    }

    private void insertProfileIntoDB(Profile model) {
        String SQL = "INSERT INTO " + PROFILES_TABLE +
                "(id, username, job_title, department, company, skill) VALUES(?,?,?,?,?,?)";
        String[] profileData = {model.getUSER_NAME(), model.getJOB_TITLE(),
                model.getDEPARTMENT(), model.getCOMPANY(), model.getSKILL(),
                String.valueOf(model.getID())};
        insertIntoDB(profileData, SQL);
    }

    private void insertIntoDB(String[] data, String SQLCommand) {
        try (Connection connection = establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommand);
            int idPositionOnArray = 5;
            int id = Integer.parseInt(data[idPositionOnArray]);
            preparedStatement.setInt(1, id);
            for (int x = 2; x < 7; x++) {
                preparedStatement.setString(x, data[x - 2]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void printTableIntoConsole(Model model) {
        if (model instanceof Account) {
            String SQL = "SELECT * FROM " + ACCOUNTS_TABLE;
            createConnectionAndPrintTable(SQL);
        } else {
            String SQL = "SELECT * FROM " + PROFILES_TABLE;
            createConnectionAndPrintTable(SQL);
        }
    }

    private void createConnectionAndPrintTable(String SQLCommand) {
        try (Connection connection = establishConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQLCommand);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + "," + resultSet.getString(2) + "," +
                        resultSet.getString(3) + "," + resultSet.getString(4) + "," +
                        resultSet.getString(5) + "," + resultSet.getString(6));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Model selectDataFromTable(int numberOfRow, Model model) {
        if (model instanceof Account) {
            String SQL = "SELECT id, first_name, last_name, city, gender, username FROM " + ACCOUNTS_TABLE +
                    " WHERE id = ?";
            ResultSet rs = getDataFromTheTable(numberOfRow, SQL);
            model = createFromTableData(model, rs);
        } else {
            String SQL = "SELECT id, username, job_title, department, company, skill FROM " + PROFILES_TABLE +
                    " WHERE id = ?";
            ResultSet rs = getDataFromTheTable(numberOfRow, SQL);
            model = createProfileFromTableData(model, rs);
        }
        return model;
    }

    private Model createFromTableData(Model model, ResultSet rs) {
        try {
            model = createAccountFromTheTableData(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    private Model createProfileFromTableData(Model model, ResultSet rs) {
        try {
            model = createProfileFromTheTableData(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    private ResultSet getDataFromTheTable(int numberOfRow, String SQLCommand) {
        ResultSet rs = null;
        try (Connection connection = establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQLCommand);
            preparedStatement.setInt(1, numberOfRow);
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    private Model createAccountFromTheTableData(ResultSet rs) throws SQLException {
        Model account = null;
        while (rs.next()) {
            account = new Account(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6));
        }
        return account;
    }

    private Model createProfileFromTheTableData(ResultSet rs) throws SQLException {
        Model profile = null;
        while (rs.next()) {
            profile = new Profile(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6));
        }
        return profile;
    }

    public void updateModelInDB(Model model) {
        if (model instanceof Account) {
            String SQL = getSQLForUpdatingAccount((Account) model);
            int id = ((Account) model).getID();
            updateDB(SQL, id);
        } else {
            String SQL = getSQLForUpdatingProfile((Profile) model);
            int id = ((Profile) model).getID();
            updateDB(SQL, id);
        }
    }

    private void updateDB(String SQL, int id) {
        try (Connection connection = establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String getSQLForUpdatingAccount(Account model) {
        return "UPDATE " + ACCOUNTS_TABLE +
                " SET first_name ='" + model.getFIRST_NAME() + "', last_name ='" + model.getLAST_NAME() +
                "', city = '" + model.getCITY() + "', gender=  '" + model.getGENDER() + "'," +
                " username = '" + model.getUSER_NAME() + "'  WHERE id = ?";
    }

    private String getSQLForUpdatingProfile(Profile model) {
        return "UPDATE " + PROFILES_TABLE +
                "  SET  username = '" + model.getUSER_NAME() + "', job_title = '" + model.getJOB_TITLE() +
                "', department= '" + model.getDEPARTMENT() + "', company= '" + model.getCOMPANY() + "', skill = '" +
                model.getSKILL() + "' WHERE id = ?";
    }

    public void removeModelFromDB(Model model) {
        if (model instanceof Account) {
            String SQL = "DELETE FROM " + ACCOUNTS_TABLE + " WHERE id = ?";
            int id = ((Account) model).getID();
            removeFromTableById(SQL, id);
        } else {
            String SQL = "DELETE FROM " + PROFILES_TABLE + " WHERE id = ?";
            int id = ((Profile) model).getID();
            removeFromTableById(SQL, id);
        }
    }

    private void removeFromTableById(String SQL, int id) {
        try (Connection connection = establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}