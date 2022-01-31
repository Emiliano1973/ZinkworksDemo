package ie.Zinkworks.demo.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ie.Zinkworks.demo.dao.MoneyStoreDao;
import ie.Zinkworks.demo.dao.repositories.MoneyStoreRepository;
import ie.Zinkworks.demo.dtos.MoneyStoreDto;
import ie.Zinkworks.demo.entities.MoneyStore;
import ie.Zinkworks.demo.services.AtmMachineService;

@Service
public class AtmMachineServiceImpl implements AtmMachineService {

	private final MoneyStoreDao moneyStoreDao;
	private final MoneyStoreRepository moneyStoreRepository;
	
	public AtmMachineServiceImpl(MoneyStoreDao moneyStoreDao, MoneyStoreRepository moneyStoreRepository) {
		this.moneyStoreDao = moneyStoreDao;
		this.moneyStoreRepository = moneyStoreRepository;
	}

	@Override
	@Transactional(Transactional.TxType.SUPPORTS)
	public Integer getTotalAmmount() {
		return this.moneyStoreDao.getTotalAmountInAtm();
	}

	@Override
	@Transactional(Transactional.TxType.SUPPORTS)
	public List<MoneyStoreDto> getBankNotesAvailable() {
		List<MoneyStore> store=this.moneyStoreRepository.findByQuantityGreaterThanOrderByMoneyDesc(Integer.valueOf(0));
		return store.stream().map(m->new MoneyStoreDto(m.getMoney(), m.getQuantity())).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public void update(Integer banknotes, Integer quantity) {
		this.moneyStoreRepository.updateBankNotesQuantity(banknotes, quantity);
		
	}
}
