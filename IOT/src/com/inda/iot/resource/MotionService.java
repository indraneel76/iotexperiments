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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;




@Path("/m")
public class MotionService {
	
	private static final Logger logger = Logger.getLogger(MotionService.class.getName());

	@GET
	@Path("/{motion}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setMotion(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp,
			
			@PathParam("motion") String motion
			
			) throws IOException, ParseException {
		logger.info("Inside POST request for setTempHumidity.");
		
		logger.info("motion=" + motion);
		insertMotion(motion);
		String output="UPLOAD SUCCESS" ;
			return Response.status(Status.OK).entity(output).build();
		}
	
	
	void insertMotion(String motion)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		 Key nameKey = KeyFactory.createKey("motion", "datetime");
	        
		    Entity motionEntity = new Entity("motion", nameKey);
		    motionEntity.setProperty("datetime", System.currentTimeMillis());
		   
		    motionEntity.setProperty("motion", motion);
		    
		  datastore.put(motionEntity) ;
		   
		    logger.info("motions added") ;
	}
	

}
