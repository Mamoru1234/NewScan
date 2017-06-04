#!/usr/bin/env bash

apt-get install --yes python-software-properties
add-apt-repository ppa:webupd8team/java
apt-get update -qq
echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
echo debconf shared/accepted-oracle-license-v1-1 seen true | /usr/bin/debconf-set-selections
apt-get install --yes oracle-java8-installer
yes "" | apt-get -f install


apt-get install postgresql -y

echo "-------------------- fixing postgres pg_hba.conf file"
# replace the ipv4 host line with the above line
cat >> /etc/postgresql/9.3/main/pg_hba.conf <<EOF
# Accept all IPv4 connections - FOR DEVELOPMENT ONLY!!!
host    all         all         0.0.0.0/0             md5
EOF

echo "-------------------- creating qualification database"

# Create semi_final database
su postgres -c "createdb -E UTF8 -T template0 --locale=en_US.utf8 semi_final"

echo "Changing password..."
su postgres -c "psql -c \"ALTER USER postgres PASSWORD 'NewsScan2017'\""

cd /vagrant/
./gradlew :bootRun
