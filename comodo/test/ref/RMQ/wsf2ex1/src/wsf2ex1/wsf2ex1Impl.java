/*******************************************************************************
 *    ESO - European Southern Observatory
 *
 *    (c) European Southern Observatory, 2011
 *    Copyright by ESO 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 * "@(#) $Id$" 
 *
 * who                when       what
 * ----------------  ----------  ----------------------------------------------
 * COMODO            -           Created.
 * 
 */

package wsf2ex1;

import wsf2ex1.wsf2ex1ImplAbstract;
import RMQCommons.Message;

import com.rabbitmq.client.QueueingConsumer;

/**
 * Developer's file to implement the ComponentAbstract
 * @see wsf2ex1.wsf2ex1ImplAbstract
 * @author COMODO
 * @version $Id$
 */
public class wsf2ex1Impl extends wsf2ex1ImplAbstract {
	public void run() throws java.io.IOException,
			java.lang.InterruptedException {
		getLogger().info("Starting ...");
		while (isRunning()) {
			try {

				QueueingConsumer.Delivery delivery = getConsumer()
						.nextDelivery();
				String message = new String(delivery.getBody());
				getLogger().debug(" [wsf2ex1Impl] Received '" + message + "'");

				String routingKey = delivery.getEnvelope().getRoutingKey();

				if (routingKey.matches(getCmdQueueName())) {
					// command signal
					String[] tokens = message.split(" ");
					String[] params = new String[tokens.length - 1];
					for (int i = 0; i < tokens.length - 1; i++) {
						params[i] = tokens[i + 1];
					}
					smFireSignal(new Message(tokens[0], params));
				} else {
					// pubsub cmd/data
					String[] params = message.split(" ");
					smFireSignal(new Message(routingKey, params));
				}
			} catch (Exception e) {
				getLogger().error(
						"Exception while processing a message: "
								+ e.getMessage());
				break;
			}
		}
		closeConnections();
		getLogger().info("Exiting ...");
	}
}
