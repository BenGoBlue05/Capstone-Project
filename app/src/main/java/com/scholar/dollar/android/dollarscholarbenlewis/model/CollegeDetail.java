package com.scholar.dollar.android.dollarscholarbenlewis.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bplewis5 on 10/6/16.
 */


//@IgnoreExtraProperties
//public class User {
//
//    public String name;
//    public String email;
//    public String photoUrl;
//    public int favoritesCount = 0;
//    public Map<String, Boolean> favorites = new HashMap<>();
//
//    public User() {
//    }
//
//    public User(String name, String email, String photoUrl) {
//        this.name = name;
//        this.email = email;
//        this.photoUrl = photoUrl;
//    }
//
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("name", name);
//        result.put("email", email);
//        result.put("photoUrl", photoUrl);
//        result.put("favorites_count", favoritesCount);
//        result.put("favorites", favorites);
//
//        return result;
//    }
//
//}
@IgnoreExtraProperties
public class CollegeDetail {
    int collegeId;
    int medianIncome25percentile;
    int medianIncome75percentile;
    double satMedian;
    double actMedian;
    double act25percentile;
    double act75percentile;
    int schoolSize;
    int familyIncome;
    double gradRate4years;
    double principal;
    double debtCompleters;
    double monthlyPayment;
    double pctLoanStudents;
    double pctPellStudents;
    String priceCalculatorUrl;
    String universityWebsite;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public CollegeDetail() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    public CollegeDetail(int collegeId, int medianIncome25percentile, int medianIncome75percentile, double satMedian,
                         double actMedian, double act25percentile, double act75percentile, int schoolSize,
                         int familyIncome, double gradRate4years, double principal,
                         double debtCompleters, double monthlyPayment, double pctLoanStudents, double pctPellStudents,
                         String priceCalculatorUrl, String universityWebsite) {
        this.collegeId = collegeId;
        this.medianIncome25percentile = medianIncome25percentile;
        this.medianIncome75percentile = medianIncome75percentile;
        this.satMedian = satMedian;
        this.actMedian = actMedian;
        this.act25percentile = act25percentile;
        this.act75percentile = act75percentile;
        this.schoolSize = schoolSize;
        this.familyIncome = familyIncome;
        this.gradRate4years = gradRate4years;
        this.principal = principal;
        this.debtCompleters = debtCompleters;
        this.monthlyPayment = monthlyPayment;
        this.pctLoanStudents = pctLoanStudents;
        this.pctPellStudents = pctPellStudents;
        this.priceCalculatorUrl = priceCalculatorUrl;
        this.universityWebsite = universityWebsite;
    }

    public int getCollegeId(){return collegeId;}

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("median_income_25_percentile", medianIncome25percentile);
        result.put("median_income_75_percentile", medianIncome75percentile);
        result.put("sat_median", satMedian);
        result.put("act_median", actMedian);
        result.put("act_25_percentile", act25percentile);
        result.put("act_75_percentile", act75percentile);
        result.put("undergrad_students", schoolSize);
        result.put("median_family_income", familyIncome);
        result.put("grad_rate_4_years", gradRate4years);
        result.put("median_loan_principal_entering_repayment", principal);
        result.put("median_debt_completers", debtCompleters);
        result.put("median_debt_monthly_payments_10_year_amortization", monthlyPayment);
        result.put("pct_students_with_any_loan", pctLoanStudents);
        result.put("pct_undergrads_received_pell_grant", pctPellStudents);
        result.put("net_price_calculator_url", priceCalculatorUrl);
        result.put("school_homepage_url", universityWebsite);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }

}
