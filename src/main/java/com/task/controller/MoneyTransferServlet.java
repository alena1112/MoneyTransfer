package com.task.controller;

import com.task.db.DatabaseType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MoneyTransferServlet extends HttpServlet {
    private static final String FROM_ACCOUNT = "from";
    private static final String TO_ACCOUNT = "to";
    private static final String AMOUNT = "amount";

    private MoneyTransferController controller = new MoneyTransferController(DatabaseType.HSQLDB);

    private static final Logger log = Logger.getLogger(MoneyTransferServlet.class);

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String fromAccCardNumber = req.getHeader(FROM_ACCOUNT);
            String toAccCardNumber = req.getHeader(TO_ACCOUNT);
            String amount = req.getHeader(AMOUNT);

            if (StringUtils.isAllBlank(fromAccCardNumber, toAccCardNumber, amount)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                log.error("Incorrect request headers");
                return;
            }

            boolean result = controller.transfer(fromAccCardNumber, toAccCardNumber, Double.parseDouble(amount));

            resp.getWriter().println(result ? "Transfer was made successfully!" :
                    "Transfer failed");

        } catch (NumberFormatException e) {
            log.error("Incorrect request headers", e);
        } catch (IOException e) {
            log.error("Exception during the transfer", e);
        }
    }
}
