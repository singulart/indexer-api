package net.subsquid.indexerapi.beans;

import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class IndexersProcessingJob implements Job {
    private Logger log = Logger.getLogger(IndexersProcessingJob.class.getName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.warning("Executing the job");
    }
}
