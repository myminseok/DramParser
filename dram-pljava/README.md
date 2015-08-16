
# pl/java module for dram test data
!! this module is for test only. it consumes too much memory.



# java file 배포경로

scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/dram-pljava/target/dram-pljava-0.1-SNAPSHOT.jar .
scp dram-pljava-0.1-SNAPSHOT.jar root@phd1:/usr/local/hawq/lib/postgresql/java
scp dram-pljava-0.1-SNAPSHOT.jar root@phd2:/usr/local/hawq/lib/postgresql/java

scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/dram-pxf/target/dram-pxf-3.0.0.0-18-SNAPSHOT.jar .
scp dram-pxf-3.0.0.0-18-SNAPSHOT.jar root@phd1:/usr/local/hawq/lib/postgresql/java
scp dram-pxf-3.0.0.0-18-SNAPSHOT.jar root@phd2:/usr/local/hawq/lib/postgresql/java


scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/springxd-dramparser/target/springxd-dramparser-1.0.0-SNAPSHOT.jar .
scp springxd-dramparser-1.0.0-SNAPSHOT.jar root@phd1:/usr/local/hawq/lib/postgresql/java
scp springxd-dramparser-1.0.0-SNAPSHOT.jar root@phd2:/usr/local/hawq/lib/postgresql/java


/usr/local/hawq/lib/postgresql/java

tail -f /var/gphd/pxf/pxf-service/logs/*

tail -f /data/hawq/master/gpseg-1/pg_log/*

find /data/hawq/ | grep pg_log | xargs tail -f


## set classpath

gpconfig -c pljava_classpath -v 'dram-pljava-0.1-SNAPSHOT.jar:springxd-dramparser-1.0.0-SNAPSHOT.jar' 
gpconfig -s pljava_classpath

gpconfig -c log_min_messages -v \'info\';
gpconfig -s log_min_messages

it will make change to: /data/hawq/master/gpseg-1/postgresql.conf


# hawq stop / start
source /usr/local/hawq/greenplum_path.sh
gpstop -ar



# test on psql

set log_min_messages=info;  -- XXX

set search_path=javatest,public;
show search_path;

set pljava_classpath='dram-pljava-0.1-SNAPSHOT.jar:springxd-dramparser-1.0.0-SNAPSHOT.jar';
show pljava_classpath;

select set_config('pljava_classpath', 'dram-pljava-0.1-SNAPSHOT.jar:springxd-dramparser-1.0.0-SNAPSHOT.jar', true);
show pljava_classpath;
  


DROP SCHEMA javatest CASCADE;
CREATE SCHEMA javatest;



## dummy pl/java

drop function javatest.executeSelect(varchar);
CREATE FUNCTION javatest.executeSelect(varchar)
  RETURNS SETOF VARCHAR
  AS 'org.postgresql.dummy.ResultSetTest.executeSelect'
  LANGUAGE java;
  
  
### pxf


CREATE FUNCTION pljava_call_handler()  RETURNS language_handler AS 'pljava' LANGUAGE C;
CREATE FUNCTION pljavau_call_handler() RETURNS language_handler AS 'pljava' LANGUAGE C;
CREATE TRUSTED LANGUAGE java HANDLER pljava_call_handler;
CREATE LANGUAGE javaU HANDLER pljavau_call_handler;


drop external table javatest.dram;
CREATE EXTERNAL TABLE javatest.dram ( key TEXT, serial FLOAT, bits BYTEA )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=dram')
FORMAT 'custom' (Formatter='pxfwritable_import');


drop external table javatest.dram;
CREATE EXTERNAL TABLE javatest.dram ( key TEXT, serial FLOAT, bits BYTEA )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/testdata/*?Profile=dram')
FORMAT 'custom' (Formatter='pxfwritable_import');


### pl/java

drop function javatest.executeSelect(varchar);
CREATE FUNCTION javatest.executeSelect(varchar)
  RETURNS SETOF VARCHAR
  AS 'org.postgresql.dram.ResultSetStateMachine.executeSelect'
  LANGUAGE java;



 
SELECT javatest.executeSelect('select * from javatest.dram group by key, serial,bits order by key, serial');
 

