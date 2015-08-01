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
import org.springframework.xd.module.options.spi.ModuleOption;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class DRAMFileParserTasklet implements Tasklet, StepExecutionListener {

	private volatile AtomicInteger counter = new AtomicInteger(0);

	private String infilepath=null;
	private String outdirpath=null;
	private String outfileextension=null;

	public String getInfilepath() {
		return infilepath;
	}

	public String getOutdirpath() {
		return outdirpath;
	}

	public String getOutfileextension() {
		return outfileextension;
	}

	public void setInfilepath(String infilepath) {
		this.infilepath = infilepath;
	}

	public void setOutdirpath(String outdirpath) {
		this.outdirpath = outdirpath;
	}

	public void setOutfileextension(String extension) {
		this.outfileextension = extension;
	}



	public DRAMFileParserTasklet() {
		super();
	}

	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		final JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
		final ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

		System.out.println("Starting DRAM data parser");
		System.out.println("infilepath:"+infilepath);
		System.out.println("outdirpath:"+outdirpath);
		System.out.println("outfileextension:"+outfileextension);

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

//				if("infilepath".equals(jobParameterEntry.getKey())){
//					infilepath=jobParameterEntry.getValue().getValue().toString();
//					System.out.println("jobparam:infilepath:"+infilepath);
//				}

				if("absoluteFilePath".equals(jobParameterEntry.getKey())){
					infilepath=jobParameterEntry.getValue().getValue().toString();
					System.out.println("jobparam: infilepath(absoluteFilePath):"+infilepath);
				}

//				if("outdirpath".equals(jobParameterEntry.getKey())){
//					outdirpath=jobParameterEntry.getValue().getValue().toString();
//					System.out.println("jobparam:outdirpath:"+outdirpath);
//				}
//
//				if("outfileextension".equals(jobParameterEntry.getKey())){
//					outfileextension=jobParameterEntry.getValue().getValue().toString();
//					System.out.println("jobparam:outfileextension:"+outfileextension);
//				}
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



			run(infilepath, outdirpath, outfileextension);
		}
		return RepeatStatus.FINISHED;
	}


	private void run(String infilepath, String outdirpath, String outfileextension) throws Exception{

		(new Parser(infilepath, outdirpath,outfileextension)).execute();

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