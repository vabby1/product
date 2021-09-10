package com.pluralsight.orderfulfillment.customer;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "Student")
@SequenceGenerator(name = "Student_id_seq", sequenceName = "Student_id_seq")
public class Student  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String toString() {
		return "Student [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

	private long id;
	   private String firstName;
	   private String lastName;
	   private String email;
	   
	   public Student() {

	   }

	   public Student(long id, String firstName, String lastName, String email) 
	   {
	      this.id = id;
	      this.firstName = firstName;
	      this.lastName = lastName;
	      this.email = email;
	   }
	   /**
	    * @return the id
	    */
	   @Id
	   @Column(name = "id")
	   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Student_id_seq")
	   public long getId() {
	      return id;
	   }

	   /**
	    * @param id
	    *           the id to set
	    */
	   public void setId(long id) {
	      this.id = id;
	   }

	   /**
	    * @return the firstName
	    */
	   @Column(name = "firstName", nullable = false)
	   public String getFirstName() {
	      return firstName;
	   }

	   /**
	    * @param firstName
	    *           the firstName to set
	    */
	   public void setFirstName(String firstName) {
	      this.firstName = firstName;
	   }

	   /**
	    * @return the lastName
	    */
	   @Column(name = "lastName", nullable = false)
	   public String getLastName() {
	      return lastName;
	   }

	   /**
	    * @param lastName
	    *           the lastName to set
	    */
	   public void setLastName(String lastName) {
	      this.lastName = lastName;
	   }

	   /**
	    * @return the email
	    */
	   @Column(name = "email", nullable = false)
	   public String getEmail() {
	      return email;
	   }

	   /**
	    * @param email
	    *           the email to set
	    */
	   public void setEmail(String email) {
	      this.email = email;
	   }


}
