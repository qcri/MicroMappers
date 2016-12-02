package org.qcri.micromappers.batch.tasklet;


import org.qcri.micromappers.model.GdeltMaster;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.repeat.RepeatStatus;


import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MyTasklet implements Tasklet{
	
	private DataSource dataSource;
	private String MASTER_URL = "http://data.gdeltproject.org/micromappers/lastupdate.txt";

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("Execution ***************************");
		URL url = new URL(MASTER_URL);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));

		String inputLine;
		JdbcBatchItemWriter<GdeltMaster> writer = new JdbcBatchItemWriter<GdeltMaster>();
		while ((inputLine = in.readLine()) != null){
			GdeltMaster gdeltMaster = new GdeltMaster(inputLine);

			writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<GdeltMaster>());
			writer.setSql("INSERT INTO gdeltmaster (mmURL, mmType) VALUES (:mmURL, :mmType)");
			writer.setDataSource(dataSource);
			System.out.println(inputLine);
		}

		in.close();

		System.out.println("Execution Finish***************************");
		return RepeatStatus.FINISHED;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

}
