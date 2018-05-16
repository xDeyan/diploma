package bg.deyan.diploma;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class RequestEntryService {

	private static final Logger LOG = LoggerFactory.getLogger(RequestEntryService.class);

	private static final int TASKS_SIZE = Integer.MAX_VALUE / 200;

	@Autowired
	private RequestEntryDao requestEntryDao;

	public void processTraditional() {
		LOG.info("Started traditional processing...");
		StopWatch stopWatch = new StopWatch("traditional");
		stopWatch.start("TRADITIONAL");

		for (int i = 0; i < TASKS_SIZE; i++) {
			// LOG.info(String.valueOf(i));
		}

		stopWatch.stop();
		LOG.info(stopWatch.prettyPrint());
	}

	public void processReactive() {
		LOG.info("Started reactive processing...");
		StopWatch stopWatch = new StopWatch("reactive");

		Flux.range(0, TASKS_SIZE)
				.parallel()
				.runOn(Schedulers.newElastic("reactive"))
				.subscribe(new Subscriber<Integer>() {
					private Subscription s;

					@Override
					public void onSubscribe(Subscription s) {
						(this.s = s).request(1);
						stopWatch.start("REACTIVE");
					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();
					}

					@Override
					public void onComplete() {
						stopWatch.stop();
						LOG.info(stopWatch.prettyPrint());
					}

					@Override
					public void onNext(Integer t) {
						if (t.equals(0) || t.equals(TASKS_SIZE - 1)) {
							LOG.info(t.toString());
						}

						s.request(1);
					}

				});

		LOG.info("sdfsdf");

	}

}
