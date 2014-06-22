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

  $METADTAIPADDR = "192.168.42.10"
  $MAGENTOIPADDR = "192.168.42.11"
  #
  # View the documentation for the provider you're using for more
  # information on available options.

  config.vm.define "metadata" do |metadata|

    metadata.vm.hostname = "metadata.ti.dev"

    # Create a private network, which allows host-only access to the machine
    # using a specific IP.
    config.vm.network "private_network", ip: $METADATAIPADDR

    config.vm.provision "shell", privileged: false, inline: <<SCRIPT

      echo Installing packages

      # For elasticsearch
      wget -qO - http://packages.elasticsearch.org/GPG-KEY-elasticsearch | sudo apt-key add -
      sudo bash -c "echo deb http://packages.elasticsearch.org/elasticsearch/1.2/debian stable main >> /etc/apt/sources.list"

      # For yeoman / angular
      sudo add-apt-repository ppa:chris-lea/node.js -y

      sudo apt-get update
      sudo bash -c "DEBIAN_FRONTEND=noninteractive apt-get install -y python-software-properties python g++ make nodejs openjdk-7-jdk maven2 tomcat7 elasticsearch mysql-server-5.6"

      # We don't need these memory hogs while we setup, we'll turn them on later
      sudo service mysql stop
      sudo service tomcat7 stop

      echo Buliding and Installing Metadata Webapp

      # Mas90 library
      cd /vagrant/mas90magmi
      mvn clean install -DskipTests

      # Use the local DB
      sudo bash -c "echo 'database.host=localhost' > ~tomcat7/ti_metadata.properties"
      sudo bash -c "echo 'database.password=metaserver' >> ~tomcat7/ti_metadata.properties"

      # Prereqs for metadata
      cd /vagrant/metadata
      sudo npm install -g yo grunt bower generator-angular
      sudo gem install compass
      sudo rm -rf /var/lib/tomcat7/webapps/ROOT
      nice mvn clean install && sudo cp target/metadata.war /var/lib/tomcat7/webapps/ROOT.war

      echo Preparing Elasticsearch
      cd /vagrant/bits/ElasticSearch
      bash create_index.sh

      echo Final preparations for metadata
      sudo mkdir -p /var/product_images
      sudo chown tomcat7:tomcat7 /var/product_images
      sudo usermod -aG tomcat7 vagrant
      ln -s /var/log/tomcat7/catalina.out ~/tomcat7.log
 
      mysql -u root -e "CREATE DATABASE IF NOT EXISTS metadata;"
      mysql -u root -e "GRANT ALL PRIVILEGES ON metadata.* TO metaserver@'%' IDENTIFIED BY 'metaserver'; FLUSH PRIVILEGES;"

      sudo service mysql restart
      sudo service tomcat7 restart

      echo "=================================================" >&2
      echo "= REQUIRED NEXT STEPS:                           " >&2
      echo "=                                                " >&2
      echo "=   1. Load mysqldump (with -R) then:            " >&2
      echo "= /vagrant/bits/ElasticSearch/index_all_parts.sh " >&2
      echo "= /vagrant/bits/rebuild_bom_ancestry.sh          " >&2
      echo "=                                                " >&2
      echo "=   2. Copy images to:                           " >&2
      echo "= /var/product_images/                           " >&2
      echo "=                                                " >&2
      echo "=   3. Install mas90 db to (required to export): " >&2
      echo "= /var/mas90.accdb                               " >&2
      echo "=                                                " >&2
      echo "= Login to the metadata instance with:           " >&2
      echo "= vagrant ssh metadata                           " >&2
      echo "= mysql -u root metadata # no password           " >&2
      echo "= mysql -u metaserver -pmetaserver metadata      " >&2
      echo "=                                                " >&2
      echo "= The metadata server is available at:           " >&2
      echo "=   http://$METADATAIPADDR:8080/                 " >&2
      echo "=                                                " >&2
      echo "= You need data before you can login!            " >&2
      echo "=================================================" >&2

SCRIPT

  end

  config.vm.define "magento" do |magento|
    magento.vm.hostname = "magento.ti.dev"
    config.vm.provision "shell", inline: <<SCRIPT

      echo Installing packages
      export DEBIAN_FRONTEND=noninteractive
      sudo apt-get update
      sudo apt-get install -y apache2 mysql-server

      echo Installing Magento
      ln -s /vagrant/magento /var/TurboInternational
      ln -s /var/TurboInternational/magento /var/www

SCRIPT


  end


end
