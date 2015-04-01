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

package Server;

import RMQCommons.Message;
import RMQCommons.Topic;

import java.io.IOException;

import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.model.ModelException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.MessageProperties;

// Generic class for activity threads 

public abstract class SMActivity implements Runnable {

	private String mName;
	private String mParentStateId;
	private SCInstance mParentSCInstance;
	private Logger mLogger;
	private volatile boolean mIsRunning = false;

	private final static String DATA_EXCHANGE_NAME = "DATA";
	private ConnectionFactory mFactory = null;
	private Connection mConnection = null;
	private Channel mChannel = null;
	private QueueingConsumer mConsumer = null;

	public SMActivity(final String name, final String parentStateId,
			SCInstance parentSCInstance, Logger logger) {
		mName = name;
		mParentStateId = parentStateId;
		mParentSCInstance = parentSCInstance;
		mLogger = logger;
	}

	public String getName() {
		return mName;
	}

	public String getParentStateId() {
		return mParentStateId;
	}

	public SCInstance getParentSCInstance() {
		return mParentSCInstance;
	}

	public Logger getLogger() {
		return mLogger;
	}

	public QueueingConsumer getConsumer() {
		return mConsumer;
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public void startRunning() {
		mIsRunning = true;
		mLogger.debug("Starting activity: " + getName());
	}

	public void stopRunning() {
		mIsRunning = false;
	}

	public abstract void run();

	public void stop(Thread threadId) {
		mIsRunning = false;
		mLogger.debug("Stopping activity: " + getName() + " (threadId: "
				+ threadId.getName() + ")");
	}

	public void openConnection() {
		try {
			boolean durable = true; // for data we want persistance
			boolean autoAck = true;

			mFactory = new ConnectionFactory();
			mFactory.setHost("localhost");
			mConnection = mFactory.newConnection();
			mChannel = mConnection.createChannel();

			// data pub/sub
			mChannel.exchangeDeclare(DATA_EXCHANGE_NAME, "topic", durable);
			String pubsubQueueName = mChannel.queueDeclare().getQueue();

			mConsumer = new QueueingConsumer(mChannel);
			mChannel.basicConsume(pubsubQueueName, autoAck, mConsumer);

		} catch (IOException e) {
			getLogger()
					.error("Could not create RMQ channel: " + e.getMessage());
			stopRunning();
		}
	}

	public void closeConnection() {
		try {
			mChannel.close();
			mConnection.close();
			mLogger.debug("Closed RMQ connections");
		} catch (IOException e) {
			mLogger.error("Could not close RMQ connection: " + e.getMessage());
		}
	}

	public Message nextMessage(long timeout)
			throws java.lang.InterruptedException,
			com.rabbitmq.client.ShutdownSignalException {

		Message msg = null;
		QueueingConsumer.Delivery delivery;

		if (timeout == 0) {
			delivery = mConsumer.nextDelivery();
		} else {
			delivery = mConsumer.nextDelivery(timeout);
		}
		if (delivery != null) {
			String routingKey = delivery.getEnvelope().getRoutingKey();
			String payload = new String(delivery.getBody());
			getLogger().debug(
					" [" + getName() + "] Received routingKey '" + routingKey
							+ "' payload '" + payload + "'");

			if (routingKey.isEmpty()) {
			}

			msg = new Message(routingKey, payload.split(" "));
		}

		return msg;
	}

	public void publish(final String exchangeName, final String routingKey,
			final String msg) {
		try {
			mChannel.basicPublish(exchangeName, routingKey,
					MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
			mLogger.debug(" [" + getName() + "] Sent '" + routingKey + "':'"
					+ msg + "'");
		} catch (IOException e) {
			mLogger.error("Failure sending '" + routingKey + "':'" + msg
					+ "' (" + e.toString() + ")");
		}
	}

	public void publish(final String routingKey, final String msg) {
		try {
			mChannel.basicPublish(DATA_EXCHANGE_NAME, routingKey,
					MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
			mLogger.debug(" [" + getName() + "] Sent '" + routingKey + "':'"
					+ msg + "'");
		} catch (IOException e) {
			mLogger.error("Failure sending '" + routingKey + "':'" + msg
					+ "' (" + e.toString() + ")");
		}
	}

	public void publish(final Topic t) {
		publish(t.getName(), t.getPayload());
	}

	public void triggerEvent(final String eventName) {
		try {
			// Trigger an event
			mParentSCInstance.getExecutor()
					.triggerEvent(
							new TriggerEvent(eventName,
									TriggerEvent.SIGNAL_EVENT, null));
			mLogger.debug("Generate " + eventName + " signal");
		} catch (ModelException e) {
			mLogger.error("Cannot trigger " + eventName + " signal: "
					+ e.getMessage());
		}
	}
}
