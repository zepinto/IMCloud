package controllers;

import java.util.ArrayList;

import models.Device;
import models.DevicePosition;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class Positions extends Controller {

	public static Result lastPositionsKml() {
		return TODO;
	}
	
	private static ArrayList<DevicePosition> getPositions() {
		ArrayList<DevicePosition> positions = new ArrayList<DevicePosition>();
		
		for (Device dev : Device.find.where().isNotNull("position").findList()) {
			System.out.println(dev);
			positions.add(dev.position);
		}
		return positions;
	}
	
	public static Result lastPositions() {
		return ok(Json.toJson(getPositions()));
	}
	
}
