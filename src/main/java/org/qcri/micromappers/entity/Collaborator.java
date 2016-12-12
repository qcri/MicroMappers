package org.qcri.micromappers.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Kushal
 *
 */
@Entity
@Table(name="collaborator", uniqueConstraints = @UniqueConstraint(name="collection_collaborator_unique_key", columnNames={"collection_id", "account_id"}))
public class Collaborator extends ExtendedBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3250952991577738698L;

	@ManyToOne
    @JoinColumn(name = "collection_id", nullable=false)
	private Collection collection;
	
	@ManyToOne
    @JoinColumn(name = "account_id", nullable=false)
	private Account account;

	/**
	 * @return the collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}
}
