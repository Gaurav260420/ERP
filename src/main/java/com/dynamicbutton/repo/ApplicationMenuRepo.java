package com.dynamicbutton.repo;

import com.dynamicbutton.entity.ApplicationMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationMenuRepo extends JpaRepository<ApplicationMenuEntity,Long> {
    List<ApplicationMenuEntity> findAllByApplicationid(Long applicaionid);
}
