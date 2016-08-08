package com.yuyue.cu.core.job;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.yuyue.cu.core.abstractor.AbstractCriteriaBuilder;
import com.yuyue.cu.core.web.criteria.BaseAccountTempCriteriaBuilder;
import com.yuyue.cu.service.SimpleModuleService;

public class PurgeJob implements Job {
	private static final Logger log = Logger.getLogger(PurgeJob.class);
	
	SimpleModuleService simpleModuleService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Start purge task...");
		simpleModuleService = (SimpleModuleService)context.getJobDetail().getJobDataMap().get("simpleModuleService");
		executeJob(context);
		log.info("End purge task...");
	}

	/**
	 * 删除30天前的account_temp表里面的数据
	 * @param context
	 */
	protected void executeJob(JobExecutionContext context) {
		AbstractCriteriaBuilder criteriaBuilder = new BaseAccountTempCriteriaBuilder(null);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -30);
		criteriaBuilder.addLtCriteria("createDate", calendar.getTime());
		simpleModuleService.deleteSimpleObject(criteriaBuilder);
	}

}
