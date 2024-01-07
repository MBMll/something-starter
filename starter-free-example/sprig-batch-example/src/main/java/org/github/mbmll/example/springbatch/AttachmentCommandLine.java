package org.github.mbmll.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class AttachmentCommandLine implements CommandLineRunner {
    @Autowired
    private Job job;
    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(job, new JobParameters());
    }
}
