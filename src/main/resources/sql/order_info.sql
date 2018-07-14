create table order_info (
id bigint(20) not null auto_increment comment '订单详情表',
user_id bigint(20) default null comment '用户ID',
goods_id bigint(20) default null comment '商品ID',
delivery_addr_id bigint(20) default null comment '地址ID',
goods_name varchar(16) default null comment '商品名称',
goods_count int(11) default null comment '商品数量',
goods_price decimal(10,2) default null comment '商品价格',
order_channel tinyint(4) default null comment '订单来源',
status tinyint(4) default null comment '订单状态',
create_date datetime default null comment '订单创建时间',
pay_date datetime default null comment '支付时间',
primary key (id)
) engine=innodb auto_increment=3 default charset=utf8;