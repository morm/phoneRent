package com.morm.phone.rent.manager.repository;

import com.morm.phone.rent.manager.domain.PhoneRent;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRentRepository extends JpaRepository<PhoneRent, Integer> {

  @Query(
      "select new com.morm.phone.rent.manager.domain.PhoneRent(p, e, pe.bookedAt, pe.releasedAt) from Phone p "
          + "left join PhoneRent pe on p.id=pe.phone.id and pe.releasedAt is null "
          + "left join Employee e on e.id=pe.employee.id")
  List<PhoneRent> findAllWithCurrentState();

  @Query("select pe from PhoneRent pe "
      + "left join pe.employee e "
      + "where pe.phone.id = :phoneId and e.username=:username and pe.releasedAt is null")
  Optional<PhoneRent> findRentedPhoneByUser(String username, Integer phoneId);
}
