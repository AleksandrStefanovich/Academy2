package by.academy;

import java.sql.*;
import java.util.Properties;

public class HW2Main {

    public static void main(String[] args) {

        Date date = Date.valueOf("2020-07-09");
        int num = 5;
        int receiver = 2;
        double value = 15.45d;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/listexpenses";
            Properties properties = new Properties();
            properties.put("user", "root");
            properties.put("password", "root");
            properties.put("useSSL", "false");

            Connection connection = DriverManager.getConnection(
                    url,
                    properties
            );

            PreparedStatement preparedStatement = connection.prepareStatement("insert into listexpenses.expenses " +
                    "values (?, ?, ?, ?)");
                preparedStatement.setInt(1, num);
                preparedStatement.setDate(2, date);
                preparedStatement.setInt(3, receiver);
                preparedStatement.setDouble(4, value);

                preparedStatement.execute();

            Statement statement = connection.createStatement();
            String query = "SELECT paydate,value,name FROM expenses,receivers WHERE receiver=receivers.num";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                System.out.print(resultSet.getRow()+" ");
                System.out.print(resultSet.getDate(1)+" ");
                System.out.print(resultSet.getDouble(2)+" ");
                System.out.println(resultSet.getString(3)+" ");
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}
