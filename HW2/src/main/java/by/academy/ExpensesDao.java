package by.academy;

import java.sql.SQLException;
import java.util.List;

public interface ExpensesDao {
    int create(ExpensesDto expensesDto) throws SQLException;

    ExpensesDto read(int num) throws SQLException;

    List<ExpensesDto> readAll() throws SQLException;

    void update(ExpensesDto expensesDto) throws SQLException;

    boolean delete(int num) throws SQLException;
}
