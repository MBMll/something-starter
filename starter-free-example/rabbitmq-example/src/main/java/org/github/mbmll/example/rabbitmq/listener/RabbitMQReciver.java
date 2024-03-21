package org.github.mbmll.example.rabbitmq.listener;

import org.github.mbmll.example.rabbitmq.configuration.RabbitmqConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReciver {

    @RabbitListener(queues = RabbitmqConfiguration.FANOUT_QUEUE1)
    public void reciveLogAll(String msg) throws Exception {
        System.out.println("log.all:" + msg);
    }
}
