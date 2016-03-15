insert into ci_item(itemcode, itemcodedesc, productline, producttype) values
('14-A-5383', 'CHRA & Nozzle Ring assy, GT174', 'CHR', 'F');


insert into bm_billdetail(billno, revision, componentitemcode, quantityperbill) values
('14-A-5383', '000', '1-A-3246', 1),
('14-A-5383', '000', '9-A-4340', 1);

insert into bm_billheader(billno, revision) values ('14-A-5383', '000');
