package ie.Zinkworks.demo.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ACCOUNT")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor 
public class Account implements Serializable {

	@Id
	@Column(name="ACCOUNT_NUMBER")
	private String accountId;
	
	@Column(name="PIN")
	private String pin;
	
	@Column(name="OPENING_BALANCE")
	private Integer openingBalance;
	
	@Column(name="OVER_DRAFT")
	private Integer overdraft;
	
}
