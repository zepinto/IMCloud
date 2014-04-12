package jobs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import models.Device;
import models.DevicePosition;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.Play;
import play.libs.WS;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.QueryIterator;

public class SpotUpdater implements CloudJob {

	private static final String pageUrl = Play.application().configuration().getString("spot.url");

	@Override
	public FiniteDuration firstDelay() {
		return Duration.create(5, TimeUnit.SECONDS);
	}
	
	@Override
	public FiniteDuration periodicDelay() {
		return Duration.create(60, TimeUnit.SECONDS);
	}
	
	@Override
	public void run() {
		Logger.info("Fetching Spot positions");
		
		if (pageUrl == null) {
			Logger.error("Please check that you have configured an 'spot.url' parameter.");
			return;
		}
		try {
			WS.Response r = WS.url(pageUrl).get().get(5, TimeUnit.SECONDS);
			Document doc = r.asXml();
			
			NodeList messages = doc.getElementsByTagName("message");
			for (int i = 0; i < messages.getLength(); i++) {
				NodeList elems = messages.item(i).getChildNodes();
				DevicePosition dp = new DevicePosition();
				String device = "";

				for (int j = 0; j < elems.getLength(); j++) {
					Node nd = elems.item(j); 
					switch (nd.getNodeName()) {
					case "unixTime":
						dp.timestamp = new Date(Long.parseLong(nd.getTextContent()) * 1000);
						break;
					case "latitude":
						dp.lat = Double.parseDouble(nd.getTextContent());
						break;
					case "longitude":
						dp.lon = Double.parseDouble(nd.getTextContent());
						break;
					case "messengerName":
						device = nd.getTextContent();
						break;
					default:
						break;
					}
				}
				
				Ebean.beginTransaction();
				Ebean.save(dp);
				QueryIterator<Device> devIt = Device.find.where().ilike("name", device).findIterate();
				if (!devIt.hasNext()) {
					Logger.error("Found spot tag with name "+device+" is unknown.");		
				}
				else {
					Device dev = devIt.next(); 
					dev.lastPosition = dp;
					Ebean.save(dev);
				}
				Ebean.endTransaction();				
			}			
		}
		catch (Exception e) {
			Logger.error("Error updating spot positions", e);
		}
	}	
}
