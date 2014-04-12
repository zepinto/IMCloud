package jobs;

import java.util.concurrent.TimeUnit;

import models.Device;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.Play;
import play.libs.WS;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import com.avaje.ebean.Ebean;

public class AddressesUpdater implements CloudJob {

	@Override
	public void run() {
		
		String addrUrl = Play.application().configuration().getString("imcaddr.url");
		if (addrUrl == null) {
			Logger.error("Please check that you have configured an 'imcaddr.url' parameter.");
			return;
		}
		
		try {
			WS.Response r = WS.url(addrUrl).get().get(5, TimeUnit.SECONDS);
			Document doc = r.asXml();
			
			NodeList addresses = doc.getElementsByTagName("address");
			
			for (int i = 0; i < addresses.getLength(); i++) {
				Node nd = addresses.item(i);
				
				String idHex = nd.getAttributes().getNamedItem("id").getTextContent();
				String name = nd.getAttributes().getNamedItem("name").getTextContent();
				int id = Integer.parseInt(idHex.replaceAll("0x", ""), 16);
				
				if (id == 0)
					continue;
				
				Device dev = Device.find.byId((long)id);
				if (dev == null) {
					dev = new Device();
					dev.id = (long)id;
					dev.name = name;
					Ebean.beginTransaction();
					Ebean.save(dev);
					Ebean.commitTransaction();					
					Logger.warn("Added previously unknown device: "+name+" with id "+id);
				}				
			}
			
		}
		catch (Exception e) {
			Logger.error("Error updating IMC addresses", e);
			return;
		}
				
	}
    
	@Override
	public FiniteDuration firstDelay() {
		return Duration.create(0, TimeUnit.SECONDS);
	}

	@Override
	public FiniteDuration periodicDelay() {
		return null;
	}

}
