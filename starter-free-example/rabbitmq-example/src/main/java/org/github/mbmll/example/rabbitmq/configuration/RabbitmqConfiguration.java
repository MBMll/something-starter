package org.github.mbmll.example.rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Configuration
public class RabbitmqConfiguration {
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    @Bean(name = FANOUT_EXCHANGE)
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }
    public static final String FANOUT_QUEUE1 = "fanout.queue1";
    @Bean(name = FANOUT_QUEUE1)
    public Queue fanoutQueue1() {
        return new Queue(FANOUT_QUEUE1, true, false, false);
    }
    @Bean
    public Binding bindingSimpleQueue1(@Qualifier(FANOUT_QUEUE1) Queue fanoutQueue1,
                                       @Qualifier(FANOUT_EXCHANGE) FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
}

