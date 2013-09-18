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
package org.vertx.java.core.datagram;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.streams.ExceptionSupport;

import java.net.InetSocketAddress;



/**
 * A {@link DatagramServer} which can be used for write and receive datagram packets.
 *
 *
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
public interface DatagramServer extends DatagramSupport<DatagramServer>, ExceptionSupport<DatagramServer> {

  /**
   * Return the {@link InetSocketAddress} to which this {@link DatagramServer} is bound too.
   */
  InetSocketAddress localAddress();

  /**
   * Joins a multicast group and so start listen for packets send to it. The {@link Handler} is notified once the operation completes.
   *
   * @param   multicastAddress  the address of the multicast group to join
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  DatagramServer listenMulticast(String multicastAddress, Handler<AsyncResult<DatagramServer>> handler);

  /**
   * Joins a multicast group and so start listen for packets send to it on the given network interface.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param   multicastAddress  the address of the multicast group to join
   * @param   networkInterface  the network interface on which to listen for packets.
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  DatagramServer listenMulticast(
          String multicastAddress, String networkInterface, String source, Handler<AsyncResult<DatagramServer>> handler);

  /**
   * Leaves a multicast group and so stop listen for packets send to it.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param   multicastAddress  the address of the multicast group to leave
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  DatagramServer unlistenMulticast(String multicastAddress, Handler<AsyncResult<DatagramServer>> handler);


  /**
   * Leaves a multicast group and so stop listen for packets send to it on the given network interface.
   * The {@link Handler} is notified once the operation completes.
   *
   * @param   multicastAddress  the address of the multicast group to join
   * @param   networkInterface  the network interface on which to listen for packets.
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  DatagramServer unlistenMulticast(
          String multicastAddress, String networkInterface, String source,
          Handler<AsyncResult<DatagramServer>> handler);

  /**
   * Block the given sourceToBlock address for the given multicastAddress and notifies the {@link Handler} once
   * the operation completes.
   *
   * @param   multicastAddress  the address for which you want to block the sourceToBlock
   * @param   sourceToBlock     the source address which should be blocked. You will not receive an multicast packets
   *                            for it anymore.
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  DatagramServer blockMulticast(
          String multicastAddress, String sourceToBlock, Handler<AsyncResult<DatagramServer>> handler);

  /**
   * Block the given sourceToBlock address for the given multicastAddress on the given network interface and notifies
   * the {@link Handler} once the operation completes.
   *
   * @param   multicastAddress  the address for which you want to block the sourceToBlock
   * @param   sourceToBlock     the source address which should be blocked. You will not receive an multicast packets
   *                            for it anymore.
   * @param   networkInterface  the network interface on which the blocking should accour.
   * @param   handler           then handler to notify once the operation completes
   * @return  this              returns itself for method-chaining
   */
  DatagramServer blockMulticast(
          String multicastAddress, String networkInterface,
          String sourceToBlock, Handler<AsyncResult<DatagramServer>> handler);

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   */
  DatagramServer dataHandler(Handler<DatagramPacket> packetHandler);

  /**
   * @see #listen(java.net.InetSocketAddress, org.vertx.java.core.Handler)
   */
  DatagramServer listen(String address, int port, Handler<AsyncResult<DatagramServer>> handler);

  /**
   * @see #listen(java.net.InetSocketAddress, org.vertx.java.core.Handler)
   */
  DatagramServer listen(int port, Handler<AsyncResult<DatagramServer>> handler);

  /**
   * Makes this {@link DatagramServer} listen to the given {@link InetSocketAddress}. Once the operation completes
   * the {@link Handler} is notified.
   *
   * @param local     the {@link InetSocketAddress} on which the {@link DatagramServer} will listen for {@link DatagramPacket}s.
   * @param handler   the {@link Handler} to notify once the operation completes
   * @return this     itself for method-chaining
   */
  DatagramServer listen(InetSocketAddress local, Handler<AsyncResult<DatagramServer>> handler);
}
