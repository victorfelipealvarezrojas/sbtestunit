package com.testing.sbtest.repository;

import com.testing.sbtest.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    // JPQL query
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQLQuery(String firstName, String lastName);

    // JPQL query with named parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Employee findByJPQLQueryNamedParams(@Param("firstName") String firstName,@Param("lastName") String lastName);

    // Native query
    @Query(value = "SELECT * FROM employees WHERE first_name = ?1 AND last_name = ?2", nativeQuery = true)
    Employee findByNativeQuery(String firstName, String lastName);

    // Native query with named parameters
    @Query(value = "SELECT * FROM employees WHERE first_name = :firstName AND last_name = :lastName", nativeQuery = true)
    Employee findByNativeQueryNamedParams(@Param("firstName") String firstName,@Param("lastName") String lastName);
}