package by.academy;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExpensesDaoImplTest {
    private ExpensesDaoImpl expensesDao;
    private IDatabaseConnection connection;

    @Before
    public void setUp() throws Exception {
        try {
            expensesDao = (ExpensesDaoImpl) ExpensesDaoFactory.getExpensesDao("mysql_test");
            connection = new MySqlConnection(MySqlDataSource.getTestConnection(), "listexpenses_test");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
        expensesDao = null;
    }

    @Test
    public void create() throws SQLException {
        ExpensesDto expensesDto = new ExpensesDto();
        expensesDto.setValue(1);
        expensesDto.setReceiver(2);
        expensesDto.setPaydate(Date.valueOf("2020-07-09"));
        expensesDto.setNum(455);
        expensesDao.create(expensesDto);
        ExpensesDto expensesDto1 = expensesDao.read(455);
        assertEquals(expensesDto, expensesDto1);
        expensesDao.delete(455);
    }

    @Test
    public void read() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ExpensesDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        ExpensesDto expensesDtoTest = new ExpensesDto();
        expensesDtoTest.setNum(2);
        expensesDtoTest.setPaydate(Date.valueOf("2004-03-03"));
        expensesDtoTest.setReceiver(2);
        expensesDtoTest.setValue(13);
        //When
        ExpensesDto expensesDto = expensesDao.read(2);
        //Then
        assertEquals(expensesDto, expensesDtoTest);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void readAll() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ExpensesDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        List<ExpensesDto> expensesTest = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ExpensesDto expensesDto = new ExpensesDto();
            expensesDto.setNum(i + 2);
            expensesDto.setPaydate(Date.valueOf("2004-03-03"));
            expensesDto.setReceiver(i + 2);
            expensesDto.setValue(13+i);
            expensesTest.add(expensesDto);
        }
        //When
        List<ExpensesDto> expenses = expensesDao.readAll();
        //Then
        assertEquals(expenses, expensesTest);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void update() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ExpensesDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        ExpensesDto expensesDtoTest = new ExpensesDto();
        expensesDtoTest.setNum(2);
        expensesDtoTest.setPaydate(Date.valueOf("2004-03-03"));
        expensesDtoTest.setReceiver(3);
        expensesDtoTest.setValue(16);
        //When
        expensesDao.update(expensesDtoTest);
        ExpensesDto expensesDto = expensesDao.read(2);
        //Then
        assertEquals(expensesDto, expensesDtoTest);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void delete() throws SQLException {
        //Given
        ExpensesDto expensesDto = new ExpensesDto();
        expensesDto.setValue(1);
        expensesDto.setReceiver(2);
        expensesDto.setPaydate(Date.valueOf("2020-07-09"));
        expensesDto.setNum(455);
        expensesDao.create(expensesDto);
        //When
        expensesDao.delete(455);
        //Then
        assertNull(expensesDao.read(455));

    }

    @Test
    public void getMaxNum() throws SQLException, DatabaseUnitException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ExpensesDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        int maxNum = expensesDao.getMaxNum();

        //Then
        assertEquals(maxNum, 5);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }
}