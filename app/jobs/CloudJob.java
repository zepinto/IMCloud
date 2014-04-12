package jobs;

import scala.concurrent.duration.FiniteDuration;

/**
 * In order to schedule a job in the system, 
 * you must create a POJO implementing this interface under jobs pacakge
 * @author zp
 *
 */
public interface CloudJob extends Runnable {

	/**
	 * Time delay until the first run of this job
	 * @return A Duration after which this job will run for the first time 
	 */
	public FiniteDuration firstDelay();
	
	/**
	 * Time delay between subsequent calls to this job
	 * @return A valid Duration or <code>null</code> if this job is to be run a single time
	 */
	public FiniteDuration periodicDelay();	
}
