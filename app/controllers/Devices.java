package controllers;

import java.util.ArrayList;

import models.Device;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Devices extends Controller {

	final static Form<Device> frm = Form.form(Device.class);
	
	public static Result list() {
        return ok(views.html.devlist.render(Device.find.all()));
    }
	
	public static Result index() {
		
		ArrayList<Device> devices = new ArrayList<Device>();
		ArrayList<models.System> systems = new ArrayList<models.System>();
		
		devices.addAll(Device.find.all());
		for (Device dev : devices) {
			systems.add(new models.System(dev));
		}
		
        return ok(Json.toJson(systems));
    }
	
	
	public static Result create() {
		return ok(views.html.device.render(frm, null));		
	}
	
	public static Result submit() {
		return TODO;
	}
	
	public static Result get(long id) {
		Device dev = Device.find.byId(id);
		if (dev == null)
			return notFound("ID not found.");
		else {
			frm.fill(dev);
			return ok(views.html.device.render(frm, dev));
		}	
	}
}
