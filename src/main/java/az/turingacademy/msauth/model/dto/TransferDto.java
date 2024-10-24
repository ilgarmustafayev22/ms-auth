package az.turingacademy.msauth.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;

}
