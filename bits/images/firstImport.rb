#!/usr/bin/env ruby

require 'mysql';

hostname = "localhost"
username = "ti"
password = "ti"
database = "ti"
location = "/home/jrodriguez/Projects/workshop/TurboInternational/images/"

ti_manufacturer_id = 11;

# Connect
db = Mysql.real_connect(hostname, username, password, database)

# Get a list of part IDs by part number for TI
partIds = {}
db.query("SELECT id, manfr_part_num FROM part WHERE manfr_id = 11 AND manfr_part_num IS NOT NULL").each do |part|
  partIds[part[1]] = part[0]
end

# Build a list of images for the parts
re = /(\d-[a-z]-\d{4}*) ?([a-z]*).jpg/i
Dir.foreach(location) do |file|
  matches = re.match(file)

  if (matches != nil) then

    partNumber = matches.captures[0];
    partId = partIds[partNumber];

    if (partId != nil) then
      puts "REPLACE INTO product_image (part_id, filename) VALUES(" + partId + ", '" + file + "');\n"
    end
  end
end


