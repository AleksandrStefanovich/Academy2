package by.academy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExpensesDaoImpl implements ExpensesDao {
    private static Logger log = Logger.getLogger(ExpensesDaoImpl.class.getName());

    private Connection connection;
    boolean isTestInstance;

    public ExpensesDaoImpl() throws SQLException {
        this.connection = MySqlDataSource.getConnection();
    }

    public ExpensesDaoImpl(boolean isTestInstance) throws SQLException {
        this.isTestInstance = false;
        this.connection = MySqlDataSource.getTestConnection();
    }
    @Override
    public int create(ExpensesDto expensesDto) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into expenses " +
                        "values (?, ?, ?, ?)");
        preparedStatement.setInt(1, expensesDto.getNum());
        preparedStatement.setDate(2, expensesDto.getPaydate());
        preparedStatement.setInt(3, expensesDto.getReceiver());
        preparedStatement.setDouble(4, expensesDto.getValue());

        boolean result = preparedStatement.execute();
        preparedStatement.close();
        if (result) return expensesDto.getNum();
        else return -1;
    }

    @Override
    public ExpensesDto read(int num) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("select * from expenses where num=?");
        statement.setInt(1,num);
        ResultSet resultSet = statement.executeQuery();
        List<ExpensesDto> expensesDtos = parseResultSet(resultSet);
        statement.close();
        return expensesDtos.size() > 0 ? expensesDtos.get(0) : null;
    }

    private List<ExpensesDto> parseResultSet(ResultSet resultSet)  throws SQLException{
        List<ExpensesDto> expenses = new ArrayList<>();
        while(resultSet.next()){
            ExpensesDto expense = new ExpensesDto();
            expense.setNum(resultSet.getInt(1));
            expense.setPaydate(resultSet.getDate(2));
            expense.setReceiver(resultSet.getInt(3));
            expense.setValue(resultSet.getDouble(4));
            expenses.add(expense);
        }
        return expenses;
    }

    @Override
    public List<ExpensesDto> readAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from expenses");
        List<ExpensesDto> expensesDtos = parseResultSet(resultSet);
        statement.close();
        return expensesDtos;
    }

    @Override
    public void update(ExpensesDto expensesDto) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update expenses " +
                        "set paydate=?, receiver=?, value=? " +
                        "where num=?"
        );
        preparedStatement.setDate(1,expensesDto.getPaydate());
        preparedStatement.setInt(2,expensesDto.getReceiver());
        preparedStatement.setDouble(3,expensesDto.getValue());
        preparedStatement.setInt(4,expensesDto.getNum());
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public boolean delete(int num) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from expenses where num=?");
        preparedStatement.setInt(1,num);
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        return result;
    }

    public int getMaxNum() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select max(num) from expenses");
        int num = 0;
        if (resultSet.next()) num = resultSet.getInt(1);
        statement.close();
        return num;
    }
}
