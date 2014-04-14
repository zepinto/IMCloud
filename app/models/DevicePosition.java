package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	
	@Required
	@JsonSerialize(using=DateTimeSerializer.class)
	public Date timestamp = new Date();
	
	public String positionClass = "Unknown";
	
	public static Finder<Long, DevicePosition> find = new Finder<Long, DevicePosition>(
			Long.class, DevicePosition.class);
}
