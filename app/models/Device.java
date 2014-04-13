package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
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
	@Constraints.Required
	public Long id;

	@Required
	public String name;

	public String iridiumImei;
	
	@OneToOne
	public DevicePosition position;
	
	public static Finder<Long, Device> find = new Finder<Long, Device>(
			Long.class, Device.class);
	
	public String validate() {
		return null;
	}

}
