#!/bin/bash

METADATA_SSH=metadata@store.turbointernational.com
MAGMI_DIR=/var/www/magmi
METADATA_IMAGES=/var/product_images/resized
MAGENTO_IMAGES=/var/www/media/import

echo "[`date`] Synchronizing metadata images"
rsync -ar --delete $METADATA_IMAGES/* $METADATA_SSH:$MAGENTO_IMAGES/ || exit 1

echo "[`date`] Exporting /tmp/products.csv.gz"
wget -qO - http://localhost:8080/magmi/products | gzip > /tmp/products.csv.gz || exit 1

echo "[`date`] Copying to $METADATA_SSH:$MAGMI_DIR/import/products.csv.gz"
scp -q /tmp/products.csv.gz $METADATA_SSH:$MAGMI_DIR/import/products.csv.gz || exit 1

echo "[`date`] Decompressing"
ssh -q $METADATA_SSH "gunzip -f $MAGMI_DIR/import/products.csv.gz" || exit 1

echo "[`date`] Starting import"
ssh -q $METADATA_SSH "php $MAGMI_DIR/cli/magmi.cli.php -profile=complete -mode=create -CSV:filename=$MAGMI_DIR/import/products.csv" || exit 1

