package ie.Zinkworks.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.Zinkworks.demo.dtos.AmountWithDrawRequest;
import ie.Zinkworks.demo.dtos.ResponseDto;
import ie.Zinkworks.demo.services.AccountService;

@RestController
@RequestMapping("/atm")
public class AtmMachineController {
	
	private final AccountService accountService;
	
	public AtmMachineController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> withDraw(@RequestBody AmountWithDrawRequest request){
		ResponseDto response=this.accountService.withdraw(request);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
	}
}
