package ie.Zinkworks.demo.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class AccountDto implements Serializable {

	private  String accountNumber;
	
	private  String pin;
	
	private  Integer openingBalance;
	
	private Integer overdraft;
}
