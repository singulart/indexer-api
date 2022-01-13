package net.subsquid.indexerapi.beans;

import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IndexersProcessingJob implements Job {
    private Logger log = Logger.getLogger(IndexersProcessingJob.class.getName());

    @Value("${indexers.list}")
    private String indexersList;

    private final RestTemplate restTemplate;

    @Autowired
    public IndexersProcessingJob(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.warning("Executing the job");
//        restTemplate.getForObject()
    }
}
