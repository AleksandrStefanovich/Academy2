package by.academy;

import java.sql.SQLException;
import java.util.List;

public interface ReceiversDao {
    int create(ReceiversDto receiversDto) throws SQLException;

    ReceiversDto read(int num) throws SQLException;

    List<ReceiversDto> readAll() throws SQLException;

    void update(ReceiversDto receiversDto) throws SQLException;

    boolean delete(int num) throws SQLException;
}
