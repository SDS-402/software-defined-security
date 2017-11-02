package com.sds.orchestration.scheduler;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sds.orchestration.core.GlobalConfig;
import com.sds.orchestration.entity.OrchestrationNode;
import com.sds.orchestration.entity.TimeEntity;
import com.sds.orchestration.entity.TimeType;
import com.sds.orchestration.entity.TimerEntity;

public class OrchestrationScheduledManager {

	private static Logger logger = LoggerFactory.getLogger(OrchestrationScheduledManager.class);
	private static OrchestrationScheduledManager instance=null;
	//private final static int DEFAULT_SCHEDULE_INTERVAL=3;
	private static SchedulerFactory sf = new StdSchedulerFactory();
	private static final String JOB_GROUP_NAME = "group";
	private static final String TRIGGER_GROUP_NAME = "trigger";

	public static OrchestrationScheduledManager getScheduledManager() {
		if(instance==null){
			instance=new OrchestrationScheduledManager();
		}
		return instance;
	}
	
	public void addJob(OrchestrationNode orchestrationNode){
		OrchestrationJob job=new OrchestrationJob();
		try {
			JobDataMap jobDataMap=new JobDataMap();
			jobDataMap.put("triggerApp", orchestrationNode.getTriggerApp());
			jobDataMap.put("eventApp", orchestrationNode.getEventApp());
			jobDataMap.put("timer",orchestrationNode.getTimerEntity());
			addJobImp(job, GlobalConfig.getInstance().Interval,jobDataMap);
		} catch (SchedulerException | ParseException e) {
			logger.error("");
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 * @param job
	 * @param minutes 配置文件默认Job执行时间间隔，如策略时间参数无定义，使用该参数
	 * @param jobDataMap 参数传递，job需要无参构造函数
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	private void addJobImp(OrchestrationJob job, int minutes,JobDataMap jobDataMap)
			throws SchedulerException, ParseException {
		Scheduler scheduler = sf.getScheduler();
//		logger.info("scheduler interval minutes is "+minutes);
		
		String jobId=jobDataMap.get("triggerApp").toString()+jobDataMap.get("eventApp").toString() + jobDataMap.get("timer").toString();
		if(scheduler.checkExists(JobKey.jobKey(jobId,JOB_GROUP_NAME))){
			logger.info("the orchestration node is existed!!!");
			return;
		}
		
		TimerEntity te = (TimerEntity)jobDataMap.get("timer");
		String qt = te.getQuantum();
		String it = te.getInterval();
		String[] qts = qt.split(" ");
		TimeEntity quantum = new TimeEntity(Integer.valueOf(qts[0]),TimeType.valueOf(qts[1].toUpperCase()));
		String[] its = it.split(" ");
		TimeEntity interval = new TimeEntity(Integer.valueOf(its[0]),TimeType.valueOf(its[1].toUpperCase()));
		int counter = te.getCounter();
		
		
		logger.info("register orchestraion node start...");
		JobDetail jobDetail = JobBuilder.newJob(job.getClass())// 任务名，任务组，任务执行类
				.withIdentity(jobId, JOB_GROUP_NAME)
				.setJobData(jobDataMap)
				.build();
		
		//set schedule time here
		
//		SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(9).repeatHourlyForever(2).withRepeatCount(4);
		SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule();
		TimeType tp = interval.getTimeType();
		int value = interval.getValue();
		
		// set interval 
		if(counter == 0)
		{
			switch (tp)
			{
				case SECOND:
					schedule = schedule.withIntervalInSeconds(value).repeatForever();
					logger.info("Job Execute interval set to " + value + " seconds");
					break;
				case MINUTE:
					schedule = schedule.withIntervalInMinutes(value).repeatForever();
					logger.info("Job Execute interval set to " + value + " minutes");
					break;
				case HOUR:
					schedule = schedule.withIntervalInHours(value).repeatForever();
					logger.info("Job Execute interval set to " + value + " hours");
					break;
				case FOREVER:
					schedule = schedule.repeatForever();
					break;
				default :
					schedule = schedule.repeatForever();
			}
		}
		else{
			switch (tp)
			{
				case SECOND:
					schedule = schedule.withIntervalInSeconds(value).withRepeatCount(counter);
					break;
				case MINUTE:
					schedule = schedule.withIntervalInMinutes(value).withRepeatCount(counter);
					break;
				case HOUR:
					schedule = schedule.withIntervalInHours(value).withRepeatCount(counter);
					break;
				case FOREVER:
					schedule = schedule.repeatForever();
					break;
				default :
					schedule = schedule.repeatForever();
			}
		}
		
		tp = quantum.getTimeType();
		value = quantum.getValue();
		int timeCounter = 0;
		// set quantum
		switch (tp)
		{
			case SECOND:
				timeCounter = value*1000;
				logger.info("Job Execute Time set to " + value + " seconds");
				break;
			case MINUTE:
				timeCounter = value*1000*60;
				logger.info("Job Execute Time set to " + value + " minutes");
				break;
			case HOUR:
				timeCounter = value*1000*60*60;
				logger.info("Job Execute Time set to " + value + " hours");
				break;
			default :
				timeCounter = 0;
				logger.info("Job Execute Time set to " + " forever");
		}
		
		Trigger trigger = TriggerBuilder
				.newTrigger()
				// 触发器名,触发器组
				.withIdentity(jobId, TRIGGER_GROUP_NAME)
//				.withSchedule(
//						SimpleScheduleBuilder.simpleSchedule()
//								.withIntervalInMinutes(minutes).repeatForever())
				.withSchedule(schedule)
				.build();
		scheduler.scheduleJob(jobDetail, trigger);
		
		if(timeCounter == 0){
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}

			logger.info("register orchestraion node ok...");
		}
		else{
			scheduler.start();
			logger.info("register orchestraion node ok...");
			Timer timer = new Timer();
			ScheduleRunTask task = new ScheduleRunTask(scheduler);
			logger.info("timeCounter : " + timeCounter);
			timer.schedule(task, timeCounter);
		}

	}

	class ScheduleRunTask extends TimerTask{
		private Scheduler sche;
		public ScheduleRunTask(Scheduler sche){
			this.sche = sche;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				sche.shutdown();
				logger.info(sche.getSchedulerName() + " shutdown.");
				SchedulerMetaData metaData = sche.getMetaData(); 
				logger.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");  
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Updating an Existing Trigger
	 * 
	 * @param job
	 * @param minutes
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void modifyTrigger(OrchestrationJob job, int minutes)
			throws SchedulerException, ParseException {
		Scheduler scheduler = sf.getScheduler();
		Trigger oldTrigger = scheduler.getTrigger(TriggerKey.triggerKey(
				job.toString(), TRIGGER_GROUP_NAME));
		TriggerBuilder tb = oldTrigger.getTriggerBuilder();
		Trigger newTrigger = tb.withSchedule(
				SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(minutes).repeatForever())
						.build();
		scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);

	}
	
	
	public static void main(String[] args) throws SchedulerException,
			ParseException, InterruptedException {
		//TestJob job=new TestJob("job1");
		/*new OrchestrationScheduledManager.addJob(job, 5);
		new OrchestrationScheduledManager.addJob(job, 5);
		
		
		Thread.sleep(10000);
		System.out.println("mod...");
		OrchestrationScheduledManager.modifyTrigger(job, 1);*/
	}
}
