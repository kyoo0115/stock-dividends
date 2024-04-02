package zerobase.stockdividends.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestScheduler {

    @Scheduled(cron = "0/5 * * * * *")
    public void test() {
        System.out.println(System.currentTimeMillis());
    }
}
