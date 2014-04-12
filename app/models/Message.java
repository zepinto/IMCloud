package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

/**
 * This class represents a message sent via IMCloud
 * @author zp
 */
@Entity
public class Message extends Model {

	private static final long serialVersionUID = 3932518110784908982L;

	/**
	 * The unique id of this message
	 */
	@Id
	public long id;
	
	/**
	 * The type (message id) of this message
	 */
	@Required
	public int type;
	
	/**
	 * The destination for this message (imc id)
	 */
	@Required
	public long dst;
	
	/**
	 * The actual data
	 */
	@Required
	public byte[] data;
	
	/**
	 * Timestamp when this message was created / produced
	 */
	@Formats.DateTime(pattern="YYYYMMdd-HH:mm:ss")
	@Required
	public Date timestamp = new Date();
	
	public static Finder<Long, Message> find = new Finder<Long, Message>(
			Long.class, Message.class);
}
