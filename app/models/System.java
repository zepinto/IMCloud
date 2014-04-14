package models;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class is used to translate used 'Device' model to legacy Hub 'System' format
 * @author zp
 *
 */
public class System {
	public int imcid;
	public String name;
	public String imei = null;
	
	@JsonSerialize(using=DateTimeSerializer.class)
	public Date created_at, updated_at;

	public double[] coordinates = new double[]{0,0};
	
	public System(String name, int imcid) {
		this.imcid = imcid;
		this.name = name;
	}
	
	public System(Device dev) {
		this(dev.name, dev.id.intValue());
		this.imei = dev.iridiumImei;
		created_at = dev.created_at;
		updated_at = created_at;
		if (dev.position != null) {
			coordinates[0] = dev.position.lat;
			coordinates[1] = dev.position.lon;
			updated_at = dev.position.timestamp;
		}
	}
}