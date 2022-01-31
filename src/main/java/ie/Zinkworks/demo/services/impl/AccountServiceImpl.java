package ie.Zinkworks.demo.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ie.Zinkworks.demo.dao.repositories.AccountRepository;
import ie.Zinkworks.demo.dtos.AccountDto;
import ie.Zinkworks.demo.dtos.AmountWithDrawRequest;
import ie.Zinkworks.demo.dtos.MoneyStoreDto;
import ie.Zinkworks.demo.dtos.ResponseDto;
import ie.Zinkworks.demo.dtos.ResponseEnum;
import ie.Zinkworks.demo.entities.Account;
import ie.Zinkworks.demo.services.AccountService;
import ie.Zinkworks.demo.services.AtmMachineService;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final AtmMachineService atmMachineService;

	public AccountServiceImpl(AccountRepository accountRepository, AtmMachineService atmMachineService) {
		this.accountRepository = accountRepository;
		this.atmMachineService = atmMachineService;
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public ResponseDto withdraw(AmountWithDrawRequest request) {
		Integer totalAtmAmmount = this.atmMachineService.getTotalAmmount();
		if (totalAtmAmmount <= 0) {
			return new ResponseDto(ResponseEnum.FAIL.name(), "Out of service: ATM machine is empty",
					HttpStatus.FORBIDDEN.value());
		}
		if (request.getPin() == null || request.getPin().trim().equals("")) {
			return new ResponseDto(ResponseEnum.FAIL.name(), "Pin not typed, please digit your pin number",
					HttpStatus.BAD_REQUEST.value());
		}
		Optional<Account> accountOpt = this.accountRepository.findByPin(request.getPin().trim());
		if (!accountOpt.isPresent()) {
			return new ResponseDto(ResponseEnum.FAIL.name(), "Account not found", HttpStatus.NOT_FOUND.value());
		}
		if (totalAtmAmmount < request.getAmountRequested()) {
			return new ResponseDto(ResponseEnum.FAIL.name(),
					"Amount content in ATM machine is insufficient, withdraw an amount less than " + totalAtmAmmount,
					HttpStatus.BAD_REQUEST.value());
		}
		AccountDto account = accountOpt
				.map(ac -> new AccountDto(ac.getAccountId(), ac.getPin(), ac.getOpeningBalance(), ac.getOverdraft()))
				.get();
		if (account.getOpeningBalance() < request.getAmountRequested()) {
			return new ResponseDto(ResponseEnum.FAIL.name(),
					"Amount requested is greater than your balance, withdraw an amount less than this",
					HttpStatus.BAD_REQUEST.value());
		}

		List<MoneyStoreDto> moneyStores = this.atmMachineService.getBankNotesAvailable();
		return withDrawFromAtom(request.getAmountRequested(), account, moneyStores);
	}

	private ResponseDto withDrawFromAtom(Integer amountRequested, AccountDto account, List<MoneyStoreDto> moneyStores) {
		Map<String, Integer> totalAmount = new HashMap<>(1);
		totalAmount.put("SUM", Integer.valueOf(0));
		Map<Integer, Integer> bankNotesWithDrawn = new HashMap<>();
		for (MoneyStoreDto moneyStore : moneyStores) {
			Integer money = moneyStore.getMoney();
			Integer quantity = moneyStore.getQuantity();
			withDrawBankNotes(money, quantity, amountRequested, bankNotesWithDrawn, totalAmount);
			if (totalAmount.get("SUM").intValue() == amountRequested.intValue()) {
				break;
			}
		}
		if (totalAmount.get("SUM").intValue() < amountRequested.intValue()) {
			return new ResponseDto(ResponseEnum.FAIL.name(), "Banknotes denominations not available for the ammount",
					HttpStatus.NOT_FOUND.value());
		}
		Integer newBalance = account.getOpeningBalance() - amountRequested;
		this.accountRepository.updateBalance(account.getAccountNumber(), newBalance);
		account.setOpeningBalance(newBalance);
		for (MoneyStoreDto moneyStore : moneyStores) {
			if (bankNotesWithDrawn.containsKey(moneyStore.getMoney())) {
				this.atmMachineService.update(moneyStore.getMoney(),
						moneyStore.getQuantity() - bankNotesWithDrawn.get(moneyStore.getMoney()));
			}
		}
				return new ResponseDto(ResponseEnum.SUCCESS.name(), "Ammount paid :  " + amountRequested,
				HttpStatus.OK.value(), account);
	}

	private void withDrawBankNotes(Integer banknote, int quantity, int amountRequested,
			Map<Integer, Integer> bankNotesWithDrawn, Map<String, Integer> totalAmount) {
		bankNotesWithDrawn.put(banknote, 0);
		int ammountWthDrawFromAtm = totalAmount.get("SUM").intValue();
		for (int i = 0; i < quantity; i++) {
			ammountWthDrawFromAtm += banknote.intValue();
			if (ammountWthDrawFromAtm <= amountRequested) {
				bankNotesWithDrawn.put(banknote, bankNotesWithDrawn.get(banknote).intValue() + 1);
			} else {
				ammountWthDrawFromAtm -= banknote;
				break;
			}
		}
		totalAmount.put("SUM", Integer.valueOf(ammountWthDrawFromAtm));

	}
}
