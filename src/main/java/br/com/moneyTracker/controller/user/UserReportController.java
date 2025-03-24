package br.com.moneyTracker.controller.user;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.service.reportTransaction.TransactionReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserReportController {

    private TransactionReportService transactionReportService;

    public UserReportController(TransactionReportService transactionReportService) {
        this.transactionReportService = transactionReportService;
    }

    @GetMapping("/transactions/report/excel")
    public void exportToExcel(HttpServletResponse response, User user) throws IOException {
        this.transactionReportService.exportToExcel(response, user);
    }

}
