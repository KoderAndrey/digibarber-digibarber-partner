package com.digibarber.app.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.digibarber.app.Beans.WalletHistory;
import com.digibarber.app.Beans.walletHistoryItems;
import com.digibarber.app.CustomAdapters.WalletHostoryAdapter;
import com.digibarber.app.CustomAdapters.WalletRecyclerAdapter;
import com.digibarber.app.CustomAdapters.WalletRecyclerPreviousAdapter;
import com.digibarber.app.CustomClasses.AppController;
import com.digibarber.app.CustomClasses.BaseActivity;
import com.digibarber.app.CustomClasses.ConnectivityReceiver;
import com.digibarber.app.CustomClasses.Constants;
import com.digibarber.app.CustomClasses.LinearLayoutManagerWithSmoothScroller;
import com.digibarber.app.Interfaces.CallBackWalletSubList;
import com.digibarber.app.R;
import com.digibarber.app.apicalls.ApiClient;
import com.digibarber.app.apicalls.ApiUrls;
import com.digibarber.app.models.Payout;
import com.digibarber.app.models.transactionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class WalletActivity extends BaseActivity implements CallBackWalletSubList {

    TextView tv_wallet_amount;
    ImageView im_back_icon, im_wallet_icon, im_cross_icon;

    RecyclerView recyclerListingView, rv_sub_list;

    LinearLayout menuLayoot, firstRowLinear, secondRowLinear, thirdRowLinear, middleLinearLayout;
    RelativeLayout listLayout;
    CardView cardView;

    ArrayList<WalletHistory> current = new ArrayList<>();
    ArrayList<Payout> previous = new ArrayList<>();

    ArrayList<transactionDetails> childPrevious = new ArrayList<>();

    String totalAmount = "";

    WalletRecyclerPreviousAdapter previousAdapter;
    WalletHostoryAdapter currentAdapter;
    WalletRecyclerAdapter previousChild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#F7F7F7"));
        }

        im_cross_icon = (ImageView) findViewById(R.id.im_cross_icon);
        tv_wallet_amount = (TextView) findViewById(R.id.tv_wallet_amount);

        menuLayoot = (LinearLayout) findViewById(R.id.menuLayout);
        firstRowLinear = (LinearLayout) findViewById(R.id.firstRowLinear);
        secondRowLinear = (LinearLayout) findViewById(R.id.secondRowLinear);
        thirdRowLinear = (LinearLayout) findViewById(R.id.thirdRowLinear);


        middleLinearLayout = (LinearLayout) findViewById(R.id.middleLinearLayout);
        im_back_icon = (ImageView) findViewById(R.id.im_back_icon);
        im_wallet_icon = (ImageView) findViewById(R.id.im_wallet_icon);

        listLayout = (RelativeLayout) findViewById(R.id.listLayout);
        rv_sub_list = (RecyclerView) findViewById(R.id.rv_sub_list);
        recyclerListingView = (RecyclerView) findViewById(R.id.recyclerListingView);
        cardView = (CardView) findViewById(R.id.childcard);

        homeSetup();
        previousChild = new WalletRecyclerAdapter(new ArrayList<transactionDetails>(), this, this);
        rv_sub_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_sub_list.setAdapter(previousChild);

        rv_sub_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerListingView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                });
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerListingView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                });
            }
        });
        im_cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletActivity.this.finish();
            }
        });
        im_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeSetup();
            }
        });
        firstRowLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                im_wallet_icon.setImageDrawable(getResources().getDrawable(R.drawable.current_stat_icon));
                showListData();
                CallHistoryStatement(Constants.WalletCurrentList);
            }
        });
        secondRowLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                im_wallet_icon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_withdraws_icon));
                showListData();
                CallPreviousStatement();
            }
        });
        thirdRowLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                im_wallet_icon.setImageDrawable(getResources().getDrawable(R.drawable.wallet_history_icon));
                showListData();
                CallHistoryStatement(Constants.WalletHistory);
            }
        });

    }

    private void homeSetup() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuLayoot.setVisibility(View.VISIBLE);
                middleLinearLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.GONE);
                recyclerListingView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
        if (totalAmount == "") {
            balanceInfo();
        }
        if (previousAdapter != null) {
            previousAdapter.clearData();
        }
        if (currentAdapter != null) {
            currentAdapter.clearData();
        }
        if (previousChild != null) {
            previousChild.clearData();
        }
    }

    private void showListData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuLayoot.setVisibility(View.GONE);
                middleLinearLayout.setVisibility(View.VISIBLE);
                listLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void CallPreviousStatement() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(WalletActivity.this);
            StringRequest req = new StringRequest(Request.Method.POST, ApiUrls.Payouts,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            try {

                                JSONObject mainobject = new JSONObject(response);
                                JSONObject getdataobj = mainobject.getJSONObject("data");

                                JSONArray pArray = getdataobj.getJSONArray("payouts");
                                previous = new ArrayList<>();
                                childPrevious = new ArrayList<>();
                                for (int i = 0; i < pArray.length(); i++) {
                                    String payoutprice = pArray.getJSONObject(i).getString("amount");
                                    String payoutcreated = pArray.getJSONObject(i).getString("created");
                                    String id = pArray.getJSONObject(i).getString("id");
                                    String status = pArray.getJSONObject(i).getString("status");
                                    String arrivalDate = pArray.getJSONObject(i).getString("arrivalDate");
                                    JSONArray transaction = pArray.getJSONObject(i).getJSONArray("transactionDetails");
                                    Payout payout = new Payout();
                                    ArrayList<transactionDetails> transactionDetailsArrayList = new ArrayList<>();
                                    for (int j = 0; j < transaction.length(); j++) {
                                        String service = transaction.getJSONObject(j).getString("services");
                                        String price = transaction.getJSONObject(j).getString("price");
                                        String bookingDate = transaction.getJSONObject(j).getString("bookingDate");
                                        String created = transaction.getJSONObject(j).getString("created");
                                        String transactionId = transaction.getJSONObject(j).getString("transactionId");
                                        transactionDetails transactionDetails = new transactionDetails();
                                        transactionDetails.setServices(service);
                                        transactionDetails.setBookingDate(bookingDate);
                                        transactionDetails.setCreated(created);
                                        transactionDetails.setPrice(price);
                                        transactionDetails.setTransactionId(transactionId);
                                        transactionDetailsArrayList.add(transactionDetails);
                                    }
                                    Double priceindouble = Double.valueOf(payoutprice);
                                    payout.setPrice(priceindouble);
                                    long payoutcreateddouble = Long.parseLong(payoutcreated);

                                    payout.setPrice(priceindouble);
                                    payout.setCreated(payoutcreateddouble);
                                    payout.setId(id);
                                    payout.setStatus(status);
                                    payout.setArrivalDate(arrivalDate);
                                    payout.setTransactionDetails(transactionDetailsArrayList);
                                    previous.add(payout);
                                }
                                previousAdapter = new WalletRecyclerPreviousAdapter(previous, WalletActivity.this, WalletActivity.this);
                                recyclerListingView.setLayoutManager(new LinearLayoutManager(WalletActivity.this, LinearLayoutManager.VERTICAL, false));
                                recyclerListingView.setAdapter(previousAdapter);
                                previousAdapter.notifyDataSetChanged();

                            } catch (JSONException ex) {
                                ex.fillInStackTrace();
                            } catch (Exception ex) {
                                ex.fillInStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("** ERROR **", "Error: " + error.getMessage());
                    Log.i(TESTING_TAG, "onErrorResponse " + error.getMessage());
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    return getApiHeaders();
                }

            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "First Wallet Api");
        } else {
            Constants.showPopupInternet(activity);
        }
    }

    private void CallHistoryStatement(final String url) {

        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(WalletActivity.this);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Constants.dismissProgress();
                            try {

                                response = response.replace("\\", "");
                                response = response.replace("\"[", "[");
                                response = response.replace("]\"", "]");
                                Log.i(TESTING_TAG, response);

                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray array = jsonObj.getJSONArray("data");

                                current = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    String week = array.getJSONObject(i).getString("bookingDate");
                                    String service = array.getJSONObject(i).getString("services");
                                    String price = array.getJSONObject(i).getString("price");
                                    String created = array.getJSONObject(i).getString("created");
                                    String transactionId = array.getJSONObject(i).getString("transactionId");
                                    ArrayList<walletHistoryItems> items = new ArrayList<>();
                                    walletHistoryItems historyItems = new walletHistoryItems();
                                    historyItems.setServices(service);
                                    historyItems.setPrice(price);
                                    historyItems.setBooking_id(transactionId);
                                    items.add(historyItems);
                                    current.add(new WalletHistory(week, "", items));
                                }
                                if (url == Constants.WalletHistory) {
                                    current = getSortByDate(current);
                                }
                                currentAdapter = new WalletHostoryAdapter(WalletActivity.this, current, url.equalsIgnoreCase(Constants.WalletHistory));
                                recyclerListingView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getApplicationContext()));
                                recyclerListingView.setAdapter(currentAdapter);
                                currentAdapter.notifyDataSetChanged();
                            } catch (JSONException ex) {
                                Log.i(TESTING_TAG, "JSONException " + ex);
                                ex.fillInStackTrace();
                            } catch (Exception ex) {
                                Log.i(TESTING_TAG, "Exception " + ex);
                                ex.fillInStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TESTING_TAG, "Error: " + error.getMessage());
                    Log.i(TESTING_TAG, "Error: " + error);
                    Log.i(TESTING_TAG, "Error: " + error.getCause());
                    Constants.dismissProgress();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    return getApiHeaders();
                }
            };
            Log.i(TESTING_TAG, "req " + req);
            try {
                Log.i(TESTING_TAG, "req params" + req.getEncodedUrlParams());
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
            Log.i(TESTING_TAG, "req url " + req.getUrl());
            try {
                Log.i(TESTING_TAG, "req body" + req.getBody());
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "First Wallet Api");
        } else {
            Constants.showPopupInternet(activity);
        }

    }

    private ArrayList<WalletHistory> getSortByDate(ArrayList<WalletHistory> data) {

        ArrayList<WalletHistory> newData = new ArrayList();
        for (WalletHistory obj : data) {
            WalletHistory walletHistory = obj;
            walletHistory.month = obj.week.split(",")[1].replaceAll("[0123456789 ]", "");
            newData.add(walletHistory);
        }
        return newData;
    }

    private void balanceInfo() {
        boolean con_result = ConnectivityReceiver.isConnected();
        if (con_result) {
            Constants.showPorgess(WalletActivity.this);
            StringRequest req = new StringRequest(Request.Method.POST, Constants.WalletPrevious,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Constants.dismissProgress();
                            try {

                                JSONObject object = new JSONObject(response);
                                JSONObject getdataobj = object.getJSONObject("data");
                                Double amount = getdataobj.getDouble("totalAmount") / 100;
                                DecimalFormat format = new DecimalFormat("0.00");
                                if (amount < 0) {
                                    tv_wallet_amount.setText("£ 0.00");
                                } else {
                                    tv_wallet_amount.setText("£" + format.format(amount));
                                    totalAmount = "£" + format.format(amount);
                                }
                            } catch (Exception ex) {
                                ex.fillInStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.dismissProgress();
                    Constants.showPopupServer(activity);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getApiHeaders();
                }

            };
            req.setRetryPolicy(ApiClient.getInstance().getRetryPolicy());
            AppController.getInstance().addToRequestQueue(req, "First Wallet Api");
        } else {
            Constants.showPopupInternet(activity);
        }

    }

    @Override
    public void sonItemClick(final int pos, ArrayList<transactionDetails> transactionDetails) {

        childPrevious.clear();
        childPrevious.addAll(transactionDetails);

        if (recyclerListingView.getLayoutParams().height == 200) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerListingView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            });
        } else {
            if (childPrevious.size() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerListingView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                        recyclerListingView.scrollToPosition(pos);
                        previousChild.setData(childPrevious);
                    }
                });
            }
        }
    }

}
