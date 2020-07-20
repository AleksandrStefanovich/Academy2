package by.academy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReceiversDaoImpl implements ReceiversDao {
    private static Logger log = Logger.getLogger(ReceiversDaoImpl.class.getName());

    private Connection connection;
    boolean isTestInstance;

    public ReceiversDaoImpl() throws SQLException {
        this.connection = MySqlDataSource.getConnection();
    }

    public ReceiversDaoImpl(boolean isTestInstance) throws SQLException {
        this.isTestInstance = false;
        this.connection = MySqlDataSource.getTestConnection();
    }
    @Override
    public int create(ReceiversDto receiversDto) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into receivers " +
                        "values (?, ?)");
        preparedStatement.setInt(1, receiversDto.getNum());
        preparedStatement.setString(2, receiversDto.getName());

        boolean result = preparedStatement.execute();
        preparedStatement.close();
        if (result) return receiversDto.getNum();
        else return -1;
    }

    @Override
    public ReceiversDto read(int num) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("select * from receivers where num=?");
        statement.setInt(1,num);
        ResultSet resultSet = statement.executeQuery();
        List<ReceiversDto> receiversDtos = parseResultSet(resultSet);
        statement.close();
        return receiversDtos.size() > 0 ? receiversDtos.get(0) : null;
    }

    private List<ReceiversDto> parseResultSet(ResultSet resultSet)  throws SQLException{
        List<ReceiversDto> receivers = new ArrayList<>();
        while(resultSet.next()){
            ReceiversDto receiver = new ReceiversDto();
            receiver.setNum(resultSet.getInt(1));
            receiver.setName(resultSet.getString(2));
            receivers.add(receiver);
        }
        return receivers;
    }

    @Override
    public List<ReceiversDto> readAll() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from receivers");
        List<ReceiversDto> receiversDtos = parseResultSet(resultSet);
        statement.close();
        return receiversDtos;
    }

    @Override
    public void update(ReceiversDto receiversDto) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update receivers " +
                        "set name=? " +
                        "where num=?"
        );
        preparedStatement.setString(1,receiversDto.getName());
        preparedStatement.setInt(2,receiversDto.getNum());
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override
    public boolean delete(int num) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from receivers where num=?");
        preparedStatement.setInt(1,num);
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        return result;
    }

    public int getMaxNum() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select max(num) from receivers");
        int num = 0;
        if (resultSet.next()) num = resultSet.getInt(1);
        statement.close();
        return num;
    }
}
