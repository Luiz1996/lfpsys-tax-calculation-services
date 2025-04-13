package com.lfpsys.lfpsys_tax_calculation_services.kafka.consumers;

import static com.lfpsys.lfpsys_tax_calculation_services.kafka.KafkaConfig.TOPIC_NAME;
import static com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadProcessStatus.COMPLETED;
import static java.lang.String.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadProcessType;
import com.lfpsys.lfpsys_tax_calculation_services.nfe_upload.NfeUploadStatusDto;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class StocksConsumer {

  private static final String REDIS_KEY_PREFIX = "LFPSys:NFe_Upload:%s";

  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  public StocksConsumer(final RedisTemplate<String, String> redisTemplate, final ObjectMapper objectMapper) {
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = "group_id")
  public void consumeMessage(ConsumerRecord<String, String> consumerRecord)
      throws JsonProcessingException, InterruptedException {
    Thread.sleep(5000);
    final var redisKey = format(REDIS_KEY_PREFIX, UUID.fromString(consumerRecord.key()));
    final var status = objectMapper.readValue(redisTemplate.opsForValue().get(redisKey), NfeUploadStatusDto.class);

    status
        .getProcesses()
        .forEach(nfeUploadProcess -> {
          if (NfeUploadProcessType.UPDATE_TAX_CALCULATION.equals(nfeUploadProcess.getProcess())) {
            nfeUploadProcess.setStatus(COMPLETED);
          }
        });

    redisTemplate.opsForValue().set(redisKey, objectMapper.writeValueAsString(status));
  }
}
