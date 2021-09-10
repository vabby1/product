package com.pluralsight.orderfulfillment.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo  extends JpaRepository<Student, Long> {

}
