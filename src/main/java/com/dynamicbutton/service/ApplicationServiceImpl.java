package com.dynamicbutton.service;

import com.dynamicbutton.entity.ApplicationEntity;
import com.dynamicbutton.entity.ApplicationMenuEntity;
import com.dynamicbutton.repo.ApplicationMenuRepo;
import com.dynamicbutton.repo.ApplicationRepo;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
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
        Map<String,ApplicationMenuEntity> applicationMenuMap = new HashMap<>();
        Map<String,List<Map<String,Object>>> applicationMenuMapp = new HashMap<>();
        if(applicationEntity != null && applicationEntity.size()>0){
            for(ApplicationMenuEntity applicationMenuEntity : applicationEntity){
                List<Map<String,Object>> applicationmenulist = new LinkedList<>();
                Map<String,Object> entityMap = new HashMap<>();
                String applicationPath = applicationMenuEntity.getPath();
                Long id = applicationMenuEntity.getId();
                String actionurl = applicationMenuEntity.getActionurl();
                Long applicationid = applicationMenuEntity.getApplicationid();
                Long mainmenuid = applicationMenuEntity.getMainmenuid();
                String label = applicationMenuEntity.getLabel();

                entityMap.put("path",applicationPath);
                entityMap.put("id",id);
                entityMap.put("actionurl",actionurl);
                entityMap.put("applicationid",applicationId);
                entityMap.put("label",label);

                if(!applicationMenuMapp.containsKey(applicationPath)){
                    applicationmenulist.add(entityMap);
                    applicationMenuMapp.put(applicationPath,applicationmenulist);
                }else{
                    applicationmenulist =applicationMenuMapp.get(applicationPath);
                    applicationmenulist.add(entityMap);
                    applicationMenuMapp.put(applicationPath,applicationmenulist);
                }

            }

        }

        result.put("applicationmenu",applicationMenuMapp);
        return result;
    }
}
