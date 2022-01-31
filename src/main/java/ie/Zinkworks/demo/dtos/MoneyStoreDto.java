package ie.Zinkworks.demo.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class MoneyStoreDto implements Serializable {
	
	private Integer money;
	private Integer quantity;
	
	
}
