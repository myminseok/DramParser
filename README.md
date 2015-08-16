# project for DRAM test data


# build

## dependency
https://github.com/Pivotal-Field-Engineering/pmr-common.git

mvn clean package -Dmaven.test.skip=true

# deploy
1) spring-xd : deploy springxd-drampraser to spring xd job.

2) deploy dram-pxf to hawq 

3) injest dram test data to spring-xd
