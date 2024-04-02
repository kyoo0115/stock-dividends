package zerobase.stockdividends.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<CompanyEntity> getAllCompanies(final Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    private Company storeCompanyAndDividend (String ticker) {
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to get company from ticker -> " + ticker);
        }

        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividends().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .toList();

        this.dividendRepository.saveAll(dividendEntities);
        return company;
    }

    public List<String> getCompanyNamesByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0,10);
        Page<CompanyEntity> companyEntities = this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(CompanyEntity::getName)
                .toList();
    }
}
