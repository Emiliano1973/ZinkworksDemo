package ie.Zinkworks.demo.services.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import ie.Zinkworks.demo.dao.repositories.AccountRepository;
import ie.Zinkworks.demo.dtos.AmountWithDrawRequest;
import ie.Zinkworks.demo.dtos.MoneyStoreDto;
import ie.Zinkworks.demo.dtos.ResponseDto;
import ie.Zinkworks.demo.dtos.ResponseEnum;
import ie.Zinkworks.demo.entities.Account;
import ie.Zinkworks.demo.services.AtmMachineService;
import ie.Zinkworks.demo.services.impl.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

	private static final String ACCOUNT_NUMBER="111111111111111111111";
	private static final Integer OPENING_BALANCE=Integer.valueOf( 800);
	private static final Integer OVER_DRAFT=Integer.valueOf(200);
	private static final Integer ATM_TOTAL_AMOUNT = Integer.valueOf(1500);
	private static final Integer ATM_TOTAL_AMOUNT_EMPTY = Integer.valueOf(0);
	private static final String PIN_NUMBER = "PIN";
	private static final Integer AMOUNT_WITHDRAW = Integer.valueOf(300);
	private static final Integer AMOUNT_WITHDRAW_2 = Integer.valueOf(10);
	private static final Integer AMOUNT_WITHDRAW_BALANCE_HIGHER = Integer.valueOf(900);
	private static final Integer AMOUNT_WITHDRAW_HIGHER = Integer.valueOf(3000);
	

	private static final String ATM_EMPTY_MESSAGE="Out of service: ATM machine is empty";
	private static final String PIN_NULL_EMPTY_MESSAGE="Pin not typed, please digit your pin number";
	private static final String ACCOUNT_NOT_FOUND_MESSAGE="Account not found";
	private static final String AMOUNT_REQUESTED_HIGHER_THAN_AVAIL_ATM_MESSAGE="Amount content in ATM machine is insufficient, withdraw an amount less than " + ATM_TOTAL_AMOUNT.intValue();
	private static final String AMOUNT_REQUESTED_HIGHER_THAN_AVAIL_IN_ACCOUNT_MESSAGE="Amount requested is greater than your balance, withdraw an amount less than this";
	private static final String AMOUNT_PAID_SUCCESS_MESSAGE="Ammount paid :  " + AMOUNT_WITHDRAW;
	private static final String BANKNOTES_INSUFICENT_MESSAGE="Banknotes denominations not available for the ammount";
	
	@Mock
	private AmountWithDrawRequest request;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AtmMachineService atmMachineService;

	@Mock
	private Account account;
	
	@InjectMocks
	private AccountServiceImpl accountService;

	
	
	@Test
	public void whenAtmAmountIsZeroTheWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT_EMPTY);
		//when(request.getPin()).thenReturn(PIN_NUMBER);
		//when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW);

		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
		assertEquals(ATM_EMPTY_MESSAGE, response.getMessage());
	}
	
	
	
	@Test
	public void whenPinNumberIsNullThenWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(request.getPin()).thenReturn(null);
		//when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW);

		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
		assertEquals(PIN_NULL_EMPTY_MESSAGE, response.getMessage());
	}
	
	
	@Test
	public void whenPinNumberIsEmptyThenWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(request.getPin()).thenReturn("");
		//when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW);

		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
		assertEquals(PIN_NULL_EMPTY_MESSAGE, response.getMessage());
	}
	
	
	@Test
	public void whenAccountINotFoundThenWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(accountRepository.findByPin(PIN_NUMBER)).thenReturn(Optional.empty());
		when(request.getPin()).thenReturn(PIN_NUMBER);
		
		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
		assertEquals(ACCOUNT_NOT_FOUND_MESSAGE, response.getMessage());
	}

	@Test
	public void whenAmmountRequestedIsHigherThanAmountInAtmThenWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(accountRepository.findByPin(PIN_NUMBER)).thenReturn(Optional.of(account));
		when(request.getPin()).thenReturn(PIN_NUMBER);
		when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW_HIGHER);
	
		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
		assertEquals(AMOUNT_REQUESTED_HIGHER_THAN_AVAIL_ATM_MESSAGE, response.getMessage());
	}
	
	@Test
	public void whenAmmountRequestedIsHigherThanAmountInAccountBalanceThenWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(accountRepository.findByPin(PIN_NUMBER)).thenReturn(Optional.of(account));
		when(request.getPin()).thenReturn(PIN_NUMBER);
		when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW_BALANCE_HIGHER);
		initAccount();
		
		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
		assertEquals(AMOUNT_REQUESTED_HIGHER_THAN_AVAIL_IN_ACCOUNT_MESSAGE, response.getMessage());
	}
	
	@Test
	public void whenAmmountRequestedIsValidateThenWithDrawShouldSuccess() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(accountRepository.findByPin(PIN_NUMBER)).thenReturn(Optional.of(account));
		when(request.getPin()).thenReturn(PIN_NUMBER);
		when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW);
		initAccount();
		when(atmMachineService.getBankNotesAvailable())
		.thenReturn(Arrays.asList(new MoneyStoreDto(Integer.valueOf(50), Integer.valueOf(10)), 
				new MoneyStoreDto(Integer.valueOf(20), Integer.valueOf(30)), new MoneyStoreDto(Integer.valueOf(10), Integer.valueOf(30)), 
				new MoneyStoreDto(Integer.valueOf(5), Integer.valueOf(20))
				));
		
		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.SUCCESS.name(), response.getResponse());
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals(AMOUNT_PAID_SUCCESS_MESSAGE, response.getMessage());
		
		verify(accountRepository).updateBalance(any(String.class), any(Integer.class));
		verify(atmMachineService, times(1)).update(any(Integer.class), any(Integer.class));
	
	}

	@Test
	public void whenBanknotesDenominationIsInsuficentThenWithDrawShouldFail() throws Exception {
		when(atmMachineService.getTotalAmmount()).thenReturn(ATM_TOTAL_AMOUNT);
		when(accountRepository.findByPin(PIN_NUMBER)).thenReturn(Optional.of(account));
		when(request.getPin()).thenReturn(PIN_NUMBER);
		when(request.getAmountRequested()).thenReturn(AMOUNT_WITHDRAW_2);
		initAccount();
		when(atmMachineService.getBankNotesAvailable())
		.thenReturn(Arrays.asList(new MoneyStoreDto(Integer.valueOf(50), Integer.valueOf(10))));
		
		ResponseDto response = this.accountService.withdraw(request);
		
		assertNotNull(response);
		assertEquals(ResponseEnum.FAIL.name(), response.getResponse());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
		assertEquals(BANKNOTES_INSUFICENT_MESSAGE, response.getMessage());
		
		verify(accountRepository, never()).updateBalance(any(String.class), any(Integer.class));
		verify(atmMachineService, never()).update(any(Integer.class), any(Integer.class));
	
	}

	private void initAccount() {
		when(account.getAccountId()).thenReturn(ACCOUNT_NUMBER);
		when(account.getPin()).thenReturn(PIN_NUMBER);
		when(account.getOpeningBalance()).thenReturn(OPENING_BALANCE);
		when(account.getOverdraft()).thenReturn(OVER_DRAFT);
		
	}
		
}
