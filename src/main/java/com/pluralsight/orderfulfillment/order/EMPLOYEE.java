package com.pluralsight.orderfulfillment.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEE")
public class EMPLOYEE {

	private int id;
	private String firstname;
	private String lastname;
	
	 @Column(name = "firstname", nullable = false)
	public String getFirstname() {
		return firstname;
	}
	
	
	
	   @Id
	   @Column(name = "id")
	   @GeneratedValue(strategy = GenerationType.SEQUENCE)
	   public int getId() {
	      return id;
	   }

	   /**
	    * @param id
	    *           the id to set
	    */
	   public void setId(int id) {
	      this.id = id;
	   }

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	 @Column(name = "lastname", nullable = false)
	public String getLastname() {
		return lastname;
	}
	@Override
	public String toString() {
		return "EMPLOYEE [firstname=" + firstname + ", lastname=" + lastname + "]";
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public EMPLOYEE(String firstname, String lastname) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	
	
}
