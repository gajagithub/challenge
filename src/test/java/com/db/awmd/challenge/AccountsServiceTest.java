package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AmountTransferVO;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidTransferException;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  
  //Test case for transferAmount Service 
  @Test
  public void transferAmountTest() throws Exception {
	  String uniqueId = "12345";
	  String uniqueId2 = "123";
	  BigDecimal bal1=new BigDecimal(10000);
	  BigDecimal bal2=new BigDecimal(5000);
	    Account account = new Account(uniqueId);
	    account.setBalance(bal1);
	    this.accountsService.createAccount(account);
	    Account account2 = new Account(uniqueId2);
	    account2.setBalance(bal2);
	    this.accountsService.createAccount(account2);
    BigDecimal b1=new BigDecimal(100);
    AmountTransferVO amountTransferVO = new AmountTransferVO("12345","123",b1);
   
    try {
    	 this.accountsService.amountTransfer(amountTransferVO);
    } catch (InvalidTransferException| DuplicateAccountIdException   ex) {
      assertThat(ex.getMessage()).isEqualTo("Invalid account details");
    }

  }
}
