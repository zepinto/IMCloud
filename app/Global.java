import jobs.CloudJob;

import org.reflections.Reflections;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application arg0) {
		super.onStart(arg0);
		scheduleJobs();
	}
	
	private void scheduleJobs() {
		Reflections ref = new Reflections("jobs");
		for (Class<? extends CloudJob> cj : ref.getSubTypesOf(CloudJob.class)) {
			try {
				CloudJob job = cj.newInstance();

				if (job.periodicDelay() != null) {
					Logger.info("Scheduling "+cj.getSimpleName()+" (periodically)");
					Akka.system()
					.scheduler()
					.schedule(job.firstDelay(), job.periodicDelay(), job,
							Akka.system().dispatcher());
				}
				else {
					Logger.info("Scheduling single execution of "
							+ cj.getSimpleName());
					Akka.system()
					.scheduler()
					.scheduleOnce(job.firstDelay(), job,
							Akka.system().dispatcher());
				}

			}
			catch (Exception e) {
				Logger.error("Error instantiating job", e);
			}
		}
	}		
}
