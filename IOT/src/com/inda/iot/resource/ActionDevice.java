package com.inda.iot.resource;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.api.services.pubsub.Pubsub;
import com.google.api.services.pubsub.model.AcknowledgeRequest;
import com.google.api.services.pubsub.model.PublishRequest;
import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.api.services.pubsub.model.PullRequest;
import com.google.api.services.pubsub.model.PullResponse;
import com.google.api.services.pubsub.model.ReceivedMessage;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.inda.iot.utils.PubsubUtils;
import com.google.common.collect.ImmutableList;




@Path("/action")
public class ActionDevice {
	
	private static final Logger logger = Logger.getLogger(ActionDevice.class.getName());

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response Action(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp
			
			
			
			) throws IOException, ParseException {
		logger.info("Inside POST request for ExecuteAction.");
		
		
		String output=getCommand();
		
			return Response.status(Status.OK).entity(output).build();
		}
	
	
	String getCommand()
	{
		String command="";
		   try
		   {
		    logger.info("inside getCommand") ;
		    
		    Pubsub client = PubsubUtils.getClient();
		    
		    String subscriptionName = PubsubUtils.getFullyQualifiedResourceName(
	                PubsubUtils.ResourceType.SUBSCRIPTION, "indaiot-1368", "controldevice");
	        PullRequest pullRequest = new PullRequest()
	                .setReturnImmediately(true)
	                .setMaxMessages(1);
	        
	        PullResponse pullResponse;
            pullResponse = client.projects().subscriptions()
                    .pull(subscriptionName, pullRequest)
                    .execute();
            List<String> ackIds = new ArrayList<>(1);
            List<ReceivedMessage> receivedMessages =
                    pullResponse.getReceivedMessages();
            if (receivedMessages != null) {
                for (ReceivedMessage receivedMessage : receivedMessages) {
                    PubsubMessage pubsubMessage =
                            receivedMessage.getMessage();
                    if (pubsubMessage != null
                            && pubsubMessage.decodeData() != null) {
                        logger.info(
                                new String(pubsubMessage.decodeData(),
                                        "UTF-8"));
                        command=new String(pubsubMessage.decodeData(),
                                "UTF-8");
                    }
                    ackIds.add(receivedMessage.getAckId());
                }
                AcknowledgeRequest ackRequest = new AcknowledgeRequest();
                ackRequest.setAckIds(ackIds);
                client.projects().subscriptions()
                        .acknowledge(subscriptionName, ackRequest)
                        .execute();
            }
		    
		   
		   }
		   catch (Exception e)
		   {
			   logger.info("Exception occurred inside startDevice "+e);
		   }
		   
		   return command;
	}
	
	
	@GET
	@Path("/e/{deviceid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response endDeviceRest(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp,
			
			@PathParam("deviceid") String deviceid
			
			) throws IOException, ParseException {
		logger.info("Inside POST request for endDeviceRest.");
		
		logger.info("deviceid=" + deviceid);
		endDevice(deviceid);
		String output="device stopped" ;
			return Response.status(Status.OK).entity(output).build();
		}
	
	
	void endDevice(String deviceid)
	{
		
		   
		    logger.info("inside endsDevice with deviceid "+deviceid) ;
	}
	

}
