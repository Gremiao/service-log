package br.com.jatai.service_log.service;

import br.com.jatai.consumer.GsonDeserializer;
import br.com.jatai.consumer.KafkaService;
import br.com.jatai.model.Message;
import br.com.jatai.service_log.model.Log;
import br.com.jatai.service_log.repository.LogRepository;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@Service
public class ServiceLog {

    @Autowired
    private LogRepository logRepository;

    @PostConstruct
    public void listenTopic(){
        var logService = new ServiceLog();
        try (var service = new KafkaService<>(
                ServiceLog.class.getSimpleName(),
                Pattern.compile("JATAI_LOG"),
                this::parse,
                Map.of(
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName(),
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()
                ))) {
            service.run();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse(ConsumerRecord<String, Message<String>> record) {
        Message<String> message = record.value();
        System.out.println("--------------------LOG---------------------");
        System.out.println("Topic: " + record.topic());
        System.out.println("Key: " + record.key());
        System.out.println("Correlation ID: " + message.getId());
        System.out.println("Payload: " + message.getPayload());
        System.out.println("Comments: " + message.getComments());
        System.out.println("DateTime: " + message.getDateTime());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());
        System.out.println("--------------------------------------------");

        logRepository.save(new Log(
                message.getDateTime(),
                record.key(),
                String.valueOf(record.partition()),
                record.topic(),
                String.valueOf(record.offset()),
                message.getComments(),
                message.getPayload()
        ));
    }
}
