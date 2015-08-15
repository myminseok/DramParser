
# PXF mdule for dram test data

# upload pxf module
on all hawq node


## copy jar:
/usr/lib/gphd/pxf/dram-pxf-3.0.0.0-18-SNAPSHOT.jar 

#
vi /etc/gphd/pxf/conf/pxf-private.classpath

/usr/lib/gphd/pxf/pmr-common-2.0.1.0-1.jar
/usr/lib/gphd/pxf/dram-pxf-3.0.0.0-18-SNAPSHOT.jar
/usr/lib/gphd/pxf/springxd-dramparser-1.0.0-SNAPSHOT.jar

## edit profiles
vi /etc/gphd/pxf/conf/pxf-profiles.xml

```

<profile>
<name>DramWholeFilePipesi2</name>
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
        <name>Dram</name>
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
                <accessor>com.pivotal.pxf.plugins.dramsm.DramBlobAccessor</accessor>
                <resolver>com.pivotal.pxf.plugins.dramsm.DramResolver</resolver>
                <linebyline>false</linebyline>
        </plugins>
</profile>


```

3) restart pxf.
service pxf-service restart

4) debug
tail -f /var/gphd/pxf/pxf-service/logs/*





# upload test data file to hdfs :
 
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
 
 

# on hawq master node


scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/dram-pxf/target/dram-pxf-3.0.0.0-18-SNAPSHOT.jar .
scp dram-pxf-3.0.0.0-18-SNAPSHOT.jar root@phd1:/usr/lib/gphd/pxf/
scp dram-pxf-3.0.0.0-18-SNAPSHOT.jar root@phd2:/usr/lib/gphd/pxf/


scp root@192.168.55.1:/Users/kimm5/_dev/DramParser/springxd-dramparser/target/springxd-dramparser-1.0.0-SNAPSHOT.jar .
scp springxd-dramparser-1.0.0-SNAPSHOT.jar root@phd1:/usr/lib/gphd/pxf/
scp springxd-dramparser-1.0.0-SNAPSHOT.jar root@phd2:/usr/lib/gphd/pxf/


scp *.jar root@phd2:/usr/lib/gphd/pxf/

drop external table javatest.dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=Dram')
FORMAT 'custom' (Formatter='pxfwritable_import');

select count(*) from javatest.dram;

select * from javatest.dram group by key, serial,bits order by key, serial limit 100;



drop external table javatest.dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=Dram')
FORMAT 'custom' (Formatter='pxfwritable_import');

select count(*) from javatest.dram;

select * from javatest.dram group by key, serial,bits order by key, serial limit 100;





### dumy query


CREATE EXTERNAL TABLE dummy_tbl (int1 integer, word text, int2 integer)
location
('pxf://localhost:51200/dummy_location?FRAGMENTER=com.pivotal.pxf.plugins.dummy.DummyFragmenter&ACCESSOR=com.pivotal.pxf.plugins.dummy.DummyAccessor&RESOLVER=com.pivotal.pxf.plugins.dummy.DummyResolver&ANALYZER=com.pivotal.pxf.plugins.dummy.DummyAnalyzer')
 format 'custom' (formatter = 'pxfwritable_import');


 CREATE WRITABLE EXTERNAL TABLE dummy_tbl_write (int1 integer, word text, int2 integer)
 location
 ('pxf://localhost:51200/dummy_location?ACCESSOR=com.pivotal.pxf.plugins.dummy.DummyAccessor&RESOLVER=com.pivotal.pxf.plugins.dummy.DummyResolver')
 format 'custom' (formatter = 'pxfwritable_export');
 　
 
 
 
 
## test query
 
/usr/local/hawq/bin/psql 
 

```
drop external table dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/*?Profile=Dram')
FORMAT 'custom' (Formatter='pxfwritable_import');

select count(*) from dram;

select * from dram group by key, serial,bits order by key, serial limit 100;

```

drop external table javatest.dram;
CREATE EXTERNAL TABLE dram ( key TEXT, serial FLOAT, bits TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?Profile=Dram')
FORMAT 'custom' (Formatter='pxfwritable_import');


drop external table javatest.dramsm;
CREATE EXTERNAL TABLE javatest.dramsm ( file TEXT, serial FLOAT, result TEXT )
LOCATION ('pxf://phd1.localdomain:51200/dramdata/sampledata/rawdata.txt.sample.0?FRAGMENTER=com.pivotal.pxf.plugins.dramsm.WholeFileFragmenter&ACCESSOR=com.pivotal.pxf.plugins.dramsm.DramBlobAccessor&RESOLVER=com.pivotal.pxf.plugins.dramsm.DramResolver')
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
 
 http://pivotal-field-engineering.github.io/pxf-field/index.html
 https://github.com/Pivotal-Field-Engineering/pxf-field
 http://pivotalhd-210.docs.pivotal.io/doc/2010/PXFInstallationandAdministration.html#PXFInstallationandAdministration-InstallingPXF
 http://hawq.docs.pivotal.io/docs-hawq/topics/PXFInstallationandAdministration.html
 http://pivotalhd-210.docs.pivotal.io/tutorial/getting-started/dataset.html
 http://pivotalhd-210.docs.pivotal.io/tutorial/getting-started/hawq/pxf-external-tables.html

