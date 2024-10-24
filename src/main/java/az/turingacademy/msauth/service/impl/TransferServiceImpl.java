package az.turingacademy.msauth.service.impl;

import az.turingacademy.msauth.dao.repository.TransferRepository;
import az.turingacademy.msauth.mapper.TransferMapper;
import az.turingacademy.msauth.model.dto.TransferDto;
import az.turingacademy.msauth.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String makeTransfer(String idempotencyKey, TransferDto transferDto) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(idempotencyKey)))
            return "This transaction has already been processed. Transaction ID: " + redisTemplate.opsForValue().get(idempotencyKey);

        String transactionId = "TXN" + System.currentTimeMillis();
        redisTemplate.opsForValue().set
                (
                        idempotencyKey,
                        transactionId,
                        2, TimeUnit.MINUTES
                );

        transferRepository.save(transferMapper.toEntity(transferDto));

        return "Payment successfully. Transaction ID: " + transactionId;
    }

}
