/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pivotal.io.batch;


import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class FileSplitAndHDFSSinkTasklet implements Tasklet, StepExecutionListener {

	private static final Logger logger = Logger.getLogger(FileSplitAndHDFSSinkTasklet.class.getName());
	private volatile AtomicInteger counter = new AtomicInteger(0);


	private String infile =null;

	private String outdir =null;

	private String fsUri;

	private String rollover;

	public String getInfile() {
		return infile;
	}

	public void setInfile(String infile) {
		this.infile = infile;
	}

	public String getOutdir() {
		return outdir;
	}

	public void setOutdir(String outdir) {
		this.outdir = outdir;
	}

	public String getFsUri() {
		return fsUri;
	}

	public void setFsUri(String fsUri) {
		this.fsUri = fsUri;
	}

	public String getRollover() {
		return rollover;
	}

	public void setRollover(String rollover) {
		this.rollover = rollover;
	}

	public FileSplitAndHDFSSinkTasklet() {
		super();
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		final JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
		final ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

		logger.info("log Starting DRAM file splitter");
		System.out.println("Starting DRAM file splitter");
		System.out.println("infile:"+ infile);
		System.out.println("outdir:"+ outdir);
		System.out.println("fsUri:"+fsUri);
		System.out.println("rollover:"+rollover);

		if (jobParameters != null && !jobParameters.isEmpty()) {
			final Set<Entry<String, JobParameter>> parameterEntries = jobParameters.getParameters().entrySet();
			System.out.println(String.format("The following %s Job Parameter(s) is/are present:", parameterEntries.size()));

			//Parameter name: absoluteFilePath; isIdentifying: true; type: STRING; value: /Users/kimm5/_dev/skhynix/testdata/sample2
			for (Entry<String, JobParameter> jobParameterEntry : parameterEntries) {
				System.out.println(String.format(
						"\tParameter name: %s; isIdentifying: %s; type: %s; value: %s",
						jobParameterEntry.getKey(),
						jobParameterEntry.getValue().isIdentifying(),
						jobParameterEntry.getValue().getType().toString(),
						jobParameterEntry.getValue().getValue()));

				if (jobParameterEntry.getKey().startsWith("context")) {
					stepExecutionContext.put(jobParameterEntry.getKey(), jobParameterEntry.getValue().getValue());
				}

				if("absoluteFilePath".equals(jobParameterEntry.getKey())){
					infile =jobParameterEntry.getValue().getValue().toString();
					System.out.println("absoluteFilePath:"+ infile);
				}

			}

			if (jobParameters.getString("throwError") != null
					&& Boolean.TRUE.toString().equalsIgnoreCase(jobParameters.getString("throwError"))) {

				if (this.counter.compareAndSet(3, 0)) {
					System.out.println("Counter reset to 0. Execution will succeed.");
				}
				else {
					this.counter.incrementAndGet();
					throw new IllegalStateException("Exception triggered by user.");
				}

			}



			run(infile, outdir, fsUri, rollover);
		}
		return RepeatStatus.FINISHED;
	}


	private void run(String infilepath, String outdirpath, String fsUri, String rollover) throws Exception{

		(new DataFileSplit()).execute(infilepath, outdirpath, fsUri, rollover);

	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// To make the job execution fail, set the step execution to fail
		// and return failed ExitStatus
		// stepExecution.setStatus(BatchStatus.FAILED);
		// return ExitStatus.FAILED;
		return ExitStatus.COMPLETED;
	}




}