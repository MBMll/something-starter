package org.github.mbmll.example.rabbitmq.listener;

import org.github.mbmll.example.rabbitmq.configuration.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReciver {

    @RabbitListener(queues = RabbitmqConfiguration.FANOUT_QUEUE1)
    public void reciveLogAll(String msg) throws Exception {
        System.out.println("log.all:" + msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "direct.queue1", durable = "true"),
            exchange = @Exchange(value = "direct.exchange", type = "direct"),
            key = "k1"
    ))
    public void reciveDirectExchange1(String msg) throws Exception {
        System.out.println("reciveDirectExchange1:" + msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "direct.queue2", durable = "true"),
            exchange = @Exchange(value = "direct.exchange", type = "direct"),
            key = "k2"
    ))
    public void reciveDirectExchange2(String msg) throws Exception {
        System.out.println("reciveDirectExchange2:" + msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "topic.queue1", durable = "true"),
            exchange = @Exchange(value = "topic.exchange", type = "topic"),
            key = "t1.#"
    ))
    public void reciveTopicExchange1(String msg) throws Exception {
        System.out.println("reciveTopicExchange1:" + msg);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "topic.queue2", durable = "true"),
            exchange = @Exchange(value = "topic.exchange", type = "topic"),
            key = "t2.#"
    ))
    public void reciveTopicExchange2(String msg) throws Exception {
        System.out.println("reciveTopicExchange2:" + msg);
    }
}
