package com.mimi.mimigroup.ui.setting;
/*
import com.npc.grc.base.BasePresenter;
import com.npc.grc.model.Customer;
import com.npc.grc.ui.scan.ScanView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CustomerPresenter extends BasePresenter<CustomerView> {
    public CustomerPresenter(CustomerView view, CompositeSubscription subscriptions) {
        super(view, subscriptions);
    }

    public void getCustomer(final String imei, final String imeisim){
        addSubscription(
                appApi.getCustomer(imei, imeisim, 1)
                        .concatMap(new Func1<List<Customer>, Observable<? extends List<Customer>>>() {
                            @Override
                            public Observable<? extends List<Customer>> call(List<Customer> customers) {
                                return appApi.saveCustomer(customers);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Customer>>() {
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
                            public void onNext(List<Customer> customers) {
                                if (view != null){
                                    view.onGetCustomerList(customers);
                                }
                            }
                        })
        );
    }

    public void getAllCustomer(){
        addSubscription(
                appApi.getAllCustomer()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Customer>>() {
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
                            public void onNext(List<Customer> customers) {
                                if (view != null){
                                    view.onGetCustomerList(customers);
                                }
                            }
                        })
        );
    }
}

*/