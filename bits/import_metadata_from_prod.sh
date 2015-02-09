ssh metadata.turbointernational.com \
  mysqldump \
    --single-transaction -R \
    -h metaserverdb1.cslctedjmc6k.us-east-1.rds.amazonaws.com \
    -umetaserver -p metadata \
  | sed -e 's/DEFINER[ ]*=[ ]*[^*]*\*/\*/' \
  | mysql -u root -h 192.168.42.10 metadata

