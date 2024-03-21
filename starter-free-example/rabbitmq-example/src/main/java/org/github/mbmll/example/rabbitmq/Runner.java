package org.github.mbmll.example.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.github.mbmll.example.rabbitmq.configuration.RabbitmqConfiguration.FANOUT_EXCHANGE;
import static org.github.mbmll.example.rabbitmq.configuration.RabbitmqConfiguration.FANOUT_QUEUE1;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public Runner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE, FANOUT_QUEUE1, "Hello from RabbitMQ!");

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.SECONDS);
    }

}
