package org.example;

import java.sql.*;

public class database {
    Statement st;
    Connection con = null;
    database () {
        connectDatabase();
    }

    void connectDatabase() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ChatBoxJAVA;encrypt=true;trustServerCertificate=true;user=sa;password=123456");
            st = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet query(String query) {
        try {
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void postUser(String username, String password) {
        String insertQuery = "INSERT INTO Client (username, password) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Failed to insert data.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int checkLogin(String username, String password) {
        String checkQuery = "select idClient from Client where username = ? and password = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(checkQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idClient = resultSet.getInt("idClient");
                return idClient;
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void closeConnect() throws SQLException {
        this.con.close();
    }
//    public static void main(String args[]) throws SQLException {
//        database a = new database();
////        ResultSet rs = a.query("select * from GIAOVIEN");
////        a.closeConnect();
//
//        String data = "user,123456";
//        a.postUser(data);
//    }
}
