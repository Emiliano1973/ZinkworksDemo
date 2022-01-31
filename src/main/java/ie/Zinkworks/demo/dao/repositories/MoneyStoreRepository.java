package ie.Zinkworks.demo.dao.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ie.Zinkworks.demo.entities.MoneyStore;

public interface MoneyStoreRepository extends JpaRepository<MoneyStore,  Long> {

  @Modifying
  @Query("update MoneyStore m set m.quantity=:quantity where m.money=:money")
  void updateBankNotesQuantity(@Param("money") Integer money, @Param("quantity") Integer quantity); 


  List<MoneyStore> findByQuantityGreaterThanOrderByMoneyDesc(Integer quantity);
}
