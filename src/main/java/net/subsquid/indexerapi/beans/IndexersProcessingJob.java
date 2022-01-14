package net.subsquid.indexerapi.beans;

import static java.lang.String.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.subsquid.indexerapi.dto.GraphqlRequestBody;
import net.subsquid.indexerapi.dto.SubsquidIndexerStatus;
import net.subsquid.indexerapi.dto.SubsquidIndexerStatusResponse;
import net.subsquid.indexerapi.store.InMemoryStatusesStore;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class IndexersProcessingJob implements Job {
    private final Logger log = Logger.getLogger(IndexersProcessingJob.class.getName());

    @Value("${indexers.list}")
    private String indexersListURL;

    private final RestTemplate restTemplate;

    private final InMemoryStatusesStore store;

    @Autowired
    public IndexersProcessingJob(RestTemplate restTemplate, InMemoryStatusesStore store) {
        this.restTemplate = restTemplate;
        this.store = store;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing the job");
        var githubFileContent = restTemplate.getForObject(indexersListURL, String.class);
        if(githubFileContent != null) {
            ArrayList<SubsquidIndexerStatus> statuses = new ArrayList<>();
            githubFileContent.lines().forEach((indexer) -> {
                try {
                    var query = new String(IndexersProcessingJob.class.getClassLoader()
                        .getResourceAsStream("getIndexerStatus.graphql").readAllBytes());
                    var requestBody = new GraphqlRequestBody();
                    requestBody.setQuery(query);
                    requestBody.setVariables(null);
                    var status = restTemplate.postForObject(indexer, requestBody, SubsquidIndexerStatusResponse.class);
                    if (status != null) {
                        status.getData().getIndexerStatus().setUrl(indexer);
                        log.info(status.toString());
                        statuses.add(status.getData().getIndexerStatus());
                    } else {
                        log.warning(format("Did not receive status from %s", indexer));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RestClientException e) {
                    log.log(Level.WARNING, "Client error getting indexer status", e);
                }
            });
            store.getStatusesStore().clear();
            store.getStatusesStore().addAll(statuses);
        } else {
            log.warning("Did not receive indexer list");
        }
    }
}
