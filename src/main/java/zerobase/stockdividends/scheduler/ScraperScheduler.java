package zerobase.stockdividends.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.ScrapedResult;
import zerobase.stockdividends.persist.entity.CompanyEntity;
import zerobase.stockdividends.persist.entity.DividendEntity;
import zerobase.stockdividends.persist.repository.CompanyRepository;
import zerobase.stockdividends.persist.repository.DividendRepository;
import zerobase.stockdividends.scraper.Scraper;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("Scheduler started!");
        List<CompanyEntity> companies = this.companyRepository.findAll();

        for (var company : companies) {
            log.info("scraping -> " + company.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(Company.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());

            scrapedResult.getDividends().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists) {
                            this.dividendRepository.save(e);
                        }
                    });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
