package net.subsquid.indexerapi.beans;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import net.subsquid.indexerapi.dto.GraphqlRequestBody;
import net.subsquid.indexerapi.dto.SubsquidIndexerStatusResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class IndexersProcessingJob implements Job {
    private final Logger log = Logger.getLogger(IndexersProcessingJob.class.getName());

    @Value("${indexers.list}")
    private String indexersListURL;

    private final RestTemplate restTemplate;

    @Autowired
    public IndexersProcessingJob(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing the job");
        String githubFileContent = restTemplate.getForObject(indexersListURL, String.class);
        if(githubFileContent != null) {
            githubFileContent.lines().forEach((indexer) -> {
                try {
                    String query = Files.readString(
                        Paths.get(IndexersProcessingJob.class.getClassLoader()
                        .getResource("getIndexerStatus.graphql").toURI())
                    );
                    GraphqlRequestBody requestBody = new GraphqlRequestBody();
                    requestBody.setQuery(query);
                    requestBody.setVariables(null);
                    SubsquidIndexerStatusResponse status = restTemplate.postForObject(indexer, requestBody, SubsquidIndexerStatusResponse.class);
                    if (status != null) {
                        log.info(status.toString());
                    } else {
                        log.warning(format("Did not receive status from %s", indexer));
                    }
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            });
        } else {
            log.warning("Did not receive indexer list");
        }
    }
}
