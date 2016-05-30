package org.opencloudengine.garuda.wrapper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.opencloudengine.garuda.HttpFilters;
import org.opencloudengine.garuda.HttpFiltersAdapter;
import org.opencloudengine.garuda.HttpFiltersSourceAdapter;
import org.opencloudengine.garuda.HttpProxyServer;
import org.opencloudengine.garuda.impl.DefaultHttpProxyServer;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by uengine on 2016. 5. 26..
 */
public class Launcher {
    public static void main(final String... args) {
        HttpProxyServer server =
                DefaultHttpProxyServer.bootstrap()
                        .withPort(9090)
                        .withFiltersSource(new HttpFiltersSourceAdapter() {
                            public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                                return new HttpFiltersAdapter(originalRequest) {
                                    @Override
                                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                        if (httpObject instanceof HttpRequest) {
                                            HttpRequest httpRequest = (HttpRequest) httpObject;
                                            String uriRemote = "http://52.79.164.208:8080" + httpRequest.getUri();
                                            httpRequest.setUri(uriRemote);
                                        }
                                        String answer = "hello";
                                        ByteBuf buffer = null;
                                        try {
                                            buffer = Unpooled.wrappedBuffer(answer.getBytes("UTF-8"));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, buffer);
                                        HttpHeaders.setContentLength(response, buffer.readableBytes());
//                                        HttpHeaders.setHeader(response, HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
//                                        return response;
                                        HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "text/html");
                                        return response;
                                    }

                                    @Override
                                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                        return httpObject;
                                    }
                                };
                            }
                        })
                        .start();
    }
}
