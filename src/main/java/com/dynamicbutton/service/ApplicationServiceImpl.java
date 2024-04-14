package com.dynamicbutton.service;

import com.dynamicbutton.entity.ApplicationEntity;
import com.dynamicbutton.entity.ApplicationMenuEntity;
import com.dynamicbutton.repo.ApplicationMenuRepo;
import com.dynamicbutton.repo.ApplicationRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepo applicationRepo;

    public ApplicationServiceImpl(ApplicationRepo applicationRepo) {
        this.applicationRepo = applicationRepo;
    }
    @Autowired
    private ApplicationMenuRepo applicationMenuRepo;
    @Override
    public Map<String, Object> getApplication(Map<String, Object> map, HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        List<ApplicationEntity> applicationEntity =applicationRepo.findAll();
        result.put("application",applicationEntity);
        return result;
    }

    @Override
    public Map<String, Object> getApplicationMenu(Map<String, Object> map, HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        Long applicationId = Long.parseLong( String.valueOf(map.get("applicationid")));
        List<ApplicationMenuEntity> applicationEntity =applicationMenuRepo.findAllByApplicationid(applicationId);
        result.put("applicationmenu",applicationEntity);
        return result;
    }
}
