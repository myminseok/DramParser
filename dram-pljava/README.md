
# PXF mdule for dram test data
# hawq stop / start
source /usr/local/hawq/greenplum_path.sh
gpstop -ar


# java file 배포경로
/usr/local/hawq/lib/postgresql/java


tail -f /var/gphd/pxf/pxf-service/logs/*

tail -f /data/hawq/master/gpseg-1/pg_log/*


scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/dram-pljava/target/dram-pljava-0.1-SNAPSHOT.jar .
scp dram-pljava-0.1-SNAPSHOT.jar root@phd1:/usr/local/hawq/lib/postgresql/java
scp dram-pljava-0.1-SNAPSHOT.jar root@phd2:/usr/local/hawq/lib/postgresql/java



CREATE FUNCTION pljava_call_handler()  RETURNS language_handler AS 'pljava' LANGUAGE C;
CREATE FUNCTION pljavau_call_handler() RETURNS language_handler AS 'pljava' LANGUAGE C;
CREATE TRUSTED LANGUAGE java HANDLER pljava_call_handler;
CREATE LANGUAGE javaU HANDLER pljavau_call_handler;



DROP SCHEMA javatest CASCADE;
CREATE SCHEMA javatest;


drop external table javatest.dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=Dram')
FORMAT 'custom' (Formatter='pxfwritable_import');



set search_path=javatest,public;
set pljava_classpath='examples.jar';
set log_min_messages=info;  -- XXX


drop function javatest.executeSelect(varchar);
CREATE FUNCTION javatest.executeSelect(varchar)
  RETURNS SETOF VARCHAR
  AS 'org.postgresql.example.ResultSetTest.executeSelect'
  LANGUAGE java;

SELECT javatest.executeSelect('select * from javatest.dram group by key, serial,bits order by key, serial');

  
  select set_config('pljava_classpath', 'dram-pljava-0.1-SNAPSHOT.jar', true);
  
  
  
  
  /data/hawq/master/gpseg-1/postgresql.conf
  
  gpconfig -c pljava_classpath -v 'dram-pljava-0.1-SNAPSHOT.jar' 
  
  gpstop -r
  
  gpconfig -s pljava_classpath


  
  set search_path=javatest,public;
  set pljava_classpath='dram-pljava-0.1-SNAPSHOT.jar';

show pljava_classpath;


drop function javatest.executeSelect(varchar);
CREATE FUNCTION javatest.executeSelect(varchar)
  RETURNS SETOF VARCHAR
  AS 'org.postgresql.dram.ResultSetStateMachine.executeSelect'
  LANGUAGE java;

SELECT javatest.executeSelect('select * from javatest.dram group by key, serial,bits order by key, serial');

