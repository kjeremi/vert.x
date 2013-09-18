/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.java.core.datagram.impl;

import io.netty.channel.ChannelFuture;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.datagram.DatagramPacket;
import org.vertx.java.core.datagram.DatagramServer;
import org.vertx.java.core.impl.DefaultFutureResult;
import org.vertx.java.core.impl.VertxInternal;

import java.net.*;

/**
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public class DefaultDatagramServer extends AbstractDatagramSupport<DatagramServer> implements DatagramServer {

  private Handler<DatagramPacket> dataHandler;
  public DefaultDatagramServer(VertxInternal vertx, StandardProtocolFamily family) {
    super(vertx, family);
    channel.pipeline().addLast("handler", new DatagramServerHandler(this.vertx, this));
  }

  @Override
  public DatagramServer listenMulticast(String multicastAddress, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    try {
      addListener(channel().joinGroup(InetAddress.getByName(multicastAddress)), handler);
    } catch (UnknownHostException e) {
      notifyException(handler, e);
    }
    return this;
  }

  @Override
  public DatagramServer listenMulticast(String multicastAddress, String networkInterface, String source, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    try {
      addListener(channel().joinGroup(InetAddress.getByName(multicastAddress),
              NetworkInterface.getByName(networkInterface), InetAddress.getByName(source)), handler);
    } catch (Exception e) {
      notifyException(handler, e);
    }
    return this;
  }

  @Override
  public DatagramServer unlistenMulticast(String multicastAddress, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    try {
      addListener(channel().leaveGroup(InetAddress.getByName(multicastAddress)), handler);
    } catch (UnknownHostException e) {
      notifyException(handler, e);
    }
    return this;
  }

  @Override
  public DatagramServer unlistenMulticast(String multicastAddress, String networkInterface, String source, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    try {
      addListener(channel().leaveGroup(InetAddress.getByName(multicastAddress),
              NetworkInterface.getByName(networkInterface), InetAddress.getByName(source)), handler);
    } catch (Exception e) {
      notifyException(handler, e);
    }
    return this;
  }

  @Override
  public DatagramServer blockMulticast(String multicastAddress, String networkInterface, String sourceToBlock, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    try {
      addListener(channel().block(InetAddress.getByName(multicastAddress),
              NetworkInterface.getByName(networkInterface), InetAddress.getByName(sourceToBlock)), handler);
    } catch (Exception e) {
      notifyException(handler, e);
    }
    return  this;
  }

  @Override
  public DatagramServer blockMulticast(String multicastAddress, String sourceToBlock, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    try {
      addListener(channel().block(InetAddress.getByName(multicastAddress), InetAddress.getByName(sourceToBlock)), handler);
    } catch (UnknownHostException e) {
      notifyException(handler, e);
    }
    return this;
  }

  @Override
  public DatagramServer listen(String address, int port, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    return listen(new InetSocketAddress(address, port), handler);
  }

  @Override
  public DatagramServer listen(int port, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    return listen(new InetSocketAddress(port), handler);
  }

  @Override
  public DatagramServer listen(InetSocketAddress local, Handler<AsyncResult<DatagramServer>> handler) {
    configurable = false;
    ChannelFuture future = channel().bind(local);
    addListener(future, handler);
    return this;
  }

  @Override
  public DatagramServer dataHandler(Handler<DatagramPacket> handler) {
    dataHandler = handler;
    return this;
  }

  final void handleMessage(DatagramPacket message) {
    if (dataHandler != null) {
      dataHandler.handle(message);
    }
  }

  @Override
  public DatagramServer exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  private void notifyException(final Handler<AsyncResult<DatagramServer>> handler, final Throwable cause) {
    if (context.isOnCorrectWorker(channel().eventLoop())) {
      try {
        vertx.setContext(context);
        handler.handle(new DefaultFutureResult<DatagramServer>(cause));
      } catch (Throwable t) {
        context.reportException(t);
      }
    } else {
      context.execute(new Runnable() {
        public void run() {
          handler.handle(new DefaultFutureResult<DatagramServer>(cause));
        }
      });
    }
  }

}
