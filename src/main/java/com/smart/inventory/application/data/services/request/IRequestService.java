package com.smart.inventory.application.data.services.request;

import javax.servlet.http.HttpServletRequest;

public interface IRequestService {

    String getClientIp(HttpServletRequest request);
}
