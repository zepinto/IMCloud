package jobs;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import models.Device;
import models.DevicePosition;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.QueryIterator;

import play.Logger;
import play.Play;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import fr.cls.argos.dataxmldistribution.service.DixService;
import fr.cls.argos.dataxmldistribution.service.types.XmlRequestType;

public class ArgosUpdater implements CloudJob {

	@Override
	public void run() {
		Logger.info("Fetching Argos positions");

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		DixService srv = new DixService();
		XmlRequestType request = new XmlRequestType();

		String username = Play.application().configuration().getString("argos.username");
		String password = Play.application().configuration().getString("argos.password");
		String platform = Play.application().configuration().getString("argos.platform");
		
		if (username == null) {
			Logger.error("Please check that you have configured an 'argos.username' parameter.");
			return;
		}		
		if (password == null) {
			Logger.error("Please check that you have configured an 'argos.password' parameter.");
			return;
		}
		if (platform == null) {
			Logger.error("Please check that you have configured an 'argos.platform' paramter.");
			return;
		}
		
		request.setUsername(username);
		request.setPassword(password);
		request.setPlatformId(platform);
		request.setMostRecentPassages(true);
		request.setNbDaysFromNow(10);
		try {
			String xml = srv.getDixServicePort().getXml(request).getReturn();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xml.getBytes()));
			NodeList locations = doc.getElementsByTagName("location");
			for (int i = 0; i < locations.getLength(); i++) {
				Node locNode = locations.item(i);
				Node platformNode = locNode.getParentNode().getParentNode();
				Node platfId = platformNode.getFirstChild();
				String id = platfId.getTextContent();
				NodeList childs = locNode.getChildNodes();
				String lat = null, lon = null, date = null, locClass = null;

				for (int j = 0; j < childs.getLength(); j++) {
					Node elem = childs.item(j);
					switch (elem.getNodeName()) {
					case "locationDate":
						date = elem.getTextContent();
						break;
					case "latitude":
						lat = elem.getTextContent();
						break;
					case "longitude":
						lon = elem.getTextContent();
						break;
					case "locationClass":
						locClass = elem.getTextContent();
						break;
					default:
						break;
					}
				}

				id = resolve(id);
				DevicePosition dp = new DevicePosition();
				dp.lat = Double.parseDouble(lat);
				dp.lon = Double.parseDouble(lon);
				dp.positionClass = locClass;
				dp.timestamp = df.parse(date.replaceAll("T", " ").replaceAll(
						"Z", ""));

				Ebean.beginTransaction();
				Ebean.save(dp);
				QueryIterator<Device> devIt = Device.find.where()
						.ilike("name", id).findIterate();
				if (!devIt.hasNext()) {
					Logger.error("Found argos tag with name " + id
							+ " is unknown.");
				} 
				else {
					Device dev = devIt.next();
					if (dev.position == null) {
						dev.position = dp.id;
						Logger.warn("Received first position for "+dev.name+".");
					}
					else {
						DevicePosition prev = DevicePosition.find.byId(dev.position);
						if (prev == null || prev.timestamp.before(dp.timestamp)) {
							dev.position = dp.id;
							Logger.warn("Position for "+dev.name+" was updated.");
						}
					}
					Ebean.save(dev);
				}
				Ebean.commitTransaction();
			}
		} catch (Exception e) {
		}
	}

	
	private String resolve(String argosId) {
		return "Argos-"+argosId;
	}
	@Override
	public FiniteDuration firstDelay() {
		return Duration.create(10, TimeUnit.SECONDS);
	}

	@Override
	public FiniteDuration periodicDelay() {
		return Duration.create(60, TimeUnit.SECONDS);
	}

}
