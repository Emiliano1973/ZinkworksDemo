package ie.Zinkworks.demo.services;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ie.Zinkworks.demo.dao.repositories.AccountRepository;
import ie.Zinkworks.demo.dao.repositories.MoneyStoreRepository;
import ie.Zinkworks.demo.entities.Account;
import ie.Zinkworks.demo.entities.MoneyStore;
/**
 * Initialize the tables , adding the inititial records
 * @author emili
 *
 */
@Service
public class ApplicationInit {

	 private final MoneyStoreRepository moneyStoreRepository;
	 private final AccountRepository accountRepository;

	public ApplicationInit(MoneyStoreRepository moneyStoreRepository, AccountRepository accountRepository) {
		this.moneyStoreRepository = moneyStoreRepository;
		this.accountRepository=accountRepository;
	}
	 
	/*
	 * It's coalled after object istantiation
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	@PostConstruct
	public void init() {
		//add money to stores
		this.moneyStoreRepository.save(new MoneyStore(50, 10));
		this.moneyStoreRepository.save(new MoneyStore(20, 30));
		this.moneyStoreRepository.save(new MoneyStore(10, 30));
		this.moneyStoreRepository.save(new MoneyStore(5, 20));
		//add the accounts
		this.accountRepository.save(new Account("123456789", "1234", 800, 200));
		this.accountRepository.save(new Account("987654321", "4321", 1230, 150));
	}
	
}
