package ie.Zinkworks.demo.services;

import ie.Zinkworks.demo.dtos.AmountWithDrawRequest;
import ie.Zinkworks.demo.dtos.ResponseDto;

public interface AccountService {
	
	
	ResponseDto withdraw(AmountWithDrawRequest request);

}

