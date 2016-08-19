"use strict";

angular.module("mockApp")
.service("mockData", [function() {
  this.filteredParts = {
    "took" : 54,
    "timed_out" : false,
    "_shards" : {
      "total" : 5,
      "successful" : 5,
      "failed" : 0
    },
    "hits" : {
      "total" : 64179,
      "max_score" : 1.0,
      "hits" : [ {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "253",
        "_score" : 1.0,
        "_source":{"description":null,"id":253,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"444964-0007","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "254",
        "_score" : 1.0,
        "_source":{"description":null,"id":254,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"409043-0033","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "256",
        "_score" : 1.0,
        "_source":{"description":null,"id":256,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"431876-0057","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "259",
        "_score" : 1.0,
        "_source":{"description":null,"id":259,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"408105-0263","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "260",
        "_score" : 1.0,
        "_source":{"description":null,"id":260,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"408105-0426","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "264",
        "_score" : 1.0,
        "_source":{"description":null,"id":264,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"442620-0004","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "265",
        "_score" : 1.0,
        "_source":{"description":null,"id":265,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"443853-0021","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "270",
        "_score" : 1.0,
        "_source":{"description":null,"id":270,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"441398-0031","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "295",
        "_score" : 1.0,
        "_source":{"description":null,"id":295,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"408774-0034","name":null,"partType":{"id":2,"name":"Cartridge"}}
      }, {
        "_index" : "metadata",
        "_type" : "part",
        "_id" : "300",
        "_score" : 1.0,
        "_source":{"description":null,"id":300,"manufacturer":{"id":1,"name":"Garrett"},"manufacturerPartNumber":"409172-0105","name":null,"partType":{"id":2,"name":"Cartridge"}}
      } ]
    },
    "aggregations" : {
      "Turbo Model" : {
        "doc_count_error_upper_bound" : 5,
        "sum_other_doc_count" : 24037,
        "buckets" : [ {
          "key" : "HX40W",
          "doc_count" : 1010
        }, {
          "key" : "S2B",
          "doc_count" : 590
        }, {
          "key" : "S2A",
          "doc_count" : 460
        }, {
          "key" : "HX35W",
          "doc_count" : 436
        }, {
          "key" : "S400",
          "doc_count" : 393
        }, {
          "key" : "S3A",
          "doc_count" : 380
        }, {
          "key" : "H1C",
          "doc_count" : 295
        }, {
          "key" : "HX82",
          "doc_count" : 268
        }, {
          "key" : "H1E",
          "doc_count" : 262
        }, {
          "key" : "S1B",
          "doc_count" : 242
        }, {
          "key" : "S300G",
          "doc_count" : 238
        }, {
          "key" : "HX83",
          "doc_count" : 216
        }, {
          "key" : "S3B",
          "doc_count" : 212
        }, {
          "key" : "HX35",
          "doc_count" : 195
        }, {
          "key" : "HX50",
          "doc_count" : 193
        }, {
          "key" : "HE431VE",
          "doc_count" : 187
        }, {
          "key" : "HC5A",
          "doc_count" : 176
        }, {
          "key" : "HX55",
          "doc_count" : 170
        }, {
          "key" : "HX80",
          "doc_count" : 168
        }, {
          "key" : "HX30W",
          "doc_count" : 165
        }, {
          "key" : "S4T",
          "doc_count" : 162
        }, {
          "key" : "HX40",
          "doc_count" : 158
        }, {
          "key" : "S200G",
          "doc_count" : 156
        }, {
          "key" : "H2D",
          "doc_count" : 149
        }, {
          "key" : "GT4082",
          "doc_count" : 145
        }, {
          "key" : "WH1E",
          "doc_count" : 142
        }, {
          "key" : "HE221W",
          "doc_count" : 138
        }, {
          "key" : "HX55W",
          "doc_count" : 135
        }, {
          "key" : "S200",
          "doc_count" : 128
        }, {
          "key" : "TO4B59",
          "doc_count" : 121
        }, {
          "key" : "S1BG",
          "doc_count" : 119
        }, {
          "key" : "4LGZ",
          "doc_count" : 118
        }, {
          "key" : "HX60",
          "doc_count" : 116
        }, {
          "key" : "TD10",
          "doc_count" : 115
        }, {
          "key" : "TD13",
          "doc_count" : 113
        }, {
          "key" : "GT3782",
          "doc_count" : 109
        }, {
          "key" : "HE451VE",
          "doc_count" : 109
        }, {
          "key" : "GTA1749V",
          "doc_count" : 107
        }, {
          "key" : "S300",
          "doc_count" : 107
        }, {
          "key" : "WH1C",
          "doc_count" : 101
        }, {
          "key" : "GT6041",
          "doc_count" : 97
        }, {
          "key" : "GTA1749MV",
          "doc_count" : 92
        }, {
          "key" : "K31",
          "doc_count" : 91
        }, {
          "key" : "S100",
          "doc_count" : 91
        }, {
          "key" : "HE341VE",
          "doc_count" : 88
        }, {
          "key" : "GT1749V",
          "doc_count" : 86
        }, {
          "key" : "TDO5",
          "doc_count" : 86
        }, {
          "key" : "K27.2-3071OXCKB11.91RNAXD",
          "doc_count" : 84
        }, {
          "key" : "HX50W",
          "doc_count" : 78
        }, {
          "key" : "GT2556S",
          "doc_count" : 76
        }, {
          "key" : "H2A",
          "doc_count" : 74
        }, {
          "key" : "S2BG",
          "doc_count" : 74
        }, {
          "key" : "BV43-1874KXB419.18KVAXC",
          "doc_count" : 73
        }, {
          "key" : "HY40V",
          "doc_count" : 72
        }, {
          "key" : "K16-2471OYCKB/5.82",
          "doc_count" : 72
        }, {
          "key" : "R2S KP39 + K04",
          "doc_count" : 71
        }, {
          "key" : "S300G-83F33DCCZM/0.80DA1",
          "doc_count" : 69
        }, {
          "key" : "S410",
          "doc_count" : 69
        }, {
          "key" : "3LM",
          "doc_count" : 66
        }, {
          "key" : "H2C",
          "doc_count" : 66
        }, {
          "key" : "S300G-76ZD2/83F/36BZ-PM/0.83 VTF",
          "doc_count" : 66
        }, {
          "key" : "HX25W",
          "doc_count" : 65
        }, {
          "key" : "HY35W",
          "doc_count" : 65
        }, {
          "key" : "TDO8",
          "doc_count" : 65
        }, {
          "key" : "HX60W",
          "doc_count" : 64
        }, {
          "key" : "S4EV-087M92EN43C01BV-095N71Al16R796N",
          "doc_count" : 64
        }, {
          "key" : "R2S KP35 + K04",
          "doc_count" : 63
        }, {
          "key" : "GTB2260VK",
          "doc_count" : 62
        }, {
          "key" : "K27.2-3071OXCKB/11.91",
          "doc_count" : 62
        }, {
          "key" : "B2UV-63J89DR27C01BV 071N71Al17R78ES",
          "doc_count" : 61
        }, {
          "key" : "HE351VE",
          "doc_count" : 61
        }, {
          "key" : "S300V110",
          "doc_count" : 61
        }, {
          "key" : "S410G",
          "doc_count" : 61
        }, {
          "key" : "B2UV-63J89DR27C01BV 071N71AA17R78ES",
          "doc_count" : 59
        }, {
          "key" : "S2BS-001",
          "doc_count" : 58
        }, {
          "key" : "GTA4294",
          "doc_count" : 57
        }, {
          "key" : "4LF",
          "doc_count" : 55
        }, {
          "key" : "K31-3775XXCKB20.90DCAWD",
          "doc_count" : 55
        }, {
          "key" : "HE551VE",
          "doc_count" : 54
        }, {
          "key" : "HX35G",
          "doc_count" : 53
        }, {
          "key" : "S300A113",
          "doc_count" : 53
        }, {
          "key" : "GTB1752VK",
          "doc_count" : 51
        }, {
          "key" : "S4D",
          "doc_count" : 50
        }, {
          "key" : "B2UV-63A84BR27C01BV-064N69Al17R80BC",
          "doc_count" : 49
        }, {
          "key" : "HT3B",
          "doc_count" : 49
        }, {
          "key" : "4LE504",
          "doc_count" : 48
        }, {
          "key" : "GT4594",
          "doc_count" : 48
        }, {
          "key" : "GTA1852VK",
          "doc_count" : 48
        }, {
          "key" : "S100G",
          "doc_count" : 48
        }, {
          "key" : "H3B",
          "doc_count" : 47
        }, {
          "key" : "TDO7",
          "doc_count" : 47
        }, {
          "key" : "S300G034",
          "doc_count" : 46
        }, {
          "key" : "GT2052S",
          "doc_count" : 45
        }, {
          "key" : "HX25",
          "doc_count" : 45
        }, {
          "key" : "S300G-76AJ/83F36BZ-PM/0.83 VTF",
          "doc_count" : 45
        }, {
          "key" : "T1810",
          "doc_count" : 45
        }, {
          "key" : "TDO6",
          "doc_count" : 45
        }, {
          "key" : "4LE303",
          "doc_count" : 44
        }, {
          "key" : "B3G",
          "doc_count" : 44
        }, {
          "key" : "GT1544",
          "doc_count" : 44
        }, {
          "key" : "GT4294",
          "doc_count" : 44
        }, {
          "key" : "S300BV124",
          "doc_count" : 44
        }, {
          "key" : "S300BV131",
          "doc_count" : 44
        }, {
          "key" : "TDO4",
          "doc_count" : 44
        }, {
          "key" : "TDO8H",
          "doc_count" : 44
        }, {
          "key" : "WH2D",
          "doc_count" : 44
        }, {
          "key" : "4LGZ352C/21.22",
          "doc_count" : 43
        }, {
          "key" : "GT4082SN",
          "doc_count" : 43
        }, {
          "key" : "GT1549",
          "doc_count" : 42
        }, {
          "key" : "HY55V",
          "doc_count" : 42
        }, {
          "key" : "K03-2080DCB5.88CCAXX",
          "doc_count" : 42
        }, {
          "key" : "R2S B1UG + B2US",
          "doc_count" : 42
        }, {
          "key" : "S410W",
          "doc_count" : 41
        }, {
          "key" : "S430S",
          "doc_count" : 41
        }, {
          "key" : "TB4122",
          "doc_count" : 41
        }, {
          "key" : "GT1544S",
          "doc_count" : 40
        }, {
          "key" : "T3021",
          "doc_count" : 40
        }, {
          "key" : "TA3103",
          "doc_count" : 40
        }, {
          "key" : "TV7701",
          "doc_count" : 40
        }, {
          "key" : "K16-2471OYCKB5.82GAAWD",
          "doc_count" : 39
        }, {
          "key" : "K27.2UG-070R84GC26O01JG-083O71AA19L56OC",
          "doc_count" : 39
        }, {
          "key" : "S76",
          "doc_count" : 39
        }, {
          "key" : "TDO4H",
          "doc_count" : 39
        }, {
          "key" : "3LD",
          "doc_count" : 38
        }, {
          "key" : "4D454",
          "doc_count" : 38
        }, {
          "key" : "4HD",
          "doc_count" : 38
        }, {
          "key" : "4LF302",
          "doc_count" : 38
        }, {
          "key" : "GT2052V",
          "doc_count" : 38
        }, {
          "key" : "GTA1749VK",
          "doc_count" : 38
        }, {
          "key" : "HT60",
          "doc_count" : 38
        }, {
          "key" : "HX27W",
          "doc_count" : 38
        }, {
          "key" : "S500",
          "doc_count" : 38
        }, {
          "key" : "4LF452",
          "doc_count" : 37
        }, {
          "key" : "4MFW731",
          "doc_count" : 37
        }, {
          "key" : "B03",
          "doc_count" : 37
        }, {
          "key" : "BHT3E",
          "doc_count" : 37
        }, {
          "key" : "BV39-1873CCB426.10AVAXC",
          "doc_count" : 37
        }, {
          "key" : "K27-3371OLAKE10.90ROAXD",
          "doc_count" : 37
        }, {
          "key" : "K27-3465MXEAA/18.20",
          "doc_count" : 37
        }, {
          "key" : "S200G-3071NRAKB0.76DK1",
          "doc_count" : 37
        }, {
          "key" : "S2AW",
          "doc_count" : 37
        }, {
          "key" : "S300V111",
          "doc_count" : 37
        }, {
          "key" : "4LE292",
          "doc_count" : 36
        }, {
          "key" : "GT1549S",
          "doc_count" : 36
        }, {
          "key" : "S200G001",
          "doc_count" : 36
        }, {
          "key" : "S300V129",
          "doc_count" : 36
        }, {
          "key" : "4MD454",
          "doc_count" : 35
        }, {
          "key" : "5HDR-761",
          "doc_count" : 35
        }, {
          "key" : "GTB2056VK",
          "doc_count" : 35
        }, {
          "key" : "HX30",
          "doc_count" : 35
        }, {
          "key" : "TDO25",
          "doc_count" : 35
        }, {
          "key" : "TO4E08",
          "doc_count" : 35
        }, {
          "key" : "GT2560S",
          "doc_count" : 34
        }, {
          "key" : "HE851",
          "doc_count" : 34
        }, {
          "key" : "TDO3",
          "doc_count" : 34
        }, {
          "key" : "TO4E66",
          "doc_count" : 34
        }, {
          "key" : "4D354",
          "doc_count" : 33
        }, {
          "key" : "4HD755",
          "doc_count" : 33
        }, {
          "key" : "4LE304",
          "doc_count" : 33
        }, {
          "key" : "4LGK",
          "doc_count" : 33
        }, {
          "key" : "4MD-755",
          "doc_count" : 33
        }, {
          "key" : "HE531V",
          "doc_count" : 33
        }, {
          "key" : "HE551V",
          "doc_count" : 33
        }, {
          "key" : "HX52",
          "doc_count" : 33
        }, {
          "key" : "K03",
          "doc_count" : 33
        }, {
          "key" : "B2UV-63J89DR27C01BV 071N67AA17R80ES",
          "doc_count" : 32
        }, {
          "key" : "BHT3B",
          "doc_count" : 32
        }, {
          "key" : "GT3776",
          "doc_count" : 32
        }, {
          "key" : "GT4082S",
          "doc_count" : 32
        }, {
          "key" : "K16",
          "doc_count" : 32
        }, {
          "key" : "TV6103",
          "doc_count" : 32
        }, {
          "key" : "TV9405",
          "doc_count" : 32
        }, {
          "key" : "4LE",
          "doc_count" : 31
        }, {
          "key" : "4LGZ352C/25.22",
          "doc_count" : 31
        }, {
          "key" : "4MD354",
          "doc_count" : 31
        }, {
          "key" : "BV43-1874KXB380.18KVAXC",
          "doc_count" : 31
        }, {
          "key" : "K14-2060GGB/3.51",
          "doc_count" : 31
        }, {
          "key" : "K16-2480DGB8.88GAAXK",
          "doc_count" : 31
        }, {
          "key" : "K33-4064MNA/24.22",
          "doc_count" : 31
        }, {
          "key" : "S200W",
          "doc_count" : 31
        }, {
          "key" : "S2BW",
          "doc_count" : 31
        }, {
          "key" : "S4DS",
          "doc_count" : 31
        }, {
          "key" : "3LKS",
          "doc_count" : 30
        }, {
          "key" : "4MD455",
          "doc_count" : 30
        }, {
          "key" : "H2E",
          "doc_count" : 30
        }, {
          "key" : "S1BG-034",
          "doc_count" : 30
        }, {
          "key" : "S300AG072",
          "doc_count" : 30
        }, {
          "key" : "S300S-080",
          "doc_count" : 30
        }, {
          "key" : "S430V095",
          "doc_count" : 30
        }, {
          "key" : "S4TW",
          "doc_count" : 30
        }, {
          "key" : "T18A73",
          "doc_count" : 30
        }, {
          "key" : "T350-04",
          "doc_count" : 30
        }, {
          "key" : "TA4521",
          "doc_count" : 30
        }, {
          "key" : "TO4B27",
          "doc_count" : 30
        }, {
          "key" : "GT2052",
          "doc_count" : 29
        }, {
          "key" : "K14-2064GGB/3.51",
          "doc_count" : 29
        }, {
          "key" : "K29",
          "doc_count" : 29
        }, {
          "key" : "RHF4H-64006P12NHBRL362CCZ",
          "doc_count" : 29
        }, {
          "key" : "S430V087PM5105/51AN-AUM",
          "doc_count" : 29
        }, {
          "key" : "S430V097",
          "doc_count" : 29
        }, {
          "key" : "T3023",
          "doc_count" : 29
        }, {
          "key" : "TCO5",
          "doc_count" : 29
        }, {
          "key" : "BV39-1872FFAAA426.18BVAXC",
          "doc_count" : 28
        }, {
          "key" : "GT3571",
          "doc_count" : 28
        }, {
          "key" : "GTA2260V",
          "doc_count" : 28
        }, {
          "key" : "GTP38",
          "doc_count" : 28
        }, {
          "key" : "HC3B",
          "doc_count" : 28
        }, {
          "key" : "HE400VG",
          "doc_count" : 28
        }, {
          "key" : "HE531VE",
          "doc_count" : 28
        }, {
          "key" : "S300S002",
          "doc_count" : 28
        }, {
          "key" : "S410T",
          "doc_count" : 28
        }, {
          "key" : "S510C004",
          "doc_count" : 28
        }, {
          "key" : "T1818",
          "doc_count" : 28
        }, {
          "key" : "TO4B71",
          "doc_count" : 28
        }, {
          "key" : "4B354",
          "doc_count" : 27
        }, {
          "key" : "GT30BB",
          "doc_count" : 27
        }, {
          "key" : "K24-2671OXCKA6.81GBAWD",
          "doc_count" : 27
        }, {
          "key" : "K37-4671OOAKB27.12GAAYD",
          "doc_count" : 27
        }, {
          "key" : "RHB6",
          "doc_count" : 27
        }, {
          "key" : "S200V 63YJ5-67M/15AU-DYM.72VO",
          "doc_count" : 27
        }, {
          "key" : "S2EGL-094",
          "doc_count" : 27
        }, {
          "key" : "S300A099",
          "doc_count" : 27
        }, {
          "key" : "S300BV127",
          "doc_count" : 27
        }, {
          "key" : "S430V096",
          "doc_count" : 27
        }, {
          "key" : "ST50",
          "doc_count" : 27
        }, {
          "key" : "TO4B49",
          "doc_count" : 27
        }, {
          "key" : "3LM-353",
          "doc_count" : 26
        }, {
          "key" : "HE431V",
          "doc_count" : 26
        }, {
          "key" : "HX52W",
          "doc_count" : 26
        }, {
          "key" : "K27-3465MXEAA18.20RNADD",
          "doc_count" : 26
        }, {
          "key" : "S200G-2471NRAKB0.48WJ1",
          "doc_count" : 26
        }, {
          "key" : "S200G-2871NXAKB0.64YK1",
          "doc_count" : 26
        }, {
          "key" : "S300-76AJ/83F/36BZ-PM/0.83 VTF",
          "doc_count" : 26
        }, {
          "key" : "S300-76AJ/83F/36BZ-PM/1.10 VTF",
          "doc_count" : 26
        }, {
          "key" : "T1817",
          "doc_count" : 26
        }, {
          "key" : "TV8118",
          "doc_count" : 26
        }, {
          "key" : "4-351",
          "doc_count" : 25
        }, {
          "key" : "4HD857",
          "doc_count" : 25
        }, {
          "key" : "4MD",
          "doc_count" : 25
        }, {
          "key" : "B2UV-63A90DR40C12BV-070S71XA20S78BS",
          "doc_count" : 25
        }, {
          "key" : "GT4288",
          "doc_count" : 25
        }, {
          "key" : "GT4294S",
          "doc_count" : 25
        }, {
          "key" : "HE551W",
          "doc_count" : 25
        }, {
          "key" : "K03-2074DCB304.98KXAXX",
          "doc_count" : 25
        }, {
          "key" : "S2EG-070",
          "doc_count" : 25
        }, {
          "key" : "S300G-83H36DS-PM/0.80VTF74DA1",
          "doc_count" : 25
        }, {
          "key" : "S310G085",
          "doc_count" : 25
        }, {
          "key" : "TO4B05",
          "doc_count" : 25
        }, {
          "key" : "4LE414",
          "doc_count" : 24
        }, {
          "key" : "4LE556",
          "doc_count" : 24
        }, {
          "key" : "CT",
          "doc_count" : 24
        }, {
          "key" : "HX32W",
          "doc_count" : 24
        }, {
          "key" : "K14-2070GGB/5.82",
          "doc_count" : 24
        }, {
          "key" : "S410W021",
          "doc_count" : 24
        }, {
          "key" : "T18A95",
          "doc_count" : 24
        }, {
          "key" : "TO4B25",
          "doc_count" : 24
        }, {
          "key" : "TO4B81",
          "doc_count" : 24
        }, {
          "key" : "TW9205",
          "doc_count" : 24
        }, {
          "key" : "4LE302",
          "doc_count" : 23
        }, {
          "key" : "5MF",
          "doc_count" : 23
        }, {
          "key" : "B2UV-63A90DR40C12BV-076S73AL",
          "doc_count" : 23
        }, {
          "key" : "BV40",
          "doc_count" : 23
        }, {
          "key" : "S200S044",
          "doc_count" : 23
        }, {
          "key" : "S200S053-70H21AC-BX/1.2270DJ2",
          "doc_count" : 23
        }, {
          "key" : "S300S008",
          "doc_count" : 23
        }, {
          "key" : "TB25",
          "doc_count" : 23
        }, {
          "key" : "TDO4-11G/4",
          "doc_count" : 23
        }, {
          "key" : "4HD-857",
          "doc_count" : 22
        }, {
          "key" : "B2UV-63J89DR27C01BV 071N67AA17R80BS",
          "doc_count" : 22
        }, {
          "key" : "K03-2074DCBAA5.88KCAXK",
          "doc_count" : 22
        }, {
          "key" : "K27-3060G/13.11",
          "doc_count" : 22
        }, {
          "key" : "K29-3569QOAKB12.91GCAWD",
          "doc_count" : 22
        }, {
          "key" : "R2S",
          "doc_count" : 22
        }, {
          "key" : "S300BV126",
          "doc_count" : 22
        }, {
          "key" : "T1402",
          "doc_count" : 22
        }, {
          "key" : "T1404",
          "doc_count" : 22
        }, {
          "key" : "T1435",
          "doc_count" : 22
        }, {
          "key" : "TA3107",
          "doc_count" : 22
        }, {
          "key" : "TA5127",
          "doc_count" : 22
        }, {
          "key" : "4LF654",
          "doc_count" : 21
        }, {
          "key" : "B2",
          "doc_count" : 21
        }, {
          "key" : "BV43-1880KCF419.18BVAXC",
          "doc_count" : 21
        }, {
          "key" : "GT2049S",
          "doc_count" : 21
        }, {
          "key" : "GTA2256V",
          "doc_count" : 21
        }, {
          "key" : "K26-2660GA/8.11",
          "doc_count" : 21
        }, {
          "key" : "K27.2-3071OXCKB/13.22",
          "doc_count" : 21
        }, {
          "key" : "K27.2-3071OXCKB8.03RNAXD",
          "doc_count" : 21
        }, {
          "key" : "K33-4067MNA/24.22",
          "doc_count" : 21
        }, {
          "key" : "RHB31CW-30004CP7NRBRDL245AZ",
          "doc_count" : 21
        }, {
          "key" : "RHB6-65003P16NFBRL406B",
          "doc_count" : 21
        }, {
          "key" : "RHF5",
          "doc_count" : 21
        }, {
          "key" : "RHF5-64006P17NHBRL382CAZ",
          "doc_count" : 21
        }, {
          "key" : "S2AS-015",
          "doc_count" : 21
        }, {
          "key" : "S300W",
          "doc_count" : 21
        }, {
          "key" : "T50",
          "doc_count" : 21
        }, {
          "key" : "TFO35HM-12T",
          "doc_count" : 21
        }, {
          "key" : "TO4E14",
          "doc_count" : 21
        }, {
          "key" : "4LF552",
          "doc_count" : 20
        }, {
          "key" : "6",
          "doc_count" : 20
        }, {
          "key" : "B3G-3769NYAKG/0.97/37%PJ6",
          "doc_count" : 20
        } ]
      },
      "Turbo Type" : {
        "doc_count_error_upper_bound" : 0,
        "sum_other_doc_count" : 2909,
        "buckets" : [ {
          "key" : "K27",
          "doc_count" : 1593
        }, {
          "key" : "HX40W",
          "doc_count" : 1010
        }, {
          "key" : "TO4B",
          "doc_count" : 721
        }, {
          "key" : "S2B",
          "doc_count" : 638
        }, {
          "key" : "RHB5",
          "doc_count" : 596
        }, {
          "key" : "S300G",
          "doc_count" : 591
        }, {
          "key" : "S200G",
          "doc_count" : 587
        }, {
          "key" : "S3A",
          "doc_count" : 532
        }, {
          "key" : "K31",
          "doc_count" : 530
        }, {
          "key" : "K16",
          "doc_count" : 525
        }, {
          "key" : "K26",
          "doc_count" : 521
        }, {
          "key" : "GT17V",
          "doc_count" : 518
        }, {
          "key" : "S400",
          "doc_count" : 487
        }, {
          "key" : "S2A",
          "doc_count" : 464
        }, {
          "key" : "4LE",
          "doc_count" : 442
        }, {
          "key" : "HX35W",
          "doc_count" : 436
        }, {
          "key" : "K29",
          "doc_count" : 360
        }, {
          "key" : "TO4E",
          "doc_count" : 356
        }, {
          "key" : "4MF",
          "doc_count" : 352
        }, {
          "key" : "4LGZ",
          "doc_count" : 344
        }, {
          "key" : "K03",
          "doc_count" : 334
        }, {
          "key" : "4LF",
          "doc_count" : 329
        }, {
          "key" : "TDO4",
          "doc_count" : 326
        }, {
          "key" : "K36",
          "doc_count" : 325
        }, {
          "key" : "B2UV",
          "doc_count" : 319
        }, {
          "key" : "R2S",
          "doc_count" : 317
        }, {
          "key" : "K24",
          "doc_count" : 314
        }, {
          "key" : "S300",
          "doc_count" : 314
        }, {
          "key" : "RHC6",
          "doc_count" : 309
        }, {
          "key" : "K33",
          "doc_count" : 301
        }, {
          "key" : "H1C",
          "doc_count" : 295
        }, {
          "key" : "S3B",
          "doc_count" : 292
        }, {
          "key" : "GT40",
          "doc_count" : 291
        }, {
          "key" : "S1B",
          "doc_count" : 290
        }, {
          "key" : "4MD",
          "doc_count" : 286
        }, {
          "key" : "RHF5",
          "doc_count" : 284
        }, {
          "key" : "S300S",
          "doc_count" : 284
        }, {
          "key" : "K28",
          "doc_count" : 275
        }, {
          "key" : "HX82",
          "doc_count" : 268
        }, {
          "key" : "GT42",
          "doc_count" : 264
        }, {
          "key" : "S200",
          "doc_count" : 263
        }, {
          "key" : "H1E",
          "doc_count" : 262
        }, {
          "key" : "S200S",
          "doc_count" : 254
        }, {
          "key" : "S300V",
          "doc_count" : 243
        }, {
          "key" : "RHC7",
          "doc_count" : 242
        }, {
          "key" : "K14",
          "doc_count" : 231
        }, {
          "key" : "TB25",
          "doc_count" : 231
        }, {
          "key" : "3LD",
          "doc_count" : 227
        }, {
          "key" : "BV39",
          "doc_count" : 226
        }, {
          "key" : "4HD",
          "doc_count" : 224
        }, {
          "key" : "RHB3",
          "doc_count" : 218
        }, {
          "key" : "HX83",
          "doc_count" : 216
        }, {
          "key" : "K37",
          "doc_count" : 216
        }, {
          "key" : "TA45",
          "doc_count" : 214
        }, {
          "key" : "RHC9",
          "doc_count" : 213
        }, {
          "key" : "T3",
          "doc_count" : 212
        }, {
          "key" : "KO3",
          "doc_count" : 210
        }, {
          "key" : "HX35",
          "doc_count" : 195
        }, {
          "key" : "BV43",
          "doc_count" : 194
        }, {
          "key" : "RHB6",
          "doc_count" : 194
        }, {
          "key" : "HX50",
          "doc_count" : 193
        }, {
          "key" : "TA31",
          "doc_count" : 188
        }, {
          "key" : "HE431VE",
          "doc_count" : 187
        }, {
          "key" : "RHF4",
          "doc_count" : 187
        }, {
          "key" : "S300BV",
          "doc_count" : 187
        }, {
          "key" : "3LM",
          "doc_count" : 186
        }, {
          "key" : "T18A",
          "doc_count" : 186
        }, {
          "key" : "GT15",
          "doc_count" : 182
        }, {
          "key" : "HC5A",
          "doc_count" : 176
        }, {
          "key" : "GT37",
          "doc_count" : 173
        }, {
          "key" : "HX55",
          "doc_count" : 170
        }, {
          "key" : "GT20V",
          "doc_count" : 168
        }, {
          "key" : "HX80",
          "doc_count" : 168
        }, {
          "key" : "4LGK",
          "doc_count" : 167
        }, {
          "key" : "TDO5H",
          "doc_count" : 167
        }, {
          "key" : "S1BG",
          "doc_count" : 166
        }, {
          "key" : "HX30W",
          "doc_count" : 165
        }, {
          "key" : "TA51",
          "doc_count" : 164
        }, {
          "key" : "TB41",
          "doc_count" : 163
        }, {
          "key" : "GT22V",
          "doc_count" : 162
        }, {
          "key" : "S4T",
          "doc_count" : 162
        }, {
          "key" : "K365",
          "doc_count" : 159
        }, {
          "key" : "HX40",
          "doc_count" : 158
        }, {
          "key" : "TV81",
          "doc_count" : 158
        }, {
          "key" : "S2BS",
          "doc_count" : 156
        }, {
          "key" : "GT25",
          "doc_count" : 155
        }, {
          "key" : "TV61",
          "doc_count" : 155
        }, {
          "key" : "TD10",
          "doc_count" : 150
        }, {
          "key" : "H2D",
          "doc_count" : 149
        }, {
          "key" : "K361",
          "doc_count" : 147
        }, {
          "key" : "WH1E",
          "doc_count" : 144
        }, {
          "key" : "HE221W",
          "doc_count" : 138
        }, {
          "key" : "HX55W",
          "doc_count" : 135
        }, {
          "key" : "S4DS",
          "doc_count" : 135
        }, {
          "key" : "T18",
          "doc_count" : 132
        }, {
          "key" : "4D",
          "doc_count" : 130
        }, {
          "key" : "TFO35",
          "doc_count" : 129
        }, {
          "key" : "S400S",
          "doc_count" : 128
        }, {
          "key" : "TBO2",
          "doc_count" : 128
        }, {
          "key" : "GT20",
          "doc_count" : 127
        }, {
          "key" : "4LG",
          "doc_count" : 125
        }, {
          "key" : "TBP4",
          "doc_count" : 125
        }, {
          "key" : "TDO8",
          "doc_count" : 125
        }, {
          "key" : "K04",
          "doc_count" : 124
        }, {
          "key" : "TD13",
          "doc_count" : 124
        }, {
          "key" : "BV50",
          "doc_count" : 123
        }, {
          "key" : "K42",
          "doc_count" : 123
        }, {
          "key" : "S3BSL",
          "doc_count" : 121
        }, {
          "key" : "TDO4H",
          "doc_count" : 121
        }, {
          "key" : "HX60",
          "doc_count" : 117
        }, {
          "key" : "KP39",
          "doc_count" : 117
        }, {
          "key" : "S300A",
          "doc_count" : 117
        }, {
          "key" : "4B",
          "doc_count" : 116
        }, {
          "key" : "GT45",
          "doc_count" : 116
        }, {
          "key" : "S430V",
          "doc_count" : 116
        }, {
          "key" : "S310G",
          "doc_count" : 111
        }, {
          "key" : "4MFW",
          "doc_count" : 110
        }, {
          "key" : "S2ESL",
          "doc_count" : 110
        }, {
          "key" : "HE451VE",
          "doc_count" : 109
        }, {
          "key" : "S410",
          "doc_count" : 107
        }, {
          "key" : "T2",
          "doc_count" : 107
        }, {
          "key" : "GT60",
          "doc_count" : 106
        }, {
          "key" : "RHF3",
          "doc_count" : 106
        }, {
          "key" : "B3G",
          "doc_count" : 104
        }, {
          "key" : "S2EGL",
          "doc_count" : 104
        }, {
          "key" : "KP35",
          "doc_count" : 101
        }, {
          "key" : "S410W",
          "doc_count" : 101
        }, {
          "key" : "T14",
          "doc_count" : 101
        }, {
          "key" : "WH1C",
          "doc_count" : 101
        }, {
          "key" : "K44",
          "doc_count" : 99
        }, {
          "key" : "TDO6",
          "doc_count" : 99
        }, {
          "key" : "S2BG",
          "doc_count" : 97
        }, {
          "key" : "T30",
          "doc_count" : 95
        }, {
          "key" : "4\"",
          "doc_count" : 94
        }, {
          "key" : "S410G",
          "doc_count" : 93
        }, {
          "key" : "S410V",
          "doc_count" : 92
        }, {
          "key" : "S100",
          "doc_count" : 91
        }, {
          "key" : "TBO3",
          "doc_count" : 89
        }, {
          "key" : "HE341VE",
          "doc_count" : 88
        }, {
          "key" : "RHE6",
          "doc_count" : 88
        }, {
          "key" : "S2BW",
          "doc_count" : 86
        }, {
          "key" : "GT35",
          "doc_count" : 84
        }, {
          "key" : "TDO25",
          "doc_count" : 83
        }, {
          "key" : "GT18V",
          "doc_count" : 82
        }, {
          "key" : "TO4",
          "doc_count" : 79
        }, {
          "key" : "HX50W",
          "doc_count" : 78
        }, {
          "key" : "GT15V",
          "doc_count" : 76
        }, {
          "key" : "H2A",
          "doc_count" : 74
        }, {
          "key" : "3HD",
          "doc_count" : 73
        }, {
          "key" : "S300W",
          "doc_count" : 73
        }, {
          "key" : "B2G",
          "doc_count" : 72
        }, {
          "key" : "HY40V",
          "doc_count" : 72
        }, {
          "key" : "S500",
          "doc_count" : 72
        }, {
          "key" : "3LKS",
          "doc_count" : 71
        }, {
          "key" : "S2AS",
          "doc_count" : 70
        }, {
          "key" : "TEO6",
          "doc_count" : 70
        }, {
          "key" : "4HF",
          "doc_count" : 68
        }, {
          "key" : "GT17",
          "doc_count" : 68
        }, {
          "key" : "3LEP",
          "doc_count" : 66
        }, {
          "key" : "H2C",
          "doc_count" : 66
        }, {
          "key" : "S2EG",
          "doc_count" : 66
        }, {
          "key" : "TCO5",
          "doc_count" : 66
        }, {
          "key" : "HX25W",
          "doc_count" : 65
        }, {
          "key" : "HY35W",
          "doc_count" : 65
        }, {
          "key" : "TV77",
          "doc_count" : 65
        }, {
          "key" : "HX60W",
          "doc_count" : 64
        }, {
          "key" : "S4EV",
          "doc_count" : 64
        }, {
          "key" : "S300AG",
          "doc_count" : 63
        }, {
          "key" : "GT47",
          "doc_count" : 62
        }, {
          "key" : "S100G",
          "doc_count" : 62
        }, {
          "key" : "HE351VE",
          "doc_count" : 61
        }, {
          "key" : "TDO8H",
          "doc_count" : 60
        }, {
          "key" : "T12",
          "doc_count" : 59
        }, {
          "key" : "B2",
          "doc_count" : 58
        }, {
          "key" : "KO4",
          "doc_count" : 58
        }, {
          "key" : "RHE7",
          "doc_count" : 57
        }, {
          "key" : "S200W",
          "doc_count" : 57
        }, {
          "key" : "S200AG",
          "doc_count" : 55
        }, {
          "key" : "TDO7",
          "doc_count" : 55
        }, {
          "key" : "HE551VE",
          "doc_count" : 54
        }, {
          "key" : "TA34",
          "doc_count" : 54
        }, {
          "key" : "HX35G",
          "doc_count" : 53
        }, {
          "key" : "S4D",
          "doc_count" : 53
        }, {
          "key" : "TV94",
          "doc_count" : 53
        }, {
          "key" : "5HDR",
          "doc_count" : 52
        }, {
          "key" : "5MF",
          "doc_count" : 52
        }, {
          "key" : "S4DC",
          "doc_count" : 52
        }, {
          "key" : "TW81",
          "doc_count" : 52
        }, {
          "key" : "B3",
          "doc_count" : 51
        }, {
          "key" : "HT3B",
          "doc_count" : 49
        }, {
          "key" : "TDO3",
          "doc_count" : 49
        }, {
          "key" : "TW92",
          "doc_count" : 49
        }, {
          "key" : "RHB7",
          "doc_count" : 48
        }, {
          "key" : "RHG6",
          "doc_count" : 48
        }, {
          "key" : "S310S",
          "doc_count" : 48
        }, {
          "key" : "TV71",
          "doc_count" : 48
        }, {
          "key" : "H3B",
          "doc_count" : 47
        }, {
          "key" : "6\"",
          "doc_count" : 46
        }, {
          "key" : "HC3",
          "doc_count" : 46
        }, {
          "key" : "S4AS",
          "doc_count" : 46
        }, {
          "key" : "TL81",
          "doc_count" : 46
        }, {
          "key" : "BV40",
          "doc_count" : 45
        }, {
          "key" : "HX25",
          "doc_count" : 45
        }, {
          "key" : "GT22",
          "doc_count" : 44
        }, {
          "key" : "S2EL",
          "doc_count" : 44
        }, {
          "key" : "T350",
          "doc_count" : 44
        }, {
          "key" : "TB22",
          "doc_count" : 44
        }, {
          "key" : "WH2D",
          "doc_count" : 44
        }, {
          "key" : "HE431V",
          "doc_count" : 43
        }, {
          "key" : "GT30",
          "doc_count" : 42
        }, {
          "key" : "HT12",
          "doc_count" : 42
        }, {
          "key" : "HY55V",
          "doc_count" : 42
        }, {
          "key" : "S200V",
          "doc_count" : 42
        }, {
          "key" : "S3BL",
          "doc_count" : 42
        }, {
          "key" : "GT32",
          "doc_count" : 41
        }, {
          "key" : "RHB8",
          "doc_count" : 41
        }, {
          "key" : "S430S",
          "doc_count" : 41
        }, {
          "key" : "S510C",
          "doc_count" : 41
        }, {
          "key" : "3HF",
          "doc_count" : 40
        }, {
          "key" : "S76",
          "doc_count" : 39
        }, {
          "key" : "HT60",
          "doc_count" : 38
        }, {
          "key" : "HX27W",
          "doc_count" : 38
        }, {
          "key" : "TB28",
          "doc_count" : 38
        }, {
          "key" : "B03",
          "doc_count" : 37
        }, {
          "key" : "BHT3E",
          "doc_count" : 37
        }, {
          "key" : "S2AW",
          "doc_count" : 37
        }, {
          "key" : "S3AS",
          "doc_count" : 37
        }, {
          "key" : "GT55",
          "doc_count" : 36
        }, {
          "key" : "RHF4V",
          "doc_count" : 36
        }, {
          "key" : "S4DW",
          "doc_count" : 36
        }, {
          "key" : "HX30",
          "doc_count" : 35
        }, {
          "key" : "BV35",
          "doc_count" : 34
        }, {
          "key" : "E",
          "doc_count" : 34
        }, {
          "key" : "GT50",
          "doc_count" : 34
        }, {
          "key" : "HE851",
          "doc_count" : 34
        }, {
          "key" : "B1G",
          "doc_count" : 33
        }, {
          "key" : "GT28",
          "doc_count" : 33
        }, {
          "key" : "HE531V",
          "doc_count" : 33
        }, {
          "key" : "HE551V",
          "doc_count" : 33
        }, {
          "key" : "HT06",
          "doc_count" : 33
        }, {
          "key" : "HX52",
          "doc_count" : 33
        }, {
          "key" : "S4TW",
          "doc_count" : 33
        }, {
          "key" : "BHT3B",
          "doc_count" : 32
        }, {
          "key" : "F",
          "doc_count" : 32
        }, {
          "key" : "GT25V",
          "doc_count" : 32
        }, {
          "key" : "S3BGL",
          "doc_count" : 32
        }, {
          "key" : "S410T",
          "doc_count" : 31
        }, {
          "key" : "TCO6",
          "doc_count" : 31
        }, {
          "key" : "H2E",
          "doc_count" : 30
        }, {
          "key" : "BTG75",
          "doc_count" : 29
        }, {
          "key" : "GTP38",
          "doc_count" : 29
        }, {
          "key" : "TL92",
          "doc_count" : 29
        }, {
          "key" : "TW61",
          "doc_count" : 29
        }, {
          "key" : "5MDY",
          "doc_count" : 28
        }, {
          "key" : "BV45",
          "doc_count" : 28
        }, {
          "key" : "GT14V",
          "doc_count" : 28
        }, {
          "key" : "HC3B",
          "doc_count" : 28
        }, {
          "key" : "HE400VG",
          "doc_count" : 28
        }, {
          "key" : "HE531VE",
          "doc_count" : 28
        }, {
          "key" : "TD45",
          "doc_count" : 28
        }, {
          "key" : "3LDZ",
          "doc_count" : 27
        }, {
          "key" : "S3BS",
          "doc_count" : 27
        }, {
          "key" : "ST50",
          "doc_count" : 27
        }, {
          "key" : "T15",
          "doc_count" : 27
        }, {
          "key" : "T300",
          "doc_count" : 27
        }, {
          "key" : "TV72",
          "doc_count" : 27
        }, {
          "key" : "TV75",
          "doc_count" : 27
        }, {
          "key" : "BV38",
          "doc_count" : 26
        }, {
          "key" : "HX52W",
          "doc_count" : 26
        }, {
          "key" : "S400G",
          "doc_count" : 26
        }, {
          "key" : "V4MD",
          "doc_count" : 26
        }, {
          "key" : "B1",
          "doc_count" : 25
        }, {
          "key" : "HE551W",
          "doc_count" : 25
        }, {
          "key" : "S1BS",
          "doc_count" : 25
        }, {
          "key" : "S310",
          "doc_count" : 25
        }, {
          "key" : "S310C",
          "doc_count" : 25
        }, {
          "key" : "4LEK",
          "doc_count" : 24
        }, {
          "key" : "CT",
          "doc_count" : 24
        }, {
          "key" : "EFR",
          "doc_count" : 24
        }, {
          "key" : "HX32W",
          "doc_count" : 24
        }, {
          "key" : "K54",
          "doc_count" : 24
        }, {
          "key" : "KP31",
          "doc_count" : 24
        }, {
          "key" : "S510W",
          "doc_count" : 24
        }, {
          "key" : "THO8A",
          "doc_count" : 24
        }, {
          "key" : "TP38",
          "doc_count" : 24
        }, {
          "key" : "GT23V",
          "doc_count" : 23
        }, {
          "key" : "TDO2",
          "doc_count" : 23
        }, {
          "key" : "4A",
          "doc_count" : 22
        }, {
          "key" : "B2DG",
          "doc_count" : 22
        }, {
          "key" : "GT12",
          "doc_count" : 22
        }, {
          "key" : "RHV4",
          "doc_count" : 22
        }, {
          "key" : "S310CG",
          "doc_count" : 22
        }, {
          "key" : "S4A",
          "doc_count" : 22
        }, {
          "key" : "TDO7S",
          "doc_count" : 22
        }, {
          "key" : "TO6",
          "doc_count" : 22
        }, {
          "key" : "3FJ",
          "doc_count" : 21
        }, {
          "key" : "3LKZ",
          "doc_count" : 21
        }, {
          "key" : "4LEV",
          "doc_count" : 21
        }, {
          "key" : "BTL85",
          "doc_count" : 21
        }, {
          "key" : "BTV75",
          "doc_count" : 21
        } ]
      },
      "Manufacturer" : {
        "doc_count_error_upper_bound" : 0,
        "sum_other_doc_count" : 0,
        "buckets" : [ {
          "key" : "Schwitzer",
          "doc_count" : 14692
        }, {
          "key" : "KKK",
          "doc_count" : 14249
        }, {
          "key" : "Garrett",
          "doc_count" : 13261
        }, {
          "key" : "Holset",
          "doc_count" : 9323
        }, {
          "key" : "Turbo International",
          "doc_count" : 5846
        }, {
          "key" : "I.H.I.",
          "doc_count" : 3325
        }, {
          "key" : "Mitsubishi",
          "doc_count" : 2694
        }, {
          "key" : "Rotomaster",
          "doc_count" : 316
        }, {
          "key" : "Komatsu",
          "doc_count" : 190
        }, {
          "key" : "Hitachi",
          "doc_count" : 152
        }, {
          "key" : "Toyota",
          "doc_count" : 131
        } ]
      },
      "Part Type" : {
        "doc_count_error_upper_bound" : 0,
        "sum_other_doc_count" : 0,
        "buckets" : [ {
          "key" : "Turbo",
          "doc_count" : 42519
        }, {
          "key" : "Cartridge",
          "doc_count" : 7907
        }, {
          "key" : "Compressor Wheel",
          "doc_count" : 2370
        }, {
          "key" : "Kit",
          "doc_count" : 2303
        }, {
          "key" : "Bearing Housing",
          "doc_count" : 1964
        }, {
          "key" : "Turbine Wheel",
          "doc_count" : 1602
        }, {
          "key" : "Journal Bearing",
          "doc_count" : 1108
        }, {
          "key" : "Piston Ring",
          "doc_count" : 627
        }, {
          "key" : "Backplate / Sealplate",
          "doc_count" : 604
        }, {
          "key" : "Miscellaneous Minor Components",
          "doc_count" : 468
        }, {
          "key" : "Thrust Parts",
          "doc_count" : 421
        }, {
          "key" : "Heatshield / Shroud",
          "doc_count" : 393
        }, {
          "key" : "O Ring",
          "doc_count" : 363
        }, {
          "key" : "Nozzle Ring",
          "doc_count" : 280
        }, {
          "key" : "Backplate",
          "doc_count" : 164
        }, {
          "key" : "Gasket",
          "doc_count" : 155
        }, {
          "key" : "Clamp",
          "doc_count" : 153
        }, {
          "key" : "Thrust Bearing",
          "doc_count" : 112
        }, {
          "key" : "Thrust Collar",
          "doc_count" : 107
        }, {
          "key" : "Oil Deflector",
          "doc_count" : 84
        }, {
          "key" : "Thrust Spacer",
          "doc_count" : 84
        }, {
          "key" : "Bolt - Screw",
          "doc_count" : 75
        }, {
          "key" : "Retaining Ring",
          "doc_count" : 75
        }, {
          "key" : "Seal Plate",
          "doc_count" : 69
        }, {
          "key" : "Nut",
          "doc_count" : 64
        }, {
          "key" : "Washer",
          "doc_count" : 34
        }, {
          "key" : "Thrust Washer",
          "doc_count" : 29
        }, {
          "key" : "Journal Bearing Spacer",
          "doc_count" : 21
        }, {
          "key" : "Bearing Spacer",
          "doc_count" : 7
        }, {
          "key" : "Carbon Seal",
          "doc_count" : 6
        }, {
          "key" : "Pin",
          "doc_count" : 4
        }, {
          "key" : "Plug",
          "doc_count" : 3
        }, {
          "key" : "Spring",
          "doc_count" : 3
        }, {
          "key" : "Fitting",
          "doc_count" : 1
        } ]
      }
    }
  };

}]);
