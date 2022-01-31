package ie.Zinkworks.demo.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Request 
 * @author emili
 *
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AmountWithDrawRequest  implements Serializable{

	private String pin;

	private Integer amountRequested;
}
