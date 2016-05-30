/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.garuda.wrapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.client.methods.HttpGet;
import org.opencloudengine.garuda.HttpFilters;
import org.opencloudengine.garuda.HttpFiltersAdapter;
import org.opencloudengine.garuda.HttpFiltersSourceAdapter;
import org.opencloudengine.garuda.HttpProxyServer;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.impl.DefaultHttpProxyServer;
import org.opencloudengine.garuda.util.JsonUtils;
import org.opencloudengine.garuda.util.ResourceUtils;
import org.opencloudengine.garuda.util.StringUtils;
import org.opencloudengine.garuda.wrapper.exception.APIServiceException;
import org.opencloudengine.garuda.wrapper.message.APIServiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper 네트워크 서비스.
 *
 * @author Seungpil, Park
 * @version 0.1
 */
public final class Wrapper implements Runnable, InitializingBean, DisposableBean, ApplicationContextAware {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(Wrapper.class);

    /**
     * Wrapper 서비스를 제공할 포트. 모든 클라이언트는 이 포트로 HTTP 요청을 처리해야 한다.
     */
    private int wrapperPort;

    /**
     * Wrapper 가 스프링과 통신하기 위한 포트.
     */
    private int springPort;

    /**
     * Wrapper 의 Url 규칙이 담겨있는 json 파일 패스.
     */
    private String policyjson;

    /**
     * Spring Framework Application Context.
     */
    private ApplicationContext applicationContext;

    private SimpleAsyncTaskExecutor taskExecutor;

    private HttpProxyServer server;

    private Map policyMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isEmpty(policyjson)){
            throw new ServiceException("Policy Json File required");
        }
        URL url = getClass().getResource(policyjson);
        File file = ResourceUtils.getFile(url);
        if(!file.exists()){
            throw new ServiceException("Policy Json File required");
        }

        if(!file.isFile()){
            throw new ServiceException("Policy Json File required");
        }
        try{
            String json = new String(Files.readAllBytes(file.toPath()));
            policyMap = JsonUtils.unmarshal(json);
        }catch (IOException ex){
            throw new ServiceException("Policy Json File Parsing failed.");
        }
    }

    /**
     * Wrapper 를 시작한다.
     */
    public void start() {

        this.taskExecutor.execute(this, 3000);
    }

    /**
     * Wrapper 서비스를 제공할 포트를 설정한다.
     *
     * @param wrapperPort Wrapper 서비스를 제공할 포트
     */
    public void setWrapperPort(int wrapperPort) {
        this.wrapperPort = wrapperPort;
    }

    /**
     * Wrapper 가 스프링과 통신할 포트를 설정한다.
     *
     * @param springPort 스프링 서비스를 제공할 포트
     */
    public void setSpringPort(int springPort) {
        this.springPort = springPort;
    }

    /**
     * Spring Framework Application Context를 설정한다.
     *
     * @param applicationContext Spring Framework Application Context
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Wrapper 서비스를 제공할 설정 파일 패스.
     *
     * @param policyjson Router 서비스를 제공할 설정 파일 패스.
     */
    public void setPolicyjson(String policyjson) {
        this.policyjson = policyjson;
    }

    /**
     * Spring Framework IoC Container가 종료될 때 HttpProxyServer 를 종료한다.
     *
     * @throws Exception 종료 에러 발생시
     */
    @Override
    public void destroy() throws Exception {
        logger.info("Stopping Router Network Service");

        if (server != null) {
            server.stop();
        }
        logger.info("Stopped Router Network Service");
    }

    @Override
    public void run() {
        try {
            server = DefaultHttpProxyServer.bootstrap()
                    .withPort(wrapperPort)
                    .withFiltersSource(new HttpFiltersSourceAdapter() {
                        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                            return new HttpFiltersAdapter(originalRequest) {

                                //워크플로우 아이디 변수 설정

                                @Override
                                public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                    if (httpObject instanceof HttpRequest) {
                                        HttpRequest httpRequest = (HttpRequest) httpObject;
                                        String uriRemote = "http://52.79.164.208:8080" + httpRequest.getUri();
                                        httpRequest.setUri(uriRemote);
                                    }
                                    return null;

                                    //1. 워크플로우 실행
                                    //2. 워크플로우가 리스폰스에 도달햇을 때 리스폰스를 리턴.
                                    //   이때 ping pong 호출로 프록시를 통과시킴
                                    //3. 워크플로우가 프록시에 도달했을 때 프록시 리턴.
                                    //4. 이때 프록시 uri, method, 헤더 변경

                                    //5. 변수가 현재 워크플로우 진행도를 저장.
                                }

                                @Override
                                public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                    //1. 프록시일경우, 리스폰스의 스테이터스를 워크플로우 조건판별에 태움.
                                    //2. 워크플로우 진행.

                                    //3. API 리스폰스일 경우, 리스폰스를 API 리스폰스로 교체.
                                    //4. 워크플로우 진행.
                                    return httpObject;
                                }


                                @Override
                                public void proxyToServerConnectionFailed() {
                                    //1. 프록시일경우, 리스폰스의 스테이터스를 워크플로우 조건판별에 태움.
                                    //2. 워크플로우 진행.
                                }

                                @Override
                                public void proxyToServerResolutionFailed(String hostAndPort) {
                                    //1. 프록시일경우, 리스폰스의 스테이터스를 워크플로우 조건판별에 태움.
                                    //2. 워크플로우 진행.
                                }

                                @Override
                                public void serverToProxyResponseTimedOut() {
                                    //1. 프록시일경우, 리스폰스의 스테이터스를 워크플로우 조건판별에 태움.
                                    //2. 워크플로우 진행.
                                }
                            };
                        }
                    })
                    .start();

            logger.info("Starting Router Network Service");
        } catch (Exception e) {
            throw new APIServiceException(e, APIServiceStatus.UNKNOWN_SERVER_ERROR);
        }
    }

    public void setTaskExecutor(SimpleAsyncTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public SimpleAsyncTaskExecutor getTaskExecutor() {
        return taskExecutor;
    }
}