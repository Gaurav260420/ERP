package com.dynamicbutton.repo;

import com.dynamicbutton.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepo extends JpaRepository<ApplicationEntity,Long> {
}
