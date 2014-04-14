package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import models.Device;
import models.IridiumMessage;

import org.apache.commons.codec.binary.Hex;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;



public class RockBlock extends Controller {
	
	private static final SimpleDateFormat incomingFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
	
	public static Result post() throws Exception {
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
		Iridium.processMessage(m);
		return created(Json.toJson(irMsg));
    }
}
