# project for DRAM test data


# build

## dependency
https://github.com/Pivotal-Field-Engineering/pmr-common.git

mvn clean package -Dmaven.test.skip=true

# deploy

1) dram-xdmodule, injest dram test data to spring-xd
2) deploy dram-pxf to hawq, run pxf.


