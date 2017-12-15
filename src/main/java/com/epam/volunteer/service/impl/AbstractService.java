package com.epam.volunteer.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class AbstractService {
    private static final Logger LOGGER = LogManager.getLogger();

    static Logger getLogger() {
        return LOGGER;
    }

}
