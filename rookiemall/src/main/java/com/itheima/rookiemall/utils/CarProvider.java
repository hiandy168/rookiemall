package com.itheima.rookiemall.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.itheima.rookiemall.bean.HomeHotBean;
import com.itheima.rookiemall.bean.ShoppingCar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyl on 2016/10/4.
 */

public class CarProvider {

    private volatile static WeakReference<CarProvider> sInstance;

    private SparseArray<ShoppingCar> mCarSparseArray;
    private Context mContext;

    private CarProvider(Context context) {
        mCarSparseArray = new SparseArray<ShoppingCar>();
        mContext = context;
        loadLocalToMemory();
    }

    public static CarProvider getInstance(Context context) {
        if (sInstance == null || sInstance.get() == null) {
            synchronized (CarProvider.class) {
                if (sInstance == null || sInstance.get() == null) {
                    sInstance = new WeakReference<CarProvider>(new CarProvider(context.getApplicationContext()));
                }
            }
        }
        return sInstance.get();
    }

    public void put(ShoppingCar car) {
        ShoppingCar tempCar = mCarSparseArray.get(car.id);
        if (tempCar != null) {
            tempCar.count++;
        } else {
            mCarSparseArray.put(car.id, car);
        }
        saveAllGoodsLocal();
    }

    public void put(HomeHotBean.ItemHomeHotBean itemHotBean) {
        put(toShoppingCar(itemHotBean));
    }

    private ShoppingCar toShoppingCar(HomeHotBean.ItemHomeHotBean itemHotBean) {
        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.id = itemHotBean.id;
        shoppingCar.name = itemHotBean.name;
        shoppingCar.price = itemHotBean.price;
        shoppingCar.imgUrl = itemHotBean.imgUrl;
        return shoppingCar;
    }

    /**
     * 更新购物车商品数据
     *
     * @param car
     */
    public void updateCarGoods(ShoppingCar car) {
        mCarSparseArray.remove(car.id);
        mCarSparseArray.put(car.id, car);
        saveAllGoodsLocal();
    }

    public void delete(ShoppingCar car) {
        mCarSparseArray.remove(car.id);
        saveAllGoodsLocal();
    }

    /**
     * 删除所有选中的商品
     *
     * @return 删除的数量
     */
    public int deleteAllSelect() {
        int deleteCount = 0;
        int goodsSize = mCarSparseArray.size();
        for (int i = goodsSize - 1; i >= 0; i--) {
            if (mCarSparseArray.get(mCarSparseArray.keyAt(i)).isChecked) {
                mCarSparseArray.removeAt(i);
                deleteCount++;
            }
        }
        saveAllGoodsLocal();
        return deleteCount;
    }

    /**
     * 保存所有的商品到本地
     */
    public void saveAllGoodsLocal() {
        List<ShoppingCar> cars = getMemoryAll();
        if (cars != null) {
            String json = GsonUtils.parseListToJson(cars);
            SPUtils.put(mContext, SPUtils.CAR_JSON, json);
        }
    }

    /**
     * 加载本地数据到内存中
     */
    public void loadLocalToMemory() {
        List<ShoppingCar> cars = getLocalAll();
        for (ShoppingCar car : cars) {
            mCarSparseArray.put(car.id, car);
        }
    }

    public List<ShoppingCar> getMemoryAll() {
        List<ShoppingCar> cars = new ArrayList<>();
        int carSize = mCarSparseArray.size();
        for (int i = 0; i < carSize; i++) {
            cars.add(mCarSparseArray.get(mCarSparseArray.keyAt(i)));
        }
        return cars;
    }

    public List<ShoppingCar> getAllSelectShopping() {
        List<ShoppingCar> cars = new ArrayList<>();
        int carSize = mCarSparseArray.size();
        for (int i = 0; i < carSize; i++) {
            ShoppingCar shoppingCar = mCarSparseArray.get(mCarSparseArray.keyAt(i));
            if (shoppingCar.isChecked) {
                cars.add(shoppingCar);
            }
        }
        return cars;
    }

    public List<ShoppingCar> getLocalAll() {
        List<ShoppingCar> cars = null;
        String json = (String) SPUtils.get(mContext, SPUtils.CAR_JSON, "");
        if (!TextUtils.isEmpty(json)) {
            cars = GsonUtils.parseToBean(json.trim(), new TypeToken<List<ShoppingCar>>() {
            }.getType());
        }
        return (cars == null ? new ArrayList<ShoppingCar>(0) : cars);
    }

    /**
     * 获取商品总价格
     *
     * @return
     */
    public double getCarTotalPrice() {
        double tempTotalPrice = 0;
        int goodsSize = mCarSparseArray.size();
        for (int i = 0; i < goodsSize; i++) {
            ShoppingCar tempCar = mCarSparseArray.get(mCarSparseArray.keyAt(i));
            if (tempCar.isChecked) {
                tempTotalPrice = Arith.add(tempTotalPrice, Arith.mul(tempCar.price, tempCar.count));
            }
        }
        return tempTotalPrice;
    }

    /**
     * 获取是否全选的状态
     *
     * @return
     */
    public boolean isAllChecked() {
        return getCheckedGoodsCount() == mCarSparseArray.size();
    }

    public void setAllCheckedState(boolean state) {
        int goodsSize = mCarSparseArray.size();
        for (int i = 0; i < goodsSize; i++) {
            mCarSparseArray.get(mCarSparseArray.keyAt(i)).isChecked = state;
        }
        saveAllGoodsLocal();
    }

    /**
     * 获取选中商品的数量
     *
     * @return
     */
    public int getCheckedGoodsCount() {
        int checkedCount = 0;
        int goodsSize = mCarSparseArray.size();
        for (int i = 0; i < goodsSize; i++) {
            if (mCarSparseArray.get(mCarSparseArray.keyAt(i)).isChecked) {
                checkedCount++;
            }
        }
        return checkedCount;
    }

}
