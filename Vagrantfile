# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  config.vm.hostname = "metadata.ti.dev"

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "ubuntu/trusty64"

  # The url from where the 'config.vm.box' box will be fetched if it
  # doesn't already exist on the user's system.
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/trusty/current/trusty-server-cloudimg-amd64-vagrant-disk1.box"


  # If true, then any SSH connections made will enable agent forwarding.
  # Default value: false
  # config.ssh.forward_agent = true

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
    # Don't boot with headless mode
    #vb.gui = true

    # Use VBoxManage to customize the VM. For example to change memory:
    vb.customize ["modifyvm", :id, "--memory", "2048"]

    # Host-only and NAT
    vb.customize ["modifyvm", :id, "--nic1", "nat"]
    vb.customize ["modifyvm", :id, "--nic2", "hostonly"]

  end

  #
  # View the documentation for the provider you're using for more
  # information on available options.

  config.vm.define "metadata" do |metadata|

    metadata.vm.hostname = "metadata.ti.dev"

    # Create a private network, which allows host-only access to the machine
    # using a specific IP.
    config.vm.network "private_network", ip: "192.168.42.10"

    config.vm.provision "shell", privileged: false, inline: <<SCRIPT

      echo Installing packages

      # For elasticsearch
      sudo dpkg -i /vagrant/bits/ElasticSearch/elasticsearch-2.1.1.deb

      # This will install later versions. War and POM need to be updated accordingly, add elasticsearch to apt-get install below
      #wget -qO - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | sudo apt-key add -
      #sudo bash -c "echo deb http://packages.elasticsearch.org/elasticsearch/1.2/debian stable main >> /etc/apt/sources.list"

      # For yeoman / angular
      sudo add-apt-repository ppa:chris-lea/node.js -y

      sudo apt-get update
      sudo bash -c "DEBIAN_FRONTEND=noninteractive apt-get install -y python-software-properties python g++ make nodejs openjdk-7-jdk maven2 tomcat7 mysql-server-5.6 phantomjs"

      # We don't need these memory hogs while we setup, we'll turn them on later
      sudo service mysql stop
      sudo service tomcat7 stop

      # Increase tomcat memory
      sudo bash -c 'echo -e "\n# From Vagrantfile\nJAVA_OPTS=\"-Xmx2g\"\n" >> /etc/default/tomcat7'

      echo Buliding and Installing Metadata Webapp

      # Use the local DB
      sudo bash -c "echo 'database.host=localhost' > ~tomcat7/ti_metadata.properties"
      sudo bash -c "echo 'database.password=metaserver' >> ~tomcat7/ti_metadata.properties"

      # Prereqs for metadata
      cd /vagrant/metadata
      sudo npm install -g yo grunt bower generator-angular
      sudo npm install -g grunt-cli
      sudo apt-get install -y ruby-dev	
      sudo gem install compass
      sudo rm -rf /var/lib/tomcat7/webapps/ROOT
      mvn install:install-file -Dfile=bits/libs/sqljdbc_4.2/enu/sqljdbc42.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.2 -Dpackaging=jar
      nice mvn clean install -DskipTests=true && sudo cp target/metadata.war /var/lib/tomcat7/webapps/ROOT.war

      echo Final preparations for metadata
      sudo ln -s /vagrant/metadata/src/test/resources/mas90.accdb /var/mas90.accdb
      sudo mkdir -p /var/product_images
      sudo chown tomcat7:tomcat7 /var/product_images
      sudo usermod -aG tomcat7 vagrant
      sudo ln -s /var/log/tomcat7/catalina.out ~/tomcat7.log

      sudo service elasticsearch restart
      sudo service mysql start
      sudo service tomcat7 start

      sleep 5 # Elasticsearch can take a bit to start
      bash /vagrant/bits/ElasticSearch/create_index.sh

      mysql -u root -e "CREATE DATABASE IF NOT EXISTS metadata;"
      mysql -u root -e "GRANT ALL PRIVILEGES ON metadata.* TO metaserver@'%' IDENTIFIED BY 'metaserver'; FLUSH PRIVILEGES;"
      mysql -u root -e "GRANT ALL PRIVILEGES ON *.* TO root@'192.168.42.1'; FLUSH PRIVILEGES;"

      echo Preparing Selenium
      /vagrant/metadata/yo/node_modules/protractor/bin/webdriver-manager update
      /vagrant/metadata/yo/node_modules/protractor/bin/webdriver-manager start

      echo "=================================================================" >&2
      echo "= REQUIRED NEXT STEPS:                                           " >&2
      echo "=                                                                " >&2
      echo "=   1. Login to metadata instance:                               " >&2
      echo "= vagrant ssh metadata                                           " >&2
      echo "=                                                                " >&2
      echo "=   2. Load mysqldump (created with -R) then:                    " >&2
      echo "= mysql -u metaserver -pmetaserver metadata < /vagrant/DUMP_FILE " >&2
      echo "=                                                                " >&2
      echo "=   3. Index the parts and rebuild the BOM:                      " >&2
      echo "= /vagrant/bits/ElasticSearch/index_all_parts.sh                 " >&2
      echo "= /vagrant/bits/rebuild_bom_ancestry.sh                          " >&2
      echo "=                                                                " >&2
      echo "=   4. Copy images to:                                           " >&2
      echo "= /var/product_images/                                           " >&2
      echo "=                                                                " >&2
      echo "=   5. Install mas90.accdb to project root (required to export). " >&2
      echo "=                                                                " >&2
      echo "= mysql -u metaserver -pmetaserver metadata                      " >&2
      echo "=                                                                " >&2
      echo "= The metadata server is available at:                           " >&2
      echo "=   http://192.168.42.10:8080/                                   " >&2
      echo "=                                                                " >&2
      echo "= You need data before you can login!                            " >&2
      echo "=================================================================" >&2

SCRIPT

  end

  config.vm.define "magento" do |magento|
    magento.vm.hostname = "magento.ti.dev"

    # Create a private network, which allows host-only access to the machine
    # using a specific IP.
    config.vm.network "private_network", ip: "192.168.42.11"

    config.vm.synced_folder "magento", "/var/www", owner: "www-data", group: "www-data"

    config.vm.provision "shell", privileged: false, inline: <<SCRIPT

      echo Installing packages
      sudo apt-get update
      sudo bash -c "DEBIAN_FRONTEND=noninteractive apt-get install -y apache2 mysql-server-5.6 php5 php5-mysql php5-curl"

      mysql -u root -e "CREATE DATABASE IF NOT EXISTS magento;"
      mysql -u root -e "GRANT ALL PRIVILEGES ON magento.* TO magento@'localhost' IDENTIFIED BY 'OI1unqzzO{=.s#?'; FLUSH PRIVILEGES;"

      echo Installing Magento
      sudo a2enmod rewrite
      sudo bash -c 'cat > /etc/apache2/sites-enabled/000-default.conf <<APACHE
<VirtualHost *:80>
  ServerAdmin webmaster@localhost
  DocumentRoot /var/www
  <Directory />
    Options FollowSymLinks
    AllowOverride None
  </Directory>
  <Directory /var/www/>
    Options Indexes FollowSymLinks MultiViews
    AllowOverride All
    Order allow,deny
    allow from all
  </Directory>

  ErrorLog \${APACHE_LOG_DIR}/error.log
  CustomLog \${APACHE_LOG_DIR}/access.log combined
</VirtualHost>

# vim: syntax=apache ts=4 sw=4 sts=4 sr noet
APACHE'

      sudo service apache2 restart

      echo "=================================================================" >&2
      echo "= REQUIRED NEXT STEPS:                                           " >&2
      echo "=                                                                " >&2
      echo "=   1. Login to metadata instance with:                          " >&2
      echo "= vagrant ssh magento                                            " >&2
      echo "=                                                                " >&2
      echo "=   2. Load mysqldump:                                           " >&2
      echo "= mysql -u root magento < /vagrant/DUMP_FILE                     " >&2
      echo "=                                                                " >&2
      echo "=   3. Update magento URL:                                       " >&2
      echo "= bash /vagrant/bits/update_magento_url.sh http://192.168.42.11/ " >&2
      echo "=                                                                " >&2
      echo "=   4. Copy images from prod to:                                 " >&2
      echo "= /var/www/media/                                                " >&2
      echo "=                                                                " >&2
      echo "= The magento server is available at:                            " >&2
      echo "=   http://192.168.42.11/                                        " >&2
      echo "=                                                                " >&2
      echo "= You need data before you can login!                            " >&2
      echo "=================================================================" >&2


SCRIPT


  end


end
