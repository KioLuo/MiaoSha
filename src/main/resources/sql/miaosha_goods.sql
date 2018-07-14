create table miaosha_goods (
id bigint(20) not null auto_increment comment '秒杀的商品表',
goods_id bigint(20) default null comment '商品ID',
miaosha_price decimal(10,2) default '0.00' comment '秒杀价',
stock_count int(11) default null comment '库存数量',
start_date datetime default null comment '秒杀开始时间',
end_date datetime default null comment '秒杀结束时间',
primary key (id)
) engine=innodb auto_increment=3 default charset=utf8;

insert into miaosha_goods values(1,1,0.01,4,'2018-04-05 15:18:00','2018-04-10 14:00:18'),(2,2,0.01,9,'2017-11-12 14:00:14','2017-11-13 14:00:24');