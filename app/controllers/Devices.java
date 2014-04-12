package controllers;

import models.Device;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Devices extends Controller {

	final static Form<Device> frm = Form.form(Device.class);
	
	public static Result index() {
        return ok(Json.toJson(Device.find.all()));
    }
	
	public static Result create() {
		return ok(views.html.device.render(frm));		
	}
	
	public static Result submit() {
		return TODO;
	}
	
	public static Result get(long id) {
		Device dev = Device.find.byId(id);
		if (dev == null)
			return notFound("ID not found.");
		else
			return ok(play.libs.Json.toJson(dev));
	}
}
