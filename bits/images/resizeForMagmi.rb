#!/usr/bin/env ruby

require 'mysql';

hostname = "localhost"
username = "ti"
password = "ti"
database = "ti"
pathSrc = "/home/jrodriguez/Projects/workshop/TurboInternational/backups/images"
pathDst = "/home/jrodriguez/Projects/workshop/TurboInternational/backups/images/magmi"

# Make the directories
if !File.directory?("#{pathDst}")
  Dir.mkdir("#{pathDst}")
  Dir.mkdir("#{pathDst}/1000")
  Dir.mkdir("#{pathDst}/135")
  Dir.mkdir("#{pathDst}/50")
end

# Connect
db = Mysql.real_connect(hostname, username, password, database)

# Rename the files
puts "Moving source files to destination directory."
db.query("SELECT part_id, id, filename FROM product_image").each do |part|
  if File.exists?("#{pathSrc}/#{part[2]}")
    File.rename("#{pathSrc}/#{part[2]}", "#{pathDst}/#{part[0]}_#{part[1]}.jpg")
  end
end

# Resize them
puts "Generating 1000px images"
`mogrify -resize 1000x1000 -path #{pathDst}/1000 #{pathDst}/*.jpg`

puts "Generating 135px images"
`mogrify -resize 135x135   -path #{pathDst}/135  #{pathDst}/*.jpg`

puts "Generating 50px images"
`mogrify -resize 50x50     -path #{pathDst}/50   #{pathDst}/*.jpg`

