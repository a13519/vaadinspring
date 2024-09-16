package net.zousys.gba;

import net.zousys.gba.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {
    @Autowired
    private BatchService batchService;

    @Override
    public void run(String... args) throws Exception {
//        batchService.runJob("S2");
//        batchService.runJob("S2");
    }
}
