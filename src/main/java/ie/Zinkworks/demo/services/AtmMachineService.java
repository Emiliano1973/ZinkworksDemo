package ie.Zinkworks.demo.services;

import java.util.List;

import ie.Zinkworks.demo.dtos.MoneyStoreDto;

public interface AtmMachineService {
	
	Integer getTotalAmmount();
	
	List<MoneyStoreDto> getBankNotesAvailable();

	void update(Integer banknotes, Integer quantity);
}
