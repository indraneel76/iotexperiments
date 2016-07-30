package com.inda.iot.resource;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
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
import com.google.api.services.pubsub.model.PublishRequest;
import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.inda.iot.utils.PubsubUtils;
import com.google.common.collect.ImmutableList;




@Path("/controldevices")
public class ControlDevice {
	
	private static final Logger logger = Logger.getLogger(ControlDevice.class.getName());

	@GET
	@Path("/{command}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response startDeviceRest(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp,
			
			@PathParam("command") String command
			
			) throws IOException, ParseException {
		logger.info("Inside POST request for setTempHumidity.");
		
		logger.info("command=" + command);
		executeCommand(command);
		String output="device started" ;
			return Response.status(Status.OK).entity(output).build();
		}
	
	
	void executeCommand(String command)
	{
		
		   try
		   {
		    logger.info("inside executeCommand with command "+command) ;
		    Pubsub client = PubsubUtils.getClient();
		    
		    String fullTopicName = String.format("projects/%s/topics/%s",
                    PubsubUtils.getProjectId(),
                    PubsubUtils.getAppTopicName());
		    PubsubMessage pubsubMessage = new PubsubMessage();
            pubsubMessage.encodeData(command.getBytes("UTF-8"));
            PublishRequest publishRequest = new PublishRequest();
            publishRequest.setMessages(ImmutableList.of(pubsubMessage));

            client.projects().topics()
                    .publish(fullTopicName, publishRequest)
                    .execute();
		   }
		   catch (Exception e)
		   {
			   logger.info("Exception occurred inside startDevice "+e);
		   }
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
