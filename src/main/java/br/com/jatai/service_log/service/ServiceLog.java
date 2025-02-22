package br.com.jatai.service_log.service;

import br.com.jatai.consumer.KafkaService;
import br.com.jatai.model.Message;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@Service
public class ServiceLog {

    @PostConstruct
    public void listenTopic(){
        var logService = new ServiceLog();
        try (var service = new KafkaService<>(
                ServiceLog.class.getSimpleName(),
                Pattern.compile("JATAI_LOG"),
                logService::parse,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
            service.run();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("--------------------LOG---------------------");
        System.out.println(record.topic());
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        System.out.println("--------------------------------------------");
    }
}
