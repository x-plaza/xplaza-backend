package com.backend.xplaza.service;

import com.backend.xplaza.model.PlatformInfo;
import com.backend.xplaza.repository.PlatformInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformInfoService {
    @Autowired
    private PlatformInfoRepository platformInfoRepo;

    public PlatformInfo listPlatform (){
        return platformInfoRepo.findAll().get(0); // for single row
    }
}
