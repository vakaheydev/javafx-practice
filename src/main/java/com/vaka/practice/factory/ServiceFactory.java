package com.vaka.practice.factory;

import com.vaka.practice.dao.JdbcEntityDao;
import com.vaka.practice.service.EntityService;
import com.vaka.practice.service.SimpleEntityService;
import com.vaka.practice.service.SimpleValidationService;

public class ServiceFactory {
    private static final EntityService ENTITY_SERVICE_INSTANCE = new SimpleEntityService(new JdbcEntityDao(), new SimpleValidationService());
    public static EntityService getEntityService() {
        return ENTITY_SERVICE_INSTANCE;
    }
}
