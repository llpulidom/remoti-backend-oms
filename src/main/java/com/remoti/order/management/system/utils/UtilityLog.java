package com.remoti.order.management.system.utils;

import com.remoti.order.management.system.model.LoggerDataDTO;
import org.slf4j.Logger;

public interface UtilityLog {

    void put(final Logger logger, final LoggerDataDTO data);
    void put(final Logger logger, final LoggerDataDTO data, final Exception exception);

}