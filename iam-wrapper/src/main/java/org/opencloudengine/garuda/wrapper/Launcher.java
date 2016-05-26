package org.opencloudengine.garuda.wrapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.opencloudengine.garuda.HttpFilters;
import org.opencloudengine.garuda.HttpFiltersAdapter;
import org.opencloudengine.garuda.HttpFiltersSourceAdapter;
import org.opencloudengine.garuda.HttpProxyServer;
import org.opencloudengine.garuda.impl.DefaultHttpProxyServer;

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
                                        // TODO: implement your filtering here
                                        return null;
                                    }

                                    @Override
                                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                        // TODO: implement your filtering here
                                        return httpObject;
                                    }
                                };
                            }
                        })
                        .start();
    }
}
