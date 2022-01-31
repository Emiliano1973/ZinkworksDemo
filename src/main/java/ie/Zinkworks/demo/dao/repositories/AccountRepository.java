package ie.Zinkworks.demo.dao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ie.Zinkworks.demo.entities.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

	@Modifying
	@Query("update Account ac set ac.openingBalance=:newBalance where ac.accountId=:accountId")
	void updateBalance(@Param("accountId") String accountId,@Param("newBalance") Integer newBalance);
	
	Optional<Account> findByPin(String pin);
}
