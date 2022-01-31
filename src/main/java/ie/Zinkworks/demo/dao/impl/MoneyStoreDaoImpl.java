package ie.Zinkworks.demo.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import ie.Zinkworks.demo.dao.MoneyStoreDao;
import ie.Zinkworks.demo.entities.MoneyStore;
import ie.Zinkworks.demo.entities.MoneyStore_;

@Repository
public class MoneyStoreDaoImpl implements MoneyStoreDao {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Return the Amount of bancknotes contained in ATM machine
	 */
	@Override
	public Integer getTotalAmountInAtm() {
		CriteriaBuilder cb=this.em.getCriteriaBuilder();
		CriteriaQuery<Integer> cq=cb.createQuery(Integer.class);
		Root<MoneyStore> root=cq.from(MoneyStore.class);
		cq.select(cb.sum(cb.prod(root.get(MoneyStore_.MONEY), root.get(MoneyStore_.QUANTITY))))
		.where( cb.greaterThan(root.get(MoneyStore_.QUANTITY), 0));
		return  this.em.createQuery(cq).getResultStream().findFirst().orElse(Integer.valueOf(0));
	}	
}
