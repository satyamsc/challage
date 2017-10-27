package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransferAmount {

  @NotNull
  @NotEmpty
  private final String accountFromId;
  
  @NotNull
  @NotEmpty
  private final String accountToId;

  @NotNull
  @Min(value = 0, message = "Transfer amount must be positive.")
  private BigDecimal amount;

  public TransferAmount(String accountFromId, String accountToId) {
    this.accountFromId = accountFromId;
    this.accountToId = accountToId;
    this.amount = BigDecimal.ZERO;
  }

  @JsonCreator
  public TransferAmount(@JsonProperty("accountFromId") String accountFromId,@JsonProperty("accounToId") String accountToId,
    @JsonProperty("amount") BigDecimal amount) {
    this.accountFromId = accountFromId;
    this.accountToId = accountToId;
    this.amount = amount;
  }
}
