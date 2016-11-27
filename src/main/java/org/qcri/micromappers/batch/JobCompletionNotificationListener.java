package org.qcri.micromappers.batch;

import org.qcri.micromappers.model.GdeltMaster;
import org.slf4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			List<GdeltMaster> results = jdbcTemplate.query("SELECT mmURL, mmType FROM gdeltmaster", new RowMapper<GdeltMaster>() {
				@Override
				public GdeltMaster mapRow(ResultSet rs, int row) throws SQLException {
					return new GdeltMaster(rs.getString(1), rs.getString(2));
				}
			});

			for (GdeltMaster person : results) {
				log.info("Found <" + person + "> in the database.");
			}

		}
	}
}
