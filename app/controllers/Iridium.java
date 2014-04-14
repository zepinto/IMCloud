package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import models.Device;
import models.IridiumMessage;

import org.apache.commons.codec.binary.Hex;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class Iridium extends Controller {

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private static final SimpleDateFormat incomingFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
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
	
	public static Result postRockBlock() throws Exception {
		Http.MultipartFormData formData = request().body().asMultipartFormData();
		
		Map<String, String[]> data = request().body().asFormUrlEncoded();
		if (data == null || data.isEmpty())
			data = formData.asFormUrlEncoded();
		
		String imei = data.get("imei")[0];
		String msg = data.get("data")[0];
		String transmitted = data.get("transmit_time")[0];
		byte buff[] = Hex.decodeHex(msg.toCharArray());
		iridium.IridiumMessage m = iridium.IridiumMessage.deserialize(buff);
		IridiumMessage irMsg = new IridiumMessage();
		irMsg.created_at = incomingFormat.parse(transmitted);
		irMsg.updated_at = new Date();
		irMsg.msg = msg;
		irMsg.type = m.getMessageType();
		long imc_id = m.getSource();
		Device dev = Device.find.byId(imc_id);
		if (dev != null && !imei.equals(dev.iridiumImei)) {
			dev.iridiumImei = imei;
			dev.save();
			Logger.warn("Updated IMEI for device "+dev.name);
		}
		irMsg.save();
		return created(Json.toJson(irMsg));
    }
	
}
