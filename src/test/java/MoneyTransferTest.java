import com.task.controller.MoneyTransferServlet;
import com.task.db.core.AccountDao;
import com.task.db.core.DaoFactory;
import com.task.db.hsqldb.HSQLDBDaoFactory;
import com.task.model.Account;
import com.task.model.Currency;
import com.task.model.CurrencyName;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class MoneyTransferTest extends Mockito {
    private DaoFactory daoFactory;

    @Test
    public void testTransfer() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("from")).thenReturn("2200000000000000");
        when(request.getHeader("to")).thenReturn("2200000000000001");
        when(request.getHeader("amount")).thenReturn("100");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        MoneyTransferServlet moneyTransferServlet = new MoneyTransferServlet();

        daoFactory = new HSQLDBDaoFactory();

        daoFactory.getCurrencyDao().create(new Currency(CurrencyName.RUB, 1));
        daoFactory.getCurrencyDao().create(new Currency(CurrencyName.USD, 62.9776));
        daoFactory.getCurrencyDao().create(new Currency(CurrencyName.EUR, 70.6546));

        daoFactory.getAccountDao().create(new Account("Ivanov Ivan Ivanovich", 10000, CurrencyName.RUB, "2200000000000000"));
        daoFactory.getAccountDao().create(new Account("Petrov Petr Petrovich", 20000, CurrencyName.RUB, "2200000000000001"));

        moneyTransferServlet.doPost(request, response);
        writer.flush();

        assertTrue(stringWriter.toString().contains("Transfer was made successfully!"));
        assertEquals((int) daoFactory.getAccountDao().getByCardNumber("2200000000000000").getAmount(), 9900);
        assertEquals((int) daoFactory.getAccountDao().getByCardNumber("2200000000000001").getAmount(), 20100);
        assertEquals(1, daoFactory.getHistoryDao().getAll().size());

        clearTables("History", "Currency", "Account");
    }

    private void clearTables(String... tableNames) {
        Connection connection = daoFactory.getConnection();
        for (String tableName : tableNames) {
            try (PreparedStatement stm = connection.prepareStatement("delete from ts_db." + tableName)) {
                stm.execute();
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
