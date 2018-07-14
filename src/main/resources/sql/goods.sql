create table goods (
id bigint(20) not null auto_increment comment '商品ID',
goods_name varchar(16) default null comment '商品名称',
goods_title varchar(64) default null comment '商品标题',
goods_img varchar(64) default null comment '商品的图片',
goods_detail longtext comment '商品的详情介绍',
goods_price decimal(10,2) default '0.00' comment '商品单价',
goods_stock int(11) default '0' comment '商品库存，-1表示没有限制',
primary key (id)
) engine=innodb auto_increment=3 default charset=utf8;

insert into goods values (1, 'iphoneX', 'Apple iPhone X(A1865) 64GB 银色 移动联通电信4G手机', '/img/iphonex.png', 'Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机',8765.00,10000),
(2, '华为Mate9', '华为Mate 9 4GB+32GB 月光银 移动联通电信4G手机 双卡双待', '/img/mate10.png','华为Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', 3212.00, -1);