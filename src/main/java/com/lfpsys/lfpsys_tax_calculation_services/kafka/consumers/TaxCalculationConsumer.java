package com.lfpsys.lfpsys_tax_calculation_services.kafka.consumers;

import static com.lfpsys.lfpsys_tax_calculation_services.kafka.KafkaConfig.TOPIC_NAME;
import static com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadProcessStatus.COMPLETED;
import static com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadProcessType.UPDATE_FINANCIAL_CONTROLS;
import static com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadProcessType.UPDATE_TAX_CALCULATION;
import static java.lang.String.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfpsys.lfpsys_tax_calculation_services.PendingBeforeStepException;
import com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadProcessType;
import com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadStatusDto;
import java.time.Duration;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TaxCalculationConsumer {

  private static final String REDIS_KEY_PREFIX = "LFPSys:NFe_Upload:%s";

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  public TaxCalculationConsumer(final RedisTemplate<String, String> redisTemplate, final ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = "group_id")
  public void consumeMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
    final var redisKey = format(REDIS_KEY_PREFIX, UUID.fromString(consumerRecord.key()));
    final var status = objectMapper.readValue(redisTemplate.opsForValue().get(redisKey), NfeUploadStatusDto.class);

    final var beforeStepIsCompleted = status
        .getProcesses()
        .stream()
        .anyMatch(process -> UPDATE_FINANCIAL_CONTROLS.equals(process.getProcess()) && COMPLETED.equals(process.getStatus()));

    if (!beforeStepIsCompleted) {
      throw new PendingBeforeStepException();
    }

    status
        .getProcesses()
        .forEach(nfeUploadProcess -> {
          if (UPDATE_TAX_CALCULATION.equals(nfeUploadProcess.getProcess())) {
            nfeUploadProcess.setStatus(COMPLETED);
          }
        });

    redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(status), Duration.ofDays(30));
  }
}
