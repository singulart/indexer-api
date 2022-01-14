package net.subsquid.indexerapi.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public JobDetailFactoryBean jobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(IndexersProcessingJob.class);
        jobDetailFactory.setDescription("Gets the data about deployed indexers and retrieves their statuses");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean trigger(JobDetail job, @Value("${repeat.interval.secs:3600}") long repeatInterval) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(job);
        trigger.setRepeatInterval(repeatInterval * 1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
        @Value("${connect.timeout.millis:200}") long connectTimeout,
        @Value("${read.timeout.millis:400}") long readTimeout
        ) {

        return builder
            .setConnectTimeout(Duration.of(connectTimeout, ChronoUnit.MILLIS))
            .setReadTimeout(Duration.of(readTimeout, ChronoUnit.MILLIS))
            .build();
    }
}
