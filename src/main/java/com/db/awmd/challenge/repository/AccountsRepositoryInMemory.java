package com.db.awmd.challenge.repository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AmountTransferVO;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidTransferException;
import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.service.ThreadPoolService;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	
	EmailNotificationService emailNotificationService;
	
  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

 
  @Autowired
  public AccountsRepositoryInMemory(EmailNotificationService emailNotificationService) {
	this.emailNotificationService = emailNotificationService;
}

@Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

 // transferAmount service 
@Override
public void amountTransfer(AmountTransferVO amountTransferVO) {
	try {
         
         Runnable runnable = () -> { 
        	 
        	 //check account number is valid or not and also amount should be greater than zero 
     		if((getAccount(amountTransferVO.getAccountFromId()).getAccountId() != null) && (getAccount(amountTransferVO.getAccountToId()).getAccountId() != null) && (amountTransferVO.getAmount().compareTo(BigDecimal.ZERO) > 0) && !(amountTransferVO.getAccountFromId().equalsIgnoreCase(amountTransferVO.getAccountToId() ))) {
    			
     			//check sufficient amount in from account or not 
    			if ((accounts.get(amountTransferVO.getAccountFromId()).getBalance().compareTo(amountTransferVO.getAmount()) == 0 )|| (accounts.get(amountTransferVO.getAccountToId()).getBalance().compareTo(amountTransferVO.getAmount())  == 1)) { 
    				MathContext mc = new MathContext(0); // 2 precision
    				accounts.get(amountTransferVO.getAccountFromId()).setBalance(accounts.get(amountTransferVO.getAccountFromId()).getBalance().subtract(amountTransferVO.getAmount(), mc));
    				//notification for the customer 
    				emailNotificationService.notifyAboutTransfer(accounts.get(amountTransferVO.getAccountFromId()), "Your account has been debited with amount"+amountTransferVO.getAmount()+".And remaining balance is "+accounts.get(amountTransferVO.getAccountFromId()).getBalance());
    				accounts.get(amountTransferVO.getAccountToId()).setBalance(accounts.get(amountTransferVO.getAccountToId()).getBalance().add(amountTransferVO.getAmount(), mc));
    	            emailNotificationService.notifyAboutTransfer(accounts.get(amountTransferVO.getAccountToId()), "Your account has been credited with amount"+amountTransferVO.getAmount()+".And remaining balance is "+accounts.get(amountTransferVO.getAccountToId()).getBalance());
    				} 
    	        
    	        else { 
    	    	           throw new InvalidTransferException(amountTransferVO.getAccountFromId() + " is lesser than " + amountTransferVO.getAmount() + "."); 
    	        } 
    	}else {
    		throw new InvalidTransferException("Invalid account details");
    	}

         };	
         ThreadPoolService.executor.execute(runnable);
   }
catch (RuntimeException  e) {
       
	 ThreadPoolService.executor.shutdown();
      
   }
}   	
    
	 

	
	
}

