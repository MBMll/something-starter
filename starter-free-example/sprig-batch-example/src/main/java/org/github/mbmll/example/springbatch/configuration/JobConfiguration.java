package org.github.mbmll.example.springbatch.configuration;

import java.util.function.Function;
import javax.persistence.EntityManagerFactory;
import org.github.mbmll.example.springbatch.entity.source.Attachment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @Author xlc
 * @Description
 * @Date 2024/1/7 18:00:12
 */
@Configuration
@EnableBatchProcessing
public class JobConfiguration {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Job job(JobRepository jobRepository,
        @Qualifier("sourceEntityManagerFactory") EntityManagerFactory sourceEntityManagerFactory,
        @Qualifier("targetEntityManagerFactory") EntityManagerFactory targetEntityManagerFactory,
        @Qualifier("targetTransactionManager") PlatformTransactionManager targetEntityManager
    ) {
        return new JobBuilder("attachmentJob")
            .start(
                new StepBuilder("step")
                    .repository(jobRepository)
                    .transactionManager(targetEntityManager)
                    .<Attachment, Attachment>chunk(1)
                    .reader(new JpaPagingItemReaderBuilder<Attachment>()
                        .entityManagerFactory(sourceEntityManagerFactory)
                        .build())
                    .processor(Function.identity())
                    .writer(new JpaItemWriterBuilder<Attachment>()
                        .entityManagerFactory(targetEntityManagerFactory).build())
                    .build())
            .build();
    }

}
