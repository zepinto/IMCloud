package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class IridiumMessage extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public long id;
	
	@Required
	public int type;
	
	@Required
	@Lob
	public String msg;
	
	@Required
	@JsonSerialize(using=DateTimeSerializer.class)
	public Date created_at;
	
	@Required
	@JsonSerialize(using=DateTimeSerializer.class)
	public Date updated_at;
	
	public static Finder<Long, IridiumMessage> find = new Finder<Long, IridiumMessage>(
			Long.class, IridiumMessage.class);
	
	
}
