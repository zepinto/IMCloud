package controllers;

import java.util.ArrayList;
import java.util.Date;

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
	
	public static Result active() {
		
		ArrayList<Device> devices = new ArrayList<Device>();
		ArrayList<models.System> systems = new ArrayList<models.System>();
		Date oneDayAgo = new Date(System.currentTimeMillis() - 24 * 3600 * 1000);
		
		devices.addAll(Device.find.all());
		for (Device dev : devices) {
			if (dev.position != null && dev.position != null && dev.position.timestamp.after(oneDayAgo))
			systems.add(new models.System(dev));
		}
		
        return ok(Json.toJson(systems));
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
		Form<Device> deviceForm = Form.form(Device.class);
		return ok(views.html.editDevice.render(deviceForm, true, 0l));
	}
	
	public static Result submit() {
		try {
			System.out.println(request().body().asMultipartFormData());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			System.out.println(request().body().asJson());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return created();
	}
	
	public static Result get(long id) {
		Device dev = Device.find.byId(id);
		if (dev == null)
			return notFound("404,Device not found,"+id);
		else {
			Form<Device> deviceForm = Form.form(Device.class);
			frm.fill(dev);
			return ok(views.html.editDevice.render(deviceForm, false, id));
		}
	}
	
	public static Result delete(long id) {
		Device dev = Device.find.byId(id);
		if (dev == null)
			return badRequest("500,Device not found,"+id);
		else {
			dev.delete();
			return ok("200,Device has been deleted,"+id);
		}
	}
}
