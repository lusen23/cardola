package com.lusen.cardola.business.main.home.data;

import com.lusen.cardola.business.network.resp.GetProductAssortListResp;
import com.lusen.cardola.framework.adapter.IAdapterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 2017/7/23.
 */

public class ProductAssortBaseData implements IAdapterData {

    public List<ProductAssortData> mDatas = new ArrayList<>();

    public static List<ProductAssortBaseData> convert(GetProductAssortListResp resp) {

        List<ProductAssortBaseData> datas = new ArrayList<>();

        String url1 = "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=4e3e0e04b4096b6395145613645aed31/f7246b600c338744af80e6575b0fd9f9d72aa050.jpg";
        String url2 = "http://img2.duitang.com/uploads/item/201201/03/20120103124956_KtWQG.thumb.600_0.jpg";

        ProductAssortDoubleData data0 = new ProductAssortDoubleData();
        data0.mDatas.add(new ProductAssortData("油品专区", url1));
        data0.mDatas.add(new ProductAssortData("轮胎专区", url1));

        ProductAssortDoubleData data1 = new ProductAssortDoubleData();
        data1.mDatas.add(new ProductAssortData("率请假专区", url1));
        data1.mDatas.add(new ProductAssortData("雨刮专区", url2));

        ProductAssortTripleData data2 = new ProductAssortTripleData();
        data2.mDatas.add(new ProductAssortData("火花塞专区", url1));
        data2.mDatas.add(new ProductAssortData("养护品专区", url2));
        data2.mDatas.add(new ProductAssortData("刹车片专区", url2));

        datas.add(data0);
        datas.add(data1);
        datas.add(data2);

        return datas;
    }

}
