package com.kioluo.miaosha.dao;

import com.kioluo.miaosha.domain.MiaoshaGoods;
import com.kioluo.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by qy_lu on 2018/4/3.
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*, ms.miaosha_price, ms.stock_count, ms.start_date, ms.end_date from goods g left join miaosha_goods ms on g.id=ms.goods_id")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*, ms.miaosha_price, ms.stock_count, ms.start_date, ms.end_date from goods g left join miaosha_goods ms on g.id=ms.goods_id where g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
    int reduceStock(MiaoshaGoods miaoshaGoods);
}
