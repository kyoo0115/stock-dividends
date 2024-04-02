package zerobase.stockdividends.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.stockdividends.service.FinanceService;

@RestController
@AllArgsConstructor
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName) {
        var result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }
}
