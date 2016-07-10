package org.opencloudengine.garuda.proxy;

import org.opencloudengine.garuda.web.iam.Iam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ProxyService {

    void doProxy(ProxyRequest proxyRequest) throws Exception;
}
