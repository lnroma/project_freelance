package com.naumoff.rnc.dto;

import com.naumoff.rnc.validators.ValidEnum;
import com.naumoff.rnc.validators.ValidUuid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateWalletRequest {
    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAW = "WITHDRAW";

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }

    @NotNull(message = "valetId is required field")
    @ValidUuid(message = "UUID is not valid")
    private UUID valetId;

    @NotNull(message = "amount is required field")
    @DecimalMin(value = "0.01", message =  "amount must be great zero")
    private Double amount;

    @ValidEnum(message = "operationType must be DEPOSIT or WITHDRAW")
    @NotBlank(message = "operationType is required field")
    private String operationType;

    public UUID getValetId() {
        return valetId;
    }

    public void setValetId(UUID valetId) {
        this.valetId = valetId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
