Developer IPs
=============
[What's My IP?](http://www.whatismyip.com/)
<table>
  <tr>
    <th>Name</th>
    <th>IP</th>
  </tr>
  <tr>
    <td>Jeff Home</td>
    <td>71.227.128.226 OLD: 67.185.158.56</td>
  </tr>
  <tr>
    <td>Jeff HUB</td>
    <td>199.231.242.26</td>
  </tr>
  <tr>
    <td>Wesson Home</td>
    <td>50.152.248.117</td>
  </tr>
  <tr>
    <td>Salman Home</td>
    <td>72.208.81.82</td>
  </tr>
</table>

Dev Servers
===========
Metadata: http://timetadata.herokuapp.com

Magento: ubuntu@ec2-54-204-208-6.compute-1.amazonaws.com

Magento Storefront: http://ec2-54-204-208-6.compute-1.amazonaws.com/

Magento Admin: http://ec2-54-204-208-6.compute-1.amazonaws.com/admin


Prod Servers
============
Metadata: http://metadata.turbointernational.com

Magento: http://54.204.39.114


Architecture
============

* TI Metadata Server
 * Spring MVC Backend
 * AngularJS Frontend
 * ElasticSearch
 * MAS90 Price Sync

* Magento

* Magmi


Metadata Server Configuration
=============================
The Metadata server uses java properties files for configuration. In the source, these are stored under `/metadata/src/main/java/resources/spring`. The webapp also looks in the [tomcat] user's home directory for `ti_metadata.properties` which can be used to override the default values. Here's a sample:

```
mas90.db.path=/var/mas90.accdb
elasticsearch.timeout=10000
elasticsearch.port=9300
elasticsearch.type=part
elasticsearch.index=metadata
elasticsearch.host=localhost
database.host=localhost
database.port=3306
database.schema=metadata_db
database.username=metadata_user
database.password=metadata_pass
```

ElasticSearch Metadata Search Engine
====================================
`bits/ElasticSearch` contains [Postman](https://chrome.google.com/webstore/detail/postman-rest-client/fdmmgilgnpjigdojojpjoooidkmcomcm) actions and environment settings. It also contains wget scripts for common scenarios.


MAS90 Price Sync
================
The metadata server will use the MAS90 access database in `/var/mas90.accdb`, this can be overridden with the `mas90.db.path` config property.

Prices are exported as part of the Magmi sync. Upload the latest file before sync and the new prices will be automatically exported.

Magmi
=====
Magmi is located at `http://magento.turbointernational.com/magmi/web/magmi.php`, it's files are in the `/var/www/magmi` on the production magento server. It is configured to connect to the magento MySQL server, and uses the permissions:
```
grant select,insert,update,delete on magento.* to magmi@'localhost' identified by 'magmi';
```

Magmi uses a CSV generated by the metadata server at `http://metadata.turbointernational.com:8080/magmi/products synchronize prices with the Magento database.

Access is controlled by IP address via .htaccess file at `/var/www/magmi/.htaccess` (no restart is required)

Magento
=======

Magento Indexing
================
Magento maintains index tables of rolled-up data. Rebuilding some of these indexes is slow - on the order of hours, others are on the order of minutes:

**Troubleshooting**
While indexing product prices, I encountered an error in the Magento exception log about foreign key constraint failure. The fix was to truncate the following tables:

* catalog_product_entity_group_price

* catalog_product_index_group_price

When Indexing URL rewrites failed, I truncated the core_url_rewrite table and was able to rebuild it. Sidenote: Doing this to a production server can break links for users - you're deleting all the rewrites that point to products. Magento will use generated URLs until it can reindex and rebuild the rewrites.


Installing Magento from Scratch
===============================
1. Install MySQL, Apache, and PHP. You'll be asked to specify a mysql root password. `sudo apt-get install -y mysql-server apache2 php5 php5-mysqlnd`
2. Setup MySQL to run off the 'ephemeral' SSD by changing the SSD's mount point from `/mnt` to `/var/lib/mysql`. You should move the MySQL data files before hiding the `/var/lib/mysql` directory behind a mount point.
3. Create the schema and load the latest MySQL dump (something like this): `mysql -p -u USER SCHEMA < dump.sql`
4. Set the new URL in the magento db: `update core_config_data set value = "http://ec2-54-197-140-225.compute-1.amazonaws.com/" where path like 'web/%/base_url';`
5. Copy `./magento/` from git to `/var/www` on the server, don't miss `.htaccess`!
6. Fix permissions: `sudo chown -R www-data:www-data /var/www`
6. Update `/var/www/app/etc/local.xml` with the MySQL server's info
7. Purge the cache: `rm -rf /var/www/var/cache/mage-*`
8. Restart Apache: `sudo /etc/init.d/apache2 restart`
