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




@Path("/t")
public class TempService {
	
	private static final Logger logger = Logger.getLogger(TempService.class.getName());

	@GET
	@Path("/{temp}/{humidity}/{light}/{sound}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setTempHumidity(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp,
			
			@PathParam("temp") String temp,
			@PathParam("humidity") String humidity,
			@PathParam("light") String light,
			@PathParam("sound") String sound
			) throws IOException, ParseException {
		logger.info("Inside POST request for setTempHumidity.");
		
		logger.info("temp=" + temp);
		logger.info("humidity=" + humidity);
		logger.info("light=" + light);
		logger.info("sound=" + sound);
		insertTempHumidity(temp, humidity,light,sound);
		String output="UPLOAD SUCCESS" ;
			return Response.status(Status.OK).entity(output).build();
		}
	
	
	void insertTempHumidity(String temp, String humidity,String light,String sound)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		 Key nameKey = KeyFactory.createKey("temphumidity", "datetime");
	        
		    Entity tempHumidityEntity = new Entity("temphumidity", nameKey);
		    tempHumidityEntity.setProperty("datetime", System.currentTimeMillis());
		   
		    tempHumidityEntity.setProperty("temp", temp);
		    tempHumidityEntity.setProperty("humidity", humidity);
		    tempHumidityEntity.setProperty("light", light);
		    tempHumidityEntity.setProperty("sound", sound);
		  datastore.put(tempHumidityEntity) ;
		   
		    logger.info("tempHumidity added") ;
	}
	

}
