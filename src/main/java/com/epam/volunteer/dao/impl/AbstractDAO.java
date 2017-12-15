package com.epam.volunteer.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


abstract class AbstractDAO {

    private static final Logger LOGGER = LogManager.getLogger();

    static Logger getLogger() {
        return LOGGER;
    }

}
