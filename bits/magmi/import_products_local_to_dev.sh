#!/bin/sh

wget -q http://localhost:8080/magmi/products -O - | gzip > products.csv.gz && \
scp products.csv.gz ubuntu@ec2-54-204-208-6.compute-1.amazonaws.com: && \
ssh ubuntu@ec2-54-204-208-6.compute-1.amazonaws.com "gunzip -f products.csv.gz" && \
ssh ubuntu@ec2-54-204-208-6.compute-1.amazonaws.com "mv products.csv /var/www/magmi/import/products.csv" && \
ssh ubuntu@ec2-54-204-208-6.compute-1.amazonaws.com "sudo php /var/www/magmi/cli/magmi.cli.php -mode=create"

