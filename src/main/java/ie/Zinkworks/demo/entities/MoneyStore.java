package ie.Zinkworks.demo.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="MONEY_STORE")
@Setter @Getter @NoArgsConstructor
public class MoneyStore implements Serializable {

	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="ID")
	private long id;
	
	@Column(name="MONEY", nullable=false, unique=true)
	private Integer money;
	
	@Column(name="QUANTITY", nullable=false)
	private Integer quantity;

	public MoneyStore(Integer money, Integer quantity) {
		super();
		this.money = money;
		this.quantity = quantity;
	}
	
	
}
