package com.teamgold.goldenharvest.common.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerHelper {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String key, Object payload, Consumer<Throwable> onFailure) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        // 공통 에러 로그는 여기서 찍고
                        log.error("Kafka send failed topic: {}", topic, ex);
                        // 각자 정의한 특화 로직 실행
                        onFailure.accept(ex);
                    }
                });
    }
}
