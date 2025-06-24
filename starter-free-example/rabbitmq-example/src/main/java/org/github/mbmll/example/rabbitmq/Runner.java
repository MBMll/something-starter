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
        rabbitTemplate.convertAndSend("direct.exchange", "k1", "Hello from RabbitMQ! k1");
        rabbitTemplate.convertAndSend("direct.exchange", "k2", "Hello from RabbitMQ! k2");
        rabbitTemplate.convertAndSend("direct.exchange", "k3", "Hello from RabbitMQ! k3");
        rabbitTemplate.convertAndSend("topic.exchange", "t1.hello", "Hello from RabbitMQ! t1.hello");
        rabbitTemplate.convertAndSend("topic.exchange", "t2.hello", "Hello from RabbitMQ! t2.hello");
        rabbitTemplate.convertAndSend("topic.exchange", "t3.hello", "Hello from RabbitMQ! t3.hello");
        rabbitTemplate.convertAndSend("topic.exchange", "t1.world", "Hello from RabbitMQ! t1.world");
        rabbitTemplate.convertAndSend("topic.exchange", "t2.world", "Hello from RabbitMQ! t2.world");
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.SECONDS);
    }

}
