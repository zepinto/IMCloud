package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.db.ebean.Model;

/**
 * This class represents a device (System) whose positions can be stored at IMCloud
 * @author zp
 *
 */
@Entity
public class Device extends Model {

	private static final long serialVersionUID = -1932992943773194504L;
	
	@Id
	public Long id;

	@Constraints.Required
	public String name;

	public String iridiumImei;
	
	public Long position;
	
	public static Finder<Long, Device> find = new Finder<Long, Device>(
			Long.class, Device.class);
	
}
