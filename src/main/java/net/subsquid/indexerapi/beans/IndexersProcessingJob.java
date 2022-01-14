package net.subsquid.indexerapi.beans;

import static java.lang.String.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.subsquid.indexerapi.dto.GraphqlRequestBody;
import net.subsquid.indexerapi.dto.IndexersRegistry;
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

    private final ObjectMapper mapper;

    @Autowired
    public IndexersProcessingJob(RestTemplate restTemplate, InMemoryStatusesStore store,
        ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.store = store;
        this.mapper = mapper;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing the job");
        var githubFileContent = restTemplate.getForObject(indexersListURL, String.class);
        if(githubFileContent != null) {
            IndexersRegistry indexerRegistry = null;
            try {
                indexerRegistry = mapper.readValue(githubFileContent, IndexersRegistry.class);
            } catch (JsonProcessingException e) {
                log.log(Level.WARNING, "Unable to parse json file", e);
                return;
            }
            ArrayList<SubsquidIndexerStatus> statuses = new ArrayList<>();
            indexerRegistry.getArchives().forEach((indexer) -> {
                try {
                    var query = new String(IndexersProcessingJob.class.getClassLoader()
                        .getResourceAsStream("getIndexerStatus.graphql").readAllBytes());
                    var requestBody = new GraphqlRequestBody();
                    requestBody.setQuery(query);
                    requestBody.setVariables(null);
                    var status = restTemplate.postForObject(indexer.getUrl(), requestBody, SubsquidIndexerStatusResponse.class);
                    if (status != null) {
                        status.getData().getIndexerStatus().setNetwork(indexer.getNetwork());
                        status.getData().getIndexerStatus().setUrl(indexer.getUrl());
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
