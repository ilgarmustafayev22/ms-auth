package az.turingacademy.msauth.service;

import az.turingacademy.msauth.model.dto.TransferDto;

import java.math.BigDecimal;

public interface TransferService {

    String makeTransfer(String idempotencyKey, TransferDto transferDto);

}
