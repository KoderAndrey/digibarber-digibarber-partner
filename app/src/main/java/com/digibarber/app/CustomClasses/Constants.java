package com.digibarber.app.CustomClasses;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digibarber.app.R;
import com.digibarber.app.activities.AddGalleryImagesActivity;
import com.digibarber.app.loader.BarberLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.digibarber.app.CustomClasses.BaseActivity.TESTING_TAG;

/**
 * Created by DIGIBARBER LTD on 7/11/2016.
 */
public class Constants {


    public static String wheelHours[] = new String[]{"12", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};
    public static String wheelMintues[] = new String[]{"00", "15", "30", "45"};
    public static String wheelAm_PM[] = new String[]{"AM", "PM"};

    public static String clientID = "bedigibarber";

    public final static String CARD_NUMBER_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]" +
            "{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
    public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.hrupin.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";


    public static final IntentFilter INTENT_FILTER = createIntentFilter();


    private static ProgressDialog pDialog;
    private static BarberLoader barberLoader;
    private static Activity mActivity;

    public final static String SHARED_PREFRENCE_DB_NAME = "Digibarber";

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public final static String APP_PLATFORM = "android"; //Platform

    public final static String USER_TYPE = "barber"; //user type

    public final static String KEY_IS_FIRST_TIME = "key_first_time"; //user type
    public final static String KEY_BALANCE = "key_balance";
    public final static String KEY_TOKEN = "token_value";

    public final static String KEY_FULL_NAME = "full_name";
    public final static String KEY_FIRST_NAME = "first_name";
    public final static String KEY_LAST_NAME = "last_name";

    public final static String KEY_MANGOPAY_ID = "mangopayId";
    public final static String KEY_USER_ID = "user_id";
    public final static String KEY_WORKPLACE = "key_work_place";
    public final static String KEY_ADDRESS = "key_address";
    public final static String KEY_POSTCODE = "key_postcode";
    public final static String KEY_LAT = "key_lat";
    public final static String KEY_LOG = "key_log";

    public final static String KEY_DEVICE_ID = "key_device_id";

    public final static String KEY_EMAIL = "email";
    public final static String ISMOBILEVERIFIED = "mobileverified";
    public final static String KEY_PHONE = "phone";
    public final static String KEY_BANK_DETAIL = "bank_detail";
    public final static String KEY_ACCOUNT_NUMBER = "key_acc_no";


    public final static String KEY_PROFILE_IMAGE = "profile_image";
    public final static String COUNTRY_NAME = "country_name";
    public final static String KEY_SERVICES = "services";
    public final static String KEY_OPEN_HOURS = "open_hours";
    public final static String STRIPE_CONNECTED = "stripe_connected";


    public final static String KEY_GALLERY_IMAGES = "gallery";

    public final static String KEY_NOTIFICATION_STATUS = "status_notification";
    public final static String KEY_PROMO_DISCCOUNT = "key_promo_discount";

    public final static String KEY_PROOF_OF_IDENTY = "Proof_of_Identy";
    public final static String KEY_PROOF_OF_ADDRESS = "proof_of_address";
    public final static String KEY_PROOF_OF_REGISTRATION = "proof_of_registration";


    public final static String KEY_FILTER_HAIRCUT = "key_prefrence_hair_cut";
    public final static String KEY_FILTER_BREAD = "key_prefrence_beard";
    public final static String KEY_FILTER_PAMEPER = "key_prefrence_pamper";
    public final static String KEY_FILTER_MISC = "key_prefrence_misc";
    public final static String KEY_FILTER_SERVICE_ALL = "key_prefrence_service_all";

    public final static String KEY_FILTER_AVAILBILTY_TIME = "key_prefrence_availablity_time";

    public final static String KEY_FILTER_MAX_PRICE = "key_prefrence_max_price";
    public final static String KEY_FILTER_MIN_PRICE = "key_prefrence_min_price";


    public final static String KEY_FILTER_MAX_DIS = "key_prefrence_max_dis";
    public final static String KEY_FILTER_MIN_DIS = "key_prefrence_min_dis";

    public final static String KEY_FILTER_EURO = "key_prefrence_euro";
    public final static String KEY_FILTER_AFRO = "key_prefrence_afro";

    public final static String KEY_IS_SIGNUP_MISSED = "key_signup_missed";

    public final static String KEY_IS_SIGNUP_ON = "key_signup_on";

    public final static String KEY_IS_MINI_GUIDE_SHOWN = "key_is_mini_guide_shown";

    public final static String KEY_IS_SIGNUP_ON_F_NAME = "key_signup_on_first_name";
    public final static String KEY_IS_SIGNUP_ON_L_NAME = "key_signup_on_last_name";
    public final static String KEY_IS_SIGNUP_ON_EMAIl = "key_signup_on_email";
    public final static String KEY_IS_SIGNUP_ON_PASSWORD = "key_signup_on_password";
    public final static String KEY_IS_SIGNUP_ON_PHONE = "key_signup_on_phone";

    public final static String KEY_IS_SIGNUP_ON_LAT = "key_signup_on_lat";
    public final static String KEY_IS_SIGNUP_ON_LOG = "key_signup_on_log";
    public final static String KEY_IS_SIGNUP_ON_WORK_PLACE = "key_signup_on_workplace";
    public final static String KEY_IS_SIGNUP_ON_ADDRESS = "key_signup_on_address";
    public final static String KEY_IS_SIGNUP_ON_POSTCODE = "key_signup_on_postocde";


    public final static String KEY_IS_SIGNUP_ON_S_HAIR = "key_signup_on_lat";
    public final static String KEY_IS_SIGNUP_ON_S_BEARD = "key_signup_on_log";
    public final static String KEY_IS_SIGNUP_ON_S_PAMPER = "key_signup_on_workplace";
    public final static String KEY_IS_SIGNUP_ON_S_MISCELL = "key_signup_on_address";
    public final static String KEY_IS_SIGNUP_ON_S_MOBILE = "key_signup_on_mobile";


    public final static String HELP_LINK = "https://www.digibarber.com/help-app-page-barber";
    public final static String TERM_CONDITION_LINK = "https://www.digibarber.com/termsandconditions";
    public final static String PRIVACY_POLICY_LINK = "https://www.digibarber.com/privacypolicy";

    public final static String DIALOG_MESSAGE = "In order to update account details please email barber@digibarber.com";


    public final static String ERROR_MESSAGE = "There has been a problem verifying these details. Please try again. if this problem continues contact us at barber@digibarber.com";


    //    public static String Path = "http://35.189.97.194:8080/DigiBarbarRestAPI/";
//    public static String Path = "http://35.189.104.193:8080/DigiBarbarRestAPI/";
//    public static String Path = "https://rest-api-dot-digibarber-instance.appspot.com/";
    // public static String Path = "https://rest-api-dot-eng-artifact-203122.appspot.com/";
    //  public static String Path = "https://rest-api-dot-numeric-habitat-247109.appspot.com/";//sandbox url by sir
    //public static String Path = "https://rest-api-dot-ferrous-kayak-209717.appspot.com/"; //sandbox url
    //public static String Path = "https://rest-api-dot-digibarber-production-golive.appspot.com/"; //production

    //  public static String Path = "https://rest-api-dot-quixotic-galaxy-277322.ue.r.appspot.com/"; //new sandbox url production
    public static String Path = "https://rest-api-dot-stunning-crane-283007.ew.r.appspot.com/"; //new sandbox url production

    //  public static String Path = "https://rest-api-dot-digibarber-production-golive.appspot.com/"; //new sandbox url production


    public static String UpdateDeviceToken = Path + "update_device";

    public static String Logout = Path + "logout";

    public static final String STRIPE_KEY = "pk_test_KyJWLQ1BtFSTc3GfKCkHJ7Ia00al6GJM42";
    //http://35.189.73.89/digibarber/social_login?device_id=fgsfdah&&device_type=android&&user_type=client&&social_id=654365364785&&uniqueId=jhfjkhdsfps97

    public static String Social_Login = Path + "social_login";


    public static String Login = Path + "login"; // login & login with social

    //public static String barberList = Path + "barbers_list"; //main home screen getting all barbers list

    public static String saveDeviceToken = Path + "update_device?";//device_id=jhdfjhsdkfh

    public static String Register = Path + "sign_up?"; //Sign up
    public static String Profile = Path + "getprofile.do?";
    public static String BarberReviewsListing = Path + "get_reviews?"; // Barber Reviews listing

    public static String MakeFavorites = Path + "make_follower?"; // Add to favorites/unfavorites


    public static String FiltersListing = Path + "all_services_list";

    public static String ClearNotfication = Path + "read_unread_notification";

    public static String ChangePrivateInfo = Path + "changePrivateInfo";

    public static String PasswordChange = Path + "change_password?";

    public static String RESETPASSWORD = Path + "reset_password.do?";

    public static String UpcomingBooking = Path + "customer_booking_list";

    public static String CancelUpcomingBooking = Path + "delete_request_api";

    public static String ForgotPasswordApi = Path + "forgot_password";

    public static String PreviousBooking = Path + "previous_booking";

    public static String GetNextAvailableBarber = Path + "next_available_barber";

    public static String Add_Gallery_Images = Path + "add_gallery_images.do";

    public static String Edit_Gallery_Images = Path + "change_gallery_images.do";

    public static String CheckShopAddress = Path + "get_shop_name.do?";

    public static String AddBarberProfile = Path + "add_barber_profile";

    public static String BookingApproveApi = Path + "add_confirm_hours";

    public static String AddBarberService = Path + "add_barberservice";

    public static String GetBarberProfile = Path + "get_barber_profile";

    public static String WalletCurrentList = Path + "wallet/current";
    public static String WalletPrevious = Path + "wallet/balance";
    public static String WalletHistory = Path + "wallet/history";


    public static String StartCloseBookingApi = Path + "update_user_status";

    public static String RescheduleBooking = Path + "reschedule_booking_api?";

    public static String Get_Booking_List_Daywise = Path + "booking_list_daywise";

    public static String Get_BookingList_MonthWise = Path + "month_wise_booking";


    public static String make_day_on_off = Path + "make_day_on_off";

    public static String BookingConfirmApi = Path + "get_confirmed_time";

    public static String WalletWithDrawList = Path + "withdraw_list";

    //public static String WalletWithDrawBalance = Path + "withdraw_balance";

    public static String WalletHistoryOld = Path + "wallet_history";

    public static String SaveBarberOpenHoursDayWise = Path + "savebarber_openhoursdaywise";

    public static String User_Exist_Validation = Path + "user_exist_validate";

    public static String BARBER_REQUESTS_LIST = "barber_booking_requests";

    public static String BOOKING_LIST_DAYWISE = "booking_list_daywise";

    public static String CONFIRM_BOOKING = "confirm_all_bookings";

    public static String EmailAvailability = Path + "users/email_availability"; // email availability

    public static String AccountVerify = Path + "users/accountverify"; //during signup email verification

    public static String ResendCode = Path + "users/account_verification_code"; //resend for signup email verification

    public static String ForgotPassword = Path + "users/forgetPassword"; // Forgot passoword

    public static String ContactEmail = Path + "contact/sendUserEmail"; // contact on the backend for any help

    public static String ResetPassword = Path + "users/resetPassword";  //Change/reset password

    public static String SubCategories = Path + "categories/getCategoryServices"; // Listing of options under one service

    public static String ProfileUpdate = Path + "users/profileUpdate"; //Update Profile of user

    public static String RateCard = Path + "service/getRateCard"; //Rate Card for all services

    public static String Questionaire = Path + "service/getQuestionnaire"; //Questionaire while hire a vendor

    public static String VendorsList = Path + "service/searchServicePartners"; // vendor listing based on location

    public static String CreateOrder = Path + "users/createOrder/"; // create order to click while hiring vendor

    public static String OrderSummary = Path + "users/orderListing";  // order liating that user have in proflie

    public static String ViewOrderDetails = Path + "users/orderDetails"; //view particular order in details

    public static String AvailableServices = "http://milagro.in/wip/handyman-app/php/api/servicepartner/availableservice_Listings"; // service for search

    public static String FaqHtmlPage = "http://milagro.in/wip/handyman-app/responsive/faq.html"; // HTML FAQ

    public static String PrivacyHtmlPage = "http://milagro.in/wip/handyman-app/responsive/privacy.html"; // HTML Privacy

    public static String TermsHtmlPage = "http://milagro.in/wip/handyman-app/responsive/terms.html"; // HTML Terms & Condition

    public static String AboutHtmlPage = "http://milagro.in/wip/handyman-app/responsive/about.html"; // HTML About

    public static String OffersListing = Path + "offer/getAllOffers"; // getting all offers listing

    public static String PaymentDetails = Path + "users/orderDetails"; // getting payment information/invoice after completion of order

    public static String UpdatePayment = Path + "users/updatePayment_status"; // Payment status updation

    public static String Rating = Path + "users/add_rating"; // Rating for service Partner

    public static String CheckRating = Path + "users/check_rating";

    public static String NotificationListing = Path + "service/getNotificationList";

    public static String GetBarberService = Path + "getBarber_Services";

    public static String GetPremuiumHours = Path + "check_premium_hours";

    public static String BookBarber = Path + "book_barber";

    public static String GetPaymentToken = "https://homologation-webpayment.payline.com/webpayment/getToken";

    public static String PostCodeValidationApi = "https://api.getaddress.io/v2/uk/";

    public static String UpdateDefaultCard = Path + "update_data";


    static Context context;

    public Constants(Context context) {
        this.context = context;
    }

    public static String GetPromoCode = Path + "get_promocodes";

    public static String addPromoCode = Path + "add_promo_code";


    //public static String DeactivateCard = MangoPaySandBoxPath + "cards/";

    private static IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION);
        return filter;
    }


    public static void showNotificationAlert(Context context, String title, String message, String button_text)  //Alert with custom title, message and button text
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));

        alert.setTitle(title);
        alert.setMessage(Html.fromHtml("<font color='#5c5c5c'>" + message + "</font>"));
        alert.setNeutralButton(Html.fromHtml("<font color='#5c5c5c'>" + button_text + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alert.show();
    }

    public static void CustomToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 50);
        toast.show();
    }

    public static void showToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showPorgess(Activity activity) {
        mActivity = activity;
//
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                barberLoader = new BarberLoader(mActivity);
                barberLoader.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                if (barberLoader != null)
                    if (!barberLoader.isShowing()) {
                        Log.i(TESTING_TAG, "showPorgess");
                        barberLoader.setCancelable(false);
                        barberLoader.show();

                    }
            }

        } catch (Exception e) {
            Log.e("Exception ", "exception" + e);
            /*
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(activity.getResources().getString(R.string.loading));
        pDialog.setCancelable(false);
        pDialog.show();*/
        }
    }

    public static void dismissProgress() {
        if (pDialog != null) {
            if (mActivity != null && !mActivity.isFinishing())
                if (pDialog.isShowing()) {
                    Log.i(TESTING_TAG, "dismissProgress");
                    pDialog.dismiss();
                }
        }
        if (barberLoader != null) {
            if (mActivity != null && !mActivity.isFinishing()) {
                if (barberLoader.isShowing()) {
                    Log.i(TESTING_TAG, "dismissProgress");
                    barberLoader.dismiss();
                }
            } else {
            }
        }
    }

    static public void showPopupWithMsg(Activity activty, String msg, String strTitle) {
        if (activty != null) {
            if (!activty.isFinishing()) {
                final Dialog dialog_first = new Dialog(activty);
                // setting custom layout to dialog
                dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_first.setContentView(R.layout.popup_time_not_avaiable);
                dialog_first.setCancelable(false);

                dialog_first.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                TextView alertTitle = (TextView) dialog_first.findViewById(R.id.alertTitle);
                TextView tv_addrress1 = (TextView) dialog_first.findViewById(R.id.tv_addrress1);
                alertTitle.setText(strTitle);
                tv_addrress1.setText(msg);
                TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
                tv_ok.setText("Ok");
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_first.dismiss();

                    }
                });
                dialog_first.show();
            }
        }
    }

    static public void showGalleryPopUp(final AddGalleryImagesActivity activty) {
        if (activty != null) {
            if (!activty.isFinishing()) {
                final Dialog dialog_first = new Dialog(activty);
                // setting custom layout to dialog
                dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_first.setContentView(R.layout.popup_gallery_images);
                dialog_first.setCancelable(false);

                dialog_first.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                TextView tv_now = (TextView) dialog_first.findViewById(R.id.tv_now);
                tv_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_first.dismiss();
                    }
                });
                TextView tv_later = (TextView) dialog_first.findViewById(R.id.tv_later);
                tv_later.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_first.dismiss();
                        activty.navigateToServices();
                    }
                });
                dialog_first.show();
            }
        }
    }


    static public void showPopupInternet(Activity activty) {
        if (activty != null && !activty.isFinishing()) {
            final Dialog dialog_first = new Dialog(activty);
            dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_first.setContentView(R.layout.popup_internet);
            dialog_first.setCancelable(false);
            dialog_first.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_first.dismiss();
                }
            });

            dialog_first.show();
        }
    }

    static public void showPopupWithTitle(Activity activty, String titlestr, String msgstr) {
        if (activty != null && !activty.isFinishing()) {
            final Dialog dialog_first = new Dialog(activty);
            dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_first.setContentView(R.layout.popup_internet);
            dialog_first.setCancelable(false);
            dialog_first.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
            TextView tv_message = (TextView) dialog_first.findViewById(R.id.tv_message);
            TextView title = (TextView) dialog_first.findViewById(R.id.title);
            tv_message.setText(msgstr);
            title.setText(titlestr);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_first.dismiss();
                }
            });

            dialog_first.show();
        }
    }

    static public void showPopupFrozen(Activity activty, String msg) {
        if (activty != null && !activty.isFinishing()) {

            final Dialog dialog_first = new Dialog(activty);
            dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_first.setContentView(R.layout.popup_frozen);
            dialog_first.setCancelable(false);
            dialog_first.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));

            TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
            TextView tv_message = (TextView) dialog_first.findViewById(R.id.tv_message);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_first.dismiss();
                }
            });
            tv_message.setText(msg);
            dialog_first.show();
        }
    }

    public static int getWidthRatio(int hgh, Activity activity) {
        int origHeight = 0;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        Log.e("Deep", "Height:");
        origHeight = hgh;
        if (height < 1000) {
            origHeight = (int) (height / 3.05);
            return origHeight;

        }
        if (height < 1400) {
            origHeight = (int) (height / 3.15);
            return origHeight;

        } else if (height < 2000) {
            origHeight = (int) (height / 3.22);
            return origHeight;

        } else if (height < 2700) {
            origHeight = (int) (height / 3.4);
            return origHeight;

        }
        return origHeight;

    }

    public static void showPopupServer(Activity activty) {

       /* if (activty!=null) {
            if (!activty.isFinishing()) {
                final Dialog dialog_first = new Dialog(activty);
                dialog_first.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_first.setContentView(R.layout.popup_server);
                dialog_first.setCancelable(false);
                dialog_first.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));

                TextView tv_ok = (TextView) dialog_first.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_first.dismiss();
                    }
                });

                dialog_first.show();
            }
        }*/
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay1(cal1, cal2);
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay1(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static Date getDateFromString(String time) {
        String inputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Date getDateFromString(String time, String dateformatter) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(dateformatter);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static boolean checkString(String str) {
        char ch;
        boolean numberFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            }

            if (numberFlag)
                return true;
        }
        return false;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    public static int getPresentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day;
    }


    public static String getPresentDayName() {
        String dayIs = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:

                dayIs = "SUNDAY";
                // Current day is Sunday
                break;
            case Calendar.MONDAY:
                dayIs = "MONDAY";
                // Current day is Monday
                break;
            case Calendar.TUESDAY:
                dayIs = "TUESDAY";

                break;
            case Calendar.WEDNESDAY:
                dayIs = "WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                dayIs = "THURSDAY";
                break;
            case Calendar.FRIDAY:
                dayIs = "FRIDAY";

                break;
            case Calendar.SATURDAY:
                dayIs = "SATURDAY";

                break;
            // etc.
        }

        return dayIs;
    }


    public static String getCurrentMonthName() {
        Calendar cal = Calendar.getInstance();
        String month = (String) DateFormat.format("MMMM", cal.getTime());
        return month;
    }

    public static String getCurrentMonthNameMMM() {
        Calendar cal = Calendar.getInstance();
        String month = (String) DateFormat.format("MMM", cal.getTime());
        return month;
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        String month = (String) DateFormat.format("dd", cal.getTime());
        return month;
    }

    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        String year = (String) DateFormat.format("yyyy", cal.getTime());
        return year;
    }

    public static String getCurrentDate_DD_MMM_YYY() {
        Calendar cal = Calendar.getInstance();
        String date = (String) DateFormat.format("dd-MMM-yyyy", cal.getTime());
        return date;
    }

    public static String getCurrentDate_yyyy_MM_dd() {
        Calendar cal = Calendar.getInstance();
        String date = (String) DateFormat.format("yyyy-MM-dd", cal.getTime());
        return date;
    }

    public static String getCalDate_yyyy_MM_dd(Calendar cal) {
        String date = (String) DateFormat.format("yyyy-MM-dd", cal.getTime());
        return date;
    }


    public static SimpleDateFormat getSimpleDateFormate_yyyy_MM_dd_kk_mm() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        return sdf;
    }

    public static SimpleDateFormat getSimpleDateFormate_yyyy_MM_dd_kk_mm_ss() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        return sdf;
    }

    public static String getCurrentTime_hh_mm(Calendar cal) {
        SimpleDateFormat slotTime = new SimpleDateFormat("kk:mm");
        String time = slotTime.format(cal.getTime());
        return time;
    }

    public static String getCurrentTime_KK_mm_24Hours(String date) {

        String time = "";
        SimpleDateFormat fomrate12 = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat fomrate24 = new SimpleDateFormat("kk:mm");
        try {
            Date d = fomrate12.parse(date);
            time = fomrate24.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }


    public static Calendar getCalenderInstance() {
        Calendar cal = Calendar.getInstance();
        return cal;
    }


    public static String addAmPMToTime(String Time) {
        String time = "";
        if (Time != null) {
            String[] startTime = Time.split(":");
            int intstartTime = Integer.parseInt(startTime[0].toString().trim());
            if (intstartTime >= 12) {
                time = Time.trim() + " PM";
            } else {
                time = Time.trim() + " AM";
            }
        } else {

        }
        return Time;
    }

    public static boolean isValidPasswordMinTwoNumber(String password) // To check Password pattern
    {
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN = "((?=.*\\d{2,}).{6,20})";
        CharSequence inputStr = password;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean isValidPasswordMinSixCharacter(String password) // To check Password pattern
    {
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN = "(.{6,20})";
        CharSequence inputStr = password;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }


    public static boolean namevalidation(String name) // To check Name Validation
    {
        Pattern pattern;
        Matcher matcher;
        String namepattern = "[a-zA-Z ]+";
        pattern = Pattern.compile(namepattern);
        matcher = pattern.matcher(name);
        return matcher.matches();

    }

    /* To match entered password */
    public static boolean checkPassWordAndConfirmPassword(String password, String confirmPassword) {
        boolean pstatus = false;
        if (confirmPassword != null && password != null) {
            if (password.equals(confirmPassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }

    public static boolean addressvalidation(String value) // To check Address Validation
    {
        Pattern pattern;
        Matcher matcher;
        String namepattern = "[a-zA-Z0-9#/, ]+";
        pattern = Pattern.compile(namepattern);
        matcher = pattern.matcher(value);
        return matcher.matches();

    }

    public static boolean pincodevalidation(String value) // To check Pincode Validation
    {
        Pattern pattern;
        Matcher matcher;
        String namepattern = "[A-Z0-9]+";
        pattern = Pattern.compile(namepattern);
        matcher = pattern.matcher(value);
        return matcher.matches();

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isEmailValid(String email) // To check Email Validation
    {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static ColorStateList getColourStateRed(Activity ac) {

        ColorStateList colorstate = ColorStateList.valueOf(ac.getResources().getColor(R.color.red_light));

        return colorstate;
    }

    public static ColorStateList getColourStateGrey(Activity ac) {

        ColorStateList colorstate = ColorStateList.valueOf(ac.getResources().getColor(R.color.colorGrey));

        return colorstate;
    }


    public static Animation getLeftToRightAnimation(Activity activity) {

        Animation leftToRight = AnimationUtils.loadAnimation(activity, R.anim.slide_left_to_right);
        return leftToRight;
    }

    public static Animation getShake(Activity activity) {

        Animation shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
        return shake;
    }


    public static boolean Empty(Context context, String value, String msg, EditText edittext) // To Check Empty Validation
    {
        if (value.isEmpty()) {
            ErrorMessages(context, msg, edittext);
            return true;
        } else
            return false;
    }

    public static boolean EmptyText(Context context, String value, String msg, EditText edittext) // To Check Empty Validation
    {
        if (value.isEmpty()) {
            // ErrorMessages(context, msg, edittext);
            return true;
        } else
            return false;
    }


    public static void ErrorMessages(Context context, String message, EditText edittext) // To show error messages
    {
        String estring = message;
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(context.getResources().getColor(R.color.colorWhite));
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
        edittext.setError(ssbuilder);
        edittext.requestFocus();
    }

 /*   public static void app_launched()  // To launch rating the App alert
    {
        SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }


        showRateDialog(context, editor);

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context, editor);
            }
        }

        editor.commit();
    }*/

  /*  public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) //Display alert dialog for rating on click of Rate The App Tab
    {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light));
        dialog.setTitle("Rate " + context.getResources().getString(R.string.app_name));

        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        MyTextView tv = new MyTextView(mContext);
        tv.setText("If you enjoy using " + context.getResources().getString(R.string.app_name) + ", please take a moment to rate it. Thanks for your support!");
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextColor(context.getResources().getColor(R.color.textColor));
        tv.setTextSize(18);
        tv.setPadding(20, 20, 20, 20);
        ll.addView(tv);

        MyButton b1 = new MyButton(mContext);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param1.gravity = Gravity.CENTER;
        param1.topMargin = 30;
        param1.leftMargin = 30;
        param1.rightMargin = 30;
        b1.setAllCaps(false);
        b1.setTextSize(20);
        b1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_btn_blue));
        b1.setLayoutParams(param1);
        b1.setTextColor(context.getResources().getColor(R.color.colorWhite));
        b1.setPadding(20,10,20,10);
        b1.setText("Rate " + context.getResources().getString(R.string.app_name));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        MyButton b2 = new MyButton(mContext);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param2.gravity = Gravity.CENTER;
        param2.topMargin = 30;
        param2.leftMargin = 30;
        param2.rightMargin = 30;
        b2.setLayoutParams(param2);
        b2.setTextSize(20);
        b2.setAllCaps(false);
        b2.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_btn_blue));
        b2.setTextColor(context.getResources().getColor(R.color.colorWhite));
        b2.setText(context.getResources().getString(R.string.remind));
        b2.setPadding(20,10,20,10);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        MyButton b3 = new MyButton(mContext);
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        param3.gravity = Gravity.CENTER;
        param3.topMargin = 30;
        param3.leftMargin = 30;
        param3.rightMargin = 30;
        param3.bottomMargin = 30;
        b3.setLayoutParams(param3);
        b3.setTextSize(20);
        b3.setAllCaps(false);
        b3.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.custom_btn_blue));
        b3.setTextColor(context.getResources().getColor(R.color.colorWhite));
        b3.setPadding(20,10,20,20);
        b3.setGravity(Gravity.CENTER);
        b3.setText(context.getResources().getString(R.string.no_thanks));
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        dialog.show();
    }*/

    public static void showCustomAlert(Context context, String title, String message, String button_text1)  //Alert with custom title, message and button text
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light));

        alert.setTitle(title);
        alert.setMessage(Html.fromHtml("<font color='#5c5c5c'>" + message + "</font>"));

        alert.setPositiveButton(Html.fromHtml("<font color='#5c5c5c'>" + button_text1 + "</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alert.show();


    }

    public static boolean isAppInForground(Context context, String packageName) {
        String[] activePackages;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activePackages = getActivePackages(am);

        if (activePackages != null) {

            if (activePackages.length > 0) {
                return activePackages[0].equals(packageName);
            }
        }
        return false;
    }


    public static String[] getActivePackages(ActivityManager mActivityManager) {
        final Set<String> activePackages = new HashSet<>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }

    public static String FormatConversion(double ServicePrice) {
        DecimalFormat form = new DecimalFormat("0.00");
        String price = form.format(ServicePrice);
        return price;
    }

    //Password encryption
    public static String md5(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // conversion from bitmap to string
    public static String convertBitmapToString(Bitmap bmp) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        } catch (NullPointerException e) {
            System.out.println(" CATCH 1" + e.getMessage());
            return null;
        } catch (OutOfMemoryError e) {
            System.out.println(" CATCH 2" + e.getMessage());
            return null;
        }
    }

    public static void showKeyboard(Activity activity, EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, 0);
    }

    public static String getPath(Uri uri, Activity ac) {
        Uri selectedImage = uri;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = ac.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static long getDateDiffernce(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;
        long finalMintues = 0;
        long elapsedSeconds = different / secondsInMilli;

        if (elapsedHours > 0) {
            finalMintues = elapsedHours * 60;
            finalMintues += elapsedMinutes;
        } else {
            finalMintues = elapsedMinutes;
        }
        return finalMintues;
    }


    // conversion from string to bitmap
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static File downloadFileFromServer(String url) {
        try {

            // create output directory if it doesn't exist
            File dir = new File(Environment.getExternalStorageDirectory() + "/DigiProfileImages");
            File file = null;

            boolean success = true;
            if (!dir.exists()) {
                success = dir.mkdirs();
            }
            if (success) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new java.util.Date());
                file = new File(dir.getAbsolutePath() + File.separator + timeStamp + ".jpg");
                file.createNewFile();
            }

            URL webURL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) webURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
//			httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            InputStream in = httpURLConnection.getInputStream();
            OutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}