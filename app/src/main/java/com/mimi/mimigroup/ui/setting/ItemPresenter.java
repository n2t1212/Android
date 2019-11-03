package com.mimi.mimigroup.ui.setting;
/*
import android.util.Log;

import com.npc.grc.api.AppApiImpl;
import com.npc.grc.base.BasePresenter;
import com.npc.grc.model.District;
import com.npc.grc.model.Province;
import com.npc.grc.model.Ward;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import com.npc.grc.db.DBGimsHelper;

public class ItemPresenter extends BasePresenter<ItemView> {
    private DBGimsHelper db;

    public ItemPresenter(ItemView view, CompositeSubscription subscriptions) {
        super(view, subscriptions);
    }

    public void getAllTinh(){
        addSubscription(
                appApi.getAllProvince()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Province>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null){
                                    view.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(List<Province> provinces) {
                                if (view != null){
                                    view.onGetDataSuccess(provinces);
                                }
                            }
                        })
        );
    }

    public void getAllHuyen(){
        addSubscription(
                appApi.getAllDistrict()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<District>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null){
                                    view.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(List<District> districts) {
                                if (view != null){
                                    view.onGetDataSuccess(districts);
                                }
                            }
                        })
        );
    }

    public void getAllXa(){
        addSubscription(
                appApi.getAllWard()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Ward>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null){
                                    view.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(List<Ward> wards) {
                                if (view != null){
                                    view.onGetDataSuccess(wards);
                                }
                            }
                        })
        );
    }

    public void getTinh(final String imei, final String imeisim, final int allrow){
        addSubscription(
                appApi.getProvince(imei, imeisim, allrow).concatMap(new Func1<List<Province>, Observable<? extends List<Province>>>() {
                    @Override
                    public Observable<? extends List<Province>> call(List<Province> provinces) {

                        //AppApiImpl.getInstance().saveTinh(provinces);
                         return appApi.saveTinh(provinces);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Province>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null){
                                    view.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(List<Province> provinces) {
                                if (view != null){
                                    view.onGetDataSuccess(provinces);
                                }
                            }
                        })
        );
    }

    public void getHuyen(final String imei, final String imeisim, final int allrow){
        addSubscription(
                appApi.getDistrict(imei, imeisim, allrow)
                        .concatMap(new Func1<List<District>, Observable<List<District>>>() {
                            @Override
                            public Observable<List<District>> call(List<District> districts) {
                                return appApi.saveHuyen(districts);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<District>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null){
                                    view.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(List<District> provinces) {
                                if (view != null){
                                    view.onGetDataSuccess(provinces);
                                }
                            }
                        })
        );
    }

    public void getXa(final String imei, final String imeisim, final int allrow){
        addSubscription(
                appApi.getWard(imei, imeisim, allrow).concatMap(new Func1<List<Ward>, Observable<List<Ward>>>() {
                    @Override
                    public Observable<List<Ward>> call(List<Ward> wards) {
                        return appApi.saveXa(wards);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Ward>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (view != null){
                                    view.onError(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(List<Ward> provinces) {
                                if (view != null){
                                    view.onGetDataSuccess(provinces);
                                }
                            }
                        })
        );
    }
}
*/