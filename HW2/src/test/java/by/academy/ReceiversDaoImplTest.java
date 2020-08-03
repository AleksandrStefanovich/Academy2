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

public class ReceiversDaoImplTest {
    private ReceiversDaoImpl receiversDao;
    private IDatabaseConnection connection;
    @Before
    public void setUp() throws Exception {
        try {
            receiversDao = (ReceiversDaoImpl) ReceivsersDaoFactory.getReceiversDao("mysql_test");
            connection = new MySqlConnection(MySqlDataSource.getTestConnection(), "listexpenses_test");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
        receiversDao = null;
    }

    @Test
    public void create() throws SQLException {
        ReceiversDto receiversDto = new ReceiversDto();
        receiversDto.setNum(1);
        receiversDto.setName("2");
        receiversDao.create(receiversDto);
        ReceiversDto receiversDto1 = receiversDao.read(1);
        assertEquals(receiversDto, receiversDto1);
        receiversDao.delete(1);
    }

    @Test
    public void read() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ReceiversDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        ReceiversDto receiversDtoTest = new ReceiversDto();
        receiversDtoTest.setNum(2);
        receiversDtoTest.setName("name2");

        //When
        ReceiversDto receiversDto = receiversDao.read(2);
        //Then
        assertEquals(receiversDto, receiversDtoTest);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void readAll() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ReceiversDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        List<ReceiversDto> receiversTest = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ReceiversDto receiversDto = new ReceiversDto();
            receiversDto.setNum(i + 2);
            receiversDto.setName("name" + (i+2));
            receiversTest.add(receiversDto);
        }
        //When
        List<ReceiversDto> receivers = receiversDao.readAll();
        //Then
        assertEquals(receivers, receiversTest);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void update() throws DatabaseUnitException, SQLException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ReceiversDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        ReceiversDto receiversDtoTest = new ReceiversDto();
        receiversDtoTest.setNum(2);
        receiversDtoTest.setName("test_name");

        //When
        receiversDao.update(receiversDtoTest);
        ReceiversDto receiversDto = receiversDao.read(2);
        //Then
        assertEquals(receiversDto, receiversDtoTest);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }

    @Test
    public void delete() throws SQLException {
        //Given
        ReceiversDto receiversDto = new ReceiversDto();
        receiversDto.setNum(1);
        receiversDto.setName("test_name");

        receiversDao.create(receiversDto);
        //When
        receiversDao.delete(1);
        //Then
        assertNull(receiversDao.read(1));

    }

    @Test
    public void getMaxNum() throws SQLException, DatabaseUnitException {
        //Given
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(ExpensesDaoImplTest.class
                        .getResourceAsStream("ReceiversDaoImplTest.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

        //When
        int maxNum = receiversDao.getMaxNum();

        //Then
        assertEquals(maxNum, 5);
        DatabaseOperation.DELETE.execute(connection, dataSet);
    }
}