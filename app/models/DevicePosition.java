package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * This class represents a position stored at IMCloud
 * @author zp
 */
@Entity
public class DevicePosition extends Model {

	private static final long serialVersionUID = 5701125973214827713L;

	@Id
	public long id;

	@Constraints.Required
	public double lat, lon;
	
	@Formats.DateTime(pattern="YYYYMMdd-HH:mm:ss")
	@Required
	public Date timestamp = new Date();
	
	public String positionClass = "Unknown";
	
	public static Finder<Long, DevicePosition> find = new Finder<Long, DevicePosition>(
			Long.class, DevicePosition.class);
}
