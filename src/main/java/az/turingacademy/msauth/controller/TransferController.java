package az.turingacademy.msauth.controller;

import az.turingacademy.msauth.model.dto.TransferDto;
import az.turingacademy.msauth.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<String> makeTransfer(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                               @RequestBody TransferDto transferDto) {
        return ResponseEntity.ok(transferService.makeTransfer(idempotencyKey, transferDto));
    }

}

