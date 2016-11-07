package com.scholar.dollar.android.dollarscholarbenlewis.service;

public class CollegeBasicService
//        extends IntentService
{

//    private static final String LOG_TAG = CollegeBasicService.class.getSimpleName();
//
//
//    private String mFilters = Utility.joinFilters(new ArrayList<String>(Arrays.asList(CollegeService.FILTERS)));
//    private String mRequestUrl = createFinalUrl(CollegeService.BASE_URL, mFilters,
//            "fields=id,school.name", CollegeService.SORT_BY, CollegeService.PER_PAGE);
//    private DatabaseReference mDbRef;
//    //Csc - College Scorecard
//    private DatabaseReference mCscRef;
//
//    public CollegeBasicService(String name) {
//        super(name);
//    }
//
//    public CollegeBasicService(){
//        super(CollegeBasicService.class.getSimpleName());
//        mDbRef = FirebaseDatabase.getInstance().getReference();
//        mCscRef = mDbRef.child("csc-ref");
//    }
//
//    private void addCollege(int cscId, String name, String nickName){
//        CollegeBasic college = new CollegeBasic(name, nickName);
//        Map<String, Object> values = college.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/" + cscId, values);
//        mCscRef.updateChildren(childUpdates);
//    }
//
//    private void addCollegesToFb(String url) {
//        try {
//            String jsonStr = Utility.fetch(url);
//            JSONObject results = new JSONObject(jsonStr);
//            JSONArray colleges = results.getJSONArray("results");
//            int collegesLength;
//            if (colleges != null) {
//                collegesLength = colleges.length();
//            } else {
//                Log.i(LOG_TAG, "ARRAY IS NULL");
//                return;
//            }
//            for (int i = 0; i < collegesLength; i++) {
//                JSONObject college = colleges.getJSONObject(i);
//                int id = college.getInt(CollegeService.ID);
//                String name = college.getString(NAME);
//                String nickName = getNickname(name);
//                addCollege(id, name, nickName);
//            }
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getNickname(String name){
//        String lcName = name.toLowerCase();
//        int ind;
//        if (lcName.contains("university of pennsylvania")){
//            return name;
//        }
//        if ((ind = lcName.indexOf("penn state")) != -1){
//            return name.substring(ind);
//        }
//        if ((ind = lcName.indexOf("california state university")) != -1){
//            return name.substring(0, ind + 16) + name.substring(ind + 27);
//        }
//
//
//        if ((ind = lcName.indexOf("university of"))!= -1){
//            return name.substring(ind + 14);
//        }
//        ind = lcName.indexOf("university");
//        if (ind != -1){
//            String nickname;
//            try{
//                nickname = name.substring(0, ind - 1);
//                return nickname;
//            } catch (IndexOutOfBoundsException e){
//                Log.i(LOG_TAG, "INDEX OUT OF BOUNDS");
//                return name.substring(0, ind);
//            }
//        }
//        return name;
//    }
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        for (int i = 0; i <9 ; i++){
//            mRequestUrl += "&page=" + i;
//            addCollegesToFb(mRequestUrl);
//        }
//    }
}
