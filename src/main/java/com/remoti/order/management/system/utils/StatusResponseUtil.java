package com.remoti.order.management.system.utils;

import com.remoti.order.management.system.enums.ServerErrorCommon;
import com.remoti.order.management.system.model.StatusDataDTO;

public interface StatusResponseUtil {

    StatusDataDTO getStatusResponse(final ServerErrorCommon commonError);
    StatusDataDTO getStatusResponse(final Throwable throwable);
    StatusDataDTO getStatusResponse(final ServerErrorCommon commonError, final Throwable throwable);

}
