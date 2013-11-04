#!/usr/bin/env ruby

require 'csv';

CSV.foreach('price_import.csv') do |row|
  print "INSERT INTO `mas90_std_price` (`ItemNumber`, `StdPrice`) VALUES('#{row[0]}', '#{row[1]}');\n"
end
