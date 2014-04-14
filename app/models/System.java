package models;

import java.text.SimpleDateFormat;

/**
 * This class is used to translate used 'Device' model to legacy Hub 'System' format
 * @author zp
 *
 */
public class System {
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public int imcid;
	public String name;
	public String imei = null;
	public String created_at = "", updated_at = "";
	public double[] coordinates = new double[]{0,0};
	
	public System(String name, int imcid) {
		this.imcid = imcid;
		this.name = name;
	}
	
	public System(Device dev) {
		this(dev.name, dev.id.intValue());
		this.imei = dev.iridiumImei;
		created_at = df.format(dev.created_at);
		updated_at = created_at;
		if (dev.position != null) {
			coordinates[0] = dev.position.lat;
			coordinates[1] = dev.position.lon;
			updated_at = df.format(dev.position.timestamp);
		}
	}
}