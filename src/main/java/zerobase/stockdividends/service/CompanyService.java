package zerobase.stockdividends.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.stockdividends.model.Company;
import zerobase.stockdividends.model.ScrapedResult;
import zerobase.stockdividends.persist.entity.CompanyEntity;
import zerobase.stockdividends.persist.entity.DividendEntity;
import zerobase.stockdividends.persist.repository.CompanyRepository;
import zerobase.stockdividends.persist.repository.DividendRepository;
import zerobase.stockdividends.scraper.Scraper;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;

    private final DividendRepository dividendRepository;

    public Company save (String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if(exists) {
            throw new RuntimeException("company already exists -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend (String ticker) {
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to get company from ticker -> " + ticker);
        }

        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .toList();

        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }
}
