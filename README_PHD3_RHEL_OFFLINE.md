
PHD를 RHEL에 Offline 설치시 depencency목록
# S/W버젼
RHEL6.5
PHD-3.0.1.0
PHD-UTILS-1.1.0.20

# yum.repo 설치 방법
1) 첨부의  phd3_rhel_rpms.tar.gz를 압축을 풉니다.
2) phd3_rhel_rpms/rpms폴더를  /var/www/html폴더 아래로 복사합니다.
3) phd3_rhel_rpms/yum.repo.d/rpms.repo를 /etc/yum.repo.d/아래에 복사.
4) rpms.repo의 nodeIP를 변경합니다.
[rpms]
name=rpms
baseurl=http://node1/rpms
gpgcheck=0

5) yum start
   service httpd restart
6) yum repo update
   rpm이 추가될때마다 모든 노드에서
   yum clean all
   yum repolist
7) 목록에 rpms.repo가 보여야합니다.
   network offline상태에서 rpm이 rpms.repo에서 검색되는지 확인.
   yum search nc

이제 offline설치가 가능합니다.

#  create repo
phd3_rhel_rpms.tar.gz에는 이미 작업이 되어서 이 작업은 할필요 없습니다.
yum repository를 메타정보를 생성하는 방법입니다.
rpm이 추가/변경되면 다시 실행해야합니다.
1) login as root
2) yum install createrepo
3) mkdir /var/www/html/rpms
4) cd /var/www/html
5) createrepo -p rpms
생성이 되었는지 확인합니다.
6) ls -al ./rpms
==> /var/www/html/rpms/repodata/repomd.xml

# rpm download only
yum install yum-downloadonly
yum reinstall —downloadonly —downloaddir=. gcc




# depencency 목록
./ambari-server-1.7.1-88.noarch.rpm
./apache-tomcat-apis-0.1-1.el6.noarch.rpm
./apr-devel-1.3.9-5.el6_2.x86_64.rpm
./axis-1.2.1-7.5.el6_5.noarch.rpm
./bcel-5.2-7.2.el6.x86_64.rpm
./classpathx-jaf-1.0-15.4.el6.x86_64.rpm
./classpathx-mail-1.1.1-9.4.el6.noarch.rpm
./createrepo-0.9.9-22.el6.noarch.rpm
./expat-devel-2.0.1-11.el6_2.x86_64.rpm
./gcc-4.4.7-16.el6.x86_64.rpm
./gd-2.0.35-11.el6.x86_64.rpm
./geronimo-specs-1.0-3.5.M2.el6.noarch.rpm
./geronimo-specs-compat-1.0-3.5.M2.el6.noarch.rpm
./hawq-plugin-1.3.0-152.noarch.rpm
./jakarta-commons-discovery-0.4-5.4.el6.noarch.rpm
./jakarta-commons-httpclient-3.1-0.9.el6_5.x86_64.rpm
./jakarta-commons-logging-1.0.4-10.el6.noarch.rpm
./java-1.5.0-gcj-1.5.0.0-29.1.el6.x86_64.rpm
./java_cup-0.10k-5.el6.x86_64.rpm
./libgcj-4.4.7-16.el6.x86_64.rpm
./libXpm-3.5.10-2.el6.x86_64.rpm
./log4j-1.2.14-6.4.el6.x86_64.rpm
./mx4j-3.0.1-9.13.el6.noarch.rpm
./mysql-5.1.73-5.el6_6.x86_64.rpm
./mysql-connector-java-5.1.17-6.el6.noarch.rpm
./mysql-libs-5.1.73-5.el6_6.x86_64.rpm
./mysql-server-5.1.73-5.el6_6.x86_64.rpm
./nc-1.84-24.el6.x86_64.rpm
./openssl-1.0.1e-42.el6.x86_64.rpm
./perl-DBD-MySQL-4.013-3.el6.x86_64.rpm
./perl-Digest-HMAC-1.01-22.el6.noarch.rpm
./perl-Digest-SHA1-2.12-2.el6.x86_64.rpm
./php-5.3.3-46.el6_6.x86_64.rpm
./php-cli-5.3.3-46.el6_6.x86_64.rpm
./php-common-5.3.3-46.el6_6.x86_64.rpm
./php-gd-5.3.3-46.el6_6.x86_64.rpm
./postgresql-8.4.20-3.el6_6.x86_64.rpm
./postgresql-libs-8.4.20-3.el6_6.x86_64.rpm
./postgresql-server-8.4.20-3.el6_6.x86_64.rpm
./python-rrdtool-1.4.5-1.el6.x86_64.rpm
./regexp-1.5-4.4.el6.x86_64.rpm
./ruby-1.8.7.374-4.el6_6.x86_64.rpm
./ruby-libs-1.8.7.374-4.el6_6.x86_64.rpm
./sinjdoc-0.5-9.1.el6.x86_64.rpm
./slf4j-1.5.8-8.el6.noarch.rpm
./snappy-1.1.0-1.el6.x86_64.rpm
./snappy-devel-1.1.0-1.el6.x86_64.rpm
./wsdl4j-1.5.2-7.8.el6.noarch.rpm
./xml-commons-apis-1.3.04-3.6.el6.x86_64.rpm
./xml-commons-resolver-1.1-4.18.el6.x86_64.rpm
./yum-3.2.29-69.el6.noarch.rpm
