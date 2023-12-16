package org.example;

import java.sql.*;

class UserData {
    String id;
    String username;
    UserData(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return this.id;
    }
    String getUsername() {
        return this.username;
    }

}

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

    public UserData postUser(String username, String password) {
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
            return checkLogin(username, password);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void insertContenChat(String id) {
        String insertQuery = "INSERT INTO Content (idContent, content) VALUES (?, ?)";
        String query = "select idClient, username from Client";
        try(PreparedStatement preparedStatement = con.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idClient = resultSet.getInt("idClient");
                String name = resultSet.getString("username");

                try (PreparedStatement st = con.prepareStatement(insertQuery)) {
                    st.setString(1, id + "," + idClient);
                    st.setString(2, "");
                    int rowsAffected = st.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Data inserted successfully!");
                    } else {
                        System.out.println("Failed to insert data.");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println(idClient + name);
            }
        }catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public UserData checkLogin(String username, String password) {
        String checkQuery = "select idClient, username from Client where username = ? and password = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(checkQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int idClient = resultSet.getInt("idClient");
                String name = resultSet.getString("username");
                return new UserData(String.valueOf(idClient), name);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getContentText(String idSend, String recieve) {
        String checkQuery = "select content from Content where idContent = ? or idContent = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(checkQuery)) {
            preparedStatement.setString(1, idSend + "," + recieve);
            preparedStatement.setString(2, recieve + "," + idSend);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String content = resultSet.getString("content");
                return content;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void update(String idSend, String idReceive, String content) {
        String updateQuery = "UPDATE Content SET content = CONCAT(content, ?) WHERE (idContent = ? OR idContent = ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, idSend + "," + idReceive);
            preparedStatement.setString(3, idReceive + "," + idSend);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows updated. idContent not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnect() throws SQLException {
        this.con.close();
    }

    public void deleteContent(String idSend, String idReceive) {
        String updateQuery = "UPDATE Content SET content = ? WHERE (idContent = ? OR idContent = ?)";

        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, "#");
            preparedStatement.setString(2, idSend + "," + idReceive);
            preparedStatement.setString(3, idReceive + "," + idSend);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows updated. idContent not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String args[]) throws SQLException {
        database a = new database();
        a.update("12", "13", "user:12356#");
        System.out.println("print:" + a.getContentText("12", "13"));
    }
}
