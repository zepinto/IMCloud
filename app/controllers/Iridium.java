package controllers;

import iridium.ActivateSubscription;
import iridium.DeactivateSubscription;
import iridium.DeviceUpdate;
import iridium.Position;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import models.Device;
import models.DevicePosition;
import models.IridiumMessage;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;

public class Iridium extends Controller {

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static final SimpleDateFormat incomingFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	public static String subscriber = null;
	
	static {
		incomingFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public static Result index() {
		String since = request().getQueryString("since");
		if (since == null || since.isEmpty()) {
			return ok(Json.toJson(IridiumMessage.find.all()));
		}
		else {
			try {
				List<IridiumMessage> msgs = IridiumMessage.find.where().gt("created_at", df.parse(since)).findList();
				return ok(Json.toJson(msgs));
			}
			catch (Exception e) {
				Logger.error("Error fetching messages", e);
				return badRequest("Bad request: "+e.getMessage());
			}
		}
    }
	
	public static Result post() {
		byte[] data = request().body().asRaw().asBytes();
		try {
			processMessage(iridium.IridiumMessage.deserialize(data));
			return created();
		}
		catch (Exception e) {
			Logger.error("Error parsing message", e);
			return badRequest(e.getClass().getSimpleName()+":"+e.getMessage());
		}
	}
	
	private static void process(DeviceUpdate msg) {
		for (Position pos: msg.getPositions().values()) {
			Device dev = Device.find.byId((long)pos.id);
			if (dev == null) {
				Logger.error("Received device update with if " + pos.id
						+ " is unknown.");
				continue;
			}
			DevicePosition dp = new DevicePosition();
			dp.lat = pos.latitude;
			dp.lon = pos.longitude;
			dp.timestamp = new Date((long)(pos.timestamp * 1000));
			dp.positionClass = "Unknown";
			dp.save();
			if (dev.position == null) {
				dev.position = dp;
				Logger.warn("Received first position for "+dev.name+".");
			}
			else {
				DevicePosition prev = dev.position;
				if (prev == null || prev.timestamp.before(dp.timestamp)) {
					dev.position = dp;
					Logger.warn("Position for "+dev.name+" was updated.");
				}
			}
			Ebean.save(dev);
		}
	}
	
	public static void processMessage(iridium.IridiumMessage msg) {
		if (msg instanceof DeviceUpdate) {
			process((DeviceUpdate) msg);
		}
		else if (msg instanceof ActivateSubscription) {
			Device dev = Device.find.byId((long)msg.source);
			if (dev != null && dev.iridiumImei != null) {
				subscriber = dev.iridiumImei;
				Logger.warn("Device named "+dev.name+" has activated subscription of device udpates");
			}
			else {
				Logger.error("Cannot subscribe without a valid iridium IMEI");
			}
		}
		else if (msg instanceof DeactivateSubscription) {
			subscriber = null;
			Logger.warn("No more updates will be sent");
		}
	}
	
	
}
