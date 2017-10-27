package com.db.awmd.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferAmount;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.EmailNotificationService;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

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
	/**
	 *  
	 */
	@Override
	public void transfer(TransferAmount transferAmount) throws DuplicateAccountIdException {

		if (transferAmount.getAccountFromId().equals(transferAmount.getAccountToId())) {
			throw new DuplicateAccountIdException("'Form Account' and 'To Acount' are same!");
		}
		Account accountForm = getAccount(transferAmount.getAccountFromId());
		Account accountTo = getAccount(transferAmount.getAccountToId());
		if (transferAmount.getAmount() <= accountform.getBalance()) {
			BigDecimal amt = accountForm.getBalance().subtract(transferAmount.getAmount());
			BigDecimal amount = accountTo.getBalance().add(transferAmount.getAmount());
			accountForm.setBalance(amt);
			accountTo.setBalance(amount);
			String discription = " Amount : " + transferAmount.getAmount() + " transferred form account :"transferAmount.getAccountFormId() + " To :" + transferAmount.getAccountToId()
			emailNotification.notifyAboutTransfer(accountForm, discription);
			emailNotification.notifyAboutTransfer(accountTo, discription);
		} else {
			throw new RuntimeException("oops...! insufficient funds ");
		}

	}

}
