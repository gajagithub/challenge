package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AmountTransferVO {

  @NotNull
  @NotEmpty
  private final String accountFromId;
  private final String accountToId;
  @NotNull
  @Min(value = 0, message = "Transfer amount must be positive.")
  private BigDecimal amount;
public BigDecimal getAmount() {
	return amount;
}
public void setAmount(BigDecimal amount) {
	this.amount = amount;
}
public String getAccountFromId() {
	return accountFromId;
}
public String getAccountToId() {
	return accountToId;
}
@JsonCreator
public AmountTransferVO(@JsonProperty("accountFromId") String accountFromId,@JsonProperty("accounToId") String accountToId,
	    @JsonProperty("amount") BigDecimal amount) {
	this.accountFromId = accountFromId;
	this.accountToId = accountToId;
	this.amount = amount;
}

  
  
}
