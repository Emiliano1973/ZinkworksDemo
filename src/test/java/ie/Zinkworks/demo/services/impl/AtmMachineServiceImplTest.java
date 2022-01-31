package ie.Zinkworks.demo.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ie.Zinkworks.demo.dao.MoneyStoreDao;
import ie.Zinkworks.demo.dao.repositories.MoneyStoreRepository;
import ie.Zinkworks.demo.dtos.MoneyStoreDto;
import ie.Zinkworks.demo.entities.MoneyStore;

@ExtendWith(MockitoExtension.class)
public class AtmMachineServiceImplTest {

	private static final Integer MONEY_TOTAL=Integer.valueOf(1500);
	
	@Mock
	private MoneyStoreDao moneyStoreDao;
	
	@Mock
	private MoneyStoreRepository moneyStoreRepository;
	
	@Mock
	private MoneyStore moneyStore;
	
	
	@InjectMocks
	private AtmMachineServiceImpl atmMachineService;
	
	
	@Test
	public void whenGetTotalAmmountIsCalledThenAtmAmountTotalShouldBeShowed() throws Exception{
		when(moneyStoreDao.getTotalAmountInAtm()).thenReturn(MONEY_TOTAL);
		
		Integer moneyTot=this.atmMachineService.getTotalAmmount();
		
		assertEquals(MONEY_TOTAL, moneyTot);
	}
	
	@Test
	public void whenGetBankNotesAvailablethenShouldReturnBanknotesAvailable() throws Exception{
	  when(moneyStore.getMoney()).thenReturn(Integer.valueOf(50));
	  when(moneyStore.getQuantity()).thenReturn(Integer.valueOf(10));
	  when(moneyStoreRepository.findByQuantityGreaterThanOrderByMoneyDesc(any(Integer.class))).thenReturn(Arrays.asList(moneyStore));
	  
	  List<MoneyStoreDto> moneyStoreDtos=this.atmMachineService.getBankNotesAvailable();
	  
	  assertNotNull(moneyStoreDtos);
	  assertFalse(moneyStoreDtos.isEmpty());
	  assertEquals(Integer.valueOf(50), moneyStoreDtos.get(0).getMoney());
	  assertEquals(Integer.valueOf(10), moneyStoreDtos.get(0).getQuantity());
	}
	
	
	@Test
	public void whenUpdateIsCalledMoneyStoryShouldBeupdatedWithNewquantity() throws Exception{
		
		this.atmMachineService.update(Integer.valueOf(50), Integer.valueOf(5));
		
		verify(moneyStoreRepository).updateBankNotesQuantity(any(Integer.class), any(Integer.class));
	}
}

