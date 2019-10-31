package com.db.awmd.challenge.service;

import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

	 private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EmailNotificationService.class);
  @Override
  public void notifyAboutTransfer(Account account, String transferDescription) {
    //THIS METHOD SHOULD NOT BE CHANGED - ASSUME YOUR COLLEAGUE WILL IMPLEMENT IT
    log.info("Sending notification to owner of account-ID {}: {}", account.getAccountId(), transferDescription);
  }

}
