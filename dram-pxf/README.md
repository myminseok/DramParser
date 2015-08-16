# PXF module for DRAM test data


## dependency

```
    <dependency>
        <groupId>pivotal.io.batch</groupId>
        <artifactId>springxd-dramparser</artifactId>
        <version>1.0.0-SNAPSHOT</version>
	</dependency>
	<dependency>	
			<groupId>com.gopivotal</groupId>
        	<artifactId>pmr-common</artifactId>
        	<version>2.0.1.0-1</version>
    </dependency>
    <repositories>
		<repository>
			<id>org.springsource.maven.milestone</id>
			<name>SpringSource Maven Release Repository</name>
			<url>http://repo.springsource.org/libs-release</url>
		</repository>
		<repository>
			<id>bintray-bigdata</id>
			<url>https://dl.bintray.com/big-data/maven</url>
			<releases>
				<enabled>true</enabled>
			</releases>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
	</repositories>
```
	
https://github.com/Pivotal-Field-Engineering/pmr-common.git

## build
mvn clean package

## upload jar to all pxf(hawq) node:

```
/usr/lib/gphd/pxf/dram-pxf-1.0.0-SNAPSHOT.jar
/usr/lib/gphd/pxf/springxd-dramparser-1.0.0-SNAPSHOT.jar
/usr/lib/gphd/pxf/pmr-common-2.0.1.0-1.jar
```


```
scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/dram-pxf/target/dram-pxf-3.0.0.0-18-SNAPSHOT.jar .
scp dram-pxf-3.0.0.0-18-SNAPSHOT.jar root@phd1:/usr/lib/gphd/pxf/
scp dram-pxf-3.0.0.0-18-SNAPSHOT.jar root@phd2:/usr/lib/gphd/pxf/

scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/springxd-dramparser/target/springxd-dramparser-1.0.0-SNAPSHOT.jar .
scp springxd-dramparser-1.0.0-SNAPSHOT.jar root@phd1:/usr/lib/gphd/pxf/
scp springxd-dramparser-1.0.0-SNAPSHOT.jar root@phd2:/usr/lib/gphd/pxf/

scp *.jar root@phd2:/usr/lib/gphd/pxf/
```


## edit classpath file
vi /etc/gphd/pxf/conf/pxf-private.classpath


## edit profiles
vi /etc/gphd/pxf/conf/pxf-profiles.xml

```
<profile>
    <name>dummy</name>
    <description>A profile for PXF Pipes using the entire file as input
    </description>
    <plugins>
        <fragmenter>com.pivotal.pxf.plugins.dram.DummyFragmenter</fragmenter>
        <accessor>com.pivotal.pxf.plugins.dram.DummyAccessor</accessor>
        <resolver>com.pivotal.pxf.plugins.dram.DummyResolver</resolver>
        <analyzer>com.pivotal.pxf.plugins.dram.DummyAnalyzer</analyzer>
        <linebyline>false</linebyline>
    </plugins>
</profile>

<profile>
    <name>dram</name>
    <description>A profile for PXF Pipes using the entire file as input
    </description>
    <plugins>
        <fragmenter>com.pivotal.pxf.plugins.dram.WholeFileFragmenter</fragmenter>
        <accessor>com.pivotal.pxf.plugins.dram.DramBlobAccessor</accessor>
        <resolver>com.pivotal.pxf.plugins.dram.DramResolver</resolver>
        <linebyline>false</linebyline>
    </plugins>
</profile>

<profile>
    <name>dramsm</name>
    <description>A profile for PXF Pipes using the entire file as input
    </description>
    <plugins>
        <fragmenter>com.pivotal.pxf.plugins.dramsm.WholeFileFragmenter</fragmenter>
        <accessor>com.pivotal.pxf.plugins.dramsm.BlobAccessor</accessor>
        <resolver>com.pivotal.pxf.plugins.dramsm.BlobResolver</resolver>
        <linebyline>false</linebyline>
    </plugins>
</profile>

```

## restart pxf.
as root
service pxf-service restart

## debug
as root
tail -f /var/gphd/pxf/pxf-service/logs/*


# test

## dumy query


CREATE EXTERNAL TABLE dummy_tbl (int1 integer, word text, int2 integer)
location
('pxf://localhost:51200/dummy_location?FRAGMENTER=com.pivotal.pxf.plugins.dummy.DummyFragmenter&ACCESSOR=com.pivotal.pxf.plugins.dummy.DummyAccessor&RESOLVER=com.pivotal.pxf.plugins.dummy.DummyResolver&ANALYZER=com.pivotal.pxf.plugins.dummy.DummyAnalyzer')
 format 'custom' (formatter = 'pxfwritable_import');


 CREATE WRITABLE EXTERNAL TABLE dummy_tbl_write (int1 integer, word text, int2 integer)
 location
 ('pxf://localhost:51200/dummy_location?ACCESSOR=com.pivotal.pxf.plugins.dummy.DummyAccessor&RESOLVER=com.pivotal.pxf.plugins.dummy.DummyResolver')
 format 'custom' (formatter = 'pxfwritable_export');
 　
 
 
 

## upload test data file to hdfs :
 
 ```
 su - hdfs 
 hdfs dfs -mkdir /user/pxf
 hdfs dfs -put dramdata /user/pxf
 
 hdfs dfs -lsr /user/pxf
 lsr: DEPRECATED: Please use 'ls -R' instead.
 drwxr-xr-x   - hdfs hdfs          0 2015-08-13 06:46 /user/pxf/dramdata
 drwxr-xr-x   - hdfs hdfs          0 2015-08-13 06:46 /user/pxf/dramdata/sampledata
 -rw-r--r--   1 hdfs hdfs    4000000 2015-08-13 06:46 /user/pxf/dramdata/sampledata/rawdata.txt.sample.0
 -rw-r--r--   1 hdfs hdfs    4000000 2015-08-13 06:46 /user/pxf/dramdata/sampledata/rawdata.txt.sample.1
 -rw-r--r--   1 hdfs hdfs    4000000 2015-08-13 06:46 /user/pxf/dramdata/sampledata/rawdata.txt.sample.2
 ```
 

## test query
 
/usr/local/hawq/bin/psql 


drop external table javatest.dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/*?Profile=dram')
FORMAT 'custom' (Formatter='pxfwritable_import');


drop external table javatest.dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=dram')
FORMAT 'custom' (Formatter='pxfwritable_import');


drop external table javatest.dramsm;
CREATE EXTERNAL TABLE javatest.dramsm ( file TEXT, serial FLOAT, result TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?FRAGMENTER=com.pivotal.pxf.plugins.dramsm.WholeFileFragmenter&ACCESSOR=com.pivotal.pxf.plugins.dramsm.BlobAccessor&RESOLVER=com.pivotal.pxf.plugins.dramsm.BlobResolver')
FORMAT 'custom' (Formatter='pxfwritable_import');


drop external table javatest.dramsm;
CREATE EXTERNAL TABLE javatest.dramsm ( file TEXT, serial FLOAT, result TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=dramsm')
FORMAT 'custom' (Formatter='pxfwritable_import');


drop external table javatest.dramsm;
CREATE EXTERNAL TABLE javatest.dramsm ( file TEXT, serial FLOAT, result TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/testdata/*?Profile=dramsm')
FORMAT 'custom' (Formatter='pxfwritable_import');




select count(*) from javatest.dram;

select * from javatest.dram group by key, serial,bits order by key, serial limit 100;




 # ref
 ```
 http://pivotal-field-engineering.github.io/pxf-field/index.html
 https://github.com/Pivotal-Field-Engineering/pxf-field
 http://pivotalhd-210.docs.pivotal.io/doc/2010/PXFInstallationandAdministration.html#PXFInstallationandAdministration-InstallingPXF
 http://hawq.docs.pivotal.io/docs-hawq/topics/PXFInstallationandAdministration.html
 http://pivotalhd-210.docs.pivotal.io/tutorial/getting-started/dataset.html
 http://pivotalhd-210.docs.pivotal.io/tutorial/getting-started/hawq/pxf-external-tables.html
```
