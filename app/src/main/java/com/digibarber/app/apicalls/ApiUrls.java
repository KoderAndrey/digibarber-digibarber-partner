package com.digibarber.app.apicalls;

import static com.digibarber.app.CustomClasses.Constants.Path;

public class ApiUrls {
    public static String UpcomigBookingNotification = Path + "notifications_list";
    public static String Stripe_Dashboard = Path + "payment/dashboard-url";
    public static String GetBookingList = Path + "barber_booking_requests";
    public static String GetAvailablehours = Path + "get_available_hours";
    public static String Payouts = Path + "wallet/payouts";

}
