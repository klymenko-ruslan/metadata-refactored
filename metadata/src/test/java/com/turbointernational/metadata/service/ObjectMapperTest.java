package com.turbointernational.metadata.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

/**
 * A test suite to research various features of the ObjectMapper library.
 *
 * @author dmytro.trunykov@zorallabs.com
 */
public class ObjectMapperTest {

    public static class Address {

        private String zip;

        private String other;

        public Address(String zip, String other) {
            this.zip = zip;
            this.other = other;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }

    }

    public static class Person {

        private String firstName;
        private String lastName;
        private Address address;

        public Person(String firstName, String lastName, Address address) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

    }

    public static class Commodity {

        private String name;

        private String sku;

        private int no;

        public Commodity(String name, String sku, int no) {
            super();
            this.setName(name);
            this.setSku(sku);
            this.setNo(no);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

    }

    public static class Order {

        private Integer orderNo;

        private Person person;

        private Commodity[] commodities;

        public Order(Integer orderNo, Person person, Commodity[] commodities) {
            super();
            this.orderNo = orderNo;
            this.person = person;
            this.commodities = commodities;
        }

        public Integer getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Integer orderNo) {
            this.orderNo = orderNo;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public Commodity[] getCommodities() {
            return commodities;
        }

        public void setCommodities(Commodity[] commodities) {
            this.commodities = commodities;
        }

    }

    public static class CommodityDto {

        private String name;

        private String sku;

        public CommodityDto() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

    }

    public static class PackageDto {

        private Integer orderNo;

        private String customerFirstName;

        private String customerLastName;

        private String address;

        private String comments;

        private CommodityDto[] commodities;

        public PackageDto() {
        }

        public Integer getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(Integer orderNo) {
            this.orderNo = orderNo;
        }

        public String getCustomerFirstName() {
            return customerFirstName;
        }

        public void setCustomerFirstName(String customerFirstName) {
            this.customerFirstName = customerFirstName;
        }

        public String getCustomerLastName() {
            return customerLastName;
        }

        public void setCustomerLastName(String customerLastName) {
            this.customerLastName = customerLastName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public CommodityDto[] getCommodities() {
            return commodities;
        }

        public void setCommodities(CommodityDto[] commodities) {
            this.commodities = commodities;
        }

    }

    @Test
    public void testModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setImplicitMappingEnabled(false);
        modelMapper.addConverter(new AbstractConverter<Address, String>() {

            @Override
            protected String convert(Address addr) {
                return addr.getZip() + ", " + addr.getOther();
            }

        });
        modelMapper.createTypeMap(Commodity.class, CommodityDto.class)
                .addMapping(Commodity::getSku, CommodityDto::setSku)
                .addMapping(Commodity::getName, CommodityDto::setName);
        modelMapper.createTypeMap(Order.class, PackageDto.class).addMapping(Order::getOrderNo, PackageDto::setOrderNo)
                .addMapping(Order::getCommodities, PackageDto::setCommodities)
                .addMappings(mapper -> mapper.map(order -> order.getPerson().getFirstName(),
                        PackageDto::setCustomerFirstName))
                .addMappings(
                        mapper -> mapper.map(order -> order.getPerson().getLastName(), PackageDto::setCustomerLastName))
                .addMappings(mapper -> mapper.map(order -> order.getPerson().getAddress(), PackageDto::setAddress));
        Person johnSmith = new Person("John", "Smith", new Address("1279", "54, Baker street, London"));
        Order order = new Order(202, johnSmith, new Commodity[] {
                new Commodity("Dickies Men's 874 Traditional Work Pants", "35945920", 1),
                new Commodity("Fruit of the Loom Womens Be-Low No Show Socks, 2 Pairs", "24726588", 2),
                new Commodity("Azzuro Men's Classic Notch Lapel Suiting Blazer (Size S / 36)", "45915478", 5) });
        PackageDto packageDto = new PackageDto();
        modelMapper.map(order, packageDto);
        assertEquals("John", packageDto.getCustomerFirstName());
        assertEquals("Smith", packageDto.getCustomerLastName());
        assertEquals("1279, 54, Baker street, London", packageDto.getAddress());
        assertEquals((Integer) 202, packageDto.getOrderNo());
        assertNotNull(packageDto.getCommodities());
        assertEquals(3, packageDto.getCommodities().length);
        assertEquals("Dickies Men's 874 Traditional Work Pants", packageDto.getCommodities()[0].getName());
        assertEquals("35945920", packageDto.getCommodities()[0].getSku());
        assertEquals("Fruit of the Loom Womens Be-Low No Show Socks, 2 Pairs",
                packageDto.getCommodities()[1].getName());
        assertEquals("24726588", packageDto.getCommodities()[1].getSku());
        assertEquals("Azzuro Men's Classic Notch Lapel Suiting Blazer (Size S / 36)",
                packageDto.getCommodities()[2].getName());
        assertEquals("45915478", packageDto.getCommodities()[2].getSku());
    }

}
