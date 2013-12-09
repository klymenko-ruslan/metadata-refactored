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
    <td>67.185.158.56</td>
  </tr>
  <tr>
    <td>Jeff HUB</td>
    <td>199.231.242.26</td>
  </tr>
  <tr>
    <td>Wesson Home</td>
    <td>50.152.248.117</td>
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
mas90.db.path=/var/local/mas90.accdb
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

MAS90 Price Sync
================
The metadata server will use the MAS90 access database in `/var/local/mas90.accdb`, this can be overridden with the `mas90.db.path` config property.

Prices are exported as part of the Magmi sync. Upload the latest file before sync and the new prices will be automatically exported.
