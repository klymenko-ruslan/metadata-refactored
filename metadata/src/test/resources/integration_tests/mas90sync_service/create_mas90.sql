create table productLine_to_parttype_value (
    ProductLineCode nvarchar(50) not null,
    part_type_value nvarchar(50) not null,
    constraint PK_productLine_to_parttype_value primary key (ProductLineCode,part_type_value)
);

create index PK_productLine_to_parttype_value ON productLine_to_parttype_value (ProductLineCode,part_type_value);

create table ci_item(
    itemcode varchar(30) not null,
    itemcodedesc varchar(30),
    productline varchar(4),
    producttype varchar(1)
);

create table bm_billdetail(
    billno varchar(30),
    componentitemcode varchar(30),
    quantityperbill decimal,
    revision varchar(3)
);

create table bm_billheader(
    billno  varchar(30),
    revision varchar(3)
);

