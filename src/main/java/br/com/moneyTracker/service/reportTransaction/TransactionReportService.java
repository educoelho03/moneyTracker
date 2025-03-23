package br.com.moneyTracker.service.reportTransaction;

import br.com.moneyTracker.domain.entities.User;
import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import br.com.moneyTracker.repository.TransactionRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TransactionReportService {

    private TransactionRepository transactionRepository;
    private TransactionExportToExcelService transactionExportToExcelService;

    public TransactionReportService(TransactionRepository transactionRepository, TransactionExportToExcelService transactionExportToExcelService) {
        this.transactionRepository = transactionRepository;
        this.transactionExportToExcelService = transactionExportToExcelService;
    }

    public void exportToExcel(HttpServletResponse response, User user) throws IOException { // TODO: CORRIGIR ESSE METODO PARA EU PASSAR O TOKEN
        List<TransactionResponseDTO> data = transactionRepository.getAllByUser(user);
        transactionExportToExcelService.exportToExcel(response, data);
    }

}
